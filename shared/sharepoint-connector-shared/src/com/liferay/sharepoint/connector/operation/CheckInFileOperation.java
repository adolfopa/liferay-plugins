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

package com.liferay.sharepoint.connector.operation;

import com.liferay.sharepoint.connector.SharepointConnection.CheckInType;
import com.liferay.sharepoint.connector.SharepointException;

import java.net.URL;

import java.rmi.RemoteException;

/**
 * @author Iván Zaera
 */
public class CheckInFileOperation extends BaseOperation {

	public boolean execute(
			String filePath, String comment, CheckInType checkInType)
		throws SharepointException {

		try {
			URL filePathURL = toURL(filePath);

			String protocolValue = String.valueOf(
				checkInType.getProtocolValue());

			return listsSoap.checkInFile(
				filePathURL.toString(), comment, protocolValue);
		}
		catch (RemoteException re) {
			throw new SharepointException(
				"Unable to communicate with the Sharepoint server", re);
		}
	}

}