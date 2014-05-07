CREATE PROCEDURE
	OSB_FirstOpenedAndLastClosedTicketDateByLiferayVersionAndAccount()
BEGIN
	(
		SELECT DISTINCT
			MIN(OSB_TicketsView.createDate) AS ticketDate,
			OSB_TicketsView.code_,
			OSB_LiferayVersionsView.minorVersion,
			CASE
				WHEN
					OSB_AccountsView.accountStatus = 1
				THEN
					'Active'
				WHEN
					OSB_AccountsView.accountStatus = 2
				THEN
					'Closed'
				WHEN
					OSB_AccountsView.accountStatus = 3
				THEN
					'Expired'
			END AS accountStatus,
			CASE
				WHEN
					OSB_TicketsView.offeringType LIKE '%Gold'
				THEN
					'Gold'
				WHEN
					OSB_TicketsView.offeringType LIKE '%Platinum'
				THEN
					'Platinum'
				WHEN
					OSB_TicketsView.offeringType LIKE '%Silver'
				THEN
					'Silver'
			END AS offeringType,
			'First Opened' AS firstOpenedOrLastClosed
		FROM
			OSB_LiferayVersionsView
			LEFT OUTER JOIN OSB_TicketsView ON OSB_LiferayVersionsView.liferayVersion = OSB_TicketsView.envLFRType
			INNER JOIN OSB_AccountsView ON OSB_TicketsView.accountEntryId = OSB_AccountsView.accountEntryId
		WHERE
			OSB_LiferayVersionsView.minorVersion != '4.4'
		GROUP BY
			OSB_LiferayVersionsView.minorVersion,
			OSB_TicketsView.code_
		ORDER BY
			OSB_TicketsView.code_,
			OSB_LiferayVersionsView.minorVersion
	)
	UNION
	(
		SELECT DISTINCT
			CASE
				WHEN(
					SELECT
						MAX(OSB_LiferayVersionsView.minorVersion)
					FROM
						OSB_TicketsView AS OSB_TicketsView_A
						LEFT OUTER JOIN OSB_LiferayVersionsView ON OSB_TicketsView_A.envLFRType = OSB_LiferayVersionsView.liferayVersion
					WHERE
						OSB_TicketsView_A.code_ = OSB_TicketsView.code_
				) = OSB_LiferayVersionsView.minorVersion
				THEN
					NULL
				ELSE
					MAX(OSB_TicketsView.closedDate)
			END AS ticketDate,
			OSB_TicketsView.code_,
			OSB_LiferayVersionsView.minorVersion,
			CASE
				WHEN
					OSB_AccountsView.accountStatus = 1
				THEN
					'Active'
				WHEN
					OSB_AccountsView.accountStatus = 2
				THEN
					'Closed'
				WHEN
					OSB_AccountsView.accountStatus = 3
				THEN
					'Expired'
			END AS accountStatus,
			CASE
				WHEN
					OSB_TicketsView.offeringType LIKE '%Gold'
				THEN
					'Gold'
				WHEN
					OSB_TicketsView.offeringType LIKE '%Platinum'
				THEN
					'Platinum'
				WHEN
					OSB_TicketsView.offeringType LIKE '%Silver'
				THEN
					'Silver'
			END AS offeringType,
			'Last Closed' AS firstOpenedOrLastClosed
		FROM
			OSB_LiferayVersionsView
			LEFT OUTER JOIN OSB_TicketsView ON OSB_LiferayVersionsView.liferayVersion = OSB_TicketsView.envLFRType
			INNER JOIN OSB_AccountsView ON OSB_TicketsView.accountEntryId = OSB_AccountsView.accountEntryId
		WHERE
			OSB_TicketsView.closedDate IS NOT NULL AND
			OSB_LiferayVersionsView.minorVersion != '4.4'
		GROUP BY
			OSB_LiferayVersionsView.minorVersion,
			OSB_TicketsView.code_
		ORDER BY
			OSB_TicketsView.code_,
			OSB_LiferayVersionsView.minorVersion
	)
	ORDER BY
		code_,
		minorVersion,
		firstOpenedOrLastClosed;
END