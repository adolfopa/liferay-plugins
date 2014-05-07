CREATE VIEW
	OSB_CSECommentsView
AS
	SELECT DISTINCT
		OSB_CSETicketsView.ticketEntryId,
		OSB_CSETicketsView.ticketWorkerUserId,
		OSB_CSETicketsView.ticketWorkerFirstName,
		OSB_CSETicketsView.ticketWorkerLastName,
		OSB_CSETicketsView.supportTeam,
		OSB_TicketComment.createDate AS commentDate
	FROM
		OSB_CSETicketsView
		INNER JOIN [$LRDCOM_DB$]OSB_TicketComment ON OSB_CSETicketsView.ticketEntryId = OSB_TicketComment.ticketEntryId AND OSB_CSETicketsView.ticketWorkerUserId = OSB_TicketComment.userId
	WHERE
		OSB_CSETicketsView.ticketWorkerUserId != 5;