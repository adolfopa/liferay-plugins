CREATE PROCEDURE
	OSB_EngineerMetricsByMonthAndSupportTeam(IN startDate DATETIME, IN endDate DATETIME, IN supportTeam VARCHAR(15))
BEGIN
	SELECT
		TEMP_TABLE_TICKET_WORKERS.ticketMonthYear,
		TEMP_TABLE_TICKET_WORKERS.supportTeam,
		TEMP_TABLE_TICKET_WORKERS.ticketWorkerFirstName,
		TEMP_TABLE_TICKET_WORKERS.ticketWorkerLastName,
		TEMP_TABLE_TICKET_ASSIGNED.ticketsAssigned,
		TEMP_TABLE_TICKETS_FEEDBACK.averageFeedbackTotal,
		TEMP_TABLE_TICKETS_FEEDBACK.ticketsWithFeedback,
		TEMP_TABLE_TICKETS_FEEDBACK.averageFinalResolutionFeedback,
		TEMP_TABLE_TICKETS_FEEDBACK.averageResponseTimeFeedback,
		TEMP_TABLE_TICKETS_FEEDBACK.averageTechnicalKnowledgeFeedback,
		TEMP_TABLE_TICKETS_FEEDBACK.averageProfessionalismFeedback,
		TEMP_TABLE_TICKETS_CLOSED.averageClosedDelta,
		TEMP_TABLE_TICKETS_CLOSED.ticketCount AS ticketResolved,
		TEMP_TABLE_TICKET_SEVERITY.averageCriticalClosedDelta,
		TEMP_TABLE_TICKET_SEVERITY.criticalTicketCount,
		TEMP_TABLE_TICKET_SEVERITY.averageMajorClosedDelta,
		TEMP_TABLE_TICKET_SEVERITY.majorTicketCount,
		TEMP_TABLE_TICKET_SEVERITY.averageMinorClosedDelta,
		TEMP_TABLE_TICKET_SEVERITY.minorTicketCount,
		TEMP_TABLE_TICKET_REPRODUCED_STATUS.averageReproducedStatusDelta,
		TEMP_TABLE_TICKETS_CLOSED.hotfixCount AS ticketWithHotfix,
		TEMP_TABLE_TICKETS_CLOSED.ticketsClosedAfterSLA,
		TEMP_TABLE_TICKET_ESCALATION.averageDaysResolvedAfter1BEscalation
	FROM
		(
			SELECT DISTINCT
				LAST_DAY(OSB_CSETicketsView.closedDate) AS ticketMonthYear,
				OSB_CSETicketsView.ticketWorkerUserId,
				OSB_CSETicketsView.ticketWorkerFirstName,
				OSB_CSETicketsView.ticketWorkerLastName,
				OSB_CSETicketsView.supportTeam
			FROM
				OSB_CSETicketsView
			WHERE
				(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL AND
				OSB_CSETicketsView.supportTeam = supportTeam

			UNION

			SELECT DISTINCT
				LAST_DAY(OSB_CSETicketsView.createDate) AS ticketMonthYear,
				OSB_CSETicketsView.ticketWorkerUserId,
				OSB_CSETicketsView.ticketWorkerFirstName,
				OSB_CSETicketsView.ticketWorkerLastName,
				OSB_CSETicketsView.supportTeam
			FROM
				OSB_CSETicketsView
			WHERE
				(TO_DAYS(OSB_CSETicketsView.createDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.createDate) <= TO_DAYS(endDate)) AND
				OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL AND
				OSB_CSETicketsView.supportTeam = supportTeam
		) TEMP_TABLE_TICKET_WORKERS
		LEFT OUTER JOIN
		(
			SELECT
				LAST_DAY(OSB_CSETicketsView.createDate) AS createMonthYear,
				OSB_CSETicketsView.ticketWorkerUserId,
				OSB_CSETicketsView.supportTeam,
				COUNT(*) AS ticketsAssigned
			FROM
				OSB_CSETicketsView
			WHERE
				(TO_DAYS(OSB_CSETicketsView.createDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.createDate) <= TO_DAYS(endDate)) AND
				OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL AND
				OSB_CSETicketsView.supportTeam = supportTeam
			GROUP BY
				LAST_DAY(OSB_CSETicketsView.createDate),
				OSB_CSETicketsView.ticketWorkerUserId,
				OSB_CSETicketsView.supportTeam
		) TEMP_TABLE_TICKET_ASSIGNED ON TEMP_TABLE_TICKET_WORKERS.ticketMonthYear = TEMP_TABLE_TICKET_ASSIGNED.createMonthYear AND TEMP_TABLE_TICKET_WORKERS.supportTeam = TEMP_TABLE_TICKET_ASSIGNED.supportTeam AND TEMP_TABLE_TICKET_WORKERS.ticketWorkerUserId = TEMP_TABLE_TICKET_ASSIGNED.ticketWorkerUserId
		LEFT OUTER JOIN
		(
			SELECT
				LAST_DAY(OSB_CSETicketsView.closedDate) AS closedMonthYear,
				OSB_CSETicketsView.ticketWorkerUserId,
				OSB_CSETicketsView.supportTeam,
				COUNT(*) AS ticketsWithFeedback,
				(SUM(OSB_CSETicketsView.rating1) / COUNT(*)) AS averageFinalResolutionFeedback,
				(SUM(OSB_CSETicketsView.rating2) / COUNT(*)) AS averageResponseTimeFeedback,
				(SUM(OSB_CSETicketsView.rating3) / COUNT(*)) AS averageTechnicalKnowledgeFeedback,
				(SUM(OSB_CSETicketsView.rating4) / COUNT(*)) AS averageProfessionalismFeedback,
				((SUM(OSB_CSETicketsView.rating1) + SUM(OSB_CSETicketsView.rating2) + SUM(OSB_CSETicketsView.rating3) + SUM(OSB_CSETicketsView.rating4)) / (4 * COUNT(*))) AS averageFeedbackTotal
			FROM
				OSB_CSETicketsView
			WHERE
				(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL AND
				OSB_CSETicketsView.supportTeam = supportTeam AND
				OSB_CSETicketsView.averageFeedbackRating > 0
			GROUP BY
				LAST_DAY(OSB_CSETicketsView.closedDate),
				OSB_CSETicketsView.ticketWorkerUserId,
				OSB_CSETicketsView.supportTeam
		) AS TEMP_TABLE_TICKETS_FEEDBACK ON TEMP_TABLE_TICKET_WORKERS.ticketMonthYear = TEMP_TABLE_TICKETS_FEEDBACK.closedMonthYear AND TEMP_TABLE_TICKET_WORKERS.supportTeam = TEMP_TABLE_TICKETS_FEEDBACK.supportTeam AND TEMP_TABLE_TICKET_WORKERS.ticketWorkerUserId = TEMP_TABLE_TICKETS_FEEDBACK.ticketWorkerUserId
		LEFT OUTER JOIN
		(
			SELECT
				LAST_DAY(OSB_CSETicketsView.closedDate) AS closedMonthYear,
				OSB_CSETicketsView.ticketWorkerUserId,
				OSB_CSETicketsView.supportTeam,
				COUNT(*) AS ticketCount,
				(SUM(OSB_CSETicketsView.closedDelta) / COUNT(*)) AS averageClosedDelta,
				SUM(OSB_CSETicketsView.overSLA) AS ticketsClosedAfterSLA,
				SUM(OSB_CSETicketsView.hotfixRequired) AS hotfixCount
			FROM
				OSB_CSETicketsView
			WHERE
				(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL AND
				OSB_CSETicketsView.supportTeam = supportTeam
			GROUP BY
				LAST_DAY(OSB_CSETicketsView.closedDate),
				OSB_CSETicketsView.ticketWorkerUserId,
				OSB_CSETicketsView.supportTeam
		) TEMP_TABLE_TICKETS_CLOSED ON TEMP_TABLE_TICKET_WORKERS.ticketMonthYear = TEMP_TABLE_TICKETS_CLOSED.closedMonthYear AND TEMP_TABLE_TICKET_WORKERS.supportTeam = TEMP_TABLE_TICKETS_CLOSED.supportTeam AND TEMP_TABLE_TICKET_WORKERS.ticketWorkerUserId = TEMP_TABLE_TICKETS_CLOSED.ticketWorkerUserId
		LEFT OUTER JOIN
		(
			SELECT
				TEMP_TABLE.closedMonthYear,
				TEMP_TABLE.supportTeam,
				TEMP_TABLE.ticketWorkerUserId,
				SUM(
					CASE
						WHEN
							TEMP_TABLE.severityType='Minor'
						THEN
							TEMP_TABLE.severityTypeTicketCount
						ELSE
							0
					END
				) AS minorTicketCount,
				SUM(
					CASE
						WHEN
							TEMP_TABLE.severityType='Minor'
						THEN
							TEMP_TABLE.averageSeverityTypeClosedDelta
						ELSE
							0
					END
				) AS averageMinorClosedDelta,
				SUM(
					CASE
						WHEN
							TEMP_TABLE.severityType='Major'
						THEN
							TEMP_TABLE.severityTypeTicketCount
						ELSE
							0
					END
				) AS majorTicketCount,
				SUM(
					CASE
						WHEN
							TEMP_TABLE.severityType='Major'
						THEN
							TEMP_TABLE.averageSeverityTypeClosedDelta
						ELSE
							0
					END
				) AS averageMajorClosedDelta,
				SUM(
					CASE
						WHEN
							TEMP_TABLE.severityType='Critical'
						THEN
							TEMP_TABLE.severityTypeTicketCount
						ELSE
							0
					END
				) AS criticalTicketCount,
				SUM(
					CASE
						WHEN
							TEMP_TABLE.severityType='Critical'
						THEN
							TEMP_TABLE.averageSeverityTypeClosedDelta
						ELSE
							0
					END
				) AS averageCriticalClosedDelta
			FROM
				(
					SELECT
						LAST_DAY(OSB_CSETicketsView.closedDate) AS closedMonthYear,
						OSB_CSETicketsView.severityType,
						OSB_CSETicketsView.ticketWorkerUserId,
						OSB_CSETicketsView.supportTeam,
						COUNT(*) AS severityTypeTicketCount,
						(SUM(OSB_CSETicketsView.closedDelta) / COUNT(*)) AS averageSeverityTypeClosedDelta
					FROM
						OSB_CSETicketsView
					WHERE
						(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
						(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
						OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL AND
						OSB_CSETicketsView.supportTeam = supportTeam
					GROUP BY
						LAST_DAY(OSB_CSETicketsView.closedDate),
						OSB_CSETicketsView.ticketWorkerUserId,
						OSB_CSETicketsView.supportTeam,
						OSB_CSETicketsView.severityType
				) TEMP_TABLE
			GROUP BY
				TEMP_TABLE.closedMonthYear,
				TEMP_TABLE.ticketWorkerUserId,
				TEMP_TABLE.supportTeam
		) TEMP_TABLE_TICKET_SEVERITY ON TEMP_TABLE_TICKET_WORKERS.ticketMonthYear = TEMP_TABLE_TICKET_SEVERITY.closedMonthYear AND TEMP_TABLE_TICKET_WORKERS.supportTeam = TEMP_TABLE_TICKET_SEVERITY.supportTeam AND TEMP_TABLE_TICKET_WORKERS.ticketWorkerUserId = TEMP_TABLE_TICKET_SEVERITY.ticketWorkerUserId
		LEFT OUTER JOIN
		(
			SELECT
				LAST_DAY(OSB_CSETicketsView.closedDate) AS closedMonthYear,
				OSB_CSETicketsView.supportTeam,
				OSB_TicketsAuditStatusView.auditorUserId,
				(SUM(OSB_TicketsAuditStatusView.statusDelta) / COUNT(*)) AS averageReproducedStatusDelta
			FROM
				(
					SELECT
						MAX(OSB_TicketsAuditStatusView.auditEntryId) AS auditEntryId
					FROM
						OSB_TicketsAuditStatusView
					WHERE
						OSB_TicketsAuditStatusView.newLabel = 'reproduced'
					GROUP BY
						OSB_TicketsAuditStatusView.ticketEntryId
				) AS TEMP_TABLE
				INNER JOIN OSB_TicketsAuditStatusView ON TEMP_TABLE.auditEntryId = OSB_TicketsAuditStatusView.auditEntryId
				INNER JOIN OSB_CSETicketsView ON OSB_TicketsAuditStatusView.ticketEntryId = OSB_CSETicketsView.ticketEntryId AND OSB_TicketsAuditStatusView.auditorUserId = OSB_CSETicketsView.ticketWorkerUserId
			WHERE
				(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_TicketsAuditStatusView.auditorUserId IS NOT NULL AND
				OSB_CSETicketsView.supportTeam = supportTeam
			GROUP BY
				LAST_DAY(OSB_CSETicketsView.closedDate),
				OSB_TicketsAuditStatusView.auditorUserId,
				OSB_CSETicketsView.supportTeam
		) TEMP_TABLE_TICKET_REPRODUCED_STATUS ON TEMP_TABLE_TICKET_WORKERS.ticketMonthYear = TEMP_TABLE_TICKET_REPRODUCED_STATUS.closedMonthYear AND TEMP_TABLE_TICKET_WORKERS.supportTeam = TEMP_TABLE_TICKET_REPRODUCED_STATUS.supportTeam AND TEMP_TABLE_TICKET_WORKERS.ticketWorkerUserId = TEMP_TABLE_TICKET_REPRODUCED_STATUS.auditorUserId
		LEFT OUTER JOIN
		(
			SELECT
				LAST_DAY(OSB_TicketsEscalatedView.closedDate) AS closedMonthYear,
				OSB_CSETicketsView.supportTeam,
				OSB_TicketsEscalatedView.ticketWorkerUserId,
				(SUM(OSB_TicketsEscalatedView.resolutionTime) / COUNT(*)) AS averageDaysResolvedAfter1BEscalation
			FROM
				OSB_CSETicketsView
				INNER JOIN OSB_TicketsEscalatedView ON OSB_CSETicketsView.ticketEntryId = OSB_TicketsEscalatedView.ticketEntryId AND OSB_CSETicketsView.ticketWorkerUserId = OSB_TicketsEscalatedView.ticketWorkerUserId
			WHERE
				(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_CSETicketsView.ticketWorkerUserId IS NOT NULL AND
				OSB_CSETicketsView.supportTeam = supportTeam AND
				OSB_TicketsEscalatedView.ticketWorkerRole = 3
			GROUP BY
				LAST_DAY(OSB_TicketsEscalatedView.closedDate),
				OSB_TicketsEscalatedView.ticketWorkerUserId,
				OSB_CSETicketsView.supportTeam
		) TEMP_TABLE_TICKET_ESCALATION ON TEMP_TABLE_TICKET_WORKERS.ticketMonthYear = TEMP_TABLE_TICKET_ESCALATION.closedMonthYear AND TEMP_TABLE_TICKET_WORKERS.supportTeam = TEMP_TABLE_TICKET_ESCALATION.supportTeam AND TEMP_TABLE_TICKET_WORKERS.ticketWorkerUserId = TEMP_TABLE_TICKET_ESCALATION.ticketWorkerUserId
	ORDER BY
		ticketMonthYear,
		ticketWorkerLastName,
		ticketWorkerFirstName;
END