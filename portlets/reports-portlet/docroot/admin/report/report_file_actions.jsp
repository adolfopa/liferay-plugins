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

<%
Entry entry = (Entry)request.getAttribute("entry");

ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

String fileName = (String)row.getObject();
%>

<liferay-ui:icon-menu icon="<%= StringPool.BLANK %>" message="<%= StringPool.BLANK %>">
	<portlet:renderURL var="deliverReportURL" windowState="<%= WindowState.MAXIMIZED.toString() %>">
		<portlet:param name="mvcPath" value="/admin/report/deliver_report.jsp" />
		<portlet:param name="entryId" value="<%= String.valueOf(entry.getEntryId()) %>" />
		<portlet:param name="fileName" value="<%= fileName %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		iconCssClass="icon-signin"
		message="deliver-report"
		url="<%= deliverReportURL %>"
	/>

	<portlet:resourceURL id="download" var="downloadURL">
		<portlet:param name="entryId" value="<%= String.valueOf(entry.getEntryId()) %>" />
		<portlet:param name="fileName" value="<%= fileName %>" />
	</portlet:resourceURL>

	<liferay-ui:icon
		iconCssClass="icon-download"
		message="download"
		method="get"
		url="<%= downloadURL %>"
	/>

	<c:if test="<%= EntryPermission.contains(permissionChecker, entry.getEntryId(), ActionKeys.DELETE) %>">
		<portlet:actionURL name="deleteReport" var="deleteReportURL" windowState="<%= WindowState.MAXIMIZED.toString() %>">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="entryId" value="<%= String.valueOf(entry.getEntryId()) %>" />
			<portlet:param name="fileName" value="<%= fileName %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteReportURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>