CREATE PROCEDURE
	OSB_TeamResolutionByMonthAndSupportRegion(IN startDate DATETIME, IN endDate DATETIME, IN supportRegion VARCHAR(15))
BEGIN
	SELECT
		TEMP_TABLE.closedMonthYear,
		TEMP_TABLE.resolutionType,
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
				LAST_DAY(OSB_CSEOfferingTicketsView.closedDate) AS closedMonthYear,
				OSB_CSEOfferingTicketsView.resolutionType,
				OSB_CSEOfferingTicketsView.offeringType,
				COUNT(*) AS ticketCount,
				SUM(OSB_CSEOfferingTicketsView.closedDelta) AS closedDelta,
				SUM(OSB_CSEOfferingTicketsView.averageFeedbackRating) AS averageFeedbackRating,
				SUM(OSB_CSEOfferingTicketsView.ticketWithFeedback) AS ticketsWithFeedback
			FROM
				OSB_CSEOfferingTicketsView
			WHERE
				(TO_DAYS(OSB_CSEOfferingTicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSEOfferingTicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_CSEOfferingTicketsView.supportRegion = supportRegion
			GROUP BY
				LAST_DAY(OSB_CSEOfferingTicketsView.closedDate),
				OSB_CSEOfferingTicketsView.offeringType,
				OSB_CSEOfferingTicketsView.resolutionType
		) TEMP_TABLE
	GROUP BY
		TEMP_TABLE.closedMonthYear,
		TEMP_TABLE.resolutionType
	ORDER BY
		TEMP_TABLE.closedMonthYear,
		TEMP_TABLE.resolutionType;
END