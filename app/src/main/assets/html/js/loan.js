(function () {
	'use strict';

	$.extend(true, $, {
		'app': {
			'loan' : {
				'vm': {
				}
			}
		}
	});

	$(function() {
		var form = $('#cWriteForm');
		$.app.loan.vm.address = ko.observable(form.find('[name=address]').val());
		$.app.loan.vm.lat = ko.observable(form.find('[name=lat]').val());
		$.app.loan.vm.lng = ko.observable(form.find('[name=lng]').val());
		$.app.loan.vm.images = ko.observableArray($.app.util.splitArray(form.find('[name=images]').val()));
		$.app.loan.vm.roomType = ko.observable(form.find('[name=roomType]').val());
		$.app.loan.vm.area = ko.observable(form.find('[name=area]').val());
		$.app.loan.vm.building = ko.observable(form.find('[name=building]').val());
		$.app.loan.vm.description = ko.observable(form.find('[name=description]').val());
		$.app.loan.vm.trans = ko.observable(form.find('[name=trans]').val());
		$.app.loan.vm.heating = ko.observable(form.find('[name=heating]').val());
		$.app.loan.vm.maintenance = ko.observable(form.find('[name=maintenance]').val());
		$.app.loan.vm.maintenanceSet = ko.observableArray($.app.util.splitArray(form.find('[name=maintenanceSet]').val()));
		$.app.loan.vm.floor = ko.observable(form.find('[name=floor]').val());
		$.app.loan.vm.liveinDate = ko.observable(form.find('[name=liveinDate]').val());
		$.app.loan.vm.deposit = ko.observable(form.find('[name=deposit]').val());
		$.app.loan.vm.rent = ko.observable(form.find('[name=rent]').val());
		$.app.loan.vm.options = ko.observableArray($.app.util.splitArray(form.find('[name=options]').val()));
		$.app.loan.vm.parking = ko.observable(form.find('[name=parking]').prop('checked'));
		$.app.loan.vm.pet = ko.observable(form.find('[name=pet]').prop('checked'));

		ko.applyBindings($.app.loan.vm, form.get(0));
		form = null;
	});
})();