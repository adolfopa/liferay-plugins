CREATE PROCEDURE 
	OSB_TicketsAssignedByTicketWorkerAndMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT
		LAST_DAY(OSB_CSETicketsView.createDate) AS assignedMonthYear,
		OSB_CSETicketsView.ticketWorkerFirstName,
		OSB_CSETicketsView.ticketWorkerLastName,
		OSB_CSETicketsView.supportTeam,
		COUNT(*) AS ticketsAssigned
	FROM
		OSB_CSETicketsView
	WHERE
		(TO_DAYS(OSB_CSETicketsView.createDate) >= TO_DAYS(startDate)) AND
		(TO_DAYS(OSB_CSETicketsView.createDate) <= TO_DAYS(endDate)) AND
		OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL
	GROUP BY
		LAST_DAY(OSB_CSETicketsView.createDate),
		OSB_CSETicketsView.ticketWorkerUserId,
		OSB_CSETicketsView.supportTeam
	ORDER BY
		assignedMonthYear,
		OSB_CSETicketsView.supportTeam,
		OSB_CSETicketsView.ticketWorkerFirstName,
		OSB_CSETicketsView.ticketWorkerLastName;
END