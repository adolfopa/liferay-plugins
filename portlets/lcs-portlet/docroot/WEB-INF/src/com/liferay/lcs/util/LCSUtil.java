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

package com.liferay.lcs.util;

import com.liferay.compat.portal.util.PortalUtil;
import com.liferay.jsonwebserviceclient.JSONWebServiceClient;
import com.liferay.osb.lcs.model.CorpEntryIdentifier;
import com.liferay.osb.lcs.model.LCSClusterNode;
import com.liferay.osb.lcs.service.LCSClusterNodeServiceUtil;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.QName;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.PublicRenderParameter;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletQNameUtil;
import com.liferay.util.portlet.PortletProps;

import javax.security.auth.login.CredentialException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Igor Beslic
 * @author Ivica Cardic
 */
public class LCSUtil {

	public static final int CREDENTIALS_INVALID = 2;

	public static final int CREDENTIALS_MISSING = 1;

	public static final int CREDENTIALS_NOT_AVAILABLE = 0;

	public static final int CREDENTIALS_SET = 3;

	public static String getCorpEntryLayoutURL(
			HttpServletRequest request, CorpEntryIdentifier corpEntryIdentifier)
		throws Exception {

		StringBundler sb = new StringBundler(6);

		sb.append(getLCSPortalURL());
		sb.append(PortletProps.get("osb.lcs.portlet.page.corp.entry"));
		sb.append("?p_p_id=5_WAR_osblcsportlet&");
		sb.append(getPublicRenderParameterName(request, "layoutCorpEntryId"));
		sb.append(StringPool.EQUAL);
		sb.append(corpEntryIdentifier.getCorpEntryId());

		return sb.toString();
	}

	public static int getCredentialsStatus() {
		javax.portlet.PortletPreferences jxPortletPreferences = null;

		try {
			jxPortletPreferences = getJxPortletPreferences();
		}
		catch (PortalException e) {
			return CREDENTIALS_MISSING;
		}
		catch (SystemException e) {
			return CREDENTIALS_NOT_AVAILABLE;
		}

		String lcsAccessToken = jxPortletPreferences.getValue(
			"lcsAccessToken", null);
		String lcsAccessSecret = jxPortletPreferences.getValue(
			"lcsAccessSecret", null);

		if (Validator.isNull(lcsAccessToken) ||
			Validator.isNull(lcsAccessSecret)) {

			return CREDENTIALS_INVALID;
		}

		try {
			LCSClusterNodeServiceUtil.isRegistered(KeyGeneratorUtil.getKey());

			return CREDENTIALS_SET;
		}
		catch (Exception e) {
			if (e.getCause() instanceof CredentialException) {
				return CREDENTIALS_INVALID;
			}

			throw new RuntimeException(e);
		}
	}

	public static String getLCSClusterEntryLayoutURL(
			HttpServletRequest request, CorpEntryIdentifier corpEntryIdentifier,
			LCSClusterNode lcsClusterNode)
		throws Exception {

		StringBundler sb = new StringBundler(10);

		sb.append(getLCSPortalURL());
		sb.append(PortletProps.get("osb.lcs.portlet.page.lcs.cluster.entry"));
		sb.append("?p_p_id=5_WAR_osblcsportlet&");
		sb.append(getPublicRenderParameterName(request, "layoutCorpEntryId"));
		sb.append(StringPool.EQUAL);
		sb.append(corpEntryIdentifier.getCorpEntryId());
		sb.append(StringPool.AMPERSAND);
		sb.append(
			getPublicRenderParameterName(request, "layoutLCSClusterEntryId"));
		sb.append(StringPool.EQUAL);
		sb.append(lcsClusterNode.getLcsClusterEntryId());

		return sb.toString();
	}

	public static String getLCSClusterNodeLayoutURL(
			HttpServletRequest request, CorpEntryIdentifier corpEntryIdentifier,
			LCSClusterNode lcsClusterNode)
		throws Exception {

		StringBundler sb = new StringBundler(14);

		sb.append(getLCSPortalURL());
		sb.append(PortletProps.get("osb.lcs.portlet.page.lcs.cluster.node"));
		sb.append("?p_p_id=5_WAR_osblcsportlet&");
		sb.append(getPublicRenderParameterName(request, "layoutCorpEntryId"));
		sb.append(StringPool.EQUAL);
		sb.append(corpEntryIdentifier.getCorpEntryId());
		sb.append(StringPool.AMPERSAND);
		sb.append(
			getPublicRenderParameterName(request, "layoutLCSClusterEntryId"));
		sb.append(StringPool.EQUAL);
		sb.append(lcsClusterNode.getLcsClusterEntryId());
		sb.append(StringPool.AMPERSAND);
		sb.append(
			getPublicRenderParameterName(request, "layoutLCSClusterNodeId"));
		sb.append(StringPool.EQUAL);
		sb.append(lcsClusterNode.getLcsClusterNodeId());

		return sb.toString();
	}

	public static String getLCSPortalURL() {
		String lcsPortalURL = Http.HTTP_WITH_SLASH.concat(
			PortletProps.get("osb.lcs.portlet.host.name"));

		String osbLCSPortletHostPort = PortletProps.get(
			"osb.lcs.portlet.host.port");

		if (Validator.equals(osbLCSPortletHostPort, Http.HTTP_PORT)) {
			return lcsPortalURL;
		}

		return lcsPortalURL.concat(StringPool.COLON).concat(
			osbLCSPortletHostPort);
	}

	public static int getLocalLCSClusterEntryType() {
		if (ClusterExecutorUtil.isEnabled()) {
			return LCSConstants.LCS_CLUSTER_ENTRY_TYPE_CLUSTER;
		}

		return LCSConstants.LCS_CLUSTER_ENTRY_TYPE_ENVIRONMENT;
	}

	public static boolean isCredentialsSet() {
		if (getCredentialsStatus() == CREDENTIALS_SET) {
			return true;
		}

		return false;
	}

	public static void setupCredentials()
		throws PortalException, SystemException {

		javax.portlet.PortletPreferences jxPortletPreferences =
			getJxPortletPreferences();

		String lcsAccessToken = jxPortletPreferences.getValue(
			"lcsAccessToken", null);
		String lcsAccessSecret = jxPortletPreferences.getValue(
			"lcsAccessSecret", null);

		if (Validator.isNull(lcsAccessToken) ||
			Validator.isNull(lcsAccessSecret)) {

			throw new SystemException("Unable to setup LCS credentials");
		}

		_jsonWebServiceClient.setLogin(lcsAccessToken);
		_jsonWebServiceClient.setPassword(lcsAccessSecret);

		_jsonWebServiceClient.resetHttpClient();
	}

	public void setJSONWebServiceClient(
		JSONWebServiceClient jsonWebServiceClient) {

		_jsonWebServiceClient = jsonWebServiceClient;
	}

	protected static javax.portlet.PortletPreferences getJxPortletPreferences()
		throws PortalException, SystemException {

		PortletPreferences portletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				CompanyConstants.SYSTEM, PortletKeys.PREFS_OWNER_TYPE_COMPANY,
				0, PortletKeys.MONITORING);

		return PortletPreferencesFactoryUtil.fromDefaultXML(
			portletPreferences.getPreferences());
	}

	protected static String getPublicRenderParameterName(
			HttpServletRequest request, String parameterName)
		throws Exception {

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			PortalUtil.getCompanyId(request), PortalUtil.getPortletId(request));

		PublicRenderParameter publicRenderParameter =
			portlet.getPublicRenderParameter(parameterName);

		if (publicRenderParameter == null) {
			return parameterName;
		}

		QName qName = publicRenderParameter.getQName();

		return PortletQNameUtil.getPublicRenderParameterName(qName);
	}

	private static JSONWebServiceClient _jsonWebServiceClient;

}