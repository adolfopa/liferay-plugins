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

package com.liferay.saml.service.base;

import aQute.bnd.annotation.ProviderType;

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

import com.liferay.saml.model.SamlSpAuthRequest;
import com.liferay.saml.service.SamlSpAuthRequestLocalService;
import com.liferay.saml.service.persistence.SamlIdpSpConnectionPersistence;
import com.liferay.saml.service.persistence.SamlIdpSpSessionPersistence;
import com.liferay.saml.service.persistence.SamlIdpSsoSessionPersistence;
import com.liferay.saml.service.persistence.SamlSpAuthRequestPersistence;
import com.liferay.saml.service.persistence.SamlSpIdpConnectionPersistence;
import com.liferay.saml.service.persistence.SamlSpMessagePersistence;
import com.liferay.saml.service.persistence.SamlSpSessionPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the saml sp auth request local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.saml.service.impl.SamlSpAuthRequestLocalServiceImpl}.
 * </p>
 *
 * @author Mika Koivisto
 * @see com.liferay.saml.service.impl.SamlSpAuthRequestLocalServiceImpl
 * @see com.liferay.saml.service.SamlSpAuthRequestLocalServiceUtil
 * @generated
 */
@ProviderType
public abstract class SamlSpAuthRequestLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements SamlSpAuthRequestLocalService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.saml.service.SamlSpAuthRequestLocalServiceUtil} to access the saml sp auth request local service.
	 */

	/**
	 * Adds the saml sp auth request to the database. Also notifies the appropriate model listeners.
	 *
	 * @param samlSpAuthRequest the saml sp auth request
	 * @return the saml sp auth request that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SamlSpAuthRequest addSamlSpAuthRequest(
		SamlSpAuthRequest samlSpAuthRequest) {
		samlSpAuthRequest.setNew(true);

		return samlSpAuthRequestPersistence.update(samlSpAuthRequest);
	}

	/**
	 * Creates a new saml sp auth request with the primary key. Does not add the saml sp auth request to the database.
	 *
	 * @param samlSpAuthnRequestId the primary key for the new saml sp auth request
	 * @return the new saml sp auth request
	 */
	@Override
	public SamlSpAuthRequest createSamlSpAuthRequest(long samlSpAuthnRequestId) {
		return samlSpAuthRequestPersistence.create(samlSpAuthnRequestId);
	}

	/**
	 * Deletes the saml sp auth request with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param samlSpAuthnRequestId the primary key of the saml sp auth request
	 * @return the saml sp auth request that was removed
	 * @throws PortalException if a saml sp auth request with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SamlSpAuthRequest deleteSamlSpAuthRequest(long samlSpAuthnRequestId)
		throws PortalException {
		return samlSpAuthRequestPersistence.remove(samlSpAuthnRequestId);
	}

	/**
	 * Deletes the saml sp auth request from the database. Also notifies the appropriate model listeners.
	 *
	 * @param samlSpAuthRequest the saml sp auth request
	 * @return the saml sp auth request that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SamlSpAuthRequest deleteSamlSpAuthRequest(
		SamlSpAuthRequest samlSpAuthRequest) {
		return samlSpAuthRequestPersistence.remove(samlSpAuthRequest);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(SamlSpAuthRequest.class,
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
		return samlSpAuthRequestPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.saml.model.impl.SamlSpAuthRequestModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return samlSpAuthRequestPersistence.findWithDynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.saml.model.impl.SamlSpAuthRequestModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return samlSpAuthRequestPersistence.findWithDynamicQuery(dynamicQuery,
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
		return samlSpAuthRequestPersistence.countWithDynamicQuery(dynamicQuery);
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
		return samlSpAuthRequestPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public SamlSpAuthRequest fetchSamlSpAuthRequest(long samlSpAuthnRequestId) {
		return samlSpAuthRequestPersistence.fetchByPrimaryKey(samlSpAuthnRequestId);
	}

	/**
	 * Returns the saml sp auth request with the primary key.
	 *
	 * @param samlSpAuthnRequestId the primary key of the saml sp auth request
	 * @return the saml sp auth request
	 * @throws PortalException if a saml sp auth request with the primary key could not be found
	 */
	@Override
	public SamlSpAuthRequest getSamlSpAuthRequest(long samlSpAuthnRequestId)
		throws PortalException {
		return samlSpAuthRequestPersistence.findByPrimaryKey(samlSpAuthnRequestId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery = new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(com.liferay.saml.service.SamlSpAuthRequestLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(SamlSpAuthRequest.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("samlSpAuthnRequestId");

		return actionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {
		actionableDynamicQuery.setBaseLocalService(com.liferay.saml.service.SamlSpAuthRequestLocalServiceUtil.getService());
		actionableDynamicQuery.setClass(SamlSpAuthRequest.class);
		actionableDynamicQuery.setClassLoader(getClassLoader());

		actionableDynamicQuery.setPrimaryKeyPropertyName("samlSpAuthnRequestId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {
		return samlSpAuthRequestLocalService.deleteSamlSpAuthRequest((SamlSpAuthRequest)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {
		return samlSpAuthRequestPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the saml sp auth requests.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.saml.model.impl.SamlSpAuthRequestModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of saml sp auth requests
	 * @param end the upper bound of the range of saml sp auth requests (not inclusive)
	 * @return the range of saml sp auth requests
	 */
	@Override
	public List<SamlSpAuthRequest> getSamlSpAuthRequests(int start, int end) {
		return samlSpAuthRequestPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of saml sp auth requests.
	 *
	 * @return the number of saml sp auth requests
	 */
	@Override
	public int getSamlSpAuthRequestsCount() {
		return samlSpAuthRequestPersistence.countAll();
	}

	/**
	 * Updates the saml sp auth request in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param samlSpAuthRequest the saml sp auth request
	 * @return the saml sp auth request that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SamlSpAuthRequest updateSamlSpAuthRequest(
		SamlSpAuthRequest samlSpAuthRequest) {
		return samlSpAuthRequestPersistence.update(samlSpAuthRequest);
	}

	/**
	 * Returns the saml idp sp connection local service.
	 *
	 * @return the saml idp sp connection local service
	 */
	public com.liferay.saml.service.SamlIdpSpConnectionLocalService getSamlIdpSpConnectionLocalService() {
		return samlIdpSpConnectionLocalService;
	}

	/**
	 * Sets the saml idp sp connection local service.
	 *
	 * @param samlIdpSpConnectionLocalService the saml idp sp connection local service
	 */
	public void setSamlIdpSpConnectionLocalService(
		com.liferay.saml.service.SamlIdpSpConnectionLocalService samlIdpSpConnectionLocalService) {
		this.samlIdpSpConnectionLocalService = samlIdpSpConnectionLocalService;
	}

	/**
	 * Returns the saml idp sp connection persistence.
	 *
	 * @return the saml idp sp connection persistence
	 */
	public SamlIdpSpConnectionPersistence getSamlIdpSpConnectionPersistence() {
		return samlIdpSpConnectionPersistence;
	}

	/**
	 * Sets the saml idp sp connection persistence.
	 *
	 * @param samlIdpSpConnectionPersistence the saml idp sp connection persistence
	 */
	public void setSamlIdpSpConnectionPersistence(
		SamlIdpSpConnectionPersistence samlIdpSpConnectionPersistence) {
		this.samlIdpSpConnectionPersistence = samlIdpSpConnectionPersistence;
	}

	/**
	 * Returns the saml idp sp session local service.
	 *
	 * @return the saml idp sp session local service
	 */
	public com.liferay.saml.service.SamlIdpSpSessionLocalService getSamlIdpSpSessionLocalService() {
		return samlIdpSpSessionLocalService;
	}

	/**
	 * Sets the saml idp sp session local service.
	 *
	 * @param samlIdpSpSessionLocalService the saml idp sp session local service
	 */
	public void setSamlIdpSpSessionLocalService(
		com.liferay.saml.service.SamlIdpSpSessionLocalService samlIdpSpSessionLocalService) {
		this.samlIdpSpSessionLocalService = samlIdpSpSessionLocalService;
	}

	/**
	 * Returns the saml idp sp session persistence.
	 *
	 * @return the saml idp sp session persistence
	 */
	public SamlIdpSpSessionPersistence getSamlIdpSpSessionPersistence() {
		return samlIdpSpSessionPersistence;
	}

	/**
	 * Sets the saml idp sp session persistence.
	 *
	 * @param samlIdpSpSessionPersistence the saml idp sp session persistence
	 */
	public void setSamlIdpSpSessionPersistence(
		SamlIdpSpSessionPersistence samlIdpSpSessionPersistence) {
		this.samlIdpSpSessionPersistence = samlIdpSpSessionPersistence;
	}

	/**
	 * Returns the saml idp sso session local service.
	 *
	 * @return the saml idp sso session local service
	 */
	public com.liferay.saml.service.SamlIdpSsoSessionLocalService getSamlIdpSsoSessionLocalService() {
		return samlIdpSsoSessionLocalService;
	}

	/**
	 * Sets the saml idp sso session local service.
	 *
	 * @param samlIdpSsoSessionLocalService the saml idp sso session local service
	 */
	public void setSamlIdpSsoSessionLocalService(
		com.liferay.saml.service.SamlIdpSsoSessionLocalService samlIdpSsoSessionLocalService) {
		this.samlIdpSsoSessionLocalService = samlIdpSsoSessionLocalService;
	}

	/**
	 * Returns the saml idp sso session persistence.
	 *
	 * @return the saml idp sso session persistence
	 */
	public SamlIdpSsoSessionPersistence getSamlIdpSsoSessionPersistence() {
		return samlIdpSsoSessionPersistence;
	}

	/**
	 * Sets the saml idp sso session persistence.
	 *
	 * @param samlIdpSsoSessionPersistence the saml idp sso session persistence
	 */
	public void setSamlIdpSsoSessionPersistence(
		SamlIdpSsoSessionPersistence samlIdpSsoSessionPersistence) {
		this.samlIdpSsoSessionPersistence = samlIdpSsoSessionPersistence;
	}

	/**
	 * Returns the saml sp auth request local service.
	 *
	 * @return the saml sp auth request local service
	 */
	public com.liferay.saml.service.SamlSpAuthRequestLocalService getSamlSpAuthRequestLocalService() {
		return samlSpAuthRequestLocalService;
	}

	/**
	 * Sets the saml sp auth request local service.
	 *
	 * @param samlSpAuthRequestLocalService the saml sp auth request local service
	 */
	public void setSamlSpAuthRequestLocalService(
		com.liferay.saml.service.SamlSpAuthRequestLocalService samlSpAuthRequestLocalService) {
		this.samlSpAuthRequestLocalService = samlSpAuthRequestLocalService;
	}

	/**
	 * Returns the saml sp auth request persistence.
	 *
	 * @return the saml sp auth request persistence
	 */
	public SamlSpAuthRequestPersistence getSamlSpAuthRequestPersistence() {
		return samlSpAuthRequestPersistence;
	}

	/**
	 * Sets the saml sp auth request persistence.
	 *
	 * @param samlSpAuthRequestPersistence the saml sp auth request persistence
	 */
	public void setSamlSpAuthRequestPersistence(
		SamlSpAuthRequestPersistence samlSpAuthRequestPersistence) {
		this.samlSpAuthRequestPersistence = samlSpAuthRequestPersistence;
	}

	/**
	 * Returns the saml sp idp connection local service.
	 *
	 * @return the saml sp idp connection local service
	 */
	public com.liferay.saml.service.SamlSpIdpConnectionLocalService getSamlSpIdpConnectionLocalService() {
		return samlSpIdpConnectionLocalService;
	}

	/**
	 * Sets the saml sp idp connection local service.
	 *
	 * @param samlSpIdpConnectionLocalService the saml sp idp connection local service
	 */
	public void setSamlSpIdpConnectionLocalService(
		com.liferay.saml.service.SamlSpIdpConnectionLocalService samlSpIdpConnectionLocalService) {
		this.samlSpIdpConnectionLocalService = samlSpIdpConnectionLocalService;
	}

	/**
	 * Returns the saml sp idp connection persistence.
	 *
	 * @return the saml sp idp connection persistence
	 */
	public SamlSpIdpConnectionPersistence getSamlSpIdpConnectionPersistence() {
		return samlSpIdpConnectionPersistence;
	}

	/**
	 * Sets the saml sp idp connection persistence.
	 *
	 * @param samlSpIdpConnectionPersistence the saml sp idp connection persistence
	 */
	public void setSamlSpIdpConnectionPersistence(
		SamlSpIdpConnectionPersistence samlSpIdpConnectionPersistence) {
		this.samlSpIdpConnectionPersistence = samlSpIdpConnectionPersistence;
	}

	/**
	 * Returns the saml sp message local service.
	 *
	 * @return the saml sp message local service
	 */
	public com.liferay.saml.service.SamlSpMessageLocalService getSamlSpMessageLocalService() {
		return samlSpMessageLocalService;
	}

	/**
	 * Sets the saml sp message local service.
	 *
	 * @param samlSpMessageLocalService the saml sp message local service
	 */
	public void setSamlSpMessageLocalService(
		com.liferay.saml.service.SamlSpMessageLocalService samlSpMessageLocalService) {
		this.samlSpMessageLocalService = samlSpMessageLocalService;
	}

	/**
	 * Returns the saml sp message persistence.
	 *
	 * @return the saml sp message persistence
	 */
	public SamlSpMessagePersistence getSamlSpMessagePersistence() {
		return samlSpMessagePersistence;
	}

	/**
	 * Sets the saml sp message persistence.
	 *
	 * @param samlSpMessagePersistence the saml sp message persistence
	 */
	public void setSamlSpMessagePersistence(
		SamlSpMessagePersistence samlSpMessagePersistence) {
		this.samlSpMessagePersistence = samlSpMessagePersistence;
	}

	/**
	 * Returns the saml sp session local service.
	 *
	 * @return the saml sp session local service
	 */
	public com.liferay.saml.service.SamlSpSessionLocalService getSamlSpSessionLocalService() {
		return samlSpSessionLocalService;
	}

	/**
	 * Sets the saml sp session local service.
	 *
	 * @param samlSpSessionLocalService the saml sp session local service
	 */
	public void setSamlSpSessionLocalService(
		com.liferay.saml.service.SamlSpSessionLocalService samlSpSessionLocalService) {
		this.samlSpSessionLocalService = samlSpSessionLocalService;
	}

	/**
	 * Returns the saml sp session persistence.
	 *
	 * @return the saml sp session persistence
	 */
	public SamlSpSessionPersistence getSamlSpSessionPersistence() {
		return samlSpSessionPersistence;
	}

	/**
	 * Sets the saml sp session persistence.
	 *
	 * @param samlSpSessionPersistence the saml sp session persistence
	 */
	public void setSamlSpSessionPersistence(
		SamlSpSessionPersistence samlSpSessionPersistence) {
		this.samlSpSessionPersistence = samlSpSessionPersistence;
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

		PersistedModelLocalServiceRegistryUtil.register("com.liferay.saml.model.SamlSpAuthRequest",
			samlSpAuthRequestLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"com.liferay.saml.model.SamlSpAuthRequest");
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
		return SamlSpAuthRequest.class;
	}

	protected String getModelClassName() {
		return SamlSpAuthRequest.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = samlSpAuthRequestPersistence.getDataSource();

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

	@BeanReference(type = com.liferay.saml.service.SamlIdpSpConnectionLocalService.class)
	protected com.liferay.saml.service.SamlIdpSpConnectionLocalService samlIdpSpConnectionLocalService;
	@BeanReference(type = SamlIdpSpConnectionPersistence.class)
	protected SamlIdpSpConnectionPersistence samlIdpSpConnectionPersistence;
	@BeanReference(type = com.liferay.saml.service.SamlIdpSpSessionLocalService.class)
	protected com.liferay.saml.service.SamlIdpSpSessionLocalService samlIdpSpSessionLocalService;
	@BeanReference(type = SamlIdpSpSessionPersistence.class)
	protected SamlIdpSpSessionPersistence samlIdpSpSessionPersistence;
	@BeanReference(type = com.liferay.saml.service.SamlIdpSsoSessionLocalService.class)
	protected com.liferay.saml.service.SamlIdpSsoSessionLocalService samlIdpSsoSessionLocalService;
	@BeanReference(type = SamlIdpSsoSessionPersistence.class)
	protected SamlIdpSsoSessionPersistence samlIdpSsoSessionPersistence;
	@BeanReference(type = com.liferay.saml.service.SamlSpAuthRequestLocalService.class)
	protected com.liferay.saml.service.SamlSpAuthRequestLocalService samlSpAuthRequestLocalService;
	@BeanReference(type = SamlSpAuthRequestPersistence.class)
	protected SamlSpAuthRequestPersistence samlSpAuthRequestPersistence;
	@BeanReference(type = com.liferay.saml.service.SamlSpIdpConnectionLocalService.class)
	protected com.liferay.saml.service.SamlSpIdpConnectionLocalService samlSpIdpConnectionLocalService;
	@BeanReference(type = SamlSpIdpConnectionPersistence.class)
	protected SamlSpIdpConnectionPersistence samlSpIdpConnectionPersistence;
	@BeanReference(type = com.liferay.saml.service.SamlSpMessageLocalService.class)
	protected com.liferay.saml.service.SamlSpMessageLocalService samlSpMessageLocalService;
	@BeanReference(type = SamlSpMessagePersistence.class)
	protected SamlSpMessagePersistence samlSpMessagePersistence;
	@BeanReference(type = com.liferay.saml.service.SamlSpSessionLocalService.class)
	protected com.liferay.saml.service.SamlSpSessionLocalService samlSpSessionLocalService;
	@BeanReference(type = SamlSpSessionPersistence.class)
	protected SamlSpSessionPersistence samlSpSessionPersistence;
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
	private SamlSpAuthRequestLocalServiceClpInvoker _clpInvoker = new SamlSpAuthRequestLocalServiceClpInvoker();
}