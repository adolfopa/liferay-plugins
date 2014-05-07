CREATE PROCEDURE
	OSB_TicketsPerLiferayVersionByAccount()
BEGIN
	SELECT DISTINCT
		CASE
			WHEN
				OSB_TicketsVersionDeltaView.versionClassName = 'com.liferay.osb.model.ProductEntry.portalAllVersions' AND
				OSB_TicketsVersionDeltaView.productName = 'Portal Non-Production'
			THEN
				CONCAT("Liferay ", OSB_TicketsVersionDeltaView.versionName, " - Non-Prod")
			WHEN
				OSB_TicketsVersionDeltaView.versionClassName = 'com.liferay.osb.model.ProductEntry.portalAllVersions' AND
				OSB_TicketsVersionDeltaView.productName = 'Portal Production'
			THEN
				CONCAT("Liferay ", OSB_TicketsVersionDeltaView.versionName, " - Prod")
			WHEN
				OSB_TicketsVersionDeltaView.versionClassName = 'com.liferay.osb.model.ProductEntry.portalAllVersions'
			THEN
				CONCAT("Liferay ", OSB_TicketsVersionDeltaView.versionName, " - Other Licenses")
			ELSE
				'Non-Portal'
		END AS version,
		OSB_TicketsVersionDeltaView.code_,
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 1 OR
					OSB_TicketsVersionDeltaView.monthX = 0
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 1',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 2
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 2',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 3
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 3',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 4
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 4',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 5
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 5',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 6
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 6',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 7
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 7',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 8
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 8',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 9
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 9',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 10
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 10',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 11
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 11',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 12
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 12',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 13
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 13',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 14
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 14',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 15
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 15',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 16
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 16',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 17
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 17',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 18
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 18',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 19
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 19',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 20
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 20',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 21
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 21',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 22
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 22',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 23
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 23',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX = 24
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 24',
		COUNT(
			CASE
				WHEN
					OSB_TicketsVersionDeltaView.monthX > 24
				THEN
					1
				ELSE
					NULL
			END
		) AS 'Month 24+',
		COUNT(OSB_TicketsVersionDeltaView.ticketEntryId) AS ticketsCount
	FROM
		OSB_TicketsVersionDeltaView
	GROUP BY
		version,
		OSB_TicketsVersionDeltaView.code_
	ORDER BY
		OSB_TicketsVersionDeltaView.code_,
		version;
END