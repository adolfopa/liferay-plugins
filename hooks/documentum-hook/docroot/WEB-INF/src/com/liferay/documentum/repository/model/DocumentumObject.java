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

package com.liferay.documentum.repository.model;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfTime;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.repository.external.ExtRepositoryModel;
import com.liferay.repository.external.ExtRepositoryObject;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ivan Zaera
 */
public abstract class DocumentumObject
	implements ExtRepositoryObject, ExtRepositoryModel {

	public DocumentumObject(IDfSysObject iDfSysObject) {
		_iDfSysObject = iDfSysObject;
	}

	@Override
	public boolean containsPermission(
		ExtRepositoryPermission extRepositoryPermission) {

		if (_unsupportedExtRepositoryPermissions.contains(
				extRepositoryPermission)) {

			return false;
		}

		try {
			int permit = _permits.get(extRepositoryPermission);

			return _iDfSysObject.getPermit() >= permit;
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public Date getCreateDate() {
		try {
			IDfTime iDfTime = _iDfSysObject.getCreationDate();

			return iDfTime.getDate();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public String getDescription() {
		return StringPool.BLANK;
	}

	@Override
	public String getExtension() {
		try {
			return FileUtil.getExtension(_iDfSysObject.getObjectName());
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public String getExtRepositoryModelKey() {
		try {
			IDfId iDfId = _iDfSysObject.getObjectId();

			return iDfId.getId();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	public IDfSysObject getIDfSysObject() {
		return _iDfSysObject;
	}

	@Override
	public Date getModifiedDate() {
		try {
			IDfTime iDfTime = _iDfSysObject.getModifyDate();

			return iDfTime.getDate();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public String getOwner() {
		try {
			return _iDfSysObject.getOwnerName();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public long getSize() {
		try {
			return _iDfSysObject.getContentSize();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	private static Map<ExtRepositoryPermission, Integer> _permits =
		new HashMap<>();
	private static Set<ExtRepositoryPermission>
		_unsupportedExtRepositoryPermissions = EnumSet.of(
			ExtRepositoryPermission.ADD_DISCUSSION,
			ExtRepositoryPermission.ADD_SHORTCUT,
			ExtRepositoryPermission.DELETE_DISCUSSION,
			ExtRepositoryPermission.PERMISSIONS,
			ExtRepositoryPermission.UPDATE_DISCUSSION);

	static {
		_permits.put(
			ExtRepositoryPermission.ACCESS, Constants.DF_PERMIT_BROWSE);
		_permits.put(
			ExtRepositoryPermission.ADD_DOCUMENT, Constants.DF_PERMIT_WRITE);
		_permits.put(
			ExtRepositoryPermission.ADD_FOLDER, Constants.DF_PERMIT_WRITE);
		_permits.put(
			ExtRepositoryPermission.ADD_SUBFOLDER, Constants.DF_PERMIT_WRITE);
		_permits.put(
			ExtRepositoryPermission.DELETE, Constants.DF_PERMIT_DELETE);
		_permits.put(ExtRepositoryPermission.UPDATE, Constants.DF_PERMIT_WRITE);
		_permits.put(ExtRepositoryPermission.VIEW, Constants.DF_PERMIT_READ);
	}

	private final IDfSysObject _iDfSysObject;

}