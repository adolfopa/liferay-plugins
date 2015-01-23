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

package com.liferay.oauth.servlet;

import com.liferay.compat.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.util.BasePortalLifecycle;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Method;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author Ivica Cardic
 */
public class OAuthServletContextListener
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
	protected void doPortalInit() throws Exception {
		loadClusterLinkHelperClass();
	}

	protected void loadClusterLinkHelperClass() throws Exception {
		Method findLoadedClassMethod = ClassLoader.class.getDeclaredMethod(
			"findLoadedClass", String.class);

		findLoadedClassMethod.setAccessible(true);

		ClassLoader portalClassLoader = PortalClassLoaderUtil.getClassLoader();

		String className = "com.liferay.oauth.util.ClusterLinkHelper";

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

	private MessageListener _messageListener;

}