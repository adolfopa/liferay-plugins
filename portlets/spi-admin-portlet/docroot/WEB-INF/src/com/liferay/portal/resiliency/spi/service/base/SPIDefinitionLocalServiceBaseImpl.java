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

package com.liferay.portal.resiliency.spi.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.PersistedModel;
import com.liferay.portal.resiliency.spi.model.SPIDefinition;
import com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalService;
import com.liferay.portal.resiliency.spi.service.persistence.SPIDefinitionPersistence;
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.service.persistence.BackgroundTaskPersistence;
import com.liferay.portal.service.persistence.ClassNamePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.service.persistence.ExpandoRowPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the s p i definition local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.resiliency.spi.service.impl.SPIDefinitionLocalServiceImpl}.
 * </p>
 *
 * @author Michael C. Han
 * @see com.liferay.portal.resiliency.spi.service.impl.SPIDefinitionLocalServiceImpl
 * @see com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalServiceUtil
 * @generated
 */
public abstract class SPIDefinitionLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements SPIDefinitionLocalService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalServiceUtil} to access the s p i definition local service.
	 */

	/**
	 * Adds the s p i definition to the database. Also notifies the appropriate model listeners.
	 *
	 * @param spiDefinition the s p i definition
	 * @return the s p i definition that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SPIDefinition addSPIDefinition(SPIDefinition spiDefinition) {
		spiDefinition.setNew(true);

		return spiDefinitionPersistence.update(spiDefinition);
	}

	/**
	 * Creates a new s p i definition with the primary key. Does not add the s p i definition to the database.
	 *
	 * @param spiDefinitionId the primary key for the new s p i definition
	 * @return the new s p i definition
	 */
	@Override
	public SPIDefinition createSPIDefinition(long spiDefinitionId) {
		return spiDefinitionPersistence.create(spiDefinitionId);
	}

	/**
	 * Deletes the s p i definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param spiDefinitionId the primary key of the s p i definition
	 * @return the s p i definition that was removed
	 * @throws PortalException if a s p i definition with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SPIDefinition deleteSPIDefinition(long spiDefinitionId)
		throws PortalException {
		return spiDefinitionPersistence.remove(spiDefinitionId);
	}

	/**
	 * Deletes the s p i definition from the database. Also notifies the appropriate model listeners.
	 *
	 * @param spiDefinition the s p i definition
	 * @return the s p i definition that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SPIDefinition deleteSPIDefinition(SPIDefinition spiDefinition)
		throws PortalException {
		return spiDefinitionPersistence.remove(spiDefinition);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(SPIDefinition.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return spiDefinitionPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.resiliency.spi.model.impl.SPIDefinitionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end) {
		return spiDefinitionPersistence.findWithDynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.resiliency.spi.model.impl.SPIDefinitionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator) {
		return spiDefinitionPersistence.findWithDynamicQuery(dynamicQuery,
			start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows that match the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return spiDefinitionPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows that match the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) {
		return spiDefinitionPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public SPIDefinition fetchSPIDefinition(long spiDefinitionId) {
		return spiDefinitionPersistence.fetchByPrimaryKey(spiDefinitionId);
	}

	/**
	 * Returns the s p i definition with the primary key.
	 *
	 * @param spiDefinitionId the primary key of the s p i definition
	 * @return the s p i definition
	 * @throws PortalException if a s p i definition with the primary key could not be found
	 */
	@Override
	public SPIDefinition getSPIDefinition(long spiDefinitionId)
		throws PortalException {
		return spiDefinitionPersistence.findByPrimaryKey(spiDefinitionId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(SPIDefinition.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("spiDefinitionId");

		return actionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(SPIDefinition.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("spiDefinitionId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return spiDefinitionLocalService.deleteSPIDefinition((SPIDefinition)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return spiDefinitionPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the s p i definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.resiliency.spi.model.impl.SPIDefinitionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of s p i definitions
	 * @param end the upper bound of the range of s p i definitions (not inclusive)
	 * @return the range of s p i definitions
	 */
	@Override
	public List<SPIDefinition> getSPIDefinitions(int start, int end) {
		return spiDefinitionPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of s p i definitions.
	 *
	 * @return the number of s p i definitions
	 */
	@Override
	public int getSPIDefinitionsCount() {
		return spiDefinitionPersistence.countAll();
	}

	/**
	 * Updates the s p i definition in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param spiDefinition the s p i definition
	 * @return the s p i definition that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SPIDefinition updateSPIDefinition(SPIDefinition spiDefinition) {
		return spiDefinitionPersistence.update(spiDefinition);
	}

	/**
	 * Returns the s p i definition local service.
	 *
	 * @return the s p i definition local service
	 */
	public com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalService getSPIDefinitionLocalService() {
		return spiDefinitionLocalService;
	}

	/**
	 * Sets the s p i definition local service.
	 *
	 * @param spiDefinitionLocalService the s p i definition local service
	 */
	public void setSPIDefinitionLocalService(
		com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalService spiDefinitionLocalService) {
		this.spiDefinitionLocalService = spiDefinitionLocalService;
	}

	/**
	 * Returns the s p i definition remote service.
	 *
	 * @return the s p i definition remote service
	 */
	public com.liferay.portal.resiliency.spi.service.SPIDefinitionService getSPIDefinitionService() {
		return spiDefinitionService;
	}

	/**
	 * Sets the s p i definition remote service.
	 *
	 * @param spiDefinitionService the s p i definition remote service
	 */
	public void setSPIDefinitionService(
		com.liferay.portal.resiliency.spi.service.SPIDefinitionService spiDefinitionService) {
		this.spiDefinitionService = spiDefinitionService;
	}

	/**
	 * Returns the s p i definition persistence.
	 *
	 * @return the s p i definition persistence
	 */
	public SPIDefinitionPersistence getSPIDefinitionPersistence() {
		return spiDefinitionPersistence;
	}

	/**
	 * Sets the s p i definition persistence.
	 *
	 * @param spiDefinitionPersistence the s p i definition persistence
	 */
	public void setSPIDefinitionPersistence(
		SPIDefinitionPersistence spiDefinitionPersistence) {
		this.spiDefinitionPersistence = spiDefinitionPersistence;
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
	 * Returns the background task local service.
	 *
	 * @return the background task local service
	 */
	public com.liferay.portal.service.BackgroundTaskLocalService getBackgroundTaskLocalService() {
		return backgroundTaskLocalService;
	}

	/**
	 * Sets the background task local service.
	 *
	 * @param backgroundTaskLocalService the background task local service
	 */
	public void setBackgroundTaskLocalService(
		com.liferay.portal.service.BackgroundTaskLocalService backgroundTaskLocalService) {
		this.backgroundTaskLocalService = backgroundTaskLocalService;
	}

	/**
	 * Returns the background task remote service.
	 *
	 * @return the background task remote service
	 */
	public com.liferay.portal.service.BackgroundTaskService getBackgroundTaskService() {
		return backgroundTaskService;
	}

	/**
	 * Sets the background task remote service.
	 *
	 * @param backgroundTaskService the background task remote service
	 */
	public void setBackgroundTaskService(
		com.liferay.portal.service.BackgroundTaskService backgroundTaskService) {
		this.backgroundTaskService = backgroundTaskService;
	}

	/**
	 * Returns the background task persistence.
	 *
	 * @return the background task persistence
	 */
	public BackgroundTaskPersistence getBackgroundTaskPersistence() {
		return backgroundTaskPersistence;
	}

	/**
	 * Sets the background task persistence.
	 *
	 * @param backgroundTaskPersistence the background task persistence
	 */
	public void setBackgroundTaskPersistence(
		BackgroundTaskPersistence backgroundTaskPersistence) {
		this.backgroundTaskPersistence = backgroundTaskPersistence;
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

	/**
	 * Returns the expando row local service.
	 *
	 * @return the expando row local service
	 */
	public com.liferay.portlet.expando.service.ExpandoRowLocalService getExpandoRowLocalService() {
		return expandoRowLocalService;
	}

	/**
	 * Sets the expando row local service.
	 *
	 * @param expandoRowLocalService the expando row local service
	 */
	public void setExpandoRowLocalService(
		com.liferay.portlet.expando.service.ExpandoRowLocalService expandoRowLocalService) {
		this.expandoRowLocalService = expandoRowLocalService;
	}

	/**
	 * Returns the expando row persistence.
	 *
	 * @return the expando row persistence
	 */
	public ExpandoRowPersistence getExpandoRowPersistence() {
		return expandoRowPersistence;
	}

	/**
	 * Sets the expando row persistence.
	 *
	 * @param expandoRowPersistence the expando row persistence
	 */
	public void setExpandoRowPersistence(
		ExpandoRowPersistence expandoRowPersistence) {
		this.expandoRowPersistence = expandoRowPersistence;
	}

	public void afterPropertiesSet() {
		Class<?> clazz = getClass();

		_classLoader = clazz.getClassLoader();

		PersistedModelLocalServiceRegistryUtil.register("com.liferay.portal.resiliency.spi.model.SPIDefinition",
			spiDefinitionLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"com.liferay.portal.resiliency.spi.model.SPIDefinition");
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
		return SPIDefinition.class;
	}

	protected String getModelClassName() {
		return SPIDefinition.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = spiDefinitionPersistence.getDataSource();

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

	@BeanReference(type = com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalService.class)
	protected com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalService spiDefinitionLocalService;
	@BeanReference(type = com.liferay.portal.resiliency.spi.service.SPIDefinitionService.class)
	protected com.liferay.portal.resiliency.spi.service.SPIDefinitionService spiDefinitionService;
	@BeanReference(type = SPIDefinitionPersistence.class)
	protected SPIDefinitionPersistence spiDefinitionPersistence;
	@BeanReference(type = com.liferay.counter.service.CounterLocalService.class)
	protected com.liferay.counter.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.service.BackgroundTaskLocalService.class)
	protected com.liferay.portal.service.BackgroundTaskLocalService backgroundTaskLocalService;
	@BeanReference(type = com.liferay.portal.service.BackgroundTaskService.class)
	protected com.liferay.portal.service.BackgroundTaskService backgroundTaskService;
	@BeanReference(type = BackgroundTaskPersistence.class)
	protected BackgroundTaskPersistence backgroundTaskPersistence;
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
	@BeanReference(type = com.liferay.portlet.expando.service.ExpandoRowLocalService.class)
	protected com.liferay.portlet.expando.service.ExpandoRowLocalService expandoRowLocalService;
	@BeanReference(type = ExpandoRowPersistence.class)
	protected ExpandoRowPersistence expandoRowPersistence;
	private String _beanIdentifier;
	private ClassLoader _classLoader;
	private SPIDefinitionLocalServiceClpInvoker _clpInvoker = new SPIDefinitionLocalServiceClpInvoker();
}