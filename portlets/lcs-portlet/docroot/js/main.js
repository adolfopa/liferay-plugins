AUI.add(
	'liferay-cloud-services',
	function(A) {
		var Lang = A.Lang;

		var CSS_ICON_CONNECTED = 'lcs-icon-connected';

		var CSS_ICON_DISCONNECTED = 'lcs-icon-disconnected';

		var CSS_ICON_PENDING = 'lcs-icon-pending';

		var CSS_LCS_CLUSTER_ENTRY_DIALOG = 'lcs-cluster-entry-dialog';

		var STR_CLICK = 'click';

		var STR_INPUT = 'input';

		var STR_REPONSE_DATA = 'responseData';

		var STR_SUBMIT = 'submit';

		var STR_SUCCESS = 'success';

		var TPL_ERROR_MESSAGE = '<span class="alert alert-error lcs-alert">{message}</span>';

		var TPL_OPTION = '<option value="{0}">{1}</option>';

		var TPL_SELECT_LCS_CLUSTER_ENTRY = '<div class="control-group">' +
			'<select class="aui-field-select" id="{portletNamespace}lcsClusterEntryId" name="{portletNamespace}lcsClusterEntryId">' +
				'{options}' +
			'</select>' +
		'</div>';

		var TYPE_ADD_LCS_CLUSTER_ENTRY = 0;

		var TYPE_SERVE_CORP_ENTRY = 1;

		var TYPE_SERVE_LCS_CLUSTER_ENTRY = 2;

		var LCS = A.Component.create(
			{
				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'liferay-cloud-services',

				prototype: {
					initializeLCSClusterNodePage: function(config) {
						var instance = this;

						var connectURL = config.connectURL;
						var disconnectURL = config.disconnectURL;
						var pending = config.pending;

						instance._connectionStatusURL = config.connectionStatusURL;
						instance._labelConnected = config.labelConnected;
						instance._labelConnectedHelp = config.labelConnectedHelp;
						instance._labelDisconnected = config.labelDisconnected;
						instance._labelDisconnectedHelp = config.labelDisconnectedHelp;
						instance._labelPending = config.labelPending;
						instance._labelPendingHelp = config.labelPendingHelp;

						var connectButton = instance.byId('connect');
						var disconnectButton = instance.byId('disconnect');
						var resetCredentialsButton = instance.byId('resetCredentials');

						var getConnectionStatus = A.bind('_getConnectionStatus', instance);

						connectButton.on(
							STR_CLICK,
							function(event) {
								A.io.request(
									connectURL,
									{
										dataType: 'json',
										method: 'GET',
										on: {
											success: function(event, id, obj) {
												var response = this.get(STR_REPONSE_DATA);

												if (response.result == STR_SUCCESS) {
													instance._refreshConnectionControls(true, false);

													setTimeout(getConnectionStatus, 1000);
												}
											}
										}
									}
								);
							}
						);

						disconnectButton.on(
							STR_CLICK,
							function(event) {
								A.io.request(
									disconnectURL,
									{
										dataType: 'json',
										method: 'GET',
										on: {
											success: function(event, id, obj) {
												var response = this.get(STR_REPONSE_DATA);

												if (response.result == STR_SUCCESS) {
													instance._refreshConnectionControls(true, true);

													setTimeout(getConnectionStatus, 1000);
												}
											}
										}
									}
								);
							}
						);

						instance._connectButton = connectButton;
						instance._disconnectButton = disconnectButton;
						instance._resetCredentialsButton = resetCredentialsButton;

						new A.TooltipDelegate(
							{
								trigger: '.lcs-portlet button',
								zIndex: 1
							}
						);

						if (pending) {
							instance._getConnectionStatus();
						}
					},

					initializeRegisterPage: function(config) {
						var instance = this;

						var urlMap = {};

						urlMap[TYPE_ADD_LCS_CLUSTER_ENTRY] = config.addLCSClusterEntryURL;
						urlMap[TYPE_SERVE_CORP_ENTRY] = config.serveCorpEntryURL;
						urlMap[TYPE_SERVE_LCS_CLUSTER_ENTRY] = config.serveLCSClusterEntryURL;

						instance._errorDuplicateEnvironment = config.errorDuplicateEnvironment;
						instance._errorGenericEnvironment = config.errorGenericEnvironment;
						instance._errorRequiredEnvironmentName = config.errorRequiredEnvironmentName;
						instance._labelNewEnvironment = config.labelNewEnvironment;
						instance._msgNoEnvironmentsCreated = config.msgNoEnvironmentsCreated;

						instance._urlMap = urlMap;

						instance._lcsClusterEntryIdNode = instance.byId('lcsClusterEntryId');
						instance._lcsClusterEntryInputWrapper = instance.byId('lcsClusterEntryInputWrapper');
						instance._registrationForm = instance.byId('registrationFm');

						instance._portletContentBox = instance._registrationForm.ancestor('.portlet-content');

						instance.byId('addEnvironmentButton').on(STR_CLICK, instance._createLCSClusterEntryPanel, instance);
						instance.byId('corpEntryId').on('change', instance._getLCSClusterEntries, instance);
						instance.byId('name').on(STR_INPUT, A.bind('_refreshSubmitDisabled', instance));

						instance._lcsClusterNewEntry = false;
					},

					_initializeLCSClusterEntryPanel: function() {
						var instance = this;

						var lcsClusterEntryFormAdd = instance.byId('fm', instance._lcsClusterEntryPanel);

						var name = instance.one('#name', lcsClusterEntryFormAdd);

						lcsClusterEntryFormAdd.detach(STR_SUBMIT);

						name.on(STR_INPUT, A.bind('_onLCSClusterEntryNameInput', instance));

						lcsClusterEntryFormAdd.on(STR_SUBMIT, instance._onLCSClusterEntryFormSubmit, instance, lcsClusterEntryFormAdd);

						instance._lcsClusterEntryFormAdd = lcsClusterEntryFormAdd;
					},

					_createSelectLCSClusterEntryHTML: function(optionsArray) {
						var instance = this;

						var options = A.Array.map(
							optionsArray,
							function(item, index, collection) {
								return Lang.sub(TPL_OPTION, [item.lcsClusterEntryId, item.name]);
							}
						);

						options = options.join('');

						return Lang.sub(
							TPL_SELECT_LCS_CLUSTER_ENTRY,
							{
								options: options,
								portletNamespace: instance.NS
							}
						);
					},

					_createLCSClusterEntryPanel: function() {
						var instance = this;

						var lcsClusterEntryPanel = instance._lcsClusterEntryPanel;

						var lcsClusterEntryPanelRenderURL = instance._createURL(TYPE_ADD_LCS_CLUSTER_ENTRY);

						if (!lcsClusterEntryPanel) {
							lcsClusterEntryPanel = Liferay.Util.Window.getWindow(
								{
									dialog: {
										centered: true,
										cssClass: CSS_LCS_CLUSTER_ENTRY_DIALOG,
										height: 560,
										modal: true,
										resizable: false,
										width: 550
									},
									title: instance._labelNewEnvironment
								}
							).render(instance._portletContentBox);

							lcsClusterEntryPanel.plug(
								A.Plugin.IO,
								{
									autoLoad: false,
									uri: lcsClusterEntryPanelRenderURL
								}
							);

							lcsClusterEntryPanel.on(
								'visibleChange',
								function(event) {
									if (!event.newVal && instance._lcsClusterEntryFormAdd) {
										instance._lcsClusterEntryFormAdd.reset();
									}
								}
							);

							instance._lcsClusterEntryPanel = lcsClusterEntryPanel;
						}
						else if (instance._currentLCSClusterEntryPanelAddIOHandle) {
							instance._currentLCSClusterEntryPanelAddIOHandle.detach();

							lcsClusterEntryPanel.io.set('uri', lcsClusterEntryPanelRenderURL);
						}

						lcsClusterEntryPanel.show();

						instance._currentLCSClusterEntryPanelAddIOHandle = lcsClusterEntryPanel.io.after(
							STR_SUCCESS,
							A.bind('_initializeLCSClusterEntryPanel', instance)
						);

						lcsClusterEntryPanel.io.start();
					},

					_createURL: function(type) {
						var instance = this;

						var url = instance._urlMap[type] || '';

						if (url && type != TYPE_SERVE_LCS_CLUSTER_ENTRY) {
							var selectedCorpEntryId = instance.one('#corpEntryId', instance._registrationForm).val();

							url = Liferay.Util.addParams(instance.ns('corpEntryId') + '=' + selectedCorpEntryId, url);
						}

						return url;
					},

					_getConnectionStatus: function() {
						var instance = this;

						A.io.request(
							instance._connectionStatusURL,
							{
								dataType: 'json',
								method: 'GET',
								on: {
									success: function(event, id, obj) {
										var response = this.get(STR_REPONSE_DATA);

										if (response.pending) {
											setTimeout(A.bind('_getConnectionStatus', instance), 1000);
										}
										else {
											instance._refreshConnectionControls(false, response.ready);
										}
									}
								}
							}
						);
					},

					_getLCSClusterEntries: function(event) {
						var instance = this;

						var lcsClusterEntryInputWrapper = instance._lcsClusterEntryInputWrapper;

						lcsClusterEntryInputWrapper.empty();

						lcsClusterEntryInputWrapper.plug(A.LoadingMask);

						lcsClusterEntryInputWrapper.loadingmask.show();

						A.io.request(
							instance._createURL(TYPE_SERVE_CORP_ENTRY),
							{
								dataType: 'json',
								form: {
									id: instance._registrationForm.getDOM()
								},
								on: {
									success: function(event, id, obj) {
										var response = this.get(STR_REPONSE_DATA);

										var corpEntrySelect = instance.byId('corpEntryId', instance._registrationForm);

										var corpEntries = response.corpEntries;

										if (corpEntries && !corpEntrySelect.get('children').size()) {
											var options = A.Array.map(
												corpEntries,
												function(item, index, collection) {
													return Lang.sub(TPL_OPTION, [item.corpEntryId, item.name]);
												}
											);

											corpEntrySelect.append(options.join(''));
										}

										var lcsClusterEntries = response.lcsClusterEntries;

										if (lcsClusterEntries.length > 0) {
											var selectLCSClusterEntryHTML = instance._createSelectLCSClusterEntryHTML(lcsClusterEntries);

											lcsClusterEntryInputWrapper.html(selectLCSClusterEntryHTML);

											var lcsClusterEntryIdNode = lcsClusterEntryInputWrapper.one('#' + instance.NS + 'lcsClusterEntryId');

											if (instance._lcsClusterNewEntry) {
												var lastIndex = lcsClusterEntryIdNode.attr('length') - 1;

												lcsClusterEntryIdNode.set('selectedIndex', lastIndex);

												instance._lcsClusterNewEntry = false;
											}

											instance._lcsClusterEntryIdNode = lcsClusterEntryIdNode;
										}
										else {
											lcsClusterEntryInputWrapper.html(instance._msgNoEnvironmentsCreated);

											instance._lcsClusterEntryIdNode = null;
										}

										lcsClusterEntryInputWrapper.unplug(A.LoadingMask);

										instance._refreshSubmitDisabled();
									}
								}
							}
						);
					},

					_handleLCSClusterEntryError: function(message) {
						var instance = this;

						var errorMessageHTML = Lang.sub(
							TPL_ERROR_MESSAGE,
							{
								message: message
							}
						);

						var dialogContainer = A.one('.' + CSS_LCS_CLUSTER_ENTRY_DIALOG);

						dialogContainer.setStyle('height', 'auto');

						var dialogBody = dialogContainer.one('.modal-body');

						dialogBody.setStyle('height', 'auto');

						var errorMessageNode = dialogBody.one('#' + instance.NS + 'lcsEnvironmentAlertContainer');

						errorMessageNode.html(errorMessageHTML);
					},

					_onLCSClusterEntryFormSubmit: function(event, form) {
						var instance = this;

						event.halt();

						A.io.request(
							instance._createURL(TYPE_SERVE_LCS_CLUSTER_ENTRY),
							{
								dataType: 'json',
								form: {
									id: form.getDOM()
								},
								on: {
									failure: function(event, id, obj) {
										instance._handleLCSClusterEntryError(instance._errorGenericEnvironment);
									},
									success: function(event, id, obj) {
										var response = this.get(STR_REPONSE_DATA);

										if (response.result == 'success') {
											instance._lcsClusterEntryPanel.hide();

											instance._lcsClusterNewEntry = true;

											instance._getLCSClusterEntries();
										}
										else {
											var message = instance._errorGenericEnvironment;

											if (response.message == 'duplicateLCSClusterEntryName') {
												message = instance._errorDuplicateEnvironment;
											}
											else if (response.message == 'requiredLCSClusterEntryName') {
												message = instance._errorRequiredEnvironmentName;
											}

											instance._handleLCSClusterEntryError(message);
										}
									}
								}
							}
						);
					},

					_onLCSClusterEntryNameInput: function(event) {
						var instance = this;

						var disabled = !(Lang.trim(event.currentTarget.val()));

						Liferay.Util.toggleDisabled(instance.byId('create'), disabled);
					},

					_refreshConnectionControls: function(pending, ready) {
						var instance = this;

						var connectButton = instance._connectButton;
						var disconnectButton = instance._disconnectButton;
						var resetCredentialsButton = instance._resetCredentialsButton;

						var connectionStatusNode = instance.one('.lcs-connection-status');

						var connectionStatusIconNode = connectionStatusNode.one('.lcs-connection-icon');
						var connectionStatusLabelNode = connectionStatusNode.one('.lcs-connection-label');
						var connectionStatusTooltipNode = connectionStatusNode.one('.tooltip-text');

						var alertContainerNode = instance.byId('connectionAlertContainer');

						Liferay.Util.toggleDisabled(connectButton, pending);
						Liferay.Util.toggleDisabled(disconnectButton, pending);
						Liferay.Util.toggleDisabled(resetCredentialsButton, pending);

						if (pending) {
							alertContainerNode.show();

							var tooltip = A.one('body > .tooltip');

							if (tooltip) {
								tooltip.addClass('tooltip-hidden');
							}

							connectionStatusIconNode.removeClass(CSS_ICON_CONNECTED);
							connectionStatusIconNode.removeClass(CSS_ICON_DISCONNECTED);
							connectionStatusIconNode.addClass(CSS_ICON_PENDING);

							connectionStatusLabelNode.html(instance._labelPending);
							connectionStatusTooltipNode.html(instance._labelPendingHelp);
						}
						else {
							alertContainerNode.hide();

							connectionStatusIconNode.removeClass(CSS_ICON_PENDING);

							if (ready) {
								connectButton.hide();
								disconnectButton.show();

								connectionStatusIconNode.addClass(CSS_ICON_CONNECTED);

								connectionStatusLabelNode.html(instance._labelConnected);
								connectionStatusTooltipNode.html(instance._labelConnectedHelp);
							}
							else {
								connectButton.show();
								disconnectButton.hide();

								connectionStatusIconNode.addClass(CSS_ICON_DISCONNECTED);

								connectionStatusLabelNode.html(instance._labelDisconnected);
								connectionStatusTooltipNode.html(instance._labelDisconnectedHelp);
							}
						}
					},

					_refreshSubmitDisabled: function(event) {
						var instance = this;

						var registrationForm = instance._registrationForm;

						var disabled = !instance._lcsClusterEntryIdNode || !instance.one('#name', registrationForm).val();

						Liferay.Util.toggleDisabled(instance.one('#register', registrationForm), disabled);
					}
				}
			}
		);

		Liferay.Portlet.LCS = LCS;
	},
	'',
	{
		requires: ['aui-io-request', 'aui-loading-mask-deprecated', 'aui-tooltip-delegate', 'liferay-portlet-base', 'liferay-portlet-url', 'liferay-util-window', 'resize']
	}
);