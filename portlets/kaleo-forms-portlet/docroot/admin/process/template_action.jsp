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
	String taglibOnClick = "javascript:" + renderResponse.getNamespace() + "openDDMPortlet('"+ ddmTemplate.getTemplateId() +"');";
	%>

	<liferay-ui:icon
		iconCssClass="icon-edit"
		message="edit"
		onClick="<%= taglibOnClick %>"
		url="javascript:;"
	/>

	<%
	taglibOnClick = "Liferay.fire('" + renderResponse.getNamespace() + "chooseTemplate', {ddmtemplateid: " + ddmTemplate.getTemplateId() + ", name: '" + HtmlUtil.escapeAttribute(ddmTemplate.getName(locale)) + "'});";
	%>

	<liferay-ui:icon
		iconCssClass="icon-check"
		message="choose"
		onClick="<%= taglibOnClick %>"
		url="javascript:;"
	/>

	<liferay-portlet:actionURL portletName="<%= PortletKeys.DYNAMIC_DATA_MAPPING %>" var="deleteURL">
		<portlet:param name="struts_action" value="/dynamic_data_mapping/edit_template" />
		<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="templateId" value="<%= String.valueOf(ddmTemplate.getTemplateId()) %>" />
	</liferay-portlet:actionURL>

	<liferay-ui:icon-delete url="<%= deleteURL %>" />
</liferay-ui:icon-menu>