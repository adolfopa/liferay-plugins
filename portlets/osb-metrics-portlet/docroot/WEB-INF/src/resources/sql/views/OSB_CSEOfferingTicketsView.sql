CREATE VIEW
	OSB_CSEOfferingTicketsView
AS
	SELECT DISTINCT
		OSB_CSETicketsView.ticketEntryId,
		OSB_CSETicketsView.closedDate,
		OSB_CSETicketsView.closedDelta,
		OSB_CSETicketsView.severityType,
		OSB_CSETicketsView.supportRegion,
		OSB_CSETicketsView.componentType,
		OSB_CSETicketsView.resolutionType,
		OSB_CSETicketsView.hotfixRequired,
		OSB_CSETicketsView.averageFeedbackRating,
		CASE
			WHEN
				OSB_CSETicketsView.averageFeedbackRating > 0
			THEN
				1
			ELSE
				0
		END AS ticketWithFeedback,
		CASE
			WHEN
				OSB_CSETicketsView.offeringType LIKE '%Gold'
			THEN
				'Gold'
			WHEN
				OSB_CSETicketsView.offeringType LIKE '%Platinum'
			THEN
				'Platinum'
		END AS offeringType
	FROM
		OSB_CSETicketsView
	WHERE
		(
			OSB_CSETicketsView.offeringType LIKE '%Gold' OR
			OSB_CSETicketsView.offeringType LIKE '%Platinum'
		) AND
		OSB_CSETicketsView.escalationLevel <> 'P1';