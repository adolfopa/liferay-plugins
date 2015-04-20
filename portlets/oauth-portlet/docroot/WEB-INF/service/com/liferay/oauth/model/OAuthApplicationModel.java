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

package com.liferay.oauth.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.AuditedModel;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the OAuthApplication service. Represents a row in the &quot;OAuth_OAuthApplication&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.oauth.model.impl.OAuthApplicationModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.oauth.model.impl.OAuthApplicationImpl}.
 * </p>
 *
 * @author Ivica Cardic
 * @see OAuthApplication
 * @see com.liferay.oauth.model.impl.OAuthApplicationImpl
 * @see com.liferay.oauth.model.impl.OAuthApplicationModelImpl
 * @generated
 */
@ProviderType
public interface OAuthApplicationModel extends AuditedModel,
	BaseModel<OAuthApplication> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a o auth application model instance should use the {@link OAuthApplication} interface instead.
	 */

	/**
	 * Returns the primary key of this o auth application.
	 *
	 * @return the primary key of this o auth application
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this o auth application.
	 *
	 * @param primaryKey the primary key of this o auth application
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the o auth application ID of this o auth application.
	 *
	 * @return the o auth application ID of this o auth application
	 */
	public long getOAuthApplicationId();

	/**
	 * Sets the o auth application ID of this o auth application.
	 *
	 * @param oAuthApplicationId the o auth application ID of this o auth application
	 */
	public void setOAuthApplicationId(long oAuthApplicationId);

	/**
	 * Returns the company ID of this o auth application.
	 *
	 * @return the company ID of this o auth application
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this o auth application.
	 *
	 * @param companyId the company ID of this o auth application
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this o auth application.
	 *
	 * @return the user ID of this o auth application
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this o auth application.
	 *
	 * @param userId the user ID of this o auth application
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this o auth application.
	 *
	 * @return the user uuid of this o auth application
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this o auth application.
	 *
	 * @param userUuid the user uuid of this o auth application
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this o auth application.
	 *
	 * @return the user name of this o auth application
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this o auth application.
	 *
	 * @param userName the user name of this o auth application
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this o auth application.
	 *
	 * @return the create date of this o auth application
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this o auth application.
	 *
	 * @param createDate the create date of this o auth application
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this o auth application.
	 *
	 * @return the modified date of this o auth application
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this o auth application.
	 *
	 * @param modifiedDate the modified date of this o auth application
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the name of this o auth application.
	 *
	 * @return the name of this o auth application
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this o auth application.
	 *
	 * @param name the name of this o auth application
	 */
	public void setName(String name);

	/**
	 * Returns the description of this o auth application.
	 *
	 * @return the description of this o auth application
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this o auth application.
	 *
	 * @param description the description of this o auth application
	 */
	public void setDescription(String description);

	/**
	 * Returns the consumer key of this o auth application.
	 *
	 * @return the consumer key of this o auth application
	 */
	@AutoEscape
	public String getConsumerKey();

	/**
	 * Sets the consumer key of this o auth application.
	 *
	 * @param consumerKey the consumer key of this o auth application
	 */
	public void setConsumerKey(String consumerKey);

	/**
	 * Returns the consumer secret of this o auth application.
	 *
	 * @return the consumer secret of this o auth application
	 */
	@AutoEscape
	public String getConsumerSecret();

	/**
	 * Sets the consumer secret of this o auth application.
	 *
	 * @param consumerSecret the consumer secret of this o auth application
	 */
	public void setConsumerSecret(String consumerSecret);

	/**
	 * Returns the access level of this o auth application.
	 *
	 * @return the access level of this o auth application
	 */
	public int getAccessLevel();

	/**
	 * Sets the access level of this o auth application.
	 *
	 * @param accessLevel the access level of this o auth application
	 */
	public void setAccessLevel(int accessLevel);

	/**
	 * Returns the logo ID of this o auth application.
	 *
	 * @return the logo ID of this o auth application
	 */
	public long getLogoId();

	/**
	 * Sets the logo ID of this o auth application.
	 *
	 * @param logoId the logo ID of this o auth application
	 */
	public void setLogoId(long logoId);

	/**
	 * Returns the shareable access token of this o auth application.
	 *
	 * @return the shareable access token of this o auth application
	 */
	public boolean getShareableAccessToken();

	/**
	 * Returns <code>true</code> if this o auth application is shareable access token.
	 *
	 * @return <code>true</code> if this o auth application is shareable access token; <code>false</code> otherwise
	 */
	public boolean isShareableAccessToken();

	/**
	 * Sets whether this o auth application is shareable access token.
	 *
	 * @param shareableAccessToken the shareable access token of this o auth application
	 */
	public void setShareableAccessToken(boolean shareableAccessToken);

	/**
	 * Returns the callback u r i of this o auth application.
	 *
	 * @return the callback u r i of this o auth application
	 */
	@AutoEscape
	public String getCallbackURI();

	/**
	 * Sets the callback u r i of this o auth application.
	 *
	 * @param callbackURI the callback u r i of this o auth application
	 */
	public void setCallbackURI(String callbackURI);

	/**
	 * Returns the website u r l of this o auth application.
	 *
	 * @return the website u r l of this o auth application
	 */
	@AutoEscape
	public String getWebsiteURL();

	/**
	 * Sets the website u r l of this o auth application.
	 *
	 * @param websiteURL the website u r l of this o auth application
	 */
	public void setWebsiteURL(String websiteURL);

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
	public int compareTo(
		com.liferay.oauth.model.OAuthApplication oAuthApplication);

	@Override
	public int hashCode();

	@Override
	public CacheModel<com.liferay.oauth.model.OAuthApplication> toCacheModel();

	@Override
	public com.liferay.oauth.model.OAuthApplication toEscapedModel();

	@Override
	public com.liferay.oauth.model.OAuthApplication toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}