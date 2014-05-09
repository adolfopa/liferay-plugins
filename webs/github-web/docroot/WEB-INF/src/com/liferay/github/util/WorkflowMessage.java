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

import java.io.Serializable;

/**
 * @author Peter Shin
 */
public class WorkflowMessage implements Serializable {

	public static final String PULL_REQUEST = "PULL_REQUEST";

	public static final String SYNC_PROFILE_BRANCH = "SYNC_PROFILE_BRANCH";

	public WorkflowMessage(String command, Object payload) {
		_command = command;
		_payload = payload;
	}

	public String getCommand() {
		return _command;
	}

	public Object getPayload() {
		return _payload;
	}

	public void setCommand(String command) {
		_command = command;
	}

	public void setPayload(Object payload) {
		_payload = payload;
	}

	private String _command;
	private Object _payload;

}