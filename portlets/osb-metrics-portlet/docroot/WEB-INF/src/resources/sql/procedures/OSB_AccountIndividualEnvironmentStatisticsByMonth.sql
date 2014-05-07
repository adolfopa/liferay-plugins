CREATE PROCEDURE
	OSB_AccountIndividualEnvironmentStatisticsByMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	(
		SELECT
			LAST_DAY(OSB_TicketsView.createDate) AS createdMonthYear,
			CASE
				WHEN
					OSB_AccountsView.strategicAccount = 1
				THEN
					'Strategic Account'
				ELSE
					'Regular Account'
			END AS accountType,
			OSB_TicketsView.productName,
			'Operating System' AS envType,
			OSB_TicketsView.envOSType AS envName,
			COUNT(DISTINCT OSB_TicketsView.ticketEntryId) AS ticketsCount
		FROM
			OSB_TicketsView
			INNER JOIN OSB_AccountsView ON OSB_TicketsView.accountEntryId = OSB_AccountsView.accountEntryId
		WHERE
			(TO_DAYS(OSB_TicketsView.createDate) >= TO_DAYS(startDate)) AND
			(TO_DAYS(OSB_TicketsView.createDate) <= TO_DAYS(endDate)) AND
			(
				OSB_TicketsView.productName LIKE '%Production' OR
				OSB_TicketsView.productName LIKE '%Non-Production'
			) AND
			OSB_AccountsView.accountStatus = 1
		GROUP BY
			LAST_DAY(OSB_TicketsView.createDate),
			OSB_TicketsView.productName,
			OSB_TicketsView.envOSType,
			OSB_AccountsView.strategicAccount
	)
	UNION
	(
		SELECT
			LAST_DAY(OSB_TicketsView.createDate) AS createdMonthYear,
			CASE
				WHEN
					OSB_AccountsView.strategicAccount = 1
				THEN
					'Strategic Account'
				ELSE
					'Regular Account'
			END AS accountType,
			OSB_TicketsView.productName,
			'Database' AS envType,
			OSB_TicketsView.envDBType AS envName,
			COUNT(DISTINCT OSB_TicketsView.ticketEntryId) AS ticketsCount
		FROM
			OSB_TicketsView
			INNER JOIN OSB_AccountsView ON OSB_TicketsView.accountEntryId = OSB_AccountsView.accountEntryId
		WHERE
			(TO_DAYS(OSB_TicketsView.createDate) >= TO_DAYS(startDate)) AND
			(TO_DAYS(OSB_TicketsView.createDate) <= TO_DAYS(endDate)) AND
			(
				OSB_TicketsView.productName LIKE '%Production' OR
				OSB_TicketsView.productName LIKE '%Non-Production'
			) AND
			OSB_AccountsView.accountStatus = 1
		GROUP BY
			LAST_DAY(OSB_TicketsView.createDate),
			OSB_TicketsView.productName,
			OSB_TicketsView.envDBType,
			OSB_AccountsView.strategicAccount
	)
	UNION
	(
		SELECT
			LAST_DAY(OSB_TicketsView.createDate) AS createdMonthYear,
			CASE
				WHEN
					OSB_AccountsView.strategicAccount = 1
				THEN
					'Strategic Account'
				ELSE
					'Regular Account'
			END AS accountType,
			OSB_TicketsView.productName,
			'Java Virtual Machine' AS envType,
			OSB_TicketsView.envJVMType AS envName,
			COUNT(DISTINCT OSB_TicketsView.ticketEntryId) AS ticketsCount
		FROM
			OSB_TicketsView
			INNER JOIN OSB_AccountsView ON OSB_TicketsView.accountEntryId = OSB_AccountsView.accountEntryId
		WHERE
			(TO_DAYS(OSB_TicketsView.createDate) >= TO_DAYS(startDate)) AND
			(TO_DAYS(OSB_TicketsView.createDate) <= TO_DAYS(endDate)) AND
			(
				OSB_TicketsView.productName LIKE '%Production' OR
				OSB_TicketsView.productName LIKE '%Non-Production'
			) AND
			OSB_AccountsView.accountStatus = 1
		GROUP BY
			LAST_DAY(OSB_TicketsView.createDate),
			OSB_TicketsView.productName,
			OSB_TicketsView.envJVMType,
			OSB_AccountsView.strategicAccount
	)
	UNION
	(
		SELECT
			LAST_DAY(OSB_TicketsView.createDate) AS createdMonthYear,
			CASE
				WHEN
					OSB_AccountsView.strategicAccount = 1
				THEN
					'Strategic Account'
				ELSE
					'Regular Account'
			END AS accountType,
			OSB_TicketsView.productName,
			'Application Server' AS envType,
			OSB_TicketsView.envASType AS envName,
			COUNT(DISTINCT OSB_TicketsView.ticketEntryId) AS ticketsCount
		FROM
			OSB_TicketsView
			INNER JOIN OSB_AccountsView ON OSB_TicketsView.accountEntryId = OSB_AccountsView.accountEntryId
		WHERE
			(TO_DAYS(OSB_TicketsView.createDate) >= TO_DAYS(startDate)) AND
			(TO_DAYS(OSB_TicketsView.createDate) <= TO_DAYS(endDate)) AND
			(
				OSB_TicketsView.productName LIKE '%Production' OR
				OSB_TicketsView.productName LIKE '%Non-Production'
			) AND
			OSB_AccountsView.accountStatus = 1
		GROUP BY
			LAST_DAY(OSB_TicketsView.createDate),
			OSB_TicketsView.productName,
			OSB_TicketsView.envASType,
			OSB_AccountsView.strategicAccount
	)
	UNION
	(
		SELECT
			LAST_DAY(OSB_TicketsView.createDate) AS createdMonthYear,
			CASE
				WHEN
					OSB_AccountsView.strategicAccount = 1
				THEN
					'Strategic Account'
				ELSE
					'Regular Account'
			END AS accountType,
			OSB_TicketsView.productName,
			'Liferay Version' AS envType,
			OSB_TicketsView.envLFRType AS envName,
			COUNT(DISTINCT OSB_TicketsView.ticketEntryId) AS ticketsCount
		FROM
			OSB_TicketsView
			INNER JOIN OSB_AccountsView ON OSB_TicketsView.accountEntryId = OSB_AccountsView.accountEntryId
		WHERE
			(TO_DAYS(OSB_TicketsView.createDate) >= TO_DAYS(startDate)) AND
			(TO_DAYS(OSB_TicketsView.createDate) <= TO_DAYS(endDate)) AND
			(
				OSB_TicketsView.productName LIKE '%Production' OR
				OSB_TicketsView.productName LIKE '%Non-Production'
			) AND
			OSB_AccountsView.accountStatus = 1
		GROUP BY
			LAST_DAY(OSB_TicketsView.createDate),
			OSB_TicketsView.productName,
			OSB_TicketsView.envLFRType,
			OSB_AccountsView.strategicAccount
	)
	ORDER BY
		createdMonthYear,
		productName,
		accountType,
		envType,
		ticketsCount DESC,
		envName;
END