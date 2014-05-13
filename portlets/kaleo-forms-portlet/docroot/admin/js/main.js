AUI.add(
	'liferay-kaleo-forms-admin',
	function(A) {
		var AArray = A.Array;

		var Lang = A.Lang;

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

					form: {
						value: null
					},

					portletId: {
						value: null
					},

					saveInPortletSessionURL: {
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

						instance.nextBtn = instance.one('.kaleo-process-next');
						instance.prevBtn = instance.one('.kaleo-process-previous');
						instance.submitBtn = instance.one('.kaleo-process-submit');

						var formWizard = instance.formWizard = new Liferay.KaleoFormWizard(
							{
								form: instance.get('form'),
								tabView: instance.get('tabView')
							}
						);

						var currentStep = formWizard.get('currentStep');

						if (currentStep > 1) {
							formWizard.validateStep(currentStep);

							instance.updateNavigationControls(currentStep);
						}

						instance.bindUI();
					},

					bindUI: function() {
						var instance = this;

						var form = instance.get('form');

						form.formNode.on('submit', instance._onSubmitForm, instance);

						instance.formWizard.after('currentStepChange', instance._afterCurrentStepChange, instance);

						instance.nextBtn.on('click', instance._onClickNext, instance);
						instance.prevBtn.on('click', instance._onClickPrev, instance);
					},

					_afterCurrentStepChange: function(event) {
						var instance = this;

						var descriptionLocalized = Liferay.component(instance.ns('description'));
						var nameLocalized = Liferay.component(instance.ns('name'));

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
						var ddmTemplateId = instance.one('#ddmTemplateId').val();
						var taskFormPairsData = instance.one('#taskFormPairsData').val();
						var workflowDefinition = instance.one('#workflowDefinition').val();

						instance._saveInPortletSession(
							A.merge(
								sessionMap,
								{
									ddmStructureId: ddmStructureId,
									ddmStructureName: ddmStructureName,
									ddmTemplateId: ddmTemplateId,
									taskFormPairsData: taskFormPairsData,
									workflowDefinition: workflowDefinition
								}
							)
						);

						var currentStep = event.newVal;

						if (currentStep === STEPS_MAP.FORMS) {
							instance._showForms();
						}

						instance.updateNavigationControls(currentStep);
					},

					_onClickNext: function(event) {
						var instance = this;

						instance.formWizard.navigate(1);
					},

					_onClickPrev: function(event) {
						var instance = this;

						instance.formWizard.navigate(-1);
					},

					_onSubmitForm: function(event) {
						var instance = this;

						event.preventDefault();

						if (instance.formWizard.validateStep(STEPS_MAP.FORMS)) {
							submitForm(event.target);
						}
					},

					_saveInPortletSession: function(data) {
						var instance = this;

						A.io.request(
							instance.get('saveInPortletSessionURL'),
							{
								data: instance.ns(data),
							}
						);
					},

					_showForms: function() {
						var instance = this;

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
							'#' + instance.NS + 'formsSearchContainer',
							function() {
								resultsContainer.unplug(A.LoadingMask);
							}
						);
					},

					updateNavigationControls: function(currentStep) {
						var instance = this;

						if (currentStep === STEPS_MAP.DETAILS) {
							instance.nextBtn.show();
							instance.prevBtn.hide();
							instance.submitBtn.hide();
						}
						else if (currentStep === STEPS_MAP.FORMS) {
							instance.nextBtn.hide();
							instance.prevBtn.show();
							instance.submitBtn.show();
						}
						else {
							instance.nextBtn.show();
							instance.prevBtn.show();
							instance.submitBtn.hide();
						}
					}
				}
			}
		);

		Liferay.KaleoFormsAdmin = KaleoFormsAdmin;
	},
	'',
	{
		requires: ['aui-base', 'aui-loading-mask-deprecated', 'aui-parse-content', 'aui-url', 'liferay-kaleo-forms-components', 'liferay-portlet-base', 'liferay-portlet-url', 'liferay-store', 'node-load']
	}
);