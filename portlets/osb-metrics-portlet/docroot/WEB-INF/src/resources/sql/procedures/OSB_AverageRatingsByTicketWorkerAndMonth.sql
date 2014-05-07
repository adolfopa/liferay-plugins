CREATE PROCEDURE
	OSB_AverageRatingsByTicketWorkerAndMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT
		LAST_DAY(OSB_CSETicketsView.closedDate) AS closedMonthYear,
		OSB_CSETicketsView.ticketWorkerFirstName,
		OSB_CSETicketsView.ticketWorkerLastName,
		OSB_CSETicketsView.supportTeam,
		COUNT(*) AS ticketsWithFeedback,
		(SUM(OSB_CSETicketsView.rating1) / COUNT(*)) AS avgFinalResolutionFeedback,
		(SUM(OSB_CSETicketsView.rating2) / COUNT(*)) AS avgResponseTimeFeedback,
		(SUM(OSB_CSETicketsView.rating3) / COUNT(*)) AS avgTechnicalKnowledgeFeedback,
		(SUM(OSB_CSETicketsView.rating4) / COUNT(*)) AS avgProfessionalismFeedback
	FROM
		OSB_CSETicketsView
	WHERE
		(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
		(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
		OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL AND
		OSB_CSETicketsView.averageFeedbackRating > 0
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