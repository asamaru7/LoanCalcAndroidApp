(function () {
	'use strict';

	$.extend(true, $, {
		'app': {
			'util': {
				'printNumber': function (val) {
					if (val == 0) {
						return "-";
					}
					return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
				},
				'moneyKorean': function (num) {
					//var arrayNum = ['', '일', '이', '삼', '사', '오', '육', '칠', '팔', '구'];
					var arrayNum = ['', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
					var arrayUnit = ['', '십', '백', '천', '만 ', '십만 ', '백만 ', '천만 ', '억 ', '십억 ', '백억 ', '천억 ', '조 ', '십조 ', '백조'];
					var arrayStr = [];
					var str = String(num);
					var len = str.length;
					var hanStr = '';
					for (var i = 0; i < len; i++) {
						arrayStr[i] = str.substr(i, 1)
					}
					var code = len;
					for (i = 0; i < len; i++) {
						code--;
						var tmpUnit = '';
						if (arrayNum[arrayStr[i]] != '') {
							tmpUnit = arrayUnit[code];
							if (code > 4) {
								if (( Math.floor(code / 4) == Math.floor((code - 1) / 4) && arrayNum[arrayStr[i + 1]] != '') ||
									( Math.floor(code / 4) == Math.floor((code - 2) / 4) && arrayNum[arrayStr[i + 2]] != '')) {
									tmpUnit = arrayUnit[code].substr(0, 1);
								}
							}
						}
						hanStr += arrayNum[arrayStr[i]] + tmpUnit;
					}
					return $.trim(hanStr);
				}
			}
		}
	});

	$.fn.scrollTo = function (target, options, callback) {
		if (typeof options == 'function' && arguments.length == 2) {
			callback = options;
			options = target;
		}
		var settings = $.extend({
			scrollTarget: target,
			offsetTop: 50,
			duration: 500,
			easing: 'swing'
		}, options);
		return this.each(function () {
			var scrollPane = $(this);
			var scrollTarget = (typeof settings.scrollTarget == "number") ? settings.scrollTarget : $(settings.scrollTarget);
			var scrollY = (typeof scrollTarget == "number") ? scrollTarget : scrollTarget.offset().top + scrollPane.scrollTop() - parseInt(settings.offsetTop);
			scrollPane.animate({scrollTop: scrollY}, parseInt(settings.duration), settings.easing, function () {
				if (typeof callback == 'function') {
					callback.call(this);
				}
			});
		});
	}
})();