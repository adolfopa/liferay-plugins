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

package com.liferay.portal.workflow.kaleo.forms.hook.upgrade.v1_0_0;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Marcellus Tavares
 */
public class UpgradeKaleoProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select classPK, workflowDefinitionName, " +
					"workflowDefinitionVersion from WorkflowDefinitionLink " +
						"where classNameId = ?");

			long kaleoProcessClassNameId = PortalUtil.getClassNameId(
				KaleoProcess.class);

			ps.setLong(1, kaleoProcessClassNameId);

			rs = ps.executeQuery();

			while (rs.next()) {
				long kaleoProcessId = rs.getLong("classPK");
				String workflowDefinitioName = rs.getString(
					"workflowDefinitionName");
				int workflowDefinitionVersion = rs.getInt(
					"workflowDefinitionVersion");

				updateKaleoProcessWorkflowDefinition(
					kaleoProcessId, workflowDefinitioName,
					workflowDefinitionVersion);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	protected void updateKaleoProcessWorkflowDefinition(
			long kaleoProcessId, String workflowDefinitioName,
			int workflowDefinitionVersion)
		throws Exception {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"update KaleoProcess set workflowDefinitionName = ?, " +
					"workflowDefinitionVersion = ? where kaleoProcessId = ?");

			ps.setString(1, workflowDefinitioName);
			ps.setInt(2, workflowDefinitionVersion);
			ps.setLong(3, kaleoProcessId);

			ps.executeUpdate();
		}
		finally {
			DataAccess.cleanUp(con, ps);
		}
	}

}