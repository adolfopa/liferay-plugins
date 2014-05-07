CREATE PROCEDURE
	OSB_AccountEnvironmentStatisticsByMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
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
		OSB_TicketsView.envOSType,
		OSB_TicketsView.envDBType,
		OSB_TicketsView.envJVMType,
		OSB_TicketsView.envASType,
		OSB_TicketsView.envLFRType,
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
		OSB_TicketsView.envASType,
		OSB_TicketsView.envDBType,
		OSB_TicketsView.envOSType,
		OSB_TicketsView.envJVMType,
		OSB_AccountsView.strategicAccount
	ORDER BY
		createdMonthYear,
		OSB_TicketsView.productName,
		accountType,
		ticketsCount DESC;
END