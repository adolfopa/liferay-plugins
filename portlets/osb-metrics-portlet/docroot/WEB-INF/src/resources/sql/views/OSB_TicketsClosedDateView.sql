CREATE VIEW
	OSB_TicketsClosedDateView
AS
	SELECT
		OSB_TicketEntry.ticketEntryId,
		MAX(OSB_AuditEntry.createDate) AS closedDate,
		OSB_TicketEntry.dueDate AS currentDueDate
	FROM
		[$LRDCOM_DB$]OSB_TicketEntry
		INNER JOIN [$LRDCOM_DB$]OSB_AuditEntry ON OSB_TicketEntry.ticketEntryId = OSB_AuditEntry.classPK
	WHERE
		OSB_AuditEntry.classNameId = 1400973 AND
		OSB_AuditEntry.field = 34017 AND
		OSB_AuditEntry.newValue = 33000
	GROUP BY
		OSB_TicketEntry.ticketEntryId,
		LAST_DAY(OSB_AuditEntry.createDate);