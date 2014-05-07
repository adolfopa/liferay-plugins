CREATE VIEW
	OSB_CSETicketsView
AS
	SELECT
		*
	FROM
		OSB_TicketsView
	WHERE
		OSB_TicketsView.component != 26008;