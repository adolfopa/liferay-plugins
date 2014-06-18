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

import org.json.JSONObject;

/**
 * @author Brian Wing Shun Chan
 * @author Peter Shin
 */
public class DevOpsUtil {

	public static JSONObject closePullRequest(JSONObject payloadJSONObject)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(_GITHUB_API_URL);
		sb.append("/repos/");

		JSONObject pullRequestJSONObject = payloadJSONObject.getJSONObject(
			"pull_request");

		JSONObject baseJSONObject = pullRequestJSONObject.getJSONObject("base");

		JSONObject repoJSONObject = baseJSONObject.getJSONObject("repo");

		sb.append(repoJSONObject.getString("full_name"));
		sb.append("/pulls/");
		sb.append(pullRequestJSONObject.getInt("number"));

		JSONObject dataJSONObject = new JSONObject();

		dataJSONObject.put("state", "closed");

		return executePost(sb.toString(), dataJSONObject);
	}

	public static String getProfileName(JSONObject payloadJSONObject)
		throws Exception {

		JSONObject pullRequestJSONObject = payloadJSONObject.getJSONObject(
			"pull_request");

		JSONObject baseJSONObject = pullRequestJSONObject.getJSONObject("base");

		String ref = baseJSONObject.getString("ref");

		String profileNames = DevOpsPropsUtil.get("profile.names");

		for (String profileName : profileNames.split(",")) {
			if (ref.equals("devops-" + profileName)) {
				return profileName;
			}

			String pluginsGitBranch = DevOpsPeekPropsUtil.get(
				profileName, "plugins.git.branch");

			if (ref.equals(pluginsGitBranch)) {
				return profileName;
			}
		}

		return "";
	}

	public static boolean isValidAction(JSONObject payloadJSONObject)
		throws Exception {

		JSONObject senderJSONObject = payloadJSONObject.getJSONObject("sender");

		String action = payloadJSONObject.getString("action");
		String login = senderJSONObject.getString("login");

		if (action.equals("reopened") &&
			login.equals(DevOpsPropsUtil.get("profile.github.user.login"))) {

			return true;
		}
		else if (action.equals("opened")) {
			return true;
		}

		return false;
	}

	public static boolean isValidLogin(
			JSONObject payloadJSONObject, String profileName)
		throws Exception {

		JSONObject pullRequestJSONObject = payloadJSONObject.getJSONObject(
			"pull_request");

		JSONObject userJSONObject = pullRequestJSONObject.getJSONObject("user");

		String gitHubUserLogins = DevOpsPropsUtil.get(
			"profile." + profileName + ".github.user.logins");

		for (String gitHubUserLogin : gitHubUserLogins.split(",")) {
			if (gitHubUserLogin.equals(userJSONObject.getString("login"))) {
				return true;
			}
		}

		return false;
	}

	public static JSONObject postPullRequestComment(
			JSONObject payloadJSONObject, String comment)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(_GITHUB_API_URL);
		sb.append("/repos/");

		JSONObject pullRequestJSONObject = payloadJSONObject.getJSONObject(
			"pull_request");

		JSONObject baseJSONObject = pullRequestJSONObject.getJSONObject("base");

		JSONObject repoJSONObject = baseJSONObject.getJSONObject("repo");

		sb.append(repoJSONObject.getString("full_name"));
		sb.append("/issues/");
		sb.append(pullRequestJSONObject.getInt("number"));
		sb.append("/comments");

		JSONObject dataJSONObject = new JSONObject();

		dataJSONObject.put("body", comment);

		return executePost(sb.toString(), dataJSONObject);
	}

	protected static JSONObject executePost(String url, JSONObject jsonObject) {
		return null;
	}

	private static final String _GITHUB_API_URL = "https://api.github.com";

}