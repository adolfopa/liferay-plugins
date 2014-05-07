CREATE PROCEDURE
	OSB_TicketDetailsBySupportRegionAndComponent(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT
		LAST_DAY(TEMP_TABLE.closedDate) AS closedMonthYear,
		TEMP_TABLE.supportRegion,
		TEMP_TABLE.componentType,
		COUNT(*) AS ticketCount,
		(SUM(TEMP_TABLE.closedDelta) / COUNT(*)) AS averageClosedDelta,
		(SUM(TEMP_TABLE.hotfixRequired)) AS hotfixCount
	FROM
		(
			SELECT DISTINCT
				OSB_CSETicketsView.ticketEntryId,
				OSB_CSETicketsView.closedDate,
				OSB_CSETicketsView.closedDelta,
				OSB_CSETicketsView.componentType,
				OSB_CSETicketsView.supportRegion,
				OSB_CSETicketsView.hotfixRequired
			FROM
				OSB_CSETicketsView
			WHERE
				(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate))
		) TEMP_TABLE
	GROUP BY
		LAST_DAY(TEMP_TABLE.closedDate),
		TEMP_TABLE.supportRegion,
		TEMP_TABLE.componentType
	ORDER BY
		closedMonthYear,
		TEMP_TABLE.supportRegion,
		TEMP_TABLE.componentType;
END