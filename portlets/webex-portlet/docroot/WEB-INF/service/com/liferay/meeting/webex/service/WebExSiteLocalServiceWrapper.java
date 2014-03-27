/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.meeting.webex.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link WebExSiteLocalService}.
 *
 * @author Anant Singh
 * @see WebExSiteLocalService
 * @generated
 */
public class WebExSiteLocalServiceWrapper implements WebExSiteLocalService,
	ServiceWrapper<WebExSiteLocalService> {
	public WebExSiteLocalServiceWrapper(
		WebExSiteLocalService webExSiteLocalService) {
		_webExSiteLocalService = webExSiteLocalService;
	}

	/**
	* Adds the web ex site to the database. Also notifies the appropriate model listeners.
	*
	* @param webExSite the web ex site
	* @return the web ex site that was added
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite addWebExSite(
		com.liferay.meeting.webex.model.WebExSite webExSite)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.addWebExSite(webExSite);
	}

	/**
	* Creates a new web ex site with the primary key. Does not add the web ex site to the database.
	*
	* @param webExSiteId the primary key for the new web ex site
	* @return the new web ex site
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite createWebExSite(
		long webExSiteId) {
		return _webExSiteLocalService.createWebExSite(webExSiteId);
	}

	/**
	* Deletes the web ex site with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param webExSiteId the primary key of the web ex site
	* @return the web ex site that was removed
	* @throws PortalException if a web ex site with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite deleteWebExSite(
		long webExSiteId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.deleteWebExSite(webExSiteId);
	}

	/**
	* Deletes the web ex site from the database. Also notifies the appropriate model listeners.
	*
	* @param webExSite the web ex site
	* @return the web ex site that was removed
	* @throws PortalException
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite deleteWebExSite(
		com.liferay.meeting.webex.model.WebExSite webExSite)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.deleteWebExSite(webExSite);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _webExSiteLocalService.dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	* @throws SystemException if a system exception occurred
	*/
	@Override
	@SuppressWarnings("rawtypes")
	public java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.meeting.webex.model.impl.WebExSiteModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@Override
	@SuppressWarnings("rawtypes")
	public java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.meeting.webex.model.impl.WebExSiteModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@Override
	@SuppressWarnings("rawtypes")
	public java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.dynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.dynamicQueryCount(dynamicQuery, projection);
	}

	@Override
	public com.liferay.meeting.webex.model.WebExSite fetchWebExSite(
		long webExSiteId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.fetchWebExSite(webExSiteId);
	}

	/**
	* Returns the web ex site with the matching UUID and company.
	*
	* @param uuid the web ex site's UUID
	* @param companyId the primary key of the company
	* @return the matching web ex site, or <code>null</code> if a matching web ex site could not be found
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite fetchWebExSiteByUuidAndCompanyId(
		java.lang.String uuid, long companyId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.fetchWebExSiteByUuidAndCompanyId(uuid,
			companyId);
	}

	/**
	* Returns the web ex site matching the UUID and group.
	*
	* @param uuid the web ex site's UUID
	* @param groupId the primary key of the group
	* @return the matching web ex site, or <code>null</code> if a matching web ex site could not be found
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite fetchWebExSiteByUuidAndGroupId(
		java.lang.String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.fetchWebExSiteByUuidAndGroupId(uuid,
			groupId);
	}

	/**
	* Returns the web ex site with the primary key.
	*
	* @param webExSiteId the primary key of the web ex site
	* @return the web ex site
	* @throws PortalException if a web ex site with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite getWebExSite(
		long webExSiteId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.getWebExSite(webExSiteId);
	}

	@Override
	public com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Returns the web ex site with the matching UUID and company.
	*
	* @param uuid the web ex site's UUID
	* @param companyId the primary key of the company
	* @return the matching web ex site
	* @throws PortalException if a matching web ex site could not be found
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite getWebExSiteByUuidAndCompanyId(
		java.lang.String uuid, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.getWebExSiteByUuidAndCompanyId(uuid,
			companyId);
	}

	/**
	* Returns the web ex site matching the UUID and group.
	*
	* @param uuid the web ex site's UUID
	* @param groupId the primary key of the group
	* @return the matching web ex site
	* @throws PortalException if a matching web ex site could not be found
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite getWebExSiteByUuidAndGroupId(
		java.lang.String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.getWebExSiteByUuidAndGroupId(uuid, groupId);
	}

	/**
	* Returns a range of all the web ex sites.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.meeting.webex.model.impl.WebExSiteModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of web ex sites
	* @param end the upper bound of the range of web ex sites (not inclusive)
	* @return the range of web ex sites
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public java.util.List<com.liferay.meeting.webex.model.WebExSite> getWebExSites(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.getWebExSites(start, end);
	}

	/**
	* Returns the number of web ex sites.
	*
	* @return the number of web ex sites
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public int getWebExSitesCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.getWebExSitesCount();
	}

	/**
	* Updates the web ex site in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param webExSite the web ex site
	* @return the web ex site that was updated
	* @throws SystemException if a system exception occurred
	*/
	@Override
	public com.liferay.meeting.webex.model.WebExSite updateWebExSite(
		com.liferay.meeting.webex.model.WebExSite webExSite)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.updateWebExSite(webExSite);
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _webExSiteLocalService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_webExSiteLocalService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _webExSiteLocalService.invokeMethod(name, parameterTypes,
			arguments);
	}

	@Override
	public void addWebExSite(long userId, long groupId, java.lang.String name,
		java.lang.String apiURL, java.lang.String login,
		java.lang.String password, java.lang.String partnerKey, long siteKey,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_webExSiteLocalService.addWebExSite(userId, groupId, name, apiURL,
			login, password, partnerKey, siteKey, serviceContext);
	}

	@Override
	public com.liferay.meeting.webex.model.WebExSite fetchSiteKeyWebExSite(
		long siteKey)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.fetchSiteKeyWebExSite(siteKey);
	}

	@Override
	public com.liferay.meeting.webex.model.WebExSite getSiteKeyWebExSite(
		long siteKey)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.getSiteKeyWebExSite(siteKey);
	}

	@Override
	public java.util.List<com.liferay.meeting.webex.model.WebExSite> getWebExSites(
		long groupId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.getWebExSites(groupId, start, end);
	}

	@Override
	public java.util.List<com.liferay.meeting.webex.model.WebExSite> getWebExSites(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator obc)
		throws com.liferay.portal.kernel.exception.SystemException {
		return _webExSiteLocalService.getWebExSites(groupId, start, end, obc);
	}

	@Override
	public void updateWebExSite(long webExSiteId, java.lang.String apiURL,
		java.lang.String login, java.lang.String password,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_webExSiteLocalService.updateWebExSite(webExSiteId, apiURL, login,
			password, serviceContext);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public WebExSiteLocalService getWrappedWebExSiteLocalService() {
		return _webExSiteLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedWebExSiteLocalService(
		WebExSiteLocalService webExSiteLocalService) {
		_webExSiteLocalService = webExSiteLocalService;
	}

	@Override
	public WebExSiteLocalService getWrappedService() {
		return _webExSiteLocalService;
	}

	@Override
	public void setWrappedService(WebExSiteLocalService webExSiteLocalService) {
		_webExSiteLocalService = webExSiteLocalService;
	}

	private WebExSiteLocalService _webExSiteLocalService;
}