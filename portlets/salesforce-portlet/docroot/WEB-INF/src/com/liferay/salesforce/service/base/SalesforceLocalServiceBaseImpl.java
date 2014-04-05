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

package com.liferay.salesforce.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.persistence.ClassNamePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import com.liferay.salesforce.service.SalesforceLocalService;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the salesforce local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.salesforce.service.impl.SalesforceLocalServiceImpl}.
 * </p>
 *
 * @author Michael C. Han
 * @see com.liferay.salesforce.service.impl.SalesforceLocalServiceImpl
 * @see com.liferay.salesforce.service.SalesforceLocalServiceUtil
 * @generated
 */
public abstract class SalesforceLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements SalesforceLocalService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.salesforce.service.SalesforceLocalServiceUtil} to access the salesforce local service.
	 */

	/**
	 * Returns the salesforce local service.
	 *
	 * @return the salesforce local service
	 */
	public com.liferay.salesforce.service.SalesforceLocalService getSalesforceLocalService() {
		return salesforceLocalService;
	}

	/**
	 * Sets the salesforce local service.
	 *
	 * @param salesforceLocalService the salesforce local service
	 */
	public void setSalesforceLocalService(
		com.liferay.salesforce.service.SalesforceLocalService salesforceLocalService) {
		this.salesforceLocalService = salesforceLocalService;
	}

	/**
	 * Returns the salesforce account local service.
	 *
	 * @return the salesforce account local service
	 */
	public com.liferay.salesforce.service.SalesforceAccountLocalService getSalesforceAccountLocalService() {
		return salesforceAccountLocalService;
	}

	/**
	 * Sets the salesforce account local service.
	 *
	 * @param salesforceAccountLocalService the salesforce account local service
	 */
	public void setSalesforceAccountLocalService(
		com.liferay.salesforce.service.SalesforceAccountLocalService salesforceAccountLocalService) {
		this.salesforceAccountLocalService = salesforceAccountLocalService;
	}

	/**
	 * Returns the salesforce contact local service.
	 *
	 * @return the salesforce contact local service
	 */
	public com.liferay.salesforce.service.SalesforceContactLocalService getSalesforceContactLocalService() {
		return salesforceContactLocalService;
	}

	/**
	 * Sets the salesforce contact local service.
	 *
	 * @param salesforceContactLocalService the salesforce contact local service
	 */
	public void setSalesforceContactLocalService(
		com.liferay.salesforce.service.SalesforceContactLocalService salesforceContactLocalService) {
		this.salesforceContactLocalService = salesforceContactLocalService;
	}

	/**
	 * Returns the salesforce event local service.
	 *
	 * @return the salesforce event local service
	 */
	public com.liferay.salesforce.service.SalesforceEventLocalService getSalesforceEventLocalService() {
		return salesforceEventLocalService;
	}

	/**
	 * Sets the salesforce event local service.
	 *
	 * @param salesforceEventLocalService the salesforce event local service
	 */
	public void setSalesforceEventLocalService(
		com.liferay.salesforce.service.SalesforceEventLocalService salesforceEventLocalService) {
		this.salesforceEventLocalService = salesforceEventLocalService;
	}

	/**
	 * Returns the salesforce lead local service.
	 *
	 * @return the salesforce lead local service
	 */
	public com.liferay.salesforce.service.SalesforceLeadLocalService getSalesforceLeadLocalService() {
		return salesforceLeadLocalService;
	}

	/**
	 * Sets the salesforce lead local service.
	 *
	 * @param salesforceLeadLocalService the salesforce lead local service
	 */
	public void setSalesforceLeadLocalService(
		com.liferay.salesforce.service.SalesforceLeadLocalService salesforceLeadLocalService) {
		this.salesforceLeadLocalService = salesforceLeadLocalService;
	}

	/**
	 * Returns the salesforce opportunity local service.
	 *
	 * @return the salesforce opportunity local service
	 */
	public com.liferay.salesforce.service.SalesforceOpportunityLocalService getSalesforceOpportunityLocalService() {
		return salesforceOpportunityLocalService;
	}

	/**
	 * Sets the salesforce opportunity local service.
	 *
	 * @param salesforceOpportunityLocalService the salesforce opportunity local service
	 */
	public void setSalesforceOpportunityLocalService(
		com.liferay.salesforce.service.SalesforceOpportunityLocalService salesforceOpportunityLocalService) {
		this.salesforceOpportunityLocalService = salesforceOpportunityLocalService;
	}

	/**
	 * Returns the salesforce task local service.
	 *
	 * @return the salesforce task local service
	 */
	public com.liferay.salesforce.service.SalesforceTaskLocalService getSalesforceTaskLocalService() {
		return salesforceTaskLocalService;
	}

	/**
	 * Sets the salesforce task local service.
	 *
	 * @param salesforceTaskLocalService the salesforce task local service
	 */
	public void setSalesforceTaskLocalService(
		com.liferay.salesforce.service.SalesforceTaskLocalService salesforceTaskLocalService) {
		this.salesforceTaskLocalService = salesforceTaskLocalService;
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

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = InfrastructureUtil.getDataSource();

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

	@BeanReference(type = com.liferay.salesforce.service.SalesforceLocalService.class)
	protected com.liferay.salesforce.service.SalesforceLocalService salesforceLocalService;
	@BeanReference(type = com.liferay.salesforce.service.SalesforceAccountLocalService.class)
	protected com.liferay.salesforce.service.SalesforceAccountLocalService salesforceAccountLocalService;
	@BeanReference(type = com.liferay.salesforce.service.SalesforceContactLocalService.class)
	protected com.liferay.salesforce.service.SalesforceContactLocalService salesforceContactLocalService;
	@BeanReference(type = com.liferay.salesforce.service.SalesforceEventLocalService.class)
	protected com.liferay.salesforce.service.SalesforceEventLocalService salesforceEventLocalService;
	@BeanReference(type = com.liferay.salesforce.service.SalesforceLeadLocalService.class)
	protected com.liferay.salesforce.service.SalesforceLeadLocalService salesforceLeadLocalService;
	@BeanReference(type = com.liferay.salesforce.service.SalesforceOpportunityLocalService.class)
	protected com.liferay.salesforce.service.SalesforceOpportunityLocalService salesforceOpportunityLocalService;
	@BeanReference(type = com.liferay.salesforce.service.SalesforceTaskLocalService.class)
	protected com.liferay.salesforce.service.SalesforceTaskLocalService salesforceTaskLocalService;
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
	private SalesforceLocalServiceClpInvoker _clpInvoker = new SalesforceLocalServiceClpInvoker();
}