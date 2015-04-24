(function () {
	'use strict';
	$.extend(true, $, {
		'app': {
			'history': {
//				'getDate': function (dateText) {
//					var date = new Date(dateText);
//					var str = date.getFullYear() + '-';
//					str += ((String(date.getMonth() + 1).length <= 1) ? '0' : '') + (date.getMonth() + 1) + '-';
//					str += ((String(date.getDate()).length <= 1) ? '0' : '') + date.getDate() + ' ';
//					str += ((String(date.getHours()).length <= 1) ? '0' : '') + date.getHours() + ':';
//					str += ((String(date.getMinutes()).length <= 1) ? '0' : '') + date.getMinutes() + ':';
//					str += ((String(date.getSeconds()).length <= 1) ? '0' : '') + date.getSeconds();
//					return str;
//				}
			}
		}
	});

	$(function () {
		$.ripple(".btn, .nav-tabs > li", {});

//		var compiled = _.template($('#pRows').text());
//		var rows = store.get('loan-history');
//		if (!$.isArray(rows)) {
//			rows = [];
//		}
//		$('#cHistory').html(compiled({'rows': rows}));
//		rows = null;
	});
})();