AUI().use('escape', function(A) {
Liferay.Report = {

	deleteParameter: function(parameterKey, parameterValue, parameterType) {
		var instance = this;

		instance._portletMessageContainer.setStyle('display', 'none');

		if (confirm(Liferay.Language.get('are-you-sure-you-want-to-delete-this-entry'))) {
			var parametersInput = A.one('.report-parameters');

			var reportParameters = JSON.parse(parametersInput.get('value'));

			for (var i in reportParameters) {
				var reportParameter = reportParameters[i];

				if (reportParameter.key == parameterKey) {
					reportParameters.splice(i, 1);

					break;
				}
			}

			parametersInput.set('value', JSON.stringify(reportParameters));

			var key = ('.report-tag-' + parameterKey).replace(/ /g,"BLANK");

			A.one(key).remove(true);
		}
	},

	initialize: function(param) {
		var instance = this;

		var namespace = param.namespace;

		instance._portletMessageContainer = A.one('.report-message');

		instance._displayParameters(param.parameters);

		A.one('.add-parameter').on(
			'click',
			function() {
				instance._addParameter(namespace);
			}
		);

		A.one('.remove-existing-report').on(
			'click',
			function() {
				A.one('.existing-report').setStyle('display', 'none');
				A.one('.template-report').setStyle('display', 'block');
				A.one('.cancel-update-template-report').setStyle('display', 'block');
			}
		);

		A.one('.cancel-update-template-report').on(
			'click',
			function() {
				A.one('.existing-report').setStyle('display', 'block');
				A.one('.template-report').setStyle('display', 'none');
				A.one('.cancel-update-template-report').setStyle('display', 'none');
			}
		);

		A.one('.parameters-input-type').on(
			'change',
			function() {
				var parametersValueFieldSet = A.one('.parameters-value-field-set');
				var parametersInputDate = A.one('.parameters-input-date');
				var parametersValue = A.one('.parameters-value');

				if (this.get('value') == 'text') {
					parametersValue.set('value', '');
					parametersValue.attr('disabled', '');
					parametersInputDate.setStyle('display', 'none');
					parametersValueFieldSet.setStyle('display', 'block');
				}

				if (this.get('value') == 'date') {
					parametersValueFieldSet.setStyle('display', 'none');
					parametersInputDate.setStyle('display', 'block');
				}

				if (this.get('value') == 'startDateDay') {
					parametersInputDate.setStyle('display', 'none');
					parametersValueFieldSet.setStyle('display', 'block');
					parametersValue.attr('disabled','disabled');
					parametersValue.set('value', '${startDateDay}');
				}

				if (this.get('value') == 'endDateDay') {
					parametersInputDate.setStyle('display', 'none');
					parametersValueFieldSet.setStyle('display', 'block');
					parametersValue.attr('disabled','disabled');
					parametersValue.set('value', '${endDateDay}');
				}
			}
		);
	},

	_addParameter: function(namespace) {
		var instance = this;

		instance._portletMessageContainer.setStyle('display', 'none');

		var parameterKey = A.one('.parameters-key').get('value');
		var parameterType = A.one('.parameters-input-type').get('value');
		var parameterValue = A.one('.parameters-value').get('value');

		// Validate

		if (parameterKey.length == 0) {
			A.all('.portlet-msg-error').setStyle('display', 'none');

			instance._sendMessage('please-enter-a-valid-report-parameter-key');

			return;
		}

		if (parameterType != 'date' && parameterValue.length == 0) {
			A.all('.portlet-msg-error').setStyle('display', 'none');

			instance._sendMessage('please-enter-a-valid-report-parameter-value');

			return;
		}

		if ((parameterKey.indexOf(',') > 0) || (parameterKey.indexOf('=') > 0) || (parameterValue.indexOf('=') > 0)) {
			instance._sendMessage('one-of-your-fields-contains-invalid-characters');

			return;
		}

		var reportParameters = A.one('.report-parameters').get('value');

		if (reportParameters) {
			var reportParametersJSON = JSON.parse(reportParameters);

			for (var i in reportParametersJSON) {
				var reportParameter = reportParametersJSON[i];

				if (reportParameter.key == parameterKey) {
					instance._sendMessage('that-vocabulary-already-exists');

					return;
				}
			}
		}

		if (parameterType == 'date') {
			var parameterDateDay = A.one('#' + namespace + 'parameterdateday');
			var parameterDateMonth = A.one('#' + namespace + 'parameterdatemonth');
			var parameterDateYear = A.one('#' + namespace + 'parameterdateyear');

			var parameterDate = new Date();

			parameterDate.setDate(parameterDateDay.get('value'));
			parameterDate.setMonth(parameterDateMonth.get('value'));
			parameterDate.setYear(parameterDateYear.get('value'));

			parameterValue = A.DataType.Date.format(parameterDate);
		}

		instance._addTag(parameterKey, parameterValue, parameterType);

		instance._addReportParameter(parameterKey, parameterValue, parameterType);

		A.one('.parameters-key').set('value', '');
		A.one('.parameters-value').set('value', '');
	},

	_addReportParameter: function(parameterKey, parameterValue, parameterType) {
		var reportParameters = [];

		var parametersInput = A.one('.report-parameters');

		if (parametersInput.get('value')) {
			reportParameters = JSON.parse(parametersInput.get('value'));
		}

		var reportParameter = {
			key: parameterKey,
			value: parameterValue,
			type: parameterType
		};

		reportParameters.push(reportParameter);

		parametersInput.set('value', JSON.stringify(reportParameters));
	},

	_addTag: function(parameterKey, parameterValue, parameterType) {
		var tagsContainer = A.one(".report-tags");

		var oldTags = tagsContainer.get('innerHTML');

		var key = ('report-tag-' + A.Escape.html(parameterKey)).replace(/ /g,"BLANK");

		var innerHTML = '<div class="form-inline ' + A.Escape.html(key) + '" >';

		innerHTML += '<input class="form-control" type="text" disabled="disabled" value="' + A.Escape.html(parameterKey) + '" > ';
		innerHTML += '<input class="form-control" type="text" disabled="disabled" value="' + A.Escape.html(parameterValue) + '" >';
		innerHTML += ' <button class="btn btn-default"';
		innerHTML += " onClick=\"Liferay.Report.deleteParameter('" + A.Escape.html(parameterKey) + "','" + A.Escape.html(parameterValue) + "','" + A.Escape.html(parameterType) + "'); \">";
		innerHTML += '<i class="icon-remove" aria-label="Remove"></i></button>';
		innerHTML += '</div>';

		tagsContainer.set('innerHTML', oldTags + innerHTML);
	},

	_displayParameters: function(parameters) {
		var instance = this;

		instance._portletMessageContainer.setStyle('display', 'none');

		A.one('.report-parameters').set('value', parameters);

		if (!parameters) {
			return;
		}

		var reportParameters = JSON.parse(parameters);

		for (var i in reportParameters) {
			var reportParameter = reportParameters[i];

			if (reportParameter.key && reportParameter.value) {
				instance._addTag(reportParameter.key, reportParameter.value, reportParameter.type);
			}
		}
	},

	_sendMessage: function(messageKey) {
		var instance = this;

		instance._portletMessageContainer.addClass('portlet-msg-error');
		instance._portletMessageContainer.set('innerHTML', Liferay.Language.get(messageKey));
		instance._portletMessageContainer.setStyle('display', 'block');
	}
}
});
