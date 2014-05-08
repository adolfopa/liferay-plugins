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

long kaleoProcessId = ParamUtil.getLong(request, "kaleoProcessId");
String workflowDefinition = ParamUtil.getString(request, "workflowDefinition");

String initialStateName = KaleoFormsUtil.getInitialStateName(company.getCompanyId(), workflowDefinition);

TaskFormPair initialStateTaskFormPair = KaleoFormsUtil.getInitialStateTaskFormPair(kaleoProcessId, initialStateName, portletSession);
%>

<div id="<portlet:namespace />formsSearchContainer">
	<liferay-portlet:renderURL varImpl="portletURL" />

	<liferay-ui:search-container
		searchContainer='<%= new SearchContainer<Object>(renderRequest, portletURL, null, "no-tasks-were-found") %>'
	>

		<liferay-ui:search-container-results>

			<%
			TaskFormPairs taskFormPairs = KaleoFormsUtil.getTaskFormPairs(company.getCompanyId(), kaleoProcessId, workflowDefinition, portletSession);

			taskFormPairs.add(0, initialStateTaskFormPair);

			searchContainer.setResults(taskFormPairs.list());
			searchContainer.setTotal(taskFormPairs.size());
			%>

		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="com.liferay.portal.workflow.kaleo.forms.util.TaskFormPair"
			modelVar="taskFormsPair"
		>

		<liferay-ui:search-container-column-text
			name="task"
			value="<%= taskFormsPair.getWorkflowTaskName() %>"
		/>

		<%
		long ddmTemplateId = taskFormsPair.getDDMTemplateId();

		String formName = StringPool.BLANK;

		if (ddmTemplateId > 0) {
			DDMTemplate ddmTemplate = DDMTemplateLocalServiceUtil.getTemplate(ddmTemplateId);

			formName = ddmTemplate.getName(locale);
		}
		%>

		<liferay-util:buffer var="taskInputBuffer">
			<c:if test="<%= taskFormsPair.equals(initialStateTaskFormPair) %>">
				<aui:input name="ddmTemplateId" type="hidden" value="<%= ddmTemplateId == 0 ? StringPool.BLANK : String.valueOf(ddmTemplateId) %>">
					<aui:validator name="required" />
				</aui:input>
			</c:if>
		</liferay-util:buffer>

		<liferay-ui:search-container-column-text
			name="form"
			value="<%= formName + taskInputBuffer %>"
		/>

		<portlet:renderURL var="selectFormURL">
			<portlet:param name="mvcPath" value="/admin/process/select_template.jsp" />
			<portlet:param name="backURL" value="<%= backURL %>" />
			<portlet:param name="workflowTaskName" value="<%= taskFormsPair.getWorkflowTaskName() %>" />
			<portlet:param name="mode" value="<%= taskFormsPair.getWorkflowTaskName().equals(initialStateName) ? DDMTemplateConstants.TEMPLATE_MODE_CREATE : DDMTemplateConstants.TEMPLATE_MODE_EDIT %>" />
		</portlet:renderURL>

		<%
		String taglibOnClick = "javascript:window.location.href='"+ selectFormURL.toString() +"';";
		%>

		<liferay-ui:search-container-column-button
			align="right"
			href="<%= taglibOnClick %>"
			name="assign-form"
		/>

		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</div>