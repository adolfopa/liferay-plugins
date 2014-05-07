CREATE PROCEDURE
	OSB_AverageFeedbackBySupportRegionAndAccount(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT
		LAST_DAY(TEMP_TABLE.closedDate) AS closedMonthYear,
		TEMP_TABLE.supportRegion,
		TEMP_TABLE.accountName,
		COUNT(*) AS ticketCount,
		(SUM(TEMP_TABLE.averageFeedbackRating) / COUNT(*)) AS averageFeedBack
	FROM
		(
			SELECT DISTINCT
				OSB_CSETicketsView.ticketEntryId,
				OSB_CSETicketsView.closedDate,
				OSB_CSETicketsView.accountName,
				OSB_CSETicketsView.supportRegion,
				OSB_CSETicketsView.averageFeedbackRating
			FROM
				OSB_CSETicketsView
			WHERE
				(TO_DAYS(OSB_CSETicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_CSETicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_CSETicketsView.averageFeedbackRating > 0
		) TEMP_TABLE
	GROUP BY
		LAST_DAY(TEMP_TABLE.closedDate),
		TEMP_TABLE.supportRegion,
		TEMP_TABLE.accountName
	ORDER BY
		closedMonthYear,
		TEMP_TABLE.supportRegion,
		TEMP_TABLE.accountName;
END