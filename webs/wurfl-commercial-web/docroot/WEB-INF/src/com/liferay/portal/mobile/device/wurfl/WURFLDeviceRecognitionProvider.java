/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation; version 3.0 of the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 */

package com.liferay.portal.mobile.device.wurfl;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mobile.device.Device;
import com.liferay.portal.kernel.mobile.device.DeviceCapabilityFilter;
import com.liferay.portal.kernel.mobile.device.DeviceRecognitionProvider;
import com.liferay.portal.kernel.mobile.device.KnownDevices;
import com.liferay.portal.kernel.mobile.device.UnknownDevice;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.mobile.device.wurfl.util.PortletPropsValues;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.wurfl.core.WURFLEngine;
import net.sourceforge.wurfl.core.resource.WURFLResources;
import net.sourceforge.wurfl.core.resource.XMLResource;

/**
 * @author Milen Dyankov
 * @author Michael C. Han
 */
public class WURFLDeviceRecognitionProvider
	implements DeviceRecognitionProvider {

	public void afterPropertiesSet() throws Exception {
		reload();
	}

	@Override
	public Device detectDevice(HttpServletRequest request) {
		net.sourceforge.wurfl.core.Device wurflDevice =
			_wurflEngine.getDeviceForRequest(request);

		Device device = null;

		if (wurflDevice != null) {
			Map<String, String> capabilities = wurflDevice.getCapabilities();

			if ((capabilities != null) && !capabilities.isEmpty()) {
				device = new WURFLDevice(capabilities, _deviceCapabilityFilter);
			}
			else {
				device = UnknownDevice.getInstance();
			}
		}

		return device;
	}

	@Override
	public KnownDevices getKnownDevices() {
		return _knownDevices;
	}

	@Override
	public void reload() throws Exception {
		List<InputStream> inputStreams = new ArrayList<InputStream>();

		try {
			XMLResource xmlResource = getXMLResource(inputStreams);

			WURFLResources wurflResources = getWURFLResources(inputStreams);

			_wurflEngine.reload(xmlResource, wurflResources);

			_knownDevices.reload();
		}
		finally {
			for (InputStream inputStream : inputStreams) {
				StreamUtil.cleanUp(inputStream);
			}
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Initialised");
		}
	}

	@Override
	public void setDeviceCapabilityFilter(
		DeviceCapabilityFilter deviceCapabilityFilter) {

		_deviceCapabilityFilter = deviceCapabilityFilter;
	}

	public void setKnownDevices(KnownDevices knownDevices) {
		_knownDevices = knownDevices;
	}

	public void setWURFLEngine(WURFLEngine wurflEngine) {
		_wurflEngine = wurflEngine;
	}

	protected WURFLResources getWURFLResources(List<InputStream> inputStreams)
		throws FileNotFoundException {

		WURFLResources wurflResources = new WURFLResources();

		String[] fileNames = FileUtil.listFiles(
			PortletPropsValues.WURFL_DATABASE_PATCHES);

		for (String fileName : fileNames) {
			File file = new File(
				PortletPropsValues.WURFL_DATABASE_PATCHES, fileName);

			FileInputStream fileInputStream = new FileInputStream(file);

			inputStreams.add(fileInputStream);

			XMLResource xmlResource = new XMLResource(file);

			wurflResources.add(xmlResource);
		}

		return wurflResources;
	}

	protected XMLResource getXMLResource(List<InputStream> inputStreams)
		throws IOException {

		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			PortletPropsValues.WURFL_DATABASE_PRIMARY);

		if (inputStream == null) {
			throw new IllegalStateException(
				"Unable to find " + PortletPropsValues.WURFL_DATABASE_PRIMARY);
		}

		if (PortletPropsValues.WURFL_DATABASE_PRIMARY.endsWith(".gz")) {
			inputStream = new GZIPInputStream(inputStream);
		}
		else if (PortletPropsValues.WURFL_DATABASE_PRIMARY.endsWith(".jar") ||
				 PortletPropsValues.WURFL_DATABASE_PRIMARY.endsWith(".zip")) {

			ZipInputStream zipInputStream = new ZipInputStream(inputStream);

			inputStream = zipInputStream;

			zipInputStream.getNextEntry();
		}

		XMLResource xmlResource = new XMLResource(
			inputStream, PortletPropsValues.WURFL_DATABASE_PRIMARY);

		inputStreams.add(inputStream);

		return xmlResource;
	}

	private static Log _log = LogFactoryUtil.getLog(
		WURFLDeviceRecognitionProvider.class);

	private DeviceCapabilityFilter _deviceCapabilityFilter;
	private KnownDevices _knownDevices;
	private WURFLEngine _wurflEngine;

}