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

package com.liferay.documentum.repository;

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.common.DfException;

import com.liferay.documentum.repository.model.DocumentumVersionNumber;
import com.liferay.portal.kernel.exception.SystemException;

import java.util.Comparator;

/**
 * @author Ivan Zaera
 */
public class IDfDocumentVersionsComparator implements Comparator<IDfDocument> {

	@Override
	public int compare(IDfDocument iDfDocument1, IDfDocument iDfDocument2) {
		try {
			String versionLabel1 = iDfDocument1.getVersionLabel(0);
			String versionLabel2 = iDfDocument2.getVersionLabel(0);

			DocumentumVersionNumber documentumVersionNumber1 =
				new DocumentumVersionNumber(versionLabel1);
			DocumentumVersionNumber documentumVersionNumber2 =
				new DocumentumVersionNumber(versionLabel2);

			return -documentumVersionNumber1.compareTo(
				documentumVersionNumber2);
		}
		catch (DfException de) {
			throw new SystemException(de);
		}
	}

}