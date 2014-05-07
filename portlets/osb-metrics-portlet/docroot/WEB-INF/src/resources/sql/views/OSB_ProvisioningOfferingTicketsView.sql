CREATE VIEW
	OSB_ProvisioningOfferingTicketsView
AS
	SELECT DISTINCT
		OSB_ProvisioningTicketsView.ticketEntryId,
		OSB_ProvisioningTicketsView.closedDate,
		OSB_ProvisioningTicketsView.closedDelta,
		OSB_ProvisioningTicketsView.severityType,
		OSB_ProvisioningTicketsView.supportRegion,
		OSB_ProvisioningTicketsView.componentType,
		OSB_ProvisioningTicketsView.resolutionType,
		OSB_ProvisioningTicketsView.hotfixRequired,
		OSB_ProvisioningTicketsView.averageFeedbackRating,
		CASE
			WHEN
				OSB_ProvisioningTicketsView.averageFeedbackRating > 0
			THEN
				1
			ELSE
				0
		END AS ticketWithFeedback,
		CASE
			WHEN
				OSB_ProvisioningTicketsView.offeringType LIKE '%Gold'
			THEN
				'Gold'
			WHEN
				OSB_ProvisioningTicketsView.offeringType LIKE '%Platinum'
			THEN
				'Platinum'
		END AS offeringType
	FROM
		OSB_ProvisioningTicketsView
	WHERE
		(
			OSB_ProvisioningTicketsView.offeringType LIKE '%Gold' OR
			OSB_ProvisioningTicketsView.offeringType LIKE '%Platinum'
		) AND
		OSB_ProvisioningTicketsView.escalationLevel <> 'P1';