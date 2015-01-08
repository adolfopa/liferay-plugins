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

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfTime;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.repository.external.ExtRepositoryFileVersion;

import java.util.Date;

/**
 * @author Ivan Zaera
 */
public class DocumentumFileVersion implements ExtRepositoryFileVersion {

	public DocumentumFileVersion(
		IDfDocument iDfDocument, IDfDocument iDfDocumentFirstVersion) {

		_iDfDocument = iDfDocument;
		_iDfDocumentFirstVersion = iDfDocumentFirstVersion;
	}

	@Override
	public String getChangeLog() {
		try {
			return _iDfDocument.getLogEntry();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public Date getCreateDate() {
		try {
			IDfTime creationDate = _iDfDocument.getCreationDate();

			return creationDate.getDate();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public String getExtRepositoryModelKey() {
		try {
			IDfId iDfId = _iDfDocumentFirstVersion.getObjectId();

			return iDfId.getId() + StringPool.AT + getVersion();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	public IDfDocument getIDfDocument() {
		return _iDfDocument;
	}

	@Override
	public String getMimeType() {
		return null;
	}

	@Override
	public String getOwner() {
		try {
			return _iDfDocument.getOwnerName();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public long getSize() {
		try {
			return _iDfDocument.getContentSize();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public String getVersion() {
		try {
			return _iDfDocument.getVersionLabel(0);
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	private final IDfDocument _iDfDocument;
	private final IDfDocument _iDfDocumentFirstVersion;

}