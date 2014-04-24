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

long classNameId = ParamUtil.getLong(request, "classNameId");
long classPK = ParamUtil.getLong(request, "classPK");

if (classPK > 0) {
	DDMStructure ddmStructure = DDMStructureServiceUtil.getStructure(classPK);

	renderRequest.setAttribute(WebKeys.DYNAMIC_DATA_MAPPING_STRUCTURE, ddmStructure);
}
%>

<div class="portlet-dynamic-data-mapping">
	<liferay-util:include page="/html/portlet/dynamic_data_mapping/edit_structure.jsp" portletId="<%= PortletKeys.DYNAMIC_DATA_MAPPING %>">
		<portlet:param name="redirect" value="<%= redirect %>" />
		<portlet:param name="refererPortletName" value="<%= portletDisplay.getId() %>" />
		<portlet:param name="portletResourceNamespace" value="<%= renderResponse.getNamespace() %>" />
		<portlet:param name="groupId" value="<%= String.valueOf(scopeGroupId) %>" />
		<portlet:param name="classNameId" value="<%= String.valueOf(classNameId) %>" />
		<portlet:param name="classPK" value="<%= String.valueOf(classPK) %>" />
		<portlet:param name="structureAvailableFields" value='<%= renderResponse.getNamespace() + "getAvailableFields" %>' />
		<portlet:param name="editStructureURL" value='<portlet:actionURL name="updateDefinition" />' />
	</liferay-util:include>
</div>