CREATE PROCEDURE
	OSB_MasterFeedbackListByMonth(IN startDate DATETIME, IN endDate DATETIME)
BEGIN
	SELECT DISTINCT
		LAST_DAY(OSB_CSETicketsView.feedbackDate) AS feedbackMonthYear,
		CONCAT('http://www.liferay.com/group/customer/support/-/support/ticket/', OSB_CSETicketsView.code_, '-', OSB_CSETicketsView.ticketId) AS ticketLink,
		OSB_CSETicketsView.accountName,
		OSB_CSETicketsView.supportRegion,
		OSB_CSETicketsView.rating1 AS finalResolutionScore,
		OSB_CSETicketsView.rating2 AS responseTimeScore,
		OSB_CSETicketsView.rating3 AS technicalKnowledgeScore,
		OSB_CSETicketsView.rating4 AS professionalismScore,
		OSB_CSETicketsView.comments AS comments,
		OSB_CSETicketsView.averageFeedbackRating AS avgFeedbackRating
	FROM
		OSB_CSETicketsView
	WHERE
		(TO_DAYS(OSB_CSETicketsView.feedbackDate) >= TO_DAYS(startDate)) AND
		(TO_DAYS(OSB_CSETicketsView.feedbackDate) <= TO_DAYS(endDate)) AND
		OSB_CSETicketsView.averageFeedbackRating > 0
	ORDER BY
		feedbackMonthYear,
		OSB_CSETicketsView.accountName,
		OSB_CSETicketsView.supportRegion;
END