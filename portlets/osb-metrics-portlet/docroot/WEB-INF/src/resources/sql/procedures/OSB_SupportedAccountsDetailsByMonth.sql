CREATE PROCEDURE
	OSB_SupportedAccountsDetailsByMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	DECLARE date DATETIME;

	CREATE TEMPORARY TABLE supportedAccountsDetails (
		partnerManagedSupport BOOLEAN,
		supportRegion VARCHAR(20),
		count INT,
		date DATETIME
	);

	SET date = startDate;

	WHILE date <= LAST_DAY(endDate) DO
		SET date = LAST_DAY(date);

		INSERT INTO supportedAccountsDetails (
			SELECT
				OSB_AccountsView.partnerManagedSupport,
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
				OSB_AccountsView.supportRegion,
				OSB_AccountsView.partnerManagedSupport
		);

		SET date = DATE_ADD(date, INTERVAL 1 MONTH);
	END WHILE;

	SELECT
		supportedAccountsDetails.supportRegion,
		supportedAccountsDetails.partnerManagedSupport,
		supportedAccountsDetails.count,
		supportedAccountsDetails.date
	FROM
		supportedAccountsDetails
	ORDER BY
		supportedAccountsDetails.date,
		supportedAccountsDetails.supportRegion,
		supportedAccountsDetails.partnerManagedSupport;

	DROP TABLE supportedAccountsDetails;
END