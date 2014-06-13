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

import java.io.IOException;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Brian Wing Shun Chan
 */
public class GitHubPropsUtil {

	public static String get(String key) {
		return _instance._get(key);
	}

	private GitHubPropsUtil() {
		try {
			Class<?> clazz = getClass();

			ClassLoader classLoader = clazz.getClassLoader();

			_properties.load(
				classLoader.getResourceAsStream("/github.properties"));
		}
		catch (IOException ioe) {
			_log.error(ioe, ioe);
		}
	}

	private String _get(String key) {
		return _properties.getProperty(key);
	}

	private static Log _log = LogFactory.getLog(GitHubPropsUtil.class);

	private static GitHubPropsUtil _instance = new GitHubPropsUtil();

	private Properties _properties = new Properties();

}