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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
				doProcess(payloadJSONObject);
			}
		}
		finally {
			_running = false;
		}
	}

	protected void doProcess(JSONObject payloadJSONObject) {
		try {
			initProfileGitRepository(payloadJSONObject);

			String[] sha1Hashes = getSHA1Hashes(payloadJSONObject);

			if (hasProtectedPath(payloadJSONObject, sha1Hashes)) {
				return;
			}

			if (hasMergeConflict(payloadJSONObject, sha1Hashes)) {
				return;
			}

			if (hasCompileError(payloadJSONObject, sha1Hashes)) {
				return;
			}

			updateProfileGitRepository(payloadJSONObject, sha1Hashes);

			installPatch(payloadJSONObject, sha1Hashes);

			updateJIRAIssue(payloadJSONObject, sha1Hashes);

			updatePullRequest(payloadJSONObject, sha1Hashes);
		}
		finally {
			sendEmail(payloadJSONObject);
		}
	}

	protected String[] getSHA1Hashes(JSONObject payloadJSONObject) {
		String sha1HashParent = "";
		String sha1HashHead = "";

		return new String[] {sha1HashParent, sha1HashHead};
	}

	protected boolean hasCompileError(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		return false;
	}

	protected boolean hasMergeConflict(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		return false;
	}

	protected boolean hasProtectedPath(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		return false;
	}

	protected void initProfileGitRepository(JSONObject payloadJSONObject) {
		_devOpsGitHubRequestProcessor.updateProfileGitRepository(_profileName);
	}

	protected void installPatch(
		JSONObject payloadJSONObject, String[] sha1Hashes) {

		_devOpsGitHubRequestProcessor.updatePeekGitRepository(_profileName);
	}

	protected void sendEmail(JSONObject payloadJSONObject) {
	}

	protected void updateJIRAIssue(
		JSONObject payloadJSONObject, String[] sha1Hashes) {
	}

	protected void updateProfileGitRepository(
		JSONObject payloadJSONObject, String[] sha1Hashes) {
	}

	protected void updatePullRequest(
		JSONObject payloadJSONObject, String[] sha1Hashes) {
	}

	private DevOpsGitHubRequestProcessor _devOpsGitHubRequestProcessor;
	private Queue<JSONObject> _payloadJSONObjects =
		new ConcurrentLinkedQueue<JSONObject>();
	private String _profileName;
	private boolean _running;

}