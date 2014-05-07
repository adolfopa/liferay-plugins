CREATE VIEW
	OSB_TicketsOriginalDueDateView
AS
	SELECT
		OSB_TicketEntry.ticketEntryId,
		OSB_TicketEntry.createDate,
		CASE
			WHEN
				MIN(OSB_AuditEntry.oldLabel) IS NULL
			THEN
				OSB_TicketEntry.dueDate
			WHEN
				MIN(STR_TO_DATE(OSB_AuditEntry.oldLabel, '%Y-%m-%d %T')) IS NULL
			THEN
				MIN(STR_TO_DATE(OSB_AuditEntry.oldLabel,'%a %b %d %T GMT %Y'))
			WHEN
				MIN(STR_TO_DATE(OSB_AuditEntry.oldLabel,'%a %b %d %T GMT %Y')) IS NULL
			THEN
				MIN(STR_TO_DATE(OSB_AuditEntry.oldLabel, '%Y-%m-%d %T'))
			WHEN
				MIN(STR_TO_DATE(OSB_AuditEntry.oldLabel, '%Y-%m-%d %T')) <= MIN(STR_TO_DATE(OSB_AuditEntry.oldLabel,'%a %b %d %T GMT %Y'))
			THEN
				MIN(STR_TO_DATE(OSB_AuditEntry.oldLabel, '%Y-%m-%d %T'))
			ELSE
				MIN(STR_TO_DATE(OSB_AuditEntry.oldLabel,'%a %b %d %T GMT %Y'))
		END AS originalDueDate
	FROM
		[$LRDCOM_DB$]OSB_TicketEntry
		LEFT OUTER JOIN [$LRDCOM_DB$]OSB_AuditEntry ON OSB_TicketEntry.ticketEntryId = OSB_AuditEntry.classPK AND OSB_AuditEntry.classNameId = 1400973 AND OSB_AuditEntry.field = 34006
	GROUP BY
		OSB_TicketEntry.ticketEntryId;