CREATE PROCEDURE
	OSB_AverageDaysClosedAgainstSLABySupportRegionAndMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT
		LAST_DAY(OSB_CSETicketsView.closedDate) AS closedMonthYear,
		OSB_CSETicketsView.supportRegion,
		(SUM(OSB_CSETicketsView.closedDelta) / COUNT(*)) AS averageClosedDelta
	FROM
		OSB_CSETicketsView
	WHERE
		(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
		(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate))
	GROUP BY
		LAST_DAY(OSB_CSETicketsView.closedDate),
		OSB_CSETicketsView.supportRegion;
END