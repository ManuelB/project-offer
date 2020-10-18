sap.ui.define([
	"incentergy/base/view/AbstractAppController"
], function (AbstractAppController) {
	"use strict";

	return AbstractAppController.extend("incentergy.projectoffer.view.App", {
		getEntityName : function() {
			return "projectoffer";
		}
	});
}, true);
