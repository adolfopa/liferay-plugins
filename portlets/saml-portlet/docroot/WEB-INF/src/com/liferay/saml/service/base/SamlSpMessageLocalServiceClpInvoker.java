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

import com.liferay.saml.service.SamlSpMessageLocalServiceUtil;

import java.util.Arrays;

/**
 * @author Mika Koivisto
 * @generated
 */
@ProviderType
public class SamlSpMessageLocalServiceClpInvoker {
	public SamlSpMessageLocalServiceClpInvoker() {
		_methodName0 = "addSamlSpMessage";

		_methodParameterTypes0 = new String[] {
				"com.liferay.saml.model.SamlSpMessage"
			};

		_methodName1 = "createSamlSpMessage";

		_methodParameterTypes1 = new String[] { "long" };

		_methodName2 = "deleteSamlSpMessage";

		_methodParameterTypes2 = new String[] { "long" };

		_methodName3 = "deleteSamlSpMessage";

		_methodParameterTypes3 = new String[] {
				"com.liferay.saml.model.SamlSpMessage"
			};

		_methodName4 = "dynamicQuery";

		_methodParameterTypes4 = new String[] {  };

		_methodName5 = "dynamicQuery";

		_methodParameterTypes5 = new String[] {
				"com.liferay.portal.kernel.dao.orm.DynamicQuery"
			};

		_methodName6 = "dynamicQuery";

		_methodParameterTypes6 = new String[] {
				"com.liferay.portal.kernel.dao.orm.DynamicQuery", "int", "int"
			};

		_methodName7 = "dynamicQuery";

		_methodParameterTypes7 = new String[] {
				"com.liferay.portal.kernel.dao.orm.DynamicQuery", "int", "int",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};

		_methodName8 = "dynamicQueryCount";

		_methodParameterTypes8 = new String[] {
				"com.liferay.portal.kernel.dao.orm.DynamicQuery"
			};

		_methodName9 = "dynamicQueryCount";

		_methodParameterTypes9 = new String[] {
				"com.liferay.portal.kernel.dao.orm.DynamicQuery",
				"com.liferay.portal.kernel.dao.orm.Projection"
			};

		_methodName10 = "fetchSamlSpMessage";

		_methodParameterTypes10 = new String[] { "long" };

		_methodName11 = "getSamlSpMessage";

		_methodParameterTypes11 = new String[] { "long" };

		_methodName12 = "getActionableDynamicQuery";

		_methodParameterTypes12 = new String[] {  };

		_methodName14 = "deletePersistedModel";

		_methodParameterTypes14 = new String[] {
				"com.liferay.portal.model.PersistedModel"
			};

		_methodName15 = "getPersistedModel";

		_methodParameterTypes15 = new String[] { "java.io.Serializable" };

		_methodName16 = "getSamlSpMessages";

		_methodParameterTypes16 = new String[] { "int", "int" };

		_methodName17 = "getSamlSpMessagesCount";

		_methodParameterTypes17 = new String[] {  };

		_methodName18 = "updateSamlSpMessage";

		_methodParameterTypes18 = new String[] {
				"com.liferay.saml.model.SamlSpMessage"
			};

		_methodName65 = "getBeanIdentifier";

		_methodParameterTypes65 = new String[] {  };

		_methodName66 = "setBeanIdentifier";

		_methodParameterTypes66 = new String[] { "java.lang.String" };

		_methodName71 = "addSamlSpMessage";

		_methodParameterTypes71 = new String[] {
				"java.lang.String", "java.lang.String", "java.util.Date",
				"com.liferay.portal.service.ServiceContext"
			};

		_methodName72 = "fetchSamlSpMessage";

		_methodParameterTypes72 = new String[] {
				"java.lang.String", "java.lang.String"
			};

		_methodName73 = "getSamlSpMessage";

		_methodParameterTypes73 = new String[] {
				"java.lang.String", "java.lang.String"
			};
	}

	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		if (_methodName0.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes0, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.addSamlSpMessage((com.liferay.saml.model.SamlSpMessage)arguments[0]);
		}

		if (_methodName1.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes1, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.createSamlSpMessage(((Long)arguments[0]).longValue());
		}

		if (_methodName2.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes2, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.deleteSamlSpMessage(((Long)arguments[0]).longValue());
		}

		if (_methodName3.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes3, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.deleteSamlSpMessage((com.liferay.saml.model.SamlSpMessage)arguments[0]);
		}

		if (_methodName4.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes4, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.dynamicQuery();
		}

		if (_methodName5.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes5, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery)arguments[0]);
		}

		if (_methodName6.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes6, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery)arguments[0],
				((Integer)arguments[1]).intValue(),
				((Integer)arguments[2]).intValue());
		}

		if (_methodName7.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes7, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery)arguments[0],
				((Integer)arguments[1]).intValue(),
				((Integer)arguments[2]).intValue(),
				(com.liferay.portal.kernel.util.OrderByComparator<?>)arguments[3]);
		}

		if (_methodName8.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes8, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery)arguments[0]);
		}

		if (_methodName9.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes9, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery)arguments[0],
				(com.liferay.portal.kernel.dao.orm.Projection)arguments[1]);
		}

		if (_methodName10.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes10, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.fetchSamlSpMessage(((Long)arguments[0]).longValue());
		}

		if (_methodName11.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes11, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.getSamlSpMessage(((Long)arguments[0]).longValue());
		}

		if (_methodName12.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes12, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.getActionableDynamicQuery();
		}

		if (_methodName14.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes14, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.deletePersistedModel((com.liferay.portal.model.PersistedModel)arguments[0]);
		}

		if (_methodName15.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes15, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.getPersistedModel((java.io.Serializable)arguments[0]);
		}

		if (_methodName16.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes16, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.getSamlSpMessages(((Integer)arguments[0]).intValue(),
				((Integer)arguments[1]).intValue());
		}

		if (_methodName17.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes17, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.getSamlSpMessagesCount();
		}

		if (_methodName18.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes18, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.updateSamlSpMessage((com.liferay.saml.model.SamlSpMessage)arguments[0]);
		}

		if (_methodName65.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes65, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.getBeanIdentifier();
		}

		if (_methodName66.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes66, parameterTypes)) {
			SamlSpMessageLocalServiceUtil.setBeanIdentifier((java.lang.String)arguments[0]);

			return null;
		}

		if (_methodName71.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes71, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.addSamlSpMessage((java.lang.String)arguments[0],
				(java.lang.String)arguments[1], (java.util.Date)arguments[2],
				(com.liferay.portal.service.ServiceContext)arguments[3]);
		}

		if (_methodName72.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes72, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.fetchSamlSpMessage((java.lang.String)arguments[0],
				(java.lang.String)arguments[1]);
		}

		if (_methodName73.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes73, parameterTypes)) {
			return SamlSpMessageLocalServiceUtil.getSamlSpMessage((java.lang.String)arguments[0],
				(java.lang.String)arguments[1]);
		}

		throw new UnsupportedOperationException();
	}

	private String _methodName0;
	private String[] _methodParameterTypes0;
	private String _methodName1;
	private String[] _methodParameterTypes1;
	private String _methodName2;
	private String[] _methodParameterTypes2;
	private String _methodName3;
	private String[] _methodParameterTypes3;
	private String _methodName4;
	private String[] _methodParameterTypes4;
	private String _methodName5;
	private String[] _methodParameterTypes5;
	private String _methodName6;
	private String[] _methodParameterTypes6;
	private String _methodName7;
	private String[] _methodParameterTypes7;
	private String _methodName8;
	private String[] _methodParameterTypes8;
	private String _methodName9;
	private String[] _methodParameterTypes9;
	private String _methodName10;
	private String[] _methodParameterTypes10;
	private String _methodName11;
	private String[] _methodParameterTypes11;
	private String _methodName12;
	private String[] _methodParameterTypes12;
	private String _methodName14;
	private String[] _methodParameterTypes14;
	private String _methodName15;
	private String[] _methodParameterTypes15;
	private String _methodName16;
	private String[] _methodParameterTypes16;
	private String _methodName17;
	private String[] _methodParameterTypes17;
	private String _methodName18;
	private String[] _methodParameterTypes18;
	private String _methodName65;
	private String[] _methodParameterTypes65;
	private String _methodName66;
	private String[] _methodParameterTypes66;
	private String _methodName71;
	private String[] _methodParameterTypes71;
	private String _methodName72;
	private String[] _methodParameterTypes72;
	private String _methodName73;
	private String[] _methodParameterTypes73;
}