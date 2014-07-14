<%--
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
--%>

<%@ include file="/admin/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

long ddlRecordId = ParamUtil.getLong(request, "ddlRecordId");

DDLRecord ddlRecord = DDLRecordServiceUtil.getRecord(ddlRecordId);

DDLRecordSet ddlRecordSet = ddlRecord.getRecordSet();

KaleoProcess kaleoProcess = (KaleoProcess)request.getAttribute(WebKeys.KALEO_PROCESS);

long kaleoProcessId = BeanParamUtil.getLong(kaleoProcess, request, "kaleoProcessId");

String version = ParamUtil.getString(request, "version", DDLRecordConstants.VERSION_DEFAULT);

DDLRecordVersion ddlRecordVersion = ddlRecord.getRecordVersion(version);

DDLRecordVersion latestDDLRecordVersion = ddlRecord.getLatestRecordVersion();
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	title='<%= LanguageUtil.format(request, "view-x", kaleoProcess.getName(locale), false) %>'
/>

<c:if test="<%= ddlRecordVersion != null %>">
	<aui:model-context bean="<%= ddlRecordVersion %>" model="<%= DDLRecordVersion.class %>" />

	<aui:workflow-status model="<%= DDLRecord.class %>" status="<%= ddlRecordVersion.getStatus() %>" version="<%= ddlRecordVersion.getVersion() %>" />
</c:if>

<aui:fieldset>

	<%
	Fields fields = null;

	if (ddlRecordVersion != null) {
		fields = StorageEngineUtil.getFields(ddlRecordVersion.getDDMStorageId());
	}

	DDMStructure ddmStructure = ddlRecordSet.getDDMStructure();

	DDMTemplate ddmTemplate = kaleoProcess.getDDMTemplate();
	%>

	<liferay-ddm:html
		classNameId="<%= PortalUtil.getClassNameId(DDMStructure.class) %>"
		classPK="<%= ddmStructure.getStructureId() %>"
		fields="<%= fields %>"
		readOnly="<%= true %>"
		requestedLocale="<%= locale %>"
	/>

	<aui:button-row>
		<aui:button href="<%= redirect %>" name="cancelButton" type="cancel" />
	</aui:button-row>
</aui:fieldset>