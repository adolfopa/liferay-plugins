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

boolean hasEditPermission = KBArticlePermission.contains(permissionChecker, kbArticle, ActionKeys.UPDATE);
%>

<c:if test="<%= enableKBArticleRatings %>">

	<%
	int kbCommentsCount = 0;

	if (hasEditPermission) {
		kbCommentsCount = KBCommentLocalServiceUtil.getKBCommentsCount(KBArticle.class.getName(), kbArticle.getClassPK());
	}
	else {
		kbCommentsCount = KBCommentLocalServiceUtil.getKBCommentsCount(themeDisplay.getUserId(), KBArticle.class.getName(), kbArticle.getClassPK());
	}
	%>

	<liferay-ui:ratings
		className="<%= KBArticle.class.getName() %>"
		classPK="<%= kbArticle.getResourcePrimKey() %>"
		numberOfStars="<%= PortletPropsValues.KNOWLEDGE_BASE_RATINGS_NUMBER_OF_STARS %>"
		type="<%= kbArticleRatingsType %>"
	/>

	<c:if test='<%= kbArticleRatingsType.equals("thumbs") && themeDisplay.isSignedIn() %>'>
		<div class="kb-article-feedback-actions" id="<portlet:namespace />additionalFeedbackActionsContainer">
			<a data-show-node-id="<portlet:namespace />feedbackContainer" href="javascript:void(0)">
				<liferay-ui:message key="do-you-have-any-suggestions" />
			</a>

			<c:choose>
				<c:when test="<%= kbCommentsCount == 1 %>">
					|
					<a data-show-node-id="<portlet:namespace />previousCommentsContainer" href="javascript:void(0)">
						<c:choose>
							<c:when test="<%= hasEditPermission %>">
								<liferay-ui:message key="a-user-sent-one-suggestion-for-this-article" />
							</c:when>
							<c:otherwise>
								<liferay-ui:message key="you-sent-one-suggestion-for-this-article" />
							</c:otherwise>
						</c:choose>
					</a>
				</c:when>
				<c:when test="<%= kbCommentsCount > 1 %>">
					|
					<a data-show-node-id="<portlet:namespace />previousCommentsContainer" href="javascript:void(0)">
						<c:choose>
							<c:when test="<%= hasEditPermission %>">
								<liferay-ui:message arguments="<%= new Object[]{ kbCommentsCount } %>" key="users-sent-x-suggestions-for-this-article" />
							</c:when>
							<c:otherwise>
								<liferay-ui:message arguments="<%= new Object[]{ kbCommentsCount } %>" key="you-sent-x-suggestions-for-this-article" />
							</c:otherwise>
						</c:choose>
					</a>
				</c:when>
			</c:choose>
		</div>

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

						<aui:button name="cancelFeedback" value="cancel" />
					</aui:button-row>
				</aui:fieldset>
			</aui:form>
		</div>

		<%
		boolean expanded = ParamUtil.getBoolean(request, "expanded");
		%>

		<div class='kb-article-previous-comments <%= expanded ? "" : "hide" %>' id="<portlet:namespace />previousCommentsContainer">
			<c:if test="<%= kbCommentsCount > 0 %>">
				<liferay-portlet:renderURL varImpl="iteratorURL">
					<portlet:param name="expanded" value="<%= Boolean.TRUE.toString() %>" />
				</liferay-portlet:renderURL>

				<liferay-ui:search-container
					emptyResultsMessage="no-comments-found"
					iteratorURL="<%= iteratorURL %>"
					orderByComparator='<%= KnowledgeBaseUtil.getKBCommentOrderByComparator("modified-date", "desc") %>'
				>

					<%
					List<KBComment> kbComments = null;

					if (hasEditPermission) {
						kbComments = KBCommentLocalServiceUtil.getKBComments(KBArticle.class.getName(), kbArticle.getClassPK(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
					}
					else {
						kbComments = KBCommentLocalServiceUtil.getKBComments(themeDisplay.getUserId(), KBArticle.class.getName(), kbArticle.getClassPK(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
					}
					%>

					<liferay-ui:search-container-results
						results="<%= kbComments %>"
						total="<%= kbCommentsCount %>"
					/>

					<liferay-ui:search-container-row
						className="com.liferay.knowledgebase.model.KBComment"
						escapedModel="true"
						modelVar="kbComment"
					>

						<c:choose>
							<c:when test="<%= hasEditPermission %>">

								<%
								request.setAttribute("article_comment.jsp-kb_comment", kbComment);
								request.setAttribute(WebKeys.KNOWLEDGE_BASE_KB_ARTICLE, kbArticle);
								%>

								<liferay-util:include page="/admin/article_comment.jsp" servletContext="<%= application %>" />
							</c:when>
							<c:otherwise>
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
							</c:otherwise>
						</c:choose>
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
					var ratingThumb = A.one('.kb-article-container input[name="<portlet:namespace />ratingThumb"]');

					if (!ratingThumb) {
						return;
					}

					var helpful = this.one('#<portlet:namespace />helpful');

					helpful.val(ratingThumb.val() === 'up');
				}
			);

			A.one('#<portlet:namespace />additionalFeedbackActionsContainer').delegate(
				'click',
				function(event) {
					var showNode = A.one('#' + event.currentTarget.getData('show-node-id'));

					showNode.toggleView();

					var content = showNode.one('#content');

					if (content) {
						content.focus();
					}
				},
				'a'
			);

			A.one('#<portlet:namespace />cancelFeedback').on(
				'click',
				function(event) {
					this.each(
						function(node) {
							var container = node.ancestor('#<portlet:namespace />feedbackContainer');

							container.hide();

							var content = container.one('#content');

							if (content) {
								content.val('');
							}
						}
					);
				}
			);

			A.one('#<portlet:namespace />previousCommentsContainer').delegate(
				'click',
				function(e) {
					if (!confirm('<liferay-ui:message key="are-you-sure-you-want-to-delete-this" />')) {
						e.halt();
					}
				},
				'.kb-feedback-actions .kb-feedback-delete'
			);
		</aui:script>
	</c:if>
</c:if>