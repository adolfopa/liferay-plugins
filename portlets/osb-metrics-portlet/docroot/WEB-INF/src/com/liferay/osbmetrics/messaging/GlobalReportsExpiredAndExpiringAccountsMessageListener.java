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

import com.liferay.osbmetrics.util.OSBMetricsUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;

import java.text.Format;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rachael Koestartyo
 */
public class GlobalReportsExpiredAndExpiringAccountsMessageListener
	extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		OSBMetricsUtil.addReportEntry(
			"OSB_ExpiredAndExpiringAccounts", "Expired and Expiring Accounts",
			"support-analytics-sales-metrics@liferay.com", getParameterMap());
	}

	protected Map<String, String> getParameterMap() {
		Map<String, String> parameterMap = new HashMap<String, String>();

		parameterMap.put("numberOfExpiringDays", "31");

		Format format = FastDateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd");

		parameterMap.put("startDate", format.format(new Date()));

		return parameterMap;
	}

}