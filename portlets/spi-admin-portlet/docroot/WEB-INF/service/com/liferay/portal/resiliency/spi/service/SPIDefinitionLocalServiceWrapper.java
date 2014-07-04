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

package com.liferay.portal.resiliency.spi.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link SPIDefinitionLocalService}.
 *
 * @author Michael C. Han
 * @see SPIDefinitionLocalService
 * @generated
 */
public class SPIDefinitionLocalServiceWrapper
	implements SPIDefinitionLocalService,
		ServiceWrapper<SPIDefinitionLocalService> {
	public SPIDefinitionLocalServiceWrapper(
		SPIDefinitionLocalService spiDefinitionLocalService) {
		_spiDefinitionLocalService = spiDefinitionLocalService;
	}

	/**
	* Adds the s p i definition to the database. Also notifies the appropriate model listeners.
	*
	* @param spiDefinition the s p i definition
	* @return the s p i definition that was added
	*/
	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition addSPIDefinition(
		com.liferay.portal.resiliency.spi.model.SPIDefinition spiDefinition) {
		return _spiDefinitionLocalService.addSPIDefinition(spiDefinition);
	}

	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition addSPIDefinition(
		long userId, java.lang.String name, java.lang.String connectorAddress,
		int connectorPort, java.lang.String description,
		java.lang.String jvmArguments, java.lang.String portletIds,
		java.lang.String servletContextNames, java.lang.String typeSettings,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.addSPIDefinition(userId, name,
			connectorAddress, connectorPort, description, jvmArguments,
			portletIds, servletContextNames, typeSettings, serviceContext);
	}

	/**
	* Creates a new s p i definition with the primary key. Does not add the s p i definition to the database.
	*
	* @param spiDefinitionId the primary key for the new s p i definition
	* @return the new s p i definition
	*/
	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition createSPIDefinition(
		long spiDefinitionId) {
		return _spiDefinitionLocalService.createSPIDefinition(spiDefinitionId);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.model.PersistedModel deletePersistedModel(
		com.liferay.portal.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.deletePersistedModel(persistedModel);
	}

	/**
	* Deletes the s p i definition from the database. Also notifies the appropriate model listeners.
	*
	* @param spiDefinition the s p i definition
	* @return the s p i definition that was removed
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition deleteSPIDefinition(
		com.liferay.portal.resiliency.spi.model.SPIDefinition spiDefinition)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.deleteSPIDefinition(spiDefinition);
	}

	/**
	* Deletes the s p i definition with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param spiDefinitionId the primary key of the s p i definition
	* @return the s p i definition that was removed
	* @throws PortalException if a s p i definition with the primary key could not be found
	*/
	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition deleteSPIDefinition(
		long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.deleteSPIDefinition(spiDefinitionId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _spiDefinitionLocalService.dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _spiDefinitionLocalService.dynamicQuery(dynamicQuery);
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
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return _spiDefinitionLocalService.dynamicQuery(dynamicQuery, start, end);
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
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return _spiDefinitionLocalService.dynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _spiDefinitionLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows that match the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return _spiDefinitionLocalService.dynamicQueryCount(dynamicQuery,
			projection);
	}

	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition fetchSPIDefinition(
		long spiDefinitionId) {
		return _spiDefinitionLocalService.fetchSPIDefinition(spiDefinitionId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _spiDefinitionLocalService.getActionableDynamicQuery();
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _spiDefinitionLocalService.getBeanIdentifier();
	}

	@Override
	public com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public com.liferay.portal.kernel.util.Tuple getPortletIdsAndServletContextNames() {
		return _spiDefinitionLocalService.getPortletIdsAndServletContextNames();
	}

	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition getSPIDefinition(
		long companyId, java.lang.String name)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.getSPIDefinition(companyId, name);
	}

	/**
	* Returns the s p i definition with the primary key.
	*
	* @param spiDefinitionId the primary key of the s p i definition
	* @return the s p i definition
	* @throws PortalException if a s p i definition with the primary key could not be found
	*/
	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition getSPIDefinition(
		long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.getSPIDefinition(spiDefinitionId);
	}

	@Override
	public java.util.List<com.liferay.portal.resiliency.spi.model.SPIDefinition> getSPIDefinitions() {
		return _spiDefinitionLocalService.getSPIDefinitions();
	}

	@Override
	public java.util.List<com.liferay.portal.resiliency.spi.model.SPIDefinition> getSPIDefinitions(
		long companyId, int status) {
		return _spiDefinitionLocalService.getSPIDefinitions(companyId, status);
	}

	@Override
	public java.util.List<com.liferay.portal.resiliency.spi.model.SPIDefinition> getSPIDefinitions(
		long companyId, int[] statuses) {
		return _spiDefinitionLocalService.getSPIDefinitions(companyId, statuses);
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
	public java.util.List<com.liferay.portal.resiliency.spi.model.SPIDefinition> getSPIDefinitions(
		int start, int end) {
		return _spiDefinitionLocalService.getSPIDefinitions(start, end);
	}

	/**
	* Returns the number of s p i definitions.
	*
	* @return the number of s p i definitions
	*/
	@Override
	public int getSPIDefinitionsCount() {
		return _spiDefinitionLocalService.getSPIDefinitionsCount();
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _spiDefinitionLocalService.invokeMethod(name, parameterTypes,
			arguments);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_spiDefinitionLocalService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public void startSPI(long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		_spiDefinitionLocalService.startSPI(spiDefinitionId);
	}

	@Override
	public long startSPIinBackground(long userId, long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.startSPIinBackground(userId,
			spiDefinitionId);
	}

	@Override
	public long startSPIinBackground(long userId, long spiDefinitionId,
		boolean automatedRestart)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.startSPIinBackground(userId,
			spiDefinitionId, automatedRestart);
	}

	@Override
	public void stopSPI(long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		_spiDefinitionLocalService.stopSPI(spiDefinitionId);
	}

	@Override
	public long stopSPIinBackground(long userId, long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.stopSPIinBackground(userId,
			spiDefinitionId);
	}

	/**
	* Updates the s p i definition in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param spiDefinition the s p i definition
	* @return the s p i definition that was updated
	*/
	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition updateSPIDefinition(
		com.liferay.portal.resiliency.spi.model.SPIDefinition spiDefinition) {
		return _spiDefinitionLocalService.updateSPIDefinition(spiDefinition);
	}

	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition updateSPIDefinition(
		long spiDefinitionId, int status, java.lang.String statusMessage)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.updateSPIDefinition(spiDefinitionId,
			status, statusMessage);
	}

	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition updateSPIDefinition(
		long userId, long spiDefinitionId, java.lang.String connectorAddress,
		int connectorPort, java.lang.String description,
		java.lang.String jvmArguments, java.lang.String portletIds,
		java.lang.String servletContextNames, java.lang.String typeSettings,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.updateSPIDefinition(userId,
			spiDefinitionId, connectorAddress, connectorPort, description,
			jvmArguments, portletIds, servletContextNames, typeSettings,
			serviceContext);
	}

	@Override
	public com.liferay.portal.resiliency.spi.model.SPIDefinition updateTypeSettings(
		long userId, long spiDefinitionId, java.lang.String typeSettings,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _spiDefinitionLocalService.updateTypeSettings(userId,
			spiDefinitionId, typeSettings, serviceContext);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public SPIDefinitionLocalService getWrappedSPIDefinitionLocalService() {
		return _spiDefinitionLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedSPIDefinitionLocalService(
		SPIDefinitionLocalService spiDefinitionLocalService) {
		_spiDefinitionLocalService = spiDefinitionLocalService;
	}

	@Override
	public SPIDefinitionLocalService getWrappedService() {
		return _spiDefinitionLocalService;
	}

	@Override
	public void setWrappedService(
		SPIDefinitionLocalService spiDefinitionLocalService) {
		_spiDefinitionLocalService = spiDefinitionLocalService;
	}

	private SPIDefinitionLocalService _spiDefinitionLocalService;
}