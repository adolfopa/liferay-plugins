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

DDMStructure ddmStructure = (DDMStructure)row.getObject();

String redirect = (String)row.getParameter("redirect");
%>

<liferay-ui:icon-menu>
	<portlet:renderURL var="editURL">
		<portlet:param name="mvcPath" value='<%= "/admin/process/edit_structure.jsp" %>' />
		<portlet:param name="redirect" value="<%= redirect %>" />
		<portlet:param name="classNameId" value="<%= String.valueOf(PortalUtil.getClassNameId(DDMStructure.class)) %>" />
		<portlet:param name="classPK" value="<%= String.valueOf(ddmStructure.getStructureId()) %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		image="../aui/edit"
		message="edit"
		url="<%= editURL %>"
	/>

	<%
	String taglibOnClick = "Liferay.fire('" + renderResponse.getNamespace() + "chooseDefinition', {ddmstructureid: " + ddmStructure.getStructureId() + ", name: '" + HtmlUtil.escapeAttribute(ddmStructure.getName(locale)) + "', node: this});";
	%>

	<liferay-ui:icon
		image="../aui/check"
		message="choose"
		onClick="<%= taglibOnClick %>"
		url="javascript:;"
	/>
</liferay-ui:icon-menu>