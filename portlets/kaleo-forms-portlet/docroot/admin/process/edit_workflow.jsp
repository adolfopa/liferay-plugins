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
%>

<liferay-util:include page="/designer/edit_kaleo_draft_definition.jsp" portletId="2_WAR_kaleodesignerportlet">
	<portlet:param name="mvcPath" value="/admin/process/edit_workflow.jsp" />
	<portlet:param name="backURL" value="<%= backURL %>" />
	<portlet:param name="name" value="<%= name %>" />
	<portlet:param name="version" value="<%= String.valueOf(version) %>" />
	<portlet:param name="draftVersion" value="<%= String.valueOf(draftVersion) %>" />
</liferay-util:include>