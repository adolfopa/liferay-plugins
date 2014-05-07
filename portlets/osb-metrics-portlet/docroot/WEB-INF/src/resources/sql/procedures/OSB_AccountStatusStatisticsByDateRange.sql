CREATE PROCEDURE
	OSB_AccountStatusStatisticsByDateRange(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT
		DATE(startDate) AS startDate,
		DATE(endDate) AS endDate,
		SUM(
			CASE
				WHEN
					TEMP_TABLE.accountStatus = 'Active' AND
					TEMP_TABLE.renewStatus = 'New'
				THEN
					1
				ELSE
					0
			END
		) AS 'New Account',
		SUM(
			CASE
				WHEN
					TEMP_TABLE.accountStatus = 'Active' AND
					TEMP_TABLE.renewStatus = 'New Renewal'
				THEN
					1
				ELSE
					0
			END
		) AS 'New Renewal Account',
		SUM(
			CASE
				WHEN
					TEMP_TABLE.accountStatus = 'Active' AND
					TEMP_TABLE.renewStatus = 'N/A'
				THEN
					1
				ELSE
					0
			END
		) AS 'Active Account',
		SUM(
			CASE
				WHEN
					TEMP_TABLE.accountStatus = 'Active' AND
					TEMP_TABLE.renewStatus = 'Renewal'
				THEN
					1
				ELSE
					0
			END
		) AS 'Active Renewal Account',
		SUM(
			CASE
				WHEN
					TEMP_TABLE.accountStatus = 'Closed' AND
					TEMP_TABLE.renewStatus = 'N/A'
				THEN
					1
				ELSE
					0
			END
		) AS 'Closed Account',
		SUM(
			CASE
				WHEN
					TEMP_TABLE.accountStatus = 'Closed' AND
					TEMP_TABLE.renewStatus = 'Renewal'
				THEN
					1
				ELSE
					0
			END
		) AS 'Closed Renewal Account'
	FROM
		(
			SELECT
				OSB_AccountsView.accountEntryId,
				CASE
					WHEN
						(
							OSB_AccountsView.accountStatus = 2 OR
							OSB_AccountsView.accountStatus = 3
						) AND
						TO_DAYS(DATE(OSB_AccountsView.accountStatusModifiedDate)) >= TO_DAYS(startDate) AND
						TO_DAYS(DATE(OSB_AccountsView.accountStatusModifiedDate)) <= TO_DAYS(endDate)
					THEN
						'Closed'
					WHEN
						OSB_AccountsView.accountStatus = 1
					THEN
						'Active'
					ELSE
						'N/A'
				END AS accountStatus,
				CASE
					WHEN
						MAX(OSB_AccountsView.renewCount) = 0 AND
						TO_DAYS(DATE(MIN(OSB_AccountsView.supportStartDate))) >= TO_DAYS(startDate) AND
						TO_DAYS(DATE(MIN(OSB_AccountsView.supportStartDate))) <= TO_DAYS(endDate)
					THEN
						'New'
					WHEN
						MAX(OSB_AccountsView.renewCount) > 0 AND
						TO_DAYS(DATE(MIN(OSB_AccountsView.supportStartDate))) >= TO_DAYS(startDate) AND
						TO_DAYS(DATE(MIN(OSB_AccountsView.supportStartDate))) <= TO_DAYS(endDate)
					THEN
						'New Renewal'
					WHEN
						MAX(OSB_AccountsView.renewCount) > 0
					THEN
						'Renewal'
					ELSE
						'N/A'
				END AS renewStatus
			FROM
				OSB_AccountsView
			GROUP BY
				OSB_AccountsView.accountEntryId
		) TEMP_TABLE;
END