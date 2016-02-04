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

package com.liferay.knowledgebase.admin.importer;

import com.liferay.knowledgebase.admin.importer.util.KBArticleMarkdownConverter;
import com.liferay.knowledgebase.exception.KBArticleImportException;
import com.liferay.knowledgebase.model.KBArticle;
import com.liferay.knowledgebase.model.KBArticleConstants;
import com.liferay.knowledgebase.model.KBFolderConstants;
import com.liferay.knowledgebase.service.KBArticleLocalServiceUtil;
import com.liferay.knowledgebase.util.PortletPropsValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipReaderFactoryUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author James Hinkey
 * @author Sergio González
 * @author Jesse Rao
 */
public class KBArticleImporter {

	public int processZipFile(
			long userId, long groupId, long parentKBFolderId,
			boolean prioritizeByNumericalPrefix, InputStream inputStream,
			ServiceContext serviceContext)
		throws PortalException {

		if (inputStream == null) {
			throw new KBArticleImportException("Input stream is null");
		}

		try {
			ZipReader zipReader = ZipReaderFactoryUtil.getZipReader(
				inputStream);

			_validateFileEntryNames(zipReader);

			Map<String, String> metadata = getMetadata(zipReader);

			return processKBArticleFiles(
				userId, groupId, parentKBFolderId, prioritizeByNumericalPrefix,
				zipReader, metadata, serviceContext);
		}
		catch (IOException ioe) {
			throw new KBArticleImportException(ioe);
		}
	}

	protected KBArticle addKBArticleMarkdown(
			long userId, long groupId, long parentKBFolderId,
			long parentResourceClassNameId, long parentResourcePrimaryKey,
			String markdown, String fileEntryName, ZipReader zipReader,
			Map<String, String> metadata,
			PrioritizationStrategy prioritizationStrategy,
			ServiceContext serviceContext)
		throws KBArticleImportException {

		if (Validator.isNull(markdown)) {
			throw new KBArticleImportException(
				"Markdown is null for file entry " + fileEntryName);
		}

		KBArticleMarkdownConverter kbArticleMarkdownConverter =
			new KBArticleMarkdownConverter(markdown, fileEntryName, metadata);

		String urlTitle = kbArticleMarkdownConverter.getUrlTitle();

		KBArticle kbArticle =
			KBArticleLocalServiceUtil.fetchKBArticleByUrlTitle(
				groupId, parentKBFolderId, urlTitle);

		boolean newKBArticle = false;

		if (kbArticle == null) {
			newKBArticle = true;
		}

		try {
			if (kbArticle == null) {
				int workflowAction = serviceContext.getWorkflowAction();

				serviceContext.setWorkflowAction(
					WorkflowConstants.ACTION_SAVE_DRAFT);

				kbArticle = KBArticleLocalServiceUtil.addKBArticle(
					userId, parentResourceClassNameId, parentResourcePrimaryKey,
					kbArticleMarkdownConverter.getTitle(), urlTitle, markdown,
					null, kbArticleMarkdownConverter.getSourceURL(), null, null,
					serviceContext);

				serviceContext.setWorkflowAction(workflowAction);
			}
		}
		catch (Exception e) {
			StringBundler sb = new StringBundler(4);

			sb.append("Unable to add basic KB article for file entry ");
			sb.append(fileEntryName);
			sb.append(": ");
			sb.append(e.getLocalizedMessage());

			throw new KBArticleImportException(sb.toString(), e);
		}

		try {
			String html =
				kbArticleMarkdownConverter.processAttachmentsReferences(
					userId, kbArticle, zipReader,
					new HashMap<String, FileEntry>());

			kbArticle = KBArticleLocalServiceUtil.updateKBArticle(
				userId, kbArticle.getResourcePrimKey(),
				kbArticleMarkdownConverter.getTitle(), html,
				kbArticle.getDescription(),
				kbArticleMarkdownConverter.getSourceURL(), null, null, null,
				serviceContext);

			if (newKBArticle) {
				prioritizationStrategy.addKBArticle(kbArticle, fileEntryName);
			}
			else {
				prioritizationStrategy.updateKBArticle(
					kbArticle, fileEntryName);
			}

			return kbArticle;
		}
		catch (Exception e) {
			StringBundler sb = new StringBundler(4);

			sb.append("Unable to update KB article for file entry ");
			sb.append(fileEntryName);
			sb.append(": ");
			sb.append(e.getLocalizedMessage());

			throw new KBArticleImportException(sb.toString(), e);
		}
	}

	protected Map<String, List<String>> getFolderNameFileEntryNamesMap(
			ZipReader zipReader)
		throws KBArticleImportException {

		Map<String, List<String>> folderNameFileEntryNamesMap = new TreeMap<>();

		for (String zipEntry : _getEntries(zipReader)) {
			String extension = FileUtil.getExtension(zipEntry);

			if (!ArrayUtil.contains(
					PortletPropsValues.MARKDOWN_IMPORTER_ARTICLE_EXTENSIONS,
					StringPool.PERIOD.concat(extension))) {

				continue;
			}

			String folderName = StringPool.SLASH;

			if (zipEntry.indexOf(CharPool.SLASH) != -1) {
				folderName = zipEntry.substring(
					0, zipEntry.lastIndexOf(StringPool.SLASH));
			}

			List<String> fileEntryNames = folderNameFileEntryNamesMap.get(
				folderName);

			if (fileEntryNames == null) {
				fileEntryNames = new ArrayList<>();
			}

			fileEntryNames.add(zipEntry);

			folderNameFileEntryNamesMap.put(folderName, fileEntryNames);
		}

		return folderNameFileEntryNamesMap;
	}

	protected Map<String, String> getMetadata(ZipReader zipReader)
		throws KBArticleImportException {

		InputStream inputStream = null;

		try {
			inputStream = zipReader.getEntryAsInputStream(".METADATA");

			if (inputStream == null) {
				return Collections.emptyMap();
			}

			Properties properties = new Properties();

			properties.load(inputStream);

			Map<String, String> metadata = new HashMap<>(properties.size());

			for (Object key : properties.keySet()) {
				Object value = properties.get(key);

				if (value != null) {
					metadata.put(key.toString(), value.toString());
				}
			}

			return metadata;
		}
		catch (IOException ioe) {
			throw new KBArticleImportException(ioe);
		}
		finally {
			StreamUtil.cleanUp(inputStream);
		}
	}

	protected int processKBArticleFiles(
			long userId, long groupId, long parentKBFolderId,
			boolean prioritizeByNumericalPrefix, ZipReader zipReader,
			Map<String, String> metadata, ServiceContext serviceContext)
		throws KBArticleImportException, PortalException {

		int importedKBArticlesCount = 0;

		PrioritizationStrategy prioritizationStrategy =
			PrioritizationStrategy.create(
				groupId, parentKBFolderId, prioritizeByNumericalPrefix);

		Map<String, List<String>> folderFileEntriesMap =
			getFolderNameFileEntryNamesMap(zipReader);

		Set<String> folders = folderFileEntriesMap.keySet();

		// Map intro files to their folders

		Map<String, String> folderIntroFileMap = new TreeMap<>();

		for (String folder : folders) {
			List<String> fileEntries = folderFileEntriesMap.get(folder);

			for (String fileEntry : fileEntries) {
				if (fileEntry.endsWith(
						PortletPropsValues.MARKDOWN_IMPORTER_ARTICLE_INTRO)) {

					folderIntroFileMap.put(folder, fileEntry);

					break;
				}
			}
		}

		// Map ancestor intro files to folders that have no intro files

		for (String folder : folders) {
			String introFile = folderIntroFileMap.get(folder);

			if (Validator.isNull(introFile)) {
				List<String> paths = _extractPaths(folder);

				ListIterator<String> li = paths.listIterator(paths.size());
				while (li.hasPrevious()) {
					String prevFolder = li.previous();

					String prevIntroFile = folderIntroFileMap.get(prevFolder);

					if (Validator.isNotNull(prevIntroFile)) {
						folderIntroFileMap.put(prevFolder, prevIntroFile);

						break;
					}
				}

				if (Validator.isNull(folderIntroFileMap.get(folder))) {
					folderIntroFileMap.put(folder, StringPool.BLANK);
				}
			}
		}

		// Add a KB article for each intro file

		Map<String, KBArticle> introFileKBArticleMap = new HashMap<>();

		for (String folder : folders) {
			long parentResourceClassNameId = PortalUtil.getClassNameId(
				KBFolderConstants.getClassName());
			long parentResourcePrimaryKey = parentKBFolderId;

			long sectionResourceClassNameId = parentResourceClassNameId;
			long sectionResourcePrimaryKey = parentResourcePrimaryKey;

			String introFile = folderIntroFileMap.get(folder);

			if (Validator.isNotNull(introFile)) {

				// Check for parent intro file

				List<String> paths = _extractPaths(folder);

				ListIterator<String> li = paths.listIterator(paths.size());
				while (li.hasPrevious()) {
					String prevFolder = li.previous();

					String parentIntroFile = folderIntroFileMap.get(prevFolder);

					if (Validator.isNotNull(parentIntroFile)) {
						KBArticle parentIntroKBArticle =
							introFileKBArticleMap.get(parentIntroFile);

						sectionResourceClassNameId = PortalUtil.getClassNameId(
							KBArticleConstants.getClassName());
						sectionResourcePrimaryKey =
							parentIntroKBArticle.getResourcePrimKey();

						break;
					}
				}

				KBArticle introKBArticle = introFileKBArticleMap.get(introFile);

				if (Validator.isNull(introKBArticle)) {
					introKBArticle = addKBArticleMarkdown(
						userId, groupId, parentKBFolderId,
						sectionResourceClassNameId, sectionResourcePrimaryKey,
						zipReader.getEntryAsString(introFile), introFile,
						zipReader, metadata, prioritizationStrategy,
						serviceContext);

					importedKBArticlesCount++;

					introFileKBArticleMap.put(introFile, introKBArticle);
				}
			}
		}

		// Add non-intro files as KB articles

		for (String folder : folders) {
			long parentResourceClassNameId = PortalUtil.getClassNameId(
				KBFolderConstants.getClassName());
			long parentResourcePrimaryKey = parentKBFolderId;

			long sectionResourceClassNameId = parentResourceClassNameId;
			long sectionResourcePrimaryKey = parentResourcePrimaryKey;

			// Lookup section intro article for folder

			String introFile = folderIntroFileMap.get(folder);

			if (Validator.isNotNull(introFile)) {
				KBArticle introKBArticle = introFileKBArticleMap.get(introFile);

				sectionResourceClassNameId = PortalUtil.getClassNameId(
					KBArticleConstants.getClassName());
				sectionResourcePrimaryKey = introKBArticle.getResourcePrimKey();
			}

			List<String> fileEntries = folderFileEntriesMap.get(folder);

			for (String fileEntry : fileEntries) {
				if (!fileEntry.endsWith(
						PortletPropsValues.MARKDOWN_IMPORTER_ARTICLE_INTRO)) {

					String markdown = zipReader.getEntryAsString(fileEntry);

					if (Validator.isNull(markdown)) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Missing Markdown in file entry " + fileEntry);
						}
					}

					addKBArticleMarkdown(
						userId, groupId, parentKBFolderId,
						sectionResourceClassNameId, sectionResourcePrimaryKey,
						markdown, fileEntry, zipReader, metadata,
						prioritizationStrategy, serviceContext);

					importedKBArticlesCount++;
				}
			}
		}

		prioritizationStrategy.prioritizeKBArticles();

		return importedKBArticlesCount;
	}

	private List<String> _extractPaths(String folder) {
		List<String> paths = new ArrayList<>();

		int length = folder.length();

		for (int from = 0; from < length;) {
			int index = folder.indexOf('/', from);

			if (index == -1) {
				break;
			}
			else {
				String path = folder.substring(0, index);
				paths.add(path);
			}

			from = index + 1;
		}

		return paths;
	}

	private List<String> _getEntries(ZipReader zipReader)
		throws KBArticleImportException {

		List<String> entries = zipReader.getEntries();

		if (entries == null) {
			throw new KBArticleImportException(
				"The uploaded file is not a ZIP archive or it is corrupted");
		}

		return entries;
	}

	private void _validateFileEntryNames(ZipReader zipReader)
		throws KBArticleImportException {

		Map<String, List<String>> folderFileEntriesMap =
			getFolderNameFileEntryNamesMap(zipReader);

		Set<String> folders = folderFileEntriesMap.keySet();

		for (String folder : folders) {
			List<String> fileEntryNames = folderFileEntriesMap.get(folder);

			String sectionFileEntryName = null;

			for (String fileEntryName : fileEntryNames) {
				if (fileEntryName.endsWith(
						PortletPropsValues.MARKDOWN_IMPORTER_ARTICLE_INTRO)) {

					if (Validator.isNull(sectionFileEntryName)) {
						sectionFileEntryName = fileEntryName;
					}
					else {
						StringBundler sb = new StringBundler(6);
						sb.append("Multiple files with section designator ");
						sb.append(
							PortletPropsValues.MARKDOWN_IMPORTER_ARTICLE_INTRO);
						sb.append(": ");
						sb.append(sectionFileEntryName);
						sb.append(", ");
						sb.append(fileEntryName);

						throw new KBArticleImportException(sb.toString());
					}
				}
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(KBArticleImporter.class);

}