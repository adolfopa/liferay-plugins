CREATE VIEW
	OSB_AccountsVersionView
AS
	SELECT
		OSB_TicketsView.accountEntryId,
		MIN(OSB_TicketsView.createDate) AS startDate,
		OSB_TicketsView.envLFRType
	FROM
		OSB_TicketsView
	GROUP BY
		OSB_TicketsView.accountEntryId,
		OSB_TicketsView.envLFRType;