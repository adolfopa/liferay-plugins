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

	public static Result execute(File workDir, String command)
		throws Exception {

		return execute(workDir, command.split("\\s+"));
	}

	public static Result execute(File workDir, String[] commands)
		throws Exception {

		Result result = new Result();

		result.setExitCode(0);
		result.setOutput("");

		return result;
	}

	public static class Result {

		public int getExitCode() {
			return _exitCode;
		}

		public String getOutput() {
			return _output;
		}

		public void setExitCode(int exitCode) {
			_exitCode = exitCode;
		}

		public void setOutput(String output) {
			_output = output;
		}

		private int _exitCode = -1;
		private String _output = "";

	}

}