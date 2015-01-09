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
import com.liferay.documentum.repository.model.DocumentumObject;
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
			final String extRepositoryParentFolderKey, final String mimeType,
			final String title, final String description,
			final String changeLog, final InputStream inputStream)
		throws PortalException {

		return run(new DocumentumAction<ExtRepositoryFileEntry>() {

			@Override
			public ExtRepositoryFileEntry run(IDfSession iDfSession)
				throws DfException, PortalException {

				validateTitle(iDfSession, extRepositoryParentFolderKey, title);

				IDfDocument iDfDocument = (IDfDocument)iDfSession.newObject(
					Constants.DM_DOCUMENT);

				String contentType = getContentType(
					iDfSession, mimeType, title);

				if (Validator.isNull(contentType)) {
					throw new FileExtensionException(
						"Unsupported file type " + title);
				}

				iDfDocument.setContentType(contentType);

				iDfDocument.setLogEntry(changeLog);
				iDfDocument.setObjectName(title);
				iDfDocument.setTitle(description);

				IDfFolder iDfFolderParent = getIDfSysObject(
					IDfFolder.class, iDfSession, extRepositoryParentFolderKey);

				iDfDocument.link(iDfFolderParent.getFolderPath(0));

				StringBundler sb = new StringBundler(5);

				sb.append(SystemProperties.get(SystemProperties.TMP_DIR));
				sb.append("/liferay/documentum/");
				sb.append(PwdGenerator.getPassword());
				sb.append(StringPool.UNDERLINE);
				sb.append(title);

				String fileName = sb.toString();

				File file = new File(fileName);

				try {
					FileUtil.write(file, inputStream);

					iDfDocument.setFile(fileName);

					iDfDocument.save();
				}
				catch (IOException ioe) {
					throw new RepositoryException(
						"Cannot update external repository file entry " +
							title,
						ioe);
				}
				finally {
					file.delete();
				}

				return toExtRepositoryObject(
					iDfSession, ExtRepositoryObjectType.FILE, iDfDocument);
			}
		});
	}

	@Override
	public ExtRepositoryFolder addExtRepositoryFolder(
			final String extRepositoryParentFolderKey, final String name,
			final String description)
		throws PortalException {

		return run(new DocumentumAction<ExtRepositoryFolder>() {

			@Override
			public ExtRepositoryFolder run(IDfSession iDfSession)
				throws DfException, PortalException {

				validateTitle(iDfSession, extRepositoryParentFolderKey, name);

				IDfFolder iDfFolder = (IDfFolder)iDfSession.newObject(
					Constants.DM_FOLDER);

				iDfFolder.setObjectName(name);
				iDfFolder.setTitle(description);

				IDfFolder iDfFolderParent = getIDfSysObject(
					IDfFolder.class, iDfSession, extRepositoryParentFolderKey);

				iDfFolder.link(iDfFolderParent.getFolderPath(0));

				iDfFolder.save();

				return toExtRepositoryObject(
					iDfSession, ExtRepositoryObjectType.FOLDER, iDfFolder);
			}
		});
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
			final String extRepositoryFileEntryKey)
		throws PortalException {

		return run(new DocumentumAction<ExtRepositoryFileVersion>() {
		
			@Override
			public ExtRepositoryFileVersion run(IDfSession iDfSession)
				throws DfException {

				IDfDocument iDfDocument = getIDfSysObject(
					IDfDocument.class, iDfSession, extRepositoryFileEntryKey);

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
		});
	}

	@Override
	public void checkInExtRepositoryFileEntry(
			final String extRepositoryFileEntryKey, boolean createMajorVersion,
			String changeLog)
		throws PortalException {

		run(new DocumentumAction<Void>() {
		
			@Override
			public Void run(IDfSession iDfSession) throws DfException {
				IDfDocument iDfDocument = getIDfSysObject(
					IDfDocument.class, iDfSession, extRepositoryFileEntryKey);

				IDfDocument iDfDocumentLastVersion = getIDfDocumentLastVersion(
					iDfSession, iDfDocument);

				iDfDocumentLastVersion.unmark(Constants.VERSION_LABEL_PWC);

				iDfDocumentLastVersion.mark(Constants.VERSION_LABEL_CURRENT);

				iDfDocumentLastVersion.save();

				return null;
			}
		});
	}

	@Override
	public ExtRepositoryFileEntry checkOutExtRepositoryFileEntry(
			final String extRepositoryFileEntryKey)
		throws PortalException {

		return run(new DocumentumAction<ExtRepositoryFileEntry>() {
			
			@Override
			public ExtRepositoryFileEntry run(IDfSession iDfSession)
				throws DfException {

				IDfDocument iDfDocument = getIDfSysObject(
					IDfDocument.class, iDfSession, extRepositoryFileEntryKey);

				Map<String, IDfDocument> iDfDocumentVersions =
					getIDfDocumentVersions(iDfSession, iDfDocument);

				if (!iDfDocumentVersions.containsKey(
						Constants.VERSION_LABEL_PWC)) {

					IDfDocument iDfDocumentLastVersion =
						getIDfDocumentLastVersion(iDfDocumentVersions);

					String versionLabel =
						iDfDocumentLastVersion.getImplicitVersionLabel();

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
		});
	}

	@Override
	public <T extends ExtRepositoryObject> T copyExtRepositoryObject(
			final ExtRepositoryObjectType<T> extRepositoryObjectType,
			final String extRepositoryFileEntryKey,
			final String newExtRepositoryFolderKey, String newTitle)
		throws PortalException {

		return run(new DocumentumAction<T>() {

			@Override
			public T run(IDfSession iDfSession)
				throws DfException, PortalException {

				if (extRepositoryObjectType != ExtRepositoryObjectType.FILE) {
					throw new UnsupportedOperationException(
						"Copying non-file external repository objects is not " +
							"supported");
				}

				IDfDocument iDfDocument = getIDfSysObject(
					IDfDocument.class, iDfSession, extRepositoryFileEntryKey);

				validateTitle(
					iDfSession, newExtRepositoryFolderKey,
					iDfDocument.getObjectName());

				IDfCopyOperation iDfCopyOperation =
					_iDfClientX.getCopyOperation();

				iDfCopyOperation.setDestinationFolderId(
					getIDfId(iDfSession, newExtRepositoryFolderKey));

				iDfCopyOperation.add(iDfDocument);

				if (!iDfCopyOperation.execute()) {
					IDfList iDfList = iDfCopyOperation.getErrors();

					IDfOperationError iDfOperationError =
						(IDfOperationError)iDfList.get(0);

					throw new PrincipalException(
						iDfOperationError.getMessage());
				}

				IDfList iDfList = iDfCopyOperation.getNewObjects();

				return toExtRepositoryObject(
					iDfSession, extRepositoryObjectType,
					(IDfDocument)iDfSession.getObject((IDfId)iDfList.get(0)));
			}
		});
	}

	@Override
	public void deleteExtRepositoryObject(
			final ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			final String extRepositoryObjectKey)
		throws PortalException {

		run(new DocumentumAction<Void>() {
		
			@Override
			public Void run(IDfSession iDfSession) throws DfException {
				IDfId iDfId = getIDfId(iDfSession, extRepositoryObjectKey);

				if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
					IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(
						iDfId);

					iDfDocument.destroyAllVersions();
				}
				else if (
							extRepositoryObjectType ==
						ExtRepositoryObjectType.FOLDER) {

					IDfFolder iDfFolder = (IDfFolder)iDfSession.getObject(
						iDfId);

					deleteFolder(iDfSession, iDfFolder);
				}

				return null;
			}
		});
	}

	@Override
	public String getAuthType() {
		return CompanyConstants.AUTH_TYPE_SN;
	}

	@Override
	public InputStream getContentStream(
			final ExtRepositoryFileEntry extRepositoryFileEntry)
		throws PortalException {

		return run(new DocumentumAction<InputStream>() {
		
			@Override
			public InputStream run(IDfSession iDfSession) throws DfException {
				IDfDocument iDfDocument = getIDfSysObject(
					IDfDocument.class, extRepositoryFileEntry);

				return iDfDocument.getContent();
			}
		});
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
			final ExtRepositoryFileEntry extRepositoryFileEntry,
			final String version)
		throws PortalException {

		return run(new DocumentumAction<ExtRepositoryFileVersion>() {
		
			@Override
			public ExtRepositoryFileVersion run(IDfSession iDfSession)
				throws DfException, PortalException {

				IDfDocument iDfDocument = getIDfSysObject(
					IDfDocument.class, extRepositoryFileEntry);

				Map<String, IDfDocument> iDfDocumentVersions =
					getIDfDocumentVersions(iDfSession, iDfDocument);

				IDfDocument iDfDocumentVersion = iDfDocumentVersions.get(
					version);

				if (iDfDocumentVersion == null) {
					throw new NoSuchFileVersionException(version);
				}

				return new DocumentumFileVersion(
					iDfDocumentVersion, iDfDocument);
			}
		});
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
			final ExtRepositoryFileEntry extRepositoryFileEntry)
		throws PortalException {

		return run(new DocumentumAction<List<ExtRepositoryFileVersion>>() {
		
			@Override
			public List<ExtRepositoryFileVersion> run(IDfSession iDfSession)
				throws DfException {

				IDfDocument iDfDocument = getIDfSysObject(
					IDfDocument.class, extRepositoryFileEntry);

				Map<String, IDfDocument> iDfDocumentVersionsMap =
					getIDfDocumentVersions(iDfSession, iDfDocument);

				Set<IDfDocument> iDfDocumentVersionsSet = new HashSet<>(
					iDfDocumentVersionsMap.values());

				List<ExtRepositoryFileVersion> extRepositoryFileVersions =
					new ArrayList<>();

				for (IDfDocument iDfDocumentVersion : iDfDocumentVersionsSet) {
					extRepositoryFileVersions.add(
						new DocumentumFileVersion(
							iDfDocumentVersion, iDfDocument));
				}

				Collections.sort(
					extRepositoryFileVersions,
					_extRepositoryFileVersionsComparator);

				return extRepositoryFileVersions;
			}
		});
	}

	@Override
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			final ExtRepositoryObjectType<T> extRepositoryObjectType,
			final String extRepositoryObjectKey)
		throws PortalException {

		return run(new DocumentumAction<T>() {
		
			@Override
			public T run(IDfSession iDfSession)
				throws DfException, PortalException {

				IDfSysObject iDfSysObject = getIDfSysObject(
					IDfSysObject.class, iDfSession, extRepositoryObjectKey);

				if (iDfSysObject == null) {
					if (
							extRepositoryObjectType ==
								ExtRepositoryObjectType.FOLDER) {

						throw new NoSuchFolderException(extRepositoryObjectKey);
					}
					else {
						throw new NoSuchFileEntryException(
							extRepositoryObjectKey);
					}
				}

				return toExtRepositoryObject(
					iDfSession, extRepositoryObjectType, iDfSysObject);
			}
		});
	}

	@Override
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			final ExtRepositoryObjectType<T> extRepositoryObjectType,
			final String extRepositoryFolderKey, final String title)
		throws PortalException {

		return run(new DocumentumAction<T>() {
		
			@Override
			public T run(IDfSession iDfSession)
				throws DfException, PortalException {

				IDfId iDfId = getIDfId(
					iDfSession, extRepositoryFolderKey,
					ExtRepositoryObjectType.FILE, title);

				if (iDfId == null) {
					throw new NoSuchFileEntryException(
						"No Documentum file entry with " +
							"{extRepositoryFolderKey=" +
							extRepositoryFolderKey + ", title=" + title + "}");
				}

				IDfDocument iDfDocument = (IDfDocument)iDfSession.getObject(
					iDfId);

				return toExtRepositoryObject(
					iDfSession, extRepositoryObjectType, iDfDocument);
			}
		});
	}

	@Override
	public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(
			final ExtRepositoryObjectType<T> extRepositoryObjectType,
			final String extRepositoryFolderKey)
		throws PortalException {

		return run(new DocumentumAction<List<T>>() {
		
			@Override
			public List<T> run(IDfSession iDfSession) throws DfException {
				DocumentumQuery documentumQuery = new DocumentumQuery(
					_iDfClientX, iDfSession);

				List<IDfSysObject> iDfSysObjects =
					documentumQuery.getIDfSysObjects(
						extRepositoryFolderKey, extRepositoryObjectType);

				return toExtRepositoryObjects(
					iDfSession, extRepositoryObjectType, iDfSysObjects);
			}
		});
	}

	@Override
	public int getExtRepositoryObjectsCount(
			final ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			final String extRepositoryFolderKey)
		throws PortalException {

		return run(new DocumentumAction<Integer>() {
		
			@Override
			public Integer run(IDfSession iDfSession) throws DfException {
				DocumentumQuery documentumQuery = new DocumentumQuery(
					_iDfClientX, iDfSession);

				return documentumQuery.getCount(
					extRepositoryFolderKey, extRepositoryObjectType);
			}
		});
	}

	@Override
	public ExtRepositoryFolder getExtRepositoryParentFolder(
			final ExtRepositoryObject extRepositoryObject)
		throws PortalException {

		return run(new DocumentumAction<ExtRepositoryFolder>() {
		
			@Override
			public ExtRepositoryFolder run(IDfSession iDfSession)
				throws DfException {

				DocumentumObject documentumObject =
					(DocumentumObject)extRepositoryObject;

				IDfSysObject iDfSysObject = documentumObject.getIDfSysObject();

				IDfFolder iDfFolderParent = getParentFolder(
					iDfSession, iDfSysObject);

				return toExtRepositoryObject(
					iDfSession, ExtRepositoryObjectType.FOLDER,
					iDfFolderParent);
			}
		});
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
			final String extRepositoryFolderKey, final boolean recurse)
		throws PortalException {

		return run(new DocumentumAction<List<String>>() {

			@Override
			public List<String> run(IDfSession iDfSession) throws DfException {
				List<String> extRepositoryFolderKeys = new ArrayList<String>();

				getSubfolderKeys(
					iDfSession, extRepositoryFolderKey, extRepositoryFolderKeys,
					recurse);

				return extRepositoryFolderKeys;
			}
		});
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
		throws InvalidRepositoryException, PrincipalException {

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
		catch (DfAuthenticationException dae) {
			throw new PrincipalException(
				"Unable to login with user " + _credentialsProvider.getLogin(),
				dae);
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
		finally {
			releaseSession(idfSession);
		}
	}

	@Override
	public <T extends ExtRepositoryObject> T moveExtRepositoryObject(
			final ExtRepositoryObjectType<T> extRepositoryObjectType,
			final String extRepositoryObjectKey,
			final String newExtRepositoryFolderKey, final String newTitle)
		throws PortalException {

		return run(new DocumentumAction<T>() {

			@Override
			public T run(IDfSession iDfSession)
				throws DfException, PortalException {

				validateTitle(iDfSession, newExtRepositoryFolderKey, newTitle);

				IDfSysObject iDfSysObject = getIDfSysObject(
					IDfSysObject.class, iDfSession, extRepositoryObjectKey);

				IDfFolder iDfFolderOld = getParentFolder(
					iDfSession, iDfSysObject);

				IDfFolder iDfFolderNew = getIDfSysObject(
					IDfFolder.class, iDfSession, newExtRepositoryFolderKey);

				IDfSysObject iDfSysObjectToMove = iDfSysObject;

				if (iDfSysObject instanceof IDfDocument) {
					IDfDocument iDfDocument = (IDfDocument)iDfSysObject;

					IDfDocument iDfDocumentLastVersion =
						getIDfDocumentLastVersion(iDfSession, iDfDocument);

					iDfSysObjectToMove = iDfDocumentLastVersion;
				}

				iDfSysObjectToMove.unlink(iDfFolderOld.getFolderPath(0));

				iDfSysObjectToMove.link(iDfFolderNew.getFolderPath(0));

				iDfSysObjectToMove.setObjectName(newTitle);

				iDfSysObjectToMove.save();

				return toExtRepositoryObject(
					iDfSession, extRepositoryObjectType, iDfSysObject);
			}
		});
	}

	@Override
	public List<ExtRepositorySearchResult<?>> search(
			final SearchContext searchContext, final Query query,
			final ExtRepositoryQueryMapper extRepositoryQueryMapper)
		throws PortalException {

		return run(new DocumentumAction<List<ExtRepositorySearchResult<?>>>() {
		
			@Override
			public List<ExtRepositorySearchResult<?>> run(IDfSession iDfSession)
				throws DfException, PortalException {

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

				List<ExtRepositorySearchResult<?>>
					extRepositorySearchResults = new ArrayList<>();

				IDfCollection iDfCollection = iDfQuery.execute(
					iDfSession, IDfQuery.DF_READ_QUERY);

				try {
					long total = 0;

					if (iDfCollection.next()) {
						total = iDfCollection.getLong("num_hits");
					}

					iDfCollection.close();

					int start = searchContext.getStart();
					int end = searchContext.getEnd();

					if ((start == QueryUtil.ALL_POS) &&
						(end == QueryUtil.ALL_POS)) {

						start = 0;
						end = (int)total;
					}
					else if (end > total) {
						end = (int)total;
					}

					if (total > 0) {
						String searchSelectQueryString =
							dqlQueryBuilder.buildSearchSelectQueryString(
								searchContext, query);

						iDfQuery.setDQL(searchSelectQueryString);

						if (_log.isDebugEnabled()) {
							_log.debug(
								"Executing query: " + searchSelectQueryString);
						}

						iDfCollection = iDfQuery.execute(
							iDfSession, IDfQuery.DF_READ_QUERY);

						for (int i = 0; i < start && i < total; i++) {
							iDfCollection.next();
						}

						for (int i = start;
									(i < end) && iDfCollection.next(); i++) {

							IDfId iDfId = iDfCollection.getId(
								Constants.R_OBJECT_ID);

							IDfDocument iDfDocument =
								(IDfDocument)iDfSession.getObject(iDfId);

							if (_log.isTraceEnabled()) {
								_log.trace(iDfDocument.dump());
							}

							ExtRepositoryFileEntry extRepositoryFileEntry =
								toExtRepositoryObject(
									iDfSession, ExtRepositoryObjectType.FILE,
									iDfDocument);

							extRepositorySearchResults.add(
								new ExtRepositorySearchResult
									<ExtRepositoryObject>(
										extRepositoryFileEntry, 1,
										StringPool.BLANK));
						}
					}
				}
				finally {
					close(iDfCollection);
				}

				return extRepositorySearchResults;
			}
		});
	}

	@Override
	public ExtRepositoryFileEntry updateExtRepositoryFileEntry(
			final String extRepositoryFileEntryKey, final String mimeType,
			final InputStream inputStream)
		throws PortalException {

		return run(new DocumentumAction<ExtRepositoryFileEntry>() {
		
			@Override
			public ExtRepositoryFileEntry run(IDfSession iDfSession)
				throws DfException, PortalException {

				IDfDocument iDfDocument = getIDfSysObject(
					IDfDocument.class, iDfSession, extRepositoryFileEntryKey);

				IDfDocument idfDocumentLastVersion = getIDfDocumentLastVersion(
					iDfSession, iDfDocument);

				String title = idfDocumentLastVersion.getObjectName();

				StringBundler sb = new StringBundler(5);

				sb.append(SystemProperties.get(SystemProperties.TMP_DIR));
				sb.append("/liferay/documentum/");
				sb.append(PwdGenerator.getPassword());
				sb.append(StringPool.UNDERLINE);
				sb.append(title);

				String fileName = sb.toString();

				File file = new File(fileName);

				try {
					FileUtil.write(file, inputStream);

					String contentType = getContentType(
						iDfSession, mimeType, title);

					if (Validator.isNull(contentType)) {
						throw new FileExtensionException(
							"Unsupported file type " + title);
					}

					idfDocumentLastVersion.setContentType(contentType);

					idfDocumentLastVersion.setFile(fileName);

					if (
							Validator.isNull(
								idfDocumentLastVersion.getLockOwner())) {

						idfDocumentLastVersion.save();
					}
					else {
						idfDocumentLastVersion.saveLock();
					}
				}
				catch (IOException ioe) {
					throw new RepositoryException(ioe);
				}
				finally {
					file.delete();
				}

				return toExtRepositoryObject(
					iDfSession, ExtRepositoryObjectType.FILE, iDfDocument);
			}
		});
	}

	protected void close(IDfCollection iDfCollection) {
		if (iDfCollection != null) {
			try {
				iDfCollection.close();
			}
			catch (DfException de) {
			}
		}
	}

	protected void deleteFolder(IDfSession iDfSession, IDfFolder iDfFolder)
		throws DfException {

		IDfCollection iDfCollection = iDfFolder.getContents(null);

		try {
			while (iDfCollection.next()) {
				IDfSysObject iDfSysObject = getIDfSysObject(
					IDfSysObject.class, iDfSession, iDfCollection);

				if (iDfSysObject instanceof IDfFolder) {
					deleteFolder(iDfSession, (IDfFolder)iDfSysObject);
				}
				else {
					iDfSysObject.destroyAllVersions();
				}
			}
		}
		finally {
			close(iDfCollection);
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
			close(iDfCollection);
		}

		return null;
	}

	protected IDfDocument getIDfDocumentLastVersion(
			IDfSession iDfSession, IDfDocument iDfDocument)
		throws DfException {

		Map<String, IDfDocument> iDfDocumentVersions = getIDfDocumentVersions(
			iDfSession, iDfDocument);

		return getIDfDocumentLastVersion(iDfDocumentVersions);
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
				IDfDocument iDfDocumentVersion = getIDfSysObject(
					IDfDocument.class, iDfSession, iDfCollection);

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
			close(iDfCollection);
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
			close(iDfCollection);
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

	protected <T extends IDfSysObject> T getIDfSysObject(
		Class<T> clazz, ExtRepositoryObject extRepositoryObject) {

		DocumentumObject documentumObject =
			(DocumentumObject)extRepositoryObject;

		return (T)documentumObject.getIDfSysObject();
	}

	protected <T extends IDfSysObject> T getIDfSysObject(
			Class<T> clazz, IDfSession iDfSession, IDfCollection iDfCollection)
		throws DfException {

		IDfId idfId = getIDfId(
			iDfSession, iDfCollection.getString(Constants.R_OBJECT_ID));

		return (T)iDfSession.getObject(idfId);
	}

	protected <T extends IDfSysObject> T getIDfSysObject(
			Class<T> clazz, IDfSession iDfSession,
			String extRepositoryObjectKey)
		throws DfException {

		IDfId iDfId = getIDfId(iDfSession, extRepositoryObjectKey);

		return (T)iDfSession.getObject(iDfId);
	}

	protected IDfFolder getParentFolder(
			IDfSession iDfSession, IDfSysObject iDfSysObject)
		throws DfException {

		IDfId iDfId = iDfSysObject.getFolderId(0);

		return (IDfFolder)iDfSession.getObject(iDfId);
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

	protected void releaseSession(IDfSession iDfSession) {
		if (iDfSession != null) {
			IDfSessionManager iDfSessionManager = getIDfSessionManager();

			iDfSessionManager.release(iDfSession);
		}
	}

	protected <T> T run(DocumentumAction<T> documentumAction)
		throws PortalException {

		IDfSession iDfSession = null;

		try {
			iDfSession = getIDfSession();

			return documentumAction.run(iDfSession);
		}
		catch (DfAuthenticationException dae) {
			throw new PrincipalException(
				"Unable to login with user " + _credentialsProvider.getLogin(),
				dae);
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
		finally {
			releaseSession(iDfSession);
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
	private Comparator<ExtRepositoryFileVersion>
		_extRepositoryFileVersionsComparator =
			new ExtRepositoryFileVersionsComparator();
	private IDfClientX _iDfClientX;
	private String _repository;
	private String _rootFolderKey;

}