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

package com.liferay.saml.hook.events;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SessionAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.saml.model.SamlSpSession;
import com.liferay.saml.service.SamlSpSessionLocalServiceUtil;
import com.liferay.saml.util.SamlUtil;

import javax.servlet.http.HttpSession;
public class SamlSpSessionDestroyAction extends SessionAction {

	@Override
	public void run(HttpSession session) throws ActionException {
		if (!SamlUtil.isEnabled() || !SamlUtil.isRoleSp()) {
			return;
		}

		try {
			SamlSpSession samlSpSession =
				SamlSpSessionLocalServiceUtil.getSamlSpSessionByJSessionId(
					session.getId());

			if (_log.isDebugEnabled()) {
				_log.debug(
					"HttpSession expired with jsessionid: " + session.getId() +
						" expiring SamlSpSession with samlSpSessionKey: " +
							samlSpSession.getSamlSpSessionKey());
			}

			SamlSpSessionLocalServiceUtil.deleteSamlSpSession(
				samlSpSession.getSamlSpSessionId());
		}
		catch (PortalException e) {
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		SamlSpSessionDestroyAction.class);
}