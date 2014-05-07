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

package com.liferay.osbreports.hook.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.util.portlet.PortletProps;

/**
 * @author Brian Wing Shun Chan
 */
public class PortletPropsValues {

	public static final boolean AUDIT_PROCESSING_SCHEDULER_INCREMENTAL =
		GetterUtil.getBoolean(
			PortletProps.get(
				PortletPropsKeys.AUDIT_PROCESSING_SCHEDULER_INCREMENTAL));

	public static final int AUDIT_PROCESSING_SCHEDULER_INITIAL_DELAY =
		GetterUtil.getInteger(
			PortletProps.get(
				PortletPropsKeys.AUDIT_PROCESSING_SCHEDULER_INITIAL_DELAY));

	public static final String AUDIT_PROCESSING_SCHEDULER_INTERVAL =
		PortletProps.get(PortletPropsKeys.AUDIT_PROCESSING_SCHEDULER_INTERVAL);

	public static final String JIRA_DB = PortletProps.get(
		PortletPropsKeys.JIRA_DB);

	public static final String LRDCOM_DB = PortletProps.get(
		PortletPropsKeys.LRDCOM_DB);

	public static final String[] SUPPORT_REGIONS = PortletProps.getArray(
		PortletPropsKeys.SUPPORT_REGIONS);

}