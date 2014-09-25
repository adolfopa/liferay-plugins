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

package com.liferay.portal.resiliency.spi.portlet.action;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.resiliency.spi.service.SPIDefinitionServiceUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseActionCommand;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * @author Michael C. Han
 */
public class StartSPIActionCommand extends BaseActionCommand {

	@Override
	protected void doProcessCommand(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception {

		long spiDefinitionId = ParamUtil.getLong(
			portletRequest, "spiDefinitionId");

		SPIDefinitionServiceUtil.startSPIinBackground(spiDefinitionId);
	}

}