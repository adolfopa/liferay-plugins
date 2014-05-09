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
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
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
public class WorkflowGitHubRequestProcessor extends BaseGitHubRequestProcessor {

	public WorkflowGitHubRequestProcessor() {
		_initProfileRepository();

		_initProfileJobs();
	}

	@Override
	public void destroy() {
		for (ScheduledExecutorService scheduledExecutorService :
				_scheduledExecutorServices) {

			scheduledExecutorService.shutdown();
		}
	}

	public void process(JSONObject payloadJSONObject) throws Exception {
	}

	private void _initProfileJobs() {
		ScheduledExecutorService scheduledExecutorService =
			Executors.newSingleThreadScheduledExecutor();

		// Every 8 hours sync the workflow branches with ee-6.1.x. Each profile
		// has a unique git workflow branch (workflow-metrics, workflow-uat).

		scheduledExecutorService.scheduleAtFixedRate(
			new Runnable() {

				@Override
				public void run() {
					for (String profileName : _PROFILE_NAMES.split(",")) {
						Queue<WorkflowMessage> queue =
							_queueToProfileNameMap.get(profileName);

						WorkflowMessage workflowMessage = new WorkflowMessage(
							WorkflowMessage.SYNC_PROFILE_BRANCH, null);

						queue.offer(workflowMessage);
					}
				}

			}, 1, 480, TimeUnit.MINUTES);

		_scheduledExecutorServices.add(scheduledExecutorService);

		// Every 30 seconds, check for a patch request

		for (String profileName : _PROFILE_NAMES.split(",")) {
			Queue<WorkflowMessage> queue =
				new ConcurrentLinkedQueue<WorkflowMessage>();

			_queueToProfileNameMap.put(profileName, queue);

			WorkflowProfilePollerProcessor workflowProfileProcessor =
				new WorkflowProfilePollerProcessor(profileName, queue);

			scheduledExecutorService =
				Executors.newSingleThreadScheduledExecutor();

			scheduledExecutorService.scheduleAtFixedRate(
				workflowProfileProcessor, 0, 30, TimeUnit.SECONDS);

			_scheduledExecutorServices.add(scheduledExecutorService);
		}
	}

	private void _initProfileRepository() {
		for (String profileName : _PROFILE_NAMES.split(",")) {

			// Initialize plugins directory

			String pluginsPathname =
				_PATHNAME_WORKFLOW_DIR + "/profiles/" + profileName +
					"/plugins";

			File pluginsDir = new File(pluginsPathname);

			if (!pluginsDir.exists()) {
				String s = _PATHNAME_PLUGINS_DIR + " " + pluginsPathname;

				WorkflowProcessUtil.execute(
					"git new-workdir " + s, new File(_PATHNAME_PLUGINS_DIR));

				if ((_APP_SERVER_DIR != null) &&
					(_APP_SERVER_DIR.length() != 0)) {

					StringBuilder sb = new StringBuilder();

					sb.append(pluginsPathname);
					sb.append("/build.");
					sb.append(System.getenv("USERNAME"));
					sb.append(".properties");

					try {
						FileUtils.writeStringToFile(
							new File(sb.toString()),
							"app.server.dir=" + _APP_SERVER_DIR, "UTF-8",
							false);
					}
					catch (IOException ioe) {
						_log.error(ioe, ioe);
					}
				}
			}

			// Initialize Git configuration

			StringBuilder originURLSB = new StringBuilder();

			originURLSB.append("https://");
			originURLSB.append(_GITHUB_USER_TOKEN);
			originURLSB.append(":x-oauth-basic@github.com/");
			originURLSB.append(_GITHUB_USER_TOKEN);
			originURLSB.append("/liferay-plugins-ee.git");

			WorkflowProcessUtil.execute(
				"git config remote.origin.url " + originURLSB.toString(),
				pluginsDir);

			StringBuilder upstreamURLSB = new StringBuilder();

			upstreamURLSB.append("https://");
			upstreamURLSB.append(_GITHUB_USER_TOKEN);
			upstreamURLSB.append(":x-oauth-basic@github.com");
			upstreamURLSB.append("/liferay/liferay-plugins-ee.git");

			WorkflowProcessUtil.execute(
				"git config remote.upstream.url " + upstreamURLSB.toString(),
				pluginsDir);
		}
	}

	private static final String _APP_SERVER_DIR = "/opt/java/liferay/tomcat";

	private static final String _GITHUB_USER_TOKEN = "XXXXXXXXXXX";

	private static final String _PATHNAME_PLUGINS_DIR =
		"/var/peek/builder-dependencies/plugins";

	private static final String _PATHNAME_WORKFLOW_DIR = "/var/workflow";

	private static final String _PROFILE_NAMES = "devops,metrics,training,uat";

	private static Log _log = LogFactory.getLog(
		WorkflowGitHubRequestProcessor.class);

	private Map<String, Queue<WorkflowMessage>> _queueToProfileNameMap =
		new ConcurrentHashMap<String, Queue<WorkflowMessage>>();
	private List<ScheduledExecutorService> _scheduledExecutorServices =
		new ArrayList<ScheduledExecutorService>();

}