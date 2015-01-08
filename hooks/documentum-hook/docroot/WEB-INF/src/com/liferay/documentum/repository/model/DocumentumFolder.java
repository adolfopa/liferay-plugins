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

import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.common.DfException;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.repository.external.ExtRepositoryFolder;

/**
 * @author Ivan Zaera
 */
public class DocumentumFolder
	extends DocumentumObject implements ExtRepositoryFolder {

	public DocumentumFolder(IDfFolder iDfFolder, boolean root) {
		super(iDfFolder);

		_iDfFolder = iDfFolder;
		_root = root;
	}

	public IDfFolder getIDfFolder() {
		return _iDfFolder;
	}

	@Override
	public String getName() {
		try {
			return _iDfFolder.getObjectName();
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

	@Override
	public boolean isRoot() {
		return _root;
	}

	private final IDfFolder _iDfFolder;
	private boolean _root;

}