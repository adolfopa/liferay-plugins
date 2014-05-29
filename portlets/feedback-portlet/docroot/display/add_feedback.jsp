<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");
%>

<div id="<portlet:namespace />errorMessage"></div>

<portlet:actionURL name="updateFeedback" var="updateFeedbackURL" />

<aui:form action="<%= updateFeedbackURL %>" enctype="multipart/form-data" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="groupId" type="hidden" value="<%= String.valueOf(groupId) %>" />
	<aui:input name="mbCategoryId" type="hidden" value="<%= String.valueOf(mbCategoryId) %>" />
	<aui:input id="type" name="type" type="hidden" value="" />

	<div class="feedback-container">
		<div class="feedback" id="<portlet:namespace />feedback">
			<h4><span class="title"><liferay-ui:message key="i-would-like-to" /></span></h4>

			<aui:field-wrapper>
				<aui:input checked="<%= true %>" inlineLabel="right" label='<%= LanguageUtil.get(pageContext, "report-a-bug") %>' name="mbCategoryName" type="radio" value="Bugs" />

				<aui:input inlineLabel="right" label='<%= LanguageUtil.get(pageContext, "make-a-suggestion") %>' name="mbCategoryName" type="radio" value="Suggestions" />

				<aui:input inlineLabel="right" label='<%= LanguageUtil.get(pageContext, "leave-a-general-comment") %>' name="mbCategoryName" type="radio" value="General" />
			</aui:field-wrapper>

			<aui:input cssClass="body" id="body" label="" name="body" required="<%= true %>" type="textarea" />

			<aui:input id="anonymous" label="Anonymous" name="anonymous" type="checkbox" />

			<aui:input id="attachment" label="" name="attachment" type="file" />

			<aui:button cssClass="btn-primary send-feedback" value="send-feedback" />
		</div>

		<div class="confirmation hide" id="<portlet:namespace />confirmation">
			<h4>
				<liferay-ui:message key="your-feedback-has-been-submitted" />
			</h4>

			<p>
				<liferay-ui:message key="we-appreciate-your-time-and-value-your-feedback" />
			</p>
		</div>
	</div>
</aui:form>

<aui:script use="aui-base,aui-io-request-deprecated,aui-loading-mask-deprecated">
	var form = A.one('#<portlet:namespace />fm');

	var sendFeedback = form.one('.feedback-container .feedback .send-feedback');

	if (sendFeedback) {
		sendFeedback.on(
			'click',
			function(event) {
				var errorMessage = A.one('#<portlet:namespace />errorMessage');
				var feedback = A.one('#<portlet:namespace />feedback');
				var body = A.one('#<portlet:namespace />body');

				if (errorMessage) {
					if (body.val().trim().length == 0) {
						body.focus();

						return;
					}

					errorMessage.setHTML('');
				}

				var loadingMask = new A.LoadingMask(
					{
						'strings.loading': '<%= UnicodeLanguageUtil.get(pageContext, "sending-feedback") %>',
						target: A.one('.feedback-portlet .feedback-container')
					}
				);

				loadingMask.show();

				A.io.request(
					form.getAttribute('action'),
					{
						dataType: 'JSON',
						form: {
							id: form.getDOM(),
							upload: true
						},
						method: 'POST',
						on: {
							complete: function(event, id, obj) {
								var responseData = obj.responseText;

								var response = A.JSON.parse(responseData);

								if (response && (response.success == 'true')) {
									var confirmation = A.one('#<portlet:namespace />confirmation');

									if (feedback) {
										feedback.hide();
									}

									if (confirmation) {
										confirmation.show();
									}
								}
								else {
									if (errorMessage) {
										errorMessage.setHTML('<span class="alert alert-error"><liferay-ui:message key="your-feedback-was-not-saved-successfully" /></span>');
									}
								}

								loadingMask.hide();
							}
						}
					}
				);
			}
		);
	}
</aui:script>