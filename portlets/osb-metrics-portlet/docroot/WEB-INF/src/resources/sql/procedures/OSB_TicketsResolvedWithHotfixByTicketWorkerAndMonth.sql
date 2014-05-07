CREATE PROCEDURE
	OSB_TicketsResolvedWithHotfixByTicketWorkerAndMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT
		LAST_DAY(OSB_CSETicketsView.closedDate) AS closedMonthYear,
		OSB_CSETicketsView.ticketWorkerFirstName,
		OSB_CSETicketsView.ticketWorkerLastName,
		OSB_CSETicketsView.supportTeam,
		COUNT(*) AS ticketsClosed,
		SUM(OSB_CSETicketsView.hotfixRequired) AS HotFixCount
	FROM
		OSB_CSETicketsView
	WHERE
		(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
		(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
		OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL
	GROUP BY
		LAST_DAY(OSB_CSETicketsView.closedDate),
		OSB_CSETicketsView.ticketWorkerUserId,
		OSB_CSETicketsView.supportTeam
	ORDER BY
		closedMonthYear,
		OSB_CSETicketsView.supportTeam,
		OSB_CSETicketsView.ticketWorkerFirstName,
		OSB_CSETicketsView.ticketWorkerLastName;
END