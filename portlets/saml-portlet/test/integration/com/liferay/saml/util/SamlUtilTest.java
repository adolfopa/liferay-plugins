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

package com.liferay.saml.util;

import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.saml.BaseSamlTestCase;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.opensaml.saml2.core.Attribute;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Mika Koivisto
 */
public class SamlUtilTest extends BaseSamlTestCase {

	@Before
	public void setUp() throws Exception {
		super.setUp();

		_http = mock(Http.class);

		HttpUtil httpUtil = new HttpUtil();

		httpUtil.setHttp(_http);
	}

	@Test
	public void testGetAttributesMap() {
		List<Attribute> attributes = new ArrayList<>();

		attributes.add(
			OpenSamlUtil.buildAttribute("emailAddress", "test@liferay.com"));
		attributes.add(
			OpenSamlUtil.buildAttribute("firstName", "TestFirstName"));
		attributes.add(OpenSamlUtil.buildAttribute("lastName", "TestLastName"));
		attributes.add(OpenSamlUtil.buildAttribute("screenName", "test"));

		Map<String, List<Serializable>> attributesMap =
			SamlUtil.getAttributesMap(attributes, new Properties());

		Assert.assertEquals(
			"test@liferay.com",
			SamlUtil.getValueAsString("emailAddress", attributesMap));
		Assert.assertEquals(
			"TestFirstName",
			SamlUtil.getValueAsString("firstName", attributesMap));
		Assert.assertEquals(
			"TestLastName",
			SamlUtil.getValueAsString("lastName", attributesMap));
		Assert.assertEquals(
			"test", SamlUtil.getValueAsString("screenName", attributesMap));
	}

	@Test
	public void testGetAttributesMapWithMapping() {
		List<Attribute> attributes = new ArrayList<>();

		attributes.add(
			OpenSamlUtil.buildAttribute("givenName", "TestFirstName"));
		attributes.add(OpenSamlUtil.buildAttribute("mail", "test@liferay.com"));
		attributes.add(OpenSamlUtil.buildAttribute("sn", "TestLastName"));
		attributes.add(
			OpenSamlUtil.buildAttribute("userPrincipalName", "test"));
		attributes.add(OpenSamlUtil.buildAttribute("title", "test job title"));

		Properties attributeMappingsProperties = new Properties();

		attributeMappingsProperties.put("givenName", "firstName");
		attributeMappingsProperties.put("mail", "emailAddress");
		attributeMappingsProperties.put("sn", "lastName");
		attributeMappingsProperties.put("userPrincipalName", "screenName");

		Map<String, List<Serializable>> attributesMap =
			SamlUtil.getAttributesMap(attributes, attributeMappingsProperties);

		Assert.assertEquals(
			"test@liferay.com",
			SamlUtil.getValueAsString("emailAddress", attributesMap));
		Assert.assertEquals(
			"TestFirstName",
			SamlUtil.getValueAsString("firstName", attributesMap));
		Assert.assertEquals(
			"TestLastName",
			SamlUtil.getValueAsString("lastName", attributesMap));
		Assert.assertEquals(
			"test", SamlUtil.getValueAsString("screenName", attributesMap));
		Assert.assertEquals(
			"test job title",
			SamlUtil.getValueAsString("title", attributesMap));
	}

	@Test
	public void testGetAttributesMapWithMappingAndFriendlyName() {
		List<Attribute> attributes = new ArrayList<>();

		attributes.add(
			OpenSamlUtil.buildAttribute(
				"mail", "emailAddress", Attribute.UNSPECIFIED,
				"test@liferay.com"));
		attributes.add(
			OpenSamlUtil.buildAttribute(
				"givenName", "firstName", Attribute.UNSPECIFIED,
				"TestFirstName"));
		attributes.add(
			OpenSamlUtil.buildAttribute(
				"sn", "lastName", Attribute.UNSPECIFIED, "TestLastName"));
		attributes.add(
			OpenSamlUtil.buildAttribute(
				"userPrincipalName", "screenName", Attribute.UNSPECIFIED,
				"test"));
		attributes.add(
			OpenSamlUtil.buildAttribute(
				"title", "jobTitle", Attribute.UNSPECIFIED, "test job title"));

		DateTime modifiedDate = new DateTime(DateTimeZone.UTC);

		attributes.add(
			OpenSamlUtil.buildAttribute(
				"whenChanged", "modifiedDate", Attribute.UNSPECIFIED,
				modifiedDate));

		Properties attributeMappingsProperties = new Properties();

		attributeMappingsProperties.put("firstName", "firstName");
		attributeMappingsProperties.put("emailAddress", "emailAddress");
		attributeMappingsProperties.put("lastName", "lastName");
		attributeMappingsProperties.put("screenName", "screenName");
		attributeMappingsProperties.put("whenChanged", "modifiedDate");

		Map<String, List<Serializable>> attributesMap =
			SamlUtil.getAttributesMap(attributes, attributeMappingsProperties);

		Assert.assertEquals(
			"test@liferay.com",
			SamlUtil.getValueAsString("emailAddress", attributesMap));
		Assert.assertEquals(
			"TestFirstName",
			SamlUtil.getValueAsString("firstName", attributesMap));
		Assert.assertEquals(
			"TestLastName",
			SamlUtil.getValueAsString("lastName", attributesMap));
		Assert.assertNotNull(
			SamlUtil.getValueAsDate("modifiedDate", attributesMap));
		Assert.assertTrue(
			modifiedDate.isEqual(
				SamlUtil.getValueAsDateTime("modifiedDate", attributesMap)));
		Assert.assertEquals(
			"test", SamlUtil.getValueAsString("screenName", attributesMap));
		Assert.assertEquals(
			"test job title",
			SamlUtil.getValueAsString("title", attributesMap));
	}

	@Test
	public void testGetRequestPath() {
		when(
			_http.removePathParameters(
				"/c/portal/login;jsessionid=ACD311312312323BF.worker1")
		).thenReturn(
			"/c/portal/login"
		);

		MockHttpServletRequest request = new MockHttpServletRequest(
			HttpMethods.GET,
			"/c/portal/login;jsessionid=ACD311312312323BF.worker1");

		Assert.assertEquals(
			"/c/portal/login", SamlUtil.getRequestPath(request));
	}

	@Test
	public void testGetRequestPathWithContext() {
		when(
			_http.removePathParameters(
				"/c/portal/login;jsessionid=ACD311312312323BF.worker1")
		).thenReturn(
			"/c/portal/login"
		);

		MockHttpServletRequest request = new MockHttpServletRequest(
			HttpMethods.GET,
			"/portal/c/portal/login;jsessionid=ACD311312312323BF.worker1");

		request.setContextPath("/portal");

		Assert.assertEquals(
			"/c/portal/login", SamlUtil.getRequestPath(request));
	}

	@Test
	public void testGetRequestPathWithoutJsessionId() {
		when(
			_http.removePathParameters(
				"/c/portal/login")
		).thenReturn(
			"/c/portal/login"
		);

		MockHttpServletRequest request = new MockHttpServletRequest(
			HttpMethods.GET, "/c/portal/login");

		Assert.assertEquals(
			"/c/portal/login", SamlUtil.getRequestPath(request));
	}

	private Http _http;

}