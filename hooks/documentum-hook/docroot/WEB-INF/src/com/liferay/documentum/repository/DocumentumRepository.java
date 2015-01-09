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

		return run(
			new DocumentumAction<ExtRepositoryFileEntry>() {

				@Override
				public ExtRepositoryFileEntry run(IDfSession idfSession)
					throws DfException, PortalException {

					validateTitle(
						idfSession, extRepositoryParentFolderKey, title);

					IDfDocument idfDocument = (IDfDocument)idfSession.newObject(
						Constants.DM_DOCUMENT);

					String contentType = getContentType(
						idfSession, mimeType, title);

					if (Validator.isNull(contentType)) {
						throw new FileExtensionException(
							"Unsupported file type " + title);
					}

					idfDocument.setContentType(contentType);

					idfDocument.setLogEntry(changeLog);
					idfDocument.setObjectName(title);
					idfDocument.setTitle(description);

					IDfFolder parentIDfFolder = getIDfSysObject(
						IDfFolder.class, idfSession,
						extRepositoryParentFolderKey);

					idfDocument.link(parentIDfFolder.getFolderPath(0));

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

						idfDocument.setFile(fileName);

						idfDocument.save();
					}
					catch (IOException ioe) {
						throw new RepositoryException(
							"Unable to update external repository file entry " +
								title,
							ioe);
					}
					finally {
						file.delete();
					}

					return toExtRepositoryObject(
						idfSession, ExtRepositoryObjectType.FILE, idfDocument);
				}

			});
	}

	@Override
	public ExtRepositoryFolder addExtRepositoryFolder(
			final String extRepositoryParentFolderKey, final String name,
			final String description)
		throws PortalException {

		return run(
			new DocumentumAction<ExtRepositoryFolder>() {

				@Override
				public ExtRepositoryFolder run(IDfSession idfSession)
					throws DfException, PortalException {

					validateTitle(
						idfSession, extRepositoryParentFolderKey, name);

					IDfFolder idfFolder = (IDfFolder)idfSession.newObject(
						Constants.DM_FOLDER);

					idfFolder.setObjectName(name);
					idfFolder.setTitle(description);

					IDfFolder parentIDfFolder = getIDfSysObject(
						IDfFolder.class, idfSession,
						extRepositoryParentFolderKey);

					idfFolder.link(parentIDfFolder.getFolderPath(0));

					idfFolder.save();

					return toExtRepositoryObject(
						idfSession, ExtRepositoryObjectType.FOLDER, idfFolder);
				}

			});
	}

	@Override
	public IDfSessionManager buildConnection() throws RepositoryException {
		IDfSessionManager idfSessionManager = null;

		try {
			IDfClient idfClient = _idfClientX.getLocalClient();

			idfSessionManager = idfClient.newSessionManager();

			IDfLoginInfo idfLoginInfo = _idfClientX.getLoginInfo();

			String password = _credentialsProvider.getPassword();

			idfLoginInfo.setPassword(password);

			String login = _credentialsProvider.getLogin();

			idfLoginInfo.setUser(login);

			idfSessionManager.setIdentity(_repository, idfLoginInfo);

			return idfSessionManager;
		}
		catch (DfException de) {
			throw new RepositoryException(de);
		}
	}

	@Override
	public ExtRepositoryFileVersion cancelCheckOut(
			final String extRepositoryFileEntryKey)
		throws PortalException {

		return run(
			new DocumentumAction<ExtRepositoryFileVersion>() {

				@Override
				public ExtRepositoryFileVersion run(IDfSession idfSession)
					throws DfException {

					IDfDocument idfDocument = getIDfSysObject(
						IDfDocument.class, idfSession,
						extRepositoryFileEntryKey);

					Map<String, IDfDocument> idfDocumentVersions =
						getIDfDocumentVersions(idfSession, idfDocument);

					IDfDocument latestIDfDocument = getLatestIDfDocument(
						idfDocumentVersions);

					latestIDfDocument.cancelCheckout();

					latestIDfDocument.destroy();

					return new DocumentumFileVersion(
						idfDocumentVersions.get(
							Constants.VERSION_LABEL_CURRENT),
						idfDocument);
				}

			});
	}

	@Override
	public void checkInExtRepositoryFileEntry(
			final String extRepositoryFileEntryKey, boolean createMajorVersion,
			String changeLog)
		throws PortalException {

		run(
			new DocumentumAction<Void>() {

				@Override
				public Void run(IDfSession idfSession) throws DfException {
					IDfDocument idfDocument = getIDfSysObject(
						IDfDocument.class, idfSession,
						extRepositoryFileEntryKey);

					IDfDocument latestIDfDocument = getLatestIDfDocument(
						idfSession, idfDocument);

					latestIDfDocument.unmark(Constants.VERSION_LABEL_PWC);

					latestIDfDocument.mark(Constants.VERSION_LABEL_CURRENT);

					latestIDfDocument.save();

					return null;
				}

			});
	}

	@Override
	public ExtRepositoryFileEntry checkOutExtRepositoryFileEntry(
			final String extRepositoryFileEntryKey)
		throws PortalException {

		return run(
			new DocumentumAction<ExtRepositoryFileEntry>() {

				@Override
				public ExtRepositoryFileEntry run(IDfSession idfSession)
					throws DfException {

					IDfDocument idfDocument = getIDfSysObject(
						IDfDocument.class, idfSession,
						extRepositoryFileEntryKey);

					Map<String, IDfDocument> idfDocumentVersions =
						getIDfDocumentVersions(idfSession, idfDocument);

					if (!idfDocumentVersions.containsKey(
							Constants.VERSION_LABEL_PWC)) {

						IDfDocument latestIDfDocument = getLatestIDfDocument(
							idfDocumentVersions);

						String versionLabel =
							latestIDfDocument.getImplicitVersionLabel();

						latestIDfDocument.checkout();

						DocumentumVersionNumber documentumVersionNumber =
							new DocumentumVersionNumber(versionLabel);

						documentumVersionNumber =
							documentumVersionNumber.increment(false);

						latestIDfDocument.checkin(
							true,
							Constants.VERSION_LABEL_PWC + StringPool.COMMA +
								documentumVersionNumber);
					}

					return toExtRepositoryObject(
						idfSession, ExtRepositoryObjectType.FILE, idfDocument);
				}

			});
	}

	@Override
	public <T extends ExtRepositoryObject> T copyExtRepositoryObject(
			final ExtRepositoryObjectType<T> extRepositoryObjectType,
			final String extRepositoryFileEntryKey,
			final String newExtRepositoryFolderKey, String newTitle)
		throws PortalException {

		return run(
			new DocumentumAction<T>() {

				@Override
				public T run(IDfSession idfSession)
					throws DfException, PortalException {

					if (extRepositoryObjectType !=
							ExtRepositoryObjectType.FILE) {

						throw new UnsupportedOperationException(
							"Copying non-file external repository objects is " +
								"not supported");
					}

					IDfDocument idfDocument = getIDfSysObject(
						IDfDocument.class, idfSession,
						extRepositoryFileEntryKey);

					validateTitle(
						idfSession, newExtRepositoryFolderKey,
						idfDocument.getObjectName());

					IDfCopyOperation idfCopyOperation =
						_idfClientX.getCopyOperation();

					idfCopyOperation.setDestinationFolderId(
						getIDfId(idfSession, newExtRepositoryFolderKey));

					idfCopyOperation.add(idfDocument);

					if (!idfCopyOperation.execute()) {
						IDfList idfList = idfCopyOperation.getErrors();

						IDfOperationError idfOperationError =
							(IDfOperationError)idfList.get(0);

						throw new PrincipalException(
							idfOperationError.getMessage());
					}

					IDfList idfList = idfCopyOperation.getNewObjects();

					return toExtRepositoryObject(
						idfSession, extRepositoryObjectType,
						(IDfDocument)idfSession.getObject(
							(IDfId)idfList.get(0)));
				}

			});
	}

	@Override
	public void deleteExtRepositoryObject(
			final ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			final String extRepositoryObjectKey)
		throws PortalException {

		run(
			new DocumentumAction<Void>() {

				@Override
				public Void run(IDfSession idfSession) throws DfException {
					IDfId idfId = getIDfId(idfSession, extRepositoryObjectKey);

					if (extRepositoryObjectType ==
							ExtRepositoryObjectType.FILE) {

						IDfDocument idfDocument =
							(IDfDocument)idfSession.getObject(idfId);

						idfDocument.destroyAllVersions();
					}
					else if (extRepositoryObjectType ==
								ExtRepositoryObjectType.FOLDER) {

						IDfFolder idfFolder = (IDfFolder)idfSession.getObject(
							idfId);

						deleteFolder(idfSession, idfFolder);
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

		return run(
			new DocumentumAction<InputStream>() {

				@Override
				public InputStream run(IDfSession idfSession)
					throws DfException {

					IDfDocument idfDocument = getIDfSysObject(
						IDfDocument.class, extRepositoryFileEntry);

					return idfDocument.getContent();
				}

			});
	}

	@Override
	public InputStream getContentStream(
		ExtRepositoryFileVersion extRepositoryFileVersion) {

		try {
			DocumentumFileVersion documentumFileVersion =
				(DocumentumFileVersion)extRepositoryFileVersion;

			IDfDocument idfDocument = documentumFileVersion.getIDfDocument();

			return idfDocument.getContent();
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

		return run(
			new DocumentumAction<ExtRepositoryFileVersion>() {

				@Override
				public ExtRepositoryFileVersion run(IDfSession idfSession)
					throws DfException, PortalException {

					IDfDocument idfDocument = getIDfSysObject(
						IDfDocument.class, extRepositoryFileEntry);

					Map<String, IDfDocument> idfDocumentVersions =
						getIDfDocumentVersions(idfSession, idfDocument);

					IDfDocument idfDocumentVersion = idfDocumentVersions.get(
						version);

					if (idfDocumentVersion == null) {
						throw new NoSuchFileVersionException(version);
					}

					return new DocumentumFileVersion(
						idfDocumentVersion, idfDocument);
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

		return run(
			new DocumentumAction<List<ExtRepositoryFileVersion>>() {

				@Override
				public List<ExtRepositoryFileVersion> run(IDfSession idfSession)
					throws DfException {

					IDfDocument idfDocument = getIDfSysObject(
						IDfDocument.class, extRepositoryFileEntry);

					Map<String, IDfDocument> idfDocumentVersionsMap =
						getIDfDocumentVersions(idfSession, idfDocument);

					Set<IDfDocument> idfDocumentVersionsSet = new HashSet<>(
						idfDocumentVersionsMap.values());

					List<ExtRepositoryFileVersion> extRepositoryFileVersions =
						new ArrayList<>();

					for (IDfDocument idfDocumentVersion :
							idfDocumentVersionsSet) {

						extRepositoryFileVersions.add(
							new DocumentumFileVersion(
								idfDocumentVersion, idfDocument));
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

		return run(
			new DocumentumAction<T>() {

				@Override
				public T run(IDfSession idfSession)
					throws DfException, PortalException {

					IDfSysObject idfSysObject = getIDfSysObject(
						IDfSysObject.class, idfSession, extRepositoryObjectKey);

					if (idfSysObject == null) {
						if (extRepositoryObjectType ==
								ExtRepositoryObjectType.FOLDER) {

							throw new NoSuchFolderException(
								extRepositoryObjectKey);
						}
						else {
							throw new NoSuchFileEntryException(
								extRepositoryObjectKey);
						}
					}

					return toExtRepositoryObject(
						idfSession, extRepositoryObjectType, idfSysObject);
				}

			});
	}

	@Override
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			final ExtRepositoryObjectType<T> extRepositoryObjectType,
			final String extRepositoryFolderKey, final String title)
		throws PortalException {

		return run(
			new DocumentumAction<T>() {

				@Override
				public T run(IDfSession idfSession)
					throws DfException, PortalException {

					IDfId idfId = getIDfId(
						idfSession, extRepositoryFolderKey,
						ExtRepositoryObjectType.FILE, title);

					if (idfId == null) {
						throw new NoSuchFileEntryException(
							"No Documentum file entry with " +
								"{extRepositoryFolderKey=" +
									extRepositoryFolderKey + ", title=" +
										title + "}");
					}

					IDfDocument idfDocument = (IDfDocument)idfSession.getObject(
						idfId);

					return toExtRepositoryObject(
						idfSession, extRepositoryObjectType, idfDocument);
				}

			});
	}

	@Override
	public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(
			final ExtRepositoryObjectType<T> extRepositoryObjectType,
			final String extRepositoryFolderKey)
		throws PortalException {

		return run(
			new DocumentumAction<List<T>>() {

				@Override
				public List<T> run(IDfSession idfSession) throws DfException {
					DocumentumQuery documentumQuery = new DocumentumQuery(
						_idfClientX, idfSession);

					List<IDfSysObject> idfSysObjects =
						documentumQuery.getIDfSysObjects(
							extRepositoryFolderKey, extRepositoryObjectType);

					return toExtRepositoryObjects(
						idfSession, extRepositoryObjectType, idfSysObjects);
				}

			});
	}

	@Override
	public int getExtRepositoryObjectsCount(
			final ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			final String extRepositoryFolderKey)
		throws PortalException {

		return run(
			new DocumentumAction<Integer>() {

				@Override
				public Integer run(IDfSession idfSession) throws DfException {
					DocumentumQuery documentumQuery = new DocumentumQuery(
						_idfClientX, idfSession);

					return documentumQuery.getCount(
						extRepositoryFolderKey, extRepositoryObjectType);
				}

			});
	}

	@Override
	public ExtRepositoryFolder getExtRepositoryParentFolder(
			final ExtRepositoryObject extRepositoryObject)
		throws PortalException {

		return run(
			new DocumentumAction<ExtRepositoryFolder>() {

			@Override
			public ExtRepositoryFolder run(IDfSession idfSession)
				throws DfException {

				DocumentumObject documentumObject =
					(DocumentumObject)extRepositoryObject;

				IDfSysObject idfSysObject = documentumObject.getIDfSysObject();

				IDfFolder parentIDfFolder = getParentFolder(
					idfSession, idfSysObject);

				return toExtRepositoryObject(
					idfSession, ExtRepositoryObjectType.FOLDER,
					parentIDfFolder);
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

		return run(
			new DocumentumAction<List<String>>() {

				@Override
				public List<String> run(IDfSession idfSession)
					throws DfException {

					List<String> extRepositoryFolderKeys =
						new ArrayList<String>();

					getSubfolderKeys(
						idfSession, extRepositoryFolderKey,
						extRepositoryFolderKeys, recurse);

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

		_idfClientX = new DfClientX();

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

		return run(
			new DocumentumAction<T>() {

				@Override
				public T run(IDfSession idfSession)
					throws DfException, PortalException {

					validateTitle(
						idfSession, newExtRepositoryFolderKey, newTitle);

					IDfSysObject idfSysObject = getIDfSysObject(
						IDfSysObject.class, idfSession, extRepositoryObjectKey);

					IDfFolder idfFolderOld = getParentFolder(
						idfSession, idfSysObject);

					IDfFolder idfFolderNew = getIDfSysObject(
						IDfFolder.class, idfSession, newExtRepositoryFolderKey);

					IDfSysObject idfSysObjectToMove = idfSysObject;

					if (idfSysObject instanceof IDfDocument) {
						IDfDocument idfDocument = (IDfDocument)idfSysObject;

						IDfDocument latestIDfDocument = getLatestIDfDocument(
							idfSession, idfDocument);

						idfSysObjectToMove = latestIDfDocument;
					}

					idfSysObjectToMove.unlink(idfFolderOld.getFolderPath(0));

					idfSysObjectToMove.link(idfFolderNew.getFolderPath(0));

					idfSysObjectToMove.setObjectName(newTitle);

					idfSysObjectToMove.save();

					return toExtRepositoryObject(
						idfSession, extRepositoryObjectType, idfSysObject);
				}

			});
	}

	@Override
	public List<ExtRepositorySearchResult<?>> search(
			final SearchContext searchContext, final Query query,
			final ExtRepositoryQueryMapper extRepositoryQueryMapper)
		throws PortalException {

		return run(
			new DocumentumAction<List<ExtRepositorySearchResult<?>>>() {

				@Override
				public List<ExtRepositorySearchResult<?>> run(
						IDfSession idfSession)
					throws DfException, PortalException {

					DQLQueryBuilder dqlQueryBuilder = new DQLQueryBuilder(
						extRepositoryQueryMapper);

					String searchCountQueryString =
						dqlQueryBuilder.buildSearchCountQueryString(
							searchContext, query);

					IDfQuery idfQuery = _idfClientX.getQuery();

					idfQuery.setDQL(searchCountQueryString);

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Executing query: " + searchCountQueryString);
					}

					List<ExtRepositorySearchResult<?>>
						extRepositorySearchResults = new ArrayList<>();

					IDfCollection idfCollection = idfQuery.execute(
						idfSession, IDfQuery.DF_READ_QUERY);

					try {
						long total = 0;

						if (idfCollection.next()) {
							total = idfCollection.getLong("num_hits");
						}

						idfCollection.close();

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

							idfQuery.setDQL(searchSelectQueryString);

							if (_log.isDebugEnabled()) {
								_log.debug(
									"Executing query: " +
										searchSelectQueryString);
							}

							idfCollection = idfQuery.execute(
								idfSession, IDfQuery.DF_READ_QUERY);

							for (int i = 0; i < start && i < total; i++) {
								idfCollection.next();
							}

							for (int i = start;
									(i < end) && idfCollection.next(); i++) {

								IDfId idfId = idfCollection.getId(
									Constants.R_OBJECT_ID);

								IDfDocument idfDocument =
									(IDfDocument)idfSession.getObject(idfId);

								if (_log.isTraceEnabled()) {
									_log.trace(idfDocument.dump());
								}

								ExtRepositoryFileEntry extRepositoryFileEntry =
									toExtRepositoryObject(
										idfSession,
										ExtRepositoryObjectType.FILE,
										idfDocument);

								extRepositorySearchResults.add(
									new ExtRepositorySearchResult
										<ExtRepositoryObject>(
											extRepositoryFileEntry, 1,
											StringPool.BLANK));
							}
						}
					}
					finally {
						close(idfCollection);
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

		return run(
			new DocumentumAction<ExtRepositoryFileEntry>() {

				@Override
				public ExtRepositoryFileEntry run(IDfSession idfSession)
					throws DfException, PortalException {

					IDfDocument idfDocument = getIDfSysObject(
						IDfDocument.class, idfSession,
						extRepositoryFileEntryKey);

					IDfDocument latestIDfDocument = getLatestIDfDocument(
						idfSession, idfDocument);

					String title = latestIDfDocument.getObjectName();

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
							idfSession, mimeType, title);

						if (Validator.isNull(contentType)) {
							throw new FileExtensionException(
								"Unsupported file type " + title);
						}

						latestIDfDocument.setContentType(contentType);

						latestIDfDocument.setFile(fileName);

						if (Validator.isNull(
								latestIDfDocument.getLockOwner())) {

							latestIDfDocument.save();
						}
						else {
							latestIDfDocument.saveLock();
						}
					}
					catch (IOException ioe) {
						throw new RepositoryException(ioe);
					}
					finally {
						file.delete();
					}

					return toExtRepositoryObject(
						idfSession, ExtRepositoryObjectType.FILE, idfDocument);
				}

			});
	}

	protected void close(IDfCollection idfCollection) {
		if (idfCollection != null) {
			try {
				idfCollection.close();
			}
			catch (DfException de) {
			}
		}
	}

	protected void deleteFolder(IDfSession idfSession, IDfFolder idfFolder)
		throws DfException {

		IDfCollection idfCollection = idfFolder.getContents(null);

		try {
			while (idfCollection.next()) {
				IDfSysObject idfSysObject = getIDfSysObject(
					IDfSysObject.class, idfSession, idfCollection);

				if (idfSysObject instanceof IDfFolder) {
					deleteFolder(idfSession, (IDfFolder)idfSysObject);
				}
				else {
					idfSysObject.destroyAllVersions();
				}
			}
		}
		finally {
			close(idfCollection);
		}

		idfFolder.destroyAllVersions();
	}

	protected String getContentType(
			IDfSession idfSession, String mimeType, String title)
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

		IDfQuery idfQuery = _idfClientX.getQuery();

		idfQuery.setDQL(
			"SELECT name FROM dm_format WHERE mime_type LIKE '%" + mimeType +
				"%'");

		IDfCollection idfCollection = idfQuery.execute(
			idfSession, IDfQuery.DF_READ_QUERY);

		try {
			if (idfCollection.next()) {
				return idfCollection.getString("name");
			}
		}
		finally {
			close(idfCollection);
		}

		return null;
	}

	protected Map<String, IDfDocument> getIDfDocumentVersions(
			IDfSession idfSession, final IDfDocument idfDocument)
		throws DfException {

		Map<String, IDfDocument> idfDocumentVersions = new HashMap<>();

		IDfCollection idfCollection = idfDocument.getVersions(null);

		try {
			while (idfCollection.next()) {
				IDfDocument idfDocumentVersion = getIDfSysObject(
					IDfDocument.class, idfSession, idfCollection);

				int versionLabelCount =
					idfDocumentVersion.getVersionLabelCount();

				for (int i = 0; i<versionLabelCount; i++) {
					idfDocumentVersions.put(
						idfDocumentVersion.getVersionLabel(i),
						idfDocumentVersion);
				}
			}
		}
		finally {
			close(idfCollection);
		}

		return idfDocumentVersions;
	}

	protected IDfId getIDfId(
			IDfSession idfSession, String extRepositoryObjectKey)
		throws DfException {

		return idfSession.getIdByQualification(
			"dm_sysobject (ALL) where r_object_id = '" +
				extRepositoryObjectKey + "'");
	}

	protected IDfId getIDfId(
			IDfSession idfSession, String extRepositoryParentFolderKey,
			ExtRepositoryObjectType<?> extRepositoryObjectType, String name)
		throws DfException {

		IDfId idfId = getIDfId(idfSession, extRepositoryParentFolderKey);

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

		IDfQuery idfQuery = _idfClientX.getQuery();

		idfQuery.setDQL(sb.toString());

		IDfCollection idfCollection = idfQuery.execute(
			idfSession, IDfQuery.DF_READ_QUERY);

		try {
			if (idfCollection.next()) {
				return getIDfId(
					idfSession, idfCollection.getString(Constants.R_OBJECT_ID));
			}
		}
		finally {
			close(idfCollection);
		}

		return null;
	}

	protected IDfSession getIDfSession() throws DfServiceException {
		IDfSessionManager idfSessionManager = getIDfSessionManager();

		return idfSessionManager.getSession(_repository);
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
			Class<T> clazz, IDfSession idfSession, IDfCollection idfCollection)
		throws DfException {

		IDfId idfId = getIDfId(
			idfSession, idfCollection.getString(Constants.R_OBJECT_ID));

		return (T)idfSession.getObject(idfId);
	}

	protected <T extends IDfSysObject> T getIDfSysObject(
			Class<T> clazz, IDfSession idfSession,
			String extRepositoryObjectKey)
		throws DfException {

		IDfId idfId = getIDfId(idfSession, extRepositoryObjectKey);

		return (T)idfSession.getObject(idfId);
	}

	protected IDfDocument getLatestIDfDocument(
			IDfSession idfSession, IDfDocument idfDocument)
		throws DfException {

		Map<String, IDfDocument> idfDocumentVersions = getIDfDocumentVersions(
			idfSession, idfDocument);

		return getLatestIDfDocument(idfDocumentVersions);
	}

	protected IDfDocument getLatestIDfDocument(
		Map<String, IDfDocument> idfDocumentVersions) {

		IDfDocument latestIDfDocument = idfDocumentVersions.get(
			Constants.VERSION_LABEL_CURRENT);

		if (idfDocumentVersions.containsKey(Constants.VERSION_LABEL_PWC)) {
			latestIDfDocument = idfDocumentVersions.get(
				Constants.VERSION_LABEL_PWC);
		}

		return latestIDfDocument;
	}

	protected IDfFolder getParentFolder(
			IDfSession idfSession, IDfSysObject idfSysObject)
		throws DfException {

		IDfId idfId = idfSysObject.getFolderId(0);

		return (IDfFolder)idfSession.getObject(idfId);
	}

	protected void getSubfolderKeys(
			IDfSession idfSession, String extRepositoryFolderKey,
			List<String> extRepositoryFolderKeys, boolean recurse)
		throws DfException {

		DocumentumQuery documentumQuery = new DocumentumQuery(
			_idfClientX, idfSession);

		List<IDfSysObject> idfSysObjects = documentumQuery.getIDfSysObjects(
			extRepositoryFolderKey, ExtRepositoryObjectType.FOLDER);

		for (IDfSysObject idfSysObject : idfSysObjects) {
			IDfId idfId = idfSysObject.getObjectId();

			String id = idfId.getId();

			extRepositoryFolderKeys.add(id);

			if (recurse) {
				getSubfolderKeys(idfSession, id, extRepositoryFolderKeys, true);
			}
		}
	}

	protected void releaseSession(IDfSession idfSession) {
		if (idfSession != null) {
			IDfSessionManager idfSessionManager = getIDfSessionManager();

			idfSessionManager.release(idfSession);
		}
	}

	protected <T> T run(DocumentumAction<T> documentumAction)
		throws PortalException {

		IDfSession idfSession = null;

		try {
			idfSession = getIDfSession();

			return documentumAction.run(idfSession);
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

	protected <T extends ExtRepositoryObject> T toExtRepositoryObject(
			IDfSession idfSession,
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			IDfSysObject idfSysObject)
		throws DfException {

		if (idfSysObject instanceof IDfDocument) {
			if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
				throw new IllegalArgumentException(
					"Invalid external repository object type " +
						extRepositoryObjectType + " for Documentum object " +
						idfSysObject);
			}

			IDfDocument idfDocument = (IDfDocument)idfSysObject;

			Map<String, IDfDocument> idfDocumentVersions =
				getIDfDocumentVersions(idfSession, idfDocument);

			IDfDocument latestIDfDocument = getLatestIDfDocument(
				idfDocumentVersions);

			return (T)new DocumentumFileEntry(
				idfDocumentVersions.get("1.0"), latestIDfDocument);
		}
		else if (idfSysObject instanceof IDfFolder) {
			if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
				throw new IllegalArgumentException(
					"Invalid external repository object type " +
						extRepositoryObjectType + " for Documentum object " +
						idfSysObject);
			}

			IDfFolder idfFolder = (IDfFolder)idfSysObject;

			IDfId idfId = idfFolder.getObjectId();

			boolean root = false;

			if (_rootFolderKey.equals(idfId.getId())) {
				root = true;
			}

			return (T)new DocumentumFolder(idfFolder, root);
		}
		else {
			throw new RepositoryException(
				"Unsupported object type " + idfSysObject);
		}
	}

	protected <T extends ExtRepositoryObject> List<T> toExtRepositoryObjects(
			IDfSession idfSession,
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			List<IDfSysObject> idfSysObjects)
		throws DfException {

		List<T> extRepositoryObjects = new ArrayList<>();

		for (IDfSysObject idfSysObject : idfSysObjects) {
			extRepositoryObjects.add(
				toExtRepositoryObject(
					idfSession, extRepositoryObjectType, idfSysObject));
		}

		return extRepositoryObjects;
	}

	protected void validateTitle(
			IDfSession idfSession, String extRepositoryParentFolderKey,
			String title)
		throws DfException, PortalException {

		IDfId idfId = getIDfId(
			idfSession, extRepositoryParentFolderKey,
			ExtRepositoryObjectType.FILE, title);

		if (idfId != null) {
			throw new DuplicateFileException(title);
		}

		idfId = getIDfId(
			idfSession, extRepositoryParentFolderKey,
			ExtRepositoryObjectType.FOLDER, title);

		if (idfId != null) {
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
	private IDfClientX _idfClientX;
	private String _repository;
	private String _rootFolderKey;

}