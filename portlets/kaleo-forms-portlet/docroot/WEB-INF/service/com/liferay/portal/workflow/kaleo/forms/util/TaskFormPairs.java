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

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class TaskFormPairs implements Iterable<TaskFormPair> {

	public static TaskFormPairs parse(String data) {
		TaskFormPairs taskFormPairs = new TaskFormPairs();

		for (String taskForm : StringUtil.split(data)) {
			String[] keyValue = StringUtil.split(taskForm, StringPool.COLON);

			TaskFormPair taskFormPair = new TaskFormPair(
				keyValue[0], Long.valueOf(keyValue[1]));

			taskFormPairs.add(taskFormPair);
		}

		return taskFormPairs;
	}

	public void add(int index, TaskFormPair taskFormPair) {
		_taskFormPairs.add(index, taskFormPair);
	}

	public void add(TaskFormPair taskFormPair) {
		_taskFormPairs.add(taskFormPair);
	}

	@Override
	public Iterator<TaskFormPair> iterator() {
		return _taskFormPairs.iterator();
	}

	public List<TaskFormPair> list() {
		return _taskFormPairs;
	}

	public int size() {
		return _taskFormPairs.size();
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(_taskFormPairs.size() * 4);

		for (TaskFormPair taskFormPair : _taskFormPairs) {
			sb.append(taskFormPair.getWorkflowTaskName());
			sb.append(StringPool.COLON);
			sb.append(taskFormPair.getDDMTemplateId());
			sb.append(StringPool.COMMA);
		}

		if (sb.index() > 0) {
			sb.setIndex(sb.index() - 1);
		}

		return sb.toString();
	}

	private List<TaskFormPair> _taskFormPairs = new ArrayList<>();

}