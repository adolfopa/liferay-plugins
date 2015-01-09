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

import com.liferay.documentum.repository.model.DocumentumVersionNumber;
import com.liferay.repository.external.ExtRepositoryFileVersion;

import java.util.Comparator;

/**
 * @author Ivan Zaera
 */
public class ExtRepositoryFileVersionsComparator
	implements Comparator<ExtRepositoryFileVersion> {

	@Override
	public int compare(
		ExtRepositoryFileVersion extRepositoryFileVersion1,
		ExtRepositoryFileVersion extRepositoryFileVersion2) {

		String version1 = extRepositoryFileVersion1.getVersion();
		String version2 = extRepositoryFileVersion2.getVersion();

		DocumentumVersionNumber documentumVersionNumber1 =
			new DocumentumVersionNumber(version1);
		DocumentumVersionNumber documentumVersionNumber2 =
			new DocumentumVersionNumber(version2);

		return -documentumVersionNumber1.compareTo(documentumVersionNumber2);
	}

}