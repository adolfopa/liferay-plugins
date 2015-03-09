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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionAttribute;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.RequiredWorkflowDefinitionException;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowDefinitionManagerUtil;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.WorkflowInstanceLink;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.service.WorkflowInstanceLinkLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.workflow.kaleo.designer.DuplicateKaleoDraftDefinitionNameException;
import com.liferay.portal.workflow.kaleo.designer.KaleoDraftDefinitionContentException;
import com.liferay.portal.workflow.kaleo.designer.KaleoDraftDefinitionNameException;
import com.liferay.portal.workflow.kaleo.designer.NoSuchKaleoDraftDefinitionException;
import com.liferay.portal.workflow.kaleo.designer.model.KaleoDraftDefinition;
import com.liferay.portal.workflow.kaleo.designer.service.KaleoDraftDefinitionServiceUtil;
import com.liferay.portal.workflow.kaleo.forms.KaleoProcessDDMTemplateIdException;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess;
import com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessServiceUtil;
import com.liferay.portal.workflow.kaleo.forms.util.TaskFormPairs;
import com.liferay.portal.workflow.kaleo.forms.util.WebKeys;
import com.liferay.portlet.dynamicdatalists.RecordSetDDMStructureIdException;
import com.liferay.portlet.dynamicdatalists.RecordSetNameException;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordServiceUtil;
import com.liferay.portlet.dynamicdatalists.util.DDLExportFormat;
import com.liferay.portlet.dynamicdatalists.util.DDLExporter;
import com.liferay.portlet.dynamicdatalists.util.DDLExporterFactory;
import com.liferay.portlet.dynamicdatamapping.RequiredStructureException;
import com.liferay.portlet.dynamicdatamapping.StructureDefinitionException;
import com.liferay.portlet.dynamicdatamapping.io.DDMFormJSONDeserializerUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMForm;
import com.liferay.portlet.dynamicdatamapping.model.DDMFormLayout;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureServiceUtil;
import com.liferay.portlet.dynamicdatamapping.util.DDMUtil;

import java.io.IOException;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * @author Marcellus Tavares
 * @author Eduardo Lundgren
 */
public class KaleoFormsAdminPortlet extends BaseKaleoFormsPorlet {

	public void deactivateWorkflowDefinition(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String name = ParamUtil.getString(actionRequest, "name");
		int version = ParamUtil.getInteger(actionRequest, "version");

		try {
			WorkflowDefinitionManagerUtil.updateActive(
				themeDisplay.getCompanyId(), themeDisplay.getUserId(), name,
				version, false);
		}
		catch (Exception e) {
			if (isSessionErrorException(e)) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}

				SessionErrors.add(actionRequest, e.getClass(), e);

				sendRedirect(actionRequest, actionResponse);
			}
			else {
				throw e;
			}
		}
	}

	public void deleteDDLRecord(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		final long ddlRecordId = ParamUtil.getLong(
			actionRequest, "ddlRecordId");

		final long workflowInstanceLinkId = getDDLRecordWorkfowInstanceLinkId(
			themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId(),
			ddlRecordId);

		try {
			Callable<Void> callable = new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					DDLRecordServiceUtil.deleteRecord(ddlRecordId);

					WorkflowInstanceLinkLocalServiceUtil.
						deleteWorkflowInstanceLink(workflowInstanceLinkId);

					return null;
				}
			};

			TransactionInvokerUtil.invoke(_transactionAttribute, callable);
		}
		catch (Throwable t) {
			if (t instanceof PortalException) {
				throw (PortalException)t;
			}

			throw new SystemException(t);
		}
	}

	public void deleteDDMStructure(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long ddmStructureId = ParamUtil.getLong(
			actionRequest, "ddmStructureId");

		try {
			DDMStructureServiceUtil.deleteStructure(ddmStructureId);
		}
		catch (Exception e) {
			if (isSessionErrorException(e)) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}

				SessionErrors.add(actionRequest, e.getClass(), e);

				sendRedirect(actionRequest, actionResponse);
			}
			else {
				throw e;
			}
		}
	}

	public void deleteKaleoDraftDefinition(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String name = ParamUtil.getString(actionRequest, "name");
		int version = ParamUtil.getInteger(actionRequest, "version");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			actionRequest);

		KaleoDraftDefinitionServiceUtil.deleteKaleoDraftDefinitions(
			name, version, serviceContext);
	}

	public void deleteKaleoProcess(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long kaleoProcessId = ParamUtil.getLong(
			actionRequest, "kaleoProcessId");

		KaleoProcessServiceUtil.deleteKaleoProcess(kaleoProcessId);
	}

	public void publishKaleoDraftDefinition(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String content = null;

		try {
			String backURL = ParamUtil.getString(actionRequest, "backURL");

			String name = ParamUtil.getString(actionRequest, "name");
			Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
				actionRequest, "title");
			content = ParamUtil.getString(actionRequest, "content");

			if (Validator.isNull(name)) {
				name = getName(
					content, titleMap.get(themeDisplay.getSiteDefaultLocale()));
			}

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			KaleoDraftDefinition kaleoDraftDefinition =
				KaleoDraftDefinitionServiceUtil.publishKaleoDraftDefinition(
					themeDisplay.getUserId(), themeDisplay.getCompanyGroupId(),
					name, titleMap, content, serviceContext);

			actionRequest.setAttribute(
				WebKeys.KALEO_DRAFT_DEFINITION_ID,
				kaleoDraftDefinition.getKaleoDraftDefinitionId());
			actionRequest.setAttribute(WebKeys.REDIRECT, backURL);

			saveInPortletSession(actionRequest, kaleoDraftDefinition);
		}
		catch (Exception e) {
			if (isSessionErrorException(e)) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}

				SessionErrors.add(actionRequest, e.getClass(), e);

				actionRequest.setAttribute(
					WebKeys.KALEO_DRAFT_DEFINITION_CONTENT, content);
			}
			else {
				throw e;
			}
		}
	}

	@Override
	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		try {
			String resourceID = resourceRequest.getResourceID();

			if (resourceID.equals("kaleoDraftDefinitions")) {
				serveKaleoDraftDefinitions(resourceRequest, resourceResponse);
			}
			else if (resourceID.equals("kaleoProcess")) {
				serveKaleoProcess(resourceRequest, resourceResponse);
			}
			else if (resourceID.equals("saveInPortletSession")) {
				saveInPortletSession(resourceRequest, resourceResponse);
			}
		}
		catch (IOException ioe) {
			throw ioe;
		}
		catch (PortletException pe) {
			throw pe;
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	public void updateKaleoDraftDefinition(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String content = null;

		try {
			long kaleoDraftDefinitionId = ParamUtil.getLong(
				actionRequest, "kaleoDraftDefinitionId");

			Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
				actionRequest, "title");
			content = ParamUtil.getString(actionRequest, "content");
			int version = ParamUtil.getInteger(actionRequest, "version");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			KaleoDraftDefinition kaleoDraftDefinition = null;

			if (kaleoDraftDefinitionId <= 0) {
				String name = getName(
					content, titleMap.get(themeDisplay.getSiteDefaultLocale()));

				kaleoDraftDefinition =
					KaleoDraftDefinitionServiceUtil.addKaleoDraftDefinition(
						themeDisplay.getUserId(),
						themeDisplay.getCompanyGroupId(), name, titleMap,
						content, version, 1, serviceContext);
			}
			else {
				String name = ParamUtil.getString(actionRequest, "name");

				kaleoDraftDefinition =
					KaleoDraftDefinitionServiceUtil.updateKaleoDraftDefinition(
						themeDisplay.getUserId(), name, titleMap, content,
						version, serviceContext);
			}

			actionRequest.setAttribute(
				WebKeys.KALEO_DRAFT_DEFINITION_ID,
				kaleoDraftDefinition.getKaleoDraftDefinitionId());
		}
		catch (Exception e) {
			if (isSessionErrorException(e)) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}

				SessionErrors.add(actionRequest, e.getClass(), e);

				actionRequest.setAttribute(
					WebKeys.KALEO_DRAFT_DEFINITION_CONTENT, content);
			}
			else {
				throw e;
			}
		}
	}

	public void updateKaleoProcess(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long kaleoProcessId = ParamUtil.getLong(
			actionRequest, "kaleoProcessId");

		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		long ddmStructureId = ParamUtil.getLong(
			actionRequest, "ddmStructureId");
		Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "name");
		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(actionRequest, "description");
		long ddmTemplateId = ParamUtil.getLong(actionRequest, "ddmTemplateId");
		String workflowDefinitionName = ParamUtil.getString(
			actionRequest, "workflowDefinitionName");
		int workflowDefinitionVersion = ParamUtil.getInteger(
			actionRequest, "workflowDefinitionVersion");
		String taskFormPairsData = ParamUtil.getString(
			actionRequest, "taskFormPairsData");

		TaskFormPairs taskFormPairs = TaskFormPairs.parse(taskFormPairsData);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			KaleoProcess.class.getName(), actionRequest);

		KaleoProcess kaleoProcess = null;

		if (kaleoProcessId <= 0) {
			kaleoProcess = KaleoProcessServiceUtil.addKaleoProcess(
				groupId, ddmStructureId, nameMap, descriptionMap, ddmTemplateId,
				workflowDefinitionName, workflowDefinitionVersion,
				taskFormPairs, serviceContext);
		}
		else {
			kaleoProcess = KaleoProcessServiceUtil.updateKaleoProcess(
				kaleoProcessId, ddmStructureId, nameMap, descriptionMap,
				ddmTemplateId, workflowDefinitionName,
				workflowDefinitionVersion, taskFormPairs, serviceContext);
		}

		String workflowDefinition = ParamUtil.getString(
			actionRequest, "workflowDefinition");

		WorkflowDefinitionLinkLocalServiceUtil.updateWorkflowDefinitionLink(
			serviceContext.getUserId(), serviceContext.getCompanyId(), groupId,
			KaleoProcess.class.getName(), kaleoProcess.getKaleoProcessId(), 0,
			workflowDefinition);
	}

	public void updateStructure(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

			long classPK = ParamUtil.getLong(actionRequest, "classPK");

			long groupId = ParamUtil.getLong(actionRequest, "groupId");
			long scopeClassNameId = ParamUtil.getLong(
				actionRequest, "scopeClassNameId");
			long parentStructureId = ParamUtil.getLong(
				actionRequest, "parentStructureId",
				DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID);
			Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(
				actionRequest, "name");
			Map<Locale, String> descriptionMap =
				LocalizationUtil.getLocalizationMap(
					actionRequest, "description");
			DDMForm ddmForm = getDDMForm(actionRequest);
			DDMFormLayout ddmFormLayout = DDMUtil.getDefaultDDMFormLayout(
				ddmForm);
			String storageType = ParamUtil.getString(
				actionRequest, "storageType");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				DDMStructure.class.getName(), actionRequest);

			if (cmd.equals(Constants.ADD)) {
				DDMStructure ddmStructure =
					DDMStructureServiceUtil.addStructure(
						groupId, parentStructureId, scopeClassNameId, null,
						nameMap, descriptionMap, ddmForm, ddmFormLayout,
						storageType, DDMStructureConstants.TYPE_DEFAULT,
						serviceContext);

				saveInPortletSession(actionRequest, ddmStructure);
			}
			else if (cmd.equals(Constants.UPDATE)) {
				DDMStructureServiceUtil.updateStructure(
					classPK, parentStructureId, nameMap, descriptionMap,
					ddmForm, ddmFormLayout, serviceContext);
			}
		}
		catch (Exception e) {
			if (isSessionErrorException(e)) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}

				SessionErrors.add(actionRequest, e.getClass(), e);
			}
			else {
				throw e;
			}
		}
	}

	protected long getDDLRecordWorkfowInstanceLinkId(
			long companyId, long groupId, long ddlRecordId)
		throws Exception {

		WorkflowInstanceLink workfowInstanceLink =
			WorkflowInstanceLinkLocalServiceUtil.getWorkflowInstanceLink(
				companyId, groupId, KaleoProcess.class.getName(), ddlRecordId);

		return workfowInstanceLink.getWorkflowInstanceLinkId();
	}

	protected DDMForm getDDMForm(ActionRequest actionRequest) throws Exception {
		String definition = ParamUtil.getString(actionRequest, "definition");

		return DDMFormJSONDeserializerUtil.deserialize(definition);
	}

	protected String getName(String content, String defaultName) {
		if (Validator.isNull(content)) {
			return defaultName;
		}

		try {
			Document document = SAXReaderUtil.read(content);

			Element rootElement = document.getRootElement();

			return rootElement.elementText("name");
		}
		catch (DocumentException de) {
			return defaultName;
		}
	}

	@Override
	protected boolean isSessionErrorException(Throwable cause) {
		if (cause instanceof DuplicateKaleoDraftDefinitionNameException ||
			cause instanceof KaleoDraftDefinitionContentException ||
			cause instanceof KaleoDraftDefinitionNameException ||
			cause instanceof KaleoProcessDDMTemplateIdException ||
			cause instanceof NoSuchKaleoDraftDefinitionException ||
			cause instanceof RecordSetDDMStructureIdException ||
			cause instanceof RecordSetNameException ||
			cause instanceof RequiredStructureException ||
			cause instanceof RequiredWorkflowDefinitionException ||
			cause instanceof StructureDefinitionException ||
			cause instanceof WorkflowException) {

			return true;
		}

		return false;
	}

	protected void saveInPortletSession(
		ActionRequest actionRequest, DDMStructure ddmStructure) {

		PortletSession portletSession = actionRequest.getPortletSession();

		portletSession.setAttribute(
			"ddmStructureId", String.valueOf(ddmStructure.getStructureId()));
	}

	protected void saveInPortletSession(
		ActionRequest actionRequest,
		KaleoDraftDefinition kaleoDraftDefinition) {

		PortletSession portletSession = actionRequest.getPortletSession();

		portletSession.setAttribute(
			"workflowDefinition",
			kaleoDraftDefinition.getName() + StringPool.AT +
				kaleoDraftDefinition.getVersion());
	}

	protected void saveInPortletSession(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		Enumeration<String> enumeration = resourceRequest.getParameterNames();

		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();

			if (name.equals("doAsUserId")) {
				continue;
			}

			PortletSession portletSession = resourceRequest.getPortletSession();

			String value = ParamUtil.getString(resourceRequest, name);

			portletSession.setAttribute(name, value);
		}
	}

	protected void serveKaleoDraftDefinitions(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String name = ParamUtil.getString(resourceRequest, "name");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		int draftVersion = ParamUtil.getInteger(
			resourceRequest, "draftVersion");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			resourceRequest);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		if (Validator.isNotNull(name) && (draftVersion > 0)) {
			KaleoDraftDefinition kaleoDraftDefinition =
				KaleoDraftDefinitionServiceUtil.getKaleoDraftDefinition(
					name, version, draftVersion, serviceContext);

			jsonObject.put("content", kaleoDraftDefinition.getContent());
			jsonObject.put(
				"draftVersion", kaleoDraftDefinition.getDraftVersion());
			jsonObject.put("name", kaleoDraftDefinition.getName());
			jsonObject.put(
				"title",
				kaleoDraftDefinition.getTitle(themeDisplay.getLocale()));
			jsonObject.put("version", kaleoDraftDefinition.getVersion());
		}

		writeJSON(resourceRequest, resourceResponse, jsonObject);
	}

	protected void serveKaleoProcess(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long kaleoProcessId = ParamUtil.getLong(
			resourceRequest, "kaleoProcessId");

		KaleoProcess kaleoProcess = KaleoProcessServiceUtil.getKaleoProcess(
			kaleoProcessId);

		String fileExtension = ParamUtil.getString(
			resourceRequest, "fileExtension");

		String fileName =
			kaleoProcess.getName(themeDisplay.getLocale()) +
				CharPool.PERIOD + fileExtension;

		int status = WorkflowConstants.STATUS_ANY;

		boolean exportOnlyApproved = ParamUtil.getBoolean(
			resourceRequest, "exportOnlyApproved");

		if (exportOnlyApproved) {
			status = WorkflowConstants.STATUS_APPROVED;
		}

		DDLExportFormat ddlExportFormat = DDLExportFormat.parse(fileExtension);

		DDLExporter ddlExporter = DDLExporterFactory.getDDLExporter(
			ddlExportFormat);

		ddlExporter.setLocale(themeDisplay.getLocale());

		byte[] bytes = ddlExporter.export(
			kaleoProcess.getDDLRecordSetId(), status);

		String contentType = MimeTypesUtil.getContentType(fileName);

		PortletResponseUtil.sendFile(
			resourceRequest, resourceResponse, fileName, bytes, contentType);
	}

	private static Log _log = LogFactoryUtil.getLog(
		KaleoFormsAdminPortlet.class);

	private static TransactionAttribute _transactionAttribute;

	static {
		TransactionAttribute.Builder builder =
			new TransactionAttribute.Builder();

		builder.setPropagation(Propagation.REQUIRES_NEW);
		builder.setRollbackForClasses(Exception.class);

		_transactionAttribute = builder.build();
	}

}