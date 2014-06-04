AUI.add(
	'liferay-kaleo-forms-components',
	function(A) {
		var KaleoFormWizard = A.Component.create(
			{
				ATTRS: {
					currentStep: {
						valueFn: '_valueCurrentStep'
					},

					form: {
						value: null
					},

					tabView: {
						value: null
					}
				},

				EXTENDS: A.Base,

				NAME: 'liferay-kaleo-form-wizard',

				prototype: {
					initializer: function(config) {
						var instance = this;

						instance.form = instance.get('form');

						instance.form.addTarget(instance);

						instance.tabView = instance.get('tabView');

						instance.tabView.addTarget(instance);

						instance.validator = instance.form.formValidator;

						instance.validator.set('validateOnInput', true);

						instance.validator.addTarget(instance);

						instance.bindUI();
					},

					bindUI: function() {
						var instance = this;

						instance.after('tab:selectedChange', instance._afterTabSelectedChange, instance);
						instance.on('tab:selectedChange', instance._onTabSelectedChange, instance);
					},

					_afterTabSelectedChange: function(event) {
						var instance = this;

						var tabView = instance.tabView;

						if (event.newVal === 1) {
							var activeTabIndex = tabView.indexOf(event.target);

							var currentStep = activeTabIndex + 1;

							instance.set('currentStep', currentStep);
						}
					},

					_onTabSelectedChange: function(event) {
						var instance = this;

						var tabView = instance.tabView;

						if (event.newVal === 1) {
							var activeTabIndex = tabView.indexOf(event.target);

							if (!instance.validateStep(activeTabIndex)) {
								event.preventDefault();
							}
						}
					},

					_valueCurrentStep: function() {
						var instance = this;

						var tabView = instance.get('tabView');

						var activeTabIndex = tabView.indexOf(tabView.get('selection'));

						return activeTabIndex + 1;
					},

					getTabViewPanels: function() {
						var instance = this;

						var queries = A.TabviewBase._queries;

						return instance.tabView.get('contentBox').all(queries.tabPanel);
					},

					navigate: function(offset) {
						var instance = this;

						var tabView = instance.tabView;

						var tabViewTabs = tabView.getTabs();
						var activeTab = tabView.getActiveTab();

						var newActiveTabIndex = tabViewTabs.indexOf(activeTab) + offset;

						var newActiveTab = tabView.item(newActiveTabIndex);

						if (newActiveTab) {
							tabView.selectChild(newActiveTabIndex);
						}
					},

					validatePanel: function(panel) {
						var instance = this;

						var validator = instance.validator;

						validator.eachRule(
							function(rule, fieldName) {
								var field = validator.getField(fieldName);

								if (panel.contains(field)) {
									validator.validateField(field);
								}
							}
						);
					},

					validateStep: function(step) {
						var instance = this;

						var tabViewPanels = instance.getTabViewPanels();

						var tabViewTabs = instance.tabView.getTabs();

						var valid = true;

						instance.validator.resetAllFields();

						tabViewPanels.each(
							function(item, index, collection) {
								if (index <= (step - 1)) {
									instance.validatePanel(item);

									var tabNode = tabViewTabs.item(index);

									var tabHasError = item.one('.error-field');

									tabNode.toggleClass('section-error', tabHasError);
									tabNode.toggleClass('section-success', !tabHasError);

									if (tabHasError) {
										valid = false;
									}
								}
							}
						);

						return valid;
					}
				}
			}
		);

		Liferay.KaleoFormWizard = KaleoFormWizard;
	},
	'',
	{
		requires: ['aui-base', 'aui-tabview']
	}
);