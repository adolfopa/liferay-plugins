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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Brian Wing Shun Chan
 * @author Peter Shin
 */
public class DevOpsProcessUtil {

	public static Result execute(File workDir, String command)
		throws Exception {

		List<String> commands = new ArrayList<String>();

		do {
			command = command.trim();

			int beginIndex = 0;
			int endIndex = 0;

			if (command.startsWith("\"")) {
				beginIndex = 1;
				endIndex = command.indexOf("\"", beginIndex);
			}
			else if (command.startsWith("'")) {
				beginIndex = 1;
				endIndex = command.indexOf("'", beginIndex);
			}
			else if (command.contains(" ")) {
				endIndex = command.indexOf(" ");
			}
			else {
				endIndex = command.length();
			}

			commands.add(command.substring(beginIndex, endIndex));

			if (endIndex != command.length()) {
				command = command.substring(endIndex + 1);
			}
			else {
				command = command.substring(endIndex);
			}
		}
		while (!command.isEmpty());

		return execute(workDir, commands.toArray(new String[commands.size()]));
	}

	public static Result execute(File workDir, String[] commands)
		throws Exception {

		BufferedReader bufferedReader = null;

		try {
			if (_log.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < commands.length; i++) {
					sb.append(commands[i]);

					if ((i + 1) < commands.length) {
						sb.append(" ");
					}
				}

				_log.info("Command: " + sb.toString());
			}

			ProcessBuilder processBuilder = new ProcessBuilder(commands);

			processBuilder.directory(workDir);
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();

			bufferedReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

			String line = null;
			StringBuilder sb = new StringBuilder();

			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();

				if (line.length() == 0) {
					continue;
				}

				if (sb.length() != 0) {
					sb.append("\n");
				}

				sb.append(line);
			}

			Result result = new Result();

			result.setExitCode(process.waitFor());
			result.setOutput(sb.toString());

			if (_log.isInfoEnabled()) {
				_log.info("Exit Code: " + result.getExitCode());
				_log.info("Output: " + result.getOutput());
			}

			return result;
		}
		finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
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

	private static Log _log = LogFactory.getLog(DevOpsProcessUtil.class);

}