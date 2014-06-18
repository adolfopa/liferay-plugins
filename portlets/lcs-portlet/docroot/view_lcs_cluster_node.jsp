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
boolean pending = HandshakeManagerUtil.isPending();
boolean ready = HandshakeManagerUtil.isReady();

LCSClusterNode lcsClusterNode = LCSClusterNodeServiceUtil.getLCSClusterNode(KeyGeneratorUtil.getKey());

LCSClusterEntry lcsClusterEntry = LCSClusterEntryServiceUtil.getLCSClusterEntry(lcsClusterNode.getLcsClusterEntryId());

CorpEntryIdentifier corpEntryIdentifier = new CorpEntryIdentifier();

for (CorpEntryIdentifier currentCorpEntryIdentifier : CorpEntryServiceUtil.getCorpEntryIdentifiers()) {
	if (currentCorpEntryIdentifier.getCorpEntryId() == lcsClusterEntry.getCorpEntryId()) {
		corpEntryIdentifier = currentCorpEntryIdentifier;

		break;
	}
}

String connectionStatus = "connected";

if (!ready && !pending) {
	connectionStatus = "disconnected";
}
else if (!ready && pending) {
	connectionStatus = "synchronizing";
}
%>

<div class="lcs-connection-status <%= connectionStatus %>">
	<span class="lcs-connection-icon"></span>

	<span class="lcs-connection-label"><%= LanguageUtil.get(pageContext, connectionStatus) %></span>

	<liferay-ui:icon-help message='<%= LanguageUtil.get(pageContext, connectionStatus + "-help") %>' />
</div>

<div class="lcs-info">
	<div class="lcs-connection-info">
		<h3><liferay-ui:message key="connection" /></h3>

		<%
		Map<String, String> lcsConnectionMetadata = HandshakeManagerUtil.getLCSConnectionMetadata();
		%>

		<dl>
			<dt>
				<liferay-ui:message key="heartbeat-interval" />
			</dt>
			<dd>

				<%
				Date heartbeatIntervalDate = new Date(GetterUtil.getLong(lcsConnectionMetadata.get("heartbeatInterval")));
				%>

				<%= intervalDateFormatDate.format(heartbeatIntervalDate) %>
			</dd>
			<dt>
				<liferay-ui:message key="message-task-interval" />
			</dt>
			<dd>

				<%
				Date messageTaskIntervalDate = new Date(GetterUtil.getLong(lcsConnectionMetadata.get("messageTaskInterval")));
				%>

				<%= intervalDateFormatDate.format(messageTaskIntervalDate) %>
			</dd>
			<dt>
				<liferay-ui:message key="metrics-task-interval" />
			</dt>
			<dd>

				<%
				Date jvmMetricsTaskIntervalDate = new Date(GetterUtil.getLong(lcsConnectionMetadata.get("jvmMetricsTaskInterval")));
				%>

				<%= intervalDateFormatDate.format(jvmMetricsTaskIntervalDate) %>
			</dd>

			<c:if test='<%= lcsConnectionMetadata.get("messageTaskTime") != null %>'>
				<dt>
					<liferay-ui:message key="last-message-received" />
				</dt>
				<dd>

					<%
					Date messageTaskTimeDate = new Date(GetterUtil.getLong(lcsConnectionMetadata.get("messageTaskTime")));
					%>

					<%= dateFormatDate.format(messageTaskTimeDate) %>
				</dd>
			</c:if>

			<c:if test="<%= !pending && HandshakeManagerUtil.isReady() %>">
				<dt>
					<liferay-ui:message key="connection-uptime" />
				</dt>
				<dd>

					<%
					String handshakeTime = lcsConnectionMetadata.get("handshakeTime");
					%>

					<c:if test="<%= handshakeTime != null %>">

						<%
						Date handshakeTimeDate = new Date(System.currentTimeMillis() - GetterUtil.getLong(handshakeTime));
						%>

						<%= intervalDateFormatDate.format(handshakeTimeDate) %>
					</c:if>
				</dd>
			</c:if>
		</dl>
	</div>

	<div class="lcs-portal-info">
		<h3><liferay-ui:message key="visit-cloud-portal" /></h3>

		<dl>
			<dt>
				<liferay-ui:message key="project" />
			</dt>
			<dd>
				<aui:a href="<%= LCSUtil.getCorpEntryPageURL(request, corpEntryIdentifier) %>" label="<%= HtmlUtil.escape(corpEntryIdentifier.getName()) %>" target="_blank" />
			</dd>
		</dl>
		<dl>
			<dt>
				<liferay-ui:message key="environment" />
			</dt>
			<dd>
				<aui:a href="<%= LCSUtil.getLCSClusterEntryPageURL(request, corpEntryIdentifier, lcsClusterNode) %>" label="<%= HtmlUtil.escape(lcsClusterEntry.getName()) %>" target="_blank" />
			</dd>
		</dl>
		<dl>
			<dt>
				<liferay-ui:message key="server" />
			</dt>
			<dd>
				<aui:a href="<%= LCSUtil.getLCSClusterNodePageURL(request, corpEntryIdentifier, lcsClusterNode) %>" label="<%= HtmlUtil.escape(lcsClusterNode.getName()) %>" target="_blank" />
			</dd>
		</dl>
	</div>

	<c:if test="<%= ClusterExecutorUtil.isEnabled() %>">
		<div class="lcs-cluster-info">
			<h3><liferay-ui:message key="cluster" /></h3>

			<dl>
				<dt>
					<liferay-ui:message key="cluster-node-ids" />
				</dt>
				<dd>

					<%
					for (ClusterNode clusterNode : ClusterExecutorUtil.getClusterNodes()) {
					%>

						<%= clusterNode.getClusterNodeId() %><br />

					<%
					}
					%>

				</dd>
			</dl>
		</div>
	</c:if>
</div>

<aui:button-row>
	<aui:button cssClass='<%= "btn-success " + (ready ? "hide" : StringPool.BLANK) %>' disabled="<%= pending %>" name="connect" title='<%= LanguageUtil.get(pageContext, "connect-help") %>' value="connect" />

	<aui:button cssClass='<%= ready ? StringPool.BLANK : "hide" %>' disabled="<%= pending %>" name="disconnect" title='<%= LanguageUtil.get(pageContext, "disconnect-help") %>' value="disconnect" />

	<liferay-portlet:actionURL name="resetCredentials" var="resetCredentialsURL">
		<portlet:param name="redirect" value="<%= currentURL %>" />
	</liferay-portlet:actionURL>

	<aui:button disabled="<%= pending %>" href="<%= resetCredentialsURL %>" name="resetCredentials" title='<%= LanguageUtil.get(pageContext, "reset-credentials-help") %>' value="reset-credentials" />
</aui:button-row>

<div class="alert alert-info lcs-synchronizing-alert <%= pending ? StringPool.BLANK : "hide" %>" id="<portlet:namespace />connectionAlertContainer">
	<liferay-ui:message key="synchronizing-help" />
</div>

<aui:script use="liferay-cloud-services">
	var lcsPortlet = new Liferay.Portlet.LCS(
		{
			namespace: '<portlet:namespace />'
		}
	);

	lcsPortlet.initializeLCSClusterNodePage(
		{
			connectURL: '<portlet:resourceURL id="connect" />',
			connectionStatusURL: '<portlet:resourceURL id="serveConnectionStatus" />',
			disconnectURL: '<portlet:resourceURL id="disconnect" />',
			labelConnected: '<%= UnicodeLanguageUtil.get(pageContext, "connected") %>',
			labelConnectedHelp: '<%= UnicodeLanguageUtil.get(pageContext, "connected-help") %>',
			labelDisconnected: '<%= UnicodeLanguageUtil.get(pageContext, "disconnected") %>',
			labelDisconnectedHelp: '<%= UnicodeLanguageUtil.get(pageContext, "disconnected-help") %>',
			labelPending: '<%= UnicodeLanguageUtil.get(pageContext, "synchronizing") %>',
			labelPendingHelp: '<%= UnicodeLanguageUtil.get(pageContext, "synchronizing-help") %>',
			pending: '<%= pending %>'
		}
	);
</aui:script>