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

import com.liferay.portal.kernel.util.StringUtil;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author Rachael Koestartyo
 */
public abstract class BaseTicketCommentsReportMetricsMessageListener
	extends BaseReportMetricsMessageListener {

	@Override
	protected String getEmailAddresses() {
		return "support-analytics-lesa-metrics@liferay.com";
	}

	@Override
	protected String getEntryReportName() {
		return "[" + getSupportTeam() + "] Engineers Ticket Comments";
	}

	protected Map<String, String> getParameterMap() {
		Map<String, String> reportParameters = super.getParameterMap();

		reportParameters.put("supportTeam", getSupportTeam());

		TimeZone timeZone = TimeZone.getTimeZone(getTimeZoneId());

		Calendar calendar = Calendar.getInstance();

		long timeOffset = timeZone.getOffset(calendar.getTime().getTime());

		reportParameters.put("timeOffsetFromUTC",
			StringUtil.valueOf(TimeUnit.MILLISECONDS.toHours(timeOffset)));

		return reportParameters;
	}

	@Override
	protected String getReportDefinitionName() {
		return "OSB_TicketCommentsByEngineerAndDay";
	}

	protected abstract String getSupportTeam();

	protected abstract String getTimeZoneId();

}