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
String backURL = ParamUtil.getString(request, "backURL");

KaleoProcess kaleoProcess = (KaleoProcess)request.getAttribute(WebKeys.KALEO_PROCESS);

long kaleoProcessId = BeanParamUtil.getLong(kaleoProcess, request, "kaleoProcessId");

long groupId = BeanParamUtil.getLong(kaleoProcess, request, "groupId", scopeGroupId);
%>

<div class="kaleo-forms-display-portlet">
	<liferay-util:include page="/display/edit_request.jsp" servletContext="<%= application %>">
		<portlet:param name="redirect" value="<%= redirect %>" />
		<portlet:param name="backURL" value="<%= backURL %>" />
		<portlet:param name="kaleoProcessId" value="<%= String.valueOf(kaleoProcessId) %>" />
	</liferay-util:include>
</div>