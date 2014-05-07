CREATE PROCEDURE
	OSB_TeamTotalsByMonthAndSupportRegion(IN startDate DATETIME, IN endDate DATETIME, IN supportRegion VARCHAR(15))
BEGIN
	SELECT
		TEMP_TABLE.closedMonthYear,
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
					TEMP_TABLE.hotfixCount
				ELSE
					0
			END
		) AS hotfixGoldCount,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Gold'
				THEN
					(TEMP_TABLE.hotfixCount / TEMP_TABLE.ticketCount)
				ELSE
					0
			END
		) AS hotfixGoldPercentage,
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
					TEMP_TABLE.hotfixCount
				ELSE
					0
			END
		) AS hotfixPlatinumCount,
		MAX(
			CASE
				WHEN
					TEMP_TABLE.offeringType = 'Platinum'
				THEN
					(TEMP_TABLE.hotfixCount / TEMP_TABLE.ticketCount)
				ELSE
					0
			END
		) AS hotfixPlatinumPercentage,
		SUM(TEMP_TABLE.ticketCount) AS combinedTicketCount,
		(SUM(TEMP_TABLE.closedDelta) / SUM(TEMP_TABLE.ticketCount)) AS averageCombinedClosedDelta,
		SUM(TEMP_TABLE.hotfixCount) AS hotfixCombinedCount,
		(SUM(TEMP_TABLE.hotfixCount) / SUM(TEMP_TABLE.ticketCount)) AS hotfixCombinedPercentage
	FROM
		(
			SELECT
				LAST_DAY(OSB_CSEOfferingTicketsView.closedDate) AS closedMonthYear,
				OSB_CSEOfferingTicketsView.offeringType,
				COUNT(*) AS ticketCount,
				SUM(OSB_CSEOfferingTicketsView.closedDelta) AS closedDelta,
				SUM(OSB_CSEOfferingTicketsView.hotfixRequired) AS hotfixCount,
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
				OSB_CSEOfferingTicketsView.offeringType
		) TEMP_TABLE
	GROUP BY
		TEMP_TABLE.closedMonthYear;
END