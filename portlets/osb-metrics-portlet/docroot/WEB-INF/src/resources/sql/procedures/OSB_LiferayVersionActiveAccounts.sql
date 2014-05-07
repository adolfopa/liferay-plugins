CREATE PROCEDURE
	OSB_LiferayVersionActiveAccounts()
BEGIN
	SELECT
		TEMP_TABLE_ACTIVE_ACCOUNT.minorVersion AS envLFR,
		COUNT(*) AS accountCount
	FROM
	(
		SELECT
			OSB_LiferayVersionsView.minorVersion
		FROM
			(
				SELECT
					MAX(OSB_TicketsView.ticketEntryId) AS latestTicketEntryId,
					OSB_TicketsView.accountEntryId
				FROM
					OSB_TicketsView
				WHERE
					OSB_TicketsView.envLFRClassName = 'com.liferay.osb.model.ProductEntry.portalAllVersions' AND
					(
						OSB_TicketsView.envLFRType LIKE '5.1%' OR
						OSB_TicketsView.envLFRType LIKE '5.2%' OR
						OSB_TicketsView.envLFRType LIKE '6.0%' OR
						OSB_TicketsView.envLFRType LIKE '6.1%' OR
						OSB_TicketsView.envLFRType LIKE '6.2%'
					) AND
					(
						OSB_TicketsView.offeringType LIKE '%Gold' OR
						OSB_TicketsView.offeringType LIKE '%Platinum' OR
						OSB_TicketsView.offeringType LIKE '%Silver'
					)
				GROUP BY
					OSB_TicketsView.accountEntryId
			) TEMP_TABLE_LATEST_TICKET_ENTRY
			INNER JOIN OSB_TicketsView ON TEMP_TABLE_LATEST_TICKET_ENTRY.latestTicketEntryId = OSB_TicketsView.ticketEntryId
			INNER JOIN OSB_AccountsView ON OSB_AccountsView.accountEntryId = TEMP_TABLE_LATEST_TICKET_ENTRY.accountEntryId AND (OSB_AccountsView.accountStatus = 1)
			LEFT OUTER JOIN OSB_LiferayVersionsView ON OSB_TicketsView.envLFRType = OSB_LiferayVersionsView.liferayVersion
		GROUP BY
			OSB_TicketsView.accountEntryId,
			OSB_TicketsView.ticketEntryId
	) AS TEMP_TABLE_ACTIVE_ACCOUNT
	GROUP BY
		TEMP_TABLE_ACTIVE_ACCOUNT.minorVersion;
END