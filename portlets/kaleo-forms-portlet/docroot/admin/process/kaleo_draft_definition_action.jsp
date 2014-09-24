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
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

KaleoDraftDefinition kaleoDraftDefinition = (KaleoDraftDefinition)row.getObject();

String backURL = (String)row.getParameter("backURL");
%>

<liferay-ui:icon-menu icon="<%= StringPool.BLANK %>" message="<%= StringPool.BLANK %>" showWhenSingleIcon="<%= true %>">
	<portlet:renderURL var="editURL">
		<portlet:param name="mvcPath" value='<%= "/admin/process/edit_workflow.jsp" %>' />
		<portlet:param name="backURL" value="<%= backURL %>" />
		<portlet:param name="name" value="<%= kaleoDraftDefinition.getName() %>" />
		<portlet:param name="version" value="<%= String.valueOf(kaleoDraftDefinition.getVersion()) %>" />
		<portlet:param name="draftVersion" value="<%= String.valueOf(kaleoDraftDefinition.getDraftVersion()) %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		iconCssClass="icon-edit"
		message="edit"
		url="<%= editURL %>"
	/>

	<portlet:actionURL name="deleteKaleoDraftDefinition" var="deleteURL">
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="name" value="<%= kaleoDraftDefinition.getName() %>" />
		<portlet:param name="version" value="<%= String.valueOf(kaleoDraftDefinition.getVersion()) %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete
		url="<%= deleteURL %>"
	/>
</liferay-ui:icon-menu>