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

package com.liferay.knowledgebase.service.impl;

import com.liferay.knowledgebase.model.KBFolder;
import com.liferay.knowledgebase.service.base.KBFolderServiceBaseImpl;
import com.liferay.knowledgebase.service.permission.AdminPermission;
import com.liferay.knowledgebase.service.permission.KBFolderPermission;
import com.liferay.knowledgebase.util.ActionKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import java.util.List;

/**
 * The implementation of the k b folder remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.knowledgebase.service.KBFolderService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.knowledgebase.service.base.KBFolderServiceBaseImpl
 * @see com.liferay.knowledgebase.service.KBFolderServiceUtil
 */
public class KBFolderServiceImpl extends KBFolderServiceBaseImpl {

	@Override
	public List<KBFolder> getFolders(
			long groupId, long parentKBFolderId, int start, int end)
		throws PortalException, SystemException {

		return kbFolderPersistence.filterFindByG_P(
			groupId, parentKBFolderId, start, end);
	}

	@Override
	public int getFoldersCount(long groupId, long parentFolderId)
		throws PortalException, SystemException {

		return kbFolderPersistence.filterCountByG_P(groupId, parentFolderId);
	}

}