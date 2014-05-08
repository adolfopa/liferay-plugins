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

package com.liferay.osbmetrics.importer;

import com.liferay.compat.portal.kernel.util.StringUtil;
import com.liferay.osbmetrics.util.PortletPropsValues;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Wesley Gong
 * @author Brian Wing Shun Chan
 */
public class SQLImporter {

	public void importSQL() throws Exception {
		importSQL("views");

		importSQL("procedures");
	}

	protected void executeSQL(List<String> fileNames, String dirName)
		throws Exception {

		List<String> processedFileNames = new ArrayList<String>();

		DB db = DBFactoryUtil.getDB();

		for (String fileName : fileNames) {
			if (_log.isDebugEnabled()) {
				_log.debug("Processing file: " + fileName);
			}

			if (!fileName.endsWith(".sql")) {
				processedFileNames.add(fileName);

				continue;
			}

			int index = fileName.lastIndexOf(".sql");

			String tableName = fileName.substring(0, index);

			if (dirName.endsWith("procedures")) {
				tableName = "DROP PROCEDURE IF EXISTS " + tableName;
			}
			else {
				tableName = "DROP VIEW IF EXISTS " + tableName;
			}

			StopWatch stopWatch = new StopWatch();

			stopWatch.start();

			db.runSQL(tableName);

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Drop SQL completed in " + stopWatch.getTime() +
						" seconds");
			}

			ClassLoader classLoader = getClassLoader();

			InputStream inputStream = classLoader.getResourceAsStream(
				dirName + StringPool.SLASH + fileName);

			String sql = new String(FileUtil.getBytes(inputStream));

			sql = replaceDatabaseName(
				sql, "[$JIRA_DB$]", PortletPropsValues.JIRA_DB);
			sql = replaceDatabaseName(
				sql, "[$LRDCOM_DB$]", PortletPropsValues.LRDCOM_DB);

			stopWatch.reset();

			stopWatch.start();

			try {
				db.runSQL(sql);
			}
			catch (Exception e) {
				_log.error(e, e);

				continue;
			}
			finally {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Processed SQL in " + stopWatch.getTime() + " seconds");
				}
			}

			processedFileNames.add(fileName);
		}

		fileNames.removeAll(processedFileNames);
	}

	protected ClassLoader getClassLoader() {
		Class<?> clazz = getClass();

		return clazz.getClassLoader();
	}

	protected void importSQL(String dirName) throws Exception {
		ClassLoader classLoader = getClassLoader();

		dirName = "/resources/sql/" + dirName;

		Enumeration<URL> enu = classLoader.getResources(dirName);

		if (!enu.hasMoreElements()) {
			return;
		}

		URL url = enu.nextElement();

		File file = new File(url.toURI());

		List<String> fileNames = ListUtil.toList(FileUtil.listFiles(file));

		while (!fileNames.isEmpty()) {
			executeSQL(fileNames, dirName);
		}
	}

	protected String replaceDatabaseName(
		String sql, String key, String databaseName) {

		if (Validator.isNotNull(databaseName)) {
			databaseName = databaseName + StringPool.PERIOD;
		}

		return StringUtil.replace(sql, key, databaseName);
	}

	private static Log _log = LogFactoryUtil.getLog(SQLImporter.class);

}