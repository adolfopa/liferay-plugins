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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Brian Wing Shun Chan
 * @author Peter Shin
 */
public class DevOpsPeekPropsUtil {

	public static String get(String profileName, String key) {
		return _instance._get(profileName, key);
	}

	private DevOpsPeekPropsUtil() {
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(
				_PEEK_GIT_REPOSITORY_DIR_NAME + "/build.properties");
		}
		catch (FileNotFoundException fnfe) {
			_log.error(fnfe, fnfe);
		}

		Properties defaultProperties = new Properties();

		try {
			defaultProperties.load(inputStream);
		}
		catch (IOException ioe) {
			_log.error(ioe, ioe);
		}

		String profileNames = DevOpsPropsUtil.get("profile.names");

		for (String profileName : profileNames.split(",")) {
			try {
				inputStream = new FileInputStream(
					_PEEK_GIT_REPOSITORY_DIR_NAME + "/build." + profileName +
						".properties");
			}
			catch (FileNotFoundException fnfe) {
				_log.error(fnfe, fnfe);
			}

			Properties properties = new Properties(defaultProperties);

			try {
				properties.load(inputStream);
			}
			catch (IOException ioe) {
				_log.error(ioe, ioe);
			}
			finally {
				_propertiesToProfileNameMap.put(profileName, properties);
			}
		}
	}

	private String _get(String profileName, String key) {
		Properties properties = _propertiesToProfileNameMap.get(profileName);

		return properties.getProperty(key);
	}

	private static final String _PEEK_GIT_REPOSITORY_DIR_NAME =
		"/var/peek/repo";

	private static Log _log = LogFactory.getLog(DevOpsPeekPropsUtil.class);

	private static DevOpsPeekPropsUtil _instance = new DevOpsPeekPropsUtil();

	private Map<String, Properties> _propertiesToProfileNameMap =
		new ConcurrentHashMap<String, Properties>();

}