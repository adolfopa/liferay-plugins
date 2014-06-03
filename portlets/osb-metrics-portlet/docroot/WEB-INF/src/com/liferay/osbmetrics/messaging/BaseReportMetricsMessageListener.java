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
public abstract class BaseReportMetricsMessageListener
	extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		OSBMetricsUtil.addReportEntry(
			getReportDefinitionName(), getEntryReportName(),
			getEmailAddresses(), getParameterMap());
	}

	protected abstract String getEmailAddresses();

	protected abstract String getEntryReportName();

	protected Map<String, String> getParameterMap() {
		Map<String, String> parameterMap = new HashMap<String, String>();

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.MONTH, -1);

		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		Format format = FastDateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd");

		parameterMap.put("endDate", format.format(calendar.getTime()));

		calendar.set(Calendar.DATE, 1);

		parameterMap.put("startDate", format.format(calendar.getTime()));

		return parameterMap;
	}

	protected abstract String getReportDefinitionName();

}