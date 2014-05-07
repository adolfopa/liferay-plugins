CREATE VIEW
	OSB_AccountAuditStatusView
AS
	SELECT
		OSB_AuditEntry.classPK AS accountEntryID,
		MAX(OSB_AuditEntry.createDate) AS accountStatusModifiedDate
	FROM
		[$LRDCOM_DB$]OSB_AuditEntry
	WHERE
		OSB_AuditEntry.classNameId = 1400963 AND
		OSB_AuditEntry.field = 34017
	GROUP BY
		OSB_AuditEntry.classPK;