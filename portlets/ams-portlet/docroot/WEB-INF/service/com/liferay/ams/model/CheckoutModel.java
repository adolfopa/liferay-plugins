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

package com.liferay.ams.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.AuditedModel;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the Checkout service. Represents a row in the &quot;AMS_Checkout&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.ams.model.impl.CheckoutModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.ams.model.impl.CheckoutImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Checkout
 * @see com.liferay.ams.model.impl.CheckoutImpl
 * @see com.liferay.ams.model.impl.CheckoutModelImpl
 * @generated
 */
public interface CheckoutModel extends AuditedModel, BaseModel<Checkout> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a checkout model instance should use the {@link Checkout} interface instead.
	 */

	/**
	 * Returns the primary key of this checkout.
	 *
	 * @return the primary key of this checkout
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this checkout.
	 *
	 * @param primaryKey the primary key of this checkout
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the checkout ID of this checkout.
	 *
	 * @return the checkout ID of this checkout
	 */
	public long getCheckoutId();

	/**
	 * Sets the checkout ID of this checkout.
	 *
	 * @param checkoutId the checkout ID of this checkout
	 */
	public void setCheckoutId(long checkoutId);

	/**
	 * Returns the company ID of this checkout.
	 *
	 * @return the company ID of this checkout
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this checkout.
	 *
	 * @param companyId the company ID of this checkout
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this checkout.
	 *
	 * @return the user ID of this checkout
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this checkout.
	 *
	 * @param userId the user ID of this checkout
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this checkout.
	 *
	 * @return the user uuid of this checkout
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this checkout.
	 *
	 * @param userUuid the user uuid of this checkout
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this checkout.
	 *
	 * @return the user name of this checkout
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this checkout.
	 *
	 * @param userName the user name of this checkout
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this checkout.
	 *
	 * @return the create date of this checkout
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this checkout.
	 *
	 * @param createDate the create date of this checkout
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this checkout.
	 *
	 * @return the modified date of this checkout
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this checkout.
	 *
	 * @param modifiedDate the modified date of this checkout
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the asset ID of this checkout.
	 *
	 * @return the asset ID of this checkout
	 */
	public long getAssetId();

	/**
	 * Sets the asset ID of this checkout.
	 *
	 * @param assetId the asset ID of this checkout
	 */
	public void setAssetId(long assetId);

	/**
	 * Returns the check out date of this checkout.
	 *
	 * @return the check out date of this checkout
	 */
	public Date getCheckOutDate();

	/**
	 * Sets the check out date of this checkout.
	 *
	 * @param checkOutDate the check out date of this checkout
	 */
	public void setCheckOutDate(Date checkOutDate);

	/**
	 * Returns the expected check in date of this checkout.
	 *
	 * @return the expected check in date of this checkout
	 */
	public Date getExpectedCheckInDate();

	/**
	 * Sets the expected check in date of this checkout.
	 *
	 * @param expectedCheckInDate the expected check in date of this checkout
	 */
	public void setExpectedCheckInDate(Date expectedCheckInDate);

	/**
	 * Returns the actual check in date of this checkout.
	 *
	 * @return the actual check in date of this checkout
	 */
	public Date getActualCheckInDate();

	/**
	 * Sets the actual check in date of this checkout.
	 *
	 * @param actualCheckInDate the actual check in date of this checkout
	 */
	public void setActualCheckInDate(Date actualCheckInDate);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(Checkout checkout);

	@Override
	public int hashCode();

	@Override
	public CacheModel<Checkout> toCacheModel();

	@Override
	public Checkout toEscapedModel();

	@Override
	public Checkout toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}