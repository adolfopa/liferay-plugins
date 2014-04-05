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

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %><%@
taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.oauth.model.OAuthApplication" %><%@
page import="com.liferay.oauth.model.OAuthApplicationConstants" %><%@
page import="com.liferay.oauth.search.OAuthApplicationDisplayTerms" %><%@
page import="com.liferay.oauth.search.OAuthApplicationSearch" %><%@
page import="com.liferay.oauth.service.OAuthApplicationLocalServiceUtil" %><%@
page import="com.liferay.oauth.service.OAuthUserLocalServiceUtil" %><%@
page import="com.liferay.oauth.service.permission.OAuthApplicationPermission" %><%@
page import="com.liferay.oauth.service.permission.OAuthPermission" %><%@
page import="com.liferay.oauth.service.permission.OAuthUserPermission" %><%@
page import="com.liferay.oauth.util.ActionKeys" %><%@
page import="com.liferay.oauth.util.OAuthAccessor" %><%@
page import="com.liferay.oauth.util.OAuthConsumer" %><%@
page import="com.liferay.oauth.util.OAuthMessage" %><%@
page import="com.liferay.oauth.util.OAuthUtil" %><%@
page import="com.liferay.portal.ImageTypeException" %><%@
page import="com.liferay.portal.RequiredFieldException" %><%@
page import="com.liferay.portal.kernel.dao.search.ResultRow" %><%@
page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %><%@
page import="com.liferay.portal.kernel.servlet.SessionMessages" %><%@
page import="com.liferay.portal.kernel.upload.UploadException" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.StringPool" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.util.Validator" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portal.webserver.WebServerServletTokenUtil" %><%@
page import="com.liferay.portlet.PortletURLUtil" %>

<%@ page import="java.net.MalformedURLException" %>

<%@ page import="java.util.LinkedHashMap" %>

<%@ page import="javax.portlet.PortletMode" %><%@
page import="javax.portlet.PortletURL" %><%@
page import="javax.portlet.WindowState" %>

<portlet:defineObjects />

<liferay-theme:defineObjects />

<%
PortletMode portletMode = liferayPortletRequest.getPortletMode();
WindowState windowState = liferayPortletRequest.getWindowState();

PortletURL currentURLObj = PortletURLUtil.getCurrent(liferayPortletRequest, liferayPortletResponse);

String currentURL = currentURLObj.toString();
%>