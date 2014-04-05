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

package com.liferay.portal.workflow.kaleo.forms.service.base;

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
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcessLink;
import com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkService;
import com.liferay.portal.workflow.kaleo.forms.service.persistence.KaleoProcessLinkPersistence;
import com.liferay.portal.workflow.kaleo.forms.service.persistence.KaleoProcessPersistence;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the kaleo process link remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.workflow.kaleo.forms.service.impl.KaleoProcessLinkServiceImpl}.
 * </p>
 *
 * @author Marcellus Tavares
 * @see com.liferay.portal.workflow.kaleo.forms.service.impl.KaleoProcessLinkServiceImpl
 * @see com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkServiceUtil
 * @generated
 */
public abstract class KaleoProcessLinkServiceBaseImpl extends BaseServiceImpl
	implements KaleoProcessLinkService, IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkServiceUtil} to access the kaleo process link remote service.
	 */

	/**
	 * Returns the kaleo process local service.
	 *
	 * @return the kaleo process local service
	 */
	public com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLocalService getKaleoProcessLocalService() {
		return kaleoProcessLocalService;
	}

	/**
	 * Sets the kaleo process local service.
	 *
	 * @param kaleoProcessLocalService the kaleo process local service
	 */
	public void setKaleoProcessLocalService(
		com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLocalService kaleoProcessLocalService) {
		this.kaleoProcessLocalService = kaleoProcessLocalService;
	}

	/**
	 * Returns the kaleo process remote service.
	 *
	 * @return the kaleo process remote service
	 */
	public com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessService getKaleoProcessService() {
		return kaleoProcessService;
	}

	/**
	 * Sets the kaleo process remote service.
	 *
	 * @param kaleoProcessService the kaleo process remote service
	 */
	public void setKaleoProcessService(
		com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessService kaleoProcessService) {
		this.kaleoProcessService = kaleoProcessService;
	}

	/**
	 * Returns the kaleo process persistence.
	 *
	 * @return the kaleo process persistence
	 */
	public KaleoProcessPersistence getKaleoProcessPersistence() {
		return kaleoProcessPersistence;
	}

	/**
	 * Sets the kaleo process persistence.
	 *
	 * @param kaleoProcessPersistence the kaleo process persistence
	 */
	public void setKaleoProcessPersistence(
		KaleoProcessPersistence kaleoProcessPersistence) {
		this.kaleoProcessPersistence = kaleoProcessPersistence;
	}

	/**
	 * Returns the kaleo process link local service.
	 *
	 * @return the kaleo process link local service
	 */
	public com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkLocalService getKaleoProcessLinkLocalService() {
		return kaleoProcessLinkLocalService;
	}

	/**
	 * Sets the kaleo process link local service.
	 *
	 * @param kaleoProcessLinkLocalService the kaleo process link local service
	 */
	public void setKaleoProcessLinkLocalService(
		com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkLocalService kaleoProcessLinkLocalService) {
		this.kaleoProcessLinkLocalService = kaleoProcessLinkLocalService;
	}

	/**
	 * Returns the kaleo process link remote service.
	 *
	 * @return the kaleo process link remote service
	 */
	public com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkService getKaleoProcessLinkService() {
		return kaleoProcessLinkService;
	}

	/**
	 * Sets the kaleo process link remote service.
	 *
	 * @param kaleoProcessLinkService the kaleo process link remote service
	 */
	public void setKaleoProcessLinkService(
		com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkService kaleoProcessLinkService) {
		this.kaleoProcessLinkService = kaleoProcessLinkService;
	}

	/**
	 * Returns the kaleo process link persistence.
	 *
	 * @return the kaleo process link persistence
	 */
	public KaleoProcessLinkPersistence getKaleoProcessLinkPersistence() {
		return kaleoProcessLinkPersistence;
	}

	/**
	 * Sets the kaleo process link persistence.
	 *
	 * @param kaleoProcessLinkPersistence the kaleo process link persistence
	 */
	public void setKaleoProcessLinkPersistence(
		KaleoProcessLinkPersistence kaleoProcessLinkPersistence) {
		this.kaleoProcessLinkPersistence = kaleoProcessLinkPersistence;
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
		return KaleoProcessLink.class;
	}

	protected String getModelClassName() {
		return KaleoProcessLink.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = kaleoProcessLinkPersistence.getDataSource();

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

	@BeanReference(type = com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLocalService.class)
	protected com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLocalService kaleoProcessLocalService;
	@BeanReference(type = com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessService.class)
	protected com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessService kaleoProcessService;
	@BeanReference(type = KaleoProcessPersistence.class)
	protected KaleoProcessPersistence kaleoProcessPersistence;
	@BeanReference(type = com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkLocalService.class)
	protected com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkLocalService kaleoProcessLinkLocalService;
	@BeanReference(type = com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkService.class)
	protected com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkService kaleoProcessLinkService;
	@BeanReference(type = KaleoProcessLinkPersistence.class)
	protected KaleoProcessLinkPersistence kaleoProcessLinkPersistence;
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
	private KaleoProcessLinkServiceClpInvoker _clpInvoker = new KaleoProcessLinkServiceClpInvoker();
}