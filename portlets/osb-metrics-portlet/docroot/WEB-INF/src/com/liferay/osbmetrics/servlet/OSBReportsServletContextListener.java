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

package com.liferay.osbreports.hook.servlet;

import com.liferay.osbreports.hook.importer.ReportsImporter;
import com.liferay.osbreports.hook.importer.SQLImporter;
import com.liferay.osbreports.hook.messaging.OSBReportsMessageListener;
import com.liferay.osbreports.hook.util.PortletPropsValues;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.CronTrigger;
import com.liferay.portal.kernel.scheduler.SchedulerEngine;
import com.liferay.portal.kernel.scheduler.SchedulerEngineUtil;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.util.BasePortalLifecycle;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.File;

import java.net.URL;

import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.ServletContext;
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
		_servletContext = servletContextEvent.getServletContext();

		registerPortalLifecycle();
	}

	@Override
	protected void doPortalDestroy() {
		try {
			unscheduleJob();
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	@Override
	protected void doPortalInit() throws Exception {
		SQLImporter sqlImporter = new SQLImporter();

		sqlImporter.importSQL();

		ReportsImporter reportsImporter = new ReportsImporter();

		reportsImporter.importReports();

		initCompiledJaspers();

		scheduleJob();
	}

	protected void initCompiledJaspers() throws Exception {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		String dirName = "/com/liferay/osbreports/hook/util/dependencies";

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
				System.getProperty("catalina.home") + "/bin", fileName);

			if (jasperFile.exists()) {
				jasperFile.delete();
			}

			FileUtil.copyFile(new File(jasperURL.toURI()), jasperFile);
		}
	}

	protected void scheduleJob() throws Exception {
		Calendar cal = Calendar.getInstance();

		cal.add(
			Calendar.MINUTE,
			PortletPropsValues.AUDIT_PROCESSING_SCHEDULER_INITIAL_DELAY);

		Trigger trigger = new CronTrigger(
			OSBReportsMessageListener.class.getName(),
			OSBReportsMessageListener.class.getName(), cal.getTime(), null,
			PortletPropsValues.AUDIT_PROCESSING_SCHEDULER_INTERVAL);

		Message message = new Message();

		message.put(
			SchedulerEngine.CONTEXT_PATH, _servletContext.getContextPath());
		message.put(
			SchedulerEngine.MESSAGE_LISTENER_CLASS_NAME,
			OSBReportsMessageListener.class.getName());
		message.put(
			SchedulerEngine.PORTLET_ID,
			_servletContext.getServletContextName());

		SchedulerEngineUtil.schedule(
			trigger, StorageType.MEMORY_CLUSTERED, null,
			DestinationNames.SCHEDULER_DISPATCH, message, 0);
	}

	protected void unscheduleJob() throws Exception {
		SchedulerEngineUtil.unschedule(
			OSBReportsMessageListener.class.getName(),
			StorageType.MEMORY_CLUSTERED);
	}

	private static Log _log = LogFactoryUtil.getLog(
		OSBReportsServletContextListener.class);

	private ServletContext _servletContext;

}