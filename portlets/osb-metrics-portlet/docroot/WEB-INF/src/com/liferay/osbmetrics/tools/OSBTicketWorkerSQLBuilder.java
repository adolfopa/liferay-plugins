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

package com.liferay.osbmetrics.tools;

import com.liferay.compat.portal.kernel.util.PortalClassInvoker;
import com.liferay.osbmetrics.util.PortletPropsValues;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ClassResolverUtil;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Rachael Koestartyo
 */
public class OSBTicketWorkerSQLBuilder {
	public String buildSQL() throws Exception {
		if (!hasNewDeletedOSBTicketWorkers()) {
			return StringPool.BLANK;
		}

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String template = StringPool.BLANK;

		try {
			con = DataAccess.getConnection();

			String sql = getDeletedUsers();

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				long userId = rs.getLong("userId");

				String userName = rs.getString("userName");

				String[] fullName = (String[])PortalClassInvoker.invoke(
						_splitFullNameMethodKey, userName);

				if (_log.isDebugEnabled()) {
					_log.debug("Writing " + userId + " " + userName);
				}

				template = template + buildUserTemplate(userId, fullName);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}

		DB db = DBFactoryUtil.getDB();

		return db.buildSQL(template);
	}

	public boolean hasNewDeletedOSBTicketWorkers() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getConnection();

			String sql = getDeletedUsers();

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}

			return false;
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	protected String buildUserTemplate(long userId, String[] fullName)
		throws Exception {

		long companyId = _COMPANY_ID;
		String createDate = "CURRENT_TIMESTAMP";
		String modifiedDate = "CURRENT_TIMESTAMP";
		String defaultUser = "FALSE";
		long contactId = userId + 1;
		String password_ = "test";
		String passwordEncrypted = "FALSE";
		String passwordReset = "FALSE";
		String passwordModifiedDate = "CURRENT_TIMESTAMP";
		int graceLoginCount = 0;
		String screenName = getScreenName(fullName);
		String emailAddress = getEmailAddress(fullName);
		String openId = "";
		long portraitId = 0;
		String languageId = "";
		String timeZoneId = "";
		String greeting = "";
		String comments = "";
		String loginDate = "CURRENT_TIMESTAMP";
		String loginIP = "";
		String lastLoginDate = "CURRENT_TIMESTAMP";
		String lastLoginIP = "";
		String lastFailedLoginDate = "CURRENT_TIMESTAMP";
		int failedLoginAttempts = 0;
		boolean lockout = false;
		String lockoutDate = "CURRENT_TIMESTAMP";
		String agreedToTermsOfUse = "TRUE";
		String uuid_ = "";
		String firstName = fullName[0];
		String middleName = fullName[1];
		String lastName = fullName[2];
		String jobTitle = "";
		String reminderQueryQuestion = "";
		String reminderQueryAnswer = "";
		double socialContributionEquity = 0;
		double socialParticipationEquity = 0;
		double socialPersonalEquity = 0;
		long facebookId = 0;
		String digest = "";
		String emailAddressVerified = "TRUE";
		int status = _WORKFLOW_CONSTANTS_STATUS_INACTIVE;

		StringBundler sb = new StringBundler(97);

		sb.append("insert into [$LRDCOM_DB$]User_ (userId,companyId,");
		sb.append("createDate,modifiedDate,defaultUser,contactId,");
		sb.append("password_,passwordEncrypted,passwordReset,");
		sb.append("passwordModifiedDate,graceLoginCount,screenName,");
		sb.append("emailAddress,openId,portraitId,languageId,timeZoneId,");
		sb.append("greeting,comments,loginDate,loginIP,lastLoginDate,");
		sb.append("lastLoginIP,lastFailedLoginDate,failedLoginAttempts,");
		sb.append("lockout,lockoutDate,agreedToTermsOfUse,uuid_,");
		sb.append("firstName,middleName,lastName,jobTitle,");
		sb.append("reminderQueryQuestion,reminderQueryAnswer,");
		sb.append("socialContributionEquity,socialParticipationEquity,");
		sb.append("socialPersonalEquity,facebookId,digest,");
		sb.append("emailAddressVerified,status) values (");
		sb.append(userId);
		sb.append(", ");
		sb.append(companyId);
		sb.append(", ");
		sb.append(createDate);
		sb.append(", ");
		sb.append(modifiedDate);
		sb.append(", ");
		sb.append(defaultUser);
		sb.append(", ");
		sb.append(contactId);
		sb.append(", '");
		sb.append(password_);
		sb.append("', ");
		sb.append(passwordEncrypted);
		sb.append(", ");
		sb.append(passwordReset);
		sb.append(", ");
		sb.append(passwordModifiedDate);
		sb.append(", ");
		sb.append(graceLoginCount);
		sb.append(", '");
		sb.append(screenName);
		sb.append("', '");
		sb.append(emailAddress);
		sb.append("', '");
		sb.append(openId);
		sb.append("', ");
		sb.append(portraitId);
		sb.append(", '");
		sb.append(languageId);
		sb.append("', '");
		sb.append(timeZoneId);
		sb.append("', '");
		sb.append(greeting);
		sb.append("', '");
		sb.append(comments);
		sb.append("', ");
		sb.append(loginDate);
		sb.append(", '");
		sb.append(loginIP);
		sb.append("', ");
		sb.append(lastLoginDate);
		sb.append(", '");
		sb.append(lastLoginIP);
		sb.append("', ");
		sb.append(lastFailedLoginDate);
		sb.append(", ");
		sb.append(failedLoginAttempts);
		sb.append(", ");
		sb.append(lockout);
		sb.append(", ");
		sb.append(lockoutDate);
		sb.append(", ");
		sb.append(agreedToTermsOfUse);
		sb.append(", '");
		sb.append(uuid_);
		sb.append("', '");
		sb.append(firstName);
		sb.append("', '");
		sb.append(middleName);
		sb.append("', '");
		sb.append(lastName);
		sb.append("', '");
		sb.append(jobTitle);
		sb.append("', '");
		sb.append(reminderQueryQuestion);
		sb.append("', '");
		sb.append(reminderQueryAnswer);
		sb.append("', ");
		sb.append(socialContributionEquity);
		sb.append(", ");
		sb.append(socialParticipationEquity);
		sb.append(", ");
		sb.append(socialPersonalEquity);
		sb.append(", ");
		sb.append(facebookId);
		sb.append(", '");
		sb.append(digest);
		sb.append("', ");
		sb.append(emailAddressVerified);
		sb.append(", ");
		sb.append(status);
		sb.append(");\n");

		String sql = sb.toString();

		sql = StringUtil.replace(
			sql, "[$LRDCOM_DB$]",
			PortletPropsValues.LRDCOM_DB + StringPool.PERIOD);

		return sql;
	}

	protected String getDeletedUsers() {
		StringBundler sb = new StringBundler(11);

		sb.append("select OSB_TicketComment.userId, ");
		sb.append("OSB_TicketComment.userName from ");
		sb.append("[$LRDCOM_DB$]OSB_TicketComment left outer join ");
		sb.append("[$LRDCOM_DB$]OSB_TicketAttachment on ");
		sb.append("OSB_TicketComment.userId = ");
		sb.append("OSB_TicketAttachment.userId left outer join ");
		sb.append("[$LRDCOM_DB$]User_ on ");
		sb.append("OSB_TicketAttachment.userId = User_.userId where ");
		sb.append("User_.userId is null and OSB_TicketAttachment.userId ");
		sb.append("is not null and OSB_TicketComment.userName ");
		sb.append("!= 'first last' group by OSB_TicketComment.userId");

		String sql = sb.toString();

		sql = StringUtil.replace(
			sql, "[$LRDCOM_DB$]",
			PortletPropsValues.LRDCOM_DB + StringPool.PERIOD);

		return sql;
	}

	protected String getEmailAddress(String[] fullName) {
		String emailAddress = fullName[0] + StringPool.PERIOD + fullName[2] +
			_USER_EMAIL_ADDRESS_SUFFIX;

		return emailAddress;
	}

	protected String getScreenName(String[] fullName) {
		String screenName = _USER_SCREEN_NAME_PREFIX + fullName[0] +
			StringPool.PERIOD + fullName[2];

		return screenName;
	}

	private static final String _CLASS_NAME =
		"com.liferay.portal.security.auth.DefaultFullNameGenerator";

	private static final long _COMPANY_ID = 1;

	private static final String _USER_EMAIL_ADDRESS_SUFFIX = "@metrics.com";

	private static final String _USER_SCREEN_NAME_PREFIX = "metrics.";

	private static final int _WORKFLOW_CONSTANTS_STATUS_INACTIVE = 5;

	private static Log _log = LogFactoryUtil.getLog(
		OSBTicketWorkerSQLBuilder.class);

	private static MethodKey _splitFullNameMethodKey = new MethodKey(
		ClassResolverUtil.resolveByPortalClassLoader(_CLASS_NAME),
		"splitFullName", String.class);

}