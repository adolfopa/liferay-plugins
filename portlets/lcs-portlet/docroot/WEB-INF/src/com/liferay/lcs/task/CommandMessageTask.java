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

package com.liferay.lcs.task;

import com.liferay.jsonwebserviceclient.JSONWebServiceUnavailableException;
import com.liferay.lcs.messaging.Message;
import com.liferay.lcs.messaging.RequestCommandMessage;
import com.liferay.lcs.service.LCSGatewayService;
import com.liferay.lcs.util.HandshakeManager;
import com.liferay.lcs.util.KeyGenerator;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSender;

import java.util.List;

/**
 * @author Ivica Cardic
 * @author Igor Beslic
 */
public class CommandMessageTask implements Runnable {

	@Override
	public void run() {
		try {
			doRun();
		}
		catch (Exception e) {
			_log.error(e, e);

			if (e.getCause() instanceof JSONWebServiceUnavailableException) {
				_handshakeManager.handleLCSGatewayUnavailable();
			}
		}
	}

	public void setHandshakeManager(HandshakeManager handshakeManager) {
		_handshakeManager = handshakeManager;
	}

	public void setKeyGenerator(KeyGenerator keyGenerator) {
		_keyGenerator = keyGenerator;
	}

	public void setLCSGatewayService(LCSGatewayService lcsGatewayService) {
		_lcsGatewayService = lcsGatewayService;
	}

	public void setMessageSender(SingleDestinationMessageSender messageSender) {
		_messageSender = messageSender;
	}

	protected void doRun() throws Exception {
		if (!_handshakeManager.isReady()) {
			if (_log.isDebugEnabled()) {
				_log.debug("Waiting for handshake manager");
			}

			return;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Checking messages for " + _keyGenerator.getKey());
		}

		List<Message> messages = _lcsGatewayService.getMessages(
			_keyGenerator.getKey());

		for (Message message : messages) {
			if (message instanceof RequestCommandMessage) {
				_messageSender.send(message);

				_handshakeManager.putLCSConnectionMetadata(
					"messageTaskTime",
					String.valueOf(System.currentTimeMillis()));
			}
			else {
				_log.error("Unknown message " + message);
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(CommandMessageTask.class);

	@BeanReference(type = HandshakeManager.class)
	private HandshakeManager _handshakeManager;

	@BeanReference(type = KeyGenerator.class)
	private KeyGenerator _keyGenerator;

	@BeanReference(type = LCSGatewayService.class)
	private LCSGatewayService _lcsGatewayService;

	@BeanReference(name = "messageSender.lcs_commands")
	private SingleDestinationMessageSender _messageSender;

}