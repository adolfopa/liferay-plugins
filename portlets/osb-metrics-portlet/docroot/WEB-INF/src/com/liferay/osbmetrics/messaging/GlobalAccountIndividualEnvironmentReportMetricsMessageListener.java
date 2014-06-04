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
public class GlobalAccountIndividualEnvironmentReportMetricsMessageListener
	extends BaseReportMetricsMessageListener {

	@Override
	protected String getEmailAddresses() {
		return "support-analytics-lesa-metrics@liferay.com";
	}

	@Override
	protected String getEntryReportName() {
		return "[Global] Account Individual Environment Statistics";
	}

	@Override
	protected String getReportDefinitionName() {
		return "OSB_AccountIndividualEnvironmentStatisticsByMonth";
	}

}