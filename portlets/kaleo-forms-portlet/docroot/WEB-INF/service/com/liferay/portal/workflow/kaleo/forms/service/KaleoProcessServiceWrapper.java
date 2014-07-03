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

package com.liferay.portal.workflow.kaleo.forms.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link KaleoProcessService}.
 *
 * @author Marcellus Tavares
 * @see KaleoProcessService
 * @generated
 */
public class KaleoProcessServiceWrapper implements KaleoProcessService,
	ServiceWrapper<KaleoProcessService> {
	public KaleoProcessServiceWrapper(KaleoProcessService kaleoProcessService) {
		_kaleoProcessService = kaleoProcessService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _kaleoProcessService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_kaleoProcessService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _kaleoProcessService.invokeMethod(name, parameterTypes, arguments);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess addKaleoProcess(
		long groupId, long ddlRecordSetId, long ddmTemplateId,
		java.lang.String workflowDefinitionName,
		long workflowDefinitionVersion,
		com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs taskFormPairs,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessService.addKaleoProcess(groupId, ddlRecordSetId,
			ddmTemplateId, workflowDefinitionName, workflowDefinitionVersion,
			taskFormPairs, serviceContext);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess deleteKaleoProcess(
		long kaleoProcessId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessService.deleteKaleoProcess(kaleoProcessId);
	}

	@Override
	public void deleteKaleoProcessData(long kaleoProcessId)
		throws com.liferay.portal.kernel.exception.PortalException {
		_kaleoProcessService.deleteKaleoProcessData(kaleoProcessId);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess getKaleoProcess(
		long kaleoProcessId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessService.getKaleoProcess(kaleoProcessId);
	}

	@Override
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> getKaleoProcesses(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator) {
		return _kaleoProcessService.getKaleoProcesses(groupId, start, end,
			orderByComparator);
	}

	@Override
	public int getKaleoProcessesCount(long groupId) {
		return _kaleoProcessService.getKaleoProcessesCount(groupId);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess updateKaleoProcess(
		long kaleoProcessId, long ddmTemplateId,
		java.lang.String workflowDefinitionName,
		long workflowDefinitionVersion,
		com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs taskFormPairs,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessService.updateKaleoProcess(kaleoProcessId,
			ddmTemplateId, workflowDefinitionName, workflowDefinitionVersion,
			taskFormPairs, serviceContext);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public KaleoProcessService getWrappedKaleoProcessService() {
		return _kaleoProcessService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedKaleoProcessService(
		KaleoProcessService kaleoProcessService) {
		_kaleoProcessService = kaleoProcessService;
	}

	@Override
	public KaleoProcessService getWrappedService() {
		return _kaleoProcessService;
	}

	@Override
	public void setWrappedService(KaleoProcessService kaleoProcessService) {
		_kaleoProcessService = kaleoProcessService;
	}

	private KaleoProcessService _kaleoProcessService;
}