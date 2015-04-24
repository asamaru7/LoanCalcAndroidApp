(function () {
	'use strict';
	$.extend(true, $, {
		'app': {
			'history': {
				'typeText': function (type) {
					switch (parseInt(type)) {
						case 1 :
							return '원리금균등상환';
						case 2 :
							return '원금균등상환';
						case 3 :
							return '원금만기일시상환';
					}
				},
				'summaryText': function (type) {
					switch (parseInt(type)) {
						case 1 :
							return '월상환금';
						case 2 :
							return '월납입원금';
						case 3 :
							return '월평균이자';
					}
				},
				'getDate': function (dateText) {
					var date = new Date(dateText);
					var str = date.getFullYear() + '-';
					str += ((String(date.getMonth() + 1).length <= 1) ? '0' : '') + (date.getMonth() + 1) + '-';
					str += ((String(date.getDate()).length <= 1) ? '0' : '') + date.getDate() + ' ';
					str += ((String(date.getHours()).length <= 1) ? '0' : '') + date.getHours() + ':';
					str += ((String(date.getMinutes()).length <= 1) ? '0' : '') + date.getMinutes() + ':';
					str += ((String(date.getSeconds()).length <= 1) ? '0' : '') + date.getSeconds();
					return str;
				}
			}
		}
	});

	$(function () {
		$.ripple(".btn, .nav-tabs > li", {});

		var compiled = _.template($('#pRows').text());
		var rows = store.get('loan-history');
		if (!$.isArray(rows)) {
			rows = [];
		}
		$('#cHistory').html(compiled({'rows': rows}));
		rows = null;
	});
})();