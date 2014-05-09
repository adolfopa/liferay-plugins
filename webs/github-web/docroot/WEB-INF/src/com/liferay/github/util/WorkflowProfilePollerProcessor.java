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

/**
 * @author Brian Wing Shun Chan
 * @author Peter Shin
 */
public class WorkflowProfilePollerProcessor implements Runnable {

	public WorkflowProfilePollerProcessor(
		String profileName, Queue<WorkflowMessage> queue) {

		_profileName = profileName;
		_queue = queue;
	}

	@Override
	public void run() {
		WorkflowMessage workflowMessage = _queue.poll();

		if (workflowMessage != null) {
			_service(workflowMessage);
		}
	}

	private void _service(WorkflowMessage workflowMessage) {
		String command = workflowMessage.getCommand();

		if (command.equals(WorkflowMessage.SYNC_PROFILE_BRANCH)) {
			WorkflowProfilePayloadHandler workflowProfilePayloadHandler =
				new WorkflowProfilePayloadHandler(_profileName, null);

			workflowProfilePayloadHandler.syncProfileBranch();
		}
	}

	private String _profileName;
	private Queue<WorkflowMessage> _queue;

}