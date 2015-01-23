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

package com.liferay.saml;

import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.io.Serializable;

import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;

/**
 * @author Mika Koivisto
 */
public class SamlSsoRequestContext implements Serializable {

	public static final int STAGE_AUTHENTICATED = 1;

	public static final int STAGE_INITIAL = 0;

	public SamlSsoRequestContext(
		String authnRequestXml, String peerEntityId,
		SAMLMessageContext<AuthnRequest, Response, NameID> samlMessageContext) {

		_authnRequestXml = authnRequestXml;
		_peerEntityId = peerEntityId;
		_samlMessageContext = samlMessageContext;
	}

	public String getAutnRequestXml() {
		return _authnRequestXml;
	}

	public String getPeerEntityId() {
		return _peerEntityId;
	}

	public SAMLMessageContext<AuthnRequest, Response, NameID>
		getSAMLMessageContext() {

		return _samlMessageContext;
	}

	public String getSamlSsoSessionId() {
		return _samlSsoSessionId;
	}

	public int getStage() {
		return _stage;
	}

	public User getUser() {
		try {
			return UserLocalServiceUtil.fetchUserById(_userId);
		}
		catch (Exception e) {
			return null;
		}
	}

	public long getUserId() {
		return _userId;
	}

	public boolean isNewSession() {
		return _isNewSession;
	}

	public void setNewSession(boolean isNewSession) {
		_isNewSession = isNewSession;
	}

	public void setSAMLMessageContext(
		SAMLMessageContext<AuthnRequest, Response, NameID> samlMessageContext) {

		_samlMessageContext = samlMessageContext;
	}

	public void setSamlSsoSessionId(String samlSsoSessionId) {
		_samlSsoSessionId = samlSsoSessionId;
	}

	public void setStage(int stage) {
		_stage = stage;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	private String _authnRequestXml;
	private boolean _isNewSession;
	private String _peerEntityId;
	private volatile SAMLMessageContext<AuthnRequest, Response, NameID>
		_samlMessageContext;
	private String _samlSsoSessionId;
	private int _stage;
	private long _userId;

}