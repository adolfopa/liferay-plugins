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
String currentSectionURL = HttpUtil.setParameter(currentURL, portletNamespace + "historyKey", "workflow");

String tabs1 = ParamUtil.getString(request, "tabs1", "published");

KaleoProcess kaleoProcess = (KaleoProcess)request.getAttribute(WebKeys.KALEO_PROCESS);

String workflowDefinition = GetterUtil.getString(portletSession.getAttribute("workflowDefinition"), BeanParamUtil.getString(kaleoProcess, request, "workflowDefinition"));

String workflowDefinitionName = StringPool.BLANK;

int workflowDefinitionVersion = 0;

if (Validator.isNotNull(workflowDefinition)) {
	String[] workflowDefinitionParts = StringUtil.split(workflowDefinition, CharPool.AT);

	workflowDefinitionName = workflowDefinitionParts[0];

	workflowDefinitionVersion = GetterUtil.getInteger(workflowDefinitionParts[1]);
}
%>

<h3><liferay-ui:message key="workflow" /></h3>

<aui:field-wrapper>

	<%
	String workflowDefinitionDisplay = StringPool.BLANK;

	if (Validator.isNotNull(workflowDefinitionName)) {
		workflowDefinitionDisplay = workflowDefinitionName + " (" + LanguageUtil.get(locale, "version") + " " + workflowDefinitionVersion + ")";
	}
	%>

	<aui:a href="javascript:;" id="workflowDefinitionDisplay" label="<%= HtmlUtil.escape(workflowDefinitionDisplay) %>" />

	<aui:input name="workflowDefinition" type="hidden" value="<%= workflowDefinition %>">
		<aui:validator name="required" />
	</aui:input>
</aui:field-wrapper>

<liferay-portlet:renderURL varImpl="iteratorURL">
	<portlet:param name="mvcPath" value="/admin/edit_kaleo_process.jsp" />
	<portlet:param name="redirect" value="<%= redirect %>" />
	<portlet:param name="tabs1" value="<%= tabs1 %>" />
	<portlet:param name="historyKey" value="workflow" />
</liferay-portlet:renderURL>

<liferay-ui:search-container
	emptyResultsMessage="no-workflow-definitions-are-defined"
	iteratorURL="<%= iteratorURL %>"
	total= '<%= tabs1.equals("published") ? WorkflowDefinitionManagerUtil.getActiveWorkflowDefinitionCount(company.getCompanyId()) : KaleoDraftDefinitionLocalServiceUtil.getLatestKaleoDraftDefinitionsCount(company.getCompanyId(), 0) %>'
>

	<portlet:renderURL var="editWorkflowURL">
		<portlet:param name="mvcPath" value="/admin/process/edit_workflow.jsp" />
		<portlet:param name="backURL" value="<%= currentSectionURL %>" />
	</portlet:renderURL>

	<aui:button href="<%= editWorkflowURL.toString() %>" primary="<%= true %>" value="add-workflow" />

	<div class="separator"><!-- --></div>

	<liferay-portlet:renderURL varImpl="portletURL">
		<portlet:param name="mvcPath" value="/admin/edit_kaleo_process.jsp" />
		<portlet:param name="redirect" value="<%= redirect %>" />
		<portlet:param name="tabs1" value="<%= tabs1 %>" />
		<portlet:param name="historyKey" value="workflow" />
	</liferay-portlet:renderURL>

	<liferay-ui:tabs
		names="published,unpublished"
		url="<%= portletURL.toString() %>"
	/>

	<c:choose>
		<c:when test='<%= tabs1.equals("published") %>'>
			<liferay-ui:search-container-results
				results="<%= WorkflowDefinitionManagerUtil.getActiveWorkflowDefinitions(company.getCompanyId(), searchContainer.getStart(), searchContainer.getEnd(), null) %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.workflow.WorkflowDefinition"
				modelVar="workflowDefinitionVar"
			>

				<liferay-ui:search-container-row-parameter
					name="backURL"
					value="<%= currentSectionURL %>"
				/>

				<liferay-ui:search-container-column-text
					name="name"
					value="<%= HtmlUtil.escape(workflowDefinitionVar.getName()) %>"
				/>

				<liferay-ui:search-container-column-text
					name="title"
					value="<%= HtmlUtil.escape(workflowDefinitionVar.getTitle(themeDisplay.getLanguageId())) %>"
				/>

				<liferay-ui:search-container-column-text
					name="version"
					value="<%= String.valueOf(workflowDefinitionVar.getVersion()) %>"
				/>

				<liferay-ui:search-container-column-jsp
					align="right"
					path="/admin/process/workflow_action.jsp"
				/>
			</liferay-ui:search-container-row>
		</c:when>
		<c:otherwise>
			<liferay-ui:search-container-results
				results="<%= KaleoDraftDefinitionLocalServiceUtil.getLatestKaleoDraftDefinitions(company.getCompanyId(), 0, searchContainer.getStart(), searchContainer.getEnd(), null) %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.workflow.kaleo.designer.model.KaleoDraftDefinition"
				keyProperty="kaleoDraftDefinitionId"
				modelVar="kaleoDraftDefinition"
			>

				<liferay-ui:search-container-row-parameter
					name="backURL"
					value="<%= currentSectionURL %>"
				/>

				<liferay-ui:search-container-row-parameter
					name="name"
					value="<%= kaleoDraftDefinition.getName() %>"
				/>

				<liferay-ui:search-container-row-parameter
					name="version"
					value="<%= kaleoDraftDefinition.getVersion() %>"
				/>

				<liferay-ui:search-container-row-parameter
					name="draftVersion"
					value="<%= kaleoDraftDefinition.getDraftVersion() %>"
				/>

				<liferay-ui:search-container-column-text
					name="name"
					value="<%= HtmlUtil.escape(kaleoDraftDefinition.getName()) %>"
				/>

				<liferay-ui:search-container-column-text
					name="title"
					value="<%= HtmlUtil.escape(kaleoDraftDefinition.getTitle(themeDisplay.getLanguageId())) %>"
				/>

				<liferay-ui:search-container-column-text
					name="draft-version"
					value="<%= String.valueOf(kaleoDraftDefinition.getDraftVersion()) %>"
				/>

				<liferay-ui:search-container-column-jsp
					align="right"
					path="/admin/process/kaleo_draft_definition_action.jsp"
				/>

			</liferay-ui:search-container-row>
		</c:otherwise>
	</c:choose>
	<liferay-ui:search-iterator />
</liferay-ui:search-container>

<aui:script>
	Liferay.on(
		'<portlet:namespace />chooseWorkflow',
		function(event) {
			var A = AUI();

			A.one('#<portlet:namespace />workflowDefinition').val(event.name + '@' + event.version);

			A.one('#<portlet:namespace />workflowDefinitionDisplay').html(
				A.Lang.sub(
					'{name} ({versionLabel} {version})',
					{
						name: Liferay.Util.escapeHTML(event.name),
						version: event.version,
						versionLabel: '<liferay-ui:message key="version" />'
					}
				)
			);
		},
		['aui-base']
	);
</aui:script>