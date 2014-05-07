CREATE VIEW
	OSB_TicketsView
AS
	SELECT DISTINCT
		OSB_TicketEntry.ticketEntryId,
		OSB_TicketEntry.ticketId,
		OSB_TicketEntry.createDate,
		OSB_TicketsClosedAndDueDateView.closedDate,
		CASE
			WHEN
				OSB_TicketsClosedAndDueDateView.dueDate IS NOT NULL
			THEN
				OSB_TicketsClosedAndDueDateView.dueDate
			ELSE
				OSB_TicketEntry.dueDate
		END AS dueDate,
		CASE
			WHEN
				OSB_TicketsClosedAndDueDateView.closedDate IS NOT NULL
			THEN
				(TO_DAYS(OSB_TicketsClosedAndDueDateView.dueDate) - TO_DAYS(OSB_TicketsClosedAndDueDateView.closedDate))
			ELSE
				NULL
		END AS closedDelta,
		CASE
			WHEN
				(OSB_TicketsClosedAndDueDateView.closedDate IS NOT NULL) AND
				(OSB_TicketsClosedAndDueDateView.closedDate > OSB_TicketsClosedAndDueDateView.dueDate)
			THEN
				1
			ELSE
				0
		END AS overSLA,
		CASE
			WHEN
				HOUR(TIMEDIFF(OSB_TicketEntry.closedDate, OSB_TicketEntry.createDate)) < 24
			THEN
				1
			ELSE
				0
		END AS resolvedInDay,
		CASE
			WHEN
				OSB_TicketEntry.escalationLevel = 31000
			THEN
				'P1'
			WHEN
				OSB_TicketEntry.escalationLevel = 31001
			THEN
				'1'
			WHEN
				OSB_TicketEntry.escalationLevel = 31002
			THEN
				'2'
		END AS escalationLevel,
		CASE
			WHEN
				OSB_TicketsEscalatedView.escalatedDate IS NOT NULL
			THEN
				1
			ELSE
				0
		END AS escalated,
		CASE
			WHEN
				OSB_TicketEntry.severity = 1
			THEN
				'Minor'
			WHEN
				OSB_TicketEntry.severity = 2
			THEN
				'Major'
			WHEN
				OSB_TicketEntry.severity = 3
			THEN
				'Critical'
		END AS severityType,
		OSB_AccountEntry.accountEntryId,
		OSB_AccountEntry.name AS accountName,
		OSB_AccountEntry.code_,
		OSB_ProductEntry.name AS productName,
		CASE
			WHEN
				OSB_SupportResponse.localeType = 2 AND
				OSB_SupportResponse.timeZoneType = 1
			THEN
				'Support-US'
			WHEN
				OSB_SupportResponse.localeType = 2 AND
				OSB_SupportResponse.timeZoneType = 2
			THEN
				'Support-CN'
			WHEN
				OSB_SupportResponse.localeType = 2 AND
				OSB_SupportResponse.timeZoneType = 3
			THEN
				'Support-HU'
			WHEN
				OSB_SupportResponse.localeType = 2 AND
				OSB_SupportResponse.timeZoneType = 4
			THEN
				'Support-IN'
			WHEN
				OSB_SupportResponse.localeType = 3 AND
				OSB_SupportResponse.timeZoneType = 1
			THEN
				'Support-BR'
			WHEN
				OSB_SupportResponse.localeType = 4 AND
				OSB_SupportResponse.timeZoneType = 3
			THEN
				'Support-ES'
		END AS supportRegion,
		OSB_SupportResponse.name AS offeringType,
		Component.listTypeId AS component,
		Component.name AS componentType,
		CASE
			WHEN
				EnvOS.name IS NOT NULL
			THEN
				EnvOS.name
			ELSE
				'Unspecified'
		END AS envOSType,
		CASE
			WHEN
				EnvDB.name IS NOT NULL
			THEN
				EnvDB.name
			ELSE
				'Unspecified'
		END AS envDBType,
		CASE
			WHEN
				EnvJVM.name IS NOT NULL
			THEN
				EnvJVM.name
			ELSE
				'Unspecified'
		END AS envJVMType,
		CASE
			WHEN
				EnvAS.name IS NOT NULL
			THEN
				EnvAS.name
			ELSE
				'Unspecified'
		END AS envASType,
		EnvLFR.type_ AS envLFRClassName,
		CASE
			WHEN
				EnvLFR.name IS NOT NULL
			THEN
				EnvLFR.name
			ELSE
				'Unspecified'
		END AS envLFRType,
		Resolution.name AS resolutionType,
		User_.userId AS ticketWorkerUserId,
		User_.firstName AS ticketWorkerFirstName,
		User_.lastName AS ticketWorkerLastName,
		CASE
			WHEN
				OSB_TicketsEscalatedView.escalatedDate IS NOT NULL AND
				OSB_TicketsEscalatedView.ticketWorkerRole = 2
			THEN
				1
			ELSE
				0
		END AS ticketEscalater,
		CASE
			WHEN
				OSB_SupportTeam.name LIKE 'Support-AU%'
			THEN
				'Support-AU'
			WHEN
				OSB_SupportTeam.name LIKE 'Support-BR%'
			THEN
				'Support-BR' 
			WHEN
				OSB_SupportTeam.name LIKE 'Support-CN%'
			THEN
				'Support-CN'
			WHEN
				OSB_SupportTeam.name LIKE 'Support-ES%'
			THEN
				'Support-ES'
			WHEN
				OSB_SupportTeam.name LIKE 'Support-HU%'
			THEN
				'Support-HU'
			WHEN
				OSB_SupportTeam.name LIKE 'Support-India%'
			THEN
				'Support-IN'
			WHEN
				OSB_SupportTeam.name LIKE 'Support-US%'
			THEN
				'Support-US'
			ELSE
				'n/a'
		END AS supportTeam,
		CASE
			WHEN
				OSB_TicketAttachmentsView.ticketEntryId IS NULL
			THEN
				0
			ELSE
				1
		END AS hotfixRequired,
		OSB_TicketFeedback.modifiedDate AS feedbackDate,
		OSB_TicketFeedback.rating1,
		OSB_TicketFeedback.rating2,
		OSB_TicketFeedback.rating3,
		OSB_TicketFeedback.rating4,
		OSB_TicketFeedback.comments,
		CASE
			WHEN
				OSB_TicketFeedback.rating1 is NULL OR
				OSB_TicketFeedback.rating2 is NULL OR
				OSB_TicketFeedback.rating3 is NULL OR
				OSB_TicketFeedback.rating4 is NULL
			THEN
				0
			ELSE
				((OSB_TicketFeedback.rating1 + OSB_TicketFeedback.rating2 + OSB_TicketFeedback.rating3 + OSB_TicketFeedback.rating4) / 4.0)
		END AS averageFeedbackRating
	FROM
		[$LRDCOM_DB$]OSB_TicketEntry
		INNER JOIN [$LRDCOM_DB$]OSB_AccountEntry ON OSB_TicketEntry.accountEntryId = OSB_AccountEntry.accountEntryId
		INNER JOIN [$LRDCOM_DB$]OSB_OfferingEntry ON OSB_TicketEntry.offeringEntryId = OSB_OfferingEntry.offeringEntryId
		INNER JOIN [$LRDCOM_DB$]OSB_OfferingDefinition ON OSB_OfferingEntry.offeringDefinitionId = OSB_OfferingDefinition.offeringDefinitionId
		INNER JOIN [$LRDCOM_DB$]OSB_ProductEntry ON OSB_OfferingDefinition.productEntryId = OSB_ProductEntry.productEntryId
		INNER JOIN [$LRDCOM_DB$]OSB_SupportResponse ON OSB_OfferingDefinition.supportResponseId = OSB_SupportResponse.supportResponseId
		LEFT OUTER JOIN [$LRDCOM_DB$]ListType Component ON OSB_TicketEntry.component = Component.listTypeId
		LEFT OUTER JOIN [$LRDCOM_DB$]ListType EnvOS ON OSB_TicketEntry.envOS = EnvOS.listTypeId
		LEFT OUTER JOIN [$LRDCOM_DB$]ListType EnvDB ON OSB_TicketEntry.envDB = EnvDB.listTypeId
		LEFT OUTER JOIN [$LRDCOM_DB$]ListType EnvJVM ON OSB_TicketEntry.envJVM = EnvJVM.listTypeId
		LEFT OUTER JOIN [$LRDCOM_DB$]ListType EnvAS ON OSB_TicketEntry.envAS = EnvAS.listTypeId
		LEFT OUTER JOIN [$LRDCOM_DB$]ListType EnvLFR ON OSB_TicketEntry.envLFR = EnvLFR.listTypeId
		LEFT OUTER JOIN [$LRDCOM_DB$]ListType Resolution ON OSB_TicketEntry.resolution = Resolution.listTypeId
		LEFT OUTER JOIN [$LRDCOM_DB$]OSB_TicketWorker ON OSB_TicketEntry.ticketEntryId = OSB_TicketWorker.ticketEntryId AND ((OSB_TicketWorker.role = 2) OR (OSB_TicketWorker.role = 3))
		LEFT OUTER JOIN [$LRDCOM_DB$]User_ ON OSB_TicketWorker.userId = User_.userId
		LEFT OUTER JOIN [$LRDCOM_DB$]OSB_SupportWorker ON OSB_TicketWorker.userId = OSB_SupportWorker.userId
		LEFT OUTER JOIN [$LRDCOM_DB$]OSB_SupportTeam ON OSB_SupportWorker.supportTeamId = OSB_SupportTeam.supportTeamId
		LEFT OUTER JOIN [$LRDCOM_DB$]OSB_TicketFeedback ON OSB_TicketEntry.ticketEntryId = OSB_TicketFeedback.ticketEntryId
		LEFT OUTER JOIN OSB_TicketAttachmentsView ON OSB_TicketEntry.ticketEntryId = OSB_TicketAttachmentsView.ticketEntryId
		LEFT OUTER JOIN OSB_TicketsClosedAndDueDateView ON OSB_TicketEntry.ticketEntryId = OSB_TicketsClosedAndDueDateView.ticketEntryId
		LEFT OUTER JOIN OSB_TicketsEscalatedView ON OSB_TicketEntry.ticketEntryId = OSB_TicketsEscalatedView.ticketEntryId AND (OSB_TicketWorker.userId = OSB_TicketsEscalatedView.ticketWorkerUserId)
	WHERE
		OSB_AccountEntry.accountEntryId != 6040682 AND
		OSB_AccountEntry.accountEntryId != 10975889;