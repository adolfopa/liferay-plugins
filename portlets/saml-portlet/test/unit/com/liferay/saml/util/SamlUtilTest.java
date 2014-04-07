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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.powermock.api.mockito.PowerMockito;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Mika Koivisto
 */
public class SamlUtilTest extends PowerMockito {

	@Before
	public void setUp() {
		_http = mock(Http.class);

		HttpUtil httpUtil = new HttpUtil();

		httpUtil.setHttp(_http);
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