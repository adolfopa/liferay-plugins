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

KaleoProcess kaleoProcess = (KaleoProcess)request.getAttribute(WebKeys.KALEO_PROCESS);

long kaleoProcessId = BeanParamUtil.getLong(kaleoProcess, request, "kaleoProcessId");

String workflowDefinition = GetterUtil.getString(portletSession.getAttribute("workflowDefinition"), StringPool.BLANK);

String initialStateName = KaleoFormsUtil.getInitialStateName(company.getCompanyId(), workflowDefinition);

long ddmTemplateId = GetterUtil.getLong(portletSession.getAttribute(initialStateName), 0);

if (kaleoProcess != null) {
	ddmTemplateId = kaleoProcess.getDDMTemplateId();
}
%>

<h3><liferay-ui:message key="forms" /></h3>

<aui:field-wrapper>

	<%
	List<ObjectValuePair<String, Long>> taskFormPairs = KaleoFormsUtil.getTaskFormPairs(company.getCompanyId(), kaleoProcessId, workflowDefinition, portletSession);
	%>

	<aui:input name="ddmTemplateId" type="hidden" value="<%= ddmTemplateId %>">
		<aui:validator name="required" />
	</aui:input>

	<aui:input name="taskFormPairsData" type="hidden" value="<%= TaskFormPairsSerializer.serialize(taskFormPairs, initialStateName) %>" />
</aui:field-wrapper>

<portlet:renderURL var="currentSectionURL">
	<portlet:param name="mvcPath" value="/admin/edit_kaleo_process.jsp" />
	<portlet:param name="redirect" value="<%= redirect %>" />
	<portlet:param name="kaleoProcessId" value="<%= String.valueOf(kaleoProcessId) %>" />
	<portlet:param name="historyKey" value="forms" />
</portlet:renderURL>

<div id="<portlet:namespace />resultsContainer">
	<liferay-util:include page="/admin/process/task_template_search_container.jsp" servletContext="<%= application %>">
		<liferay-util:param name="backURL" value="<%= currentSectionURL %>" />
		<liferay-util:param name="kaleoProcessId" value="<%= String.valueOf(kaleoProcessId) %>" />
		<liferay-util:param name="workflowDefinition" value="<%= workflowDefinition %>" />
	</liferay-util:include>
</div>