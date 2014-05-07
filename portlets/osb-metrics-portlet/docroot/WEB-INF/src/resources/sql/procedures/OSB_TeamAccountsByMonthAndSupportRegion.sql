CREATE PROCEDURE
	OSB_TeamAccountsByMonthAndSupportRegion(IN startDate DATETIME, IN endDate DATETIME, IN supportRegion VARCHAR(15))
BEGIN
	SELECT
		TEMP_TABLE_TEAM_ACCOUNTS.ticketMonthYear,
		TEMP_TABLE_TEAM_ACCOUNTS.code_,
		TEMP_TABLE_TEAM_ACCOUNTS.accountName,
		TEMP_TABLE_ACCOUNT_DETAILS.ticketCount,
		TEMP_TABLE_ACCOUNT_DETAILS.averageClosedDelta,
		TEMP_TABLE_ACCOUNT_DETAILS.hotfixCount,
		CASE
			WHEN
				TEMP_TABLE_FEEDBACK_ONLY.averageFeedbackRating IS NULL
			THEN
				0
			ELSE
				TEMP_TABLE_FEEDBACK_ONLY.averageFeedbackRating
		END AS averageFeedbackRating
	FROM
		(
			SELECT DISTINCT
				LAST_DAY(OSB_TicketsView.closedDate) AS ticketMonthYear,
				OSB_TicketsView.accountEntryId,
				OSB_TicketsView.accountName,
				OSB_TicketsView.code_
			FROM
				OSB_TicketsView
			WHERE
				(TO_DAYS(OSB_TicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_TicketsView.closedDate) <= TO_DAYS(endDate)) AND
				OSB_TicketsView.supportRegion = supportRegion AND
				OSB_TicketsView.escalationLevel <> 'P1'

			UNION

			SELECT DISTINCT
				LAST_DAY(OSB_TicketsView.feedbackDate) AS ticketMonthYear,
				OSB_TicketsView.accountEntryId,
				OSB_TicketsView.accountName,
				OSB_TicketsView.code_
			FROM
				OSB_TicketsView
			WHERE
				(TO_DAYS(OSB_TicketsView.feedbackDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_TicketsView.feedbackDate) <= TO_DAYS(endDate)) AND
				OSB_TicketsView.closedDate IS NOT NULL AND
				OSB_TicketsView.averageFeedbackRating > 0 AND
				OSB_TicketsView.supportRegion = supportRegion AND
				OSB_TicketsView.escalationLevel <> 'P1'
		) TEMP_TABLE_TEAM_ACCOUNTS
		LEFT OUTER JOIN
		(
			SELECT
				LAST_DAY(TEMP_TABLE.closedDate) AS closedMonthYear,
				TEMP_TABLE.accountEntryId,
				COUNT(*) AS ticketCount,
				(SUM(TEMP_TABLE.closedDelta) / COUNT(*)) AS averageClosedDelta,
				SUM(TEMP_TABLE.hotfixRequired) AS hotfixCount
			FROM
				(
					SELECT DISTINCT
						OSB_TicketsView.ticketEntryId,
						OSB_TicketsView.closedDate,
						OSB_TicketsView.closedDelta,
						OSB_TicketsView.accountEntryId,
						OSB_TicketsView.hotfixRequired
					FROM
						OSB_TicketsView
					WHERE
						(TO_DAYS(OSB_TicketsView.closedDate) >= TO_DAYS(startDate)) AND
						(TO_DAYS(OSB_TicketsView.closedDate) <= TO_DAYS(endDate)) AND
						OSB_TicketsView.supportRegion = supportRegion AND
						OSB_TicketsView.escalationLevel <> 'P1'
				) TEMP_TABLE
			GROUP BY
				LAST_DAY(TEMP_TABLE.closedDate),
				TEMP_TABLE.accountEntryId
		) TEMP_TABLE_ACCOUNT_DETAILS ON TEMP_TABLE_TEAM_ACCOUNTS.ticketMonthYear = TEMP_TABLE_ACCOUNT_DETAILS.closedMonthYear AND TEMP_TABLE_TEAM_ACCOUNTS.accountEntryId = TEMP_TABLE_ACCOUNT_DETAILS.accountEntryId
		LEFT OUTER JOIN
		(
			SELECT
				CASE
					WHEN
						TO_DAYS(TEMP_TABLE.closedDate) >= TO_DAYS(startDate) AND
						TO_DAYS(TEMP_TABLE.closedDate) <= TO_DAYS(endDate)
					THEN
						LAST_DAY(TEMP_TABLE.closedDate)
					ELSE
						LAST_DAY(TEMP_TABLE.feedbackDate)
				END AS ticketMonthYear,
				TEMP_TABLE.accountEntryId,
				COUNT(*) AS ticketCount,
				(SUM(TEMP_TABLE.averageFeedbackRating) / COUNT(*)) AS averageFeedbackRating
			FROM
				(
					SELECT DISTINCT
						OSB_TicketsView.ticketEntryId,
						OSB_TicketsView.closedDate,
						OSB_TicketsView.accountEntryId,
						OSB_TicketsView.feedbackDate,
						OSB_TicketsView.averageFeedbackRating
					FROM
						OSB_TicketsView
					WHERE
						(
							(
								(TO_DAYS(OSB_TicketsView.closedDate) >= TO_DAYS(startDate)) AND
								(TO_DAYS(OSB_TicketsView.closedDate) <= TO_DAYS(endDate))
							) OR
							(
								(TO_DAYS(OSB_TicketsView.feedbackDate) >= TO_DAYS(startDate)) AND
								(TO_DAYS(OSB_TicketsView.feedbackDate) <= TO_DAYS(endDate)) AND
								(OSB_TicketsView.closedDate IS NOT NULL)
							)
						) AND
						OSB_TicketsView.averageFeedbackRating > 0 AND
						OSB_TicketsView.supportRegion = supportRegion AND
						OSB_TicketsView.escalationLevel <> 'P1'
				) TEMP_TABLE
			GROUP BY
				ticketMonthYear,
				TEMP_TABLE.accountEntryId
		) TEMP_TABLE_FEEDBACK_ONLY ON TEMP_TABLE_TEAM_ACCOUNTS.ticketMonthYear = TEMP_TABLE_FEEDBACK_ONLY.ticketMonthYear AND TEMP_TABLE_TEAM_ACCOUNTS.accountEntryId = TEMP_TABLE_FEEDBACK_ONLY.accountEntryId
	ORDER BY
		TEMP_TABLE_TEAM_ACCOUNTS.ticketMonthYear,
		TEMP_TABLE_TEAM_ACCOUNTS.code_;
END