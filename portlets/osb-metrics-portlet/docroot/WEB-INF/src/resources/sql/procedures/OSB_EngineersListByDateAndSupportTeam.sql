CREATE PROCEDURE
	OSB_EngineersListByDateAndSupportTeam (IN startDate DATETIME, IN endDate DATETIME, IN supportTeam VARCHAR(15))
BEGIN
	SELECT DISTINCT
		OSB_CSETicketsView.ticketWorkerUserId,
		OSB_CSETicketsView.ticketWorkerFirstName,
		OSB_CSETicketsView.ticketWorkerLastName
	FROM
		OSB_CSETicketsView
	WHERE
		(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
		(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
		OSB_CSETicketsView.supportTeam = supportTeam AND
		OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL;
END