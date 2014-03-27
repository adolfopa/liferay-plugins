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

package com.liferay.oauth.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link OAuthUserService}.
 *
 * @author Ivica Cardic
 * @see OAuthUserService
 * @generated
 */
public class OAuthUserServiceWrapper implements OAuthUserService,
	ServiceWrapper<OAuthUserService> {
	public OAuthUserServiceWrapper(OAuthUserService oAuthUserService) {
		_oAuthUserService = oAuthUserService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _oAuthUserService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_oAuthUserService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _oAuthUserService.invokeMethod(name, parameterTypes, arguments);
	}

	@Override
	public com.liferay.oauth.model.OAuthUser deleteOAuthUser(
		long oAuthApplicationId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _oAuthUserService.deleteOAuthUser(oAuthApplicationId);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public OAuthUserService getWrappedOAuthUserService() {
		return _oAuthUserService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedOAuthUserService(OAuthUserService oAuthUserService) {
		_oAuthUserService = oAuthUserService;
	}

	@Override
	public OAuthUserService getWrappedService() {
		return _oAuthUserService;
	}

	@Override
	public void setWrappedService(OAuthUserService oAuthUserService) {
		_oAuthUserService = oAuthUserService;
	}

	private OAuthUserService _oAuthUserService;
}