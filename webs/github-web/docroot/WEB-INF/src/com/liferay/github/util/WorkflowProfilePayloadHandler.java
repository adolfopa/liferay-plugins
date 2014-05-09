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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

/**
 * @author Brian Wing Shun Chan
 * @author Peter Shin
 */
public class WorkflowProfilePayloadHandler {

	public WorkflowProfilePayloadHandler(
		String profileName, JSONObject payloadJSONObject) {

		_messages = new ArrayList<String>();
		_payloadJSONObject = payloadJSONObject;
		_profileName = profileName;
	}

	public void processPullRequest() {
		/*

		try {
			_initGitRepository();

			String[] sha1Hashes = _getSHA1Hashes();

			if (_hasProtectedPath(sha1Hashes)) {
				return;
			}

			if (_hasMergeConflict(sha1Hashes)) {
				return;
			}

			if (_hasCompileError(sha1Hashes)) {
				return;
			}

			_updateProfileBranch(sha1Hashes);

			_updateServers();

			_updateJIRA(sha1Hashes);

			_updatePullRequest();
		}
		finally {
			_sendEmail();
		}

		*/
	}

	public void syncProfileBranch() {
		File file = new File(
			_PATHNAME_WORKFLOW_DIR + "/profiles/" + _profileName + "/plugins");

		String excludePattern =
			"build." + System.getenv("USERNAME") + ".properties";

		WorkflowProcessUtil.execute(
			"git clean -d -f -q -x -e .ivy -e " + excludePattern, file);

		WorkflowProcessUtil.execute("git reset --hard HEAD", file);

		WorkflowProcessUtil.execute("git fetch upstream", file);

		WorkflowProcessUtil.execute(
			"git checkout upstream/" + _PROFIE_BASE_BRANCH, file);

		String workflowBranch = "workflow-" + _profileName;

		Object[] returnValue = WorkflowProcessUtil.execute(
			"git branch --list " + workflowBranch, file);

		int exitCode = (Integer)returnValue[0];
		String output = (String)returnValue[1];

		if ((exitCode == 0) && (output.length() > 0)) {
			WorkflowProcessUtil.execute(
				"git branch -D " + workflowBranch, file);
		}

		WorkflowProcessUtil.execute("git checkout -b " + workflowBranch, file);

		WorkflowProcessUtil.execute("git push origin " + workflowBranch, file);
	}

	private static final String _PATHNAME_WORKFLOW_DIR = "/var/workflow";

	private static final String _PROFIE_BASE_BRANCH = "ee-6.1.x";

	private List<String> _messages;
	private JSONObject _payloadJSONObject;
	private String _profileName;

}