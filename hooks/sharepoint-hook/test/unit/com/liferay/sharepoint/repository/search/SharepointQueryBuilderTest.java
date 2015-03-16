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

package com.liferay.sharepoint.repository.search;

import static org.mockito.Matchers.any;

import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.search.RepositorySearchQueryBuilderUtil;
import com.liferay.portal.kernel.search.BaseSearchEngine;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.DateFormatFactory;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.model.RepositoryEntry;
import com.liferay.portal.repository.search.RepositorySearchQueryBuilderImpl;
import com.liferay.portal.search.generic.BooleanClauseFactoryImpl;
import com.liferay.portal.search.generic.BooleanQueryFactoryImpl;
import com.liferay.portal.search.generic.TermQueryFactoryImpl;
import com.liferay.portal.search.generic.TermRangeQueryFactoryImpl;
import com.liferay.portal.search.lucene.LuceneHelperUtil;
import com.liferay.portal.service.RepositoryEntryLocalService;
import com.liferay.portal.service.RepositoryEntryLocalServiceUtil;
import com.liferay.portal.util.HtmlImpl;
import com.liferay.portlet.documentlibrary.service.DLAppService;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.repository.external.ExtRepositoryObjectType;
import com.liferay.repository.external.search.ExtRepositoryQueryMapper;
import com.liferay.sharepoint.connector.SharepointConnection;
import com.liferay.sharepoint.connector.SharepointConnectionInfo;
import com.liferay.sharepoint.connector.SharepointObject;
import com.liferay.sharepoint.connector.schema.query.Query;
import com.liferay.sharepoint.connector.schema.query.QueryClause;
import com.liferay.sharepoint.connector.schema.query.QueryField;
import com.liferay.sharepoint.connector.schema.query.QueryOptionsList;
import com.liferay.sharepoint.connector.schema.query.QueryValue;
import com.liferay.sharepoint.connector.schema.query.join.AndJoin;
import com.liferay.sharepoint.connector.schema.query.join.OrJoin;
import com.liferay.sharepoint.connector.schema.query.operator.BeginsWithOperator;
import com.liferay.sharepoint.connector.schema.query.operator.ContainsOperator;
import com.liferay.sharepoint.connector.schema.query.operator.EqOperator;
import com.liferay.sharepoint.connector.schema.query.operator.GeqOperator;
import com.liferay.sharepoint.connector.schema.query.operator.LeqOperator;
import com.liferay.sharepoint.connector.schema.query.operator.NeqOperator;
import com.liferay.sharepoint.connector.schema.query.option.FolderQueryOption;
import com.liferay.sharepoint.repository.SharepointWSRepository;
import com.liferay.sharepoint.repository.model.SharepointWSFolder;

import java.lang.reflect.Field;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.KeywordAnalyzer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Iván Zaera
 */
@PrepareForTest(LuceneHelperUtil.class)
@RunWith(PowerMockRunner.class)
public class SharepointQueryBuilderTest extends PowerMockito {

	@Before
	public void setUp() throws PortalException {
		mockBeanLocator();

		mockDateFormatFactory();

		mockExtRepositoryQueryMapper();

		mockLuceneHelperUtil();

		mockProps();

		mockSharepointExtRepository();

		initHtmlUtil();

		initRepositorySearchQueryBuilderUtil();

		initSearchEngineUtil();
	}

	@After
	public void tearDown() {
		for (Class<?> serviceUtilClass : _serviceUtilClasses) {
			try {
				Field field = serviceUtilClass.getDeclaredField("_service");

				field.setAccessible(true);

				field.set(serviceUtilClass, null);
			}
			catch (Exception e) {
			}
		}
	}

	@Test
	public void testBooleanQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("+test* -test.doc");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new AndJoin(
					new BeginsWithOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test")),
					new NeqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test.doc"))),
				new AndJoin(
					new BeginsWithOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test")),
					new NeqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test.doc")))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testExactFilenameQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("test.jpg");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new EqOperator(
					new QueryField(SharepointField.NAME),
					new QueryValue("test.jpg")),
				new EqOperator(
					new QueryField(SharepointField.MODIFIED_BY),
					new QueryValue("test.jpg"))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testFolderQuery() throws Exception {
		mockFolders(1000);

		SearchContext searchContext = buildSearchContext();

		searchContext.setFolderIds(new long[] {1000});
		searchContext.setKeywords("test");

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setSearchSubfolders(false);

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new AndJoin(
				new EqOperator(
					new QueryField(SharepointField.FOLDER_PATH),
					new QueryValue("Library/Root/1000")),
				new OrJoin(
					new EqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test")),
					new EqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test")))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testFoldersQuery() throws Exception {
		mockFolders(1000, 2000);

		SearchContext searchContext = buildSearchContext();

		searchContext.setFolderIds(new long[] {1000, 2000});
		searchContext.setKeywords("test");

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setSearchSubfolders(false);

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new AndJoin(
				new OrJoin(
					new EqOperator(
						new QueryField(SharepointField.FOLDER_PATH),
						new QueryValue("Library/Root/1000")),
					new EqOperator(
						new QueryField(SharepointField.FOLDER_PATH),
						new QueryValue("Library/Root/2000"))),
				new OrJoin(
					new EqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test")),
					new EqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test")))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testFuzzyQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("test~");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new BeginsWithOperator(
					new QueryField(SharepointField.NAME),
					new QueryValue("test")),
				new BeginsWithOperator(
					new QueryField(SharepointField.MODIFIED_BY),
					new QueryValue("test"))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testMultipleKeywordsQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("test multiple");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new OrJoin(
					new EqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test")),
					new EqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("multiple"))),
				new OrJoin(
					new EqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test")),
					new EqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("multiple")))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testPhraseQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("\"My test document.jpg\"");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new EqOperator(
					new QueryField(SharepointField.NAME),
					new QueryValue("My test document.jpg")),
				new EqOperator(
					new QueryField(SharepointField.MODIFIED_BY),
					new QueryValue("My test document.jpg"))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testPrefixQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("Test*");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new BeginsWithOperator(
					new QueryField(SharepointField.NAME),
					new QueryValue("Test")),
				new BeginsWithOperator(
					new QueryField(SharepointField.MODIFIED_BY),
					new QueryValue("Test"))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testProximityQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("\"test document\"~10");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new EqOperator(
					new QueryField(SharepointField.NAME),
					new QueryValue("test document")),
				new EqOperator(
					new QueryField(SharepointField.MODIFIED_BY),
					new QueryValue("test document"))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testQueryWithConjunction() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("+test +multiple");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new AndJoin(
					new EqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test")),
					new EqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("multiple"))),
				new AndJoin(
					new EqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test")),
					new EqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("multiple")))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testQueryWithNegation() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("test -multiple");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new AndJoin(
					new NeqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("multiple")),
					new EqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test"))),
				new AndJoin(
					new NeqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("multiple")),
					new EqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test")))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testQueryWithNegationPhrase() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("test -\"multiple words\"");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new AndJoin(
					new NeqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("multiple words")),
					new EqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test"))),
				new AndJoin(
					new NeqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("multiple words")),
					new EqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test")))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testRangeQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords(
			"createDate:[20091011000000 TO 20091110235959]");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new AndJoin(
				new GeqOperator(
					new QueryField(SharepointField.CREATE_DATE),
					new QueryValue("2009-10-11 00:00:00")),
				new LeqOperator(
					new QueryField(SharepointField.CREATE_DATE),
					new QueryValue("2009-11-10 23:59:59"))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testSingleKeywordQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("test");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new EqOperator(
					new QueryField(SharepointField.NAME),
					new QueryValue("test")),
				new EqOperator(
					new QueryField(SharepointField.MODIFIED_BY),
					new QueryValue("test"))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testSubfolderQuery() throws Exception {
		mockFolders(1000);

		SearchContext searchContext = buildSearchContext();

		searchContext.setFolderIds(new long[] {1000});
		searchContext.setKeywords("test");

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setSearchSubfolders(true);

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new AndJoin(
				new EqOperator(
					new QueryField(SharepointField.FOLDER_PATH),
					new QueryValue("Library/Root/1000")),
				new OrJoin(
					new EqOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test")),
					new EqOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test")))),
			new QueryOptionsList(new FolderQueryOption("")),
			sharepointQueryBuilder);
	}

	@Test
	public void testWildcardFieldQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("+title:test*.jpg +userName:bar*");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new AndJoin(
				new AndJoin(
					new ContainsOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test")),
					new ContainsOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue(".jpg"))),
				new BeginsWithOperator(
					new QueryField(SharepointField.MODIFIED_BY),
					new QueryValue("bar"))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	@Test
	public void testWildcardQuery() throws Exception {
		SearchContext searchContext = buildSearchContext();

		searchContext.setKeywords("test*.jpg");

		BooleanQuery searchQuery =
			RepositorySearchQueryBuilderUtil.getFullQuery(searchContext);

		SharepointQueryBuilder sharepointQueryBuilder =
			buildSharepointQueryBuilder(searchContext, searchQuery);

		assertQueryEquals(
			new OrJoin(
				new AndJoin(
					new ContainsOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue("test")),
					new ContainsOperator(
						new QueryField(SharepointField.NAME),
						new QueryValue(".jpg"))),
				new AndJoin(
					new ContainsOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue("test")),
					new ContainsOperator(
						new QueryField(SharepointField.MODIFIED_BY),
						new QueryValue(".jpg")))),
			new QueryOptionsList(), sharepointQueryBuilder);
	}

	protected void assertQueryEquals(
		QueryClause expectedQueryClause,
		QueryOptionsList expectedQueryOptionsList,
		SharepointQueryBuilder sharepointQueryBuilder) {

		Query query = sharepointQueryBuilder.getQuery();

		QueryClause queryClause = query.getQueryClause();

		QueryOptionsList queryOptionsList =
			sharepointQueryBuilder.getQueryOptionsList();

		Assert.assertEquals(
			expectedQueryClause.toString(), queryClause.toString());
		Assert.assertEquals(
			expectedQueryOptionsList.toString(), queryOptionsList.toString());
	}

	protected SearchContext buildSearchContext() {
		SearchContext searchContext = new SearchContext();

		searchContext.setSearchEngineId(SearchEngineUtil.GENERIC_ENGINE_ID);

		return searchContext;
	}

	protected SharepointQueryBuilder buildSharepointQueryBuilder(
			SearchContext searchContext, BooleanQuery booleanQuery)
		throws Exception {

		return new SharepointQueryBuilder(
			_sharepointExtRepository, searchContext, booleanQuery,
			_extRepositoryQueryMapper);
	}

	protected <T> T getService(
		Class<?> serviceUtilClass, Class<T> serviceClass) {

		_serviceUtilClasses.add(serviceUtilClass);

		T service = mock(serviceClass);

		when(
			_beanLocator.locate(Mockito.eq(serviceClass.getName()))
		).thenReturn(
			service
		);

		return service;
	}

	protected void initHtmlUtil() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(new HtmlImpl());
	}

	protected void initRepositorySearchQueryBuilderUtil() {
		RepositorySearchQueryBuilderImpl repositorySearchQueryBuilderImpl =
			new RepositorySearchQueryBuilderImpl();

		repositorySearchQueryBuilderImpl.setAnalyzer(new KeywordAnalyzer());

		RepositorySearchQueryBuilderUtil repositorySearchQueryBuilderUtil =
			new RepositorySearchQueryBuilderUtil();

		repositorySearchQueryBuilderUtil.setRepositorySearchQueryBuilder(
			repositorySearchQueryBuilderImpl);
	}

	protected void initSearchEngineUtil() {
		BaseSearchEngine searchEngine = new BaseSearchEngine();

		searchEngine.setBooleanClauseFactory(new BooleanClauseFactoryImpl());
		searchEngine.setBooleanQueryFactory(new BooleanQueryFactoryImpl());
		searchEngine.setLuceneBased(false);
		searchEngine.setTermQueryFactory(new TermQueryFactoryImpl());
		searchEngine.setTermRangeQueryFactory(new TermRangeQueryFactoryImpl());

		SearchEngineUtil.setSearchEngine(
			SearchEngineUtil.GENERIC_ENGINE_ID, searchEngine);
	}

	protected void mockBeanLocator() {
		_beanLocator = mock(BeanLocator.class);

		PortalBeanLocatorUtil.setBeanLocator(_beanLocator);
	}

	protected void mockDateFormatFactory() {
		DateFormatFactory dateFormatFactory = mock(DateFormatFactory.class);

		when(
			dateFormatFactory.getSimpleDateFormat(Matchers.anyString())
		).then(
			new Answer<SimpleDateFormat>() {

				@Override
				public SimpleDateFormat answer(InvocationOnMock invocation)
					throws Throwable {

					String pattern = (String)invocation.getArguments()[0];

					if (pattern == null) {
						pattern = "yyyyMMddHHmmss";
					}

					return new SimpleDateFormat(pattern);
				}

			}
		);

		new DateFormatFactoryUtil().setDateFormatFactory(dateFormatFactory);
	}

	protected void mockExtRepositoryQueryMapper() throws SearchException {
		_extRepositoryQueryMapper = mock(ExtRepositoryQueryMapper.class);

		when(
			_extRepositoryQueryMapper.formatDateParameterValue(
				Matchers.anyString(), Matchers.anyString())
		).thenAnswer(
			new Answer<Date>() {

				@Override
				public Date answer(InvocationOnMock invocationOnMock)
					throws Throwable {

					DateFormat dateFormat =
						DateFormatFactoryUtil.getSimpleDateFormat(
							PropsUtil.get(PropsKeys.INDEX_DATE_FORMAT_PATTERN));

					String value = (String)invocationOnMock.getArguments()[1];

					return dateFormat.parse(value);
				}

			}
		);

		when(
			_extRepositoryQueryMapper.formatParameterValue(
				Matchers.anyString(), Matchers.anyString())
		).thenAnswer(
			new Answer<String>() {

				@Override
				public String answer(InvocationOnMock invocationOnMock)
					throws Throwable {

					return (String)invocationOnMock.getArguments()[1];
				}

			});
	}

	protected void mockFolders(long... folderIds) {
		getService(DLAppServiceUtil.class, DLAppService.class);

		RepositoryEntryLocalService repositoryEntryLocalService = getService(
			RepositoryEntryLocalServiceUtil.class,
			RepositoryEntryLocalService.class);

		for (long folderId : folderIds) {
			RepositoryEntry repositoryEntry = mock(RepositoryEntry.class);

			when(
				repositoryEntry.getMappedId()
			).thenReturn(
				Long.toString(folderId)
			);

			when(
				repositoryEntryLocalService.fetchRepositoryEntry(
					Mockito.eq(folderId))
			).thenReturn(
				repositoryEntry
			);
		}
	}

	protected void mockLuceneHelperUtil() {
		mockStatic(LuceneHelperUtil.class);

		when(
			LuceneHelperUtil.getVersion()
		).thenReturn(
			org.apache.lucene.util.Version.LUCENE_35
		);
	}

	protected void mockProps() {
		Props props = mock(Props.class);

		PropsUtil.setProps(props);

		when(
			props.get(PropsKeys.INDEX_READ_ONLY)
		).thenReturn(
			"true"
		);
	}

	protected void mockSharepointExtRepository() throws PortalException {
		SharepointConnection sharepointConnection = mock(
			SharepointConnection.class);

		when(
			sharepointConnection.getSharepointConnectionInfo()
		).thenReturn(
			new SharepointConnectionInfo(
				SharepointConnection.ServerVersion.SHAREPOINT_2010, "http",
				"host", 80, "", "Library", "Library", "username", "password")
		);

		_sharepointExtRepository = mock(SharepointWSRepository.class);

		when(
			_sharepointExtRepository.getExtRepositoryObject(
				any(ExtRepositoryObjectType.class), Matchers.anyString())
		).thenAnswer(
			new Answer<SharepointWSFolder>() {

				@Override
				public SharepointWSFolder answer(InvocationOnMock invocation)
					throws Throwable {

					long sharepointObjectId = GetterUtil.getLong(
						invocation.getArguments()[1]);
					Set<SharepointObject.Permission> permissions =
						Collections.emptySet();

					return new SharepointWSFolder(
						new SharepointObject(
							"", null, new Date(), true, new Date(),
							"/Root/" + sharepointObjectId, permissions,
							sharepointObjectId, 0, null));
				}

			}
		);

		when(
			_sharepointExtRepository.getSharepointConnection()
		).thenReturn(
			sharepointConnection
		);

		when(
			_sharepointExtRepository.getSharepointLogin(Matchers.anyString())
		).thenAnswer(
			new Answer<String>() {

				@Override
				public String answer(InvocationOnMock invocation)
					throws Throwable {

					String liferayLogin = GetterUtil.getString(
						invocation.getArguments()[0]);

					return liferayLogin;
				}

			}
		);
	}

	protected void resetServices() {
		for (Class<?> serviceUtilClass : _serviceUtilClasses) {
			try {
				Field field = serviceUtilClass.getDeclaredField("_service");

				field.setAccessible(true);

				field.set(serviceUtilClass, null);
			}
			catch (Exception e) {
			}
		}
	}

	private BeanLocator _beanLocator;
	private ExtRepositoryQueryMapper _extRepositoryQueryMapper;
	private List<Class<?>> _serviceUtilClasses = new ArrayList<Class<?>>();
	private SharepointWSRepository _sharepointExtRepository;

}