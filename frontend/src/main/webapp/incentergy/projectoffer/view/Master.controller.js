sap.ui.define([
	"incentergy/base/view/AbstractMasterController",
	'sap/ui/model/Filter',
	'sap/ui/model/FilterOperator'
], function (AbstractMasterController, Filter, FilterOperator) {
	"use strict";

	return AbstractMasterController.extend("incentergy.projectoffer.view.Master", {
		getEntityName: function() {
			return "projectoffer";
		},
		getFilter: function(sQuery) {
			return [new Filter({
				filters: [
					new Filter("Title", FilterOperator.Contains, sQuery),
					new Filter("Location", FilterOperator.Contains, sQuery)
				],
				and: false
				}
			)];
		},
		getSortField: function() {
			return "OriginalPublicationDate";
		}
	});
}, true);
