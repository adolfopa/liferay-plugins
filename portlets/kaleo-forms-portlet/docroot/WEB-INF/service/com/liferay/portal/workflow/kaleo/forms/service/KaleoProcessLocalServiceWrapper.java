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

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link KaleoProcessLocalService}.
 *
 * @author Marcellus Tavares
 * @see KaleoProcessLocalService
 * @generated
 */
@ProviderType
public class KaleoProcessLocalServiceWrapper implements KaleoProcessLocalService,
	ServiceWrapper<KaleoProcessLocalService> {
	public KaleoProcessLocalServiceWrapper(
		KaleoProcessLocalService kaleoProcessLocalService) {
		_kaleoProcessLocalService = kaleoProcessLocalService;
	}

	/**
	* Adds the kaleo process to the database. Also notifies the appropriate model listeners.
	*
	* @param kaleoProcess the kaleo process
	* @return the kaleo process that was added
	*/
	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess addKaleoProcess(
		com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess kaleoProcess) {
		return _kaleoProcessLocalService.addKaleoProcess(kaleoProcess);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess addKaleoProcess(
		long userId, long groupId, long ddmStructureId,
		java.util.Map<java.util.Locale, java.lang.String> nameMap,
		java.util.Map<java.util.Locale, java.lang.String> descriptionMap,
		long ddmTemplateId, java.lang.String workflowDefinitionName,
		int workflowDefinitionVersion,
		com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs taskFormPairs,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessLocalService.addKaleoProcess(userId, groupId,
			ddmStructureId, nameMap, descriptionMap, ddmTemplateId,
			workflowDefinitionName, workflowDefinitionVersion, taskFormPairs,
			serviceContext);
	}

	/**
	* Creates a new kaleo process with the primary key. Does not add the kaleo process to the database.
	*
	* @param kaleoProcessId the primary key for the new kaleo process
	* @return the new kaleo process
	*/
	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess createKaleoProcess(
		long kaleoProcessId) {
		return _kaleoProcessLocalService.createKaleoProcess(kaleoProcessId);
	}

	/**
	* Deletes the kaleo process from the database. Also notifies the appropriate model listeners.
	*
	* @param kaleoProcess the kaleo process
	* @return the kaleo process that was removed
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess deleteKaleoProcess(
		com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess kaleoProcess)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessLocalService.deleteKaleoProcess(kaleoProcess);
	}

	/**
	* Deletes the kaleo process with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param kaleoProcessId the primary key of the kaleo process
	* @return the kaleo process that was removed
	* @throws PortalException if a kaleo process with the primary key could not be found
	*/
	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess deleteKaleoProcess(
		long kaleoProcessId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessLocalService.deleteKaleoProcess(kaleoProcessId);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.model.PersistedModel deletePersistedModel(
		com.liferay.portal.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _kaleoProcessLocalService.dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _kaleoProcessLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.forms.model.impl.KaleoProcessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return _kaleoProcessLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.forms.model.impl.KaleoProcessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return _kaleoProcessLocalService.dynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _kaleoProcessLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return _kaleoProcessLocalService.dynamicQueryCount(dynamicQuery,
			projection);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess fetchKaleoProcess(
		long kaleoProcessId) {
		return _kaleoProcessLocalService.fetchKaleoProcess(kaleoProcessId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _kaleoProcessLocalService.getActionableDynamicQuery();
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _kaleoProcessLocalService.getBeanIdentifier();
	}

	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess getDDLRecordSetKaleoProcess(
		long ddlRecordSetId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessLocalService.getDDLRecordSetKaleoProcess(ddlRecordSetId);
	}

	/**
	* Returns the kaleo process with the primary key.
	*
	* @param kaleoProcessId the primary key of the kaleo process
	* @return the kaleo process
	* @throws PortalException if a kaleo process with the primary key could not be found
	*/
	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess getKaleoProcess(
		long kaleoProcessId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessLocalService.getKaleoProcess(kaleoProcessId);
	}

	@Override
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> getKaleoProcesses(
		long groupId) {
		return _kaleoProcessLocalService.getKaleoProcesses(groupId);
	}

	@Override
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> getKaleoProcesses(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator) {
		return _kaleoProcessLocalService.getKaleoProcesses(groupId, start, end,
			orderByComparator);
	}

	/**
	* Returns a range of all the kaleo processes.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.forms.model.impl.KaleoProcessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of kaleo processes
	* @param end the upper bound of the range of kaleo processes (not inclusive)
	* @return the range of kaleo processes
	*/
	@Override
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> getKaleoProcesses(
		int start, int end) {
		return _kaleoProcessLocalService.getKaleoProcesses(start, end);
	}

	/**
	* Returns the number of kaleo processes.
	*
	* @return the number of kaleo processes
	*/
	@Override
	public int getKaleoProcessesCount() {
		return _kaleoProcessLocalService.getKaleoProcessesCount();
	}

	@Override
	public int getKaleoProcessesCount(long groupId) {
		return _kaleoProcessLocalService.getKaleoProcessesCount(groupId);
	}

	@Override
	public com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _kaleoProcessLocalService.invokeMethod(name, parameterTypes,
			arguments);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_kaleoProcessLocalService.setBeanIdentifier(beanIdentifier);
	}

	/**
	* Updates the kaleo process in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param kaleoProcess the kaleo process
	* @return the kaleo process that was updated
	*/
	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess updateKaleoProcess(
		com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess kaleoProcess) {
		return _kaleoProcessLocalService.updateKaleoProcess(kaleoProcess);
	}

	@Override
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess updateKaleoProcess(
		long kaleoProcessId, long ddmStructureId,
		java.util.Map<java.util.Locale, java.lang.String> nameMap,
		java.util.Map<java.util.Locale, java.lang.String> descriptionMap,
		long ddmTemplateId, java.lang.String workflowDefinitionName,
		int workflowDefinitionVersion,
		com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs taskFormPairs,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoProcessLocalService.updateKaleoProcess(kaleoProcessId,
			ddmStructureId, nameMap, descriptionMap, ddmTemplateId,
			workflowDefinitionName, workflowDefinitionVersion, taskFormPairs,
			serviceContext);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public KaleoProcessLocalService getWrappedKaleoProcessLocalService() {
		return _kaleoProcessLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedKaleoProcessLocalService(
		KaleoProcessLocalService kaleoProcessLocalService) {
		_kaleoProcessLocalService = kaleoProcessLocalService;
	}

	@Override
	public KaleoProcessLocalService getWrappedService() {
		return _kaleoProcessLocalService;
	}

	@Override
	public void setWrappedService(
		KaleoProcessLocalService kaleoProcessLocalService) {
		_kaleoProcessLocalService = kaleoProcessLocalService;
	}

	private KaleoProcessLocalService _kaleoProcessLocalService;
}