/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.github.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

/**
 * @author Brian Wing Shun Chan
 * @author Peter Shin
 */
public class DevOpsGitHubRequestProcessor extends BaseGitHubRequestProcessor {

	public DevOpsGitHubRequestProcessor() throws Exception {
		initProfiles();

		initScheduledExecutorServices();
	}

	public void buildProfileBundle(String profileName) throws Exception {
		File workDir = new File(
			DevOpsConstants.PEEK_GIT_REPOSITORY_DIR_NAME + "/" + profileName);

		File profileLiferayDir = getProfileLiferayDir(profileName);

		DevOpsProcessUtil.execute(
			workDir,
			"rsync -az --delete " +
				DevOpsPropsUtil.get("peek.liferay.bundle.dir") + "/ " +
					profileLiferayDir.getPath());

		File fixPacksFile = new File(
			DevOpsConstants.PEEK_GIT_REPOSITORY_DIR_NAME + "/" + profileName +
				"/artifacts/liferay/fix-packs.txt");

		if (!fixPacksFile.exists()) {
			return;
		}

		File profileAppServerDir = getProfileAppServerDir(profileName);

		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new FileReader(fixPacksFile));

			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();

				if (line.length() == 0) {
					continue;
				}

				StringBuilder sb = new StringBuilder();

				sb.append("wget -nv http://mirrors.lax.liferay.com");
				sb.append("/files.liferay.com/private/ee/fix-packs/");

				String fixPacksVersion = DevOpsPropsUtil.get(
					"profile." + profileName + ".fix.packs.version");

				sb.append(fixPacksVersion);
				sb.append("/");
				sb.append(line);
				sb.append("-");
				sb.append(fixPacksVersion.replaceAll(".", ""));
				sb.append(".zip -P ");
				sb.append(profileAppServerDir.getPath());
				sb.append("/patching-tool/patches");

				DevOpsProcessUtil.execute(profileAppServerDir, sb.toString());
			}
		}
		finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}

		DevOpsProcessUtil.execute(
			workDir,
			profileAppServerDir.getPath() +
				"/patching-tool/patching-tool.sh install");
	}

	@Override
	public void destroy() {
		for (ScheduledExecutorService scheduledExecutorService :
				_scheduledExecutorServices) {

			scheduledExecutorService.shutdown();
		}
	}

	public File getProfileGitRepositoryDir(String profileName) {
		return new File(
			DevOpsConstants.DEV_OPS_DIR_NAME + "/" + profileName + "/plugins");
	}

	@Override
	public void process(JSONObject payloadJSONObject) throws Exception {
		if (!DevOpsUtil.isValidAction(payloadJSONObject)) {
			return;
		}

		String profileName = DevOpsUtil.getProfileName(payloadJSONObject);

		if (profileName.isEmpty()) {
			return;
		}

		if (DevOpsUtil.isValidLogin(payloadJSONObject, profileName)) {
			DevOpsPatchRequestProcessor devOpsPatchRequestProcessor =
				_devOpsPatchRequestProcessors.get(profileName);

			devOpsPatchRequestProcessor.addPayloadJSONObject(payloadJSONObject);
		}
		else {
			String gitHubUserLogins = DevOpsPropsUtil.get(
				"profile." + profileName + ".github.user.logins");

			String comment = MessageFormat.format(
				DevOpsPropsUtil.get("github.comment.code.review"),
				gitHubUserLogins.replaceAll(",", " or "));

			DevOpsUtil.postPullRequestComment(payloadJSONObject, comment);
		}
	}

	public synchronized void updatePeekGitRepository(String profileName)
		throws Exception {

		File workDir = new File(DevOpsConstants.PEEK_GIT_REPOSITORY_DIR_NAME);

		DevOpsProcessUtil.execute(workDir, "git clean -d -f -q -x");
		DevOpsProcessUtil.execute(workDir, "git reset --hard HEAD");
		DevOpsProcessUtil.execute(workDir, "git checkout master");
		DevOpsProcessUtil.execute(workDir, "git fetch -f origin");
		DevOpsProcessUtil.execute(workDir, "git pull -f origin master");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

		dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

		String commitMessage =
			profileName + " " + dateFormat.format(new Date());

		FileUtils.writeStringToFile(
			new File(
				DevOpsConstants.PEEK_GIT_REPOSITORY_DIR_NAME + "/" +
					profileName + "/portal/redeploy.marker"),
			commitMessage, "UTF-8", false);

		DevOpsProcessUtil.execute(
			workDir, "git add " + profileName + "/portal/redeploy.marker");
		DevOpsProcessUtil.execute(
			workDir, "git commit -m \"" + commitMessage + "\"");
		DevOpsProcessUtil.execute(workDir, "git push origin");
	}

	public void updateProfileGitRepository(String profileName)
		throws Exception {

		File workDir = getProfileGitRepositoryDir(profileName);

		DevOpsProcessUtil.execute(
			workDir,
			"git clean -d -e .ivy -e build." + System.getProperty("user.name") +
				".properties -f -q -x");
		DevOpsProcessUtil.execute(workDir, "git reset --hard HEAD");
		DevOpsProcessUtil.execute(workDir, "git fetch devops");
		DevOpsProcessUtil.execute(workDir, "git fetch upstream");

		DevOpsProcessUtil.Result result = DevOpsProcessUtil.execute(
			workDir, "git branch --list devops-" + profileName);

		String output = result.getOutput();

		String baseRef = DevOpsPropsUtil.get(
			"profile." + profileName + ".base.ref");

		if (output.isEmpty()) {
			DevOpsProcessUtil.execute(
				workDir,
				"git checkout -b devops-" + profileName + " upstream/" +
					baseRef);
		}
		else {
			DevOpsProcessUtil.execute(
				workDir, "git rebase upstream/" + baseRef);
		}

		DevOpsProcessUtil.execute(
			workDir, "git push devops devops-" + profileName);
	}

	protected File getProfileAppServerDir(String profileName) {
		File profileLiferayDir = getProfileLiferayDir(profileName);

		return new File(
			profileLiferayDir.getPath() + "/" +
				DevOpsPropsUtil.get("peek.liferay.tomcat.folder"));
	}

	protected String getProfileGitHubUserLogin() {
		return DevOpsPropsUtil.get("profile.github.user.login");
	}

	protected File getProfileLiferayDir(String profileName) {
		return new File(
			DevOpsConstants.DEV_OPS_DIR_NAME + "/" + profileName + "/liferay");
	}

	protected String[] getProfileNames() {
		String profileNames = DevOpsPropsUtil.get("profile.names");

		return profileNames.split(",");
	}

	protected void initProfileGitRepository(String profileName)
		throws Exception {

		File profileGitRepositoryDir = getProfileGitRepositoryDir(profileName);

		if (profileGitRepositoryDir.exists()) {
			return;
		}

		DevOpsProcessUtil.execute(
			new File(DevOpsConstants.PEEK_PLUGINS_GIT_REPOSITORY_DIR_NAME),
			"git new-workdir " +
				DevOpsConstants.PEEK_PLUGINS_GIT_REPOSITORY_DIR_NAME + " " +
					profileGitRepositoryDir.getPath());

		DevOpsProcessUtil.Result result = DevOpsProcessUtil.execute(
			profileGitRepositoryDir, "git config --get remote.devops.url");

		String output = result.getOutput();

		if (output.isEmpty()) {
			DevOpsProcessUtil.execute(
				profileGitRepositoryDir,
				"git remote add devops git@github.com:" +
					getProfileGitHubUserLogin() + "/liferay-plugins-ee.git");
		}

		File profileAppServerDir = getProfileAppServerDir(profileName);

		FileUtils.writeStringToFile(
			new File(
				profileGitRepositoryDir + "/build." +
					System.getProperty("user.name") + ".properties"),
			"app.server.dir=" + profileAppServerDir.getPath(), "UTF-8", false);
	}

	protected void initProfiles() throws Exception {
		for (String profileName : getProfileNames()) {
			initProfileGitRepository(profileName);

			buildProfileBundle(profileName);
		}
	}

	protected void initScheduledExecutorService(String profileName) {
		final DevOpsPatchRequestProcessor devOpsPatchRequestProcessor =
			new DevOpsPatchRequestProcessor(this, profileName);

		_devOpsPatchRequestProcessors.put(
			profileName, devOpsPatchRequestProcessor);

		ScheduledExecutorService scheduledExecutorService =
			Executors.newSingleThreadScheduledExecutor();

		scheduledExecutorService.scheduleAtFixedRate(
			new Runnable() {

				@Override
				public void run() {
					devOpsPatchRequestProcessor.process();
				}

			},
			0, 30, TimeUnit.SECONDS);

		_scheduledExecutorServices.add(scheduledExecutorService);
	}

	protected void initScheduledExecutorServices() {
		ScheduledExecutorService scheduledExecutorService =
			Executors.newSingleThreadScheduledExecutor();

		scheduledExecutorService.scheduleAtFixedRate(
			new Runnable() {

				@Override
				public void run() {
					for (String profileName : getProfileNames()) {
						DevOpsPatchRequestProcessor
							devOpsPatchRequestProcessor =
								_devOpsPatchRequestProcessors.get(profileName);

						if (devOpsPatchRequestProcessor.isRunning()) {
							return;
						}

						try {
							updateProfileGitRepository(profileName);
						}
						catch (Exception e) {
							_log.error(e, e);
						}
					}
				}

			}, 0, 480, TimeUnit.MINUTES);

		_scheduledExecutorServices.add(scheduledExecutorService);

		for (String profileName : getProfileNames()) {
			initScheduledExecutorService(profileName);
		}
	}

	private static Log _log = LogFactory.getLog(
		DevOpsGitHubRequestProcessor.class);

	private Map<String, DevOpsPatchRequestProcessor>
		_devOpsPatchRequestProcessors =
			new HashMap<String, DevOpsPatchRequestProcessor>();
	private List<ScheduledExecutorService> _scheduledExecutorServices =
		new ArrayList<ScheduledExecutorService>();

}