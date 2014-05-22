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

package com.liferay.github.util;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

/**
 * @author Brian Wing Shun Chan
 * @author Peter Shin
 */
public class DevOpsGitHubRequestProcessor extends BaseGitHubRequestProcessor {

	public DevOpsGitHubRequestProcessor() {
		initProfileGitRepositories();

		initScheduledExecutorServices();
	}

	@Override
	public void destroy() {
		for (ScheduledExecutorService scheduledExecutorService :
				_scheduledExecutorServices) {

			scheduledExecutorService.shutdown();
		}
	}

	@Override
	public void process(JSONObject payloadJSONObject) throws Exception {
		if (!DevOpsUtil.isValidAction(payloadJSONObject)) {
			return;
		}

		String profileName = DevOpsUtil.getProfileName(payloadJSONObject);

		if (DevOpsUtil.isValidDeveloper(payloadJSONObject, profileName)) {
			DevOpsPatchRequestProcessor devOpsPatchRequestProcessor =
				_devOpsPatchRequestProcessors.get(profileName);

			devOpsPatchRequestProcessor.addPayloadJSONObject(payloadJSONObject);
		}
	}

	public synchronized void updatePeekGitRepository(String profileName) {
	}

	public void updateProfileGitRepository(String profileName) {
	}

	protected File getProfileGitRepositoryDir(String profileName) {
		return null;
	}

	protected String[] getProfileNames() {
		return new String[0];
	}

	protected void initProfileGitRepositories() {
		for (String profileName : getProfileNames()) {
			initProfileGitRepository(profileName);
		}
	}

	protected void initProfileGitRepository(String profileName) {
		File profileGitRepositoryDir = getProfileGitRepositoryDir(profileName);

		if (!profileGitRepositoryDir.exists()) {
			DevOpsProcessUtil.execute(
				new File(_PLUGINS_GIT_REPOSITORY_DIR_NAME),
				"git new-workdir " + _PLUGINS_GIT_REPOSITORY_DIR_NAME +
					" " + profileGitRepositoryDir);

			try {
				FileUtils.writeStringToFile(
					new File(
						profileGitRepositoryDir + "/build." +
							System.getenv("USERNAME") + ".properties"),
					"app.server.dir=/opt/java/liferay/tomcat", "UTF-8", false);
			}
			catch (IOException ioe) {
				_log.error(ioe, ioe);
			}
		}
	}

	protected void initScheduledExecutorService(String profileName) {
		final DevOpsPatchRequestProcessor devOpsPatchRequestProcessor =
			new DevOpsPatchRequestProcessor(this, profileName);

		_devOpsPatchRequestProcessors.put(
			profileName, devOpsPatchRequestProcessor);

		ScheduledExecutorService scheduledExecutorService =
			Executors.newSingleThreadScheduledExecutor();

		scheduledExecutorService.scheduleAtFixedRate(
			new Runnable() {

				@Override
				public void run() {
					devOpsPatchRequestProcessor.process();
				}

			},
			0, 30, TimeUnit.SECONDS);

		_scheduledExecutorServices.add(scheduledExecutorService);
	}

	protected void initScheduledExecutorServices() {
		ScheduledExecutorService scheduledExecutorService =
			Executors.newSingleThreadScheduledExecutor();

		scheduledExecutorService.scheduleAtFixedRate(
			new Runnable() {

				@Override
				public void run() {
					for (String profileName : getProfileNames()) {
						DevOpsPatchRequestProcessor
							devOpsPatchRequestProcessor =
								_devOpsPatchRequestProcessors.get(profileName);

						if (devOpsPatchRequestProcessor.isRunning()) {
							return;
						}

						updateProfileGitRepository(profileName);
					}
				}

			}, 1, 480, TimeUnit.MINUTES);

		_scheduledExecutorServices.add(scheduledExecutorService);

		for (String profileName : getProfileNames()) {
			initScheduledExecutorService(profileName);
		}
	}

	private static final String _PEEK_GIT_REPOSITORY_DIR_NAME =
		"/var/peek/repo";

	private static final String _PLUGINS_GIT_REPOSITORY_DIR_NAME =
		"/var/peek/builder-dependencies/plugins";

	private static Log _log = LogFactory.getLog(
		DevOpsGitHubRequestProcessor.class);

	private Map<String, DevOpsPatchRequestProcessor>
		_devOpsPatchRequestProcessors =
			new HashMap<String, DevOpsPatchRequestProcessor>();
	private List<ScheduledExecutorService> _scheduledExecutorServices =
		new ArrayList<ScheduledExecutorService>();

}