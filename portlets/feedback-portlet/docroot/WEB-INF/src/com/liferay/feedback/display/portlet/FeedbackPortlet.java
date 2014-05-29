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

package com.liferay.feedback.display.portlet;

import com.liferay.compat.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.PortletPreferencesIds;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBCategoryLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

/**
 * @author Lin Cui
 */
public class FeedbackPortlet extends MVCPortlet {

	@Override
	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws PortletException {

		try {
			String actionName = ParamUtil.getString(
				actionRequest, ActionRequest.ACTION_NAME);

			if (actionName.equals("updateFeedback")) {
				updateFeedback(actionRequest, actionResponse);
			}
			else {
				super.processAction(actionRequest, actionResponse);
			}
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	protected List<ObjectValuePair<String, InputStream>> getInputStreamOVPs(
			UploadPortletRequest uploadPortletRequest)
		throws Exception {

		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			new ArrayList<ObjectValuePair<String, InputStream>>();

		try {
			String fileName = uploadPortletRequest.getFileName("attachment");

			if (Validator.isNull(fileName)) {
				return inputStreamOVPs;
			}

			File file = uploadPortletRequest.getFile("attachment");

			if (file == null) {
				return inputStreamOVPs;
			}

			byte[] bytes = FileUtil.getBytes(file);

			if ((bytes == null) || (bytes.length == 0)) {
				return inputStreamOVPs;
			}

			ByteArrayInputStream byteArrayInputStream =
				new ByteArrayInputStream(bytes);

			ObjectValuePair<String, InputStream> inputStreamOVP =
				new ObjectValuePair<String, InputStream>(
					fileName, byteArrayInputStream);

			inputStreamOVPs.add(inputStreamOVP);
		}
		finally {
			for (ObjectValuePair<String, InputStream> inputStreamOVP :
					inputStreamOVPs) {

				InputStream inputStream = inputStreamOVP.getValue();

				StreamUtil.cleanUp(inputStream);
			}
		}

		return inputStreamOVPs;
	}

	protected long getSubcategoryId(long mbCategoryId, String mbCategoryName)
		throws Exception {

		MBCategory mbCategory = MBCategoryLocalServiceUtil.getMBCategory(
			mbCategoryId);

		List<MBCategory> mbCategories =
			MBCategoryLocalServiceUtil.getCategories(
				mbCategory.getGroupId(), mbCategoryId,
				WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS);

		if (!mbCategories.isEmpty()) {
			for (MBCategory curMBCategory : mbCategories) {
				String curMBCategoryName = curMBCategory.getName();

				if (curMBCategoryName.equals(mbCategoryName)) {
					return curMBCategory.getCategoryId();
				}
			}
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		serviceContext.setScopeGroupId(mbCategory.getGroupId());
		serviceContext.setUuid(PortalUUIDUtil.generate());

		MBCategory subcategory = MBCategoryLocalServiceUtil.addCategory(
			mbCategory.getUserId(), mbCategory.getCategoryId(), mbCategoryName,
			StringPool.BLANK, serviceContext);

		return subcategory.getCategoryId();
	}

	protected void updateFeedback(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		try {
			UploadPortletRequest uploadPortletRequest =
				PortalUtil.getUploadPortletRequest(actionRequest);

			ThemeDisplay themeDisplay =
				(ThemeDisplay)uploadPortletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			User user = themeDisplay.getUser();

			long groupId = ParamUtil.getLong(uploadPortletRequest, "groupId");
			long mbCategoryId = ParamUtil.getLong(
				uploadPortletRequest, "mbCategoryId");
			String body = ParamUtil.getString(uploadPortletRequest, "body");

			String subject = StringUtil.shorten(body);

			boolean anonymous = ParamUtil.getBoolean(
				uploadPortletRequest, "anonymous");

			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			serviceContext.setAddGroupPermissions(true);

			long plid = PortalUtil.getPlidFromPortletId(
				groupId, true, PortletKeys.MESSAGE_BOARDS);

			if (plid == 0) {
				plid = PortalUtil.getPlidFromPortletId(
					groupId, false, PortletKeys.MESSAGE_BOARDS);
			}

			serviceContext.setPlid(plid);

			PortletPreferencesIds portletPreferencesIds =
				new PortletPreferencesIds(
					themeDisplay.getCompanyId(), groupId,
					PortletKeys.PREFS_OWNER_TYPE_GROUP, 0,
					PortletKeys.MESSAGE_BOARDS);

			serviceContext.setPortletPreferencesIds(portletPreferencesIds);

			String mbCategoryName = ParamUtil.getString(
				uploadPortletRequest, "mbCategoryName");

			List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
				getInputStreamOVPs(uploadPortletRequest);

			MBMessage mbMessage = MBMessageLocalServiceUtil.addMessage(
				user.getUserId(), user.getFullName(), groupId,
				getSubcategoryId(mbCategoryId, mbCategoryName), subject, body,
				"bbcode", inputStreamOVPs, anonymous, 0, false, serviceContext);

			MBThreadLocalServiceUtil.updateQuestion(
				mbMessage.getThreadId(), true);

			jsonObject.put("success", Boolean.TRUE.toString());
		}
		catch (Exception e) {
			jsonObject.put("success", Boolean.FALSE.toString());
		}

		writeJSON(actionRequest, actionResponse, jsonObject);
	}

}