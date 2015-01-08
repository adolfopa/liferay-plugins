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

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.DfAuthenticationException;
import com.documentum.fc.client.DfServiceException;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfList;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.operations.IDfCopyOperation;
import com.documentum.operations.IDfOperationError;

import com.liferay.documentum.repository.model.Constants;
import com.liferay.documentum.repository.model.DocumentumFileEntry;
import com.liferay.documentum.repository.model.DocumentumFileVersion;
import com.liferay.documentum.repository.model.DocumentumFolder;
import com.liferay.documentum.repository.model.DocumentumVersionNumber;
import com.liferay.documentum.repository.search.DQLQueryBuilder;
import com.liferay.portal.InvalidRepositoryException;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.RepositoryException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.PwdGenerator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portlet.documentlibrary.DuplicateFileException;
import com.liferay.portlet.documentlibrary.DuplicateFolderNameException;
import com.liferay.portlet.documentlibrary.FileExtensionException;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.NoSuchFileVersionException;
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
import com.liferay.repository.external.cache.ConnectionBuilder;
import com.liferay.repository.external.cache.ConnectionCache;
import com.liferay.repository.external.search.ExtRepositoryQueryMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ivan Zaera
 */
public class DocumentumRepository
	extends ExtRepositoryAdapter
	implements ConnectionBuilder<IDfSessionManager>, ExtRepository {

	public DocumentumRepository() {
		super(null);
	}

	@Override
	public ExtRepositoryFileEntry addExtRepositoryFileEntry(
			String extRepositoryParentFolderKey, String mimeType, String title,
			String description, String changeLog, InputStream inputStream)
		throws PortalException {

		IDfSession iDfSession = null;

		File file = null;

		try {
			iDfSession = getIDfSession();

			validateTitle(iDfSession, extRepositoryParentFolderKey, title);

			IDfDocument iDfDocument = (IDfDocument)iDfSession.newObject(
				Constants.DM_DOCUMENT);

			String contentType = getContentType(iDfSession, mimeType, title);

			if (Validator.isNull(contentType)) {
				throw new FileExtensionException(
					"Unsupported file type " + title);
			}

			iDfDocument.setContentType(contentType);

			iDfDocument.setLogEntry(changeLog);
			iDfDocument.setObjectName(title);
			iDfDocument.setTitle(description);

			IDfId iDfId = getIDfId(iDfSession, extRepositoryParentFolderKey);

			IDfFolder iDfFolder = (IDfFolder)iDfSession.getObject(iDfId);

			iDfDocument.link(iDfFolder.getFolderPath(0));

			StringBundler sb = new StringBundler(5);

			sb.append(SystemProperties.get(SystemProperties.TMP_DIR));
			sb.append("/liferay/documentum/");
			sb.append(PwdGenerator.getPassword());
			sb.append(StringPool.UNDERLINE);
			sb.append(title);

			String fileName = sb.toString();

			file = new File(fileName);

			FileUtil.write(file, inputStream);

			iDfDocument.setFile(fileName);

			iDfDocument.save();

			return toExtRepositoryObject(
				iDfSession, ExtRepositoryObjectType.FILE, iDfDocument);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		catch (IOException ioe) {
			throw new RepositoryException(ioe);
		}
		finally {
			_releaseSession(iDfSession);

			if ((file != null) && file.exists()) {
				file.delete();
			}
		}
	}

	@Override
	public ExtRepositoryFolder addExtRepositoryFolder(
			String extRepositoryParentFolderKey, String name,
			String description)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			validateTitle(iDfSession, extRepositoryParentFolderKey, name);

			IDfFolder iDfFolder = (IDfFolder)iDfSession.newObject(
				Constants.DM_FOLDER);

			iDfFolder.setObjectName(name);
			iDfFolder.setTitle(description);

			IDfId iDfId = getIDfId(iDfSession, extRepositoryParentFolderKey);

			IDfFolder iDfParentFolder = (IDfFolder)iDfSession.getObject(iDfId);

			iDfFolder.link(iDfParentFolder.getFolderPath(0));

			iDfFolder.save();

			return toExtRepositoryObject(
				iDfSession, ExtRepositoryObjectType.FOLDER, iDfFolder);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public IDfSessionManager buildConnection() throws RepositoryException {
		IDfSessionManager iDfSessionManager = null;

		try {
			IDfClient iDfClient = _iDfClientX.getLocalClient();

			iDfSessionManager = iDfClient.newSessionManager();

			IDfLoginInfo iDfLoginInfo = _iDfClientX.getLoginInfo();

			String password = _credentialsProvider.getPassword();

			iDfLoginInfo.setPassword(password);

			String login = _credentialsProvider.getLogin();

			iDfLoginInfo.setUser(login);

			iDfSessionManager.setIdentity(_repository, iDfLoginInfo);

			return iDfSessionManager;
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
	}

	@Override
	public ExtRepositoryFileVersion cancelCheckOut(
			String extRepositoryFileEntryKey)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = getIDfId(iDfSession, extRepositoryFileEntryKey);

			IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(iDfId);

			Map<String, IDfDocument> iDfDocumentVersions =
				getIDfDocumentVersions(iDfSession, iDfDocument);

			IDfDocument iDfDocumentLastVersion = getIDfDocumentLastVersion(
				iDfDocumentVersions);

			iDfDocumentLastVersion.cancelCheckout();

			iDfDocumentLastVersion.destroy();

			return new DocumentumFileVersion(
				iDfDocumentVersions.get(Constants.VERSION_LABEL_CURRENT),
				iDfDocument);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public void checkInExtRepositoryFileEntry(
		String extRepositoryFileEntryKey, boolean createMajorVersion,
		String changeLog) throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = getIDfId(iDfSession, extRepositoryFileEntryKey);

			IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(iDfId);

			Map<String, IDfDocument> iDfDocumentVersions =
				getIDfDocumentVersions(iDfSession, iDfDocument);

			IDfDocument iDfDocumentLastVersion = getIDfDocumentLastVersion(
				iDfDocumentVersions);

			iDfDocumentLastVersion.unmark(Constants.VERSION_LABEL_PWC);

			iDfDocumentLastVersion.mark(Constants.VERSION_LABEL_CURRENT);

			iDfDocumentLastVersion.save();
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public ExtRepositoryFileEntry checkOutExtRepositoryFileEntry(
			String extRepositoryFileEntryKey)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = getIDfId(iDfSession, extRepositoryFileEntryKey);

			IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(iDfId);

			Map<String, IDfDocument> iDfDocumentVersions =
				getIDfDocumentVersions(iDfSession, iDfDocument);

			IDfDocument iDfDocumentLastVersion = getIDfDocumentLastVersion(
				iDfDocumentVersions);

			String versionLabel = iDfDocumentLastVersion.getVersionLabel(0);

			if (!versionLabel.equals(Constants.VERSION_LABEL_PWC)) {
				iDfDocumentLastVersion.checkout();

				DocumentumVersionNumber documentumVersionNumber =
					new DocumentumVersionNumber(versionLabel);

				documentumVersionNumber = documentumVersionNumber.increment(
					false);

				iDfDocumentLastVersion.checkin(
					true,
					Constants.VERSION_LABEL_PWC + StringPool.COMMA +
						documentumVersionNumber.toString());
			}

			return toExtRepositoryObject(
				iDfSession, ExtRepositoryObjectType.FILE, iDfDocument);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> T copyExtRepositoryObject(
		ExtRepositoryObjectType<T> extRepositoryObjectType,
		String extRepositoryFileEntryKey, String newExtRepositoryFolderKey,
		String newTitle) throws PortalException {

		if (extRepositoryObjectType != ExtRepositoryObjectType.FILE) {
			throw new UnsupportedOperationException(
				"Copying non-file external repository objects is not " +
					"supported");
		}

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = getIDfId(iDfSession, extRepositoryFileEntryKey);

			IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(iDfId);

			validateTitle(
				iDfSession, newExtRepositoryFolderKey,
				iDfDocument.getObjectName());

			IDfCopyOperation iDfCopyOperation = _iDfClientX.getCopyOperation();

			IDfId iDfIdDestFolder = getIDfId(
				iDfSession, newExtRepositoryFolderKey);

			iDfCopyOperation.setDestinationFolderId(iDfIdDestFolder);

			iDfCopyOperation.add(iDfDocument);

			if (!iDfCopyOperation.execute()) {
				IDfList iDfList = iDfCopyOperation.getErrors();

				IDfOperationError iDfOperationError =
					(IDfOperationError)iDfList.get(0);

				throw new PrincipalException(iDfOperationError.getMessage());
			}

			IDfList iDfList = iDfCopyOperation.getNewObjects();

			IDfId iDfIdNewFileEntry = (IDfId)iDfList.get(0);

			return toExtRepositoryObject(
				iDfSession, extRepositoryObjectType,
				(IDfDocument)iDfSession.getObject(iDfIdNewFileEntry));
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public void deleteExtRepositoryObject(
			ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			String extRepositoryObjectKey)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = getIDfId(iDfSession, extRepositoryObjectKey);

			if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
				IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(
					iDfId);

				iDfDocument.destroyAllVersions();
			}
			else if (
						extRepositoryObjectType ==
							ExtRepositoryObjectType.FOLDER) {

				IDfFolder iDfFolder = (IDfFolder)iDfSession.getObject(iDfId);

				deleteFolder(iDfSession, iDfFolder);
			}
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public String getAuthType() {
		return CompanyConstants.AUTH_TYPE_SN;
	}

	@Override
	public InputStream getContentStream(
		ExtRepositoryFileEntry extRepositoryFileEntry) {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			DocumentumFileEntry documentumFileEntry =
				(DocumentumFileEntry)extRepositoryFileEntry;

			IDfDocument iDfDocument = documentumFileEntry.getIDfDocument();

			return iDfDocument.getContent();
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public InputStream getContentStream(
		ExtRepositoryFileVersion extRepositoryFileVersion) {

		try {
			DocumentumFileVersion documentumFileVersion =
				(DocumentumFileVersion)extRepositoryFileVersion;

			IDfDocument iDfDocument = documentumFileVersion.getIDfDocument();

			return iDfDocument.getContent();
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
	}

	@Override
	public ExtRepositoryFileVersion getExtRepositoryFileVersion(
			ExtRepositoryFileEntry extRepositoryFileEntry, final String version)
		throws NoSuchFileVersionException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			DocumentumFileEntry documentumFileEntry =
				(DocumentumFileEntry)extRepositoryFileEntry;

			IDfDocument iDfDocument = documentumFileEntry.getIDfDocument();

			Map<String, IDfDocument> iDfDocumentVersions =
				getIDfDocumentVersions(iDfSession, iDfDocument);

			IDfDocument iDfDocumentVersion = iDfDocumentVersions.get(version);

			if (iDfDocumentVersion == null) {
				throw new NoSuchFileVersionException(version);
			}

			return new DocumentumFileVersion(iDfDocumentVersion, iDfDocument);
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
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
		ExtRepositoryFileEntry extRepositoryFileEntry) {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			DocumentumFileEntry documentumFileEntry =
				(DocumentumFileEntry)extRepositoryFileEntry;

			IDfDocument iDfDocument = documentumFileEntry.getIDfDocument();

			Map<String, IDfDocument> iDfDocumentVersionsMap =
				getIDfDocumentVersions(iDfSession, iDfDocument);

			Set<IDfDocument> iDfDocumentVersionsSet = new HashSet<>(
				iDfDocumentVersionsMap.values());

			List<IDfDocument> iDfDocumentVersions = new ArrayList<>(
				iDfDocumentVersionsSet);

			Collections.sort(
				iDfDocumentVersions, _iDfDocumentVersionsComparator);

			List<ExtRepositoryFileVersion> extRepositoryFileVersions =
				new ArrayList<>();

			for (IDfDocument iDfDocumentVersion : iDfDocumentVersions) {
				extRepositoryFileVersions.add(
					new DocumentumFileVersion(iDfDocumentVersion, iDfDocument));
			}

			return extRepositoryFileVersions;
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryObjectKey)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = getIDfId(iDfSession, extRepositoryObjectKey);

			IDfSysObject iDfSysObject = (IDfSysObject)iDfSession.getObject(
				iDfId);

			if (iDfSysObject == null) {
				if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
					throw new NoSuchFolderException(extRepositoryObjectKey);
				}
				else {
					throw new NoSuchFileEntryException(extRepositoryObjectKey);
				}
			}

			return toExtRepositoryObject(
				iDfSession, extRepositoryObjectType, iDfSysObject);
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFolderKey, String title)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = getIDfId(
				iDfSession, extRepositoryFolderKey,
				ExtRepositoryObjectType.FILE, title);

			if (iDfId == null) {
				throw new NoSuchFileEntryException(
					"No Documentum file entry with {extRepositoryFolderKey=" +
						extRepositoryFolderKey + ", title=" + title + "}");
			}

			IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(iDfId);

			return toExtRepositoryObject(
				iDfSession, extRepositoryObjectType, iDfDocument);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(
			final ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFolderKey)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			DocumentumQuery documentumQuery = new DocumentumQuery(
				_iDfClientX, iDfSession);

			List<IDfSysObject> iDfSysObjects = documentumQuery.getIDfSysObjects(
				extRepositoryFolderKey, extRepositoryObjectType);

			return toExtRepositoryObjects(
				iDfSession, extRepositoryObjectType, iDfSysObjects);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public int getExtRepositoryObjectsCount(
			ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			String extRepositoryFolderKey)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			DocumentumQuery documentumQuery = new DocumentumQuery(
				_iDfClientX, iDfSession);

			return documentumQuery.getCount(
				extRepositoryFolderKey, extRepositoryObjectType);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public ExtRepositoryFolder getExtRepositoryParentFolder(
			ExtRepositoryObject extRepositoryObject)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = null;

			if (extRepositoryObject instanceof ExtRepositoryFileEntry) {
				DocumentumFileEntry documentumFileEntry =
					(DocumentumFileEntry)extRepositoryObject;

				IDfDocument iDfDocument = documentumFileEntry.getIDfDocument();

				iDfId = iDfDocument.getFolderId(0);
			}
			else {
				DocumentumFolder documentumFolder =
					(DocumentumFolder)extRepositoryObject;

				IDfFolder iDfFolder = documentumFolder.getIDfFolder();

				iDfId = iDfFolder.getFolderId(0);
			}

			IDfSysObject iDfSysObject = (IDfSysObject)iDfSession.getObject(
				iDfId);

			return toExtRepositoryObject(
				iDfSession, ExtRepositoryObjectType.FOLDER, iDfSysObject);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public String getLiferayLogin(String extRepositoryLogin) {
		return extRepositoryLogin;
	}

	@Override
	public String getRootFolderKey() {
		return _rootFolderKey;
	}

	@Override
	public List<String> getSubfolderKeys(
		String extRepositoryFolderKey, boolean recurse) {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			List<String> extRepositoryFolderKeys = new ArrayList<String>();

			getSubfolderKeys(
				iDfSession, extRepositoryFolderKey, extRepositoryFolderKeys,
				recurse);

			return extRepositoryFolderKeys;
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
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
		throws InvalidRepositoryException {

		_cabinet = typeSettingsProperties.getProperty(_CABINET);

		if (Validator.isNull(_cabinet)) {
			throw new InvalidRepositoryException();
		}

		_repository = typeSettingsProperties.getProperty(_REPOSITORY);

		if (Validator.isNull(_repository)) {
			throw new InvalidRepositoryException();
		}

		_credentialsProvider = credentialsProvider;

		_iDfClientX = new DfClientX();

		_connectionCache = new ConnectionCache<IDfSessionManager>(
			IDfSessionManager.class, getRepositoryId(), this);

		IDfSession idfSession = null;

		try {
			idfSession = getIDfSession();

			IDfFolder idfFolder = idfSession.getFolderByPath(
				StringPool.SLASH + _cabinet);

			IDfId idfFolderId = idfFolder.getObjectId();

			_rootFolderKey = idfFolderId.getId();
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(idfSession);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> T moveExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryObjectKey, String newExtRepositoryFolderKey,
			String newTitle)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = getIDfId(iDfSession, extRepositoryObjectKey);

			IDfSysObject iDfSysObject = (IDfSysObject)iDfSession.getObject(
				iDfId);

			validateTitle(iDfSession, newExtRepositoryFolderKey, newTitle);

			IDfId iDfIdNewFolder = getIDfId(
				iDfSession, newExtRepositoryFolderKey);

			IDfFolder iDfFolderNew = (IDfFolder)iDfSession.getObject(
				iDfIdNewFolder);

			IDfFolder iDfFolderOld = null;

			if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
				iDfFolderOld = (IDfFolder)iDfSession.getObject(
					iDfSysObject.getFolderId(0));
			}
			else if (
						extRepositoryObjectType ==
							ExtRepositoryObjectType.FOLDER) {

				IDfFolder iDfFolder = (IDfFolder)iDfSysObject;

				IDfId oldFolderIDfId = getIDfId(
					iDfSession, iDfFolder.getAncestorId(1));

				iDfFolderOld = (IDfFolder)iDfSession.getObject(oldFolderIDfId);
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported external repository object type " +
						extRepositoryObjectType);
			}

			iDfSysObject.unlink(iDfFolderOld.getFolderPath(0));

			iDfSysObject.link(iDfFolderNew.getFolderPath(0));

			iDfSysObject.setObjectName(newTitle);

			iDfSysObject.save();

			return toExtRepositoryObject(
				iDfSession, extRepositoryObjectType, iDfSysObject);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		finally {
			_releaseSession(iDfSession);
		}
	}

	@Override
	public List<ExtRepositorySearchResult<?>> search(
			SearchContext searchContext, Query query,
			ExtRepositoryQueryMapper extRepositoryQueryMapper)
		throws PortalException {

		IDfSession iDfSession = null;
		IDfCollection iDfCollection = null;

		try {
			iDfSession = getIDfSession();

			DQLQueryBuilder dqlQueryBuilder = new DQLQueryBuilder(
				extRepositoryQueryMapper);

			String searchCountQueryString =
				dqlQueryBuilder.buildSearchCountQueryString(
					searchContext, query);

			IDfQuery iDfQuery = _iDfClientX.getQuery();

			iDfQuery.setDQL(searchCountQueryString);

			if (_log.isDebugEnabled()) {
				_log.debug("Executing query: " + searchCountQueryString);
			}

			iDfCollection = iDfQuery.execute(
				iDfSession, IDfQuery.DF_READ_QUERY);

			long total = 0;

			if (iDfCollection.next()) {
				total = iDfCollection.getLong("num_hits");
			}

			iDfCollection.close();

			int start = searchContext.getStart();
			int end = searchContext.getEnd();

			if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS)) {
				start = 0;
				end = (int)total;
			}
			else if (end > total) {
				end = (int)total;
			}

			List<ExtRepositorySearchResult<?>> extRepositorySearchResults =
				new ArrayList<>();

			if (total > 0) {
				String searchSelectQueryString =
					dqlQueryBuilder.buildSearchSelectQueryString(
						searchContext, query);

				iDfQuery.setDQL(searchSelectQueryString);

				if (_log.isDebugEnabled()) {
					_log.debug("Executing query: " + searchSelectQueryString);
				}

				iDfCollection = iDfQuery.execute(
					iDfSession, IDfQuery.DF_READ_QUERY);

				for (int i = 0; i < start && i < total; i++) {
					iDfCollection.next();
				}

				for (int i = start; (i < end) && iDfCollection.next(); i++) {
					IDfId iDfId = iDfCollection.getId(Constants.R_OBJECT_ID);

					IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(
						iDfId);

					if (_log.isTraceEnabled()) {
						_log.trace(iDfDocument.dump());
					}

					ExtRepositoryFileEntry extRepositoryFileEntry =
						toExtRepositoryObject(
							iDfSession, ExtRepositoryObjectType.FILE,
							iDfDocument);

					extRepositorySearchResults.add(
						new ExtRepositorySearchResult<ExtRepositoryObject>(
							extRepositoryFileEntry, 1, StringPool.BLANK));
				}
			}

			return extRepositorySearchResults;
		}
		catch (DfException de) {
			processException(de);

			throw new SearchException(de);
		}
		finally {
			_close(iDfCollection);

			_releaseSession(iDfSession);
		}
	}

	@Override
	public ExtRepositoryFileEntry updateExtRepositoryFileEntry(
			String extRepositoryFileEntryKey, String mimeType,
			InputStream inputStream)
		throws PortalException {

		IDfSession iDfSession = null;

		File file = null;

		try {
			iDfSession = getIDfSession();

			IDfId iDfId = getIDfId(iDfSession, extRepositoryFileEntryKey);

			IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(iDfId);

			Map<String, IDfDocument> iDfDocumentVersions =
				getIDfDocumentVersions(iDfSession, iDfDocument);

			IDfDocument idfDocumentLastVersion = getIDfDocumentLastVersion(
				iDfDocumentVersions);

			String title = idfDocumentLastVersion.getObjectName();

			StringBundler sb = new StringBundler(5);

			sb.append(SystemProperties.get(SystemProperties.TMP_DIR));
			sb.append("/liferay/documentum/");
			sb.append(PwdGenerator.getPassword());
			sb.append(StringPool.UNDERLINE);
			sb.append(title);

			String fileName = sb.toString();

			file = new File(fileName);

			FileUtil.write(file, inputStream);

			String contentType = getContentType(iDfSession, mimeType, title);

			if (Validator.isNull(contentType)) {
				throw new FileExtensionException(
					"Unsupported file type " + title);
			}

			idfDocumentLastVersion.setContentType(contentType);

			idfDocumentLastVersion.setFile(fileName);

			if (Validator.isNull(idfDocumentLastVersion.getLockOwner())) {
				idfDocumentLastVersion.save();
			}
			else {
				idfDocumentLastVersion.saveLock();
			}

			return toExtRepositoryObject(
				iDfSession, ExtRepositoryObjectType.FILE, iDfDocument);
		}
		catch (DfException de) {
			processException(de);

			throw new RepositoryException(de);
		}
		catch (IOException ioe) {
			throw new RepositoryException(ioe);
		}
		finally {
			_releaseSession(iDfSession);

			if ((file != null) && file.exists()) {
				file.delete();
			}
		}
	}

	protected void deleteFolder(IDfSession iDfSession, IDfFolder iDfFolder)
		throws DfException {

		IDfCollection iDfCollection = iDfFolder.getContents(null);

		try {
			while (iDfCollection.next()) {
				IDfId idfId = getIDfId(
					iDfSession, iDfCollection.getString(Constants.R_OBJECT_ID));

				IDfSysObject iDfSysObject = (IDfSysObject)iDfSession.getObject(
					idfId);

				if (iDfSysObject instanceof IDfFolder) {
					deleteFolder(iDfSession, (IDfFolder)iDfSysObject);
				}
				else {
					iDfSysObject.destroyAllVersions();
				}
			}
		}
		finally {
			_close(iDfCollection);
		}

		iDfFolder.destroyAllVersions();
	}

	protected String getContentType(
			IDfSession iDfSession, String mimeType, String title)
		throws DfException {

		if (Validator.isNull(mimeType)) {
			mimeType = MimeTypesUtil.getContentType(title);
		}

		if (Validator.isNull(mimeType)) {
			return null;
		}

		if (mimeType.indexOf(StringPool.SEMICOLON) != -1) {
			mimeType = mimeType.substring(
				0, mimeType.indexOf(StringPool.SEMICOLON));
		}

		IDfQuery iDfQuery = _iDfClientX.getQuery();

		iDfQuery.setDQL(
			"SELECT name FROM dm_format WHERE mime_type LIKE '%" + mimeType +
				"%'");

		IDfCollection iDfCollection = iDfQuery.execute(
			iDfSession, IDfQuery.DF_READ_QUERY);

		try {
			if (iDfCollection.next()) {
				return iDfCollection.getString("name");
			}
		}
		finally {
			_close(iDfCollection);
		}

		return null;
	}

	protected IDfDocument getIDfDocument(
			IDfSession iDfSession, IDfCollection iDfCollection)
		throws DfException {

		String objectId = iDfCollection.getString("r_object_id");

		IDfId iDfId = getIDfId(iDfSession, objectId);

		return (IDfDocument)iDfSession.getObject(iDfId);
	}

	protected IDfDocument getIDfDocumentLastVersion(
		Map<String, IDfDocument> iDfDocumentVersions) {

		IDfDocument iDfDocumentLastVersion = iDfDocumentVersions.get(
			Constants.VERSION_LABEL_CURRENT);

		if (iDfDocumentVersions.containsKey(Constants.VERSION_LABEL_PWC)) {
			iDfDocumentLastVersion = iDfDocumentVersions.get(
				Constants.VERSION_LABEL_PWC);
		}

		return iDfDocumentLastVersion;
	}

	protected Map<String, IDfDocument> getIDfDocumentVersions(
			IDfSession iDfSession, final IDfDocument iDfDocument)
		throws DfException {

		Map<String, IDfDocument> iDfDocumentVersions = new HashMap<>();

		IDfCollection iDfCollection = iDfDocument.getVersions(null);

		try {
			while (iDfCollection.next()) {
				IDfDocument iDfDocumentVersion = getIDfDocument(
					iDfSession, iDfCollection);

				int versionLabelCount =
					iDfDocumentVersion.getVersionLabelCount();

				for (int i = 0; i<versionLabelCount; i++) {
					iDfDocumentVersions.put(
						iDfDocumentVersion.getVersionLabel(i),
						iDfDocumentVersion);
				}
			}
		}
		finally {
			_close(iDfCollection);
		}

		return iDfDocumentVersions;
	}

	protected IDfId getIDfId(
			IDfSession iDfSession, String extRepositoryObjectKey)
		throws DfException {

		return iDfSession.getIdByQualification(
			"dm_sysobject (ALL) where r_object_id = '" +
				extRepositoryObjectKey + "'");
	}

	protected IDfId getIDfId(
			IDfSession iDfSession, String extRepositoryParentFolderKey,
			ExtRepositoryObjectType extRepositoryObjectType, String name)
		throws DfException {

		IDfId idfId = getIDfId(iDfSession, extRepositoryParentFolderKey);

		StringBundler sb = new StringBundler(7);

		sb.append("SELECT r_object_id FROM ");

		if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
			sb.append(Constants.DM_DOCUMENT);
		}
		else if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
			sb.append(Constants.DM_FOLDER);
		}
		else {
			throw new IllegalArgumentException(
				"Invalid external repository object type " +
					extRepositoryObjectType);
		}

		sb.append(" WHERE object_name = '");
		sb.append(name);
		sb.append("' AND FOLDER (ID('");
		sb.append(idfId.getId());
		sb.append("'))");

		IDfQuery iDfQuery = _iDfClientX.getQuery();

		iDfQuery.setDQL(sb.toString());

		IDfCollection iDfCollection = iDfQuery.execute(
			iDfSession, IDfQuery.DF_READ_QUERY);

		try {
			if (iDfCollection.next()) {
				return getIDfId(
					iDfSession, iDfCollection.getString(Constants.R_OBJECT_ID));
			}
		}
		finally {
			_close(iDfCollection);
		}

		return null;
	}

	protected IDfSession getIDfSession() throws DfServiceException {
		IDfSessionManager iDfSessionManager = getIDfSessionManager();

		return iDfSessionManager.getSession(_repository);
	}

	protected IDfSessionManager getIDfSessionManager() {
		return _connectionCache.getConnection();
	}

	protected void getSubfolderKeys(
			IDfSession iDfSession, String extRepositoryFolderKey,
			List<String> extRepositoryFolderKeys, boolean recurse)
		throws DfException {

		DocumentumQuery documentumQuery = new DocumentumQuery(
			_iDfClientX, iDfSession);

		List<IDfSysObject> iDfSysObjects = documentumQuery.getIDfSysObjects(
			extRepositoryFolderKey, ExtRepositoryObjectType.FOLDER);

		for (IDfSysObject iDfSysObject : iDfSysObjects) {
			IDfId iDfId = iDfSysObject.getObjectId();

			String id = iDfId.getId();

			extRepositoryFolderKeys.add(id);

			if (recurse) {
				getSubfolderKeys(iDfSession, id, extRepositoryFolderKeys, true);
			}
		}
	}

	protected void processException(DfException de) throws PortalException {
		if (de instanceof DfAuthenticationException) {
			throw new PrincipalException(
				"Unable to login with user " + _credentialsProvider.getLogin(),
				de);
		}
	}

	protected <T extends ExtRepositoryObject> T toExtRepositoryObject(
			IDfSession iDfSession,
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			IDfSysObject iDfSysObject)
		throws DfException {

		if (iDfSysObject instanceof IDfDocument) {
			if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
				throw new IllegalArgumentException(
					"Invalid external repository object type " +
						extRepositoryObjectType + " for Documentum object " +
						iDfSysObject);
			}

			IDfDocument iDfDocument = (IDfDocument)iDfSysObject;

			Map<String, IDfDocument> iDfDocumentVersions =
				getIDfDocumentVersions(iDfSession, iDfDocument);

			IDfDocument iDfDocumentLastVersion = getIDfDocumentLastVersion(
				iDfDocumentVersions);

			return (T)new DocumentumFileEntry(
				iDfDocumentVersions.get("1.0"), iDfDocumentLastVersion);
		}
		else if (iDfSysObject instanceof IDfFolder) {
			if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
				throw new IllegalArgumentException(
					"Invalid external repository object type " +
						extRepositoryObjectType + " for Documentum object " +
						iDfSysObject);
			}

			IDfFolder iDfFolder = (IDfFolder)iDfSysObject;

			IDfId iDfId = iDfFolder.getObjectId();

			boolean root = false;

			if (_rootFolderKey.equals(iDfId.getId())) {
				root = true;
			}

			return (T)new DocumentumFolder(iDfFolder, root);
		}
		else {
			throw new RepositoryException(
				"Unsupported object type " + iDfSysObject);
		}
	}

	protected <T extends ExtRepositoryObject> List<T> toExtRepositoryObjects(
			IDfSession iDfSession,
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			List<IDfSysObject> iDfSysObjects)
		throws DfException {

		List<T> extRepositoryObjects = new ArrayList<>();

		for (IDfSysObject iDfSysObject : iDfSysObjects) {
			extRepositoryObjects.add(
				toExtRepositoryObject(
					iDfSession, extRepositoryObjectType, iDfSysObject));
		}

		return extRepositoryObjects;
	}

	protected void validateTitle(
			IDfSession iDfSession, String extRepositoryParentFolderKey,
			String title)
		throws DfException, PortalException {

		IDfId iDfId = getIDfId(
			iDfSession, extRepositoryParentFolderKey,
			ExtRepositoryObjectType.FILE, title);

		if (iDfId != null) {
			throw new DuplicateFileException(title);
		}

		iDfId = getIDfId(
			iDfSession, extRepositoryParentFolderKey,
			ExtRepositoryObjectType.FOLDER, title);

		if (iDfId != null) {
			throw new DuplicateFolderNameException(title);
		}
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

	private void _releaseSession(IDfSession iDfSession) {
		if (iDfSession != null) {
			IDfSessionManager iDfSessionManager = getIDfSessionManager();

			iDfSessionManager.release(iDfSession);
		}
	}

	private static final String _CABINET = "CABINET";

	private static final String _CONFIGURATION_DFC = "CONFIGURATION_DFC";

	private static final String _REPOSITORY = "REPOSITORY";

	private static final String[] _SUPPORTED_CONFIGURATIONS = {
		_CONFIGURATION_DFC
	};

	private static final String[][] _SUPPORTED_PARAMETERS = {
		new String[] {_REPOSITORY, _CABINET}
	};

	private static Log _log = LogFactoryUtil.getLog(DocumentumRepository.class);

	private String _cabinet;
	private ConnectionCache<IDfSessionManager> _connectionCache;
	private CredentialsProvider _credentialsProvider;
	private IDfClientX _iDfClientX;
	private Comparator<IDfDocument> _iDfDocumentVersionsComparator =
		new IDfDocumentVersionsComparator();
	private String _repository;
	private String _rootFolderKey;

}