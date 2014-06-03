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

import java.util.Date;
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

	@Override
	protected Map<String, String> getParameterMap() {
		Map<String, String> reportParameters = super.getParameterMap();

		reportParameters.put("supportTeam", getSupportTeam());

		TimeZone timeZone = TimeZone.getTimeZone(getTimeZoneId());
		TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		Date now = new Date();

		long hours = timeUnit.toHours(timeZone.getOffset(now.getTime()));

		reportParameters.put("timeOffsetFromUTC", String.valueOf(hours));

		return reportParameters;
	}

	@Override
	protected String getReportDefinitionName() {
		return "OSB_TicketCommentsByEngineerAndDay";
	}

	protected abstract String getSupportTeam();

	protected abstract String getTimeZoneId();

}