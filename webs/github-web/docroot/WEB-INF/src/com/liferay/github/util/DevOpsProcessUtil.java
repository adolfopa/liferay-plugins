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

/**
 * @author Brian Wing Shun Chan
 * @author Peter Shin
 */
public class DevOpsProcessUtil {

	public static Object[] execute(File workDir, String command) {
		return execute(workDir, command.split("\\s+"));
	}

	public static Object[] execute(File workDir, String[] commands) {
		return null;
	}

	public static String getOutput(File workDir, String command) {
		Object[] returnValue = execute(workDir, command);

		if ((returnValue == null) || (returnValue.length < 2) ||
			(returnValue[1] == null)) {

			return "";
		}

		return (String)returnValue[1];
	}

}