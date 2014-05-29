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

package com.liferay.osbmetrics.util;

import com.liferay.osbmetrics.tools.OSBTicketWorkerSQLBuilder;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.reports.model.Definition;
import com.liferay.reports.service.ClpSerializer;
import com.liferay.reports.service.DefinitionLocalServiceUtil;
import com.liferay.reports.service.EntryLocalServiceUtil;

import java.io.File;

import java.text.Format;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Rachael Koestartyo
 * @author Peter Shin
 */
public class OSBMetricsUtil {

	public static void addReportEntry(
			String reportFormat, String reportName, String newReportName,
			String emailAddresses, Map<String, String> reportParametersMap)
		throws Exception {

		Group group = GroupLocalServiceUtil.getGroup(
			PortalUtil.getDefaultCompanyId(), GroupConstants.GUEST);

		Definition definition = getDefinition(reportName);

		ServiceContext serviceContext = new ServiceContext();

		EntryLocalServiceUtil.addEntry(
			UserLocalServiceUtil.getDefaultUserId(group.getCompanyId()),
			group.getGroupId(), definition.getDefinitionId(), reportFormat,
			false, null, null, false, StringPool.BLANK, StringPool.BLANK,
			emailAddresses, StringPool.BLANK, StringPool.BLANK, newReportName,
			getReportParameters(reportParametersMap), serviceContext);
	}

	public static void checkOSBTicketWorkers() throws Exception {

		// Database

		OSBTicketWorkerSQLBuilder osbTicketWorkerSQLBuilder =
			new OSBTicketWorkerSQLBuilder();

		String sql = osbTicketWorkerSQLBuilder.buildSQL();

		if (Validator.isNull(sql)) {
			return;
		}

		DB db = DBFactoryUtil.getDB();

		db.runSQLTemplateString(sql, false, true);

		// Document library

		addOSBTicketWorkerSQLFileEntry(sql);
	}

	public static long getDefinitionId(String reportName)
		throws SystemException {

		ClassLoader classLoader = (ClassLoader)PortletBeanLocatorUtil.locate(
			ClpSerializer.getServletContextName(), "portletClassLoader");

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			Definition.class, classLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("definitionId"));

		dynamicQuery.add(RestrictionsFactoryUtil.eq("reportName", reportName));

		List<Long> definitionIds = DefinitionLocalServiceUtil.dynamicQuery(
			dynamicQuery);

		if ((definitionIds == null) || definitionIds.isEmpty()) {
			return 0;
		}

		return definitionIds.get(0);
	}

	protected static FileEntry addOSBTicketWorkerSQLFileEntry(String sql)
		throws Exception {

		File file = null;

		try {
			file = FileUtil.createTempFile("sql");

			FileUtil.write(file, sql);

			Folder folder = getOSBTicketWorkerSQLFolder();

			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			serviceContext.setScopeGroupId(folder.getGroupId());

			Format format = FastDateFormatFactoryUtil.getSimpleDateFormat(
				"yyyy-MM-dd_HH-mm-z");

			String sourceFileName =
				_OSB_TICKET_WORKER_SQL_FILE_NAME_PREFIX +
					format.format(new Date()) + ".sql";

			serviceContext.setAttribute("sourceFileName", sourceFileName);

			return DLAppLocalServiceUtil.addFileEntry(
				folder.getUserId(), folder.getRepositoryId(),
				folder.getFolderId(), sourceFileName,
				MimeTypesUtil.getContentType(file),
				FileUtil.stripExtension(sourceFileName), StringPool.BLANK,
				StringPool.BLANK, file, serviceContext);
		}
		finally {
			FileUtil.delete(file);
		}
	}

	protected static Folder getOSBTicketWorkerSQLFolder() throws Exception {
		long companyId = PortalUtil.getDefaultCompanyId();

		Group group = GroupLocalServiceUtil.getGroup(
			companyId, GroupConstants.GUEST);

		Folder folder = null;

		try {
			folder = DLAppLocalServiceUtil.getFolder(
				group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				_OSB_TICKET_WORKER_SQL_FOLDER_NAME);
		}
		catch (Exception e) {
		}

		if (folder != null) {
			return folder;
		}

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setScopeGroupId(group.getGroupId());

		return DLAppLocalServiceUtil.addFolder(
			UserLocalServiceUtil.getDefaultUserId(companyId),
			group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			_OSB_TICKET_WORKER_SQL_FOLDER_NAME, StringPool.BLANK,
			serviceContext);
	}

	protected static String getReportParameters(
		Map<String, String> reportParametersMap) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (String key : reportParametersMap.keySet()) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.put("key", key);
			jsonObject.put("value", reportParametersMap.get(key));

			jsonArray.put(jsonObject);
		}

		return jsonArray.toString();
	}

	private static final String _OSB_TICKET_WORKER_SQL_FILE_NAME_PREFIX =
		"osb_ticket_worker-";

	private static final String _OSB_TICKET_WORKER_SQL_FOLDER_NAME =
		"OSB Ticket Worker SQL";

}