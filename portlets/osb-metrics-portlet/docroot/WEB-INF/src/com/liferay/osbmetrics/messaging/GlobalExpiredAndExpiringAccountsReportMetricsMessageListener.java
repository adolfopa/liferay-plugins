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

import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;

import java.text.Format;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rachael Koestartyo
 */
public class GlobalExpiredAndExpiringAccountsReportMetricsMessageListener
	extends BaseReportMetricsMessageListener {

	@Override
	protected String getEmailAddresses() {
		return "support-analytics-sales-metrics@liferay.com";
	}

	@Override
	protected String getEntryReportName() {
		return "[Global] Expired and Expiring Accounts";
	}

	@Override
	protected Map<String, String> getParameterMap() {
		Map<String, String> parameterMap = new HashMap<String, String>();

		parameterMap.put("numberOfExpiringDays", "31");

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.DATE, 1);

		Format format = FastDateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd");

		parameterMap.put("startDate", format.format(calendar.getTime()));

		return parameterMap;
	}

	@Override
	protected String getReportDefinitionName() {
		return "OSB_ExpiredAndExpiringAccounts";
	}

}