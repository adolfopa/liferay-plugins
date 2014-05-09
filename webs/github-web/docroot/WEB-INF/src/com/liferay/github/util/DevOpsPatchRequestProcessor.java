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

	public void process() {
		if (_running) {
			return;
		}

		_running = true;

		try {
			JSONObject payloadJSONObject = _payloadJSONObjects.poll();

			if (payloadJSONObject != null) {
				process(payloadJSONObject);
			}
		}
		finally {
			_running = false;
		}
	}

	protected void process(JSONObject payloadJSONObject) {
		_devOpsGitHubRequestProcessor.updatePeekGitRepository(_profileName);
	}

	private DevOpsGitHubRequestProcessor _devOpsGitHubRequestProcessor;
	private Queue<JSONObject> _payloadJSONObjects =
		new ConcurrentLinkedQueue<JSONObject>();
	private String _profileName;
	private boolean _running;

}