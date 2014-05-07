CREATE VIEW
	OSB_FirstCommentDateView
AS
	SELECT
		OSB_TicketComment.ticketEntryId,
		MIN(OSB_TicketComment.createDate) AS commentDate
	FROM
		[$LRDCOM_DB$]OSB_TicketComment
		LEFT OUTER JOIN [$LRDCOM_DB$]OSB_TicketWorker ON OSB_TicketWorker.ticketEntryId = OSB_TicketComment.ticketEntryId AND OSB_TicketWorker.userId = OSB_TicketComment.userId
	WHERE
		OSB_TicketComment.visibility = 1 AND
		(
			OSB_TicketWorker.role = 2 OR
			OSB_TicketWorker.role = 3
		)
	GROUP BY
		OSB_TicketComment.ticketEntryId;