package com.liferay.documentum.repository;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Ivan Zaera
 */
public interface DocumentumAction<T> {
	public T run(IDfSession iDfSession) throws DfException, PortalException;

}