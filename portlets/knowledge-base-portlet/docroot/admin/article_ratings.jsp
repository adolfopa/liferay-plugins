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

<%@ include file="/admin/init.jsp" %>

<%
KBArticle kbArticle = (KBArticle)request.getAttribute(WebKeys.KNOWLEDGE_BASE_KB_ARTICLE);
%>

<c:if test="<%= enableKBArticleRatings %>">

	<%
	int kbCommentsCount = KBCommentLocalServiceUtil.getKBCommentsCount(themeDisplay.getUserId(), KBArticle.class.getName(), kbArticle.getClassPK());
	%>

	<div id="<portlet:namespace />kbArticleRatingsContainer">
		<liferay-ui:ratings
			className="<%= KBArticle.class.getName() %>"
			classPK="<%= kbArticle.getResourcePrimKey() %>"
			numberOfStars="<%= PortletPropsValues.KNOWLEDGE_BASE_RATINGS_NUMBER_OF_STARS %>"
			type="<%= kbArticleRatingsType %>"
		/>

		<c:if test="<%= themeDisplay.isSignedIn() %>">
			<div class="kb-article-feedback-actions" id="<portlet:namespace />additionalFeedbackActionsContainer">
				<a data-show-node-id="<portlet:namespace />feedbackContainer" href="javascript:void(0)">
					<liferay-ui:message key="do-you-have-any-suggestions" />
				</a>

				<c:choose>
					<c:when test="<%= kbCommentsCount == 1 %>">
						| <a data-show-node-id="<portlet:namespace />previousCommentsContainer" href="javascript:void(0)">
							<liferay-ui:message key="you-have-sent-one-suggestion-for-this-article" />
						</a>
					</c:when>
					<c:when test="<%= kbCommentsCount > 1 %>">
						| <a data-show-node-id="<portlet:namespace />previousCommentsContainer" href="javascript:void(0)">
							<liferay-ui:message arguments="<%= new Object[]{ kbCommentsCount } %>" key="you-have-sent-x-suggestions-for-this-article" />
						</a>
					</c:when>
				</c:choose>
			</div>
		</c:if>
	</div>

	<c:if test='<%= kbArticleRatingsType.equals("thumbs") && themeDisplay.isSignedIn() %>'>
		<div class="hide kb-article-feedback" id="<portlet:namespace />feedbackContainer">
			<liferay-portlet:actionURL name="updateKBComment" var="updateKBCommentURL" />

			<aui:form action="<%= updateKBCommentURL %>" method="post" name="feedbackFm">
				<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD %>" />
				<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
				<aui:input name="classNameId" type="hidden" value="<%= PortalUtil.getClassNameId(KBArticle.class) %>" />
				<aui:input name="classPK" type="hidden" value="<%= kbArticle.getResourcePrimKey() %>" />

				<liferay-ui:error exception="<%= KBCommentContentException.class %>" message="please-enter-valid-content" />

				<aui:model-context model="<%= KBComment.class %>" />

				<aui:fieldset>
					<span class="kb-helpful-text">
						<liferay-ui:message key="what-did-you-like-the-most-what-would-you-improve" />
					</span>

					<aui:input name="helpful" type="hidden" value="0" />

					<aui:input label="" name="content" />

					<aui:button-row cssClass="kb-submit-buttons">
						<aui:button type="submit" value="submit" />
						<aui:button cssClass="kb-article-feedback-cancel-button" value="cancel" />
					</aui:button-row>
				</aui:fieldset>
			</aui:form>
		</div>

		<%
		boolean paginating = GetterUtil.getBoolean(ParamUtil.getBoolean(request, "paginating"), false);
		%>

		<div class='kb-article-previous-comments <%= paginating ? "" : "hide" %>' id="<portlet:namespace />previousCommentsContainer">
			<c:if test="<%= kbCommentsCount > 0 %>">
				<liferay-portlet:renderURL varImpl="iteratorURL">
					<portlet:param name="paginating" value="true" />
				</liferay-portlet:renderURL>

				<liferay-ui:search-container
						emptyResultsMessage="no-comments-found"
						iteratorURL="<%= iteratorURL %>"
				>

					<liferay-ui:search-container-results
							results="<%= KBCommentLocalServiceUtil.getKBComments(themeDisplay.getUserId(), KBArticle.class.getName(), kbArticle.getClassPK(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator()) %>"
							total="<%= kbCommentsCount %>"
					/>

					<liferay-ui:search-container-row
							className="com.liferay.knowledgebase.model.KBComment"
							escapedModel="true"
							modelVar="kbComment"
					>
						<div class="kb-article-comment">
							<p class="kb-article-comment-body">
								<%= kbComment.getContent() %>
							</p>

							<div class="kb-article-comment-post-date">
								<i class="icon-calendar"></i>

								<%
								DateSearchEntry dateSearchEntry = new DateSearchEntry();

								dateSearchEntry.setDate(kbComment.getModifiedDate());
								%>

								<%= dateSearchEntry.getName(pageContext) %>
							</div>
						</div>
					</liferay-ui:search-container-row>

					<liferay-ui:search-iterator />
				</liferay-ui:search-container>
			</c:if>
		</div>

		<aui:script use="aui-base">
			var feedbackFm = A.one('#<portlet:namespace />feedbackFm');

			feedbackFm.on(
				'submit',
				function(event) {
					var helpful = this.one('#<portlet:namespace />helpful');

					helpful.val(thumbUp.hasClass('rating-on'));
				}
			);

			var additionalFeedbackActions = A.all('#<portlet:namespace />additionalFeedbackActionsContainer a');

			additionalFeedbackActions.each(
				function(node) {
					var nodeToShow = A.one('#' + node.getData('show-node-id'));

					node.on(
						'click',
						function(event) {
							nodeToShow.toggleView();

							var textArea = nodeToShow.one('textarea');

							if (textArea) {
								textArea.focus();
							}
						}
					);
				}
			);

			var cancelButtons = A.all('.kb-article-feedback-cancel-button');

			cancelButtons.on(
				'click',
				function(event) {
					this.each(
						function(node) {
							var container = node.ancestor('.kb-article-feedback');

							container.hide();
							container.all('textarea').val('');
						}
					);
				}
			);
		</aui:script>
	</c:if>
</c:if>