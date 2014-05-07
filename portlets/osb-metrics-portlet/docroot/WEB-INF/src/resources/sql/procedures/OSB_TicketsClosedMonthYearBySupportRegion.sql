CREATE PROCEDURE
	OSB_TicketsClosedMonthYearBySupportRegion(IN startDate DATETIME, IN endDate DATETIME, IN supportRegion VARCHAR(15))
BEGIN
	SELECT DISTINCT
		LAST_DAY(OSB_TicketsView.closedDate) AS closedMonthYear
	FROM
		OSB_TicketsView
	WHERE
		(TO_DAYS(OSB_TicketsView.closedDate) >= TO_DAYS(startDate)) AND
		(TO_DAYS(OSB_TicketsView.closedDate) <= TO_DAYS(endDate)) AND
		OSB_TicketsView.supportRegion = supportRegion
	ORDER BY
		closedMonthYear;
END