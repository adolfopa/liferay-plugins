/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This file is part of Liferay Social Office. Liferay Social Office is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Liferay Social Office is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Liferay Social Office. If not, see http://www.gnu.org/licenses/agpl-3.0.html.
 */

package com.liferay.so.hook.upgrade.v3_0_0;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.LayoutTemplate;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.persistence.LayoutActionableDynamicQuery;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.so.service.SocialOfficeServiceUtil;
import com.liferay.so.util.LayoutSetPrototypeUtil;
import com.liferay.so.util.LayoutUtil;
import com.liferay.so.util.PortletKeys;
import com.liferay.so.util.SocialOfficeConstants;

/**
 * @author Matthew Kong
 */
public class UpgradeLayout extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(null);

			rs = ps.executeQuery();

			while (rs.next()) {

			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}

		for (long companyId : PortalUtil.getCompanyIds()) {
			updateSOAnnouncements(companyId);
		}
	}

	protected String getJoinSQL() {
		StringBuilder sb = new StringBuilder();

		sb.append("inner join ExpandoValue on ");
		sb.append("((ExpandoValue.classPK = Layout.groupId) and ");
		sb.append("(ExpandoValue.data_ = 'true')) ");

		sb.append("inner join ExpandoColumn on ");
		sb.append("((ExpandoColumn.columnId = ExpandoValue.columnId) and ");
		sb.append("(ExpandoColumn.name = 'socialOfficeEnabled')) ");

		sb.append("inner join ExpandoTable on ");
		sb.append("((ExpandoTable.tableId = ExpandoValue.tableId) and ");
		sb.append("(ExpandoTable.name = '");
		sb.append(ExpandoTableConstants.DEFAULT_TABLE_NAME);
		sb.append("')) ");

		sb.append("inner join Group_ on ");
		sb.append("((Group_.groupId == Layout.groupId) and ");
		sb.append("((Layout.privateLayout = true) or ");
		sb.append("((Layout.privateLayout = false) and ");
		sb.append("(Group_.classNameId != ");
		sb.append(PortalUtil.getClassNameId(User.class));
		sb.append("))))");

		return sb.toString();
	}

	protected void updateSOAnnouncements(final long companyId)
		throws Exception {

		ActionableDynamicQuery actionableDynamicQuery =
			new LayoutActionableDynamicQuery() {

			@Override
			protected void performAction(Object object) throws PortalException {
				Layout layout = (Layout)object;

				if (!SocialOfficeServiceUtil.isSocialOfficeGroup(
						layout.getGroupId())) {

					return;
				}

				Group group = GroupLocalServiceUtil.fetchGroup(
					layout.getGroupId());

				if (layout.isPublicLayout() && group.isUser()) {
					return;
				}

				LayoutTypePortlet layoutTypePortlet =
					(LayoutTypePortlet)layout.getLayoutType();

				if (layoutTypePortlet.hasPortletId(
						PortletKeys.SO_ANNOUNCEMENTS)) {

					return;
				}

				UnicodeProperties typeSettingsProperties =
					layout.getTypeSettingsProperties();

				if (layoutTypePortlet.hasPortletId(PortletKeys.ANNOUNCEMENTS)) {
					LayoutTemplate layoutTemplate =
						layoutTypePortlet.getLayoutTemplate();

					for (String columnName : layoutTemplate.getColumns()) {
						String columnValue = typeSettingsProperties.getProperty(
							columnName);

						columnValue = StringUtil.replace(
							columnValue, PortletKeys.ANNOUNCEMENTS,
							PortletKeys.SO_ANNOUNCEMENTS);

						typeSettingsProperties.setProperty(
							columnName, columnValue);
					}

					layout.setTypeSettingsProperties(typeSettingsProperties);
				}
				else {
					if (layout.getPriority() != 0) {
						return;
					}

					if (layout.getGroupId() == _layoutSetPrototypeGroupId) {
						return;
					}

					String columnValue = typeSettingsProperties.getProperty(
						"column-1");

					if (Validator.isNull(columnValue)) {
						return;
					}

					int columnPos = 0;

					if (StringUtil.contains(
							columnValue,
							PortletKeys.MICROBLOGS_STATUS_UPDATE)) {

						columnPos = 1;
					}

					layoutTypePortlet.addPortletId(
						0, PortletKeys.SO_ANNOUNCEMENTS, "column-1", columnPos,
						false);

					layout = layoutTypePortlet.getLayout();
				}

				LayoutLocalServiceUtil.updateLayout(layout);

				LayoutUtil.addResources(layout, PortletKeys.SO_ANNOUNCEMENTS);
			}

			protected long getLayoutSetPrototypeGroupId(
					long companyId, String layoutSetPrototypeKey)
				throws Exception {

				LayoutSetPrototype layoutSetPrototype =
					LayoutSetPrototypeUtil.fetchLayoutSetPrototype(
						companyId, layoutSetPrototypeKey);

				if (layoutSetPrototype != null) {
					return layoutSetPrototype.getGroupId();
				}

				return 0;
			}

			private long _layoutSetPrototypeGroupId =
				getLayoutSetPrototypeGroupId(
					companyId,
					SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY_USER_PUBLIC);

		};

		actionableDynamicQuery.setCompanyId(companyId);

		actionableDynamicQuery.performActions();
	}

}