CREATE PROCEDURE
	OSB_TicketsEscalatedPerSeverityByEmployeeAndMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT
		LAST_DAY(OSB_TicketsEscalatedView.escalatedDate) AS escalatedMonthYear,
		OSB_CSETicketsView.supportTeam,
		CASE
			WHEN
				OSB_TicketsEscalatedView.ticketWorkerRole = 2
			THEN
				'1A'
			WHEN
				OSB_TicketsEscalatedView.ticketWorkerRole = 3
			THEN
				'1B'
		END AS employeeRole,
		OSB_TicketsEscalatedView.ticketWorkerFirstName,
		OSB_TicketsEscalatedView.ticketWorkerLastName,
		OSB_CSETicketsView.severityType,
		COUNT(*) AS ticketsEscalated
	FROM
		OSB_CSETicketsView
		INNER JOIN OSB_TicketsEscalatedView ON OSB_CSETicketsView.ticketEntryId = OSB_TicketsEscalatedView.ticketEntryId AND OSB_CSETicketsView.ticketWorkerUserId = OSB_TicketsEscalatedView.ticketWorkerUserId
	WHERE
		(TO_DAYS(OSB_TicketsEscalatedView.escalatedDate) >= TO_DAYS(startDate)) AND
		(TO_DAYS(OSB_TicketsEscalatedView.escalatedDate) <= TO_DAYS(endDate)) AND
		OSB_TicketsEscalatedView.employee = 1
	GROUP BY
		LAST_DAY(OSB_TicketsEscalatedView.escalatedDate),
		OSB_TicketsEscalatedView.ticketWorkerUserId,
		OSB_TicketsEscalatedView.ticketWorkerRole,
		OSB_CSETicketsView.severityType
	ORDER BY
		escalatedMonthYear,
		OSB_CSETicketsView.supportTeam,
		OSB_TicketsEscalatedView.ticketWorkerRole,
		OSB_TicketsEscalatedView.ticketWorkerFirstName,
		OSB_TicketsEscalatedView.ticketWorkerLastName,
		OSB_CSETicketsView.severityType;
END