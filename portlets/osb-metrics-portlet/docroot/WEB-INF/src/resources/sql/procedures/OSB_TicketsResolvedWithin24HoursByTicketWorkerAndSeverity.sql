CREATE PROCEDURE
	OSB_TicketsResolvedWithin24HoursByTicketWorkerAndSeverity(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT
		LAST_DAY(TEMP_TABLE.closedDate) AS closedMonthYear,
		TEMP_TABLE.supportTeam,
		TEMP_TABLE.severityType,
		TEMP_TABLE.ticketWorkerFirstName,
		TEMP_TABLE.ticketWorkerLastName,
		COUNT(*) AS ticketClosed,
		SUM(TEMP_TABLE.resolvedInDay) AS resolvedInDay,
		((SUM(TEMP_TABLE.resolvedInDay) / COUNT(*)) * 100) AS resolvedInDayPercentage
	FROM
		(
			SELECT DISTINCT
				OSB_CSETicketsView.ticketEntryId,
				OSB_CSETicketsView.closedDate,
				OSB_CSETicketsView.severityType,
				OSB_CSETicketsView.ticketWorkerUserId,
				OSB_CSETicketsView.ticketWorkerFirstName,
				OSB_CSETicketsView.ticketWorkerLastName,
				OSB_CSETicketsView.supportTeam,
				CASE
					WHEN
						HOUR(TIMEDIFF(OSB_CSETicketsView.closedDate, OSB_CSETicketsView.createDate)) < 24
					THEN
						1
					ELSE
						0
				END AS resolvedInDay
			FROM
				OSB_CSETicketsView
			WHERE
				(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL
		) TEMP_TABLE
	GROUP BY
		closedMonthYear,
		TEMP_TABLE.ticketWorkerUserId,
		TEMP_TABLE.supportTeam,
		TEMP_TABLE.severityType
	ORDER BY
		closedMonthYear,
		TEMP_TABLE.supportTeam,
		TEMP_TABLE.severityType,
		TEMP_TABLE.ticketWorkerFirstName,
		TEMP_TABLE.ticketWorkerLastName;
END
