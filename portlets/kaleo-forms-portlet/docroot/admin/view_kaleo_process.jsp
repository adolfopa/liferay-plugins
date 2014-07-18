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

KaleoProcess kaleoProcess = (KaleoProcess)request.getAttribute(WebKeys.KALEO_PROCESS);

long kaleoProcessId = BeanParamUtil.getLong(kaleoProcess, request, "kaleoProcessId");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("mvcPath", "/admin/view_kaleo_process.jsp");
portletURL.setParameter("backURL", backURL);
portletURL.setParameter("kaleoProcessId", String.valueOf(kaleoProcessId));
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="<%= kaleoProcess.getName(locale) %>"
/>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">

	<%
	List<String> headerNames = new ArrayList<String>();

	DDLRecordSet ddlRecordSet = kaleoProcess.getDDLRecordSet();

	DDMStructure ddmStructure = ddlRecordSet.getDDMStructure();

	List<DDMFormField> ddmFormfields = ddmStructure.getDDMFormFields(false);

	for (DDMFormField ddmFormField : ddmFormfields) {
		if (ddmStructure.isFieldPrivate(ddmFormField.getName())) {
			continue;
		}

		LocalizedValue label = ddmFormField.getLabel();

		headerNames.add(label.getValue(locale));
	}

	headerNames.add("status");
	headerNames.add("modified-date");
	headerNames.add("author");
	headerNames.add(StringPool.BLANK);
	%>

	<liferay-ui:search-container
		searchContainer='<%= new SearchContainer(renderRequest, new DisplayTerms(request), null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, portletURL, headerNames, "no-records-were-found") %>'
	>

		<aui:nav-bar>
			<aui:nav cssClass="navbar-nav" searchContainer="<%= searchContainer %>">
				<portlet:renderURL var="submitURL">
					<portlet:param name="mvcPath" value="/admin/edit_request.jsp" />
					<portlet:param name="redirect" value="<%= currentURL %>" />
					<portlet:param name="backURL" value="<%= currentURL %>" />
					<portlet:param name="kaleoProcessId" value="<%= String.valueOf(kaleoProcessId) %>" />
				</portlet:renderURL>

				<aui:nav-item href="<%= submitURL %>" iconCssClass="icon-plus" label='<%= LanguageUtil.format(request, "submit-new-x", HtmlUtil.escape(kaleoProcess.getName(locale)), false) %>' />

				<portlet:resourceURL id="kaleoProcess" var="exportURL">
					<portlet:param name="kaleoProcessId" value="<%= String.valueOf(kaleoProcess.getKaleoProcessId()) %>" />
				</portlet:resourceURL>

				<%
				StringBundler sb = new StringBundler(6);

				sb.append("javascript:");
				sb.append(renderResponse.getNamespace());
				sb.append("exportKaleoProcess");
				sb.append("('");
				sb.append(exportURL);
				sb.append("');");
				%>

				<aui:nav-item href="<%= sb.toString() %>" iconCssClass="icon-arrow-up" label="export" />
			</aui:nav>

			<aui:nav-bar-search cssClass="navbar-search-advanced" searchContainer="<%= searchContainer %>">
				<%@ include file="/admin/record_search.jspf" %>
			</aui:nav-bar-search>
		</aui:nav-bar>

		<liferay-ui:search-container-results>
			<%@ include file="/admin/record_search_results.jspf" %>
		</liferay-ui:search-container-results>

		<%
		List resultRows = searchContainer.getResultRows();

		for (int i = 0; i < results.size(); i++) {
			DDLRecord ddlRecord = (DDLRecord)results.get(i);

			DDLRecordVersion ddlRecordVersion = ddlRecord.getLatestRecordVersion();

			PortletURL rowURL = renderResponse.createRenderURL();

			rowURL.setParameter("mvcPath", "/admin/view_record.jsp");
			rowURL.setParameter("redirect", currentURL);
			rowURL.setParameter("ddlRecordId", String.valueOf(ddlRecord.getRecordId()));
			rowURL.setParameter("kaleoProcessId", String.valueOf(kaleoProcessId));
			rowURL.setParameter("version", String.valueOf(ddlRecordVersion.getVersion()));

			Fields fields = ddlRecord.getFields();

			ResultRow row = new ResultRow(ddlRecord, ddlRecord.getRecordId(), i);

			// Columns

			for (DDMFormField ddmFormField : ddmFormfields) {
				String name = ddmFormField.getName();

				if (ddmStructure.isFieldPrivate(name)) {
					continue;
				}

				String value = null;

				if (fields.contains(name)) {
					com.liferay.portlet.dynamicdatamapping.storage.Field field = fields.get(name);

					value = field.getRenderedValue(themeDisplay.getLocale());
				}
				else {
					value = StringPool.BLANK;
				}

				row.addText(HtmlUtil.escape(value), rowURL);
			}

			row.addText(LanguageUtil.get(request, WorkflowConstants.getStatusLabel(ddlRecord.getStatus())), rowURL);
			row.addDate(ddlRecord.getModifiedDate(), rowURL);
			row.addText(HtmlUtil.escape(PortalUtil.getUserName(ddlRecord.getUserId(), ddlRecord.getUserName())), rowURL);

			// Action

			row.setParameter("kaleoProcessId", String.valueOf(kaleoProcessId));

			row.addJSP("/admin/record_action.jsp", application, request, response);

			// Add result row

			resultRows.add(row);
		}
		%>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>

<%@ include file="/admin/export_kaleo_process.jspf" %>