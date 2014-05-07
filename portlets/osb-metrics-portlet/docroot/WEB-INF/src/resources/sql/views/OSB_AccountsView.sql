CREATE VIEW
	OSB_AccountsView
AS
	SELECT DISTINCT
		OSB_AccountEntry.accountEntryId,
		OSB_AccountEntry.name AS account,
		OSB_AccountEntry.code_,
		OSB_AccountEntry.partnerManagedSupport,
		OSB_AccountEntry.status AS accountStatus,
		OSB_OrderEntry.startDate AS supportStartDate,
		OSB_OrderEntry.renewCount,
		OSB_OfferingEntry.supportEndDate,
		OSB_OfferingEntry.status,
		OSB_ProductEntry.name AS product,
		OSB_SupportResponse.name AS region,
		OSB_SupportResponse.severity1Response,
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
		OSB_SupportTeam.name AS supportTeam,
		CASE
			WHEN
				OSB_AccountWorker_A.role IS NOT NULL
			THEN
				1
			ELSE
				0
		END AS strategicAccount,
		OSB_AccountWorker_B.accountWorkerId,
		OSB_AccountWorker_B.userId AS accountWorkerUserId,
		OSB_AccountWorker_B.role AS accountWorkerRole,
		User_.firstName AS accountWorkerFirstName,
		User_.lastName AS accountWorkerLastName,
		OSB_AccountAuditStatusView.accountStatusModifiedDate
	FROM
		[$LRDCOM_DB$]OSB_AccountEntry
		INNER JOIN [$LRDCOM_DB$]OSB_OrderEntry ON OSB_AccountEntry.accountEntryId = OSB_OrderEntry.accountEntryId
		INNER JOIN [$LRDCOM_DB$]OSB_OfferingEntry ON OSB_OrderEntry.orderEntryId = OSB_OfferingEntry.orderEntryId
		INNER JOIN [$LRDCOM_DB$]OSB_OfferingDefinition ON OSB_OfferingEntry.offeringDefinitionId = OSB_OfferingDefinition.offeringDefinitionId
		INNER JOIN [$LRDCOM_DB$]OSB_ProductEntry ON OSB_OfferingDefinition.productEntryId = OSB_ProductEntry.productEntryId
		INNER JOIN [$LRDCOM_DB$]OSB_SupportResponse ON OSB_OfferingDefinition.supportResponseId = OSB_SupportResponse.supportResponseId
		INNER JOIN [$LRDCOM_DB$]OSB_SupportTeamLocale ON OSB_SupportResponse.localeType = OSB_SupportTeamLocale.localeType
		INNER JOIN [$LRDCOM_DB$]OSB_SupportTeamTimeZone ON OSB_SupportResponse.timeZoneType = OSB_SupportTeamTimeZone.timeZoneType
		INNER JOIN [$LRDCOM_DB$]OSB_SupportTeam ON OSB_SupportTeamLocale.supportTeamId = OSB_SupportTeam.supportTeamId AND OSB_SupportTeamTimeZone.supportTeamId = OSB_SupportTeam.supportTeamId
		LEFT OUTER JOIN [$LRDCOM_DB$]OSB_AccountWorker OSB_AccountWorker_A ON (OSB_AccountEntry.accountEntryId = OSB_AccountWorker_A.accountEntryId) AND ((OSB_AccountWorker_A.role = 1) OR (OSB_AccountWorker_A.role = 4))
		LEFT OUTER JOIN [$LRDCOM_DB$]OSB_AccountWorker OSB_AccountWorker_B ON (OSB_AccountEntry.accountEntryId = OSB_AccountWorker_B.accountEntryId) AND (OSB_AccountWorker_B.role != 0) 
		LEFT OUTER JOIN [$LRDCOM_DB$]User_ ON OSB_AccountWorker_B.userId = User_.userId
		LEFT OUTER JOIN OSB_AccountAuditStatusView ON OSB_AccountEntry.accountEntryId = OSB_AccountAuditStatusView.accountEntryId;