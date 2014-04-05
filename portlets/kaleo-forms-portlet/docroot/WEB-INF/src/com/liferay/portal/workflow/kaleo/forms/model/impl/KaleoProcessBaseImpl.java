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

package com.liferay.portal.workflow.kaleo.forms.model.impl;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess;
import com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLocalServiceUtil;

/**
 * The extended model base implementation for the KaleoProcess service. Represents a row in the &quot;KaleoProcess&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link KaleoProcessImpl}.
 * </p>
 *
 * @author Marcellus Tavares
 * @see KaleoProcessImpl
 * @see com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess
 * @generated
 */
public abstract class KaleoProcessBaseImpl extends KaleoProcessModelImpl
	implements KaleoProcess {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a kaleo process model instance should use the {@link KaleoProcess} interface instead.
	 */
	@Override
	public void persist() throws SystemException {
		if (this.isNew()) {
			KaleoProcessLocalServiceUtil.addKaleoProcess(this);
		}
		else {
			KaleoProcessLocalServiceUtil.updateKaleoProcess(this);
		}
	}
}