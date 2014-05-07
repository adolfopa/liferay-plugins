CREATE PROCEDURE
	OSB_MasterFeedbackAndTicketClosedListByMonthAndSupportRegion(IN startDate DATETIME, IN endDate DATETIME, IN supportRegion VARCHAR(15))
BEGIN
	SELECT DISTINCT
		OSB_TicketsView.ticketEntryId,
		LAST_DAY(OSB_TicketsView.closedDate) AS closedMonthYear,
		LAST_DAY(OSB_TicketsView.feedbackDate) AS feedbackMonthYear,
		CASE
			WHEN
				TO_DAYS(OSB_TicketsView.closedDate) < TO_DAYS(startDate)
			THEN
				1
			ELSE
				0
		END AS feedbackOnly,
		CONCAT('http://www.liferay.com/group/customer/support/-/support/ticket/', OSB_TicketsView.code_, '-', OSB_TicketsView.ticketId) AS ticketLink,
		OSB_TicketsView.code_,
		CASE
			WHEN
				OSB_TicketsView.rating1 IS NULL
			THEN
				0
			ELSE
				OSB_TicketsView.rating1
		END AS finalResolutionScore,
		CASE
			WHEN
				OSB_TicketsView.rating2 IS NULL
			THEN
				0
			ELSE
				OSB_TicketsView.rating2
		END AS responseTimeScore,
		CASE
			WHEN
				OSB_TicketsView.rating3 IS NULL
			THEN
				0
			ELSE
				OSB_TicketsView.rating3
		END AS technicalKnowledgeScore,
		CASE
			WHEN
				OSB_TicketsView.rating4 IS NULL
			THEN
				0
			ELSE
				OSB_TicketsView.rating4
		END AS professionalismScore,
		OSB_TicketsView.averageFeedbackRating,
		CASE
			WHEN
				OSB_TicketsView.comments IS NULL
			THEN
				''
			ELSE
				OSB_TicketsView.comments
		END AS feedbackComments
	FROM
		OSB_TicketsView
	WHERE
		(
			(
				(TO_DAYS(OSB_TicketsView.closedDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_TicketsView.closedDate) <= TO_DAYS(endDate))
			) OR
			(
				(TO_DAYS(OSB_TicketsView.feedbackDate) >= TO_DAYS(startDate)) AND
				(TO_DAYS(OSB_TicketsView.feedbackDate) <= TO_DAYS(endDate)) AND
				(OSB_TicketsView.closedDate IS NOT NULL) AND
				(OSB_TicketsView.averageFeedbackRating > 0)
			)
		) AND
		OSB_TicketsView.escalationLevel <> 'P1' AND
		OSB_TicketsView.supportRegion = supportRegion
	ORDER BY
		OSB_TicketsView.code_,
		OSB_TicketsView.ticketId;
END