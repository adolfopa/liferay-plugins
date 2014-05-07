CREATE PROCEDURE
	OSB_TicketsPerLiferayVersionPerMonth(IN startYear VARCHAR(4))
BEGIN
	SELECT
		COUNT(DISTINCT OSB_TicketsView.ticketEntryId) AS ticketCount,
		COUNT(DISTINCT OSB_TicketsView.accountEntryId) AS accountCount,
		YEAR(LAST_DAY(OSB_TicketsView.createDate)) AS createYear,
		LAST_DAY(OSB_TicketsView.createDate) AS createMonthYear,
		OSB_TicketsView.envLFRType,
		OSB_LiferayVersionsView.minorVersion,
		TEMP_TABLE.totalTicketsPerMonth,
		TEMP_TABLE.totalAccountsPerMonth
	FROM
		OSB_TicketsView
		LEFT OUTER JOIN OSB_LiferayVersionsView ON OSB_TicketsView.envLFRType = OSB_LiferayVersionsView.liferayVersion
		LEFT OUTER JOIN
		(
			SELECT
				COUNT(DISTINCT OSB_TicketsView.ticketEntryId) AS totalTicketsPerMonth,
				COUNT(DISTINCT OSB_TicketsView.accountEntryId) AS totalAccountsPerMonth,
				LAST_DAY(OSB_TicketsView.createDate) AS createMonthYear
			FROM
				OSB_TicketsView
			WHERE
				OSB_TicketsView.envLFRClassName = 'com.liferay.osb.model.ProductEntry.portalAllVersions' AND
				OSB_TicketsView.envLFRType != 'other' AND
				OSB_TicketsView.envLFRType != '4.4' AND
				YEAR(OSB_TicketsView.createDate) >= startYear
			GROUP BY
				LAST_DAY(OSB_TicketsView.createDate)
		) TEMP_TABLE ON LAST_DAY(OSB_TicketsView.createDate) = TEMP_TABLE.createMonthYear
	WHERE
		OSB_TicketsView.envLFRClassName = 'com.liferay.osb.model.ProductEntry.portalAllVersions' AND
		OSB_TicketsView.envLFRType != 'other' AND
		OSB_TicketsView.envLFRType != '4.4' AND
		YEAR(OSB_TicketsView.createDate) >= startYear
	GROUP BY
		YEAR(LAST_DAY(OSB_TicketsView.createDate)),
		LAST_DAY(OSB_TicketsView.createDate),
		OSB_LiferayVersionsView.minorVersion,
		OSB_TicketsView.envLFRType;
END