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

package com.liferay.sharepoint.repository;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.RepositoryException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.util.AutoResetThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TransientValue;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.DuplicateFolderNameException;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.NoSuchFolderException;
import com.liferay.repository.external.CredentialsProvider;
import com.liferay.repository.external.ExtRepository;
import com.liferay.repository.external.ExtRepositoryAdapter;
import com.liferay.repository.external.ExtRepositoryFileEntry;
import com.liferay.repository.external.ExtRepositoryFileVersion;
import com.liferay.repository.external.ExtRepositoryFileVersionDescriptor;
import com.liferay.repository.external.ExtRepositoryFolder;
import com.liferay.repository.external.ExtRepositoryObject;
import com.liferay.repository.external.ExtRepositoryObjectType;
import com.liferay.repository.external.ExtRepositorySearchResult;
import com.liferay.repository.external.search.ExtRepositoryQueryMapper;
import com.liferay.sharepoint.connector.SharepointConnection;
import com.liferay.sharepoint.connector.SharepointConnection.CheckInType;
import com.liferay.sharepoint.connector.SharepointConnection.ObjectTypeFilter;
import com.liferay.sharepoint.connector.SharepointConnectionFactory;
import com.liferay.sharepoint.connector.SharepointException;
import com.liferay.sharepoint.connector.SharepointObject;
import com.liferay.sharepoint.connector.SharepointRuntimeException;
import com.liferay.sharepoint.connector.SharepointVersion;
import com.liferay.sharepoint.connector.operation.PathHelper;
import com.liferay.sharepoint.connector.operation.URLHelper;
import com.liferay.sharepoint.repository.model.SharepointWSFileEntry;
import com.liferay.sharepoint.repository.model.SharepointWSFileVersion;
import com.liferay.sharepoint.repository.model.SharepointWSFolder;
import com.liferay.sharepoint.repository.model.SharepointWSObject;
import com.liferay.sharepoint.repository.search.SharepointQueryBuilder;

import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * @author Ivan Zaera
 */
public class SharepointWSRepository
	extends ExtRepositoryAdapter implements ExtRepository {

	public SharepointWSRepository() {
		super(null);
	}

	@Override
	public ExtRepositoryFileEntry addExtRepositoryFileEntry(
			String extRepositoryParentFolderKey, String mimeType, String title,
			String description, String changeLog, InputStream inputStream)
		throws PortalException, SystemException {

		String filePath = null;

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject parentFolderSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryParentFolderKey));

			String parentFolderPath = parentFolderSharepointObject.getPath();

			filePath = pathHelper.buildPath(parentFolderPath, title);

			sharepointConnection.addFile(
				parentFolderPath, title, changeLog, inputStream);

			SharepointObject fileSharepointObject =
				sharepointConnection.getSharepointObject(filePath);

			return new SharepointWSFileEntry(fileSharepointObject);
		}
		catch (SharepointException se) {
			processAddEntryException(se, false, filePath, title);

			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	protected void processAddEntryException(
			SharepointException se, boolean folder, String path, String name)
		throws PortalException, SystemException {

		if (path == null) {
			return;
		}

		SharepointConnection sharepointConnection = getSharepointConnection();

		try {
			SharepointObject sharepointObject =
				sharepointConnection.getSharepointObject(path);

			if (sharepointObject == null) {
				return;
			}

			if (folder) {
				throw new DuplicateFolderNameException(name);
			}
			else {
				throw new DuplicateFileException(name);
			}
		}
		catch (SharepointException se) {

			// Ignore this exception: if it failed object does not exist

		}
	}

	@Override
	public ExtRepositoryFolder addExtRepositoryFolder(
			String extRepositoryParentFolderKey, String name,
			String description)
		throws PortalException, SystemException {

		String folderPath = null;

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject parentFolderSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryParentFolderKey));

			String parentFolderPath = parentFolderSharepointObject.getPath();

			folderPath = pathHelper.buildPath(parentFolderPath, name);

			sharepointConnection.addFolder(parentFolderPath, name);

			SharepointObject folderSharepointObject =
				sharepointConnection.getSharepointObject(folderPath);

			return new SharepointWSFolder(folderSharepointObject);
		}
		catch (SharepointException se) {
			processAddEntryException(se, true, folderPath, name);

			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public ExtRepositoryFileVersion cancelCheckOut(
			String extRepositoryFileEntryKey)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject fileSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryFileEntryKey));

			String filePath = fileSharepointObject.getPath();

			sharepointConnection.cancelCheckOutFile(filePath);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}

		return null;
	}

	@Override
	public void checkInExtRepositoryFileEntry(
			String extRepositoryFileEntryKey, boolean createMajorVersion,
			String changeLog)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject fileSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryFileEntryKey));

			String filePath = fileSharepointObject.getPath();

			CheckInType checkInType = null;

			if (createMajorVersion) {
				checkInType = CheckInType.MAJOR;
			}
			else {
				checkInType = CheckInType.MINOR;
			}

			sharepointConnection.checkInFile(filePath, changeLog, checkInType);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public ExtRepositoryFileEntry checkOutExtRepositoryFileEntry(
			String extRepositoryFileEntryKey)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject fileSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryFileEntryKey));

			String filePath = fileSharepointObject.getPath();

			sharepointConnection.checkOutFile(filePath);

			return new SharepointWSFileEntry(fileSharepointObject);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> T copyExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFileEntryKey, String newExtRepositoryFolderKey,
			String newTitle)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject fileSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryFileEntryKey));

			String filePath = fileSharepointObject.getPath();

			SharepointObject folderSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(newExtRepositoryFolderKey));

			String folderPath = folderSharepointObject.getPath();

			String newFilePath = pathHelper.buildPath(folderPath, newTitle);

			sharepointConnection.copySharepointObject(filePath, newFilePath);

			SharepointObject newSharepointObject =
				sharepointConnection.getSharepointObject(newFilePath);

			return toExtRepositoryObject(
				extRepositoryObjectType, newSharepointObject);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public void deleteExtRepositoryObject(
			ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			String extRepositoryObjectKey)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject sharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryObjectKey));

			String path = sharepointObject.getPath();

			sharepointConnection.deleteSharepointObject(path);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public String getAuthType() {
		return CompanyConstants.AUTH_TYPE_SN;
	}

	@Override
	public InputStream getContentStream(
			ExtRepositoryFileEntry extRepositoryFileEntry)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointWSFileEntry sharepointWSFileEntry =
				(SharepointWSFileEntry)extRepositoryFileEntry;

			SharepointObject fileSharepointObject =
				sharepointWSFileEntry.getSharepointObject();

			return sharepointConnection.getInputStream(fileSharepointObject);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public InputStream getContentStream(
			ExtRepositoryFileVersion extRepositoryFileVersion)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointWSFileVersion sharepointWSFileVersion =
				(SharepointWSFileVersion)extRepositoryFileVersion;

			SharepointVersion sharepointVersion =
				sharepointWSFileVersion.getSharepointVersion();

			return sharepointConnection.getInputStream(sharepointVersion);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public ExtRepositoryFileVersion getExtRepositoryFileVersion(
			ExtRepositoryFileEntry extRepositoryFileEntry, String version)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointWSFileEntry sharepointWSFileEntry =
				(SharepointWSFileEntry)extRepositoryFileEntry;

			SharepointObject fileSharepointObject =
				sharepointWSFileEntry.getSharepointObject();

			String filePath = fileSharepointObject.getPath();

			List<SharepointVersion> sharepointVersions =
				sharepointConnection.getSharepointVersions(filePath);

			for (SharepointVersion sharepointVersion : sharepointVersions) {
				if (version.equals(sharepointVersion.getVersion())) {
					return new SharepointWSFileVersion(sharepointVersion);
				}
			}

			return null;
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public ExtRepositoryFileVersionDescriptor
		getExtRepositoryFileVersionDescriptor(
			String extRepositoryFileVersionKey) {

		String[] extRepositoryFileVersionKeyParts = StringUtil.split(
			extRepositoryFileVersionKey, StringPool.AT);

		String extRepositoryFileEntryKey = extRepositoryFileVersionKeyParts[0];
		String version = extRepositoryFileVersionKeyParts[1];

		return new ExtRepositoryFileVersionDescriptor(
			extRepositoryFileEntryKey, version);
	}

	@Override
	public List<ExtRepositoryFileVersion> getExtRepositoryFileVersions(
			ExtRepositoryFileEntry extRepositoryFileEntry)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointWSFileEntry sharepointWSFileEntry =
				(SharepointWSFileEntry)extRepositoryFileEntry;

			SharepointObject fileSharepointObject =
				sharepointWSFileEntry.getSharepointObject();

			String filePath = fileSharepointObject.getPath();

			List<SharepointVersion> sharepointVersions =
				sharepointConnection.getSharepointVersions(filePath);

			List<ExtRepositoryFileVersion> sharepointWSVersions =
				new ArrayList<ExtRepositoryFileVersion>();

			for (SharepointVersion sharepointVersion : sharepointVersions) {
				SharepointWSFileVersion sharepointWSFileVersion =
					new SharepointWSFileVersion(sharepointVersion);

				sharepointWSVersions.add(sharepointWSFileVersion);
			}

			return sharepointWSVersions;
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryObjectKey)
		throws PortalException, SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject sharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryObjectKey));

			if (sharepointObject == null) {
				if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
					throw new NoSuchFolderException(extRepositoryObjectKey);
				}
				else {
					throw new NoSuchFileEntryException(extRepositoryObjectKey);
				}
			}

			return toExtRepositoryObject(
				extRepositoryObjectType, sharepointObject);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFolderKey, String title)
		throws PortalException, SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject folderSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryFolderKey));

			String folderPath = folderSharepointObject.getPath();

			ObjectTypeFilter objectTypeFilter = toObjectTypeFilter(
				extRepositoryObjectType);

			List<SharepointObject> sharepointObjects =
				sharepointConnection.getSharepointObjects(
					folderPath, objectTypeFilter);

			for (SharepointObject sharepointObject : sharepointObjects) {
				if (title.equals(sharepointObject.getName())) {
					return toExtRepositoryObject(
						extRepositoryObjectType, sharepointObject);
				}
			}

			if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
				throw new NoSuchFolderException(title);
			}

			throw new NoSuchFileEntryException(title);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFolderKey)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject folderSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryFolderKey));

			String folderPath = folderSharepointObject.getPath();

			ObjectTypeFilter objectTypeFilter = toObjectTypeFilter(
				extRepositoryObjectType);

			List<SharepointObject> sharepointObjects =
				sharepointConnection.getSharepointObjects(
					folderPath, objectTypeFilter);

			List<T> extRepositoryObjects = new ArrayList<T>();

			for (SharepointObject sharepointObject : sharepointObjects) {
				T extRepositoryObject = toExtRepositoryObject(
					extRepositoryObjectType, sharepointObject);

				extRepositoryObjects.add(extRepositoryObject);
			}

			return extRepositoryObjects;
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public int getExtRepositoryObjectsCount(
			ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			String extRepositoryFolderKey)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject folderSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryFolderKey));

			String folderPath = folderSharepointObject.getPath();

			ObjectTypeFilter objectTypeFilter = toObjectTypeFilter(
				extRepositoryObjectType);

			return sharepointConnection.getSharepointObjectsCount(
				folderPath, objectTypeFilter);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public ExtRepositoryFolder getExtRepositoryParentFolder(
			ExtRepositoryObject extRepositoryObject)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointWSObject sharepointWSObject =
				(SharepointWSObject)extRepositoryObject;

			SharepointObject sharepointObject =
				sharepointWSObject.getSharepointObject();

			String parentFolderPath = sharepointObject.getFolderPath();

			if (parentFolderPath == null) {
				return null;
			}

			SharepointObject parentFolderSharepointObject =
				sharepointConnection.getSharepointObject(parentFolderPath);

			return new SharepointWSFolder(parentFolderSharepointObject);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public String getLiferayLogin(String extRepositoryLogin) {
		int index = extRepositoryLogin.lastIndexOf(StringPool.BACK_SLASH);

		return extRepositoryLogin.substring(index + 1);
	}

	@Override
	public String getRootFolderKey() {
		return _rootFolderKey;
	}

	public SharepointConnection getSharepointConnection()
		throws RepositoryException {

		SharepointConnection sharepointConnection = null;

		HttpSession httpSession = PortalSessionThreadLocal.getHttpSession();

		if (httpSession != null) {
			TransientValue<SharepointConnection> transientValue =
				(TransientValue<SharepointConnection>)
					httpSession.getAttribute(
						SharepointWSRepository.class.getName());

			if (transientValue != null) {
				sharepointConnection = transientValue.getValue();
			}
		}
		else {
			sharepointConnection = _sharepointConnectionThreadLocal.get();
		}

		if (sharepointConnection != null) {
			return sharepointConnection;
		}

		sharepointConnection = buildSharepointConnection();

		if (httpSession != null) {
			TransientValue<SharepointConnection> transientValue =
				new TransientValue<SharepointConnection>(sharepointConnection);

			httpSession.setAttribute(
				SharepointWSRepository.class.getName(), transientValue);
		}

		_sharepointConnectionThreadLocal.set(sharepointConnection);

		return sharepointConnection;
	}

	public String getSharepointLogin(String liferayLogin) {
		return _host + StringPool.BACK_SLASH + liferayLogin;
	}

	@Override
	public List<String> getSubfolderKeys(
			String extRepositoryFolderKey, boolean recurse)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject folderSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryFolderKey));

			String folderPath = folderSharepointObject.getPath();

			List<String> extRepositoryFolderKeys = new ArrayList<String>();

			getSubfolderKeys(folderPath, extRepositoryFolderKeys);

			return extRepositoryFolderKeys;
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
	}

	@Override
	public String[] getSupportedConfigurations() {
		return _SUPPORTED_CONFIGURATIONS;
	}

	@Override
	public String[][] getSupportedParameters() {
		return _SUPPORTED_PARAMETERS;
	}

	@Override
	public void initRepository(
			UnicodeProperties typeSettingsProperties,
			CredentialsProvider credentialsProvider)
		throws SystemException {

		try {
			_credentialsProvider = credentialsProvider;

			_libraryName = typeSettingsProperties.getProperty(_LIBRARY_NAME);

			String siteURL = typeSettingsProperties.getProperty(_SITE_URL);

			URL url = urlHelper.toURL(siteURL);

			_host = url.getHost();
			_protocol = url.getProtocol();
			_sitePath = url.getPath();

			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject rootFolderSharepointObject =
				sharepointConnection.getSharepointObject(StringPool.SLASH);

			_rootFolderKey = String.valueOf(
				rootFolderSharepointObject.getSharepointObjectId());
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> T moveExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryObjectKey, String newExtRepositoryFolderKey,
			String newTitle)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject sharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryObjectKey));

			String path = sharepointObject.getPath();

			SharepointObject folderSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(newExtRepositoryFolderKey));

			String folderPath = folderSharepointObject.getPath();

			String newPath = pathHelper.buildPath(folderPath, newTitle);

			if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
				sharepointConnection.checkOutFile(path);
			}

			sharepointConnection.moveSharepointObject(path, newPath);

			if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
				sharepointConnection.checkInFile(
					newPath, StringPool.BLANK, CheckInType.MAJOR);
			}

			sharepointObject = sharepointConnection.getSharepointObject(
				newPath);

			return toExtRepositoryObject(
				extRepositoryObjectType, sharepointObject);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	@Override
	public List<ExtRepositorySearchResult<?>> search(
			SearchContext searchContext, Query query,
			ExtRepositoryQueryMapper extRepositoryQueryMapper)
		throws PortalException, SystemException {

		List<ExtRepositorySearchResult<?>> extRepositorySearchResults =
			new ArrayList<ExtRepositorySearchResult<?>>();

		List<SharepointObject> sharepointObjects = doSearch(
			searchContext, query, extRepositoryQueryMapper);

		sharepointObjects = filter(searchContext, sharepointObjects);

		for (SharepointObject sharepointObject : sharepointObjects) {
			ExtRepositoryObject extRepositoryObject = toExtRepositoryObject(
				ExtRepositoryObjectType.OBJECT, sharepointObject);

			ExtRepositorySearchResult<?> extRepositorySearchResult =
				new ExtRepositorySearchResult<ExtRepositoryObject>(
					extRepositoryObject, 1, StringPool.BLANK);

			extRepositorySearchResults.add(extRepositorySearchResult);
		}

		return extRepositorySearchResults;
	}

	@Override
	public ExtRepositoryFileEntry updateExtRepositoryFileEntry(
			String extRepositoryFileEntryKey, String mimeType,
			InputStream inputStream)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			SharepointObject fileSharepointObject =
				sharepointConnection.getSharepointObject(
					toSharepointObjectId(extRepositoryFileEntryKey));

			String filePath = fileSharepointObject.getPath();

			sharepointConnection.updateFile(filePath, inputStream);

			return new SharepointWSFileEntry(fileSharepointObject);
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
		catch (SharepointRuntimeException sre) {
			throw new SystemException(sre);
		}
	}

	protected SharepointConnection buildSharepointConnection()
		throws RepositoryException {

		try {
			int serverPort = 80;

			if (_protocol.equals("https")) {
				serverPort = 443;
			}

			return SharepointConnectionFactory.getInstance(
				_protocol, _host, serverPort, _sitePath, _libraryName,
				_credentialsProvider.getLogin(),
				_credentialsProvider.getPassword());
		}
		catch (SharepointRuntimeException sre) {
			throw new RepositoryException(
				"Unable to communicate with the Sharepoint server", sre);
		}
	}

	protected List<SharepointObject> doSearch(
			SearchContext searchContext, Query query,
			ExtRepositoryQueryMapper extRepositoryQueryMapper)
		throws PortalException, SystemException {

		try {
			SharepointQueryBuilder sharepointQueryBuilder =
				new SharepointQueryBuilder(
					this, searchContext, query, extRepositoryQueryMapper);

			SharepointConnection sharepointConnection =
				getSharepointConnection();

			return sharepointConnection.getSharepointObjects(
				sharepointQueryBuilder.getQuery(),
				sharepointQueryBuilder.getQueryOptionsList());
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
	}

	protected List<SharepointObject> filter(
		SearchContext searchContext, List<SharepointObject> sharepointObjects) {

		return ListUtil.subList(
			sharepointObjects, searchContext.getStart(),
			searchContext.getEnd());
	}

	protected void getSubfolderKeys(
			String path, List<String> extRepositoryFolderKeys)
		throws SystemException {

		try {
			SharepointConnection sharepointConnection =
				getSharepointConnection();

			List<SharepointObject> folderSharepointObjects =
				sharepointConnection.getSharepointObjects(
					path, ObjectTypeFilter.FOLDERS);

			for (
				SharepointObject folderSharepointObject :
					folderSharepointObjects) {

				String extRepositoryFolderKey = String.valueOf(
					folderSharepointObject.getSharepointObjectId());

				extRepositoryFolderKeys.add(extRepositoryFolderKey);

				getSubfolderKeys(
					folderSharepointObject.getPath(), extRepositoryFolderKeys);
			}
		}
		catch (SharepointException se) {
			throw new SystemException(se);
		}
	}

	protected <T extends ExtRepositoryObject> T toExtRepositoryObject(
		ExtRepositoryObjectType<T> extRepositoryObjectType,
		SharepointObject sharepointObject) {

		if (sharepointObject.isFile()) {
			if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
				throw new IllegalArgumentException(
					"Invalid external repository object type " +
						extRepositoryObjectType + " for Sharepoint object " +
							sharepointObject);
			}

			return (T)new SharepointWSFileEntry(sharepointObject);
		}
		else {
			if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
				throw new IllegalArgumentException(
					"Invalid external repository object type " +
						extRepositoryObjectType + " for Sharepoint object " +
							sharepointObject);
			}

			return (T)new SharepointWSFolder(sharepointObject);
		}
	}

	protected ObjectTypeFilter toObjectTypeFilter(
		ExtRepositoryObjectType<? extends ExtRepositoryObject>
			extRepositoryObjectType) {

		ObjectTypeFilter objectTypeFilter = _objectTypeFilters.get(
			extRepositoryObjectType);

		if (objectTypeFilter == null) {
			throw new IllegalArgumentException(
				"Invalid external repository object type " +
					extRepositoryObjectType);
		}

		return objectTypeFilter;
	}

	protected long toSharepointObjectId(String key) {
		return GetterUtil.getLong(key);
	}

	protected static PathHelper pathHelper = new PathHelper();
	protected static URLHelper urlHelper = new URLHelper();

	private static final String _CONFIGURATION_WS = "SHAREPOINT_WS";

	private static final String _LIBRARY_NAME = "LIBRARY_NAME";

	private static final String _SITE_URL = "SITE_URL";

	private static final String[] _SUPPORTED_CONFIGURATIONS =
		{_CONFIGURATION_WS};

	private static final String[][] _SUPPORTED_PARAMETERS =
		{{_SITE_URL, _LIBRARY_NAME}};

	private static final Map<ExtRepositoryObjectType<?>, ObjectTypeFilter>
		_objectTypeFilters =
			new HashMap<ExtRepositoryObjectType<?>, ObjectTypeFilter>();

	static {
		_objectTypeFilters.put(
			ExtRepositoryObjectType.FILE, ObjectTypeFilter.FILES);
		_objectTypeFilters.put(
			ExtRepositoryObjectType.FOLDER, ObjectTypeFilter.FOLDERS);
		_objectTypeFilters.put(
			ExtRepositoryObjectType.OBJECT, ObjectTypeFilter.ALL);
	}

	private CredentialsProvider _credentialsProvider;
	private String _host;
	private String _libraryName;
	private String _protocol;
	private String _rootFolderKey;
	private AutoResetThreadLocal<SharepointConnection>
		_sharepointConnectionThreadLocal =
			new AutoResetThreadLocal<SharepointConnection>(
				SharepointConnection.class.getName());
	private String _sitePath;

}