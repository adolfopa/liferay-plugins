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

package com.liferay.vldap.server.directory.builder;

import com.liferay.compat.portal.util.PortalUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactory;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.PasswordPolicy;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.comparator.UserScreenNameComparator;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.vldap.BaseVLDAPTestCase;
import com.liferay.vldap.server.directory.FilterConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.time.FastDateFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Peter Shin
 */
@RunWith(PowerMockRunner.class)
public class UserBuilderFilterConstraintsTest extends BaseVLDAPTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		setupUsers();

		setupExpando();
		setupFastDateFormat();
		setupOrganizations();
		setupPasswordPolicy();
		setupPortalUtil();
		setupRoles();
	}

	@Test
	public void testGetUsers() throws Exception {
		List<FilterConstraint> filterConstraints =
			new ArrayList<FilterConstraint>();

		FilterConstraint organizationFilterConstraint = new FilterConstraint();

		organizationFilterConstraint.addAttribute(
			"mail", "testHasOrganizationUser@email");
		organizationFilterConstraint.addAttribute(
			"member",
			"cn=testOrganizationName,ou=testOrganizationName," +
				"ou=Organizations,ou=liferay.com,o=Liferay");

		filterConstraints.add(organizationFilterConstraint);

		FilterConstraint roleFilterConstraint = new FilterConstraint();

		roleFilterConstraint.addAttribute("mail", "testUserWtihRole@email");
		roleFilterConstraint.addAttribute(
			"member",
			"cn=testRoleName,ou=testRoleName,ou=Roles,ou=liferay.com," +
				"o=Liferay");

		filterConstraints.add(roleFilterConstraint);

		List<User> users = _userBuilder.getUsers(
			company, searchBase, filterConstraints);

		Assert.assertEquals(2, users.size());
	}

	protected void setupExpando() {
		ExpandoBridge expandBridge = mock(ExpandoBridge.class);

		when(
			expandBridge.getAttribute(
				Mockito.eq("sambaLMPassword"), Mockito.eq(false))
		).thenReturn(
			"testLMPassword"
		);

		when(
			expandBridge.getAttribute(
				Mockito.eq("sambaNTPassword"), Mockito.eq(false))
		).thenReturn(
			"testNTPassword"
		);

		when(
			_hasOrganizationUser.getExpandoBridge()
		).thenReturn(
			expandBridge
		);

		when(
			_hasRoleUser.getExpandoBridge()
		).thenReturn(
			expandBridge
		);
	}

	protected void setupFastDateFormat() {
		FastDateFormat fastDateFormat = FastDateFormat.getInstance(
			"yyyyMMddHHmmss.SZ", (TimeZone)null, LocaleUtil.getDefault());

		FastDateFormatFactory fastDateFormatFactory = mock(
			FastDateFormatFactory.class);

		when(
			fastDateFormatFactory.getSimpleDateFormat(Mockito.anyString())
		).thenReturn(
			fastDateFormat
		);

		FastDateFormatFactoryUtil fastDateFormatFactoryUtil =
			new FastDateFormatFactoryUtil();

		fastDateFormatFactoryUtil.setFastDateFormatFactory(
			fastDateFormatFactory);
	}

	protected void setupOrganizations() throws Exception {
		Organization organization = mock(Organization.class);

		when(
			organization.getName()
		).thenReturn(
			"testOrganizationName"
		);

		when(
			organization.getOrganizationId()
		).thenReturn(
			42l
		);

		List<Organization> organizations = new ArrayList<Organization>();

		organizations.add(organization);

		when(
			organizationLocalService.getOrganization(
				Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			organization
		);

		when(
			_hasOrganizationUser.getOrganizations()
		).thenReturn(
			organizations
		);
	}

	protected void setupPasswordPolicy() throws Exception {
		PasswordPolicy passwordPolicy = mock(PasswordPolicy.class);

		when(
			passwordPolicy.getGraceLimit()
		).thenReturn(
			7200000
		);

		when(
			passwordPolicy.getHistoryCount()
		).thenReturn(
			3600000
		);

		when(
			passwordPolicy.getLockoutDuration()
		).thenReturn(
			7200000l
		);

		when(
			passwordPolicy.getMaxAge()
		).thenReturn(
			14400000l
		);

		when(
			passwordPolicy.getMinAge()
		).thenReturn(
			3600000l
		);

		when(
			passwordPolicy.getResetFailureCount()
		).thenReturn(
			3600000l
		);

		when(
			passwordPolicy.isExpireable()
		).thenReturn(
			false
		);

		when(
			passwordPolicy.isLockout()
		).thenReturn(
			true
		);

		when(
			passwordPolicy.isRequireUnlock()
		).thenReturn(
			true
		);

		when(
			_hasOrganizationUser.getPasswordPolicy()
		).thenReturn(
			passwordPolicy
		);

		when(
			_hasRoleUser.getPasswordPolicy()
		).thenReturn(
			passwordPolicy
		);
	}

	protected void setupPortalUtil() {
		Portal portal = mock(Portal.class);

		when(
			portal.getClassNameId(Mockito.any(Class.class))
		).thenReturn(
			42l
		);

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(portal);
	}

	protected void setupRoles() throws Exception {
		Role role = mock(Role.class);

		when(
			role.getName()
		).thenReturn(
			"testRoleName"
		);

		when(
			role.getRoleId()
		).thenReturn(
			42l
		);

		List<Role> roles = new ArrayList<Role>();

		roles.add(role);

		when(
			roleLocalService.getRole(Mockito.anyLong(), Mockito.anyString())
		).thenReturn(
			role
		);

		when(
			_hasRoleUser.getRoles()
		).thenReturn(
			roles
		);
	}

	protected void setupUsers() throws Exception {
		_hasOrganizationUser = mock(User.class);

		when(
			_hasOrganizationUser.getCompanyId()
		).thenReturn(
			42l
		);

		when(
			_hasOrganizationUser.getCreateDate()
		).thenReturn(
			null
		);

		when(
			_hasOrganizationUser.getEmailAddress()
		).thenReturn(
			"testHasOrganizationUser@email"
		);

		when(
			_hasOrganizationUser.getFirstName()
		).thenReturn(
			"testHasOrganizationUserFirstName"
		);

		when(
			_hasOrganizationUser.getFullName()
		).thenReturn(
			"testHasOrganizationUserFullName"
		);

		when(
			_hasOrganizationUser.getLastName()
		).thenReturn(
			"testHasOrganizationUserLastName"
		);

		when(
			_hasOrganizationUser.getModifiedDate()
		).thenReturn(
			null
		);

		when(
			_hasOrganizationUser.getScreenName()
		).thenReturn(
			"testHasOrganizationUserScreenName"
		);

		when(
			_hasOrganizationUser.getUserId()
		).thenReturn(
			42l
		);

		when(
			_hasOrganizationUser.getUuid()
		).thenReturn(
			"testHasOrganizationUserUuid"
		);

		_users.add(_hasOrganizationUser);

		_hasRoleUser = mock(User.class);

		when(
			_hasRoleUser.getCompanyId()
		).thenReturn(
			42l
		);

		when(
			_hasRoleUser.getCreateDate()
		).thenReturn(
			null
		);

		when(
			_hasRoleUser.getEmailAddress()
		).thenReturn(
			"testUserWtihRole@email"
		);

		when(
			_hasRoleUser.getFirstName()
		).thenReturn(
			"testHasRoleUserFirstName"
		);

		when(
			_hasRoleUser.getFullName()
		).thenReturn(
			"testHasRoleUserFullName"
		);

		when(
			_hasRoleUser.getLastName()
		).thenReturn(
			"testHasRoleUserLastName"
		);

		when(
			_hasRoleUser.getModifiedDate()
		).thenReturn(
			null
		);

		when(
			_hasRoleUser.getScreenName()
		).thenReturn(
			"testHasRoleUserScreenName"
		);

		when(
			_hasRoleUser.getUserId()
		).thenReturn(
			42l
		);

		when(
			_hasRoleUser.getUuid()
		).thenReturn(
			"testHasRoleUserUuid"
		);

		_users.add(_hasRoleUser);

		when(
			userLocalService.getCompanyUsers(
				Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())
		).thenReturn(
			_users
		);

		LinkedHashMap<String, Object> usersRolesParams =
			new LinkedHashMap<String, Object>();

		usersRolesParams.put("usersRoles", 42l);

		when(
			userLocalService.search(
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyInt(), Mockito.eq(usersRolesParams),
				Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt(),
				Mockito.any(UserScreenNameComparator.class))
		).thenReturn(
			Arrays.asList(_hasRoleUser)
		);

		LinkedHashMap<String, Object> usersOrgsParams =
			new LinkedHashMap<String, Object>();

		usersOrgsParams.put("usersOrgs", 42l);

		when(
			userLocalService.search(
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyInt(), Mockito.eq(usersOrgsParams),
				Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt(),
				Mockito.any(UserScreenNameComparator.class))
		).thenReturn(
			Arrays.asList(_hasOrganizationUser)
		);
	}

	private User _hasOrganizationUser;
	private User _hasRoleUser;
	private UserBuilder _userBuilder = new UserBuilder();
	private List<User> _users = new ArrayList<User>();

}