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

<h3><liferay-ui:message arguments="2" key="registration-step-x-2" /></h3>

<span class="alert alert-info lcs-alert">
	<liferay-ui:message key="please-add-information-about-your-server" />
</span>

<portlet:renderURL var="viewLCSClusterNodeURL">
	<portlet:param name="mvcPath" value="/view.jsp" />
</portlet:renderURL>

<portlet:actionURL name="addLCSClusterNode" var="addLCSClusterNodeURL">
	<portlet:param name="redirect" value="<%= viewLCSClusterNodeURL %>" />
</portlet:actionURL>

<aui:form action="<%= addLCSClusterNodeURL %>" name="registrationFm">
	<liferay-ui:error exception="<%= DuplicateLCSClusterNodeNameException.class %>" message="please-enter-an-unique-server-name" />
	<liferay-ui:error exception="<%= RequiredLCSClusterNodeNameException.class %>" message="server-name-is-required" />

	<aui:model-context model="<%= LCSClusterNode.class %>" />

	<%
	List<CorpEntryIdentifier> corpEntryIdentifiers = CorpEntryServiceUtil.getCorpEntryIdentifiers();
	%>

	<aui:select name="corpEntryId">

		<%
		long corpEntryId = ParamUtil.getLong(request, "corpEntryId");

		for (CorpEntryIdentifier corpEntryIdentifier : corpEntryIdentifiers) {
		%>

			<aui:option label="<%= corpEntryIdentifier.getName() %>" selected="<%= corpEntryId == corpEntryIdentifier.getCorpEntryId() %>" value="<%= corpEntryIdentifier.getCorpEntryId() %>" />

		<%
		}
		%>

	</aui:select>

	<%
	CorpEntryIdentifier corpEntryIdentifier = corpEntryIdentifiers.get(0);

	long corpEntryId = corpEntryIdentifier.getCorpEntryId();

	List<LCSClusterEntry> lcsClusterEntries = LCSClusterEntryServiceUtil.getCorpEntryLCSClusterEntries(corpEntryId);
	%>

	<aui:field-wrapper cssClass="lcs-environment-container" helpMessage="environment-help" label="environment-required">
		<div class="lcs-environment-input-wrapper" id="<portlet:namespace />lcsClusterEntryInputWrapper">
			<c:choose>
				<c:when test="<%= lcsClusterEntries.isEmpty() %>">
					<liferay-ui:message key="there-are-no-environments-created-yet" />
				</c:when>
				<c:otherwise>
					<aui:select id="lcsClusterEntryId" label="" name="lcsClusterEntryId">

						<%
						for (LCSClusterEntry lcsClusterEntry : lcsClusterEntries) {
						%>

							<aui:option value="<%= lcsClusterEntry.getLcsClusterEntryId() %>">
								<%= lcsClusterEntry.getName() %>
							</aui:option>

						<%
						}
						%>

					</aui:select>
				</c:otherwise>
			</c:choose>
		</div>

		<aui:button-row>
			<aui:button name="addEnvironmentButton" value="add-new-environment" />
		</aui:button-row>
	</aui:field-wrapper>

	<aui:input label="server-name" name="name" />

	<aui:input label="server-location" name="location" />

	<aui:input label="server-description" name="description" />

	<aui:field-wrapper label="lcs-server-key">
		<%= KeyGeneratorUtil.getKey() %>
	</aui:field-wrapper>

	<aui:input checked="<%= true %>" disabled="<%= !ClusterExecutorUtil.isEnabled() %>" id="registerAllClusterNodes" label="register-all-nodes-of-this-cluster" name="registerAllClusterNodes" type="checkbox" />

	<aui:button-row>
		<portlet:actionURL name="resetCredentials" var="resetCredentialsURL">
			<portlet:param name="redirect" value="<%= currentURL %>" />
		</portlet:actionURL>

		<aui:button href="<%= resetCredentialsURL.toString() %>" name="back" value="back" />

		<aui:button cssClass="btn-success" disabled="<%= true %>" name="register" type="submit" value="register" />
	</aui:button-row>
</aui:form>

<aui:script use="liferay-cloud-services">
	var lcsPortlet = new Liferay.Portlet.LCS(
		{
			namespace: '<portlet:namespace />'
		}
	);

	<portlet:renderURL var="addLCSClusterEntryURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
		<portlet:param name="mvcPath" value="/add_lcs_cluster_entry.jsp" />
	</portlet:renderURL>

	lcsPortlet.initializeRegisterPage(
		{
			addLCSClusterEntryURL: '<%= addLCSClusterEntryURL %>',
			errorDuplicateEnvironment: '<%= UnicodeLanguageUtil.get(pageContext, "please-enter-an-unique-environment-name") %>',
			errorGenericEnvironment: '<%= UnicodeLanguageUtil.get(pageContext, "your-request-failed-to-complete") %>',
			errorRequiredEnvironmentName: '<%= UnicodeLanguageUtil.get(pageContext, "environment-name-is-required") %>',
			labelNewEnvironment: '<%= UnicodeLanguageUtil.get(pageContext, "new-environment") %>',
			msgNoEnvironmentsCreated: '<%= UnicodeLanguageUtil.get(pageContext, "there-are-no-environments-created-yet") %>',
			serveCorpEntryURL: '<portlet:resourceURL id="serveCorpEntry" />',
			serveLCSClusterEntryURL: '<portlet:resourceURL id="serveLCSClusterEntry" />'
		}
	);
</aui:script>