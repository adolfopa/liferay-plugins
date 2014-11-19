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

package com.liferay.vldap.server.directory.builder;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UniqueList;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.vldap.server.directory.FilterConstraint;
import com.liferay.vldap.server.directory.SearchBase;
import com.liferay.vldap.server.directory.ldap.CompanyDirectory;
import com.liferay.vldap.server.directory.ldap.Directory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonathan Potter
 * @author Brian Wing Shun Chan
 */
public class CompanyBuilder extends DirectoryBuilder {

	@Override
	public boolean isValidAttribute(String attributeId, String value) {
		if (StringUtil.equalsIgnoreCase(attributeId, "objectclass")) {
			if (StringUtil.equalsIgnoreCase(value, "organizationalUnit") ||
				StringUtil.equalsIgnoreCase(value, "top") ||
				StringUtil.equalsIgnoreCase(value, "*")) {

				return true;
			}
		}
		else if (StringUtil.equalsIgnoreCase(attributeId, "ou")) {
			return true;
		}

		return false;
	}

	@Override
	protected List<Directory> buildDirectories(
			SearchBase searchBase, List<FilterConstraint> filterConstraints)
		throws Exception {

		List<Directory> directories = new ArrayList<Directory>();

		List<Company> companies = getCompanies(searchBase, filterConstraints);

		for (Company company : companies) {
			Directory directory = new CompanyDirectory(
				searchBase.getTop(), company);

			directories.add(directory);
		}

		return directories;
	}

	protected List<Company> getCompanies(
			SearchBase searchBase, List<FilterConstraint> filterConstraints)
		throws Exception {

		if (filterConstraints == null) {
			return CompanyLocalServiceUtil.getCompanies();
		}

		List<Company> companies = new UniqueList<Company>();

		for (FilterConstraint filterConstraint : filterConstraints) {
			if (!isValidFilterConstraint(filterConstraint)) {
				continue;
			}

			String companyWebId = filterConstraint.getValue("ou");

			if (companyWebId == null) {
				companies.addAll(CompanyLocalServiceUtil.getCompanies(false));
			}
			else {
				Company company = CompanyLocalServiceUtil.getCompanyByWebId(
					companyWebId);

				companies.add(company);
			}
		}

		return companies;
	}

}