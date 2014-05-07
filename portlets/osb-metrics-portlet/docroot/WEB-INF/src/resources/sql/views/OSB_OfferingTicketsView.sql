CREATE VIEW
	OSB_OfferingTicketsView
AS
	SELECT DISTINCT
		OSB_TicketsView.ticketEntryId,
		OSB_TicketsView.createDate,
		OSB_TicketsView.accountEntryId,
		CASE
			WHEN
				OSB_TicketsView.offeringType LIKE '%Gold'
			THEN
				'Gold'
			WHEN
				OSB_TicketsView.offeringType LIKE '%Platinum'
			THEN
				'Platinum'
		END AS offeringType
	FROM
		OSB_TicketsView
	WHERE
		OSB_TicketsView.offeringType LIKE '%Gold' OR
		OSB_TicketsView.offeringType LIKE '%Platinum';