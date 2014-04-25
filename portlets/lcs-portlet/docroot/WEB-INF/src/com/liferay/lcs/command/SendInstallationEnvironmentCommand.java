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

package com.liferay.lcs.command;

import com.liferay.lcs.messaging.RequestCommandMessage;
import com.liferay.lcs.messaging.ResponseCommandMessage;
import com.liferay.lcs.service.LCSGatewayService;
import com.liferay.lcs.util.ResponseCommandMessageUtil;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Igor Beslic
 */
public class SendInstallationEnvironmentCommand implements Command {

	@Override
	public void execute(RequestCommandMessage requestCommandMessage)
		throws PortalException, SystemException {

		Map<String, Object> payload = new HashMap<String, Object>();

		payload.put("hardwareMetadata", getHardwareMetadata());
		payload.put("softwareMetadata", getSoftwareMetadata());

		ResponseCommandMessage responseCommandMessage =
			ResponseCommandMessageUtil.createResponseCommandMessage(
				requestCommandMessage, payload);

		_lcsGatewayService.sendMessage(responseCommandMessage);
	}

	protected Map<String, String> getHardwareMetadata() {
		Map<String, String> hardwareMetadata = new HashMap<String, String>();

		OperatingSystemMXBean operatingSystemMXBean =
			ManagementFactory.getOperatingSystemMXBean();

		hardwareMetadata.put(
			"availableProcessors",
			String.valueOf(operatingSystemMXBean.getAvailableProcessors()));

		File[] roots = File.listRoots();

		if (roots.length > 0) {
			hardwareMetadata.put("fs.root", roots[0].getAbsolutePath());
			hardwareMetadata.put(
				"fs.root.total.space",
				String.valueOf(roots[0].getTotalSpace()));
			hardwareMetadata.put(
				"fs.root.usable.space",
				String.valueOf(roots[0].getUsableSpace()));
		}

		String javaVendor = SystemProperties.get("java.vendor");

		if (!javaVendor.startsWith("Oracle")) {
			return hardwareMetadata;
		}

		try {
			com.sun.management.OperatingSystemMXBean sunOperatingSystemMXBean =
				(com.sun.management.OperatingSystemMXBean)operatingSystemMXBean;

			hardwareMetadata.put(
				"physical.memory.total",
				String.valueOf(
					sunOperatingSystemMXBean.getTotalPhysicalMemorySize()));
			hardwareMetadata.put(
				"physical.memory.free",
				String.valueOf(
					sunOperatingSystemMXBean.getFreePhysicalMemorySize()));
			hardwareMetadata.put(
				"swap.free",
				String.valueOf(
					sunOperatingSystemMXBean.getFreeSwapSpaceSize()));
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to get extra hardware metadata");
			}
		}

		return hardwareMetadata;
	}

	protected Map<String, String> getSoftwareMetadata() {
		Map<String, String> softwareMetadata = new HashMap<String, String>();

		for (String key : _SYSTEM_PROPERTIES_SOFTWARE_KEYS) {
			String value = SystemProperties.get(key);

			if (Validator.isNotNull(value)) {
				softwareMetadata.put(key, value);
			}
		}

		softwareMetadata.put("appsrv.name", ServerDetector.getServerId());

		DB db = DBFactoryUtil.getDB();

		String databaseName = db.getType();

		softwareMetadata.put("database.name", databaseName);

		return softwareMetadata;
	}

	private static final String[] _SYSTEM_PROPERTIES_SOFTWARE_KEYS = {
		"file.encoding", "java.vendor", "java.version", "java.vm.name",
		"os.arch", "os.name", "os.version", "user.timezone"
	};

	private static Log _log = LogFactoryUtil.getLog(
		SendInstallationEnvironmentCommand.class);

	@BeanReference(type = LCSGatewayService.class)
	private LCSGatewayService _lcsGatewayService;

}