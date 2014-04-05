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

package com.liferay.lcs.servlet;

import com.liferay.compat.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.HotDeployMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.util.BasePortalLifecycle;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Ivica Cardic
 */
public class LCSServletContextListener
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
	protected void doPortalDestroy() throws Exception {
		MessageBusUtil.unregisterMessageListener(
			DestinationNames.HOT_DEPLOY, _messageListener);
	}

	@Override
	protected void doPortalInit() {
		_messageListener = new HotDeployMessageListener(
			"lcs-portlet") {

			@Override
			protected void onDeploy(Message message) throws Exception {
				_onDeploy = true;

				updateMonitoring();

				loadClusterLinkHelperClass();
			}

			@Override
			protected void onUndeploy(Message message) throws Exception {
				_onDeploy = false;

				updateMonitoring();
			}

		};

		MessageBusUtil.registerMessageListener(
			DestinationNames.HOT_DEPLOY, _messageListener);
	}

	protected Class<?> findLoadedClass(String className) throws Exception {
		Method findLoadedClassMethod = ClassLoader.class.getDeclaredMethod(
			"findLoadedClass", String.class);

		findLoadedClassMethod.setAccessible(true);

		ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();

		return (Class<?>)findLoadedClassMethod.invoke(classLoader, className);
	}

	protected Configuration getConfiguration() throws Exception {
		Class<?> propsUtilClass = findLoadedClass(
			"com.liferay.portal.util.PropsUtil");

		Method getConfigurationMethod = propsUtilClass.getDeclaredMethod(
			"_getConfiguration");

		getConfigurationMethod.setAccessible(true);

		Field instanceField = propsUtilClass.getDeclaredField("_instance");

		instanceField.setAccessible(true);

		Object instance = instanceField.get(propsUtilClass);

		return (Configuration)getConfigurationMethod.invoke(instance);
	}

	protected void loadClusterLinkHelperClass() throws Exception {
		Method findLoadedClassMethod = ClassLoader.class.getDeclaredMethod(
			"findLoadedClass", String.class);

		findLoadedClassMethod.setAccessible(true);

		ClassLoader portalClassLoader = PortalClassLoaderUtil.getClassLoader();

		String className = "com.liferay.lcs.util.ClusterLinkHelper";

		if (findLoadedClassMethod.invoke(portalClassLoader, className)
				!= null) {

			return;
		}

		InputStream inputStream = null;

		try {
			Method defineClassMethod = ClassLoader.class.getDeclaredMethod(
				"defineClass", String.class, byte[].class, int.class,
				int.class);

			defineClassMethod.setAccessible(true);

			Class<?> clazz = getClass();

			ClassLoader classLoader = clazz.getClassLoader();

			String classResource = StringUtil.replace(
				className, StringPool.PERIOD, StringPool.SLASH);

			classResource += ".class";

			inputStream = classLoader.getResourceAsStream(classResource);

			byte[] bytes = FileUtil.getBytes(inputStream);

			defineClassMethod.invoke(
				portalClassLoader, className, bytes, 0, bytes.length);
		}
		catch (IOException ioe) {
			throw new ClassNotFoundException(
				"Unable to load " + className, ioe);
		}
		finally {
			StreamUtil.cleanUp(inputStream);
		}
	}

	protected void updateHibernateGenerateStatistics() throws Exception {
		boolean hibernateGenerateStatistics = updatePropsValues(
			"HIBERNATE_GENERATE_STATISTICS",
			PropsKeys.HIBERNATE_GENERATE_STATISTICS);

		Object sessionFactory = PortalBeanLocatorUtil.locate(
			"liferayHibernateSessionFactory");

		Class<?> sessionFactoryClass = sessionFactory.getClass();

		Method getStatisticsMethod = sessionFactoryClass.getDeclaredMethod(
			"getStatistics");

		Object statistics = getStatisticsMethod.invoke(sessionFactory);

		Class<?> statisticsClass = statistics.getClass();

		Method setStatisticsEnabledMethod = statisticsClass.getDeclaredMethod(
			"setStatisticsEnabled", boolean.class);

		setStatisticsEnabledMethod.invoke(
			statistics, hibernateGenerateStatistics);
	}

	protected void updateMonitoring() throws Exception {
		updateHibernateGenerateStatistics();
		updateMonitoringPortalRequest();
		updateMonitoringPortletManager();
		updateMonitoringPortletRequest();
	}

	protected void updateMonitoringPortalRequest() throws Exception {
		boolean monitoringPortalRequest = updatePropsValues(
			"MONITORING_PORTAL_REQUEST", PropsKeys.MONITORING_PORTAL_REQUEST);

		Class<?> monitoringFilterClass = findLoadedClass(
			"com.liferay.portal.servlet.filters.monitoring.MonitoringFilter");

		Field monitoringPortalRequestField =
			monitoringFilterClass.getDeclaredField("_monitoringPortalRequest");

		monitoringPortalRequestField.setAccessible(true);
		monitoringPortalRequestField.setBoolean(
			monitoringFilterClass, monitoringPortalRequest);
	}

	protected void updateMonitoringPortletRequest() throws Exception {
		updateMonitoringPortletRequest(
			"_monitoringPortletActionRequest",
			"MONITORING_PORTLET_ACTION_REQUEST",
			PropsKeys.MONITORING_PORTLET_ACTION_REQUEST);

		updateMonitoringPortletRequest(
			"_monitoringPortletEventRequest",
			"MONITORING_PORTLET_EVENT_REQUEST",
			PropsKeys.MONITORING_PORTLET_EVENT_REQUEST);

		updateMonitoringPortletRequest(
			"_monitoringPortletRenderRequest",
			"MONITORING_PORTLET_RENDER_REQUEST",
			PropsKeys.MONITORING_PORTLET_RENDER_REQUEST);

		updateMonitoringPortletRequest(
			"_monitoringPortletResourceRequest",
			"MONITORING_PORTLET_RESOURCE_REQUEST",
			PropsKeys.MONITORING_PORTLET_RESOURCE_REQUEST);
	}

	protected void updateMonitoringPortletRequest(
			String fieldName, String propsValuesKey, String configurationKey)
		throws Exception {

		boolean monitoringPortletRenderRequest = updatePropsValues(
			propsValuesKey, configurationKey);

		Class<?> monitoringPortletClass = findLoadedClass(
			"com.liferay.portlet.MonitoringPortlet");

		Field monitoringPortletRenderRequestField =
			monitoringPortletClass.getDeclaredField(fieldName);

		monitoringPortletRenderRequestField.setAccessible(true);
		monitoringPortletRenderRequestField.setBoolean(
			monitoringPortletClass, monitoringPortletRenderRequest);
	}

	protected boolean updatePropsValues(
			String propsValuesKey, String configurationKey)
		throws Exception {

		Field modifiersField = Field.class.getDeclaredField("modifiers");

		modifiersField.setAccessible(true);

		Class<?> propsValuesClass = findLoadedClass(
			"com.liferay.portal.util.PropsValues");

		Field propsValuesKeyField = propsValuesClass.getField(propsValuesKey);

		modifiersField.setInt(
			propsValuesKeyField,
			propsValuesKeyField.getModifiers() & ~Modifier.FINAL);

		boolean value = false;

		if (_onDeploy) {
			_originalPropsValues.put(
				propsValuesKey,
				propsValuesKeyField.getBoolean(propsValuesClass));

			value = true;
		}
		else {
			value = _originalPropsValues.get(propsValuesKey);
		}

		propsValuesKeyField.setBoolean(propsValuesClass, value);

		Configuration configuration = getConfiguration();

		configuration.set(configurationKey, String.valueOf(value));

		return value;
	}

	private void updateMonitoringPortletManager() throws Exception {
		Object monitoringPortletManager = PortalBeanLocatorUtil.locate(
			"com.liferay.portlet.MonitoringPortletManager");

		Class<?> monitoringPortletManagerClass =
			monitoringPortletManager.getClass();

		Method setActiveMethod =
			monitoringPortletManagerClass.getDeclaredMethod(
				"setActive", boolean.class);

		setActiveMethod.invoke(monitoringPortletManager, true);
	}

	private MessageListener _messageListener;
	private boolean _onDeploy;
	private Map<String, Boolean> _originalPropsValues =
		new HashMap<String, Boolean>();

}