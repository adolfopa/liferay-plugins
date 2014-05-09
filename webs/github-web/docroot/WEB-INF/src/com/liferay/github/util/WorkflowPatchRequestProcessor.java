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
public class WorkflowPatchRequestProcessor {

	public WorkflowPatchRequestProcessor(
		WorkflowGitHubRequestProcessor workflowGitHubRequestProcessor,
		String profileName) {

		_workflowGitHubRequestProcessor = workflowGitHubRequestProcessor;
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
		_workflowGitHubRequestProcessor.updatePeekGitRepository(_profileName);
	}

	private Queue<JSONObject> _payloadJSONObjects =
		new ConcurrentLinkedQueue<JSONObject>();
	private String _profileName;
	private boolean _running;
	private WorkflowGitHubRequestProcessor _workflowGitHubRequestProcessor;

}