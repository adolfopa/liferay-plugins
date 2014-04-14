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

package com.liferay.pushnotifications.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.BaseServiceImpl;
import com.liferay.portal.service.persistence.ClassNamePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import com.liferay.pushnotifications.model.PushNotificationsDevice;
import com.liferay.pushnotifications.service.PushNotificationsDeviceService;
import com.liferay.pushnotifications.service.persistence.PushNotificationsDevicePersistence;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the push notifications device remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.pushnotifications.service.impl.PushNotificationsDeviceServiceImpl}.
 * </p>
 *
 * @author Bruno Farache
 * @see com.liferay.pushnotifications.service.impl.PushNotificationsDeviceServiceImpl
 * @see com.liferay.pushnotifications.service.PushNotificationsDeviceServiceUtil
 * @generated
 */
public abstract class PushNotificationsDeviceServiceBaseImpl
	extends BaseServiceImpl implements PushNotificationsDeviceService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.pushnotifications.service.PushNotificationsDeviceServiceUtil} to access the push notifications device remote service.
	 */

	/**
	 * Returns the push notifications device local service.
	 *
	 * @return the push notifications device local service
	 */
	public com.liferay.pushnotifications.service.PushNotificationsDeviceLocalService getPushNotificationsDeviceLocalService() {
		return pushNotificationsDeviceLocalService;
	}

	/**
	 * Sets the push notifications device local service.
	 *
	 * @param pushNotificationsDeviceLocalService the push notifications device local service
	 */
	public void setPushNotificationsDeviceLocalService(
		com.liferay.pushnotifications.service.PushNotificationsDeviceLocalService pushNotificationsDeviceLocalService) {
		this.pushNotificationsDeviceLocalService = pushNotificationsDeviceLocalService;
	}

	/**
	 * Returns the push notifications device remote service.
	 *
	 * @return the push notifications device remote service
	 */
	public com.liferay.pushnotifications.service.PushNotificationsDeviceService getPushNotificationsDeviceService() {
		return pushNotificationsDeviceService;
	}

	/**
	 * Sets the push notifications device remote service.
	 *
	 * @param pushNotificationsDeviceService the push notifications device remote service
	 */
	public void setPushNotificationsDeviceService(
		com.liferay.pushnotifications.service.PushNotificationsDeviceService pushNotificationsDeviceService) {
		this.pushNotificationsDeviceService = pushNotificationsDeviceService;
	}

	/**
	 * Returns the push notifications device persistence.
	 *
	 * @return the push notifications device persistence
	 */
	public PushNotificationsDevicePersistence getPushNotificationsDevicePersistence() {
		return pushNotificationsDevicePersistence;
	}

	/**
	 * Sets the push notifications device persistence.
	 *
	 * @param pushNotificationsDevicePersistence the push notifications device persistence
	 */
	public void setPushNotificationsDevicePersistence(
		PushNotificationsDevicePersistence pushNotificationsDevicePersistence) {
		this.pushNotificationsDevicePersistence = pushNotificationsDevicePersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.service.ClassNameLocalService getClassNameLocalService() {
		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.service.ClassNameLocalService classNameLocalService) {
		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name remote service.
	 *
	 * @return the class name remote service
	 */
	public com.liferay.portal.service.ClassNameService getClassNameService() {
		return classNameService;
	}

	/**
	 * Sets the class name remote service.
	 *
	 * @param classNameService the class name remote service
	 */
	public void setClassNameService(
		com.liferay.portal.service.ClassNameService classNameService) {
		this.classNameService = classNameService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {
		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.service.UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		Class<?> clazz = getClass();

		_classLoader = clazz.getClassLoader();
	}

	public void destroy() {
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	@Override
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	@Override
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	@Override
	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		if (contextClassLoader != _classLoader) {
			currentThread.setContextClassLoader(_classLoader);
		}

		try {
			return _clpInvoker.invokeMethod(name, parameterTypes, arguments);
		}
		finally {
			if (contextClassLoader != _classLoader) {
				currentThread.setContextClassLoader(contextClassLoader);
			}
		}
	}

	protected Class<?> getModelClass() {
		return PushNotificationsDevice.class;
	}

	protected String getModelClassName() {
		return PushNotificationsDevice.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = pushNotificationsDevicePersistence.getDataSource();

			DB db = DBFactoryUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = com.liferay.pushnotifications.service.PushNotificationsDeviceLocalService.class)
	protected com.liferay.pushnotifications.service.PushNotificationsDeviceLocalService pushNotificationsDeviceLocalService;
	@BeanReference(type = com.liferay.pushnotifications.service.PushNotificationsDeviceService.class)
	protected com.liferay.pushnotifications.service.PushNotificationsDeviceService pushNotificationsDeviceService;
	@BeanReference(type = PushNotificationsDevicePersistence.class)
	protected PushNotificationsDevicePersistence pushNotificationsDevicePersistence;
	@BeanReference(type = com.liferay.counter.service.CounterLocalService.class)
	protected com.liferay.counter.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.service.ClassNameLocalService.class)
	protected com.liferay.portal.service.ClassNameLocalService classNameLocalService;
	@BeanReference(type = com.liferay.portal.service.ClassNameService.class)
	protected com.liferay.portal.service.ClassNameService classNameService;
	@BeanReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;
	@BeanReference(type = com.liferay.portal.service.ResourceLocalService.class)
	protected com.liferay.portal.service.ResourceLocalService resourceLocalService;
	@BeanReference(type = com.liferay.portal.service.UserLocalService.class)
	protected com.liferay.portal.service.UserLocalService userLocalService;
	@BeanReference(type = com.liferay.portal.service.UserService.class)
	protected com.liferay.portal.service.UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private String _beanIdentifier;
	private ClassLoader _classLoader;
	private PushNotificationsDeviceServiceClpInvoker _clpInvoker = new PushNotificationsDeviceServiceClpInvoker();
}