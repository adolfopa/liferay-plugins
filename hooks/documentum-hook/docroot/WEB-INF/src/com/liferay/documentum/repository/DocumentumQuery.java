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

import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;

import com.liferay.documentum.repository.model.Constants;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.RepositoryException;
import com.liferay.repository.external.ExtRepositoryObjectType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Zaera
 */
public class DocumentumQuery {

	public DocumentumQuery(IDfClientX iDfClientX, IDfSession iDfSession) {
		_iDfClientX = iDfClientX;
		_iDfSession = iDfSession;
	}

	public int getCount(
			String extRepositoryFolderKey,
			ExtRepositoryObjectType<?> extRepositoryObjectType)
		throws DfException {

		IDfCollection iDfCollection = null;

		try {
			IDfQuery iDfQuery = _iDfClientX.getQuery();

			String queryString = _buildSysObjectQuery(
				"COUNT(r_object_id) AS num_hits", extRepositoryFolderKey,
				extRepositoryObjectType);

			iDfQuery.setDQL(queryString);

			if (_log.isDebugEnabled()) {
				_log.debug("Executing query: " + queryString);
			}

			iDfCollection = iDfQuery.execute(
				_iDfSession, IDfQuery.DF_READ_QUERY);

			int total = 0;

			if (iDfCollection.next()) {
				total = iDfCollection.getInt("num_hits");
			}

			return total;
		}
		finally {
			_close(iDfCollection);
		}
	}

	public List<IDfSysObject> getIDfSysObjects(
			String extRepositoryFolderKey,
			ExtRepositoryObjectType<?> extRepositoryObjectType)
		throws DfException {

		IDfCollection iDfCollection = null;

		try {
			List<IDfSysObject> iDfSysObjects = new ArrayList<>();

			IDfQuery iDfQuery = _iDfClientX.getQuery();

			String queryString = _buildSysObjectQuery(
				Constants.R_OBJECT_ID, extRepositoryFolderKey,
				extRepositoryObjectType);

			iDfQuery.setDQL(queryString);

			if (_log.isDebugEnabled()) {
				_log.debug("Executing query: " + queryString);
			}

			iDfCollection = iDfQuery.execute(
				_iDfSession, IDfQuery.DF_READ_QUERY);

			while (iDfCollection.next()) {
				IDfId idfId = iDfCollection.getId(Constants.R_OBJECT_ID);

				IDfSysObject idfSysObject = (IDfSysObject)_iDfSession.getObject(
					idfId);

				if (_log.isTraceEnabled()) {
					_log.trace(idfSysObject.dump());
				}

				try {
					iDfSysObjects.add(idfSysObject);
				}
				catch (RepositoryException re) {
					if (_log.isWarnEnabled()) {
						_log.warn(re, re);
					}
				}
			}

			return iDfSysObjects;
		}
		finally {
			_close(iDfCollection);
		}
	}

	private String _buildSysObjectQuery(
		String selector, String extRepositoryFolderKey,
		ExtRepositoryObjectType<?> extRepositoryObjectType) {

		StringBuilder sb = new StringBuilder(16);

		sb.append("SELECT ");
		sb.append(selector);
		sb.append(" FROM ");
		sb.append(Constants.DM_SYSOBJECT);
		sb.append(" WHERE FOLDER(ID('");
		sb.append(extRepositoryFolderKey);
		sb.append("')) AND ");

		if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
			sb.append(Constants.R_OBJECT_TYPE);
			sb.append("='");
			sb.append(Constants.DM_DOCUMENT);
			sb.append("'");
		}
		else if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
			sb.append(Constants.R_OBJECT_TYPE);
			sb.append("='");
			sb.append(Constants.DM_FOLDER);
			sb.append("'");
		}
		else {
			sb.append("((");
			sb.append(Constants.R_OBJECT_TYPE);
			sb.append("='");
			sb.append(Constants.DM_DOCUMENT);
			sb.append("') OR (");
			sb.append(Constants.R_OBJECT_TYPE);
			sb.append("='");
			sb.append(Constants.DM_FOLDER);
			sb.append("'))");
		}

		return sb.toString();
	}

	private void _close(IDfCollection iDfCollection) {
		if (iDfCollection != null) {
			try {
				iDfCollection.close();
			}
			catch (DfException de) {
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(DocumentumQuery.class);

	private final IDfClientX _iDfClientX;
	private final IDfSession _iDfSession;

}