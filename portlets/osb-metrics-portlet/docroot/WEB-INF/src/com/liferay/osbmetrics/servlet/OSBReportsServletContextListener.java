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

package com.liferay.osbmetrics.servlet;

import com.liferay.osbmetrics.importer.ReportsImporter;
import com.liferay.osbmetrics.importer.SQLImporter;
import com.liferay.portal.kernel.util.BasePortalLifecycle;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.File;

import java.net.URL;

import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Wesley Gong
 * @author Lin Cui
 * @author Rachael Koestartyo
 */
public class OSBReportsServletContextListener
	extends BasePortalLifecycle implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		portalDestroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		registerPortalLifecycle();
	}

	@Override
	protected void doPortalDestroy() {
	}

	@Override
	protected void doPortalInit() throws Exception {
		SQLImporter sqlImporter = new SQLImporter();

		sqlImporter.importSQL();

		ReportsImporter reportsImporter = new ReportsImporter();

		reportsImporter.importReports();

		initCompiledJaspers();
	}

	protected void initCompiledJaspers() throws Exception {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		String dirName = "/com/liferay/osbmetrics/dependencies";

		Enumeration<URL> enu = classLoader.getResources(dirName);

		if (!enu.hasMoreElements()) {
			return;
		}

		URL url = enu.nextElement();

		File file = new File(url.toURI());

		String[] fileNames = FileUtil.listFiles(file);

		for (String fileName : fileNames) {
			if (!fileName.endsWith(".jasper")) {
				continue;
			}

			URL jasperURL = classLoader.getResource(
				dirName + StringPool.SLASH + fileName);

			File jasperFile = new File(
				System.getProperty("catalina.home") + "/lib", fileName);

			if (jasperFile.exists()) {
				jasperFile.delete();
			}

			FileUtil.copyFile(new File(jasperURL.toURI()), jasperFile);
		}
	}

}