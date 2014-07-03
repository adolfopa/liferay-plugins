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

package com.liferay.portal.workflow.kaleo.designer.service.base;

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
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.service.persistence.ClassNamePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.workflow.kaleo.designer.model.KaleoDraftDefinition;
import com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalService;
import com.liferay.portal.workflow.kaleo.designer.service.persistence.KaleoDraftDefinitionPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the kaleo draft definition local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.workflow.kaleo.designer.service.impl.KaleoDraftDefinitionLocalServiceImpl}.
 * </p>
 *
 * @author Eduardo Lundgren
 * @see com.liferay.portal.workflow.kaleo.designer.service.impl.KaleoDraftDefinitionLocalServiceImpl
 * @see com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalServiceUtil
 * @generated
 */
public abstract class KaleoDraftDefinitionLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements KaleoDraftDefinitionLocalService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalServiceUtil} to access the kaleo draft definition local service.
	 */

	/**
	 * Adds the kaleo draft definition to the database. Also notifies the appropriate model listeners.
	 *
	 * @param kaleoDraftDefinition the kaleo draft definition
	 * @return the kaleo draft definition that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public KaleoDraftDefinition addKaleoDraftDefinition(
		KaleoDraftDefinition kaleoDraftDefinition) {
		kaleoDraftDefinition.setNew(true);

		return kaleoDraftDefinitionPersistence.update(kaleoDraftDefinition);
	}

	/**
	 * Creates a new kaleo draft definition with the primary key. Does not add the kaleo draft definition to the database.
	 *
	 * @param kaleoDraftDefinitionId the primary key for the new kaleo draft definition
	 * @return the new kaleo draft definition
	 */
	@Override
	public KaleoDraftDefinition createKaleoDraftDefinition(
		long kaleoDraftDefinitionId) {
		return kaleoDraftDefinitionPersistence.create(kaleoDraftDefinitionId);
	}

	/**
	 * Deletes the kaleo draft definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param kaleoDraftDefinitionId the primary key of the kaleo draft definition
	 * @return the kaleo draft definition that was removed
	 * @throws PortalException if a kaleo draft definition with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public KaleoDraftDefinition deleteKaleoDraftDefinition(
		long kaleoDraftDefinitionId) throws PortalException {
		return kaleoDraftDefinitionPersistence.remove(kaleoDraftDefinitionId);
	}

	/**
	 * Deletes the kaleo draft definition from the database. Also notifies the appropriate model listeners.
	 *
	 * @param kaleoDraftDefinition the kaleo draft definition
	 * @return the kaleo draft definition that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public KaleoDraftDefinition deleteKaleoDraftDefinition(
		KaleoDraftDefinition kaleoDraftDefinition) {
		return kaleoDraftDefinitionPersistence.remove(kaleoDraftDefinition);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(KaleoDraftDefinition.class,
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
		return kaleoDraftDefinitionPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.designer.model.impl.KaleoDraftDefinitionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return kaleoDraftDefinitionPersistence.findWithDynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.designer.model.impl.KaleoDraftDefinitionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return kaleoDraftDefinitionPersistence.findWithDynamicQuery(dynamicQuery,
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
		return kaleoDraftDefinitionPersistence.countWithDynamicQuery(dynamicQuery);
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
		return kaleoDraftDefinitionPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public KaleoDraftDefinition fetchKaleoDraftDefinition(
		long kaleoDraftDefinitionId) {
		return kaleoDraftDefinitionPersistence.fetchByPrimaryKey(kaleoDraftDefinitionId);
	}

	/**
	 * Returns the kaleo draft definition with the primary key.
	 *
	 * @param kaleoDraftDefinitionId the primary key of the kaleo draft definition
	 * @return the kaleo draft definition
	 * @throws PortalException if a kaleo draft definition with the primary key could not be found
	 */
	@Override
	public KaleoDraftDefinition getKaleoDraftDefinition(
		long kaleoDraftDefinitionId) throws PortalException {
		return kaleoDraftDefinitionPersistence.findByPrimaryKey(kaleoDraftDefinitionId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(KaleoDraftDefinition.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"kaleoDraftDefinitionId");

		return actionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(KaleoDraftDefinition.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"kaleoDraftDefinitionId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return kaleoDraftDefinitionLocalService.deleteKaleoDraftDefinition((KaleoDraftDefinition)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return kaleoDraftDefinitionPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the kaleo draft definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.designer.model.impl.KaleoDraftDefinitionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @return the range of kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> getKaleoDraftDefinitions(int start,
		int end) {
		return kaleoDraftDefinitionPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of kaleo draft definitions.
	 *
	 * @return the number of kaleo draft definitions
	 */
	@Override
	public int getKaleoDraftDefinitionsCount() {
		return kaleoDraftDefinitionPersistence.countAll();
	}

	/**
	 * Updates the kaleo draft definition in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param kaleoDraftDefinition the kaleo draft definition
	 * @return the kaleo draft definition that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public KaleoDraftDefinition updateKaleoDraftDefinition(
		KaleoDraftDefinition kaleoDraftDefinition) {
		return kaleoDraftDefinitionPersistence.update(kaleoDraftDefinition);
	}

	/**
	 * Returns the kaleo draft definition local service.
	 *
	 * @return the kaleo draft definition local service
	 */
	public com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalService getKaleoDraftDefinitionLocalService() {
		return kaleoDraftDefinitionLocalService;
	}

	/**
	 * Sets the kaleo draft definition local service.
	 *
	 * @param kaleoDraftDefinitionLocalService the kaleo draft definition local service
	 */
	public void setKaleoDraftDefinitionLocalService(
		com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalService kaleoDraftDefinitionLocalService) {
		this.kaleoDraftDefinitionLocalService = kaleoDraftDefinitionLocalService;
	}

	/**
	 * Returns the kaleo draft definition remote service.
	 *
	 * @return the kaleo draft definition remote service
	 */
	public com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionService getKaleoDraftDefinitionService() {
		return kaleoDraftDefinitionService;
	}

	/**
	 * Sets the kaleo draft definition remote service.
	 *
	 * @param kaleoDraftDefinitionService the kaleo draft definition remote service
	 */
	public void setKaleoDraftDefinitionService(
		com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionService kaleoDraftDefinitionService) {
		this.kaleoDraftDefinitionService = kaleoDraftDefinitionService;
	}

	/**
	 * Returns the kaleo draft definition persistence.
	 *
	 * @return the kaleo draft definition persistence
	 */
	public KaleoDraftDefinitionPersistence getKaleoDraftDefinitionPersistence() {
		return kaleoDraftDefinitionPersistence;
	}

	/**
	 * Sets the kaleo draft definition persistence.
	 *
	 * @param kaleoDraftDefinitionPersistence the kaleo draft definition persistence
	 */
	public void setKaleoDraftDefinitionPersistence(
		KaleoDraftDefinitionPersistence kaleoDraftDefinitionPersistence) {
		this.kaleoDraftDefinitionPersistence = kaleoDraftDefinitionPersistence;
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

		PersistedModelLocalServiceRegistryUtil.register("com.liferay.portal.workflow.kaleo.designer.model.KaleoDraftDefinition",
			kaleoDraftDefinitionLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"com.liferay.portal.workflow.kaleo.designer.model.KaleoDraftDefinition");
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
		return KaleoDraftDefinition.class;
	}

	protected String getModelClassName() {
		return KaleoDraftDefinition.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = kaleoDraftDefinitionPersistence.getDataSource();

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

	@BeanReference(type = com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalService.class)
	protected com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalService kaleoDraftDefinitionLocalService;
	@BeanReference(type = com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionService.class)
	protected com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionService kaleoDraftDefinitionService;
	@BeanReference(type = KaleoDraftDefinitionPersistence.class)
	protected KaleoDraftDefinitionPersistence kaleoDraftDefinitionPersistence;
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
	private KaleoDraftDefinitionLocalServiceClpInvoker _clpInvoker = new KaleoDraftDefinitionLocalServiceClpInvoker();
}