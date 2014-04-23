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
		<div class="start" id="<portlet:namespace />start">
			<h2><liferay-ui:message key="social-office-is" /></h2>

			<aui:button cssClass="btn btn-success feedback-positive" icon="icon-thumbs-up" value="positive" />

			<aui:button cssClass="btn btn-danger feedback-negative" icon="icon-thumbs-down" value="negative" />
		</div>

		<div class="feedback hide" id="<portlet:namespace />feedback">
			<h3><span class="title"></span></h3>

			<p class="subject"></p>

			<aui:input cssClass="body" id="body" label="" name="body" required="<%= true %>" type="textarea" />

			<div class="attachments">
				<liferay-ui:panel defaultState="closed" extended="<%= false %>" id="attachmentsPanel" persistState="<%= false %>" title="attachments">
					<div>
						<aui:input id="file1" label="" name="file1" size="70" type="file" />
					</div>
					<div>
						<aui:input id="file2" label="" name="file2" size="70" type="file" />
					</div>
				</liferay-ui:panel>
			</div>

			<aui:input id="anonymous" label="Anonymous" name="anonymous" type="checkbox" />

			<aui:button cssClass="btn btn-primary send-feedback" value="send-feedback" />
		</div>

		<div class="confirmation hide" id="<portlet:namespace />confirmation">
			<h3>
				<liferay-ui:message key="your-feedback-has-been-submitted" />
			</h3>

			<p>
				<liferay-ui:message key="we-appreciate-your-time-and-value-your-feedback" />
			</p>
		</div>
	</div>
</aui:form>

<aui:script use="aui-base,aui-io-request-deprecated,aui-loading-mask-deprecated">
	var form = A.one('#<portlet:namespace />fm');

	var displayFeedBack = function(feedbackType) {
		var title = form.one('.feedback-container .feedback .title');
		var subject = form.one('.feedback-container .feedback .subject');
		var attachments = form.one('.feedback-container .feedback .attachments');

		if (feedbackType == '<%= FeedbackConstant.TYPE_POSITIVE %>') {
			title.setHTML('<liferay-ui:message key="we-are-glad-you-like-it" />');
			subject.setHTML('<%= FeedbackUtil.getFeedbackSubject(FeedbackConstant.TYPE_POSITIVE) %>');

			if (!attachments.hasClass("hide")) {
				attachments.addClass("hide");
			}
		}
		else if (feedbackType == '<%= FeedbackConstant.TYPE_NEGATIVE %>') {
			title.setHTML('<liferay-ui:message key="help-us-fix-it" />');
			subject.setHTML('<%= FeedbackUtil.getFeedbackSubject(FeedbackConstant.TYPE_NEGATIVE) %>');

			if (attachments.hasClass("hide")) {
				attachments.removeClass("hide");
			}
		}

		var type = form.one('#<portlet:namespace />type');

		type.val(feedbackType);

		var start = form.one('#<portlet:namespace />start');

		if (start) {
			start.hide();
		}

		var feedback = form.one('#<portlet:namespace />feedback');

		if (feedback) {
			feedback.show();

			var body = form.one('#<portlet:namespace />body');

			body.focus();
		}
	}

	var feedbackNegative = form.one('.feedback-container .start .feedback-negative');

	if (feedbackNegative) {
		feedbackNegative.on(
			'click',
			function(event) {
				displayFeedBack('<%= FeedbackConstant.TYPE_NEGATIVE %>');
			}
		);
	}

	var feedbackPositive = form.one('.feedback-container .start .feedback-positive');

	if (feedbackPositive) {
		feedbackPositive.on(
			'click',
			function(event) {
				displayFeedBack('<%= FeedbackConstant.TYPE_POSITIVE %>');
			}
		);
	}

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