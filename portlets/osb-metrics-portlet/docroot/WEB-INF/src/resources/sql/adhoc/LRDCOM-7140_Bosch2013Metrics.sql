SELECT DISTINCT
	CONCAT('http://www.liferay.com/group/customer/support/-/support/ticket/', OSB_CSETicketsView.code_, '-', OSB_CSETicketsView.ticketId) AS ticketURL,
	OSB_CSETicketsView.severityType,
	DATE_ADD(OSB_CSETicketsView.createDate, INTERVAL 1 HOUR) AS createDate,
	DATE_ADD(OSB_CSETicketsView.closedDate, INTERVAL 1 HOUR) AS closedDate,
	OSB_CSETicketsView.closedDelta,
	OSB_CSETicketsView.envLFRType
FROM
	OSB_CSETicketsView
WHERE
	OSB_CSETicketsView.code_ = 'BOSCH' AND
	YEAR(OSB_CSETicketsView.createDate) = '2013' AND
	OSB_CSETicketsView.closedDate IS NOT NULL
ORDER BY
	OSB_CSETicketsView.severityType,
	OSB_CSETicketsView.ticketId,
	OSB_CSETicketsView.createDate,
	OSB_CSETicketsView.closedDate;