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

DDMTemplate ddmTemplate = (DDMTemplate)row.getObject();
%>

<liferay-ui:icon-menu>

	<%
	String taglibOnClick = "javascript:" + portletNamespace + "openDDMPortlet('"+ ddmTemplate.getTemplateId() +"');";
	%>

	<liferay-ui:icon
		image="../aui/edit"
		message="edit"
		onClick="<%= taglibOnClick %>"
		url="javascript:;"
	/>

	<%
	taglibOnClick = "Liferay.fire('" + portletNamespace + "chooseTemplate', {ddmtemplateid: " + ddmTemplate.getTemplateId() + ", name: '" + HtmlUtil.escapeAttribute(ddmTemplate.getName(locale)) + "'});";
	%>

	<liferay-ui:icon
		image="../aui/check"
		message="choose"
		onClick="<%= taglibOnClick %>"
		url="javascript:;"
	/>
</liferay-ui:icon-menu>