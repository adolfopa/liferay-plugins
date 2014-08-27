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

package com.liferay.saml.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.ServiceContext;

import com.liferay.saml.model.SamlSpAuthRequest;

import java.util.List;

/**
 * The persistence utility for the saml sp auth request service. This utility wraps {@link SamlSpAuthRequestPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Mika Koivisto
 * @see SamlSpAuthRequestPersistence
 * @see SamlSpAuthRequestPersistenceImpl
 * @generated
 */
@ProviderType
public class SamlSpAuthRequestUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#clearCache(com.liferay.portal.model.BaseModel)
	 */
	public static void clearCache(SamlSpAuthRequest samlSpAuthRequest) {
		getPersistence().clearCache(samlSpAuthRequest);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<SamlSpAuthRequest> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<SamlSpAuthRequest> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<SamlSpAuthRequest> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<SamlSpAuthRequest> orderByComparator) {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel)
	 */
	public static SamlSpAuthRequest update(SamlSpAuthRequest samlSpAuthRequest) {
		return getPersistence().update(samlSpAuthRequest);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel, ServiceContext)
	 */
	public static SamlSpAuthRequest update(
		SamlSpAuthRequest samlSpAuthRequest, ServiceContext serviceContext) {
		return getPersistence().update(samlSpAuthRequest, serviceContext);
	}

	/**
	* Returns the saml sp auth request where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63; or throws a {@link com.liferay.saml.NoSuchSpAuthRequestException} if it could not be found.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @return the matching saml sp auth request
	* @throws com.liferay.saml.NoSuchSpAuthRequestException if a matching saml sp auth request could not be found
	*/
	public static com.liferay.saml.model.SamlSpAuthRequest findBySIEI_SSARK(
		java.lang.String samlIdpEntityId, java.lang.String samlSpAuthRequestKey)
		throws com.liferay.saml.NoSuchSpAuthRequestException {
		return getPersistence()
				   .findBySIEI_SSARK(samlIdpEntityId, samlSpAuthRequestKey);
	}

	/**
	* Returns the saml sp auth request where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @return the matching saml sp auth request, or <code>null</code> if a matching saml sp auth request could not be found
	*/
	public static com.liferay.saml.model.SamlSpAuthRequest fetchBySIEI_SSARK(
		java.lang.String samlIdpEntityId, java.lang.String samlSpAuthRequestKey) {
		return getPersistence()
				   .fetchBySIEI_SSARK(samlIdpEntityId, samlSpAuthRequestKey);
	}

	/**
	* Returns the saml sp auth request where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching saml sp auth request, or <code>null</code> if a matching saml sp auth request could not be found
	*/
	public static com.liferay.saml.model.SamlSpAuthRequest fetchBySIEI_SSARK(
		java.lang.String samlIdpEntityId,
		java.lang.String samlSpAuthRequestKey, boolean retrieveFromCache) {
		return getPersistence()
				   .fetchBySIEI_SSARK(samlIdpEntityId, samlSpAuthRequestKey,
			retrieveFromCache);
	}

	/**
	* Removes the saml sp auth request where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63; from the database.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @return the saml sp auth request that was removed
	*/
	public static com.liferay.saml.model.SamlSpAuthRequest removeBySIEI_SSARK(
		java.lang.String samlIdpEntityId, java.lang.String samlSpAuthRequestKey)
		throws com.liferay.saml.NoSuchSpAuthRequestException {
		return getPersistence()
				   .removeBySIEI_SSARK(samlIdpEntityId, samlSpAuthRequestKey);
	}

	/**
	* Returns the number of saml sp auth requests where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63;.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @return the number of matching saml sp auth requests
	*/
	public static int countBySIEI_SSARK(java.lang.String samlIdpEntityId,
		java.lang.String samlSpAuthRequestKey) {
		return getPersistence()
				   .countBySIEI_SSARK(samlIdpEntityId, samlSpAuthRequestKey);
	}

	/**
	* Caches the saml sp auth request in the entity cache if it is enabled.
	*
	* @param samlSpAuthRequest the saml sp auth request
	*/
	public static void cacheResult(
		com.liferay.saml.model.SamlSpAuthRequest samlSpAuthRequest) {
		getPersistence().cacheResult(samlSpAuthRequest);
	}

	/**
	* Caches the saml sp auth requests in the entity cache if it is enabled.
	*
	* @param samlSpAuthRequests the saml sp auth requests
	*/
	public static void cacheResult(
		java.util.List<com.liferay.saml.model.SamlSpAuthRequest> samlSpAuthRequests) {
		getPersistence().cacheResult(samlSpAuthRequests);
	}

	/**
	* Creates a new saml sp auth request with the primary key. Does not add the saml sp auth request to the database.
	*
	* @param samlSpAuthnRequestId the primary key for the new saml sp auth request
	* @return the new saml sp auth request
	*/
	public static com.liferay.saml.model.SamlSpAuthRequest create(
		long samlSpAuthnRequestId) {
		return getPersistence().create(samlSpAuthnRequestId);
	}

	/**
	* Removes the saml sp auth request with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param samlSpAuthnRequestId the primary key of the saml sp auth request
	* @return the saml sp auth request that was removed
	* @throws com.liferay.saml.NoSuchSpAuthRequestException if a saml sp auth request with the primary key could not be found
	*/
	public static com.liferay.saml.model.SamlSpAuthRequest remove(
		long samlSpAuthnRequestId)
		throws com.liferay.saml.NoSuchSpAuthRequestException {
		return getPersistence().remove(samlSpAuthnRequestId);
	}

	public static com.liferay.saml.model.SamlSpAuthRequest updateImpl(
		com.liferay.saml.model.SamlSpAuthRequest samlSpAuthRequest) {
		return getPersistence().updateImpl(samlSpAuthRequest);
	}

	/**
	* Returns the saml sp auth request with the primary key or throws a {@link com.liferay.saml.NoSuchSpAuthRequestException} if it could not be found.
	*
	* @param samlSpAuthnRequestId the primary key of the saml sp auth request
	* @return the saml sp auth request
	* @throws com.liferay.saml.NoSuchSpAuthRequestException if a saml sp auth request with the primary key could not be found
	*/
	public static com.liferay.saml.model.SamlSpAuthRequest findByPrimaryKey(
		long samlSpAuthnRequestId)
		throws com.liferay.saml.NoSuchSpAuthRequestException {
		return getPersistence().findByPrimaryKey(samlSpAuthnRequestId);
	}

	/**
	* Returns the saml sp auth request with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param samlSpAuthnRequestId the primary key of the saml sp auth request
	* @return the saml sp auth request, or <code>null</code> if a saml sp auth request with the primary key could not be found
	*/
	public static com.liferay.saml.model.SamlSpAuthRequest fetchByPrimaryKey(
		long samlSpAuthnRequestId) {
		return getPersistence().fetchByPrimaryKey(samlSpAuthnRequestId);
	}

	public static java.util.Map<java.io.Serializable, com.liferay.saml.model.SamlSpAuthRequest> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys) {
		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	* Returns all the saml sp auth requests.
	*
	* @return the saml sp auth requests
	*/
	public static java.util.List<com.liferay.saml.model.SamlSpAuthRequest> findAll() {
		return getPersistence().findAll();
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
	public static java.util.List<com.liferay.saml.model.SamlSpAuthRequest> findAll(
		int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	* Returns an ordered range of all the saml sp auth requests.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.saml.model.impl.SamlSpAuthRequestModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of saml sp auth requests
	* @param end the upper bound of the range of saml sp auth requests (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of saml sp auth requests
	*/
	public static java.util.List<com.liferay.saml.model.SamlSpAuthRequest> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.saml.model.SamlSpAuthRequest> orderByComparator) {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Removes all the saml sp auth requests from the database.
	*/
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of saml sp auth requests.
	*
	* @return the number of saml sp auth requests
	*/
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static SamlSpAuthRequestPersistence getPersistence() {
		if (_persistence == null) {
			_persistence = (SamlSpAuthRequestPersistence)PortletBeanLocatorUtil.locate(com.liferay.saml.service.ClpSerializer.getServletContextName(),
					SamlSpAuthRequestPersistence.class.getName());

			ReferenceRegistry.registerReference(SamlSpAuthRequestUtil.class,
				"_persistence");
		}

		return _persistence;
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	@Deprecated
	public void setPersistence(SamlSpAuthRequestPersistence persistence) {
	}

	private static SamlSpAuthRequestPersistence _persistence;
}