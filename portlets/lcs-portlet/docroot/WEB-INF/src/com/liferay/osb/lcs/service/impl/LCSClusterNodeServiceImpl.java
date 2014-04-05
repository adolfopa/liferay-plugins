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

package com.liferay.osb.lcs.service.impl;

import com.liferay.lcs.service.impl.BaseLCSServiceImpl;
import com.liferay.osb.lcs.DuplicateLCSClusterNodeNameException;
import com.liferay.osb.lcs.RequiredLCSClusterNodeNameException;
import com.liferay.osb.lcs.model.LCSClusterNode;
import com.liferay.osb.lcs.model.impl.LCSClusterNodeImpl;
import com.liferay.osb.lcs.service.LCSClusterNodeService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivica Cardic
 * @author Igor Beslic
 */
public class LCSClusterNodeServiceImpl
	extends BaseLCSServiceImpl implements LCSClusterNodeService {

	@Override
	public LCSClusterNode addLCSClusterNode(
			long lcsClusterEntryId, String name, String description,
			int buildNumber, String key, String location,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		validate(lcsClusterEntryId, name);

		if (Validator.isNull(description)) {
			description = null;
		}

		if (Validator.isNull(location)) {
			location = null;
		}

		try {
			return doGetToObject(
				LCSClusterNodeImpl.class,
				_URL_LCS_CLUSTER_NODE_ADD_LCS_CLUSTER_NODE, "buildNumber",
				String.valueOf(buildNumber), "name", name, "description",
				description, "key", key, "lcsClusterEntryId",
				String.valueOf(lcsClusterEntryId), "location", location);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public LCSClusterNode addLCSClusterNode(
			String siblingKey, String name, String description, String key,
			String location, ServiceContext serviceContext)
		throws SystemException {

		try {
			return doGetToObject(
				LCSClusterNodeImpl.class,
				_URL_LCS_CLUSTER_NODE_ADD_LCS_CLUSTER_NODE, "siblingKey",
				siblingKey, "name", name, "description", description, "key",
				key, "location", location);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public void deleteLCSClusterNode(long lcsClusterNodeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getBeanIdentifier() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<LCSClusterNode> getCorpEntryLCSClusterNodes(long corpEntryId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<LCSClusterNode> getCorpEntryLCSClusterNodes(
		long corpEntryId, boolean details) {

		throw new UnsupportedOperationException();
	}

	@Override
	public List<LCSClusterNode> getLCSClusterEntryLCSClusterNodes(
			long lcsClusterEntryId)
		throws SystemException {

		List<LCSClusterNodeImpl> remoteLCSClusterNodes = null;

		try {
			remoteLCSClusterNodes = doGetToList(
				LCSClusterNodeImpl.class,
				_URL_LCS_CLUSTER_NODE_GET_LCS_CLUSTER_ENTRY_GET_LCS_CLUSTER_NODES,
				"lcsClusterEntryId", String.valueOf(lcsClusterEntryId));
		}
		catch (Exception e) {
			throw new SystemException(e);
		}

		List<LCSClusterNode> localLCSClusterNodes =
			new ArrayList<LCSClusterNode>();

		for (LCSClusterNode lcsClusterNode : remoteLCSClusterNodes) {
			localLCSClusterNodes.add(lcsClusterNode);
		}

		return localLCSClusterNodes;
	}

	@Override
	public List<LCSClusterNode> getLCSClusterEntryLCSClusterNodes(
		long lcsClusterEntryId, boolean details) {

		throw new UnsupportedOperationException();
	}

	@Override
	public LCSClusterNode getLCSClusterNode(long lcsClusterNodeId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public LCSClusterNode getLCSClusterNode(
		long lcsClusterNodeId, boolean details) {

		throw new UnsupportedOperationException();
	}

	@Override
	public LCSClusterNode getLCSClusterNode(String key) throws SystemException {
		try {
			return doGetToObject(
				LCSClusterNodeImpl.class,
				_URL_LCS_CLUSTER_NODE_GET_LCS_CLUSTER_NODE, "key", key);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@Override
	public Object invokeMethod(
			String name, String[] parameterTypes, Object[] arguments)
		throws Throwable {

		throw new UnsupportedOperationException();
	}

	@Override
	public void setBeanIdentifier(String beanIdentifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	public LCSClusterNode updateLCSClusterNode(
		long lcsClusterNodeId, long lcsClusterEntryId, String name,
		String description, String location) {

		throw new UnsupportedOperationException();
	}

	protected void validate(long lcsClusterEntryId, String lcsClusterNodeName)
		throws PortalException, SystemException {

		if (Validator.isNull(lcsClusterNodeName)) {
			throw new RequiredLCSClusterNodeNameException();
		}

		List<LCSClusterNode> lcsClusterNodes =
			getLCSClusterEntryLCSClusterNodes(lcsClusterEntryId);

		for (LCSClusterNode lcsClusterNode : lcsClusterNodes) {
			if (lcsClusterNodeName.equalsIgnoreCase(lcsClusterNode.getName())) {
				throw new DuplicateLCSClusterNodeNameException();
			}
		}
	}

	private static final String _URL_LCS_CLUSTER_NODE =
		"/api/secure/jsonws/osb-lcs-portlet.lcsclusternode";

	private static final String _URL_LCS_CLUSTER_NODE_ADD_LCS_CLUSTER_NODE =
		_URL_LCS_CLUSTER_NODE + "/add-lcs-cluster-node";

	private static final String
		_URL_LCS_CLUSTER_NODE_GET_LCS_CLUSTER_ENTRY_GET_LCS_CLUSTER_NODES =
			_URL_LCS_CLUSTER_NODE +
				"/get-lcs-cluster-entry-lcs-cluster-nodes";

	private static final String _URL_LCS_CLUSTER_NODE_GET_LCS_CLUSTER_NODE =
		_URL_LCS_CLUSTER_NODE + "/get-lcs-cluster-node";

}