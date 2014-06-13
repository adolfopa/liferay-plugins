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

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
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
		initProfileGitRepositories();

		initScheduledExecutorServices();
	}

	@Override
	public void destroy() {
		for (ScheduledExecutorService scheduledExecutorService :
				_scheduledExecutorServices) {

			scheduledExecutorService.shutdown();
		}
	}

	public File getProfileGitRepositoryDir(String profileName) {
		return new File(_DEV_OPS_DIR_NAME + "/" + profileName + "/plugins");
	}

	@Override
	public void process(JSONObject payloadJSONObject) throws Exception {
		if (!DevOpsUtil.isValidAction(payloadJSONObject)) {
			return;
		}

		String profileName = DevOpsUtil.getProfileName(payloadJSONObject);

		if (DevOpsUtil.isValidDeveloper(payloadJSONObject, profileName)) {
			DevOpsPatchRequestProcessor devOpsPatchRequestProcessor =
				_devOpsPatchRequestProcessors.get(profileName);

			devOpsPatchRequestProcessor.addPayloadJSONObject(payloadJSONObject);
		}
	}

	public synchronized void updatePeekGitRepository(String profileName)
		throws Exception {

		File workDir = new File(_PEEK_GIT_REPOSITORY_DIR_NAME);

		DevOpsProcessUtil.execute(workDir, "git clean -d -f -q -x");
		DevOpsProcessUtil.execute(workDir, "git reset --hard HEAD");
		DevOpsProcessUtil.execute(workDir, "git co master");
		DevOpsProcessUtil.execute(workDir, "git fetch -f origin");
		DevOpsProcessUtil.execute(workDir, "git pull -f origin master");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

		dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

		String commitMessage =
			profileName + " " + dateFormat.format(new Date());

		try {
			FileUtils.writeStringToFile(
				new File(
				_PEEK_GIT_REPOSITORY_DIR_NAME + profileName +
					"/portal/redeploy.marker"),
				commitMessage, "UTF-8", false);
		}
		catch (IOException ioe) {
			_log.error(ioe, ioe);
		}

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
			"git clean -d -e .ivy -e build." + System.getenv("USERNAME") +
				".properties -f -q -x");
		DevOpsProcessUtil.execute(workDir, "git reset --hard HEAD");
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

	protected String getProfileGitHubUserLogin() {
		return null;
	}

	protected String[] getProfileNames() {
		return new String[0];
	}

	protected void initProfileGitRepositories() throws Exception {
		for (String profileName : getProfileNames()) {
			initProfileGitRepository(profileName);
		}
	}

	protected void initProfileGitRepository(String profileName)
		throws Exception {

		File profileGitRepositoryDir = getProfileGitRepositoryDir(profileName);

		if (profileGitRepositoryDir.exists()) {
			return;
		}

		DevOpsProcessUtil.execute(
			new File(_PEEK_PLUGINS_GIT_REPOSITORY_DIR_NAME),
			"git new-workdir " + _PEEK_PLUGINS_GIT_REPOSITORY_DIR_NAME + " " +
				profileGitRepositoryDir);

		DevOpsProcessUtil.Result result = DevOpsProcessUtil.execute(
			profileGitRepositoryDir, "git config --get remote.devops.url");

		String output = result.getOutput();

		if (output.isEmpty()) {
			DevOpsProcessUtil.execute(
				profileGitRepositoryDir,
				"git remote add devops git@github.com:" +
					getProfileGitHubUserLogin() + "/liferay-plugins-ee.git");
		}

		try {
			FileUtils.writeStringToFile(
				new File(
					profileGitRepositoryDir + "/build." +
						System.getenv("USERNAME") + ".properties"),
				"app.server.dir=" + _APP_SERVER_DIR_NAME, "UTF-8", false);
		}
		catch (IOException ioe) {
			_log.error(ioe, ioe);
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

			}, 1, 480, TimeUnit.MINUTES);

		_scheduledExecutorServices.add(scheduledExecutorService);

		for (String profileName : getProfileNames()) {
			initScheduledExecutorService(profileName);
		}
	}

	private static final String _APP_SERVER_DIR_NAME =
		"/opt/java/liferay/tomcat";

	private static final String _DEV_OPS_DIR_NAME = "/tmp/devops";

	private static final String _PEEK_GIT_REPOSITORY_DIR_NAME =
		"/var/peek/repo";

	private static final String _PEEK_PLUGINS_GIT_REPOSITORY_DIR_NAME =
		"/var/peek/builder-dependencies/plugins";

	private static Log _log = LogFactory.getLog(
		DevOpsGitHubRequestProcessor.class);

	private Map<String, DevOpsPatchRequestProcessor>
		_devOpsPatchRequestProcessors =
			new HashMap<String, DevOpsPatchRequestProcessor>();
	private List<ScheduledExecutorService> _scheduledExecutorServices =
		new ArrayList<ScheduledExecutorService>();

}