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

package com.liferay.portal.workflow.kaleo.forms.portlet;

import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.workflow.WorkflowInstanceManagerUtil;
import com.liferay.portal.kernel.workflow.WorkflowTaskDueDateException;
import com.liferay.portal.kernel.workflow.WorkflowTaskManagerUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.workflow.kaleo.forms.util.ActionKeys;
import com.liferay.portal.workflow.kaleo.forms.util.WebKeys;
import com.liferay.portlet.dynamicdatalists.model.DDLRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * @author Marcellus Tavares
 */
public class KaleoFormsDisplayPortlet extends BaseKaleoFormsPorlet {

	public void assignWorkflowTask(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long workflowTaskId = ParamUtil.getLong(
			actionRequest, "workflowTaskId");
		long assigneeUserId = ParamUtil.getLong(
			actionRequest, "assigneeUserId");
		String comment = HtmlUtil.stripHtml(
			ParamUtil.getString(actionRequest, "comment"));

		WorkflowTaskManagerUtil.assignWorkflowTaskToUser(
			themeDisplay.getCompanyId(), themeDisplay.getUserId(),
			workflowTaskId, assigneeUserId, comment, null, null);
	}

	public void completeForm(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		UploadPortletRequest uploadPortletRequest =
			PortalUtil.getUploadPortletRequest(actionRequest);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDLRecord.class.getName(), uploadPortletRequest);

		checkKaleoProcessPermission(serviceContext, ActionKeys.COMPLETE_FORM);

		updateDDLRecord(serviceContext);

		long workflowTaskId = ParamUtil.getLong(
			uploadPortletRequest, "workflowTaskId");

		List<String> transitionNames =
			WorkflowTaskManagerUtil.getNextTransitionNames(
				serviceContext.getCompanyId(), serviceContext.getUserId(),
				workflowTaskId);

		if (transitionNames.size() == 1) {
			WorkflowTaskManagerUtil.completeWorkflowTask(
				serviceContext.getCompanyId(), serviceContext.getUserId(),
				workflowTaskId, null, null, null);
		}
	}

	public void completeWorkflowTask(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long workflowTaskId = ParamUtil.getLong(
			actionRequest, "workflowTaskId");

		String transitionName = ParamUtil.getString(
			actionRequest, "transitionName");
		String comment = HtmlUtil.stripHtml(
			ParamUtil.getString(actionRequest, "comment"));

		WorkflowTaskManagerUtil.completeWorkflowTask(
			themeDisplay.getCompanyId(), themeDisplay.getUserId(),
			workflowTaskId, transitionName, comment, null);
	}

	public void deleteWorkflowInstance(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long workflowInstanceId = ParamUtil.getLong(
			actionRequest, "workflowInstanceId");

		WorkflowInstanceManagerUtil.deleteWorkflowInstance(
			themeDisplay.getCompanyId(), workflowInstanceId);
	}

	public void updateWorkflowTask(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long workflowTaskId = ParamUtil.getLong(
			actionRequest, "workflowTaskId");

		String comment = HtmlUtil.stripHtml(
			ParamUtil.getString(actionRequest, "comment"));

		int dueDateMonth = ParamUtil.getInteger(actionRequest, "dueDateMonth");
		int dueDateDay = ParamUtil.getInteger(actionRequest, "dueDateDay");
		int dueDateYear = ParamUtil.getInteger(actionRequest, "dueDateYear");
		int dueDateHour = ParamUtil.getInteger(actionRequest, "dueDateHour");
		int dueDateMinute = ParamUtil.getInteger(
			actionRequest, "dueDateMinute");
		int dueDateAmPm = ParamUtil.getInteger(actionRequest, "dueDateAmPm");

		if (dueDateAmPm == Calendar.PM) {
			dueDateHour += 12;
		}

		Date dueDate = PortalUtil.getDate(
			dueDateMonth, dueDateDay, dueDateYear, dueDateHour, dueDateMinute,
			WorkflowTaskDueDateException.class);

		WorkflowTaskManagerUtil.updateDueDate(
			themeDisplay.getCompanyId(), themeDisplay.getUserId(),
			workflowTaskId, comment, dueDate);
	}

}