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

package com.liferay.portal.workflow.kaleo.forms.util;

import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class TaskFormPairsSerializer {

	public static List<ObjectValuePair<String, Long>> deserialize(
		String taskFormPairsData) {

		List<ObjectValuePair<String, Long>> taskFormPairs =
			new ArrayList<ObjectValuePair<String, Long>>();

		for (String taskForm : StringUtil.split(taskFormPairsData)) {
			String[] keyValue = StringUtil.split(taskForm, "#");

			ObjectValuePair<String, Long> taskFormPair =
				new ObjectValuePair<String, Long>();

			taskFormPair.setKey(keyValue[0]);
			taskFormPair.setValue(Long.valueOf(keyValue[1]));

			taskFormPairs.add(taskFormPair);
		}

		return taskFormPairs;
	}

	public static String serialize(
		List<ObjectValuePair<String, Long>> taskFormPairs,
		String initialStateName) {

		StringBundler sb = new StringBundler(taskFormPairs.size() * 4);

		for (ObjectValuePair<String, Long> taskFormPair : taskFormPairs) {
			if (initialStateName.equals(taskFormPair.getKey())) {
				continue;
			}

			sb.append(taskFormPair.getKey());
			sb.append("#");
			sb.append(taskFormPair.getValue());
			sb.append(",");
		}

		if (sb.index() > 0) {
			sb.setIndex(sb.index() - 1);
		}

		return sb.toString();
	}

}