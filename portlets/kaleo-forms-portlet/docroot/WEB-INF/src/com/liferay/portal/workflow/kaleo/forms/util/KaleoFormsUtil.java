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

import com.liferay.compat.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
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

	public static void cleanupSession(PortletSession portletSession) {
		Enumeration<String> enu = portletSession.getAttributeNames();

		while (enu.hasMoreElements()) {
			String name = enu.nextElement();

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
			long kaleoProcessId, String initialStateName,
			PortletSession portletSession)
		throws Exception {

		long ddmTemplateId = GetterUtil.getLong(
			portletSession.getAttribute(initialStateName), 0);

		if ((ddmTemplateId == 0) && (kaleoProcessId > 0)) {
			KaleoProcess kaleoProcess = KaleoProcessServiceUtil.getKaleoProcess(
				kaleoProcessId);

			ddmTemplateId = kaleoProcess.getDDMTemplateId();
		}

		return new TaskFormPair(initialStateName, ddmTemplateId);
	}

	public static long getKaleoProcessDDMStructureId(
			KaleoProcess kaleoProcess, PortletSession portletSession)
		throws Exception {

		long ddmStructureId = GetterUtil.getLong(
			portletSession.getAttribute("ddmStructureId"), 0);

		if (ddmStructureId > 0) {
			return ddmStructureId;
		}

		if (kaleoProcess != null) {
			DDLRecordSet ddlRecordSet = kaleoProcess.getDDLRecordSet();

			return ddlRecordSet.getDDMStructureId();
		}

		return 0;
	}

	public static String getKaleoProcessDescription(
			KaleoProcess kaleoProcess, PortletSession portletSession)
		throws Exception {

		Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

		String translatedLanguagesDescription = GetterUtil.getString(
			portletSession.getAttribute("translatedLanguagesDescription"),
			StringPool.BLANK);

		for (String translatedLanguage :
				StringUtil.split(translatedLanguagesDescription)) {

			String description = GetterUtil.getString(
				portletSession.getAttribute("description" + translatedLanguage),
				StringPool.BLANK);

			Locale locale = LocaleUtil.fromLanguageId(translatedLanguage);

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

		Map<Locale, String> nameMap = new HashMap<Locale, String>();

		String translatedLanguagesName = GetterUtil.getString(
			portletSession.getAttribute("translatedLanguagesName"),
			StringPool.BLANK);

		for (String translatedLanguage :
				StringUtil.split(translatedLanguagesName)) {

			String name = GetterUtil.getString(
				portletSession.getAttribute("name" + translatedLanguage),
				StringPool.BLANK);

			Locale locale = LocaleUtil.fromLanguageId(translatedLanguage);

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

	public static TaskFormPairs getTaskFormPairs(
			long companyId, long kaleoProcessId, String workflowDefinition,
			PortletSession portletSession)
		throws Exception {

		TaskFormPairs taskFormPairs = new TaskFormPairs();

		for (String taskName : _getTaskNames(companyId, workflowDefinition)) {
			long ddmTemplateId = _getDDMTemplateId(
				kaleoProcessId, taskName, portletSession);

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
			String workflowDefinitionName =
				kaleoProcess.getWorkflowDefinitionName();

			long workflowDefinitionVersion =
				kaleoProcess.getWorkflowDefinitionVersion();

			workflowDefinition =
				workflowDefinitionName + "@" + workflowDefinitionVersion;
		}

		return workflowDefinition;
	}

	private static void _addTaskNames(Element element, List<String> taskNames) {
		for (Element taskElement : element.elements("task")) {
			taskNames.add(taskElement.elementText("name"));

			_addTaskNames(taskElement, taskNames);
		}
	}

	private static long _getDDMTemplateId(
			long kaleoProcessId, String taskName, PortletSession portletSession)
		throws Exception {

		long ddmTemplateId = GetterUtil.getLong(
			portletSession.getAttribute(taskName), 0);

		if (ddmTemplateId > 0) {
			return ddmTemplateId;
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
		List<String> taskNames = new ArrayList<String>();

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