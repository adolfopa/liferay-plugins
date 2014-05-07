CREATE PROCEDURE
	OSB_GoldAndPlatinumTicketsMetricsByYear(IN year VARCHAR(4))
BEGIN
	SELECT
		TEMP_TABLE_TICKET_COUNT.ticketYear,
		TEMP_TABLE_TICKET_COUNT.offeringType,
		TEMP_TABLE_TICKET_COUNT.ticketCount,
		CASE
			WHEN
				TEMP_TABLE_TICKET_COUNT.offeringType = 'Gold'
			THEN
				TEMP_TABLE_ACCOUNT_COUNT.goldAccount
			WHEN
				TEMP_TABLE_TICKET_COUNT.offeringType = 'Platinum'
			THEN
				TEMP_TABLE_ACCOUNT_COUNT.platinumAccount
		END AS accountCount,
		CASE
			WHEN
				TEMP_TABLE_TICKET_COUNT.offeringType = 'Gold'
			THEN
				(TEMP_TABLE_TICKET_COUNT.ticketCount / TEMP_TABLE_ACCOUNT_COUNT.goldAccount)
			WHEN
				TEMP_TABLE_TICKET_COUNT.offeringType = 'Platinum'
			THEN
				(TEMP_TABLE_TICKET_COUNT.ticketCount / TEMP_TABLE_ACCOUNT_COUNT.platinumAccount)
		END AS averageTicketPerAccount
	FROM
		(
			SELECT
				YEAR(OSB_OfferingTicketsView.createDate) AS ticketYear,
				OSB_OfferingTicketsView.offeringType,
				COUNT(*) AS ticketCount
			FROM
				OSB_OfferingTicketsView
			WHERE
				YEAR(OSB_OfferingTicketsView.createDate) = year
			GROUP BY
				YEAR(OSB_OfferingTicketsView.createDate),
				OSB_OfferingTicketsView.offeringType
		) TEMP_TABLE_TICKET_COUNT
		LEFT OUTER JOIN
		(
			SELECT
				TEMP_TABLE.ticketYear,
				SUM(
					CASE
						WHEN
							TEMP_TABLE.offeringType = 'Gold'
						THEN
							1
						ELSE
							0
					END
				) AS goldAccount,
				SUM(
					CASE
						WHEN
							TEMP_TABLE.offeringType = 'Platinum'
						THEN
							1
						ELSE
							0
					END
				) AS platinumAccount
			FROM
				(
					SELECT DISTINCT
						YEAR(OSB_OfferingTicketsView.createDate) AS ticketYear,
						OSB_OfferingTicketsView.accountEntryId,
						OSB_OfferingTicketsView.offeringType
					FROM
						OSB_OfferingTicketsView
					WHERE
						YEAR(OSB_OfferingTicketsView.createDate) = year
				) TEMP_TABLE
			GROUP BY
				TEMP_TABLE.ticketYear
		) TEMP_TABLE_ACCOUNT_COUNT ON TEMP_TABLE_TICKET_COUNT.ticketYear = TEMP_TABLE_ACCOUNT_COUNT.ticketYear
	ORDER BY
		TEMP_TABLE_TICKET_COUNT.ticketYear,
		TEMP_TABLE_TICKET_COUNT.offeringType;
END