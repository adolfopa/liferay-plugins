CREATE PROCEDURE
	OSB_ExpiredAndExpiringAccounts(IN startDate DATETIME, IN numberOfExpiringDays VARCHAR(3))
BEGIN
	SELECT DISTINCT
		OSB_AccountsView.accountEntryId,
		OSB_AccountsView.account,
		OSB_AccountsView.code_,
		OSB_AccountsView.accountWorkerFirstName AS salesRepresentativeFirstName,
		OSB_AccountsView.accountWorkerLastName AS salesRepresentativeLastName,
		CASE
			WHEN
				TEMP_TABLE.accountStatus = 3
			THEN
				'Expired'
			ELSE
				'Expiring'
		END AS expireStatus,
		DATE(TEMP_TABLE.supportEndDate) AS supportEndDate
	FROM
		OSB_AccountsView
		LEFT OUTER JOIN
		(
			SELECT
				OSB_AccountsView.accountEntryId,
				OSB_AccountsView.accountStatus,
				MAX(OSB_AccountsView.supportEndDate) AS supportEndDate
			FROM
				OSB_AccountsView
			GROUP BY
				OSB_AccountsView.accountEntryId
		) TEMP_TABLE ON OSB_AccountsView.accountEntryId = TEMP_TABLE.accountEntryId
	WHERE
		(TEMP_TABLE.supportEndDate <= DATE_ADD(startDate, INTERVAL numberOfExpiringDays DAY)) AND
		TEMP_TABLE.accountStatus != 2 AND
		OSB_AccountsView.accountWorkerRole = 3
	ORDER BY
		salesRepresentativeFirstName,
		salesRepresentativeLastName,
		supportEndDate ASC;
END