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

package com.liferay.portal.workflow.kaleo.forms.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess;
import com.liferay.portal.workflow.kaleo.forms.service.base.KaleoProcessServiceBaseImpl;
import com.liferay.portal.workflow.kaleo.forms.service.permission.KaleoFormsPermission;
import com.liferay.portal.workflow.kaleo.forms.service.permission.KaleoProcessPermission;
import com.liferay.portal.workflow.kaleo.forms.util.ActionKeys;
import com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class KaleoProcessServiceImpl extends KaleoProcessServiceBaseImpl {

	public KaleoProcess addKaleoProcess(
			long groupId, long ddmStructureId, Map<Locale, String> nameMap,
			Map<Locale, String> descriptionMap, long ddmTemplateId,
			String workflowDefinitionName, int workflowDefinitionVersion,
			TaskFormPairs taskFormPairs, ServiceContext serviceContext)
		throws PortalException {

		KaleoFormsPermission.check(
			getPermissionChecker(), groupId, ActionKeys.ADD_PROCESS);

		return kaleoProcessLocalService.addKaleoProcess(
			getUserId(), groupId, ddmStructureId, nameMap, descriptionMap,
			ddmTemplateId, workflowDefinitionName, workflowDefinitionVersion,
			taskFormPairs, serviceContext);
	}

	public KaleoProcess deleteKaleoProcess(long kaleoProcessId)
		throws PortalException {

		KaleoProcessPermission.check(
			getPermissionChecker(), kaleoProcessId, ActionKeys.DELETE);

		return kaleoProcessLocalService.deleteKaleoProcess(kaleoProcessId);
	}

	public KaleoProcess getKaleoProcess(long kaleoProcessId)
		throws PortalException {

		KaleoProcessPermission.check(
			getPermissionChecker(), kaleoProcessId, ActionKeys.VIEW);

		return kaleoProcessLocalService.getKaleoProcess(kaleoProcessId);
	}

	public List<KaleoProcess> getKaleoProcesses(
		long groupId, int start, int end, OrderByComparator orderByComparator) {

		return kaleoProcessPersistence.filterFindByGroupId(
			groupId, start, end, orderByComparator);
	}

	public int getKaleoProcessesCount(long groupId) {
		return kaleoProcessPersistence.filterCountByGroupId(groupId);
	}

	public KaleoProcess updateKaleoProcess(
			long kaleoProcessId, long ddmStructureId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			long ddmTemplateId, String workflowDefinitionName,
			int workflowDefinitionVersion, TaskFormPairs taskFormPairs,
			ServiceContext serviceContext)
		throws PortalException {

		KaleoProcessPermission.check(
			getPermissionChecker(), kaleoProcessId, ActionKeys.UPDATE);

		return kaleoProcessLocalService.updateKaleoProcess(
			kaleoProcessId, ddmStructureId, nameMap, descriptionMap,
			ddmTemplateId, workflowDefinitionName, workflowDefinitionVersion,
			taskFormPairs, serviceContext);
	}

}