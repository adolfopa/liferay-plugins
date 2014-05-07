CREATE VIEW
	OSB_TicketsEscalatedView
AS
	SELECT DISTINCT
		OSB_TicketEntry.ticketEntryId,
		OSB_TicketEntry.closedDate AS closedDate,
		MAX(OSB_AuditEntry.createDate) AS escalatedDate,
		(TO_DAYS(OSB_TicketEntry.closedDate) - TO_DAYS(MAX(OSB_AuditEntry.createDate))) AS resolutionTime,
		(TO_DAYS(OSB_TicketEntry.dueDate) - TO_DAYS(OSB_TicketEntry.closedDate)) AS closedDelta,
		OSB_TicketWorker.role AS ticketWorkerRole,
		User_.userId AS ticketWorkerUserId,
		CASE
			WHEN
				User_.emailAddress LIKE '%@liferay.com'
			THEN
				1
			ELSE
				0
		END AS employee,
		User_.firstName AS ticketWorkerFirstName,
		User_.lastName AS ticketWorkerLastName
	FROM
		[$LRDCOM_DB$]OSB_TicketEntry
		INNER JOIN [$LRDCOM_DB$]OSB_AuditEntry ON OSB_TicketEntry.ticketEntryId = OSB_AuditEntry.classPK
		INNER JOIN [$LRDCOM_DB$]OSB_TicketWorker ON OSB_TicketEntry.ticketEntryId = OSB_TicketWorker.ticketEntryId
		INNER JOIN [$LRDCOM_DB$]User_ ON OSB_TicketWorker.userId = User_.userId
	WHERE
		OSB_AuditEntry.classNameId = 1400973 AND
		OSB_AuditEntry.oldValue = 31001 AND
		OSB_AuditEntry.newValue = 31002 AND
		(
			OSB_TicketWorker.role = 3 OR
			OSB_TicketWorker.role = 2
		)
	GROUP BY
		OSB_TicketWorker.userId,
		OSB_TicketEntry.ticketEntryId;