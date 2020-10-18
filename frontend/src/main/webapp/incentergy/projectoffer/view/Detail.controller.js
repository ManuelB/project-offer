sap.ui.define([
	"incentergy/base/view/AbstractDetailController",
], function (AbstractDetailController) {
	"use strict";

	return AbstractDetailController.extend("incentergy.projectoffer.view.Detail", {
		getEntityName : function () {
			return "projectoffer";
		}
	});
}, true);
