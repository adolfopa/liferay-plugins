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

package com.liferay.osbreports.hook.importer;

import com.liferay.compat.portal.kernel.util.LocalizationUtil;
import com.liferay.compat.portal.kernel.util.StringUtil;
import com.liferay.compat.portal.util.PortalUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.reports.model.Definition;
import com.liferay.reports.service.DefinitionLocalServiceUtil;

import java.io.InputStream;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Wesley Gong
 * @author Brian Wing Shun Chan
 */
public class ReportsImporter {

	public void importReports() throws Exception {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			"/resources/reports/definitions.xml");

		String xml = new String(FileUtil.getBytes(inputStream));

		Document document = SAXReaderUtil.read(xml);

		Element rootElement = document.getRootElement();

		List<Element> definitionElements = rootElement.elements("definition");

		for (Element definitionElement : definitionElements) {
			addDefinition(definitionElement);
		}
	}

	protected void addDefinition(Element definitionElement) throws Exception {
		User user = getUser();

		Element nameElement = definitionElement.element("name");

		Element descriptionElement = definitionElement.element("description");

		Element sourceIdElement = definitionElement.element("source-id");

		long sourceId = GetterUtil.getLong(sourceIdElement.getText());

		Element parametersElement = definitionElement.element("parameters");

		Element fileElement = definitionElement.element("file");

		String fileName = fileElement.getText();

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			"/resources/reports/" + fileName);

		ServiceContext serviceContext = getServiceContext();

		List<Definition> definitions = getDefinitions(
			StringUtil.extractFirst(fileName, StringPool.PERIOD));

		if (definitions.isEmpty()) {
			DefinitionLocalServiceUtil.addDefinition(
				user.getUserId(), serviceContext.getScopeGroupId(),
				getLocalizationMap(nameElement),
				getLocalizationMap(descriptionElement), sourceId,
				getParameters(parametersElement), fileName, inputStream,
				serviceContext);
		}
		else {
			for (Definition reportDefinition : definitions) {
				DefinitionLocalServiceUtil.updateDefinition(
					reportDefinition.getDefinitionId(),
					reportDefinition.getNameMap(),
					reportDefinition.getDescriptionMap(), sourceId,
					getParameters(parametersElement), fileName, inputStream,
					serviceContext);
			}
		}
	}

	protected List<Definition> getDefinitions(String reportName)
		throws Exception {

		DynamicQuery dynamicQuery = DefinitionLocalServiceUtil.dynamicQuery();

		Property property = PropertyFactoryUtil.forName("reportName");

		dynamicQuery.add(property.eq(reportName));

		return DefinitionLocalServiceUtil.dynamicQuery(dynamicQuery);
	}

	protected Map<Locale, String> getLocalizationMap(
			Element localizationElement)
		throws Exception {

		List<Element> entryElements = localizationElement.elements("entry");

		String[] languageIds = new String[entryElements.size()];
		String[] values = new String[entryElements.size()];;

		for (int i = 0; i < entryElements.size(); i++) {
			Element entryElement = entryElements.get(i);

			languageIds[i] = entryElement.attributeValue("language-id");
			values[i] = entryElement.getText();
		}

		return LocalizationUtil.getLocalizationMap(languageIds, values);
	}

	protected String getParameters(Element parametersElement) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		List<Element> parameterElements = parametersElement.elements(
			"parameter");

		for (Element parameterElement : parameterElements) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			String name = parameterElement.attributeValue("key");

			jsonObject.put("key", name);

			String type = parameterElement.attributeValue("type");

			jsonObject.put("type", type);

			String value = parameterElement.getText();

			jsonObject.put("value", value);

			jsonArray.put(jsonObject);
		}

		return jsonArray.toString();
	}

	protected ServiceContext getServiceContext() throws Exception {
		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);

		Group group = GroupLocalServiceUtil.getGroup(
			PortalUtil.getDefaultCompanyId(), GroupConstants.GUEST);

		serviceContext.setScopeGroupId(group.getGroupId());

		return serviceContext;
	}

	protected User getUser() throws Exception {
		return UserLocalServiceUtil.getDefaultUser(
			PortalUtil.getDefaultCompanyId());
	}

}