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
String currentSectionURL = HttpUtil.setParameter(currentURL, portletNamespace + "historyKey", "fields-definition");

KaleoProcess kaleoProcess = (KaleoProcess)request.getAttribute(WebKeys.KALEO_PROCESS);

long ddlRecordSetId = BeanParamUtil.getLong(kaleoProcess, request, "DDLRecordSetId");

DDLRecordSet ddlRecordSet = KaleoFormsUtil.getDDLRecordSet(kaleoProcess, portletSession);

long ddmStructureId = ddlRecordSet.getDDMStructureId();

String ddmStructureName = StringPool.BLANK;

if (ddmStructureId > 0) {
	DDMStructure ddmStructure = DDMStructureLocalServiceUtil.fetchDDMStructure(ddmStructureId);

	if (ddmStructure != null) {
		ddmStructureName = ddmStructure.getName(locale);
	}
}
%>

<h3><liferay-ui:message key="fields-definition" /></h3>

<aui:field-wrapper>
	<aui:a href="javascript:;" id="ddmStructureDisplay" label="<%= HtmlUtil.escape(ddmStructureName) %>" />

	<aui:input name="ddlRecordSetId" type="hidden" value="<%= ddlRecordSetId %>" />
	<aui:input name="ddmStructureId" type="hidden" value="<%= ddmStructureId %>" />

	<aui:input name="ddmStructureName" type="hidden" value="<%= ddmStructureName %>">
		<aui:validator name="required" />
	</aui:input>
</aui:field-wrapper>

<liferay-ui:error exception="<%= RecordSetDDMStructureIdException.class %>" message="please-enter-a-valid-definition" />

<liferay-portlet:renderURL varImpl="iteratorURL">
	<portlet:param name="mvcPath" value="/admin/edit_kaleo_process.jsp" />
	<portlet:param name="redirect" value="<%= redirect %>" />
	<portlet:param name="historyKey" value="fields-definition" />
</liferay-portlet:renderURL>

<liferay-ui:search-container
	searchContainer='<%= new SearchContainer(renderRequest, new DisplayTerms(request), null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, iteratorURL, null, "there-are-no-results") %>'
>

	<%
	DisplayTerms displayTerms = searchContainer.getDisplayTerms();
	%>

	<liferay-ui:search-container-results>

		<%
		total = DDMStructureServiceUtil.searchCount(company.getCompanyId(), PortalUtil.getSiteAndCompanyGroupIds(scopeGroupId), new long[] {PortalUtil.getClassNameId(ddmDisplay.getStructureType())}, displayTerms.getKeywords());

		searchContainer.setTotal(total);

		results = DDMStructureServiceUtil.search(company.getCompanyId(), PortalUtil.getSiteAndCompanyGroupIds(scopeGroupId), new long[] {PortalUtil.getClassNameId(ddmDisplay.getStructureType())}, displayTerms.getKeywords(), searchContainer.getStart(), searchContainer.getEnd(), null);

		searchContainer.setResults(results);
		%>

	</liferay-ui:search-container-results>

	<portlet:renderURL var="editDefinitionURL">
		<portlet:param name="mvcPath" value="/admin/process/edit_structure.jsp" />
		<portlet:param name="redirect" value="<%= currentSectionURL %>" />
	</portlet:renderURL>

	<aui:button href="<%= editDefinitionURL.toString() %>" primary="<%= true %>" value="add-fields-definition" />

	<liferay-ui:search-container-row
		className="com.liferay.portlet.dynamicdatamapping.model.DDMStructure"
		keyProperty="structureId"
		modelVar="structure"
	>

		<liferay-ui:search-container-row-parameter
			name="redirect"
			value="<%= currentSectionURL %>"
		/>

		<liferay-ui:search-container-column-text
			name="name"
			value="<%= HtmlUtil.escape(structure.getName(locale)) %>"
		/>

		<liferay-ui:search-container-column-text
			name="description"
			value="<%= HtmlUtil.escape(structure.getDescription(locale)) %>"
		/>

		<liferay-ui:search-container-column-date
			name="modified-date"
			value="<%= structure.getModifiedDate() %>"
		/>

		<liferay-ui:search-container-column-jsp
			align="right"
			path="/admin/process/structure_action.jsp"
		/>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator />
</liferay-ui:search-container>

<aui:script>
	Liferay.on(
		'<portlet:namespace />chooseDefinition',
		function(event) {
			var A = new AUI();

			A.one('#<portlet:namespace />ddmStructureDisplay').html(Liferay.Util.unescapeHTML(event.name));
			A.one('#<portlet:namespace />ddmStructureId').val(event.ddmstructureid);
			A.one('#<portlet:namespace />ddmStructureName').val(Liferay.Util.unescapeHTML(event.name));
		},
		['aui-base']
	);
</aui:script>