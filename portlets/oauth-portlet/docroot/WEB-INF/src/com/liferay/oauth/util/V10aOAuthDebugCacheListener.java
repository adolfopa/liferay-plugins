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

package com.liferay.oauth.util;

import com.liferay.portal.kernel.cache.CacheListener;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;

/**
 * @author Igor Beslic
 */
public class V10aOAuthDebugCacheListener implements CacheListener {

	@Override
	public void dispose() {
	}

	@Override
	public void notifyEntryEvicted(
			PortalCache portalCache, Serializable key, Object value,
			int timeToLive)
		throws PortalCacheException {

		logDebug("Entry evicted", key, value);
	}

	@Override
	public void notifyEntryExpired(
			PortalCache portalCache, Serializable key, Object value,
			int timeToLive)
		throws PortalCacheException {

		logDebug("Entry expired", key, value);
	}

	@Override
	public void notifyEntryPut(
			PortalCache portalCache, Serializable key, Object value,
			int timeToLive)
		throws PortalCacheException {

		logDebug("Entry put", key, value);
	}

	@Override
	public void notifyEntryRemoved(
			PortalCache portalCache, Serializable key, Object value,
			int timeToLive)
		throws PortalCacheException {

		logDebug("Entry removed", key, value);
	}

	@Override
	public void notifyEntryUpdated(
			PortalCache portalCache, Serializable key, Object value,
			int timeToLive)
		throws PortalCacheException {

		logDebug("Entry updated", key, value);
	}

	@Override
	public void notifyRemoveAll(PortalCache portalCache)
		throws PortalCacheException {

		if (_log.isDebugEnabled()) {
			_log.debug("Remove all " + portalCache.getName());
		}
	}

	protected void logDebug(String method, Serializable key, Object value) {
		if (!_log.isDebugEnabled()) {
			return;
		}

		if (!(value instanceof OAuthAccessor)) {
			return;
		}

		OAuthAccessor oAuthAccessor = (OAuthAccessor)value;

		StringBundler sb = new StringBundler(7);

		sb.append(method);
		sb.append("  ");
		sb.append(key);
		sb.append(":");
		sb.append(oAuthAccessor.getRequestToken());
		sb.append(":");
		sb.append(oAuthAccessor.getProperty(OAuthAccessorConstants.AUTHORIZED));

		_log.debug(sb.toString());
	}

	private static Log _log = LogFactoryUtil.getLog(
		V10aOAuthDebugCacheListener.class);

}