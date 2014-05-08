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

long groupId = BeanParamUtil.getLong(kaleoProcess, request, "groupId", scopeGroupId);

long ddlRecordSetId = BeanParamUtil.getLong(kaleoProcess, request, "DDLRecordSetId");

DDLRecordSet ddlRecordSet = null;

if (ddlRecordSetId > 0) {
	ddlRecordSet = DDLRecordSetLocalServiceUtil.getRecordSet(ddlRecordSetId);
}

long ddmStructureId = BeanParamUtil.getLong(ddlRecordSet, request, "DDMStructureId");

boolean kaleoProcessStarted = false;

String kaleoProcessName = StringPool.BLANK;

if (kaleoProcess != null) {
	kaleoProcessStarted = (DDLRecordLocalServiceUtil.getRecordsCount(kaleoProcess.getDDLRecordSetId(), WorkflowConstants.STATUS_ANY) > 0);

	kaleoProcessName = kaleoProcess.getName(locale);
}
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	localizeTitle="<%= (kaleoProcess == null) %>"
	title='<%= Validator.isNull(kaleoProcessName) ? "new-process" : kaleoProcessName %>'
/>

<c:if test="<%= kaleoProcessStarted %>">
	<div class="alert alert-info">
		<liferay-ui:message key="updating-the-fields-definition-or-workflow-will-cause-loss-of-data" />
	</div>
</c:if>

<portlet:actionURL name="updateKaleoProcess" var="editKaleoProcessURL">
	<portlet:param name="mvcPath" value="/admin/edit_kaleo_process.jsp" />
	<portlet:param name="redirect" value="<%= redirect %>" />
</portlet:actionURL>

<aui:form action="<%= editKaleoProcessURL %>" method="post" name="fm">
	<aui:input name="kaleoProcessId" type="hidden" value="<%= kaleoProcessId %>" />
	<aui:input name="groupId" type="hidden" value="<%= groupId %>" />
	<aui:input name="scope" type="hidden" value="1" />
	<aui:input name="oldDDMStructureId" type="hidden" value="<%= ddmStructureId %>" />

	<liferay-ui:error exception="<%= KaleoProcessDDMTemplateIdException.class %>" message="please-enter-a-valid-initial-form" />

	<liferay-util:buffer var="htmlBottom">
		<aui:button-row cssClass="kaleo-process-buttons">
			<aui:button cssClass="hide kaleo-process-previous pull-left" icon="icon-circle-arrow-left" value="previous" />

			<aui:button cssClass="hide kaleo-process-submit pull-right" primary="<%= true %>" type="submit" />

			<aui:button cssClass="kaleo-process-next pull-right" icon="icon-circle-arrow-right" iconAlign="right" primary="<%= true %>" value="next" />

			<aui:button cssClass="kaleo-process-cancel pull-right" href="<%= redirect %>" value="cancel" />
		</aui:button-row>
	</liferay-util:buffer>

	<liferay-ui:form-navigator
		categoryNames="<%= _CATEGORY_NAMES %>"
		categorySections="<%= _CATEGORY_SECTIONS %>"
		displayStyle="steps"
		formName="fm"
		htmlBottom="<%= htmlBottom %>"
		jspPath="/admin/process/"
		showButtons="<%= false %>"
	/>

	<aui:script use="liferay-component,liferay-form,liferay-kaleo-forms-admin">
		Liferay.after(
			'form:registered',
			function(event) {
				var form = Liferay.Form.get('<portlet:namespace />fm');

				if (form === event.form) {
					new Liferay.KaleoFormsAdmin(
						{
							currentURL: '<%= currentURL %>',
							form: form,
							namespace: '<portlet:namespace />',
							portletId: '<%= PortalUtil.getPortletId(request) %>',
							saveInPortletSessionURL: '<portlet:resourceURL id="saveInPortletSession" />',
							tabView: Liferay.component('<portlet:namespace />fmTabview')
						}
					);
				}
			}
		);
	</aui:script>
</aui:form>

<%!
private static final String[] _CATEGORY_NAMES = {""};

private static final String[][] _CATEGORY_SECTIONS = {{"details", "fields-definition", "workflow", "forms"}};
%>