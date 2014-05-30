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

import java.text.MessageFormat;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

/**
 * @author Brian Wing Shun Chan
 * @author Peter Shin
 */
public class DevOpsPatchRequestProcessor {

	public DevOpsPatchRequestProcessor(
		DevOpsGitHubRequestProcessor devOpsGitHubRequestProcessor,
		String profileName) {

		_devOpsGitHubRequestProcessor = devOpsGitHubRequestProcessor;
		_profileName = profileName;
	}

	public void addPayloadJSONObject(JSONObject payloadJSONObject) {
		_payloadJSONObjects.add(payloadJSONObject);
	}

	public boolean isRunning() {
		return _running;
	}

	public void process() {
		if (isRunning()) {
			return;
		}

		_running = true;

		try {
			JSONObject payloadJSONObject = _payloadJSONObjects.poll();

			if (payloadJSONObject != null) {
				try {
					process(payloadJSONObject);
				}
				catch (Exception e) {
					_log.error(e, e);
				}
				finally {
					sendEmail(payloadJSONObject);
				}
			}
		}
		finally {
			_running = false;
		}
	}

	protected String[] getSHA1Hashes(JSONObject payloadJSONObject)
		throws Exception {

		File workDir = _devOpsGitHubRequestProcessor.getProfileGitRepositoryDir(
			_profileName);

		String configOutput = DevOpsProcessUtil.getOutput(
			workDir, "git config --get remote.devops.url");

		JSONObject pullRequestJSONObject = payloadJSONObject.getJSONObject(
			"pull_request");

		int pullRequestNumber = pullRequestJSONObject.getInt("number");

		DevOpsProcessUtil.execute(
			workDir, "git fetch " + configOutput + " refs/pull/" +
				pullRequestNumber + "/head:" + "pull-request-" +
					pullRequestNumber);

		DevOpsProcessUtil.execute(
			workDir, "git checkout pull-request-" + pullRequestNumber);

		JSONObject baseJSONObject = pullRequestJSONObject.getJSONObject("base");

		String baseRef = baseJSONObject.getString("ref");

		if (baseRef.equals("devops-" + _profileName)) {
			DevOpsProcessUtil.execute(
				workDir, "git rebase origin/devops-" + _profileName);
		}

		String sha1HashParent = DevOpsProcessUtil.getOutput(
			workDir,
			"git merge-base origin/" + baseRef + " pull-request-" +
				pullRequestNumber);
		String sha1HashHead = DevOpsProcessUtil.getOutput(
			workDir, "git rev-parse HEAD");

		return new String[] {sha1HashParent, sha1HashHead};
	}

	protected boolean hasBlacklistedFile(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		File workDir = _devOpsGitHubRequestProcessor.getProfileGitRepositoryDir(
			_profileName);

		String blacklistFiles = DevOpsPropsUtil.get(
			"profile." + _profileName + ".blacklist.files");

		for (String blacklistFile : blacklistFiles.split(",")) {
			String diffOutput = DevOpsProcessUtil.getOutput(
				workDir, "git diff " + sha1Hashes[0] + ".." + sha1Hashes[1] +
					" --name-only -- " + blacklistFile);

			if (!diffOutput.isEmpty()) {
				String comment = MessageFormat.format(
					DevOpsPropsUtil.get("comment.modified.blacklisted.file"),
					blacklistFiles.replaceAll(",", " or "));

				DevOpsUtil.githubPostComment(payloadJSONObject, comment);

				return true;
			}
		}

		return false;
	}

	protected boolean hasCompileError(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		File workDir = _devOpsGitHubRequestProcessor.getProfileGitRepositoryDir(
			_profileName);

		String plugins = DevOpsPeekPropsUtil.get(
			_profileName, "plugins.includes.portal");

		for (String plugin : plugins.split(",")) {
			String pluginDir = plugin.trim();

			if (plugin.endsWith("-ext")) {
				pluginDir = "ext/" + plugin;
			}
			else if (plugin.endsWith("-hook")) {
				pluginDir = "hooks/" + plugin;
			}
			else if (plugin.endsWith("-layouttpl")) {
				pluginDir = "layouttpl/" + plugin;
			}
			else if (plugin.endsWith("-portlet")) {
				pluginDir = "portlets/" + plugin;
			}
			else if (plugin.endsWith("-theme")) {
				pluginDir = "themes/" + plugin;
			}
			else if (plugin.endsWith("-web")) {
				pluginDir = "webs/" + plugin;
			}

			String logOutput = DevOpsProcessUtil.getOutput(
				workDir,
				"git log " + sha1Hashes[0] + ".." + sha1Hashes[1] +
					" --oneline -- " + pluginDir);

			if (logOutput.isEmpty()) {
				logOutput = DevOpsProcessUtil.getOutput(
					workDir, "git log " + sha1Hashes[0] + ".." + sha1Hashes[1] +
						" --oneline -- shared");

				if (logOutput.isEmpty()) {
					continue;
				}
			}

			File pluginWorkDir = new File(
				workDir.getPath() + "/plugins/" + pluginDir);

			Object[] returnValue = DevOpsProcessUtil.execute(
				pluginWorkDir, "ant direct-deploy");

			if ((Integer)returnValue[0] == 0) {
				continue;
			}

			String comment = MessageFormat.format(
				DevOpsPropsUtil.get("comment.compile.error"), returnValue[1]);

			DevOpsUtil.githubPostComment(payloadJSONObject, comment);

			return true;
		}

		return false;
	}

	protected boolean hasMergeConflict(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		File workDir = _devOpsGitHubRequestProcessor.getProfileGitRepositoryDir(
			_profileName);

		String pluginsGitBranch = DevOpsPeekPropsUtil.get(
			_profileName, "plugins.git.branch");

		String branchOutput = DevOpsProcessUtil.getOutput(
			workDir, "git branch --list " + pluginsGitBranch);

		if (branchOutput.isEmpty()) {
			DevOpsProcessUtil.execute(
				workDir, "git checkout origin/" + pluginsGitBranch);

			DevOpsProcessUtil.execute(
				workDir, "git checkout -b " + pluginsGitBranch);
		}

		String rebaseOutput = DevOpsProcessUtil.getOutput(
			workDir,
			"git rebase --onto " + pluginsGitBranch + " " + sha1Hashes[0] +
				" " + sha1Hashes[1]);

		if (rebaseOutput.isEmpty()) {
			return false;
		}

		DevOpsProcessUtil.execute(workDir, "git rebase --abort");

		String comment = MessageFormat.format(
			DevOpsPropsUtil.get("comment.merge.conflict"), pluginsGitBranch);

		DevOpsUtil.githubPostComment(payloadJSONObject, comment);

		return true;
	}

	protected void initProfileGitRepository(JSONObject payloadJSONObject) {
		_devOpsGitHubRequestProcessor.updateProfileGitRepository(_profileName);
	}

	protected void mergePatch(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		_devOpsGitHubRequestProcessor.updatePeekGitRepository(_profileName);

		DevOpsUtil.githubPostComment(
			payloadJSONObject, DevOpsPropsUtil.get("comment.updating.servers"));
	}

	protected void process(JSONObject payloadJSONObject) throws Exception {
		initProfileGitRepository(payloadJSONObject);

		String[] sha1Hashes = getSHA1Hashes(payloadJSONObject);

		if (hasBlacklistedFile(payloadJSONObject, sha1Hashes)) {
			return;
		}

		if (hasMergeConflict(payloadJSONObject, sha1Hashes)) {
			return;
		}

		if (hasCompileError(payloadJSONObject, sha1Hashes)) {
			return;
		}

		updateProfileGitRepository(payloadJSONObject, sha1Hashes);

		mergePatch(payloadJSONObject, sha1Hashes);

		updateJIRAIssue(payloadJSONObject, sha1Hashes);

		updatePullRequest(payloadJSONObject, sha1Hashes);
	}

	protected void sendEmail(JSONObject payloadJSONObject) {
	}

	protected void updateJIRAIssue(
		JSONObject payloadJSONObject, String[] sha1Hashes) {
	}

	protected void updateProfileGitRepository(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		File workDir = _devOpsGitHubRequestProcessor.getProfileGitRepositoryDir(
			_profileName);

		DevOpsProcessUtil.execute(
			workDir, "git clean -d -f -q -x -e .ivy -e build." +
				System.getenv("USERNAME") + ".properties");

		DevOpsProcessUtil.execute(workDir, "git reset --hard HEAD");

		String pluginsGitBranch = DevOpsPeekPropsUtil.get(
			_profileName, "plugins.git.branch");

		String branchOutput = DevOpsProcessUtil.getOutput(
			workDir, "git branch --list " + pluginsGitBranch);

		if (branchOutput.isEmpty()) {
			DevOpsProcessUtil.execute(
				workDir, "git branch -D " + pluginsGitBranch);
		}

		DevOpsProcessUtil.execute(
			workDir, "git checkout origin/" + pluginsGitBranch);

		DevOpsProcessUtil.execute(
			workDir, "git checkout -b " + pluginsGitBranch);

		DevOpsProcessUtil.execute(
			workDir,
			"git rebase --onto " + pluginsGitBranch + " " + sha1Hashes[0] +
				" " + sha1Hashes[1]);

		DevOpsProcessUtil.execute(
			workDir, "git push origin " + pluginsGitBranch);
	}

	protected void updatePullRequest(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		String sleepMillis = DevOpsPropsUtil.get(
			"profile." + _profileName + ".sleep.millis");

		try {
			Thread.sleep(Integer.valueOf(sleepMillis));
		}
		catch (InterruptedException ie) {
			_log.error(ie, ie);
		}

		DevOpsUtil.githubPostComment(
			payloadJSONObject, DevOpsPropsUtil.get("comment.patch.installed"));

		DevOpsUtil.githubClosePullRequest(payloadJSONObject);
	}

	private static Log _log = LogFactory.getLog(
		DevOpsPatchRequestProcessor.class);

	private DevOpsGitHubRequestProcessor _devOpsGitHubRequestProcessor;
	private Queue<JSONObject> _payloadJSONObjects =
		new ConcurrentLinkedQueue<JSONObject>();
	private String _profileName;
	private boolean _running;

}