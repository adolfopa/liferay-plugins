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
KaleoProcess kaleoProcess = (KaleoProcess)request.getAttribute(WebKeys.KALEO_PROCESS);

DDLRecordSet ddlRecordSet = KaleoFormsUtil.getDDLRecordSet(kaleoProcess, portletSession);
%>

<h3><liferay-ui:message key="details" /></h3>

<liferay-ui:error exception="<%= RecordSetNameException.class %>" message="please-enter-a-valid-name" />

<aui:model-context bean="<%= ddlRecordSet %>" model="<%= DDLRecordSet.class %>" />

<aui:fieldset>
	<aui:input name="name">
		<aui:validator name="required" />
	</aui:input>

	<aui:input name="description" />
</aui:fieldset>