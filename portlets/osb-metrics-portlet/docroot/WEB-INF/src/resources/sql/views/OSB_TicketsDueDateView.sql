CREATE VIEW
	OSB_TicketsDueDateView
AS
	SELECT
		OSB_TicketEntry.ticketEntryId,
		OSB_AuditEntry.createDate,
		CASE
			WHEN
				STR_TO_DATE(OSB_AuditEntry.oldLabel, '%Y-%m-%d %T') IS NULL
			THEN
				STR_TO_DATE(OSB_AuditEntry.oldLabel,'%a %b %d %T GMT %Y')
			ELSE
				STR_TO_DATE(OSB_AuditEntry.oldLabel, '%Y-%m-%d %T')
		END AS oldDueDate,
		CASE
			WHEN
				STR_TO_DATE(OSB_AuditEntry.newLabel, '%Y-%m-%d %T') IS NULL
			THEN
				STR_TO_DATE(OSB_AuditEntry.newLabel,'%a %b %d %T GMT %Y')
			ELSE
				STR_TO_DATE(OSB_AuditEntry.newLabel, '%Y-%m-%d %T')
		END AS newDueDate
	FROM
		[$LRDCOM_DB$]OSB_TicketEntry
		INNER JOIN [$LRDCOM_DB$]OSB_AuditEntry ON OSB_TicketEntry.ticketEntryId = OSB_AuditEntry.classPK AND OSB_AuditEntry.classNameId = 1400973 AND OSB_AuditEntry.field = 34006

	UNION

	SELECT
		OSB_TicketsOriginalDueDateView.ticketEntryId,
		OSB_TicketsOriginalDueDateView.createDate,
		OSB_TicketsOriginalDueDateView.originalDueDate AS oldDueDate,
		OSB_TicketsOriginalDueDateView.originalDueDate AS newDueDate
	FROM
		OSB_TicketsOriginalDueDateView;