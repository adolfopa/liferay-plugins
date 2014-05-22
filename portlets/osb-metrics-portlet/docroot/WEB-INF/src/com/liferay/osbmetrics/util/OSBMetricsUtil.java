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

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import java.io.File;

import java.text.Format;

import java.util.Date;

/**
 * @author Rachael Koestartyo
 */
public class OSBMetricsUtil {

	public static FileEntry addOSBTicketWorkerSQLFileEntry(String sql)
		throws Exception {

		File file = null;

		try {
			file = FileUtil.createTempFile("sql");

			FileUtil.write(file, sql);

			Folder folder = getOSBTicketWorkerSQLFolder();

			Format dateFormat = FastDateFormatFactoryUtil.getSimpleDateFormat(
				"yyyy-MM-dd hh-mm-ss");

			String now = dateFormat.format(new Date());

			long userId = UserLocalServiceUtil.getDefaultUserId(
				PortalUtil.getDefaultCompanyId());

			String fileName =
				_SQL_FILE_NAME_PREFIX + now + StringPool.PERIOD + "sql";

			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setAddGuestPermissions(true);

			return DLAppLocalServiceUtil.addFileEntry(
				userId, folder.getGroupId(), folder.getFolderId(), fileName,
				MimeTypesUtil.getContentType(file),
				FileUtil.stripExtension(fileName), StringPool.BLANK,
				StringPool.BLANK, file, serviceContext);
		}
		finally {
			FileUtil.delete(file);
		}
	}

	protected static Folder getOSBTicketWorkerSQLFolder() throws Exception {
		long companyId = PortalUtil.getDefaultCompanyId();

		long userId = UserLocalServiceUtil.getDefaultUserId(companyId);

		Group group = GroupLocalServiceUtil.getGroup(
			companyId, GroupConstants.GUEST);

		long groupId = group.getGroupId();

		try {
			Folder folder = DLAppLocalServiceUtil.getFolder(
				groupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				_SQL_FOLDER_NAME);

			if (folder != null) {
				return folder;
			}
		}
		catch (Exception e) {
		}

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		return DLAppLocalServiceUtil.addFolder(
			userId, groupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			_SQL_FOLDER_NAME, StringPool.BLANK, serviceContext);
	}

	private static final String _SQL_FILE_NAME_PREFIX = "osb_ticket_worker-";

	private static final String _SQL_FOLDER_NAME = "OSB Ticket Worker SQL";

}