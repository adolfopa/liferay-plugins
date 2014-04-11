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

<%@ include file="/init.jsp" %>

<%@ page import="com.liferay.portal.kernel.dao.search.DisplayTerms" %><%@
page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil" %><%@
page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %><%@
page import="com.liferay.portal.kernel.util.CharPool" %><%@
page import="com.liferay.portal.kernel.util.HttpUtil" %><%@
page import="com.liferay.portal.kernel.util.ObjectValuePair" %><%@
page import="com.liferay.portal.kernel.util.StringBundler" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.workflow.WorkflowDefinition" %><%@
page import="com.liferay.portal.kernel.workflow.WorkflowDefinitionManagerUtil" %><%@
page import="com.liferay.portal.util.PortletKeys" %><%@
page import="com.liferay.portal.workflow.kaleo.designer.model.KaleoDraftDefinition" %><%@
page import="com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionLocalServiceUtil" %><%@
page import="com.liferay.portal.workflow.kaleo.forms.KaleoProcessDDMTemplateIdException" %><%@
page import="com.liferay.portal.workflow.kaleo.forms.model.KaleoProcessLink" %><%@
page import="com.liferay.portal.workflow.kaleo.forms.search.KaleoProcessSearch" %><%@
page import="com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessServiceUtil" %><%@
page import="com.liferay.portal.workflow.kaleo.forms.service.permission.KaleoFormsPermission" %><%@
page import="com.liferay.portal.workflow.kaleo.forms.util.KaleoFormsUtil" %><%@
page import="com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairsSerializer" %><%@
page import="com.liferay.portlet.PortletURLFactoryUtil" %><%@
page import="com.liferay.portlet.dynamicdatalists.RecordSetDDMStructureIdException" %><%@
page import="com.liferay.portlet.dynamicdatalists.RecordSetNameException" %><%@
page import="com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalServiceUtil" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants" %><%@
page import="com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil" %><%@
page import="com.liferay.portlet.dynamicdatamapping.service.DDMStructureServiceUtil" %><%@
page import="com.liferay.portlet.dynamicdatamapping.storage.FieldConstants" %><%@
page import="com.liferay.portlet.dynamicdatamapping.storage.StorageType" %><%@
page import="com.liferay.portlet.dynamicdatamapping.util.DDMDisplay" %><%@
page import="com.liferay.portlet.dynamicdatamapping.util.DDMDisplayRegistryUtil" %>

<%@ page import="javax.portlet.PortletRequest" %>

<%
String refererPortletName = ParamUtil.getString(request, "refererPortletName", portletName);
String refererWebDAVToken = ParamUtil.getString(request, "refererWebDAVToken", portletConfig.getInitParameter("refererWebDAVToken"));
String scopeTitle = ParamUtil.getString(request, "scopeTitle");
boolean showGlobalScope = ParamUtil.getBoolean(request, "showGlobalScope");
boolean showManageTemplates = ParamUtil.getBoolean(request, "showManageTemplates", true);
boolean showToolbar = ParamUtil.getBoolean(request, "showToolbar", true);

DDMDisplay ddmDisplay = DDMDisplayRegistryUtil.getDDMDisplay(portletDisplay.getId());

long scopeClassNameId = PortalUtil.getClassNameId(ddmDisplay.getStructureType());

String scopeAvailableFields = ddmDisplay.getAvailableFields();
String scopeStorageType = ddmDisplay.getStorageType();
String scopeTemplateType = ddmDisplay.getTemplateType();

String storageTypeValue = StringPool.BLANK;

if (scopeStorageType.equals("expando")) {
	storageTypeValue = StorageType.EXPANDO.getValue();
}
else if (scopeStorageType.equals("xml")) {
	storageTypeValue = StorageType.XML.getValue();
}

String templateTypeValue = StringPool.BLANK;

if (scopeTemplateType.equals(DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY)) {
	templateTypeValue = DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY;
}
else if (scopeTemplateType.equals(DDMTemplateConstants.TEMPLATE_TYPE_FORM)) {
	templateTypeValue = DDMTemplateConstants.TEMPLATE_TYPE_FORM;
}

String portletNamespace = renderResponse.getNamespace();
%>