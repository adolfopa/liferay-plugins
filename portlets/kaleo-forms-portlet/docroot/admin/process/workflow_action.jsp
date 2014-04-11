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

WorkflowDefinition workflowDefinition = (WorkflowDefinition)row.getObject();

String backURL = (String)row.getParameter("backURL");
%>

<liferay-ui:icon-menu>
	<portlet:renderURL var="editURL">
		<portlet:param name="mvcPath" value='<%= "/admin/process/edit_workflow.jsp" %>' />
		<portlet:param name="backURL" value="<%= backURL %>" />
		<portlet:param name="name" value="<%= workflowDefinition.getName() %>" />
		<portlet:param name="version" value="<%= String.valueOf(workflowDefinition.getVersion()) %>" />
		<portlet:param name="draftVersion" value="<%= String.valueOf(workflowDefinition.getVersion()) %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		image="../aui/edit"
		message="edit"
		url="<%= editURL %>"
	/>

	<%
	String taglibOnClick = "Liferay.fire('" + portletNamespace + "chooseWorkflow', {name: '" + workflowDefinition.getName() + "', version: '" + workflowDefinition.getVersion() + "'});";
	%>

	<liferay-ui:icon
		image="../aui/check"
		message="choose"
		onClick="<%= taglibOnClick %>"
		url="javascript:;"
	/>
</liferay-ui:icon-menu>