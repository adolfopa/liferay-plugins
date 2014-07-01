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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcessLink;
import com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkLocalServiceUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;

import java.util.List;
import java.util.Locale;

/**
 * @author Marcellus Tavares
 */
public class KaleoProcessImpl extends KaleoProcessBaseImpl {

	public DDLRecordSet getDDLRecordSet() throws PortalException {
		return DDLRecordSetLocalServiceUtil.getRecordSet(getDDLRecordSetId());
	}

	public DDMTemplate getDDMTemplate() throws PortalException {
		return DDMTemplateLocalServiceUtil.getTemplate(getDDMTemplateId());
	}

	public String getDescription() throws PortalException {
		DDLRecordSet ddlRecordSet = getDDLRecordSet();

		return ddlRecordSet.getDescription();
	}

	public String getDescription(Locale locale) throws PortalException {
		DDLRecordSet ddlRecordSet = getDDLRecordSet();

		return ddlRecordSet.getDescription(locale);
	}

	public List<KaleoProcessLink> getKaleoProcessLinks() {
		return KaleoProcessLinkLocalServiceUtil.getKaleoProcessLinks(
			getKaleoProcessId());
	}

	public String getName() throws PortalException {
		DDLRecordSet ddlRecordSet = getDDLRecordSet();

		return ddlRecordSet.getName();
	}

	public String getName(Locale locale) throws PortalException {
		DDLRecordSet ddlRecordSet = getDDLRecordSet();

		return ddlRecordSet.getName(locale);
	}

	public String getWorkflowDefinition() {
		String workflowDefinition =
			getWorkflowDefinitionName() + "@" + getWorkflowDefinitionVersion();

		return workflowDefinition;
	}

}