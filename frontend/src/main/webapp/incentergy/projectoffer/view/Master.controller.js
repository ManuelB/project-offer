sap.ui.define([
	"incentergy/base/view/AbstractMasterController",
	'sap/ui/model/Filter',
	'sap/ui/model/FilterOperator',
	'sap/ui/model/FilterType'
], function (AbstractMasterController, Filter, FilterOperator, FilterType) {
	"use strict";

	return AbstractMasterController.extend("incentergy.projectoffer.view.Master", {
		
		onInit: function () {
			AbstractMasterController.prototype.onInit.apply(this, arguments);
			this.oRouter = this.getOwnerComponent().getRouter();
			
			this.oRouter.attachRouteMatched(function(oEvent) {
				if(oEvent.getParameter("name") == "search") {
					this.onRouteSearchMatched(oEvent);
				}
			}.bind(this));
			
		},
		onRouteSearchMatched: function(oEvent) {
			let sQuery = oEvent.getParameter("arguments").query;
			
			let aQueryParts = sQuery.split(/&/).map(s => s.split(/=/)).map(a => [a[0].replace("_", "/"), a[1]]);
			
			let aFilters = [];
			for(let aPart of aQueryParts) {
				let sField = aPart[0];
				let sValue = aPart[1];
				aFilters.push(new Filter({path: sField, operator: FilterOperator.EQ, value1: sValue}));
			}
			this.byId("projectofferTable").getBinding("items").filter(aFilters, FilterType.Application);
		},
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
