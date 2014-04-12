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
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.PortletPreferencesIds;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.messageboards.model.MBMessage;
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

	protected void updateFeedback(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			new ArrayList<ObjectValuePair<String, InputStream>>();

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
			String type = ParamUtil.getString(uploadPortletRequest, "type");
			String body = ParamUtil.getString(uploadPortletRequest, "body");

			StringBundler sb = new StringBundler(5);

			sb.append(themeDisplay.translate(type));
			sb.append(StringPool.SPACE);
			sb.append(StringPool.DASH);
			sb.append(StringPool.SPACE);
			sb.append(StringUtil.shorten(body));

			String subject = sb.toString();

			boolean anonymous = ParamUtil.getBoolean(
				uploadPortletRequest, "anonymous");

			for (int i = 1; i < 3; i++) {
				String fileName = uploadPortletRequest.getFileName("file" + i);

				if (Validator.isNull(fileName)) {
					continue;
				}

				File file = uploadPortletRequest.getFile("file" + i);

				if (file == null) {
					continue;
				}

				byte[] bytes = FileUtil.getBytes(file);

				if ((bytes == null) || (bytes.length == 0)) {
					continue;
				}

				ByteArrayInputStream byteArrayInputStream =
					new ByteArrayInputStream(bytes);

				ObjectValuePair<String, InputStream> inputStreamOVP =
					new ObjectValuePair<String, InputStream>(
						fileName, byteArrayInputStream);

				inputStreamOVPs.add(inputStreamOVP);
			}

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

			MBMessage mbMessage = MBMessageLocalServiceUtil.addMessage(
				user.getUserId(), user.getFullName(), groupId, mbCategoryId,
				subject, body, "plain", inputStreamOVPs, anonymous, 0, false,
				serviceContext);

			MBThreadLocalServiceUtil.updateQuestion(
				mbMessage.getThreadId(), true);

			jsonObject.put("success", Boolean.TRUE.toString());
		}
		catch (Exception e) {
			jsonObject.put("success", Boolean.FALSE.toString());
		}
		finally {
			for (ObjectValuePair<String, InputStream> inputStreamOVP :
					inputStreamOVPs) {

				InputStream inputStream = inputStreamOVP.getValue();

				StreamUtil.cleanUp(inputStream);
			}
		}

		writeJSON(actionRequest, actionResponse, jsonObject);
	}

}