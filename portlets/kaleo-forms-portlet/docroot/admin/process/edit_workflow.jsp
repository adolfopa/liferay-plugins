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
String backURL = ParamUtil.getString(request, "backURL");

String name = ParamUtil.getString(request, "name");
int version = ParamUtil.getInteger(request, "version");
int draftVersion = ParamUtil.getInteger(request, "draftVersion");

Portlet kaleoDesignerPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), PortletKeys.KALEO_DESIGNER);
%>

<liferay-util:html-top>
	<link href="<%= PortalUtil.getStaticResourceURL(request, kaleoDesignerPortlet.getContextPath() + "/designer/css/main.css") %>" rel="stylesheet" type="text/css" />
</liferay-util:html-top>

<liferay-util:html-bottom>
	<script defer="defer" src="<%= PortalUtil.getStaticResourceURL(request, kaleoDesignerPortlet.getContextPath() + "/designer/js/main.js") %>" type="text/javascript"></script>
</liferay-util:html-bottom>

<liferay-util:include page="/designer/edit_kaleo_draft_definition.jsp" portletId="<%= PortletKeys.KALEO_DESIGNER %>">
	<portlet:param name="mvcPath" value="/admin/process/edit_workflow.jsp" />
	<portlet:param name="backURL" value="<%= backURL %>" />
	<portlet:param name="name" value="<%= name %>" />
	<portlet:param name="version" value="<%= String.valueOf(version) %>" />
	<portlet:param name="draftVersion" value="<%= String.valueOf(draftVersion) %>" />
</liferay-util:include>