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

import java.net.HttpURLConnection;
import java.net.URL;

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

		DevOpsProcessUtil.Result gitConfigResult = DevOpsProcessUtil.execute(
			workDir, "git config --get remote.devops.url");

		JSONObject pullRequestJSONObject = payloadJSONObject.getJSONObject(
			"pull_request");

		int pullRequestNumber = pullRequestJSONObject.getInt("number");

		DevOpsProcessUtil.execute(
			workDir,
			"git fetch " + gitConfigResult.getOutput() + " refs/pull/" +
				pullRequestNumber + "/head:" + "pull-request-" +
					pullRequestNumber);

		DevOpsProcessUtil.execute(
			workDir, "git checkout pull-request-" + pullRequestNumber);

		JSONObject baseJSONObject = pullRequestJSONObject.getJSONObject("base");

		String baseRef = baseJSONObject.getString("ref");

		if (baseRef.equals("devops-" + _profileName)) {
			DevOpsProcessUtil.execute(
				workDir, "git rebase devops/devops-" + _profileName);
		}

		DevOpsProcessUtil.Result gitMergeBaseResult = DevOpsProcessUtil.execute(
			workDir,
			"git merge-base devops/" + baseRef + " pull-request-" +
				pullRequestNumber);
		DevOpsProcessUtil.Result revParseResult = DevOpsProcessUtil.execute(
			workDir, "git rev-parse HEAD");

		return new String[] {
			gitMergeBaseResult.getOutput(), revParseResult.getOutput()
		};
	}

	protected boolean hasBlacklistedFile(
			JSONObject payloadJSONObject, String[] sha1Hashes)
		throws Exception {

		File workDir = _devOpsGitHubRequestProcessor.getProfileGitRepositoryDir(
			_profileName);

		String blacklistPaths = DevOpsPropsUtil.get(
			"profile." + _profileName + ".blacklist.paths");

		for (String blacklistPath : blacklistPaths.split(",")) {
			DevOpsProcessUtil.Result result = DevOpsProcessUtil.execute(
				workDir,
				"git diff " + sha1Hashes[0] + ".." + sha1Hashes[1] +
					" --name-only -- " + blacklistPath);

			String output = result.getOutput();

			if (!output.isEmpty()) {
				String comment = MessageFormat.format(
					DevOpsPropsUtil.get(
						"github.comment.modified.blacklist.paths"),
					blacklistPaths.replaceAll(",", " or "));

				DevOpsUtil.postPullRequestComment(payloadJSONObject, comment);

				return true;
			}
		}

		return false;
	}

	protected boolean hasCompileError(
			JSONObject payloadJSONObject, String[] sha1Hashes)
		throws Exception {

		_devOpsGitHubRequestProcessor.buildProfileBundle(_profileName);

		File workDir = _devOpsGitHubRequestProcessor.getProfileGitRepositoryDir(
			_profileName);

		String plugins = DevOpsPeekPropsUtil.get(
			_profileName, "plugins.includes.portal");

		for (String plugin : plugins.split(",")) {
			String pluginPath = "";

			String pluginName = plugin.trim();

			if (pluginName.endsWith("-ext")) {
				pluginPath = "ext/" + pluginName;
			}
			else if (pluginName.endsWith("-hook")) {
				pluginPath = "hooks/" + pluginName;
			}
			else if (pluginName.endsWith("-layouttpl")) {
				pluginPath = "layouttpl/" + pluginName;
			}
			else if (pluginName.endsWith("-portlet")) {
				pluginPath = "portlets/" + pluginName;
			}
			else if (pluginName.endsWith("-theme")) {
				pluginPath = "themes/" + pluginName;
			}
			else if (pluginName.endsWith("-web")) {
				pluginPath = "webs/" + pluginName;
			}

			DevOpsProcessUtil.Result gitLogResult = DevOpsProcessUtil.execute(
				workDir,
				"git log " + sha1Hashes[0] + ".." + sha1Hashes[1] +
					" --oneline -- " + pluginPath);

			String logOutput = gitLogResult.getOutput();

			if (logOutput.isEmpty()) {
				gitLogResult = DevOpsProcessUtil.execute(
					workDir,
					"git log " + sha1Hashes[0] + ".." + sha1Hashes[1] +
						" --oneline -- shared");

				logOutput = gitLogResult.getOutput();

				if (logOutput.isEmpty()) {
					continue;
				}
			}

			File pluginWorkDir = new File(workDir.getPath() + "/" + pluginPath);

			DevOpsProcessUtil.Result antDirectDeployResult =
				DevOpsProcessUtil.execute(pluginWorkDir, "ant direct-deploy");

			if (antDirectDeployResult.getExitCode() == 0) {
				continue;
			}

			String comment = MessageFormat.format(
				DevOpsPropsUtil.get("github.comment.compile.error"),
				antDirectDeployResult.getOutput());

			DevOpsUtil.postPullRequestComment(payloadJSONObject, comment);

			return true;
		}

		return false;
	}

	protected boolean hasMergeConflict(
			JSONObject payloadJSONObject, String[] sha1Hashes)
		throws Exception {

		File workDir = _devOpsGitHubRequestProcessor.getProfileGitRepositoryDir(
			_profileName);

		String pluginsGitBranch = DevOpsPeekPropsUtil.get(
			_profileName, "plugins.git.branch");

		DevOpsProcessUtil.Result gitBranchResult = DevOpsProcessUtil.execute(
			workDir, "git branch --list " + pluginsGitBranch);

		String gitBranchOutput = gitBranchResult.getOutput();

		if (gitBranchOutput.isEmpty()) {
			DevOpsProcessUtil.execute(
				workDir,
				"git checkout -b " + pluginsGitBranch + " devops/" +
					pluginsGitBranch);
		}

		DevOpsProcessUtil.Result gitRebaseResult = DevOpsProcessUtil.execute(
			workDir,
			"git rebase --onto " + pluginsGitBranch + " " + sha1Hashes[0] +
				" " + sha1Hashes[1]);

		String gitRebaseOutput = gitRebaseResult.getOutput();

		if (gitRebaseOutput.isEmpty()) {
			return false;
		}

		DevOpsProcessUtil.execute(workDir, "git rebase --abort");

		String comment = MessageFormat.format(
			DevOpsPropsUtil.get("github.comment.merge.conflict"),
			pluginsGitBranch);

		DevOpsUtil.postPullRequestComment(payloadJSONObject, comment);

		return true;
	}

	protected void initProfileGitRepository(JSONObject payloadJSONObject)
		throws Exception {

		_devOpsGitHubRequestProcessor.updateProfileGitRepository(_profileName);
	}

	protected void mergePatch(JSONObject payloadJSONObject, String[] sha1Hashes)
		throws Exception {

		_devOpsGitHubRequestProcessor.updatePeekGitRepository(_profileName);

		DevOpsUtil.postPullRequestComment(
			payloadJSONObject,
			DevOpsPropsUtil.get("github.comment.updating.servers"));
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
			JSONObject payloadJSONObject, String[] sha1Hashes)
		throws Exception {

		File workDir = _devOpsGitHubRequestProcessor.getProfileGitRepositoryDir(
			_profileName);

		DevOpsProcessUtil.execute(
			workDir,
			"git clean -d -f -q -x -e .ivy -e build." +
				System.getenv("USERNAME") + ".properties");
		DevOpsProcessUtil.execute(workDir, "git reset --hard HEAD");

		String pluginsGitBranch = DevOpsPeekPropsUtil.get(
			_profileName, "plugins.git.branch");

		DevOpsProcessUtil.Result result = DevOpsProcessUtil.execute(
			workDir, "git branch --list " + pluginsGitBranch);

		String output = result.getOutput();

		if (output.isEmpty()) {
			DevOpsProcessUtil.execute(
				workDir, "git branch -D " + pluginsGitBranch);
		}

		DevOpsProcessUtil.execute(
			workDir,
			"git checkout -b " + pluginsGitBranch + " devops/" +
				pluginsGitBranch);
		DevOpsProcessUtil.execute(
			workDir,
			"git rebase --onto " + pluginsGitBranch + " " + sha1Hashes[0] +
				" " + sha1Hashes[1]);
		DevOpsProcessUtil.execute(
			workDir, "git push devops " + pluginsGitBranch);
	}

	protected void updatePullRequest(
			JSONObject payloadJSONObject, String[] sha1Hashes)
		throws Exception {

		boolean restartingServer = false;

		for (int i = 0; i < 180; i++) {
			try {
				Thread.sleep(5000);
			}
			catch (InterruptedException ie) {
			}

			int responseCode = 0;

			try {
				URL url = new URL(
					DevOpsPropsUtil.get(
						"profile." + _profileName + ".server.url"));

				HttpURLConnection httpURLConnection =
					(HttpURLConnection)url.openConnection();

				responseCode = httpURLConnection.getResponseCode();
			}
			catch (Exception e) {
				continue;
			}

			if (responseCode != HttpURLConnection.HTTP_OK) {
				restartingServer = true;
			}

			if (!restartingServer) {
				continue;
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				break;
			}
		}

		DevOpsUtil.postPullRequestComment(
			payloadJSONObject,
			DevOpsPropsUtil.get("github.comment.patch.installed"));

		DevOpsUtil.closePullRequest(payloadJSONObject);
	}

	private static Log _log = LogFactory.getLog(
		DevOpsPatchRequestProcessor.class);

	private DevOpsGitHubRequestProcessor _devOpsGitHubRequestProcessor;
	private Queue<JSONObject> _payloadJSONObjects =
		new ConcurrentLinkedQueue<JSONObject>();
	private String _profileName;
	private boolean _running;

}