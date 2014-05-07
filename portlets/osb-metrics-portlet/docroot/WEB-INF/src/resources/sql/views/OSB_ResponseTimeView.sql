CREATE VIEW
	OSB_ResponseTimeView
AS
	SELECT
		OSB_TicketComment_A.ticketCommentId,
		OSB_TicketComment_A.userId AS commenterUserId,
		OSB_TicketComment_A.createDate AS commentDate,
		OSB_TicketComment_A.ticketEntryId,
		CASE
			WHEN
				OSB_TicketWorker.userId IS NOT NULL
			THEN
				1
			ELSE
				0
		END AS ticketWorker,
		(
			SELECT
				OSB_TicketComment_B.userId
			FROM
				[$LRDCOM_DB$]OSB_TicketComment AS OSB_TicketComment_B
			WHERE
				OSB_TicketComment_B.createDate < OSB_TicketComment_A.createDate AND
				OSB_TicketComment_B.ticketEntryId = OSB_TicketComment_A.ticketEntryId AND
				OSB_TicketComment_B.visibility = 1
			ORDER BY
				OSB_TicketComment_B.createDate DESC
			LIMIT
				1
		) AS previousCommenterUserId,
		TIMEDIFF(
			OSB_TicketComment_A.createDate,
			(
				SELECT
					OSB_TicketComment_B.createDate
				FROM
					[$LRDCOM_DB$]OSB_TicketComment AS OSB_TicketComment_B
				WHERE
					OSB_TicketComment_B.createDate < OSB_TicketComment_A.createDate AND
					OSB_TicketComment_B.ticketEntryId = OSB_TicketComment_A.ticketEntryId AND
					OSB_TicketComment_B.visibility = 1
				ORDER BY
					OSB_TicketComment_B.createDate DESC
				LIMIT
					1
			)
		) AS timeFromPreviousComment
	FROM
		[$LRDCOM_DB$]OSB_TicketComment AS OSB_TicketComment_A
		LEFT OUTER JOIN [$LRDCOM_DB$]OSB_TicketWorker ON OSB_TicketComment_A.ticketEntryId = OSB_TicketWorker.ticketEntryId AND OSB_TicketComment_A.userId = OSB_TicketWorker.userId
		LEFT OUTER JOIN [$LRDCOM_DB$]User_ ON OSB_TicketWorker.userId = User_.userId
	WHERE
		OSB_TicketComment_A.visibility = 1
	ORDER BY
		OSB_TicketComment_A.ticketEntryId,
		commentDate;