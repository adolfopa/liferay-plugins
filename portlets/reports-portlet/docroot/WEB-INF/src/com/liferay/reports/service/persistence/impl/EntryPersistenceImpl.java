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

package com.liferay.reports.service.persistence.impl;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import com.liferay.reports.NoSuchEntryException;
import com.liferay.reports.model.Entry;
import com.liferay.reports.model.impl.EntryImpl;
import com.liferay.reports.model.impl.EntryModelImpl;
import com.liferay.reports.service.persistence.EntryPersistence;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see EntryPersistence
 * @see EntryUtil
 * @generated
 */
public class EntryPersistenceImpl extends BasePersistenceImpl<Entry>
	implements EntryPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link EntryUtil} to access the entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = EntryImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(EntryModelImpl.ENTITY_CACHE_ENABLED,
			EntryModelImpl.FINDER_CACHE_ENABLED, EntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(EntryModelImpl.ENTITY_CACHE_ENABLED,
			EntryModelImpl.FINDER_CACHE_ENABLED, EntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(EntryModelImpl.ENTITY_CACHE_ENABLED,
			EntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public EntryPersistenceImpl() {
		setModelClass(Entry.class);
	}

	/**
	 * Caches the entry in the entity cache if it is enabled.
	 *
	 * @param entry the entry
	 */
	@Override
	public void cacheResult(Entry entry) {
		EntityCacheUtil.putResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
			EntryImpl.class, entry.getPrimaryKey(), entry);

		entry.resetOriginalValues();
	}

	/**
	 * Caches the entries in the entity cache if it is enabled.
	 *
	 * @param entries the entries
	 */
	@Override
	public void cacheResult(List<Entry> entries) {
		for (Entry entry : entries) {
			if (EntityCacheUtil.getResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
						EntryImpl.class, entry.getPrimaryKey()) == null) {
				cacheResult(entry);
			}
			else {
				entry.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all entries.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(EntryImpl.class.getName());
		}

		EntityCacheUtil.clearCache(EntryImpl.class);

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the entry.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Entry entry) {
		EntityCacheUtil.removeResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
			EntryImpl.class, entry.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<Entry> entries) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Entry entry : entries) {
			EntityCacheUtil.removeResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
				EntryImpl.class, entry.getPrimaryKey());
		}
	}

	/**
	 * Creates a new entry with the primary key. Does not add the entry to the database.
	 *
	 * @param entryId the primary key for the new entry
	 * @return the new entry
	 */
	@Override
	public Entry create(long entryId) {
		Entry entry = new EntryImpl();

		entry.setNew(true);
		entry.setPrimaryKey(entryId);

		return entry;
	}

	/**
	 * Removes the entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the entry
	 * @return the entry that was removed
	 * @throws com.liferay.reports.NoSuchEntryException if a entry with the primary key could not be found
	 */
	@Override
	public Entry remove(long entryId) throws NoSuchEntryException {
		return remove((Serializable)entryId);
	}

	/**
	 * Removes the entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the entry
	 * @return the entry that was removed
	 * @throws com.liferay.reports.NoSuchEntryException if a entry with the primary key could not be found
	 */
	@Override
	public Entry remove(Serializable primaryKey) throws NoSuchEntryException {
		Session session = null;

		try {
			session = openSession();

			Entry entry = (Entry)session.get(EntryImpl.class, primaryKey);

			if (entry == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(entry);
		}
		catch (NoSuchEntryException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected Entry removeImpl(Entry entry) {
		entry = toUnwrappedModel(entry);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(entry)) {
				entry = (Entry)session.get(EntryImpl.class,
						entry.getPrimaryKeyObj());
			}

			if (entry != null) {
				session.delete(entry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (entry != null) {
			clearCache(entry);
		}

		return entry;
	}

	@Override
	public Entry updateImpl(com.liferay.reports.model.Entry entry) {
		entry = toUnwrappedModel(entry);

		boolean isNew = entry.isNew();

		Session session = null;

		try {
			session = openSession();

			if (entry.isNew()) {
				session.save(entry);

				entry.setNew(false);
			}
			else {
				session.merge(entry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		EntityCacheUtil.putResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
			EntryImpl.class, entry.getPrimaryKey(), entry, false);

		entry.resetOriginalValues();

		return entry;
	}

	protected Entry toUnwrappedModel(Entry entry) {
		if (entry instanceof EntryImpl) {
			return entry;
		}

		EntryImpl entryImpl = new EntryImpl();

		entryImpl.setNew(entry.isNew());
		entryImpl.setPrimaryKey(entry.getPrimaryKey());

		entryImpl.setEntryId(entry.getEntryId());
		entryImpl.setGroupId(entry.getGroupId());
		entryImpl.setCompanyId(entry.getCompanyId());
		entryImpl.setUserId(entry.getUserId());
		entryImpl.setUserName(entry.getUserName());
		entryImpl.setCreateDate(entry.getCreateDate());
		entryImpl.setModifiedDate(entry.getModifiedDate());
		entryImpl.setDefinitionId(entry.getDefinitionId());
		entryImpl.setFormat(entry.getFormat());
		entryImpl.setScheduleRequest(entry.isScheduleRequest());
		entryImpl.setStartDate(entry.getStartDate());
		entryImpl.setEndDate(entry.getEndDate());
		entryImpl.setRepeating(entry.isRepeating());
		entryImpl.setRecurrence(entry.getRecurrence());
		entryImpl.setEmailNotifications(entry.getEmailNotifications());
		entryImpl.setEmailDelivery(entry.getEmailDelivery());
		entryImpl.setPortletId(entry.getPortletId());
		entryImpl.setPageURL(entry.getPageURL());
		entryImpl.setReportParameters(entry.getReportParameters());
		entryImpl.setStatus(entry.getStatus());
		entryImpl.setErrorMessage(entry.getErrorMessage());

		return entryImpl;
	}

	/**
	 * Returns the entry with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the entry
	 * @return the entry
	 * @throws com.liferay.reports.NoSuchEntryException if a entry with the primary key could not be found
	 */
	@Override
	public Entry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEntryException {
		Entry entry = fetchByPrimaryKey(primaryKey);

		if (entry == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return entry;
	}

	/**
	 * Returns the entry with the primary key or throws a {@link com.liferay.reports.NoSuchEntryException} if it could not be found.
	 *
	 * @param entryId the primary key of the entry
	 * @return the entry
	 * @throws com.liferay.reports.NoSuchEntryException if a entry with the primary key could not be found
	 */
	@Override
	public Entry findByPrimaryKey(long entryId) throws NoSuchEntryException {
		return findByPrimaryKey((Serializable)entryId);
	}

	/**
	 * Returns the entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the entry
	 * @return the entry, or <code>null</code> if a entry with the primary key could not be found
	 */
	@Override
	public Entry fetchByPrimaryKey(Serializable primaryKey) {
		Entry entry = (Entry)EntityCacheUtil.getResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
				EntryImpl.class, primaryKey);

		if (entry == _nullEntry) {
			return null;
		}

		if (entry == null) {
			Session session = null;

			try {
				session = openSession();

				entry = (Entry)session.get(EntryImpl.class, primaryKey);

				if (entry != null) {
					cacheResult(entry);
				}
				else {
					EntityCacheUtil.putResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
						EntryImpl.class, primaryKey, _nullEntry);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
					EntryImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return entry;
	}

	/**
	 * Returns the entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the entry
	 * @return the entry, or <code>null</code> if a entry with the primary key could not be found
	 */
	@Override
	public Entry fetchByPrimaryKey(long entryId) {
		return fetchByPrimaryKey((Serializable)entryId);
	}

	@Override
	public Map<Serializable, Entry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, Entry> map = new HashMap<Serializable, Entry>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			Entry entry = fetchByPrimaryKey(primaryKey);

			if (entry != null) {
				map.put(primaryKey, entry);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Entry entry = (Entry)EntityCacheUtil.getResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
					EntryImpl.class, primaryKey);

			if (entry == null) {
				if (uncachedPrimaryKeys == null) {
					uncachedPrimaryKeys = new HashSet<Serializable>();
				}

				uncachedPrimaryKeys.add(primaryKey);
			}
			else {
				map.put(primaryKey, entry);
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_ENTRY_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append(String.valueOf(primaryKey));

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (Entry entry : (List<Entry>)q.list()) {
				map.put(entry.getPrimaryKeyObj(), entry);

				cacheResult(entry);

				uncachedPrimaryKeys.remove(entry.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				EntityCacheUtil.putResult(EntryModelImpl.ENTITY_CACHE_ENABLED,
					EntryImpl.class, primaryKey, _nullEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the entries.
	 *
	 * @return the entries
	 */
	@Override
	public List<Entry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.reports.model.impl.EntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of entries
	 * @param end the upper bound of the range of entries (not inclusive)
	 * @return the range of entries
	 */
	@Override
	public List<Entry> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.reports.model.impl.EntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of entries
	 * @param end the upper bound of the range of entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of entries
	 */
	@Override
	public List<Entry> findAll(int start, int end,
		OrderByComparator<Entry> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<Entry> list = (List<Entry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_ENTRY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_ENTRY;

				if (pagination) {
					sql = sql.concat(EntryModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Entry>)QueryUtil.list(q, getDialect(), start,
							end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<Entry>)QueryUtil.list(q, getDialect(), start,
							end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (Entry entry : findAll()) {
			remove(entry);
		}
	}

	/**
	 * Returns the number of entries.
	 *
	 * @return the number of entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_ENTRY);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the entry persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		EntityCacheUtil.removeCache(EntryImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_ENTRY = "SELECT entry FROM Entry entry";
	private static final String _SQL_SELECT_ENTRY_WHERE_PKS_IN = "SELECT entry FROM Entry entry WHERE entryId IN (";
	private static final String _SQL_COUNT_ENTRY = "SELECT COUNT(entry) FROM Entry entry";
	private static final String _ORDER_BY_ENTITY_ALIAS = "entry.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Entry exists with the primary key ";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(EntryPersistenceImpl.class);
	private static Entry _nullEntry = new EntryImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Entry> toCacheModel() {
				return _nullEntryCacheModel;
			}
		};

	private static CacheModel<Entry> _nullEntryCacheModel = new CacheModel<Entry>() {
			@Override
			public Entry toEntityModel() {
				return _nullEntry;
			}
		};
}