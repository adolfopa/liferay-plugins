/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.bbb.model.impl;

import com.liferay.bbb.model.BBBParticipant;
import com.liferay.bbb.model.BBBParticipantModel;
import com.liferay.bbb.model.BBBParticipantSoap;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.User;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the BBBParticipant service. Represents a row in the &quot;BBBParticipant&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.liferay.bbb.model.BBBParticipantModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link BBBParticipantImpl}.
 * </p>
 *
 * @author Shinn Lok
 * @see BBBParticipantImpl
 * @see com.liferay.bbb.model.BBBParticipant
 * @see com.liferay.bbb.model.BBBParticipantModel
 * @generated
 */
@JSON(strict = true)
public class BBBParticipantModelImpl extends BaseModelImpl<BBBParticipant>
	implements BBBParticipantModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a b b b participant model instance should use the {@link com.liferay.bbb.model.BBBParticipant} interface instead.
	 */
	public static final String TABLE_NAME = "BBBParticipant";
	public static final Object[][] TABLE_COLUMNS = {
			{ "bbbParticipantId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "bbbMeetingId", Types.BIGINT },
			{ "name", Types.VARCHAR },
			{ "emailAddress", Types.VARCHAR },
			{ "type_", Types.INTEGER },
			{ "status", Types.INTEGER }
		};
	public static final String TABLE_SQL_CREATE = "create table BBBParticipant (bbbParticipantId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,bbbMeetingId LONG,name VARCHAR(75) null,emailAddress VARCHAR(75) null,type_ INTEGER,status INTEGER)";
	public static final String TABLE_SQL_DROP = "drop table BBBParticipant";
	public static final String ORDER_BY_JPQL = " ORDER BY bbbParticipant.bbbParticipantId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY BBBParticipant.bbbParticipantId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.bbb.model.BBBParticipant"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.bbb.model.BBBParticipant"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.bbb.model.BBBParticipant"),
			true);
	public static long BBBMEETINGID_COLUMN_BITMASK = 1L;
	public static long BBBPARTICIPANTID_COLUMN_BITMASK = 2L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static BBBParticipant toModel(BBBParticipantSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		BBBParticipant model = new BBBParticipantImpl();

		model.setBbbParticipantId(soapModel.getBbbParticipantId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setBbbMeetingId(soapModel.getBbbMeetingId());
		model.setName(soapModel.getName());
		model.setEmailAddress(soapModel.getEmailAddress());
		model.setType(soapModel.getType());
		model.setStatus(soapModel.getStatus());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<BBBParticipant> toModels(BBBParticipantSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<BBBParticipant> models = new ArrayList<BBBParticipant>(soapModels.length);

		for (BBBParticipantSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.util.service.ServiceProps.get(
				"lock.expiration.time.com.liferay.bbb.model.BBBParticipant"));

	public BBBParticipantModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _bbbParticipantId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setBbbParticipantId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _bbbParticipantId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return BBBParticipant.class;
	}

	@Override
	public String getModelClassName() {
		return BBBParticipant.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("bbbParticipantId", getBbbParticipantId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("bbbMeetingId", getBbbMeetingId());
		attributes.put("name", getName());
		attributes.put("emailAddress", getEmailAddress());
		attributes.put("type", getType());
		attributes.put("status", getStatus());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long bbbParticipantId = (Long)attributes.get("bbbParticipantId");

		if (bbbParticipantId != null) {
			setBbbParticipantId(bbbParticipantId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long bbbMeetingId = (Long)attributes.get("bbbMeetingId");

		if (bbbMeetingId != null) {
			setBbbMeetingId(bbbMeetingId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String emailAddress = (String)attributes.get("emailAddress");

		if (emailAddress != null) {
			setEmailAddress(emailAddress);
		}

		Integer type = (Integer)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		Integer status = (Integer)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}
	}

	@JSON
	@Override
	public long getBbbParticipantId() {
		return _bbbParticipantId;
	}

	@Override
	public void setBbbParticipantId(long bbbParticipantId) {
		_bbbParticipantId = bbbParticipantId;
	}

	@JSON
	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() throws SystemException {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return StringPool.BLANK;
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return StringPool.BLANK;
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public long getBbbMeetingId() {
		return _bbbMeetingId;
	}

	@Override
	public void setBbbMeetingId(long bbbMeetingId) {
		_columnBitmask |= BBBMEETINGID_COLUMN_BITMASK;

		if (!_setOriginalBbbMeetingId) {
			_setOriginalBbbMeetingId = true;

			_originalBbbMeetingId = _bbbMeetingId;
		}

		_bbbMeetingId = bbbMeetingId;
	}

	public long getOriginalBbbMeetingId() {
		return _originalBbbMeetingId;
	}

	@JSON
	@Override
	public String getName() {
		if (_name == null) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	@JSON
	@Override
	public String getEmailAddress() {
		if (_emailAddress == null) {
			return StringPool.BLANK;
		}
		else {
			return _emailAddress;
		}
	}

	@Override
	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}

	@JSON
	@Override
	public int getType() {
		return _type;
	}

	@Override
	public void setType(int type) {
		_type = type;
	}

	@JSON
	@Override
	public int getStatus() {
		return _status;
	}

	@Override
	public void setStatus(int status) {
		_status = status;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			BBBParticipant.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public BBBParticipant toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (BBBParticipant)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		BBBParticipantImpl bbbParticipantImpl = new BBBParticipantImpl();

		bbbParticipantImpl.setBbbParticipantId(getBbbParticipantId());
		bbbParticipantImpl.setGroupId(getGroupId());
		bbbParticipantImpl.setCompanyId(getCompanyId());
		bbbParticipantImpl.setUserId(getUserId());
		bbbParticipantImpl.setUserName(getUserName());
		bbbParticipantImpl.setCreateDate(getCreateDate());
		bbbParticipantImpl.setModifiedDate(getModifiedDate());
		bbbParticipantImpl.setBbbMeetingId(getBbbMeetingId());
		bbbParticipantImpl.setName(getName());
		bbbParticipantImpl.setEmailAddress(getEmailAddress());
		bbbParticipantImpl.setType(getType());
		bbbParticipantImpl.setStatus(getStatus());

		bbbParticipantImpl.resetOriginalValues();

		return bbbParticipantImpl;
	}

	@Override
	public int compareTo(BBBParticipant bbbParticipant) {
		long primaryKey = bbbParticipant.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BBBParticipant)) {
			return false;
		}

		BBBParticipant bbbParticipant = (BBBParticipant)obj;

		long primaryKey = bbbParticipant.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		BBBParticipantModelImpl bbbParticipantModelImpl = this;

		bbbParticipantModelImpl._originalBbbMeetingId = bbbParticipantModelImpl._bbbMeetingId;

		bbbParticipantModelImpl._setOriginalBbbMeetingId = false;

		bbbParticipantModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<BBBParticipant> toCacheModel() {
		BBBParticipantCacheModel bbbParticipantCacheModel = new BBBParticipantCacheModel();

		bbbParticipantCacheModel.bbbParticipantId = getBbbParticipantId();

		bbbParticipantCacheModel.groupId = getGroupId();

		bbbParticipantCacheModel.companyId = getCompanyId();

		bbbParticipantCacheModel.userId = getUserId();

		bbbParticipantCacheModel.userName = getUserName();

		String userName = bbbParticipantCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			bbbParticipantCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			bbbParticipantCacheModel.createDate = createDate.getTime();
		}
		else {
			bbbParticipantCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			bbbParticipantCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			bbbParticipantCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		bbbParticipantCacheModel.bbbMeetingId = getBbbMeetingId();

		bbbParticipantCacheModel.name = getName();

		String name = bbbParticipantCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			bbbParticipantCacheModel.name = null;
		}

		bbbParticipantCacheModel.emailAddress = getEmailAddress();

		String emailAddress = bbbParticipantCacheModel.emailAddress;

		if ((emailAddress != null) && (emailAddress.length() == 0)) {
			bbbParticipantCacheModel.emailAddress = null;
		}

		bbbParticipantCacheModel.type = getType();

		bbbParticipantCacheModel.status = getStatus();

		return bbbParticipantCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(25);

		sb.append("{bbbParticipantId=");
		sb.append(getBbbParticipantId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", bbbMeetingId=");
		sb.append(getBbbMeetingId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", emailAddress=");
		sb.append(getEmailAddress());
		sb.append(", type=");
		sb.append(getType());
		sb.append(", status=");
		sb.append(getStatus());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(40);

		sb.append("<model><model-name>");
		sb.append("com.liferay.bbb.model.BBBParticipant");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>bbbParticipantId</column-name><column-value><![CDATA[");
		sb.append(getBbbParticipantId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>bbbMeetingId</column-name><column-value><![CDATA[");
		sb.append(getBbbMeetingId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>emailAddress</column-name><column-value><![CDATA[");
		sb.append(getEmailAddress());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>type</column-name><column-value><![CDATA[");
		sb.append(getType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>status</column-name><column-value><![CDATA[");
		sb.append(getStatus());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static ClassLoader _classLoader = BBBParticipant.class.getClassLoader();
	private static Class<?>[] _escapedModelInterfaces = new Class[] {
			BBBParticipant.class
		};
	private long _bbbParticipantId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _bbbMeetingId;
	private long _originalBbbMeetingId;
	private boolean _setOriginalBbbMeetingId;
	private String _name;
	private String _emailAddress;
	private int _type;
	private int _status;
	private long _columnBitmask;
	private BBBParticipant _escapedModel;
}