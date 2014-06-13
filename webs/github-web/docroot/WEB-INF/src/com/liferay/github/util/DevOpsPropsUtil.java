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
 * @author Peter Shin
 */
public class DevOpsPropsUtil {

	public static String get(String key) {
		return _instance._get(key);
	}

	private DevOpsPropsUtil() {
		try {
			Class<?> clazz = getClass();

			ClassLoader classLoader = clazz.getClassLoader();

			_properties.load(
				classLoader.getResourceAsStream("/devops.properties"));
		}
		catch (IOException ioe) {
			_log.error(ioe, ioe);
		}
	}

	private String _get(String key) {
		return _properties.getProperty(key);
	}

	private static Log _log = LogFactory.getLog(DevOpsPropsUtil.class);

	private static DevOpsPropsUtil _instance = new DevOpsPropsUtil();

	private Properties _properties = new Properties();

}