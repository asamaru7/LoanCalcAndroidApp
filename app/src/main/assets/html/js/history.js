(function () {
	'use strict';
	$.extend(true, $, {
		'app': {
			'history': {
				'reCalcByKey': function (key) {
					$.app.interface.reCalc(key);
				},
				'removeByKey': function (key, id) {
					alertify.confirm('삭제하시겠습니까?', function (e) {
						if (e) {
							$.app.interface.removeHistory(key);
							console.log('#pHis_' + id);
							$('#pHis_' + id).remove();
						}
					});
				}
			}
		}
	});

	$(function () {
		$.ripple(".btn", {});
	});
})();