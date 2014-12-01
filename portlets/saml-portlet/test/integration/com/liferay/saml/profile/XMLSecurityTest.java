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

package com.liferay.saml.profile;

import com.liferay.saml.BaseSamlTestCase;
import com.liferay.saml.SamlSsoRequestContext;
import com.liferay.saml.metadata.MetadataManagerUtil;
import com.liferay.saml.model.SamlSpIdpConnection;
import com.liferay.saml.model.impl.SamlSpIdpConnectionImpl;
import com.liferay.saml.service.SamlSpAuthRequestLocalService;
import com.liferay.saml.service.SamlSpAuthRequestLocalServiceUtil;
import com.liferay.saml.service.SamlSpIdpConnectionLocalService;
import com.liferay.saml.service.SamlSpIdpConnectionLocalServiceUtil;
import com.liferay.saml.util.OpenSamlUtil;

import java.io.ByteArrayOutputStream;

import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.xml.security.SigningUtil;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.util.Base64;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
public class XMLSecurityTest extends BaseSamlTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		SamlSpAuthRequestLocalService samlSpAuthRequestLocalService =
			getMockPortletService(
				SamlSpAuthRequestLocalServiceUtil.class,
				SamlSpAuthRequestLocalService.class);

		_webSsoProfileImpl.setIdentifierGenerator(identifierGenerator);
		_webSsoProfileImpl.setSamlBindings(samlBindings);

		prepareServiceProvider(SP_ENTITY_ID);
	}

	@Test(expected = MessageDecodingException.class)
	public void testXMLBombBillionLaughs() throws Exception {
		String redirectURL = getAuthnRequestRedirectURL();
		AuthnRequest authnRequest = getAuthnRequest(redirectURL);

		String authnRequestXml = OpenSamlUtil.marshall(authnRequest);

		String samlMessageXml = authnRequestXml.substring(38);

		authnRequestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
			"<!DOCTYPE saml2p:AuthnRequest [\n"+
			" <!ENTITY lol1 \"lol\">\n";

		for (int i = 2; i < 10; i++) {
			String lol = "";

			for (int j = 0; j < 10; j++) {
				lol += "&lol"+(i-1) + ";";
			}

			authnRequestXml =
				authnRequestXml + " <!ENTITY lol" + i +" \"" + lol + "\">\n";
		}

		authnRequestXml += "]>" + samlMessageXml;

		authnRequestXml = authnRequestXml.substring(
			0, authnRequestXml.length() - 22) + "&lol9;</saml2p:AuthnRequest>";

		decodeAuthnRequest(authnRequestXml, redirectURL);
	}

	@Test(expected = MessageDecodingException.class)
	public void testXMLBombQuadraticBlowup() throws Exception {
		String redirectURL = getAuthnRequestRedirectURL();
		AuthnRequest authnRequest = getAuthnRequest(redirectURL);

		String authnRequestXml = OpenSamlUtil.marshall(authnRequest);

		String samlMessageXml = authnRequestXml.substring(38);

		authnRequestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
			"<!DOCTYPE saml2p:AuthnRequest [\n"+
			" <!ENTITY a \"";

		for (int i = 0; i < 5000; i++) {
			authnRequestXml += "aaaaaaaaaa";
		}

		authnRequestXml = authnRequestXml + "\">\n";

		authnRequestXml += "]>" + samlMessageXml;

		String entity = "";

		for (int i = 0; i < 5000; i++) {
			entity += "&a;&a;&a;&a;&a;&a;&a;&a;&a;&a;";
		}

		authnRequestXml = authnRequestXml.substring(
			0, authnRequestXml.length() - 22) + entity +
				"</saml2p:AuthnRequest>";

		decodeAuthnRequest(authnRequestXml, redirectURL);
	}

	@Test(expected = MessageDecodingException.class)
	public void testXXEGeneralEntities() throws Exception {
		String redirectURL = getAuthnRequestRedirectURL();
		AuthnRequest authnRequest = getAuthnRequest(redirectURL);

		String authnRequestXml = OpenSamlUtil.marshall(authnRequest);

		authnRequestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
			"<!DOCTYPE saml2p:AuthnRequest [\n"+
			"	<!ENTITY xxe SYSTEM \"http://localhost/saml-request\">\n"+
			"]>" + authnRequestXml.substring(38);

		authnRequestXml = authnRequestXml.substring(
			0, authnRequestXml.length() - 22) + "&xxe;</saml2p:AuthnRequest>";

		decodeAuthnRequest(authnRequestXml, redirectURL);
	}

	@Test(expected = MessageDecodingException.class)
	public void testXXEGeneralEntities2() throws Exception {
		String redirectURL = getAuthnRequestRedirectURL();
		AuthnRequest authnRequest = getAuthnRequest(redirectURL);

		String authnRequestXml = OpenSamlUtil.marshall(authnRequest);

		authnRequestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
			"<!DOCTYPE saml2p:AuthnRequest [\n"+
			"	<!ENTITY xxe PUBLIC \"SOME//PUBLIC//ID\" " +
			"		\"http://localhost/saml-request\">\n"+
			"]>" + authnRequestXml.substring(38);

		authnRequestXml = authnRequestXml.substring(
			0, authnRequestXml.length() - 22) + "&xxe;</saml2p:AuthnRequest>";

		decodeAuthnRequest(authnRequestXml, redirectURL);
	}

	@Test(expected = MessageDecodingException.class)
	public void testXXEParameterEntities() throws Exception {
		String redirectURL = getAuthnRequestRedirectURL();
		AuthnRequest authnRequest = getAuthnRequest(redirectURL);

		String authnRequestXml = OpenSamlUtil.marshall(authnRequest);

		authnRequestXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
			"<!DOCTYPE saml2p:AuthnRequest [\n"+
			"	<!ENTITY % remote SYSTEM \"http://localhost/saml-request\">\n"+
			" %remote;\n"+
			"]>" + authnRequestXml.substring(38);

		decodeAuthnRequest(authnRequestXml, redirectURL);
	}

	protected void decodeAuthnRequest(
			String authnRequestXml, String redirectURL)
		throws Exception {

		String encodedAuthnRequest = encodeRequest(authnRequestXml);

		MockHttpServletRequest mockHttpServletRequest =
			getMockHttpServletRequest(redirectURL);

		mockHttpServletRequest.removeParameter("SAMLRequest");
		mockHttpServletRequest.removeParameter("Signature");

		mockHttpServletRequest.setParameter("SAMLRequest", encodedAuthnRequest);

		Credential credential = MetadataManagerUtil.getSigningCredential();

		String signature = generateSignature(
			credential, mockHttpServletRequest.getParameter("SigAlg"),
			mockHttpServletRequest.getQueryString());

		mockHttpServletRequest.setParameter("Signature", signature);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_webSsoProfileImpl.decodeAuthnRequest(
			mockHttpServletRequest, mockHttpServletResponse);
	}

	protected String encodeRequest(String requestXml) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Deflater deflater = new Deflater(Deflater.DEFLATED, true);

		DeflaterOutputStream deflaterStream = new DeflaterOutputStream(
			baos, deflater);
		deflaterStream.write(requestXml.getBytes("UTF-8"));
		deflaterStream.finish();

		return Base64.encodeBytes(baos.toByteArray(), Base64.DONT_BREAK_LINES);
	}

	protected String generateSignature(
			Credential signingCredential, String algorithmURI,
			String queryString)
		throws Exception {

		byte[] rawSignature = SigningUtil.signWithURI(
			signingCredential, algorithmURI, queryString.getBytes("UTF-8"));

		return Base64.encodeBytes(rawSignature, Base64.DONT_BREAK_LINES);
	}

	protected AuthnRequest getAuthnRequest(String redirectURL)
		throws Exception {

		MockHttpServletRequest mockHttpServletRequest =
			getMockHttpServletRequest(redirectURL);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		SamlSsoRequestContext samlSsoRequestContext =
			_webSsoProfileImpl.decodeAuthnRequest(
				mockHttpServletRequest, mockHttpServletResponse);

		SAMLMessageContext<AuthnRequest, Response, NameID> samlMessageContext =
			samlSsoRequestContext.getSAMLMessageContext();

		return samlMessageContext.getInboundSAMLMessage();
	}

	protected String getAuthnRequestRedirectURL() throws Exception {
		SamlSpIdpConnectionLocalService samlSpIdpConnectionLocalService =
			getMockPortletService(
				SamlSpIdpConnectionLocalServiceUtil.class,
				SamlSpIdpConnectionLocalService.class);

		SamlSpIdpConnection samlSpIdpConnection = new SamlSpIdpConnectionImpl();

		when(
			samlSpIdpConnectionLocalService.getSamlSpIdpConnection(
				Mockito.eq(COMPANY_ID), Mockito.eq(IDP_ENTITY_ID))
		).thenReturn(
			samlSpIdpConnection
		);

		MockHttpServletRequest mockHttpServletRequest =
			getMockHttpServletRequest(LOGIN_URL);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_webSsoProfileImpl.doSendAuthnRequest(
			mockHttpServletRequest, mockHttpServletResponse, RELAY_STATE);

		return mockHttpServletResponse.getRedirectedUrl();
	}

	private WebSsoProfileImpl _webSsoProfileImpl = new WebSsoProfileImpl();

}