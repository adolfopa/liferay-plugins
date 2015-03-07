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

package com.liferay.portal.workflow.kaleo.forms.ddm;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.dynamicdatalists.model.DDLRecordSet;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants;
import com.liferay.portlet.dynamicdatamapping.storage.StorageType;
import com.liferay.portlet.dynamicdatamapping.util.BaseDDMDisplay;
import com.liferay.portlet.dynamicdatamapping.util.DDMPermissionHandler;

import java.util.Locale;

/**
 * @author Marcellus Tavares
 */
public class KaleoFormsDDMDisplay extends BaseDDMDisplay {

	@Override
	public DDMPermissionHandler getDDMPermissionHandler() {
		return _ddmPermissionHandler;
	}

	@Override
	public String getEditTemplateTitle(
		DDMStructure structure, DDMTemplate template, Locale locale) {

		if ((structure != null) && (template == null)) {
			return LanguageUtil.format(
				locale, "new-form-for-field-set-x", structure.getName(locale),
				false);
		}

		return super.getEditTemplateTitle(structure, template, locale);
	}

	@Override
	public String getPortletId() {
		return "2_WAR_kaleoformsportlet";
	}

	@Override
	public String getStorageType() {
		return StorageType.XML.toString();
	}

	@Override
	public String getStructureName(Locale locale) {
		return LanguageUtil.get(locale, "field-set");
	}

	@Override
	public String getStructureType() {
		return DDLRecordSet.class.getName();
	}

	@Override
	public String getTemplateMode() {
		return DDMTemplateConstants.TEMPLATE_MODE_CREATE;
	}

	@Override
	public String getTemplateType() {
		return DDMTemplateConstants.TEMPLATE_TYPE_FORM;
	}

	@Override
	public String getViewTemplatesBackURL(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse, long classPK)
		throws Exception {

		return StringPool.BLANK;
	}

	private final DDMPermissionHandler _ddmPermissionHandler =
		new KaleoFormsDDMPermissionHandler();

}