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

package com.liferay.portal.workflow.kaleo.forms.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.workflow.kaleo.forms.model.KaleoProcessLink;
import com.liferay.portal.workflow.kaleo.forms.service.base.KaleoProcessLinkLocalServiceBaseImpl;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class KaleoProcessLinkLocalServiceImpl
	extends KaleoProcessLinkLocalServiceBaseImpl {

	public KaleoProcessLink addKaleoProcessLink(
		long kaleoProcessId, String workflowTaskName, long ddmTemplateId) {

		long kaleoProcessLinkId = counterLocalService.increment();

		KaleoProcessLink kaleoProcessLink = kaleoProcessLinkPersistence.create(
			kaleoProcessLinkId);

		kaleoProcessLink.setKaleoProcessId(kaleoProcessId);
		kaleoProcessLink.setWorkflowTaskName(workflowTaskName);
		kaleoProcessLink.setDDMTemplateId(ddmTemplateId);

		kaleoProcessLinkPersistence.update(kaleoProcessLink);

		return kaleoProcessLink;
	}

	public void deleteKaleoProcessLinks(long kaleoProcessId) {
		List<KaleoProcessLink> kaleoProcessLinks =
			kaleoProcessLinkPersistence.findByKaleoProcessId(kaleoProcessId);

		for (KaleoProcessLink kaleoProcessLink : kaleoProcessLinks) {
			deleteKaleoProcessLink(kaleoProcessLink);
		}
	}

	public KaleoProcessLink fetchKaleoProcessLink(
		long kaleoProcessId, String workflowTaskName) {

		return kaleoProcessLinkPersistence.fetchByKPI_WTN(
			kaleoProcessId, workflowTaskName);
	}

	public List<KaleoProcessLink> getKaleoProcessLinks(long kaleoProcessId) {
		return kaleoProcessLinkPersistence.findByKaleoProcessId(kaleoProcessId);
	}

	public KaleoProcessLink updateKaleoProcessLink(
			long kaleoProcessLinkId, long kaleoProcessId)
		throws PortalException {

		KaleoProcessLink kaleoProcessLink =
			kaleoProcessLinkPersistence.findByPrimaryKey(kaleoProcessLinkId);

		kaleoProcessLink.setKaleoProcessId(kaleoProcessId);

		kaleoProcessLinkPersistence.update(kaleoProcessLink);

		return kaleoProcessLink;
	}

	public KaleoProcessLink updateKaleoProcessLink(
			long kaleoProcessLinkId, long kaleoProcessId,
			String workflowTaskName, long ddmTemplateId)
		throws PortalException {

		KaleoProcessLink kaleoProcessLink =
			kaleoProcessLinkPersistence.findByPrimaryKey(kaleoProcessLinkId);

		kaleoProcessLink.setKaleoProcessId(kaleoProcessId);
		kaleoProcessLink.setWorkflowTaskName(workflowTaskName);
		kaleoProcessLink.setDDMTemplateId(ddmTemplateId);

		kaleoProcessLinkPersistence.update(kaleoProcessLink);

		return kaleoProcessLink;
	}

	public KaleoProcessLink updateKaleoProcessLink(
		long kaleoProcessId, String workflowTaskName, long ddmTemplateId) {

		KaleoProcessLink kaleoProcessLink =
			kaleoProcessLinkPersistence.fetchByKPI_WTN(
				kaleoProcessId, workflowTaskName);

		if (kaleoProcessLink == null) {
			return addKaleoProcessLink(
				kaleoProcessId, workflowTaskName, ddmTemplateId);
		}

		kaleoProcessLink.setDDMTemplateId(ddmTemplateId);

		kaleoProcessLinkPersistence.update(kaleoProcessLink);

		return kaleoProcessLink;
	}

}