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

package com.liferay.saml.resolver;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.PwdGenerator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.security.exportimport.UserImporterUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.saml.metadata.MetadataManagerUtil;
import com.liferay.saml.util.SamlUtil;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.joda.time.DateTime;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.core.Response;

/**
 * @author Mika Koivisto
 */
public class DefaultUserResolver implements UserResolver {

	@Override
	public User resolveUser(
			Assertion assertion,
			SAMLMessageContext<Response, SAMLObject, NameID> samlMessageContext,
			ServiceContext serviceContext)
		throws Exception {

		if (_log.isDebugEnabled()) {
			NameID nameId = samlMessageContext.getSubjectNameIdentifier();

			_log.debug(
				"Resolving user with name ID format " + nameId.getFormat() +
					" and value " + nameId.getValue());
		}

		User user = null;

		long companyId = CompanyThreadLocal.getCompanyId();

		String subjectNameIdentifier = getSubjectNameIdentifier(
			companyId, assertion, samlMessageContext);
		String subjectNameIdentifierType = getSubjectNameIdentifierType(
			companyId, assertion, samlMessageContext);

		if (SamlUtil.isLDAPImportEnabled()) {
			user = importLdapUser(
				companyId, subjectNameIdentifier, subjectNameIdentifierType);
		}

		if (user == null) {
			return importUser(
				companyId, subjectNameIdentifier, subjectNameIdentifierType,
				assertion, samlMessageContext, serviceContext);
		}

		return user;
	}

	protected User addUser(
			long companyId, Map<String, List<Serializable>> attributesMap,
			ServiceContext serviceContext)
		throws PortalException {

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Adding user with attributes map " +
					MapUtil.toString(attributesMap));
		}

		long creatorUserId = 0;
		boolean autoPassword = false;
		String password1 = PwdGenerator.getPassword();
		String password2 = password1;
		boolean autoScreenName = false;
		String screenName = getValueAsString("screenName", attributesMap);
		String emailAddress = getValueAsString("emailAddress", attributesMap);
		long facebookId = 0;
		String openId = StringPool.BLANK;
		Locale locale = serviceContext.getLocale();
		String firstName = getValueAsString("firstName", attributesMap);
		String middleName = StringPool.BLANK;
		String lastName = getValueAsString("lastName", attributesMap);
		int prefixId = 0;
		int suffixId = 0;
		boolean male = true;
		int birthdayMonth = Calendar.JANUARY;
		int birthdayDay = 1;
		int birthdayYear = 1970;
		String jobTitle = StringPool.BLANK;
		long[] groupIds = null;
		long[] organizationIds = null;
		long[] roleIds = null;
		long[] userGroupIds = null;
		boolean sendEmail = false;

		String uuid = getValueAsString("uuid", attributesMap);

		serviceContext.setUuid(uuid);

		User user = UserLocalServiceUtil.addUser(
			creatorUserId, companyId, autoPassword, password1, password2,
			autoScreenName, screenName, emailAddress, facebookId, openId,
			locale, firstName, middleName, lastName, prefixId, suffixId, male,
			birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds,
			organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);

		user = UserLocalServiceUtil.updateEmailAddressVerified(
			user.getUserId(), true);

		user = UserLocalServiceUtil.updatePasswordReset(
			user.getUserId(), false);

		Date modifiedDate = getValueAsDate("modifiedDate", attributesMap);

		if (modifiedDate != null) {
			user = UserLocalServiceUtil.updateModifiedDate(
				user.getUserId(), modifiedDate);
		}

		return user;
	}

	protected List<Attribute> getAttributes(
		Assertion assertion,
		SAMLMessageContext<Response, SAMLObject, NameID>
			samlMessageContext) {

		List<Attribute> attributes = new ArrayList<Attribute>();

		for (AttributeStatement attributeStatement :
				assertion.getAttributeStatements()) {

			attributes.addAll(attributeStatement.getAttributes());
		}

		return attributes;
	}

	protected Map<String, List<Serializable>> getAttributesMap(
		List<Attribute> attributes,
		SAMLMessageContext<Response, SAMLObject, NameID> samlMessageContext) {

		String peerEntityId = samlMessageContext.getPeerEntityId();

		try {
			String userAttributeMappings =
				MetadataManagerUtil.getUserAttributeMappings(peerEntityId);

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Attributes mapping for " + peerEntityId + " " +
						userAttributeMappings);
			}

			Properties userAttributeMappingsProperties = new Properties();

			if (Validator.isNotNull(userAttributeMappings)) {
				userAttributeMappingsProperties = PropertiesUtil.load(
					userAttributeMappings);
			}

			return SamlUtil.getAttributesMap(
				attributes, userAttributeMappingsProperties);
}
		catch (Exception e) {
		}

		return Collections.emptyMap();
	}

	protected String getSubjectNameIdentifier(
		long companyId, Assertion assertion,
		SAMLMessageContext<Response, SAMLObject, NameID> samlMessageContext) {

		NameID nameId = samlMessageContext.getSubjectNameIdentifier();

		return nameId.getValue();
	}

	protected String getSubjectNameIdentifierType(
		long companyId, Assertion assertion,
		SAMLMessageContext<Response, SAMLObject, NameID> samlMessageContext) {

		NameID nameId = samlMessageContext.getSubjectNameIdentifier();

		String format = nameId.getFormat();

		if (format.equals(NameIDType.EMAIL)) {
			return _SUBJECT_NAME_TYPE_EMAIL_ADDRESS;
		}

		return _SUBJECT_NAME_TYPE_SCREENNAME;
	}

	protected User getUser(
			long companyId, String subjectNameIdentifier,
			String subjectNameIdentifierType)
		throws PortalException {

		try {
			if (subjectNameIdentifierType.endsWith(
					_SUBJECT_NAME_TYPE_EMAIL_ADDRESS)) {

				return UserLocalServiceUtil.getUserByEmailAddress(
					companyId, subjectNameIdentifier);
			}
			else if (subjectNameIdentifierType.endsWith(
						_SUBJECT_NAME_TYPE_SCREENNAME)) {

				return UserLocalServiceUtil.getUserByScreenName(
					companyId, subjectNameIdentifier);
			}
			else if (subjectNameIdentifierType.endsWith(
						_SUBJECT_NAME_TYPE_UUID)) {

				return UserLocalServiceUtil.getUserByUuidAndCompanyId(
					subjectNameIdentifier, companyId);
			}
		}
		catch (NoSuchUserException nsue) {
		}

		return null;
	}

	protected Date getValueAsDate(
		String key, Map<String, List<Serializable>> attributesMap) {

		List<Serializable> values = attributesMap.get(key);

		if (ListUtil.isEmpty(values)) {
			return null;
		}

		DateTime dateTime = new DateTime(values.get(0));

		return dateTime.toDate();
	}

	protected String getValueAsString(
		String key, Map<String, List<Serializable>> attributesMap) {

		List<Serializable> values = attributesMap.get(key);

		if (ListUtil.isEmpty(values)) {
			return null;
		}

		return String.valueOf(values.get(0));
	}

	protected User importLdapUser(
			long companyId, String subjectNameIdentifier,
			String subjectNameIdentifierType)
		throws Exception {

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Importing user from LDAP with identifier " +
					subjectNameIdentifier + " of type " +
						subjectNameIdentifierType);
		}

		User user = null;

		if (subjectNameIdentifierType.endsWith(
				_SUBJECT_NAME_TYPE_EMAIL_ADDRESS)) {

			user = UserImporterUtil.importUser(
				companyId, subjectNameIdentifier, StringPool.BLANK);
		}
		else {
			user = UserImporterUtil.importUser(
				companyId, StringPool.BLANK, subjectNameIdentifier);
		}

		return user;
	}

	protected User importUser(
			long companyId, String subjectNameIdentifier,
			String subjectNameIdentifierType, Assertion assertion,
			SAMLMessageContext<Response, SAMLObject, NameID>
				samlMessageContext, ServiceContext serviceContext)
		throws PortalException {

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Importing user with identifier " + subjectNameIdentifier +
					" of type " + subjectNameIdentifierType);
		}

		List<Attribute> attributes = getAttributes(
			assertion, samlMessageContext);

		Map<String, List<Serializable>> attributesMap = getAttributesMap(
			attributes, samlMessageContext);

		User user = getUser(
			companyId, subjectNameIdentifier, subjectNameIdentifierType);

		if (user != null) {
			if (_log.isDebugEnabled()) {
				_log.debug("Found user " + user.toString());
			}

			user = updateUser(companyId, user, attributesMap, serviceContext);
		}
		else {
			user = addUser(companyId, attributesMap, serviceContext);

			if (_log.isDebugEnabled()) {
				_log.debug("Added user " + user.toString());
			}
		}

		return user;
	}

	protected User updateUser(
			long companyId, User user,
			Map<String, List<Serializable>> attributesMap,
			ServiceContext serviceContext)
		throws PortalException {

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Updating user " + user.getUserId() + " with attributes map " +
					MapUtil.toString(attributesMap));
		}

		Date modifiedDate = null;
		String screenName = null;
		String emailAddress = null;
		String firstName = null;
		String lastName = null;

		if (getValueAsDate("modifiedDate", attributesMap) != null) {
			modifiedDate = getValueAsDate("modifiedDate", attributesMap);
		}
		else {
			modifiedDate = user.getModifiedDate();
		}

		if (Validator.isNotNull(
				getValueAsString("screenName", attributesMap))) {

			screenName = getValueAsString("screenName", attributesMap);
		}
		else {
			screenName = user.getScreenName();
		}

		if (Validator.isNotNull(
				getValueAsString("emailAddress", attributesMap))) {

			emailAddress = getValueAsString("emailAddress", attributesMap);
		}
		else {
			emailAddress = user.getEmailAddress();
		}

		if (Validator.isNotNull(getValueAsString("firstName", attributesMap))) {
			firstName = getValueAsString("firstName", attributesMap);
		}
		else {
			firstName = user.getFirstName();
		}

		if (Validator.isNotNull(getValueAsString("lastName", attributesMap))) {
			lastName = getValueAsString("lastName", attributesMap);
		}
		else {
			lastName = user.getScreenName();
		}

		Contact contact = user.getContact();

		if (!StringUtil.equalsIgnoreCase(
				emailAddress, user.getEmailAddress())) {

			user = UserLocalServiceUtil.updateEmailAddress(
				user.getUserId(), StringPool.BLANK, emailAddress, emailAddress);

			user = UserLocalServiceUtil.updateEmailAddressVerified(
				user.getUserId(), true);
		}

		if (!Validator.equals(user.getFirstName(), firstName) ||
			!Validator.equals(user.getLastName(), lastName) ||
			!Validator.equals(user.getScreenName(), screenName) ||
			!Validator.equals(user.getModifiedDate(), modifiedDate)) {

			Date oldModifiedDate = user.getModifiedDate();

			Calendar birthdayCalendar = CalendarFactoryUtil.getCalendar();

			birthdayCalendar.setTime(contact.getBirthday());

			user = UserLocalServiceUtil.updateUser(
				user.getUserId(), StringPool.BLANK, StringPool.BLANK,
				StringPool.BLANK, false, user.getReminderQueryQuestion(),
				user.getReminderQueryAnswer(), screenName, emailAddress,
				user.getFacebookId(), user.getOpenId(), false, null,
				user.getLanguageId(), user.getTimeZoneId(), user.getGreeting(),
				user.getComments(), firstName, user.getMiddleName(), lastName,
				contact.getPrefixId(), contact.getSuffixId(), user.getMale(),
				birthdayCalendar.get(Calendar.MONTH),
				birthdayCalendar.get(Calendar.DATE),
				birthdayCalendar.get(Calendar.YEAR), contact.getSmsSn(),
				contact.getAimSn(), contact.getFacebookSn(), contact.getIcqSn(),
				contact.getJabberSn(), contact.getMsnSn(),
				contact.getMySpaceSn(), contact.getSkypeSn(),
				contact.getTwitterSn(), contact.getYmSn(),
				contact.getJobTitle(), null, null, null, null, null,
				serviceContext);

			if (!Validator.equals(oldModifiedDate, modifiedDate)) {
				user = UserLocalServiceUtil.updateModifiedDate(
					user.getUserId(), modifiedDate);
			}
		}

		return user;
	}

	private static final String _SUBJECT_NAME_TYPE_EMAIL_ADDRESS =
		"emailAddress";

	private static final String _SUBJECT_NAME_TYPE_SCREENNAME = "screenName";

	private static final String _SUBJECT_NAME_TYPE_UUID = "uuid";

	private static Log _log = LogFactoryUtil.getLog(DefaultUserResolver.class);

}