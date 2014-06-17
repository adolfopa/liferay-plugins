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

package com.liferay.lcs.service.impl;

import com.liferay.lcs.messaging.Message;
import com.liferay.lcs.service.LCSGatewayService;
import com.liferay.lcs.util.CompressionUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.security.auth.PrincipalException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.CredentialException;

/**
 * @author Ivica Cardic
 * @author Igor Beslic
 */
public class LCSGatewayServiceImpl
	extends BaseLCSServiceImpl implements LCSGatewayService {

	@Override
	public void deleteMessages(String key)
		throws PortalException, SystemException {

		try {
			doGet(_URL_LCS_GATEWAY_DELETE_MESSAGES, "key", key);
		}
		catch (CredentialException ce) {
			throw new PrincipalException(ce);
		}
		catch (IOException ioe) {
			throw new SystemException(ioe);
		}
	}

	@Override
	public List<Message> getMessages(String key)
		throws PortalException, SystemException {

		try {
			List<Message> messages = new ArrayList<Message>();

			List<String> messageJSONs = doGetToList(
				String.class, _URL_LCS_GATEWAY_GET_MESSAGES, "key", key);

			for (String messageJSON : messageJSONs) {
				Message message = (Message)fromJSON(messageJSON);

				if (_log.isDebugEnabled()) {
					_log.debug("Getting " + message);
				}

				messages.add(message);
			}

			return messages;
		}
		catch (CredentialException ce) {
			throw new PrincipalException(ce);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public void sendMessage(Message message)
		throws PortalException, SystemException {

		String json = toJSON(message);

		try {
			json = CompressionUtil.compress(json);

			if (_log.isDebugEnabled()) {
				_log.debug("Sending " + message);
			}

			doPost(_URL_LCS_GATEWAY_SEND_MESSAGE, "json", json);
		}
		catch (CredentialException ce) {
			throw new PrincipalException(ce);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	private static final String _URL_LCS_GATEWAY =
		"/osb-lcs-gateway-web/api/jsonws/lcsgateway";

	private static final String _URL_LCS_GATEWAY_DELETE_MESSAGES =
		LCSGatewayServiceImpl._URL_LCS_GATEWAY + "/delete-messages";

	private static final String _URL_LCS_GATEWAY_GET_MESSAGES =
		LCSGatewayServiceImpl._URL_LCS_GATEWAY + "/get-messages";

	private static final String _URL_LCS_GATEWAY_SEND_MESSAGE =
		LCSGatewayServiceImpl._URL_LCS_GATEWAY + "/send-message";

	private static Log _log = LogFactoryUtil.getLog(
		LCSGatewayServiceImpl.class);

}