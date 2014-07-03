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

import com.liferay.lcs.messaging.HandshakeMessage;
import com.liferay.lcs.messaging.Message;
import com.liferay.lcs.service.LCSGatewayService;
import com.liferay.lcs.util.HandshakeManager;
import com.liferay.lcs.util.KeyGenerator;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Ivica Cardic
 */
public class SignoffTask implements Runnable {

	@Override
	public void run() {
		try {
			doRun();
		}
		catch (Exception e) {
			_handshakeManager.setPending(false);

			_log.error(e);
		}
	}

	public void setDeregister(boolean deregister) {
		_deregister = deregister;
	}

	public void setHeartbeatInterval(long heartbeatInterval) {
		_heartbeatInterval = heartbeatInterval;
	}

	protected void doRun() throws PortalException {
		if (_log.isInfoEnabled()) {
			_log.info("Initiate sign off");
		}

		List<ScheduledFuture<?>> scheduledFutures =
			_handshakeManager.getScheduledFutures();

		for (ScheduledFuture<?> scheduledFuture : scheduledFutures) {
			while (!scheduledFuture.isCancelled()) {
				scheduledFuture.cancel(true);
			}
		}

		scheduledFutures.clear();

		if (!_deregister) {
			String key = _keyGenerator.getKey();

			HandshakeMessage handshakeMessage = new HandshakeMessage();

			handshakeMessage.put(
				Message.KEY_SIGN_OFF, String.valueOf(_heartbeatInterval));
			handshakeMessage.setKey(key);

			_lcsGatewayService.sendMessage(handshakeMessage);
		}

		_handshakeManager.setPending(false);

		if (_log.isInfoEnabled()) {
			_log.info("Terminated connection");
		}
	}

	private static Log _log = LogFactoryUtil.getLog(SignoffTask.class);

	private boolean _deregister;

	@BeanReference(type = HandshakeManager.class)
	private HandshakeManager _handshakeManager;

	private long _heartbeatInterval;

	@BeanReference(type = KeyGenerator.class)
	private KeyGenerator _keyGenerator;

	@BeanReference(type = LCSGatewayService.class)
	private LCSGatewayService _lcsGatewayService;

}