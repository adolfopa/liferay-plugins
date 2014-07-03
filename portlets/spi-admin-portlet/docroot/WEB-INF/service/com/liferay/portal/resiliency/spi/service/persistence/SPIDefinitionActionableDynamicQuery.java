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

package com.liferay.portal.resiliency.spi.service.persistence;

import com.liferay.portal.kernel.dao.orm.BaseActionableDynamicQuery;
import com.liferay.portal.resiliency.spi.model.SPIDefinition;
import com.liferay.portal.resiliency.spi.service.SPIDefinitionLocalServiceUtil;

/**
 * @author Michael C. Han
 * @deprecated As of 7.0.0, replaced by {@link SPIDefinitionLocalServiceUtil#getActionableDynamicQuery()}
 * @generated
 */
@Deprecated
public abstract class SPIDefinitionActionableDynamicQuery
	extends BaseActionableDynamicQuery {
	public SPIDefinitionActionableDynamicQuery() {
		setBaseLocalService(SPIDefinitionLocalServiceUtil.getService());
		setClass(SPIDefinition.class);

		setClassLoader(com.liferay.portal.resiliency.spi.service.ClpSerializer.class.getClassLoader());

		setPrimaryKeyPropertyName("spiDefinitionId");
	}
}