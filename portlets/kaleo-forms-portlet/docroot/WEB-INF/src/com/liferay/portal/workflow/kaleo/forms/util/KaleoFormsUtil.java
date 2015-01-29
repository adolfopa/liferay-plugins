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

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowDefinition;
import com.liferay.portal.kernel.workflow.WorkflowDefinitionManagerUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcessLink;
import com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessLinkLocalServiceUtil;
import com.liferay.portal.workflow.kaleo.forms.service.KaleoProcessServiceUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletSession;

/**
 * @author Marcellus Tavares
 */
public class KaleoFormsUtil {

	public static void cleanUpPortletSession(PortletSession portletSession) {
		Enumeration<String> enumeration = portletSession.getAttributeNames();

		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();

			portletSession.removeAttribute(name);
		}
	}

	public static String getInitialStateName(
			long companyId, String workflowDefinition)
		throws Exception {

		if (Validator.isNull(workflowDefinition)) {
			return StringPool.BLANK;
		}

		String[] workflowDefinitionParts = StringUtil.split(
			workflowDefinition, CharPool.AT);

		String workflowDefinitionName = workflowDefinitionParts[0];
		int workflowDefinitionVersion = GetterUtil.getInteger(
			workflowDefinitionParts[1]);

		Document document = _getWorkflowDefinitionDocument(
			companyId, workflowDefinitionName, workflowDefinitionVersion);

		return _getInitalStateName(document.getRootElement());
	}

	public static TaskFormPair getInitialStateTaskFormPair(
			long kaleoProcessId, long ddmStructureId, String workflowDefinition,
			String initialStateName, PortletSession portletSession)
		throws Exception {

		String taskSessionKey = _getTaskSessionKey(
			ddmStructureId, workflowDefinition, initialStateName);

		long ddmTemplateId = GetterUtil.getLong(
			portletSession.getAttribute(taskSessionKey));

		if ((ddmTemplateId == 0) && (kaleoProcessId > 0)) {
			KaleoProcess kaleoProcess = KaleoProcessServiceUtil.getKaleoProcess(
				kaleoProcessId);

			DDLRecordSet ddlRecordSet = kaleoProcess.getDDLRecordSet();

			String kaleoProcessWorkflowDefinition =
				kaleoProcess.getWorkflowDefinition();

			if ((ddlRecordSet.getDDMStructureId() == ddmStructureId) &&
				kaleoProcessWorkflowDefinition.equals(workflowDefinition)) {

				ddmTemplateId = kaleoProcess.getDDMTemplateId();
			}
		}

		return new TaskFormPair(initialStateName, ddmTemplateId);
	}

	public static long getKaleoProcessDDMStructureId(
			KaleoProcess kaleoProcess, PortletSession portletSession)
		throws Exception {

		long ddmStructureId = GetterUtil.getLong(
			portletSession.getAttribute("ddmStructureId"));

		if (ddmStructureId > 0) {
			return ddmStructureId;
		}

		if (kaleoProcess != null) {
			DDLRecordSet ddlRecordSet = kaleoProcess.getDDLRecordSet();

			return ddlRecordSet.getDDMStructureId();
		}

		return 0;
	}

	public static long getKaleoProcessDDMStructureId(
			long kaleoProcessId, PortletSession portletSession)
		throws Exception {

		KaleoProcess kaleoProcess = null;

		if (kaleoProcessId > 0) {
			kaleoProcess = KaleoProcessServiceUtil.getKaleoProcess(
				kaleoProcessId);
		}

		return getKaleoProcessDDMStructureId(kaleoProcess, portletSession);
	}

	public static String getKaleoProcessDescription(
			KaleoProcess kaleoProcess, PortletSession portletSession)
		throws Exception {

		Map<Locale, String> descriptionMap = new HashMap<>();

		String translatedLanguagesDescription = GetterUtil.getString(
			portletSession.getAttribute("translatedLanguagesDescription"),
			StringPool.BLANK);

		for (String translatedLanguageId :
				StringUtil.split(translatedLanguagesDescription)) {

			String description = GetterUtil.getString(
				portletSession.getAttribute(
					"description" + translatedLanguageId),
				StringPool.BLANK);

			Locale locale = LocaleUtil.fromLanguageId(translatedLanguageId);

			descriptionMap.put(locale, description);
		}

		if (!descriptionMap.isEmpty()) {
			String description = LocalizationUtil.updateLocalization(
				descriptionMap, StringPool.BLANK, "Description",
				_getDefaultLanguageId());

			return description;
		}

		if (kaleoProcess != null) {
			return kaleoProcess.getDescription();
		}

		return StringPool.BLANK;
	}

	public static String getKaleoProcessName(
			KaleoProcess kaleoProcess, PortletSession portletSession)
		throws Exception {

		Map<Locale, String> nameMap = new HashMap<>();

		String translatedLanguagesName = GetterUtil.getString(
			portletSession.getAttribute("translatedLanguagesName"),
			StringPool.BLANK);

		for (String translatedLanguageId :
				StringUtil.split(translatedLanguagesName)) {

			String name = GetterUtil.getString(
				portletSession.getAttribute("name" + translatedLanguageId),
				StringPool.BLANK);

			Locale locale = LocaleUtil.fromLanguageId(translatedLanguageId);

			nameMap.put(locale, name);
		}

		if (!nameMap.isEmpty()) {
			String name = LocalizationUtil.updateLocalization(
				nameMap, StringPool.BLANK, "Name", _getDefaultLanguageId());

			return name;
		}

		if (kaleoProcess != null) {
			return kaleoProcess.getName();
		}

		return StringPool.BLANK;
	}

	public static String getKaleoProcessName(
			KaleoProcess kaleoProcess, PortletSession portletSession,
			Locale locale)
		throws Exception {

		String defaultName = GetterUtil.getString(
			portletSession.getAttribute("name" + _getDefaultLanguageId()),
			StringPool.BLANK);

		String languageId = LocaleUtil.toLanguageId(locale);

		String name = GetterUtil.getString(
			portletSession.getAttribute("name" + languageId), defaultName);

		if (Validator.isNotNull(name)) {
			return name;
		}

		if (kaleoProcess != null) {
			return kaleoProcess.getName(locale);
		}

		return StringPool.BLANK;
	}

	public static TaskFormPairs getTaskFormPairs(
			long companyId, long kaleoProcessId, long ddmStructureId,
			String workflowDefinition, PortletSession portletSession)
		throws Exception {

		TaskFormPairs taskFormPairs = new TaskFormPairs();

		for (String taskName : _getTaskNames(companyId, workflowDefinition)) {
			long ddmTemplateId = _getDDMTemplateId(
				kaleoProcessId, ddmStructureId, workflowDefinition, taskName,
				portletSession);

			TaskFormPair taskFormPair = new TaskFormPair(
				taskName, ddmTemplateId);

			taskFormPairs.add(taskFormPair);
		}

		return taskFormPairs;
	}

	public static String getWorkflowDefinition(
		KaleoProcess kaleoProcess, PortletSession portletSession) {

		String workflowDefinition = GetterUtil.getString(
			portletSession.getAttribute("workflowDefinition"),
			StringPool.BLANK);

		if (Validator.isNotNull(workflowDefinition)) {
			return workflowDefinition;
		}

		if (kaleoProcess != null) {
			workflowDefinition = kaleoProcess.getWorkflowDefinition();
		}

		return workflowDefinition;
	}

	public static WorkflowDefinition getWorkflowDefinition(
		long companyId, String name, int version) {

		try {
			WorkflowDefinition workflowDefinition =
				WorkflowDefinitionManagerUtil.getWorkflowDefinition(
					companyId, name, version);

			return workflowDefinition;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static boolean isWorkflowDefinitionActive(
		long companyId, String name, int version) {

		WorkflowDefinition workflowDefinition =
			KaleoFormsUtil.getWorkflowDefinition(companyId, name, version);

		if (workflowDefinition != null) {
			return workflowDefinition.isActive();
		}

		return false;
	}

	private static void _addTaskNames(Element element, List<String> taskNames) {
		for (Element taskElement : element.elements("task")) {
			taskNames.add(taskElement.elementText("name"));

			_addTaskNames(taskElement, taskNames);
		}
	}

	private static long _getDDMTemplateId(
			long kaleoProcessId, long ddmStructureId, String workflowDefinition,
			String taskName, PortletSession portletSession)
		throws Exception {

		String taskSessionKey = _getTaskSessionKey(
			ddmStructureId, workflowDefinition, taskName);

		long ddmTemplateId = GetterUtil.getLong(
			portletSession.getAttribute(taskSessionKey));

		if (ddmTemplateId > 0) {
			return ddmTemplateId;
		}

		if (kaleoProcessId > 0) {
			KaleoProcess kaleoProcess = KaleoProcessServiceUtil.getKaleoProcess(
				kaleoProcessId);

			DDLRecordSet ddlRecordSet = kaleoProcess.getDDLRecordSet();

			String kaleoProcessWorkflowDefinition =
				kaleoProcess.getWorkflowDefinition();

			if ((ddlRecordSet.getDDMStructureId() != ddmStructureId) ||
				!kaleoProcessWorkflowDefinition.equals(workflowDefinition)) {

				return 0;
			}
		}

		KaleoProcessLink kaleoProcessLink =
			KaleoProcessLinkLocalServiceUtil.fetchKaleoProcessLink(
				kaleoProcessId, taskName);

		if (kaleoProcessLink != null) {
			return kaleoProcessLink.getDDMTemplateId();
		}

		return 0;
	}

	private static String _getDefaultLanguageId() {
		Locale defaultLocale = LocaleUtil.getSiteDefault();

		return LocaleUtil.toLanguageId(defaultLocale);
	}

	private static String _getInitalStateName(Element rootElement) {
		for (Element element : rootElement.elements("state")) {
			boolean initial = GetterUtil.getBoolean(
				element.elementText("initial"));

			if (initial) {
				return element.elementText("name");
			}
		}

		return null;
	}

	private static List<String> _getTaskNames(Element rootElement) {
		List<String> taskNames = new ArrayList<>();

		_addTaskNames(rootElement, taskNames);

		return taskNames;
	}

	private static List<String> _getTaskNames(
			long companyId, String workflowDefinition)
		throws Exception {

		if (Validator.isNull(workflowDefinition)) {
			return Collections.emptyList();
		}

		String[] workflowDefinitionParts = StringUtil.split(
			workflowDefinition, CharPool.AT);

		String workflowDefinitionName = workflowDefinitionParts[0];
		int workflowDefinitionVersion = GetterUtil.getInteger(
			workflowDefinitionParts[1]);

		Document document = _getWorkflowDefinitionDocument(
			companyId, workflowDefinitionName, workflowDefinitionVersion);

		return _getTaskNames(document.getRootElement());
	}

	private static String _getTaskSessionKey(
		long ddmStructureId, String workflowDefinition, String taskName) {

		StringBundler sb = new StringBundler(3);

		sb.append(ddmStructureId);
		sb.append(workflowDefinition);
		sb.append(taskName);

		return sb.toString();
	}

	private static Document _getWorkflowDefinitionDocument(
			long companyId, String workflowDefinitionName,
			int workflowDefinitionVersion)
		throws Exception {

		WorkflowDefinition workflowDefinition =
			WorkflowDefinitionManagerUtil.getWorkflowDefinition(
				companyId, workflowDefinitionName, workflowDefinitionVersion);

		return SAXReaderUtil.read(workflowDefinition.getContent());
	}

}