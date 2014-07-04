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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.portlet.PortletURLUtil" %><%@
page import="com.liferay.lcs.oauth.OAuthUtil" %><%@
page import="com.liferay.lcs.util.HandshakeManagerUtil" %><%@
page import="com.liferay.lcs.util.KeyGeneratorUtil" %><%@
page import="com.liferay.lcs.util.LCSUtil" %><%@
page import="com.liferay.osb.lcs.DuplicateLCSClusterNodeNameException" %><%@
page import="com.liferay.osb.lcs.RequiredLCSClusterNodeNameException" %><%@
page import="com.liferay.osb.lcs.model.CorpEntryIdentifier" %><%@
page import="com.liferay.osb.lcs.model.LCSClusterEntry" %><%@
page import="com.liferay.osb.lcs.model.LCSClusterNode" %><%@
page import="com.liferay.osb.lcs.service.CorpEntryServiceUtil" %><%@
page import="com.liferay.osb.lcs.service.LCSClusterEntryServiceUtil" %><%@
page import="com.liferay.osb.lcs.service.LCSClusterNodeServiceUtil" %><%@
page import="com.liferay.portal.kernel.cluster.ClusterExecutorUtil" %><%@
page import="com.liferay.portal.kernel.cluster.ClusterNode" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil" %><%@
page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %><%@
page import="com.liferay.portal.kernel.util.FastDateFormatConstants" %><%@
page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil" %><%@
page import="com.liferay.portal.kernel.util.GetterUtil" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.StringPool" %>

<%@ page import="java.text.Format" %>

<%@ page import="java.util.Date" %><%@
page import="java.util.List" %><%@
page import="java.util.Map" %><%@
page import="java.util.TimeZone" %>

<%@ page import="javax.portlet.PortletMode" %><%@
page import="javax.portlet.PortletURL" %><%@
page import="javax.portlet.WindowState" %>

<%@ page import="org.scribe.model.Token" %>

<portlet:defineObjects />

<liferay-theme:defineObjects />

<%
PortletMode portletMode = liferayPortletRequest.getPortletMode();
WindowState windowState = liferayPortletRequest.getWindowState();

PortletURL currentURLObj = PortletURLUtil.getCurrent(liferayPortletRequest, liferayPortletResponse);

String currentURL = currentURLObj.toString();

Format dateFormatDate = FastDateFormatFactoryUtil.getDateTime(FastDateFormatConstants.MEDIUM, FastDateFormatConstants.MEDIUM, locale, timeZone);
Format intervalDateFormatDate = FastDateFormatFactoryUtil.getSimpleDateFormat("HH:mm:ss", TimeZone.getTimeZone(StringPool.UTC));
%>