CREATE PROCEDURE
	OSB_TicketsListPerEngineerByDateAndSupportTeam (IN startDate DATETIME, IN endDate DATETIME, IN supportTeam VARCHAR(15), IN engineerId BIGINT(20))
BEGIN
	SELECT DISTINCT
		OSB_CSETicketsView.ticketEntryId,
		CONCAT(OSB_CSETicketsView.code_, '-', OSB_CSETicketsView.ticketId) AS ticketNumber,
		OSB_CSETicketsView.closedDelta,
		OSB_CSETicketsView.overSLA,
		OSB_CSETicketsView.resolvedInDay,
		OSB_CSETicketsView.escalated,
		OSB_CSETicketsView.severityType,
		OSB_CSETicketsView.resolutionType,
		OSB_CSETicketsView.ticketEscalater,
		OSB_CSETicketsView.hotfixRequired,
		OSB_CSETicketsView.rating1,
		OSB_CSETicketsView.rating2,
		OSB_CSETicketsView.rating3,
		OSB_CSETicketsView.rating4,
		OSB_CSETicketsView.averageFeedbackRating,
		CASE
			WHEN
				TEMP_TABLE_REOPENED.reopenedCount IS NOT NULL
			THEN
				TEMP_TABLE_REOPENED.reopenedCount
			ELSE
				0
		END AS reopenedCount,
		CASE
			WHEN
				TEMP_TABLE_COMMENTS.commentCount IS NOT NULL
			THEN
				TEMP_TABLE_COMMENTS.commentCount
			ELSE
				0
		END AS commentsCount,
		CASE
			WHEN
				TEMP_TABLE_FIRST_RESPONSE.commentDate IS NOT NULL
			THEN
				TIME_TO_SEC(TIMEDIFF(TEMP_TABLE_FIRST_RESPONSE.commentDate, OSB_CSETicketsView.createDate))
			ELSE
				NULL
		END AS firstResponseTime,
		TEMP_TABLE_AVERAGE_RESPONSE_TIME.averageResponseTime,
		TEMP_TABLE_TOTAL_TICKET_TIME.totalTicketTime,
		TEMP_TABLE_REPRODUCTION_TIME.reproductionTime
	FROM
		OSB_CSETicketsView
		LEFT OUTER JOIN
		(
			SELECT
				OSB_TicketsAuditStatusView.ticketEntryId,
				COUNT(*) AS reopenedCount
			FROM
				OSB_TicketsAuditStatusView
			WHERE
				OSB_TicketsAuditStatusView.newLabel = 'reopened'
			GROUP BY
				OSB_TicketsAuditStatusView.ticketEntryId
		) TEMP_TABLE_REOPENED ON OSB_CSETicketsView.ticketEntryId = TEMP_TABLE_REOPENED.ticketEntryId
		LEFT OUTER JOIN
		(
			SELECT
				OSB_CSECommentsView.ticketEntryId,
				OSB_CSECommentsView.ticketWorkerUserId,
				COUNT(*) AS commentCount
			FROM
				OSB_CSECommentsView
			GROUP BY
				OSB_CSECommentsView.ticketEntryId,
				OSB_CSECommentsView.ticketWorkerUserId
		) TEMP_TABLE_COMMENTS ON OSB_CSETicketsView.ticketEntryId = TEMP_TABLE_COMMENTS.ticketEntryId AND OSB_CSETicketsView.ticketWorkerUserId = TEMP_TABLE_COMMENTS.ticketWorkerUserId
		LEFT OUTER JOIN
		(
			SELECT DISTINCT
				OSB_CSECommentsView.ticketEntryId,
				OSB_CSECommentsView.ticketWorkerUserId,
				OSB_CSECommentsView.commentDate
			FROM
				OSB_CSECommentsView
				INNER JOIN OSB_FirstCommentDateView ON OSB_CSECommentsView.ticketEntryId = OSB_FirstCommentDateView.ticketEntryId AND OSB_CSECommentsView.commentDate = OSB_FirstCommentDateView.commentDate
		) TEMP_TABLE_FIRST_RESPONSE ON OSB_CSETicketsView.ticketEntryId = TEMP_TABLE_FIRST_RESPONSE.ticketEntryId AND OSB_CSETicketsView.ticketWorkerUserId = TEMP_TABLE_FIRST_RESPONSE.ticketWorkerUserId
		LEFT OUTER JOIN
		(
			SELECT
				OSB_CSEResponseTimeView.ticketEntryId,
				OSB_CSEResponseTimeView.commenterUserId,
				CEIL(AVG(TIME_TO_SEC(OSB_CSEResponseTimeView.timeFromPreviousCustomerComment))) AS averageResponseTime
			FROM
				OSB_CSEResponseTimeView
			GROUP BY
				OSB_CSEResponseTimeView.ticketEntryId,
				OSB_CSEResponseTimeView.commenterUserId
		) TEMP_TABLE_AVERAGE_RESPONSE_TIME ON OSB_CSETicketsView.ticketEntryId = TEMP_TABLE_AVERAGE_RESPONSE_TIME.ticketEntryId AND OSB_CSETicketsView.ticketWorkerUserId = TEMP_TABLE_AVERAGE_RESPONSE_TIME.commenterUserId
		LEFT OUTER JOIN
		(
			SELECT
				OSB_TicketsAuditStatusView.ticketEntryId,
				SUM(TIME_TO_SEC(OSB_TicketsAuditStatusView.workTime)) AS totalTicketTime
			FROM
				OSB_TicketsAuditStatusView
			GROUP BY
				OSB_TicketsAuditStatusView.ticketEntryId
		) TEMP_TABLE_TOTAL_TICKET_TIME ON OSB_CSETicketsView.ticketEntryId = TEMP_TABLE_TOTAL_TICKET_TIME.ticketEntryId
		LEFT OUTER JOIN
		(
			SELECT
				TEMP_TABLE.ticketEntryId,
				(
					SELECT
						SUM(TIME_TO_SEC(OSB_TicketsAuditStatusView.workTime))
					FROM
						OSB_TicketsAuditStatusView
					WHERE
						OSB_TicketsAuditStatusView.ticketEntryId = TEMP_TABLE.ticketEntryId AND
						OSB_TicketsAuditStatusView.auditEntryId <= TEMP_TABLE.reproducedAuditEntryId
				) AS reproductionTime
			FROM
				(
					SELECT
						OSB_TicketsAuditStatusView.ticketEntryId,
						MAX(auditEntryId) AS reproducedAuditEntryId
					FROM
						OSB_TicketsAuditStatusView
					WHERE
						OSB_TicketsAuditStatusView.newLabel = 'reproduced'
					GROUP BY
						OSB_TicketsAuditStatusView.ticketEntryId
				) TEMP_TABLE
		) AS TEMP_TABLE_REPRODUCTION_TIME ON OSB_CSETicketsView.ticketEntryId = TEMP_TABLE_REPRODUCTION_TIME.ticketEntryId
	WHERE
		(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
		(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
		OSB_CSETicketsView.supportTeam = supportTeam AND
		OSB_CSETicketsView.ticketWorkerUserId = engineerId
	ORDER BY
		OSB_CSETicketsView.severityType,
		OSB_CSETicketsView.resolutionType;
END