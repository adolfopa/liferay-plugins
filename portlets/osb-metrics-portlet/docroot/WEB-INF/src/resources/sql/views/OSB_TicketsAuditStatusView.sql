CREATE VIEW
	OSB_TicketsAuditStatusView
AS
	SELECT DISTINCT
		OSB_TicketEntry.ticketEntryId,
		OSB_TicketEntry.createDate AS ticketCreated,
		OSB_TicketEntry.dueDate AS ticketDue,
		OSB_AuditEntry.auditEntryId,
		OSB_AuditEntry.createDate AS auditDate,
		OSB_AuditEntry.userId AS auditorUserId,
		(TO_DAYS(OSB_TicketEntry.dueDate) - TO_DAYS(OSB_AuditEntry.createDate)) AS statusDelta,
		CASE
			WHEN
				User_.emailAddress LIKE '%@liferay.com'
			THEN
				1
			ELSE
				0
		END AS employee,
		OSB_AuditEntry.oldLabel,
		OSB_AuditEntry.newLabel,
		CASE
			WHEN
				OSB_AuditEntry.previousAuditEntryId = 0
			THEN
				TIMEDIFF(OSB_AuditEntry.createDate, OSB_TicketEntry.createDate)
			ELSE
				(
					CASE
						WHEN
							OSB_AuditEntry.oldValue != 33000
						THEN
							TIMEDIFF(
								OSB_AuditEntry.createDate,
								(
									SELECT
										OSB_AuditEntry_B.createDate
									FROM
										[$LRDCOM_DB$]OSB_AuditEntry AS OSB_AuditEntry_B
									WHERE
										OSB_AuditEntry_B.auditEntryId = OSB_AuditEntry.previousAuditEntryId
								)
							)
						ELSE
							NULL
					END
				)
		END AS workTime
	FROM
		[$LRDCOM_DB$]OSB_TicketEntry
		INNER JOIN [$LRDCOM_DB$]OSB_AuditEntry ON OSB_TicketEntry.ticketEntryId = OSB_AuditEntry.classPK
		INNER JOIN [$LRDCOM_DB$]User_ ON OSB_AuditEntry.userId = User_.userId
	WHERE
		OSB_AuditEntry.classNameId = 1400973 AND
		OSB_AuditEntry.field = 34017;