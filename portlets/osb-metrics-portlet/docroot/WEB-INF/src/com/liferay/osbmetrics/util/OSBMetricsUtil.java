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

import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;

import java.io.File;
import java.io.FileWriter;

import java.text.Format;

import java.util.Date;

/**
 * @author Rachael Koestartyo
 */
public class OSBMetricsUtil {

	public static DLFileEntry createDLFileEntry(String content)
		throws Exception {

		DLFolder dlFolder = getDLFolder(_SQL_FOLDER_NAME);

		return createDLFileEntry(dlFolder, content, "sql");
	}

	protected static DLFileEntry createDLFileEntry(
			DLFolder dlFolder, String content, String extension)
		throws Exception {

		File file = FileUtil.createTempFile(extension);

		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());

		fileWriter.write(content);

		fileWriter.close();

		Format dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd hh-mm-ss");

		String now = dateFormat.format(new Date());

		long userId = UserLocalServiceUtil.getDefaultUserId(
			PortalUtil.getDefaultCompanyId());

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGuestPermissions(true);

		return DLFileEntryLocalServiceUtil.addFileEntry(
			userId, dlFolder.getGroupId(), dlFolder.getGroupId(),
			dlFolder.getFolderId(), _SQL_FILE_NAME_PREFIX + now + ".sql",
			"text/plain", _SQL_FILE_NAME_PREFIX + now + ".sql",
			StringPool.BLANK, StringPool.BLANK, 0L, null, file, null,
			file.length(), serviceContext);
	}

	protected static DLFolder getDLFolder(String folderName) throws Exception {
		long companyId = PortalUtil.getDefaultCompanyId();

		long userId = UserLocalServiceUtil.getDefaultUserId(companyId);

		Group group = GroupLocalServiceUtil.getGroup(
			companyId, GroupConstants.GUEST);

		long groupId = group.getGroupId();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGuestPermissions(true);

		DLFolder dlFolder = DLFolderLocalServiceUtil.fetchFolder(
			groupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			_SQL_FOLDER_NAME);

		if (dlFolder != null) {
			return dlFolder;
		}

		return DLFolderLocalServiceUtil.addFolder(
			userId, groupId, groupId, false,
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, folderName,
			StringPool.BLANK, false, serviceContext);
	}

	private static final String _SQL_FILE_NAME_PREFIX = "osb_ticket_worker-";

	private static final String _SQL_FOLDER_NAME = "OSB Ticket Worker SQL";

}