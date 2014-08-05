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

package com.liferay.meeting.webex.service.base;

import com.liferay.meeting.webex.model.WebExAccount;
import com.liferay.meeting.webex.service.WebExAccountLocalService;
import com.liferay.meeting.webex.service.persistence.WebExAccountPersistence;
import com.liferay.meeting.webex.service.persistence.WebExSitePersistence;

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
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.ExportImportHelperUtil;
import com.liferay.portal.kernel.lar.ManifestSummary;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.PersistedModel;
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.service.persistence.ClassNamePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.service.persistence.ExpandoValuePersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the web ex account local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.meeting.webex.service.impl.WebExAccountLocalServiceImpl}.
 * </p>
 *
 * @author Anant Singh
 * @see com.liferay.meeting.webex.service.impl.WebExAccountLocalServiceImpl
 * @see com.liferay.meeting.webex.service.WebExAccountLocalServiceUtil
 * @generated
 */
public abstract class WebExAccountLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements WebExAccountLocalService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.meeting.webex.service.WebExAccountLocalServiceUtil} to access the web ex account local service.
	 */

	/**
	 * Adds the web ex account to the database. Also notifies the appropriate model listeners.
	 *
	 * @param webExAccount the web ex account
	 * @return the web ex account that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WebExAccount addWebExAccount(WebExAccount webExAccount) {
		webExAccount.setNew(true);

		return webExAccountPersistence.update(webExAccount);
	}

	/**
	 * Creates a new web ex account with the primary key. Does not add the web ex account to the database.
	 *
	 * @param webExAccountId the primary key for the new web ex account
	 * @return the new web ex account
	 */
	@Override
	public WebExAccount createWebExAccount(long webExAccountId) {
		return webExAccountPersistence.create(webExAccountId);
	}

	/**
	 * Deletes the web ex account with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param webExAccountId the primary key of the web ex account
	 * @return the web ex account that was removed
	 * @throws PortalException if a web ex account with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WebExAccount deleteWebExAccount(long webExAccountId)
		throws PortalException {
		return webExAccountPersistence.remove(webExAccountId);
	}

	/**
	 * Deletes the web ex account from the database. Also notifies the appropriate model listeners.
	 *
	 * @param webExAccount the web ex account
	 * @return the web ex account that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WebExAccount deleteWebExAccount(WebExAccount webExAccount)
		throws PortalException {
		return webExAccountPersistence.remove(webExAccount);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(WebExAccount.class,
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
		return webExAccountPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.meeting.webex.model.impl.WebExAccountModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return webExAccountPersistence.findWithDynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.meeting.webex.model.impl.WebExAccountModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return webExAccountPersistence.findWithDynamicQuery(dynamicQuery,
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
		return webExAccountPersistence.countWithDynamicQuery(dynamicQuery);
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
		return webExAccountPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public WebExAccount fetchWebExAccount(long webExAccountId) {
		return webExAccountPersistence.fetchByPrimaryKey(webExAccountId);
	}

	/**
	 * Returns the web ex account matching the UUID and group.
	 *
	 * @param uuid the web ex account's UUID
	 * @param groupId the primary key of the group
	 * @return the matching web ex account, or <code>null</code> if a matching web ex account could not be found
	 */
	@Override
	public WebExAccount fetchWebExAccountByUuidAndGroupId(String uuid,
		long groupId) {
		return webExAccountPersistence.fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the web ex account with the primary key.
	 *
	 * @param webExAccountId the primary key of the web ex account
	 * @return the web ex account
	 * @throws PortalException if a web ex account with the primary key could not be found
	 */
	@Override
	public WebExAccount getWebExAccount(long webExAccountId)
		throws PortalException {
		return webExAccountPersistence.findByPrimaryKey(webExAccountId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(com.liferay.meeting.webex.service.WebExAccountLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(WebExAccount.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("webExAccountId");

		return actionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(com.liferay.meeting.webex.service.WebExAccountLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(WebExAccount.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("webExAccountId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {
		final ExportActionableDynamicQuery exportActionableDynamicQuery = new ExportActionableDynamicQuery() {
				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary = portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(stagedModelType.toString(),
						modelAdditionCount);

					long modelDeletionCount = ExportImportHelperUtil.getModelDeletionCount(portletDataContext,
							stagedModelType);

					manifestSummary.addModelDeletionCount(stagedModelType.toString(),
						modelDeletionCount);

					return modelAdditionCount;
				}
			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(new ActionableDynamicQuery.AddCriteriaMethod() {
				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					portletDataContext.addDateRangeCriteria(dynamicQuery,
						"modifiedDate");
				}
			});

		exportActionableDynamicQuery.setCompanyId(portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setGroupId(portletDataContext.getScopeGroupId());

		exportActionableDynamicQuery.setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod() {
				@Override
				public void performAction(Object object)
					throws PortalException {
					WebExAccount stagedModel = (WebExAccount)object;

					StagedModelDataHandlerUtil.exportStagedModel(portletDataContext,
						stagedModel);
				}
			});
		exportActionableDynamicQuery.setStagedModelType(new StagedModelType(
				PortalUtil.getClassNameId(WebExAccount.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return webExAccountLocalService.deleteWebExAccount((WebExAccount)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return webExAccountPersistence.findByPrimaryKey(primaryKeyObj);
	}

	@Override
	public List<WebExAccount> getWebExAccountsByUuidAndCompanyId(String uuid,
		long companyId) {
		return webExAccountPersistence.findByUuid_C(uuid, companyId);
	}

	@Override
	public List<WebExAccount> getWebExAccountsByUuidAndCompanyId(String uuid,
		long companyId, int start, int end,
		OrderByComparator<WebExAccount> orderByComparator) {
		return webExAccountPersistence.findByUuid_C(uuid, companyId, start,
			end, orderByComparator);
	}

	/**
	 * Returns the web ex account matching the UUID and group.
	 *
	 * @param uuid the web ex account's UUID
	 * @param groupId the primary key of the group
	 * @return the matching web ex account
	 * @throws PortalException if a matching web ex account could not be found
	 */
	@Override
	public WebExAccount getWebExAccountByUuidAndGroupId(String uuid,
		long groupId) throws PortalException {
		return webExAccountPersistence.findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns a range of all the web ex accounts.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.meeting.webex.model.impl.WebExAccountModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of web ex accounts
	 * @param end the upper bound of the range of web ex accounts (not inclusive)
	 * @return the range of web ex accounts
	 */
	@Override
	public List<WebExAccount> getWebExAccounts(int start, int end) {
		return webExAccountPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of web ex accounts.
	 *
	 * @return the number of web ex accounts
	 */
	@Override
	public int getWebExAccountsCount() {
		return webExAccountPersistence.countAll();
	}

	/**
	 * Updates the web ex account in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param webExAccount the web ex account
	 * @return the web ex account that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WebExAccount updateWebExAccount(WebExAccount webExAccount) {
		return webExAccountPersistence.update(webExAccount);
	}

	/**
	 * Returns the web ex account local service.
	 *
	 * @return the web ex account local service
	 */
	public com.liferay.meeting.webex.service.WebExAccountLocalService getWebExAccountLocalService() {
		return webExAccountLocalService;
	}

	/**
	 * Sets the web ex account local service.
	 *
	 * @param webExAccountLocalService the web ex account local service
	 */
	public void setWebExAccountLocalService(
		com.liferay.meeting.webex.service.WebExAccountLocalService webExAccountLocalService) {
		this.webExAccountLocalService = webExAccountLocalService;
	}

	/**
	 * Returns the web ex account remote service.
	 *
	 * @return the web ex account remote service
	 */
	public com.liferay.meeting.webex.service.WebExAccountService getWebExAccountService() {
		return webExAccountService;
	}

	/**
	 * Sets the web ex account remote service.
	 *
	 * @param webExAccountService the web ex account remote service
	 */
	public void setWebExAccountService(
		com.liferay.meeting.webex.service.WebExAccountService webExAccountService) {
		this.webExAccountService = webExAccountService;
	}

	/**
	 * Returns the web ex account persistence.
	 *
	 * @return the web ex account persistence
	 */
	public WebExAccountPersistence getWebExAccountPersistence() {
		return webExAccountPersistence;
	}

	/**
	 * Sets the web ex account persistence.
	 *
	 * @param webExAccountPersistence the web ex account persistence
	 */
	public void setWebExAccountPersistence(
		WebExAccountPersistence webExAccountPersistence) {
		this.webExAccountPersistence = webExAccountPersistence;
	}

	/**
	 * Returns the web ex site local service.
	 *
	 * @return the web ex site local service
	 */
	public com.liferay.meeting.webex.service.WebExSiteLocalService getWebExSiteLocalService() {
		return webExSiteLocalService;
	}

	/**
	 * Sets the web ex site local service.
	 *
	 * @param webExSiteLocalService the web ex site local service
	 */
	public void setWebExSiteLocalService(
		com.liferay.meeting.webex.service.WebExSiteLocalService webExSiteLocalService) {
		this.webExSiteLocalService = webExSiteLocalService;
	}

	/**
	 * Returns the web ex site remote service.
	 *
	 * @return the web ex site remote service
	 */
	public com.liferay.meeting.webex.service.WebExSiteService getWebExSiteService() {
		return webExSiteService;
	}

	/**
	 * Sets the web ex site remote service.
	 *
	 * @param webExSiteService the web ex site remote service
	 */
	public void setWebExSiteService(
		com.liferay.meeting.webex.service.WebExSiteService webExSiteService) {
		this.webExSiteService = webExSiteService;
	}

	/**
	 * Returns the web ex site persistence.
	 *
	 * @return the web ex site persistence
	 */
	public WebExSitePersistence getWebExSitePersistence() {
		return webExSitePersistence;
	}

	/**
	 * Sets the web ex site persistence.
	 *
	 * @param webExSitePersistence the web ex site persistence
	 */
	public void setWebExSitePersistence(
		WebExSitePersistence webExSitePersistence) {
		this.webExSitePersistence = webExSitePersistence;
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

	/**
	 * Returns the expando value local service.
	 *
	 * @return the expando value local service
	 */
	public com.liferay.portlet.expando.service.ExpandoValueLocalService getExpandoValueLocalService() {
		return expandoValueLocalService;
	}

	/**
	 * Sets the expando value local service.
	 *
	 * @param expandoValueLocalService the expando value local service
	 */
	public void setExpandoValueLocalService(
		com.liferay.portlet.expando.service.ExpandoValueLocalService expandoValueLocalService) {
		this.expandoValueLocalService = expandoValueLocalService;
	}

	/**
	 * Returns the expando value remote service.
	 *
	 * @return the expando value remote service
	 */
	public com.liferay.portlet.expando.service.ExpandoValueService getExpandoValueService() {
		return expandoValueService;
	}

	/**
	 * Sets the expando value remote service.
	 *
	 * @param expandoValueService the expando value remote service
	 */
	public void setExpandoValueService(
		com.liferay.portlet.expando.service.ExpandoValueService expandoValueService) {
		this.expandoValueService = expandoValueService;
	}

	/**
	 * Returns the expando value persistence.
	 *
	 * @return the expando value persistence
	 */
	public ExpandoValuePersistence getExpandoValuePersistence() {
		return expandoValuePersistence;
	}

	/**
	 * Sets the expando value persistence.
	 *
	 * @param expandoValuePersistence the expando value persistence
	 */
	public void setExpandoValuePersistence(
		ExpandoValuePersistence expandoValuePersistence) {
		this.expandoValuePersistence = expandoValuePersistence;
	}

	public void afterPropertiesSet() {
		Class<?> clazz = getClass();

		_classLoader = clazz.getClassLoader();

		PersistedModelLocalServiceRegistryUtil.register("com.liferay.meeting.webex.model.WebExAccount",
			webExAccountLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"com.liferay.meeting.webex.model.WebExAccount");
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
		return WebExAccount.class;
	}

	protected String getModelClassName() {
		return WebExAccount.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = webExAccountPersistence.getDataSource();

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

	@BeanReference(type = com.liferay.meeting.webex.service.WebExAccountLocalService.class)
	protected com.liferay.meeting.webex.service.WebExAccountLocalService webExAccountLocalService;
	@BeanReference(type = com.liferay.meeting.webex.service.WebExAccountService.class)
	protected com.liferay.meeting.webex.service.WebExAccountService webExAccountService;
	@BeanReference(type = WebExAccountPersistence.class)
	protected WebExAccountPersistence webExAccountPersistence;
	@BeanReference(type = com.liferay.meeting.webex.service.WebExSiteLocalService.class)
	protected com.liferay.meeting.webex.service.WebExSiteLocalService webExSiteLocalService;
	@BeanReference(type = com.liferay.meeting.webex.service.WebExSiteService.class)
	protected com.liferay.meeting.webex.service.WebExSiteService webExSiteService;
	@BeanReference(type = WebExSitePersistence.class)
	protected WebExSitePersistence webExSitePersistence;
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
	@BeanReference(type = com.liferay.portlet.expando.service.ExpandoValueLocalService.class)
	protected com.liferay.portlet.expando.service.ExpandoValueLocalService expandoValueLocalService;
	@BeanReference(type = com.liferay.portlet.expando.service.ExpandoValueService.class)
	protected com.liferay.portlet.expando.service.ExpandoValueService expandoValueService;
	@BeanReference(type = ExpandoValuePersistence.class)
	protected ExpandoValuePersistence expandoValuePersistence;
	private String _beanIdentifier;
	private ClassLoader _classLoader;
	private WebExAccountLocalServiceClpInvoker _clpInvoker = new WebExAccountLocalServiceClpInvoker();
}