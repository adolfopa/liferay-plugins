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
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.workflow.kaleo.forms.KaleoProcessDDMTemplateIdException;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess;
import com.liferay.portal.workflow.kaleo.forms.service.base.KaleoProcessLocalServiceBaseImpl;
import com.liferay.portal.workflow.kaleo.forms.util.TaskFormPair;
import com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs;
import com.liferay.portlet.dynamicdatalists.model.DDLRecord;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSetConstants;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class KaleoProcessLocalServiceImpl
	extends KaleoProcessLocalServiceBaseImpl {

	public KaleoProcess addKaleoProcess(
			long userId, long groupId, long ddmStructureId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			long ddmTemplateId, String workflowDefinitionName,
			int workflowDefinitionVersion, TaskFormPairs taskFormPairs,
			ServiceContext serviceContext)
		throws PortalException {

		// Kaleo process

		User user = userLocalService.getUser(userId);
		Date now = new Date();

		validate(ddmTemplateId);

		long kaleoProcessId = counterLocalService.increment();

		KaleoProcess kaleoProcess = kaleoProcessPersistence.create(
			kaleoProcessId);

		kaleoProcess.setGroupId(groupId);
		kaleoProcess.setCompanyId(user.getCompanyId());
		kaleoProcess.setUserId(user.getUserId());
		kaleoProcess.setUserName(user.getFullName());
		kaleoProcess.setCreateDate(serviceContext.getCreateDate(now));
		kaleoProcess.setModifiedDate(serviceContext.getModifiedDate(now));

		DDLRecordSet ddlRecordSet = addDDLRecordSet(
			userId, groupId, ddmStructureId, nameMap, descriptionMap,
			serviceContext);

		kaleoProcess.setDDLRecordSetId(ddlRecordSet.getRecordSetId());

		kaleoProcess.setDDMTemplateId(ddmTemplateId);
		kaleoProcess.setWorkflowDefinitionName(workflowDefinitionName);
		kaleoProcess.setWorkflowDefinitionVersion(workflowDefinitionVersion);

		kaleoProcessPersistence.update(kaleoProcess);

		// Resources

		resourceLocalService.addModelResources(kaleoProcess, serviceContext);

		// Kaleo process links

		updateKaleoProcessLinks(kaleoProcessId, taskFormPairs);

		return kaleoProcess;
	}

	@Override
	public KaleoProcess deleteKaleoProcess(KaleoProcess kaleoProcess)
		throws PortalException {

		// Kaleo process

		kaleoProcessPersistence.remove(kaleoProcess);

		// Kaleo process links

		kaleoProcessLinkLocalService.deleteKaleoProcessLinks(
			kaleoProcess.getPrimaryKey());

		// Workflow

		deleteKaleoProcessData(kaleoProcess);

		// Dynamic data lists record set

		ddlRecordSetLocalService.deleteRecordSet(
			kaleoProcess.getDDLRecordSetId());

		return kaleoProcess;
	}

	@Override
	public KaleoProcess deleteKaleoProcess(long kaleoProcessId)
		throws PortalException {

		KaleoProcess kaleoProcess = kaleoProcessPersistence.findByPrimaryKey(
			kaleoProcessId);

		return deleteKaleoProcess(kaleoProcess);
	}

	public KaleoProcess getDDLRecordSetKaleoProcess(long ddlRecordSetId)
		throws PortalException {

		return kaleoProcessPersistence.findByDDLRecordSetId(ddlRecordSetId);
	}

	@Override
	public KaleoProcess getKaleoProcess(long kaleoProcessId)
		throws PortalException {

		return kaleoProcessPersistence.findByPrimaryKey(kaleoProcessId);
	}

	public List<KaleoProcess> getKaleoProcesses(long groupId) {
		return kaleoProcessPersistence.findByGroupId(groupId);
	}

	public List<KaleoProcess> getKaleoProcesses(
		long groupId, int start, int end, OrderByComparator orderByComparator) {

		return kaleoProcessPersistence.findByGroupId(
			groupId, start, end, orderByComparator);
	}

	public int getKaleoProcessesCount(long groupId) {
		return kaleoProcessPersistence.countByGroupId(groupId);
	}

	public KaleoProcess updateKaleoProcess(
			long kaleoProcessId, long ddmStructureId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			long ddmTemplateId, String workflowDefinitionName,
			int workflowDefinitionVersion, TaskFormPairs taskFormPairs,
			ServiceContext serviceContext)
		throws PortalException {

		// Kaleo process

		KaleoProcess kaleoProcess = kaleoProcessPersistence.findByPrimaryKey(
			kaleoProcessId);

		boolean kaleoProcessDataStale = isKaleoProcessDataStale(
			kaleoProcess, ddmStructureId,
			workflowDefinitionName + "@" + workflowDefinitionVersion);

		validate(ddmTemplateId);

		kaleoProcess.setModifiedDate(serviceContext.getModifiedDate(null));
		kaleoProcess.setDDMTemplateId(ddmTemplateId);
		kaleoProcess.setWorkflowDefinitionName(workflowDefinitionName);
		kaleoProcess.setWorkflowDefinitionVersion(workflowDefinitionVersion);

		kaleoProcessPersistence.update(kaleoProcess);

		// Kaleo process links

		kaleoProcessLinkLocalService.deleteKaleoProcessLinks(kaleoProcessId);

		updateKaleoProcessLinks(kaleoProcessId, taskFormPairs);

		// Kaleo process data

		if (kaleoProcessDataStale) {
			deleteKaleoProcessData(kaleoProcess);
		}

		// Dynamic data lists record set

		updateDDLRecordSet(
			kaleoProcess.getDDLRecordSetId(), ddmStructureId, nameMap,
			descriptionMap, serviceContext);

		return kaleoProcess;
	}

	protected DDLRecordSet addDDLRecordSet(
			long userId, long groupId, long ddmStructureId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			ServiceContext serviceContext)
		throws PortalException {

		int scope = GetterUtil.getInteger(serviceContext.getAttribute("scope"));

		return ddlRecordSetLocalService.addRecordSet(
			userId, groupId, ddmStructureId, null, nameMap, descriptionMap,
			DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT, scope,
			serviceContext);
	}

	protected void deleteKaleoProcessData(KaleoProcess kaleoProcess)
		throws PortalException {

		workflowDefinitionLinkLocalService.deleteWorkflowDefinitionLink(
			kaleoProcess.getCompanyId(), kaleoProcess.getGroupId(),
			KaleoProcess.class.getName(), kaleoProcess.getKaleoProcessId(), 0);

		List<DDLRecord> ddlRecords = ddlRecordLocalService.getRecords(
			kaleoProcess.getDDLRecordSetId());

		for (DDLRecord ddlRecord : ddlRecords) {
			workflowInstanceLinkLocalService.deleteWorkflowInstanceLinks(
				kaleoProcess.getCompanyId(), kaleoProcess.getGroupId(),
				KaleoProcess.class.getName(), ddlRecord.getRecordId());

			ddlRecordLocalService.deleteRecord(ddlRecord.getRecordId());
		}
	}

	protected boolean isKaleoProcessDataStale(
			KaleoProcess kaleoProcess, long newDDMStructureId,
			String newWorkflowDefinition)
		throws PortalException {

		DDLRecordSet ddlRecordSet = kaleoProcess.getDDLRecordSet();

		if ((newDDMStructureId != ddlRecordSet.getDDMStructureId()) ||
			!newWorkflowDefinition.equals(
				kaleoProcess.getWorkflowDefinition())) {

			return true;
		}

		return false;
	}

	protected void updateDDLRecordSet(
			long ddlRecordSetId, long ddmStructureId,
			Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			ServiceContext serviceContext)
		throws PortalException {

		ddlRecordSetLocalService.updateRecordSet(
			ddlRecordSetId, ddmStructureId, nameMap, descriptionMap,
			DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT, serviceContext);
	}

	protected void updateKaleoProcessLinks(
		long kaleoProcessId, TaskFormPairs taskFormPairs) {

		for (TaskFormPair taskFormPair : taskFormPairs) {
			kaleoProcessLinkLocalService.addKaleoProcessLink(
				kaleoProcessId, taskFormPair.getWorkflowTaskName(),
				taskFormPair.getDDMTemplateId());
		}
	}

	protected void validate(long ddmTemplateId) throws PortalException {
		if (Validator.isNull(ddmTemplateId)) {
			throw new KaleoProcessDDMTemplateIdException();
		}
	}

}