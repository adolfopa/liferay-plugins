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
import com.liferay.osb.lcs.model.CorpEntryIdentifier;
import com.liferay.osb.lcs.model.LCSClusterNode;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.QName;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.PublicRenderParameter;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portlet.PortletQNameUtil;
import com.liferay.util.portlet.PortletProps;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marko Cikos
 */
public class LCSPortalUtil {

	public static String getCorpEntryPageURL(
			HttpServletRequest request, CorpEntryIdentifier corpEntryIdentifier)
		throws Exception {

		StringBundler sb = new StringBundler(10);

		sb.append(getLCSPortalURL());
		sb.append(PortletProps.get("osb.lcs.portlet.page.corp.entry"));
		sb.append(StringPool.QUESTION);
		sb.append(PARAM_PORTLET_ID);
		sb.append(StringPool.EQUAL);
		sb.append(NAVIGATION_PORTLET_ID);
		sb.append(StringPool.AMPERSAND);
		sb.append(getPublicRenderParameterName(request, PARAM_CORP_ENTRY));
		sb.append(StringPool.EQUAL);
		sb.append(corpEntryIdentifier.getCorpEntryId());

		return sb.toString();
	}

	public static String getLCSClusterEntryPageURL(
			HttpServletRequest request, CorpEntryIdentifier corpEntryIdentifier,
			LCSClusterNode lcsClusterNode)
		throws Exception {

		StringBundler sb = new StringBundler(14);

		sb.append(getLCSPortalURL());
		sb.append(PortletProps.get("osb.lcs.portlet.page.lcs.cluster.entry"));
		sb.append(StringPool.QUESTION);
		sb.append(PARAM_PORTLET_ID);
		sb.append(StringPool.EQUAL);
		sb.append(NAVIGATION_PORTLET_ID);
		sb.append(StringPool.AMPERSAND);
		sb.append(getPublicRenderParameterName(request, PARAM_CORP_ENTRY));
		sb.append(StringPool.EQUAL);
		sb.append(corpEntryIdentifier.getCorpEntryId());
		sb.append(StringPool.AMPERSAND);
		sb.append(
			getPublicRenderParameterName(request, PARAM_LCS_CLUSTER_ENTRY));
		sb.append(StringPool.EQUAL);
		sb.append(lcsClusterNode.getLcsClusterEntryId());

		return sb.toString();
	}

	public static String getLCSClusterNodePageURL(
			HttpServletRequest request, CorpEntryIdentifier corpEntryIdentifier,
			LCSClusterNode lcsClusterNode)
		throws Exception {

		StringBundler sb = new StringBundler(18);

		sb.append(getLCSPortalURL());
		sb.append(PortletProps.get("osb.lcs.portlet.page.lcs.cluster.node"));
		sb.append(StringPool.QUESTION);
		sb.append(PARAM_PORTLET_ID);
		sb.append(StringPool.EQUAL);
		sb.append(NAVIGATION_PORTLET_ID);
		sb.append(StringPool.AMPERSAND);
		sb.append(getPublicRenderParameterName(request, PARAM_CORP_ENTRY));
		sb.append(StringPool.EQUAL);
		sb.append(corpEntryIdentifier.getCorpEntryId());
		sb.append(StringPool.AMPERSAND);
		sb.append(
			getPublicRenderParameterName(request, PARAM_LCS_CLUSTER_ENTRY));
		sb.append(StringPool.EQUAL);
		sb.append(lcsClusterNode.getLcsClusterEntryId());
		sb.append(StringPool.AMPERSAND);
		sb.append(
			getPublicRenderParameterName(request, PARAM_LCS_CLUSTER_NODE));
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

	private static final String NAVIGATION_PORTLET_ID = "5_WAR_osblcsportlet";

	private static final String PARAM_CORP_ENTRY = "layoutCorpEntryId";

	private static final String PARAM_LCS_CLUSTER_ENTRY =
		"layoutLCSClusterEntryId";

	private static final String PARAM_LCS_CLUSTER_NODE =
		"layoutLCSClusterNodeId";

	private static final String PARAM_PORTLET_ID = "p_p_id";

}