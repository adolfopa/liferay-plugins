CREATE VIEW
	OSB_TicketsClosedAndDueDateView
AS
	SELECT
		OSB_TicketsClosedDateView.ticketEntryId,
		OSB_TicketsClosedDateView.closedDate,
		CASE
			WHEN(
				MAX(
					CASE
						WHEN
							LAST_DAY(OSB_TicketsDueDateView.createDate) = LAST_DAY(OSB_TicketsClosedDateView.closedDate)
						THEN
							OSB_TicketsDueDateView.newDueDate
						ELSE
							NULL
					END
				) IS NOT NULL
			) THEN (
				MAX(
					CASE
						WHEN
							LAST_DAY(OSB_TicketsDueDateView.createDate) = LAST_DAY(OSB_TicketsClosedDateView.closedDate)
						THEN
							OSB_TicketsDueDateView.newDueDate
						ELSE
							NULL
					END
				)
			)
			WHEN(
				MIN(
					CASE
						WHEN
							LAST_DAY(OSB_TicketsDueDateView.createDate) > LAST_DAY(OSB_TicketsClosedDateView.closedDate)
						THEN
							OSB_TicketsDueDateView.oldDueDate
						ELSE
							NULL
					END
				) IS NOT NULL
			) THEN (
				MIN(
					CASE
						WHEN
							LAST_DAY(OSB_TicketsDueDateView.createDate) > LAST_DAY(OSB_TicketsClosedDateView.closedDate)
						THEN
							OSB_TicketsDueDateView.oldDueDate
						ELSE
							NULL
					END
				)
			)
			ELSE
				MAX(OSB_TicketsDueDateView.newDueDate)
		END AS dueDate
	FROM
		OSB_TicketsClosedDateView
		LEFT OUTER JOIN OSB_TicketsDueDateView ON OSB_TicketsClosedDateView.ticketEntryId = OSB_TicketsDueDateView.ticketEntryId
	GROUP BY
		OSB_TicketsClosedDateView.ticketEntryId,
		OSB_TicketsClosedDateView.closedDate;