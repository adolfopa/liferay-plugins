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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.security.ac.AccessControlled;
import com.liferay.portal.service.BaseService;
import com.liferay.portal.service.InvokableService;

/**
 * Provides the remote service interface for SPIDefinition. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Michael C. Han
 * @see SPIDefinitionServiceUtil
 * @see com.liferay.portal.resiliency.spi.service.base.SPIDefinitionServiceBaseImpl
 * @see com.liferay.portal.resiliency.spi.service.impl.SPIDefinitionServiceImpl
 * @generated
 */
@AccessControlled
@JSONWebService
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface SPIDefinitionService extends BaseService, InvokableService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link SPIDefinitionServiceUtil} to access the s p i definition remote service. Add custom service methods to {@link com.liferay.portal.resiliency.spi.service.impl.SPIDefinitionServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier();

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier);

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable;

	public com.liferay.portal.resiliency.spi.model.SPIDefinition addSPIDefinition(
		java.lang.String name, java.lang.String connectorAddress,
		int connectorPort, java.lang.String description,
		java.lang.String jvmArguments, java.lang.String portletIds,
		java.lang.String servletContextNames, java.lang.String typeSettings,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException;

	public com.liferay.portal.resiliency.spi.model.SPIDefinition deleteSPIDefinition(
		long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public com.liferay.portal.kernel.util.Tuple getPortletIdsAndServletContextNames()
		throws com.liferay.portal.kernel.exception.PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public com.liferay.portal.resiliency.spi.model.SPIDefinition getSPIDefinition(
		long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public com.liferay.portal.resiliency.spi.model.SPIDefinition getSPIDefinition(
		long companyId, java.lang.String name)
		throws com.liferay.portal.kernel.exception.PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public java.util.List<com.liferay.portal.resiliency.spi.model.SPIDefinition> getSPIDefinitions()
		throws com.liferay.portal.kernel.exception.PortalException;

	public void startSPI(long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException;

	public long startSPIinBackground(long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException;

	public void stopSPI(long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException;

	public long stopSPIinBackground(long spiDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException;

	public com.liferay.portal.resiliency.spi.model.SPIDefinition updateSPIDefinition(
		long spiDefinitionId, java.lang.String connectorAddress,
		int connectorPort, java.lang.String description,
		java.lang.String jvmArguments, java.lang.String portletIds,
		java.lang.String servletContextNames, java.lang.String typeSettings,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException;

	public com.liferay.portal.resiliency.spi.model.SPIDefinition updateTypeSettings(
		long userId, long spiDefinitionId, java.lang.String recoveryOptions,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException;
}