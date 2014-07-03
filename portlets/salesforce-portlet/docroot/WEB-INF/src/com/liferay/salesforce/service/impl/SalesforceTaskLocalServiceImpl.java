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

package com.liferay.salesforce.service.impl;

import com.liferay.portal.kernel.dao.orm.ObjectNotFoundException;
import com.liferay.portal.kernel.messaging.MessageBatch;
import com.liferay.salesforce.query.Condition;
import com.liferay.salesforce.query.ConditionImpl;
import com.liferay.salesforce.query.builder.SelectQuery;
import com.liferay.salesforce.service.base.SalesforceTaskLocalServiceBaseImpl;
import com.liferay.salesforce.util.SalesforceObjectNames;

import java.util.Arrays;
import java.util.List;

/**
 * @author Michael C. Han
 */
public class SalesforceTaskLocalServiceImpl
	extends SalesforceTaskLocalServiceBaseImpl {

	public MessageBatch getTasksByAccountId(
			long companyId, String accountId, List<String> fieldNames)
		throws ObjectNotFoundException {

		Condition condition = ConditionImpl.EQUALS("AccountId", accountId);

		return executeQuery(companyId, fieldNames, condition);
	}

	public MessageBatch getTasksByUserId(
			long companyId, String userId, List<String> fieldNames)
		throws ObjectNotFoundException {

		Condition condition = ConditionImpl.EQUALS("OwnerId", userId);

		return executeQuery(companyId, fieldNames, condition);
	}

	public MessageBatch getTasksByUserName(
			long companyId, String userName, List<String> fieldNames)
		throws ObjectNotFoundException {

		Condition condition = ConditionImpl.EQUALS("Owner.Username", userName);

		return executeQuery(companyId, fieldNames, condition);
	}

	protected MessageBatch executeQuery(
		long companyId, List<String> fieldNames, Condition condition) {

		String query = SelectQuery.build(
			SalesforceObjectNames.TASK, fieldNames, condition,
			_orderByFieldNames);

		return salesforceLocalService.executeQuery(companyId, query);
	}

	private static final List<String> _orderByFieldNames = Arrays.asList(
		"ReminderDateTime");

}