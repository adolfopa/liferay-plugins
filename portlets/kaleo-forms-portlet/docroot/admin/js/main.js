AUI.add(
	'liferay-kaleo-forms-admin',
	function(A) {
		var AArray = A.Array;

		var STEPS_MAP = {
			DEFINITION: 2,
			DETAILS: 1,
			FORMS: 4,
			WORKFLOW: 3
		};

		var KaleoFormsAdmin = A.Component.create(
			{
				ATTRS: {
					currentURL: {
						value: null
					},

					saveInSessionURL: {
						value: null
					},

					tabView: {
						value: null
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'liferay-kaleo-forms-admin',

				prototype: {
					initializer: function(config) {
						var instance = this;

						instance.tabView = instance.get('tabView');

						instance.tabView.addTarget(instance);

						instance.bindUI();
					},

					bindUI: function() {
						var instance = this;

						instance.after('tab:selectedChange', A.bind(instance._afterTabSelectedChange, instance));
					},

					_afterTabSelectedChange: function(event) {
						var instance = this;

						var namespace = instance.get('namespace');

						var tabView = instance.tabView;

						if (event.newVal === 1) {
							var descriptionLocalized = Liferay.component(namespace + 'description');
							var nameLocalized = Liferay.component(namespace + 'name');

							var translatedLanguagesDescription = descriptionLocalized.get('translatedLanguages').values();
							var translatedLanguagesName = nameLocalized.get('translatedLanguages').values();

							var sessionMap = {
								'translatedLanguagesDescription': translatedLanguagesDescription.join(),
								'translatedLanguagesName': translatedLanguagesName.join()
							};

							AArray.each(
								translatedLanguagesDescription,
								function(item, index, collection) {
									var val = instance.one('#description_' + item).val();

									sessionMap['description' + item] = val;
								}
							);

							AArray.each(
								translatedLanguagesName,
								function(item, index, collection) {
									var val = instance.one('#name_' + item).val();

									sessionMap['name' + item] = val;
								}
							);

							var ddmStructureId = instance.one('#ddmStructureId').val();

							var ddmStructureName = instance.one('#ddmStructureName').val();

							var workflowDefinition = instance.one('#workflowDefinition').val();

							instance._saveInSession(
								A.merge(
									sessionMap,
									{
										ddmStructureId: ddmStructureId,
										ddmStructureName: ddmStructureName,
										workflowDefinition: workflowDefinition
									}
								)
							);

							var activeTabIndex = tabView.indexOf(event.target);

							var currentStep = activeTabIndex + 1;

							if (currentStep === STEPS_MAP.FORMS) {
								instance._showForms();
							}
						}
					},

					_saveInSession: function(data) {
						var instance = this;

						var namespace = instance.get('namespace');

						A.each(
							data,
							function(item, index, collection) {
								collection[namespace + index] = item;

								delete collection[index];
							}
						);

						A.io.request(
							instance.get('saveInSessionURL'),
							{
								data: data,
							}
						);
					},

					_showForms: function() {
						var instance = this;

						var namespace = instance.get('namespace');

						var currentURL = instance.get('currentURL');

						var resultsContainer = instance.one('#resultsContainer');

						var workflowDefinition = instance.one('#workflowDefinition').val();

						var backURL = new Liferay.PortletURL(Liferay.PortletURL.RENDER_PHASE, null, currentURL);

						backURL.setParameter('historyKey', 'forms');

						var formsURL = new Liferay.PortletURL(Liferay.PortletURL.RENDER_PHASE, null, currentURL);

						formsURL.setParameter('mvcPath', '/admin/process/task_template_search_container.jsp');
						formsURL.setParameter('backURL', backURL.toString());
						formsURL.setParameter('workflowDefinition', workflowDefinition);

						resultsContainer.plug(A.LoadingMask).loadingmask.show();

						resultsContainer.load(
							formsURL.toString(),
							'#' + namespace + 'formsSearchContainer',
							function() {
								resultsContainer.unplug(A.LoadingMask);
							}
						);
					}
				}
			}
		);

		Liferay.KaleoFormsAdmin = KaleoFormsAdmin;
	},
	'',
	{
		requires: ['aui-base', 'aui-io-request', 'aui-loading-mask-deprecated', 'aui-parse-content', 'liferay-portlet-base', 'liferay-portlet-url', 'node-load']
	}
);