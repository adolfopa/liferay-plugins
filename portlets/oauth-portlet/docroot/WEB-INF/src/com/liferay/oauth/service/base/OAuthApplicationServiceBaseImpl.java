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

package com.liferay.oauth.service.base;

import com.liferay.oauth.model.OAuthApplication;
import com.liferay.oauth.service.OAuthApplicationService;
import com.liferay.oauth.service.persistence.OAuthApplicationPersistence;
import com.liferay.oauth.service.persistence.OAuthUserPersistence;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.BaseServiceImpl;
import com.liferay.portal.service.persistence.ClassNamePersistence;
import com.liferay.portal.service.persistence.ImagePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the o auth application remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.oauth.service.impl.OAuthApplicationServiceImpl}.
 * </p>
 *
 * @author Ivica Cardic
 * @see com.liferay.oauth.service.impl.OAuthApplicationServiceImpl
 * @see com.liferay.oauth.service.OAuthApplicationServiceUtil
 * @generated
 */
public abstract class OAuthApplicationServiceBaseImpl extends BaseServiceImpl
	implements OAuthApplicationService, IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.liferay.oauth.service.OAuthApplicationServiceUtil} to access the o auth application remote service.
	 */

	/**
	 * Returns the o auth application local service.
	 *
	 * @return the o auth application local service
	 */
	public com.liferay.oauth.service.OAuthApplicationLocalService getOAuthApplicationLocalService() {
		return oAuthApplicationLocalService;
	}

	/**
	 * Sets the o auth application local service.
	 *
	 * @param oAuthApplicationLocalService the o auth application local service
	 */
	public void setOAuthApplicationLocalService(
		com.liferay.oauth.service.OAuthApplicationLocalService oAuthApplicationLocalService) {
		this.oAuthApplicationLocalService = oAuthApplicationLocalService;
	}

	/**
	 * Returns the o auth application remote service.
	 *
	 * @return the o auth application remote service
	 */
	public com.liferay.oauth.service.OAuthApplicationService getOAuthApplicationService() {
		return oAuthApplicationService;
	}

	/**
	 * Sets the o auth application remote service.
	 *
	 * @param oAuthApplicationService the o auth application remote service
	 */
	public void setOAuthApplicationService(
		com.liferay.oauth.service.OAuthApplicationService oAuthApplicationService) {
		this.oAuthApplicationService = oAuthApplicationService;
	}

	/**
	 * Returns the o auth application persistence.
	 *
	 * @return the o auth application persistence
	 */
	public OAuthApplicationPersistence getOAuthApplicationPersistence() {
		return oAuthApplicationPersistence;
	}

	/**
	 * Sets the o auth application persistence.
	 *
	 * @param oAuthApplicationPersistence the o auth application persistence
	 */
	public void setOAuthApplicationPersistence(
		OAuthApplicationPersistence oAuthApplicationPersistence) {
		this.oAuthApplicationPersistence = oAuthApplicationPersistence;
	}

	/**
	 * Returns the o auth user local service.
	 *
	 * @return the o auth user local service
	 */
	public com.liferay.oauth.service.OAuthUserLocalService getOAuthUserLocalService() {
		return oAuthUserLocalService;
	}

	/**
	 * Sets the o auth user local service.
	 *
	 * @param oAuthUserLocalService the o auth user local service
	 */
	public void setOAuthUserLocalService(
		com.liferay.oauth.service.OAuthUserLocalService oAuthUserLocalService) {
		this.oAuthUserLocalService = oAuthUserLocalService;
	}

	/**
	 * Returns the o auth user remote service.
	 *
	 * @return the o auth user remote service
	 */
	public com.liferay.oauth.service.OAuthUserService getOAuthUserService() {
		return oAuthUserService;
	}

	/**
	 * Sets the o auth user remote service.
	 *
	 * @param oAuthUserService the o auth user remote service
	 */
	public void setOAuthUserService(
		com.liferay.oauth.service.OAuthUserService oAuthUserService) {
		this.oAuthUserService = oAuthUserService;
	}

	/**
	 * Returns the o auth user persistence.
	 *
	 * @return the o auth user persistence
	 */
	public OAuthUserPersistence getOAuthUserPersistence() {
		return oAuthUserPersistence;
	}

	/**
	 * Sets the o auth user persistence.
	 *
	 * @param oAuthUserPersistence the o auth user persistence
	 */
	public void setOAuthUserPersistence(
		OAuthUserPersistence oAuthUserPersistence) {
		this.oAuthUserPersistence = oAuthUserPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.service.ClassNameLocalService getClassNameLocalService() {
		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.service.ClassNameLocalService classNameLocalService) {
		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name remote service.
	 *
	 * @return the class name remote service
	 */
	public com.liferay.portal.service.ClassNameService getClassNameService() {
		return classNameService;
	}

	/**
	 * Sets the class name remote service.
	 *
	 * @param classNameService the class name remote service
	 */
	public void setClassNameService(
		com.liferay.portal.service.ClassNameService classNameService) {
		this.classNameService = classNameService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {
		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the image local service.
	 *
	 * @return the image local service
	 */
	public com.liferay.portal.service.ImageLocalService getImageLocalService() {
		return imageLocalService;
	}

	/**
	 * Sets the image local service.
	 *
	 * @param imageLocalService the image local service
	 */
	public void setImageLocalService(
		com.liferay.portal.service.ImageLocalService imageLocalService) {
		this.imageLocalService = imageLocalService;
	}

	/**
	 * Returns the image remote service.
	 *
	 * @return the image remote service
	 */
	public com.liferay.portal.service.ImageService getImageService() {
		return imageService;
	}

	/**
	 * Sets the image remote service.
	 *
	 * @param imageService the image remote service
	 */
	public void setImageService(
		com.liferay.portal.service.ImageService imageService) {
		this.imageService = imageService;
	}

	/**
	 * Returns the image persistence.
	 *
	 * @return the image persistence
	 */
	public ImagePersistence getImagePersistence() {
		return imagePersistence;
	}

	/**
	 * Sets the image persistence.
	 *
	 * @param imagePersistence the image persistence
	 */
	public void setImagePersistence(ImagePersistence imagePersistence) {
		this.imagePersistence = imagePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.service.UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		Class<?> clazz = getClass();

		_classLoader = clazz.getClassLoader();
	}

	public void destroy() {
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	@Override
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	@Override
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	@Override
	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		if (contextClassLoader != _classLoader) {
			currentThread.setContextClassLoader(_classLoader);
		}

		try {
			return _clpInvoker.invokeMethod(name, parameterTypes, arguments);
		}
		finally {
			if (contextClassLoader != _classLoader) {
				currentThread.setContextClassLoader(contextClassLoader);
			}
		}
	}

	protected Class<?> getModelClass() {
		return OAuthApplication.class;
	}

	protected String getModelClassName() {
		return OAuthApplication.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = oAuthApplicationPersistence.getDataSource();

			DB db = DBFactoryUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = com.liferay.oauth.service.OAuthApplicationLocalService.class)
	protected com.liferay.oauth.service.OAuthApplicationLocalService oAuthApplicationLocalService;
	@BeanReference(type = com.liferay.oauth.service.OAuthApplicationService.class)
	protected com.liferay.oauth.service.OAuthApplicationService oAuthApplicationService;
	@BeanReference(type = OAuthApplicationPersistence.class)
	protected OAuthApplicationPersistence oAuthApplicationPersistence;
	@BeanReference(type = com.liferay.oauth.service.OAuthUserLocalService.class)
	protected com.liferay.oauth.service.OAuthUserLocalService oAuthUserLocalService;
	@BeanReference(type = com.liferay.oauth.service.OAuthUserService.class)
	protected com.liferay.oauth.service.OAuthUserService oAuthUserService;
	@BeanReference(type = OAuthUserPersistence.class)
	protected OAuthUserPersistence oAuthUserPersistence;
	@BeanReference(type = com.liferay.counter.service.CounterLocalService.class)
	protected com.liferay.counter.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.service.ClassNameLocalService.class)
	protected com.liferay.portal.service.ClassNameLocalService classNameLocalService;
	@BeanReference(type = com.liferay.portal.service.ClassNameService.class)
	protected com.liferay.portal.service.ClassNameService classNameService;
	@BeanReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;
	@BeanReference(type = com.liferay.portal.service.ImageLocalService.class)
	protected com.liferay.portal.service.ImageLocalService imageLocalService;
	@BeanReference(type = com.liferay.portal.service.ImageService.class)
	protected com.liferay.portal.service.ImageService imageService;
	@BeanReference(type = ImagePersistence.class)
	protected ImagePersistence imagePersistence;
	@BeanReference(type = com.liferay.portal.service.ResourceLocalService.class)
	protected com.liferay.portal.service.ResourceLocalService resourceLocalService;
	@BeanReference(type = com.liferay.portal.service.UserLocalService.class)
	protected com.liferay.portal.service.UserLocalService userLocalService;
	@BeanReference(type = com.liferay.portal.service.UserService.class)
	protected com.liferay.portal.service.UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private String _beanIdentifier;
	private ClassLoader _classLoader;
	private OAuthApplicationServiceClpInvoker _clpInvoker = new OAuthApplicationServiceClpInvoker();
}