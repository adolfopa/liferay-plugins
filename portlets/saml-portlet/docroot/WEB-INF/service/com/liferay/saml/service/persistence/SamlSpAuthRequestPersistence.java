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

import com.liferay.portal.service.persistence.BasePersistence;

import com.liferay.saml.model.SamlSpAuthRequest;

/**
 * The persistence interface for the saml sp auth request service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Mika Koivisto
 * @see SamlSpAuthRequestPersistenceImpl
 * @see SamlSpAuthRequestUtil
 * @generated
 */
@ProviderType
public interface SamlSpAuthRequestPersistence extends BasePersistence<SamlSpAuthRequest> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link SamlSpAuthRequestUtil} to access the saml sp auth request persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns the saml sp auth request where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63; or throws a {@link com.liferay.saml.NoSuchSpAuthRequestException} if it could not be found.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @return the matching saml sp auth request
	* @throws com.liferay.saml.NoSuchSpAuthRequestException if a matching saml sp auth request could not be found
	*/
	public com.liferay.saml.model.SamlSpAuthRequest findBySIEI_SSARK(
		java.lang.String samlIdpEntityId, java.lang.String samlSpAuthRequestKey)
		throws com.liferay.saml.NoSuchSpAuthRequestException;

	/**
	* Returns the saml sp auth request where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @return the matching saml sp auth request, or <code>null</code> if a matching saml sp auth request could not be found
	*/
	public com.liferay.saml.model.SamlSpAuthRequest fetchBySIEI_SSARK(
		java.lang.String samlIdpEntityId, java.lang.String samlSpAuthRequestKey);

	/**
	* Returns the saml sp auth request where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching saml sp auth request, or <code>null</code> if a matching saml sp auth request could not be found
	*/
	public com.liferay.saml.model.SamlSpAuthRequest fetchBySIEI_SSARK(
		java.lang.String samlIdpEntityId,
		java.lang.String samlSpAuthRequestKey, boolean retrieveFromCache);

	/**
	* Removes the saml sp auth request where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63; from the database.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @return the saml sp auth request that was removed
	*/
	public com.liferay.saml.model.SamlSpAuthRequest removeBySIEI_SSARK(
		java.lang.String samlIdpEntityId, java.lang.String samlSpAuthRequestKey)
		throws com.liferay.saml.NoSuchSpAuthRequestException;

	/**
	* Returns the number of saml sp auth requests where samlIdpEntityId = &#63; and samlSpAuthRequestKey = &#63;.
	*
	* @param samlIdpEntityId the saml idp entity ID
	* @param samlSpAuthRequestKey the saml sp auth request key
	* @return the number of matching saml sp auth requests
	*/
	public int countBySIEI_SSARK(java.lang.String samlIdpEntityId,
		java.lang.String samlSpAuthRequestKey);

	/**
	* Caches the saml sp auth request in the entity cache if it is enabled.
	*
	* @param samlSpAuthRequest the saml sp auth request
	*/
	public void cacheResult(
		com.liferay.saml.model.SamlSpAuthRequest samlSpAuthRequest);

	/**
	* Caches the saml sp auth requests in the entity cache if it is enabled.
	*
	* @param samlSpAuthRequests the saml sp auth requests
	*/
	public void cacheResult(
		java.util.List<com.liferay.saml.model.SamlSpAuthRequest> samlSpAuthRequests);

	/**
	* Creates a new saml sp auth request with the primary key. Does not add the saml sp auth request to the database.
	*
	* @param samlSpAuthnRequestId the primary key for the new saml sp auth request
	* @return the new saml sp auth request
	*/
	public com.liferay.saml.model.SamlSpAuthRequest create(
		long samlSpAuthnRequestId);

	/**
	* Removes the saml sp auth request with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param samlSpAuthnRequestId the primary key of the saml sp auth request
	* @return the saml sp auth request that was removed
	* @throws com.liferay.saml.NoSuchSpAuthRequestException if a saml sp auth request with the primary key could not be found
	*/
	public com.liferay.saml.model.SamlSpAuthRequest remove(
		long samlSpAuthnRequestId)
		throws com.liferay.saml.NoSuchSpAuthRequestException;

	public com.liferay.saml.model.SamlSpAuthRequest updateImpl(
		com.liferay.saml.model.SamlSpAuthRequest samlSpAuthRequest);

	/**
	* Returns the saml sp auth request with the primary key or throws a {@link com.liferay.saml.NoSuchSpAuthRequestException} if it could not be found.
	*
	* @param samlSpAuthnRequestId the primary key of the saml sp auth request
	* @return the saml sp auth request
	* @throws com.liferay.saml.NoSuchSpAuthRequestException if a saml sp auth request with the primary key could not be found
	*/
	public com.liferay.saml.model.SamlSpAuthRequest findByPrimaryKey(
		long samlSpAuthnRequestId)
		throws com.liferay.saml.NoSuchSpAuthRequestException;

	/**
	* Returns the saml sp auth request with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param samlSpAuthnRequestId the primary key of the saml sp auth request
	* @return the saml sp auth request, or <code>null</code> if a saml sp auth request with the primary key could not be found
	*/
	public com.liferay.saml.model.SamlSpAuthRequest fetchByPrimaryKey(
		long samlSpAuthnRequestId);

	@Override
	public java.util.Map<java.io.Serializable, com.liferay.saml.model.SamlSpAuthRequest> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the saml sp auth requests.
	*
	* @return the saml sp auth requests
	*/
	public java.util.List<com.liferay.saml.model.SamlSpAuthRequest> findAll();

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
	public java.util.List<com.liferay.saml.model.SamlSpAuthRequest> findAll(
		int start, int end);

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
	public java.util.List<com.liferay.saml.model.SamlSpAuthRequest> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.saml.model.SamlSpAuthRequest> orderByComparator);

	/**
	* Removes all the saml sp auth requests from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of saml sp auth requests.
	*
	* @return the number of saml sp auth requests
	*/
	public int countAll();
}