CREATE PROCEDURE
	OSB_TicketCommentsByEngineerAndDay(IN startDate DATETIME, IN endDate DATETIME, IN supportTeam VARCHAR(15), IN timeOffsetFromUTC VARCHAR(6))
BEGIN
	SELECT
		DATE(DATE_ADD(DATE_ADD(OSB_CSECommentsView.commentDate, INTERVAL(timeOffsetFromUTC * 60) MINUTE), INTERVAL (7 - DAYOFWEEK(DATE_ADD(OSB_CSECommentsView.commentDate, INTERVAL(timeOffsetFromUTC * 60) MINUTE))) DAY)) AS lastDayOfWeek,
		DATE(DATE_ADD(OSB_CSECommentsView.commentDate, INTERVAL(timeOffsetFromUTC * 60) MINUTE)) AS commentDate,
		OSB_CSECommentsView.ticketWorkerFirstName,
		OSB_CSECommentsView.ticketWorkerLastName,
		COUNT(*) AS commentsCountInDay,
		MIN(TIME(DATE_ADD(OSB_CSECommentsView.commentDate, INTERVAL(timeOffsetFromUTC * 60) MINUTE))) AS firstCommentTimeInDay,
		COUNT(DISTINCT OSB_CSECommentsView.ticketEntryId) AS ticketsCommentedOnInDay,
		(COUNT(*) / COUNT(DISTINCT OSB_CSECommentsView.ticketEntryId)) AS averageCommentsPerTicketPerDay
	FROM
		OSB_CSECommentsView
	WHERE
		(TO_DAYS(DATE_ADD(OSB_CSECommentsView.commentDate, INTERVAL(timeOffsetFromUTC * 60) MINUTE)) >= TO_DAYS(DATE_ADD(startDate, INTERVAL(1 - DAYOFWEEK(startDate)) DAY))) AND
		(TO_DAYS(DATE_ADD(OSB_CSECommentsView.commentDate, INTERVAL(timeOffsetFromUTC * 60) MINUTE)) <= TO_DAYS(DATE_ADD(endDate, INTERVAL(7 - DAYOFWEEK(endDate)) DAY))) AND
		OSB_CSECommentsView.supportTeam = supportTeam
	GROUP BY
		DATE(DATE_ADD(OSB_CSECommentsView.commentDate, INTERVAL(timeOffsetFromUTC * 60) MINUTE)),
		OSB_CSECommentsView.ticketWorkerUserId,
		OSB_CSECommentsView.supportTeam
	ORDER BY
		lastDayOfWeek,
		OSB_CSECommentsView.ticketWorkerFirstName,
		OSB_CSECommentsView.ticketWorkerLastName,
		commentDate;
END