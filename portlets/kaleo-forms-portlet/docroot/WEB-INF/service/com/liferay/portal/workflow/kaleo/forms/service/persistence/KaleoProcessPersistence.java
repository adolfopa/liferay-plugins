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

package com.liferay.portal.workflow.kaleo.forms.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess;

/**
 * The persistence interface for the kaleo process service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Marcellus Tavares
 * @see com.liferay.portal.workflow.kaleo.forms.service.persistence.impl.KaleoProcessPersistenceImpl
 * @see KaleoProcessUtil
 * @generated
 */
@ProviderType
public interface KaleoProcessPersistence extends BasePersistence<KaleoProcess> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link KaleoProcessUtil} to access the kaleo process persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the kaleo processes where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the matching kaleo processes
	*/
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> findByGroupId(
		long groupId);

	/**
	* Returns a range of all the kaleo processes where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.forms.model.impl.KaleoProcessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of kaleo processes
	* @param end the upper bound of the range of kaleo processes (not inclusive)
	* @return the range of matching kaleo processes
	*/
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> findByGroupId(
		long groupId, int start, int end);

	/**
	* Returns an ordered range of all the kaleo processes where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.forms.model.impl.KaleoProcessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of kaleo processes
	* @param end the upper bound of the range of kaleo processes (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching kaleo processes
	*/
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> findByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> orderByComparator);

	/**
	* Returns the first kaleo process in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching kaleo process
	* @throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException if a matching kaleo process could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess findByGroupId_First(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> orderByComparator)
		throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException;

	/**
	* Returns the first kaleo process in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching kaleo process, or <code>null</code> if a matching kaleo process could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess fetchByGroupId_First(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> orderByComparator);

	/**
	* Returns the last kaleo process in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching kaleo process
	* @throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException if a matching kaleo process could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess findByGroupId_Last(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> orderByComparator)
		throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException;

	/**
	* Returns the last kaleo process in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching kaleo process, or <code>null</code> if a matching kaleo process could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess fetchByGroupId_Last(
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> orderByComparator);

	/**
	* Returns the kaleo processes before and after the current kaleo process in the ordered set where groupId = &#63;.
	*
	* @param kaleoProcessId the primary key of the current kaleo process
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next kaleo process
	* @throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException if a kaleo process with the primary key could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess[] findByGroupId_PrevAndNext(
		long kaleoProcessId, long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> orderByComparator)
		throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException;

	/**
	* Returns all the kaleo processes that the user has permission to view where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the matching kaleo processes that the user has permission to view
	*/
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> filterFindByGroupId(
		long groupId);

	/**
	* Returns a range of all the kaleo processes that the user has permission to view where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.forms.model.impl.KaleoProcessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of kaleo processes
	* @param end the upper bound of the range of kaleo processes (not inclusive)
	* @return the range of matching kaleo processes that the user has permission to view
	*/
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> filterFindByGroupId(
		long groupId, int start, int end);

	/**
	* Returns an ordered range of all the kaleo processes that the user has permissions to view where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.forms.model.impl.KaleoProcessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of kaleo processes
	* @param end the upper bound of the range of kaleo processes (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching kaleo processes that the user has permission to view
	*/
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> filterFindByGroupId(
		long groupId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> orderByComparator);

	/**
	* Returns the kaleo processes before and after the current kaleo process in the ordered set of kaleo processes that the user has permission to view where groupId = &#63;.
	*
	* @param kaleoProcessId the primary key of the current kaleo process
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next kaleo process
	* @throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException if a kaleo process with the primary key could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess[] filterFindByGroupId_PrevAndNext(
		long kaleoProcessId, long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> orderByComparator)
		throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException;

	/**
	* Removes all the kaleo processes where groupId = &#63; from the database.
	*
	* @param groupId the group ID
	*/
	public void removeByGroupId(long groupId);

	/**
	* Returns the number of kaleo processes where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the number of matching kaleo processes
	*/
	public int countByGroupId(long groupId);

	/**
	* Returns the number of kaleo processes that the user has permission to view where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the number of matching kaleo processes that the user has permission to view
	*/
	public int filterCountByGroupId(long groupId);

	/**
	* Returns the kaleo process where DDLRecordSetId = &#63; or throws a {@link com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException} if it could not be found.
	*
	* @param DDLRecordSetId the d d l record set ID
	* @return the matching kaleo process
	* @throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException if a matching kaleo process could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess findByDDLRecordSetId(
		long DDLRecordSetId)
		throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException;

	/**
	* Returns the kaleo process where DDLRecordSetId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param DDLRecordSetId the d d l record set ID
	* @return the matching kaleo process, or <code>null</code> if a matching kaleo process could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess fetchByDDLRecordSetId(
		long DDLRecordSetId);

	/**
	* Returns the kaleo process where DDLRecordSetId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param DDLRecordSetId the d d l record set ID
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching kaleo process, or <code>null</code> if a matching kaleo process could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess fetchByDDLRecordSetId(
		long DDLRecordSetId, boolean retrieveFromCache);

	/**
	* Removes the kaleo process where DDLRecordSetId = &#63; from the database.
	*
	* @param DDLRecordSetId the d d l record set ID
	* @return the kaleo process that was removed
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess removeByDDLRecordSetId(
		long DDLRecordSetId)
		throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException;

	/**
	* Returns the number of kaleo processes where DDLRecordSetId = &#63;.
	*
	* @param DDLRecordSetId the d d l record set ID
	* @return the number of matching kaleo processes
	*/
	public int countByDDLRecordSetId(long DDLRecordSetId);

	/**
	* Caches the kaleo process in the entity cache if it is enabled.
	*
	* @param kaleoProcess the kaleo process
	*/
	public void cacheResult(
		com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess kaleoProcess);

	/**
	* Caches the kaleo processes in the entity cache if it is enabled.
	*
	* @param kaleoProcesses the kaleo processes
	*/
	public void cacheResult(
		java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> kaleoProcesses);

	/**
	* Creates a new kaleo process with the primary key. Does not add the kaleo process to the database.
	*
	* @param kaleoProcessId the primary key for the new kaleo process
	* @return the new kaleo process
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess create(
		long kaleoProcessId);

	/**
	* Removes the kaleo process with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param kaleoProcessId the primary key of the kaleo process
	* @return the kaleo process that was removed
	* @throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException if a kaleo process with the primary key could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess remove(
		long kaleoProcessId)
		throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException;

	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess updateImpl(
		com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess kaleoProcess);

	/**
	* Returns the kaleo process with the primary key or throws a {@link com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException} if it could not be found.
	*
	* @param kaleoProcessId the primary key of the kaleo process
	* @return the kaleo process
	* @throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException if a kaleo process with the primary key could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess findByPrimaryKey(
		long kaleoProcessId)
		throws com.liferay.portal.workflow.kaleo.forms.NoSuchKaleoProcessException;

	/**
	* Returns the kaleo process with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param kaleoProcessId the primary key of the kaleo process
	* @return the kaleo process, or <code>null</code> if a kaleo process with the primary key could not be found
	*/
	public com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess fetchByPrimaryKey(
		long kaleoProcessId);

	@Override
	public java.util.Map<java.io.Serializable, com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the kaleo processes.
	*
	* @return the kaleo processes
	*/
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> findAll();

	/**
	* Returns a range of all the kaleo processes.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.forms.model.impl.KaleoProcessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of kaleo processes
	* @param end the upper bound of the range of kaleo processes (not inclusive)
	* @return the range of kaleo processes
	*/
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> findAll(
		int start, int end);

	/**
	* Returns an ordered range of all the kaleo processes.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portal.workflow.kaleo.forms.model.impl.KaleoProcessModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of kaleo processes
	* @param end the upper bound of the range of kaleo processes (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of kaleo processes
	*/
	public java.util.List<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess> orderByComparator);

	/**
	* Removes all the kaleo processes from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of kaleo processes.
	*
	* @return the number of kaleo processes
	*/
	public int countAll();
}