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

package com.liferay.oauth.service.base;

import aQute.bnd.annotation.ProviderType;

import com.liferay.oauth.service.OAuthUserServiceUtil;

import java.util.Arrays;

/**
 * @author Ivica Cardic
 * @generated
 */
@ProviderType
public class OAuthUserServiceClpInvoker {
	public OAuthUserServiceClpInvoker() {
		_methodName30 = "getBeanIdentifier";

		_methodParameterTypes30 = new String[] {  };

		_methodName31 = "setBeanIdentifier";

		_methodParameterTypes31 = new String[] { "java.lang.String" };

		_methodName36 = "addOAuthUser";

		_methodParameterTypes36 = new String[] {
				"java.lang.String", "com.liferay.portal.service.ServiceContext"
			};

		_methodName37 = "deleteOAuthUser";

		_methodParameterTypes37 = new String[] { "long" };
	}

	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		if (_methodName30.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes30, parameterTypes)) {
			return OAuthUserServiceUtil.getBeanIdentifier();
		}

		if (_methodName31.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes31, parameterTypes)) {
			OAuthUserServiceUtil.setBeanIdentifier((java.lang.String)arguments[0]);

			return null;
		}

		if (_methodName36.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes36, parameterTypes)) {
			return OAuthUserServiceUtil.addOAuthUser((java.lang.String)arguments[0],
				(com.liferay.portal.service.ServiceContext)arguments[1]);
		}

		if (_methodName37.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes37, parameterTypes)) {
			return OAuthUserServiceUtil.deleteOAuthUser(((Long)arguments[0]).longValue());
		}

		throw new UnsupportedOperationException();
	}

	private String _methodName30;
	private String[] _methodParameterTypes30;
	private String _methodName31;
	private String[] _methodParameterTypes31;
	private String _methodName36;
	private String[] _methodParameterTypes36;
	private String _methodName37;
	private String[] _methodParameterTypes37;
}