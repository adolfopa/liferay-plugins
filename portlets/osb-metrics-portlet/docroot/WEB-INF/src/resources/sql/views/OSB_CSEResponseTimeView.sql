CREATE VIEW
	OSB_CSEResponseTimeView
AS
	SELECT
		OSB_CSETicketsView.ticketEntryId,
		OSB_ResponseTimeView.commenterUserId,
		OSB_ResponseTimeView.timeFromPreviousComment AS timeFromPreviousCustomerComment
	FROM
		OSB_CSETicketsView
		LEFT OUTER JOIN OSB_ResponseTimeView ON OSB_CSETicketsView.ticketEntryId = OSB_ResponseTimeView.ticketEntryId AND OSB_CSETicketsView.ticketWorkerUserId = OSB_ResponseTimeView.commenterUserId
		LEFT OUTER JOIN [$LRDCOM_DB$]User_ ON OSB_ResponseTimeView.previousCommenterUserId = User_.userId
	WHERE 
		OSB_ResponseTimeView.commenterUserId != OSB_ResponseTimeView.previousCommenterUserId AND
		OSB_ResponseTimeView.ticketWorker = 1 AND
		User_.emailAddress NOT LIKE '%@liferay.com'
	GROUP BY
		OSB_CSETicketsView.ticketEntryId,
		OSB_CSETicketsView.ticketWorkerUserId,
		OSB_ResponseTimeView.timeFromPreviousComment;