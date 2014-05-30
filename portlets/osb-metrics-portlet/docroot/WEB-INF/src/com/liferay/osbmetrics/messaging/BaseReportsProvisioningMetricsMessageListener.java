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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rachael Koestartyo
 */
public abstract class BaseReportsProvisioningMetricsMessageListener
	extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		OSBMetricsUtil.addReportEntry(
			_DEFINITION_REPORT_NAME, getEntryReportName(), getEmailAddresses(),
			getParameterMap());
	}

	protected abstract String getEmailAddresses();

	protected String getEntryReportName() {
		return _ENTRY_REPORT_NAME + " [" + getSupportRegion() + "]";
	}

	protected Map<String, String> getParameterMap() {
		Map<String, String> reportParameters = new HashMap<String, String>();

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.MONTH, -1);

		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		Format format = FastDateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd");

		reportParameters.put("endDate", format.format(calendar.getTime()));

		calendar.set(Calendar.DATE, 1);

		reportParameters.put("startDate", format.format(calendar.getTime()));
		reportParameters.put("supportRegion", getSupportRegion());

		return reportParameters;
	}

	protected abstract String getSupportRegion();

	private static final String _DEFINITION_REPORT_NAME =
		"OSB_ProvisioningMetricsByMonthAndSupportRegion";

	private static final String _ENTRY_REPORT_NAME = "Provisioning Metrics";

}