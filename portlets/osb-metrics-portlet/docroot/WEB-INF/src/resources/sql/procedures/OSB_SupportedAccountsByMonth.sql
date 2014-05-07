CREATE PROCEDURE
	OSB_SupportedAccountsByMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	DECLARE date DATETIME;

	CREATE TEMPORARY TABLE supportedAccounts (
		supportRegion VARCHAR(20),
		count INT,
		date DATETIME
	);

	SET date = startDate;

	WHILE date <= LAST_DAY(endDate) DO
		SET date = LAST_DAY(date);

		INSERT INTO supportedAccounts (
			SELECT
				OSB_AccountsView.supportRegion,
				COUNT(DISTINCT OSB_AccountsView.account) AS count,
				date
			FROM
				OSB_AccountsView
			WHERE
				OSB_AccountsView.supportTeam LIKE 'Support%' AND
				OSB_AccountsView.supportTeam NOT LIKE 'Support-India%' AND
				OSB_AccountsView.severity1Response != 0 AND
				date BETWEEN OSB_AccountsView.supportStartDate AND LAST_DAY(OSB_AccountsView.supportEndDate)
			GROUP BY
				OSB_AccountsView.supportRegion
		);

		SET date = DATE_ADD(date, INTERVAL 1 MONTH);
	END WHILE;

	SELECT
		supportedAccounts.supportRegion,
		supportedAccounts.count,
		supportedAccounts.date
	FROM
		supportedAccounts
	ORDER BY
		supportedAccounts.date,
		supportedAccounts.supportRegion;

	DROP TABLE supportedAccounts;
END