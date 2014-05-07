CREATE VIEW
	OSB_ProvisioningTicketsView
AS
	SELECT
		*
	FROM
		OSB_TicketsView
	WHERE
		OSB_TicketsView.component = 26008;