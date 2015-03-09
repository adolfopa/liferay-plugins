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

package com.liferay.portal.workflow.kaleo.forms.service.base;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessServiceUtil;

import java.util.Arrays;

/**
 * @author Marcellus Tavares
 * @generated
 */
@ProviderType
public class KaleoProcessServiceClpInvoker {
	public KaleoProcessServiceClpInvoker() {
		_methodName48 = "getBeanIdentifier";

		_methodParameterTypes48 = new String[] {  };

		_methodName49 = "setBeanIdentifier";

		_methodParameterTypes49 = new String[] { "java.lang.String" };

		_methodName54 = "addKaleoProcess";

		_methodParameterTypes54 = new String[] {
				"long", "long", "java.util.Map", "java.util.Map", "long",
				"java.lang.String", "int",
				"com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs",
				"com.liferay.portal.service.ServiceContext"
			};

		_methodName55 = "deleteKaleoProcess";

		_methodParameterTypes55 = new String[] { "long" };

		_methodName56 = "getKaleoProcess";

		_methodParameterTypes56 = new String[] { "long" };

		_methodName57 = "getKaleoProcesses";

		_methodParameterTypes57 = new String[] {
				"long", "int", "int",
				"com.liferay.portal.kernel.util.OrderByComparator"
			};

		_methodName58 = "getKaleoProcessesCount";

		_methodParameterTypes58 = new String[] { "long" };

		_methodName59 = "updateKaleoProcess";

		_methodParameterTypes59 = new String[] {
				"long", "long", "java.util.Map", "java.util.Map", "long",
				"java.lang.String", "int",
				"com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs",
				"com.liferay.portal.service.ServiceContext"
			};
	}

	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		if (_methodName48.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes48, parameterTypes)) {
			return KaleoProcessServiceUtil.getBeanIdentifier();
		}

		if (_methodName49.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes49, parameterTypes)) {
			KaleoProcessServiceUtil.setBeanIdentifier((java.lang.String)arguments[0]);

			return null;
		}

		if (_methodName54.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes54, parameterTypes)) {
			return KaleoProcessServiceUtil.addKaleoProcess(((Long)arguments[0]).longValue(),
				((Long)arguments[1]).longValue(),
				(java.util.Map<java.util.Locale, java.lang.String>)arguments[2],
				(java.util.Map<java.util.Locale, java.lang.String>)arguments[3],
				((Long)arguments[4]).longValue(),
				(java.lang.String)arguments[5],
				((Integer)arguments[6]).intValue(),
				(com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs)arguments[7],
				(com.liferay.portal.service.ServiceContext)arguments[8]);
		}

		if (_methodName55.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes55, parameterTypes)) {
			return KaleoProcessServiceUtil.deleteKaleoProcess(((Long)arguments[0]).longValue());
		}

		if (_methodName56.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes56, parameterTypes)) {
			return KaleoProcessServiceUtil.getKaleoProcess(((Long)arguments[0]).longValue());
		}

		if (_methodName57.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes57, parameterTypes)) {
			return KaleoProcessServiceUtil.getKaleoProcesses(((Long)arguments[0]).longValue(),
				((Integer)arguments[1]).intValue(),
				((Integer)arguments[2]).intValue(),
				(com.liferay.portal.kernel.util.OrderByComparator)arguments[3]);
		}

		if (_methodName58.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes58, parameterTypes)) {
			return KaleoProcessServiceUtil.getKaleoProcessesCount(((Long)arguments[0]).longValue());
		}

		if (_methodName59.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes59, parameterTypes)) {
			return KaleoProcessServiceUtil.updateKaleoProcess(((Long)arguments[0]).longValue(),
				((Long)arguments[1]).longValue(),
				(java.util.Map<java.util.Locale, java.lang.String>)arguments[2],
				(java.util.Map<java.util.Locale, java.lang.String>)arguments[3],
				((Long)arguments[4]).longValue(),
				(java.lang.String)arguments[5],
				((Integer)arguments[6]).intValue(),
				(com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs)arguments[7],
				(com.liferay.portal.service.ServiceContext)arguments[8]);
		}

		throw new UnsupportedOperationException();
	}

	private String _methodName48;
	private String[] _methodParameterTypes48;
	private String _methodName49;
	private String[] _methodParameterTypes49;
	private String _methodName54;
	private String[] _methodParameterTypes54;
	private String _methodName55;
	private String[] _methodParameterTypes55;
	private String _methodName56;
	private String[] _methodParameterTypes56;
	private String _methodName57;
	private String[] _methodParameterTypes57;
	private String _methodName58;
	private String[] _methodParameterTypes58;
	private String _methodName59;
	private String[] _methodParameterTypes59;
}