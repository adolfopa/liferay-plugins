CREATE VIEW
	OSB_TicketsVersionDeltaView
AS
	SELECT DISTINCT
		OSB_TicketsView.ticketEntryId,
		OSB_TicketsView.code_,
		OSB_TicketsView.productName,
		OSB_TicketsView.envLFRClassName AS versionClassName,
		OSB_TicketsView.envLFRType AS versionName,
		CEIL((TO_DAYS(OSB_TicketsView.createDate) - TO_DAYS(OSB_AccountsVersionView.startDate)) / 30) AS monthX
	FROM
		OSB_TicketsView
		INNER JOIN OSB_AccountsVersionView ON OSB_TicketsView.accountEntryId = OSB_AccountsVersionView.accountEntryId AND OSB_TicketsView.envLFRType = OSB_AccountsVersionView.envLFRType
	WHERE
		OSB_TicketsView.envLFRType != 'Unspecified';