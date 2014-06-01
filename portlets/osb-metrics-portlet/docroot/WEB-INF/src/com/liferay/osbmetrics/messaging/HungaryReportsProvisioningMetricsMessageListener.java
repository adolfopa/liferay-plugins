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

package com.liferay.osbmetrics.messaging;

/**
 * @author Rachael Koestartyo
 */
public class HungaryReportsProvisioningMetricsMessageListener
	extends BaseReportsProvisioningMetricsMessageListener {

	@Override
	protected String getEmailAddresses() {
		return _EMAIL_ADDRESSES;
	}

	@Override
	protected String getSupportRegion() {
		return _SUPPORT_REGION;
	}

	private static final String _EMAIL_ADDRESSES =
		"support-analytics-metrics-hu@liferay.com";

	private static final String _SUPPORT_REGION = "Support-HU";

}