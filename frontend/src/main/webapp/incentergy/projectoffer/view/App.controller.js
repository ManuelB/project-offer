sap.ui.define([
	"incentergy/base/view/AbstractAppController"
], function (AbstractAppController) {
	"use strict";

/**
 * Vorqualifizierung -> Default
 * Grundbetreuung -> The recruited responded
 * Intensivbetreuung -> The recruiter forwareded the CV to the customer
 * Angebotsbetreuung -> I had an interview
 * Unverbindliche zusage -> The interview was successful and the customer selected me
 */
	return AbstractAppController.extend("incentergy.projectoffer.view.App", {
		getEntityName : function() {
			return "projectoffer";
		}
	});
}, true);
