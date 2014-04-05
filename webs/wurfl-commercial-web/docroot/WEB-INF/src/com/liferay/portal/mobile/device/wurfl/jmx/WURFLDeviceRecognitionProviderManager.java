/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation; version 3.0 of the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 */

package com.liferay.portal.mobile.device.wurfl.jmx;

import com.liferay.portal.mobile.device.wurfl.WURFLDeviceRecognitionProvider;

/**
 * @author Michael C. Han
 */
public class WURFLDeviceRecognitionProviderManager
	implements WURFLDeviceRecognitionProviderMBean {

	@Override
	public void reload() throws Exception {
		_wurflDeviceRecognitionProvider.reload();
	}

	public void setWURFLDeviceRecognitionProvider(
		WURFLDeviceRecognitionProvider wurflDeviceRecognitionProvider) {

		_wurflDeviceRecognitionProvider = wurflDeviceRecognitionProvider;
	}

	private WURFLDeviceRecognitionProvider _wurflDeviceRecognitionProvider;

}