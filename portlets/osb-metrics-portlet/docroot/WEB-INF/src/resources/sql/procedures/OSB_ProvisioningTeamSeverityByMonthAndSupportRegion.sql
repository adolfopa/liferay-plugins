CREATE PROCEDURE
	OSB_ProvisioningTeamSeverityByMonthAndSupportRegion(IN startDate DATETIME, IN endDate DATETIME, IN supportRegion VARCHAR(15))
BEGIN
	SELECT
		TEMP_TABLE.closedMonthYear,
		TEMP_TABLE.severityType,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Gold'
				THEN
					TEMP_TABLE.ticketCount
				ELSE
					0
			END
		) AS goldTicketCount,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Gold'
				THEN
					(TEMP_TABLE.closedDelta / TEMP_TABLE.ticketCount)
				ELSE
					0
			END
		) AS averageGoldClosedDelta,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Gold'
				THEN
					(TEMP_TABLE.averageFeedbackRating / TEMP_TABLE.ticketsWithFeedback)
				ELSE
					0
			END
		) AS averageGoldFeedbackRating,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Gold'
				THEN
					TEMP_TABLE.ticketsWithFeedback
				ELSE
					0
			END
		) AS goldTicketsWithFeedback,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Platinum'
				THEN
					TEMP_TABLE.ticketCount
				ELSE
					0
			END
		) AS platinumTicketCount,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Platinum'
				THEN
					(TEMP_TABLE.closedDelta / TEMP_TABLE.ticketCount)
				ELSE
					0
			END
		) AS averagePlatinumClosedDelta,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Platinum'
				THEN
					(TEMP_TABLE.averageFeedbackRating / TEMP_TABLE.ticketsWithFeedback)
				ELSE
					0
			END
		) AS averagePlatinumFeedbackRating,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Platinum'
				THEN
					TEMP_TABLE.ticketsWithFeedback
				ELSE
					0
			END
		) AS platinumTicketsWithFeedback,
		SUM(TEMP_TABLE.ticketCount) AS combinedTicketCount,
		(SUM(TEMP_TABLE.closedDelta) / SUM(TEMP_TABLE.ticketCount)) AS averageCombinedClosedDelta,
		(SUM(TEMP_TABLE.averageFeedbackRating) / SUM(TEMP_TABLE.ticketsWithFeedback)) AS averageCombinedFeedbackRating,
		SUM(TEMP_TABLE.ticketsWithFeedback) AS combinedTicketsWithFeedback
	FROM
		(
			SELECT
				LAST_DAY(OSB_ProvisioningOfferingTicketsView.closedDate) AS closedMonthYear,
				OSB_ProvisioningOfferingTicketsView.severityType,
				OSB_ProvisioningOfferingTicketsView.offeringType,
				COUNT(*) AS ticketCount,
				SUM(OSB_ProvisioningOfferingTicketsView.closedDelta) AS closedDelta,
				SUM(OSB_ProvisioningOfferingTicketsView.averageFeedbackRating) AS averageFeedbackRating,
				SUM(OSB_ProvisioningOfferingTicketsView.ticketWithFeedback) AS ticketsWithFeedback
			FROM
				OSB_ProvisioningOfferingTicketsView
			WHERE
				(TO_DAYS(OSB_ProvisioningOfferingTicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_ProvisioningOfferingTicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_ProvisioningOfferingTicketsView.supportRegion = supportRegion
			GROUP BY
				LAST_DAY(OSB_ProvisioningOfferingTicketsView.closedDate),
				OSB_ProvisioningOfferingTicketsView.offeringType,
				OSB_ProvisioningOfferingTicketsView.severityType
		) TEMP_TABLE
	GROUP BY
		TEMP_TABLE.closedMonthYear,
		TEMP_TABLE.severityType
	ORDER BY
		TEMP_TABLE.closedMonthYear,
		TEMP_TABLE.severityType;
END