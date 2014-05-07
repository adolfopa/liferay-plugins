CREATE VIEW
	OSB_LiferayVersionsView
AS
	SELECT
		ListType.name AS liferayVersion,
		SUBSTRING_INDEX(ListType.name, ' ', 1) AS minorVersion
	FROM
		[$LRDCOM_DB$]ListType
	WHERE
		ListType.name != 'other' AND
		ListType.type_ = 'com.liferay.osb.model.ProductEntry.portalAllVersions';