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
import com.liferay.portal.kernel.util.ObjectValuePair;
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
import com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalServiceUtil;

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

	public static DDLRecordSet getDDLRecordSet(
			KaleoProcess kaleoProcess, PortletSession portletSession)
		throws Exception {

		Map<Locale, String> nameMap = _getNameMap(portletSession);

		Map<Locale, String> descriptionMap = _getDescriptionMap(portletSession);

		long ddmStructureId = GetterUtil.getLong(
			portletSession.getAttribute("ddmStructureId"), 0);

		DDLRecordSet ddlRecordSet = null;

		if (kaleoProcess == null) {
			ddlRecordSet = DDLRecordSetLocalServiceUtil.createDDLRecordSet(0);
		}
		else {
			ddlRecordSet = kaleoProcess.getDDLRecordSet();

			if (nameMap.isEmpty()) {
				nameMap = ddlRecordSet.getNameMap();
			}

			if (descriptionMap.isEmpty()) {
				descriptionMap = ddlRecordSet.getDescriptionMap();
			}

			if (ddmStructureId == 0) {
				ddmStructureId = ddlRecordSet.getDDMStructureId();
			}
		}

		ddlRecordSet.setNameMap(nameMap);
		ddlRecordSet.setDescriptionMap(descriptionMap);
		ddlRecordSet.setDDMStructureId(ddmStructureId);

		return ddlRecordSet;
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

	public static List<ObjectValuePair<String, Long>> getTaskFormPairs(
			long companyId, long kaleoProcessId, String workflowDefinition,
			PortletSession portletSession)
		throws Exception {

		List<ObjectValuePair<String, Long>> taskFormPairs =
			new ArrayList<ObjectValuePair<String, Long>>();

		String initialStateName = getInitialStateName(
			companyId, workflowDefinition);

		long ddmTemplateId = GetterUtil.getLong(
			portletSession.getAttribute(initialStateName), 0);

		if ((ddmTemplateId == 0) && (kaleoProcessId > 0)) {
			KaleoProcess kaleoProcess = KaleoProcessServiceUtil.getKaleoProcess(
				kaleoProcessId);

			ddmTemplateId = kaleoProcess.getDDMTemplateId();
		}

		taskFormPairs.add(
			new ObjectValuePair<String, Long>(initialStateName, ddmTemplateId));

		for (String taskName : _getTaskNames(companyId, workflowDefinition)) {
			ObjectValuePair<String, Long> taskFormPair =
				new ObjectValuePair<String, Long>();

			taskFormPair.setKey(taskName);
			taskFormPair.setValue(
				_getDDMTemplateId(kaleoProcessId, taskName, portletSession));

			taskFormPairs.add(taskFormPair);
		}

		return taskFormPairs;
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

	private static Map<Locale, String> _getDescriptionMap(
			PortletSession portletSession)
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

		return descriptionMap;
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

	private static Map<Locale, String> _getNameMap(
			PortletSession portletSession)
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

		return nameMap;
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