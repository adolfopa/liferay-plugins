CREATE VIEW
	OSB_TicketAttachmentsView
AS
	SELECT
		OSB_TicketAttachment.ticketEntryId,
		OSB_TicketAttachment.fileName
	FROM
		[$LRDCOM_DB$]OSB_TicketAttachment
	WHERE
		OSB_TicketAttachment.type_ = 1
	GROUP BY
		OSB_TicketAttachment.ticketEntryId;