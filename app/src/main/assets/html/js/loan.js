(function () {
	'use strict';

	// https://spot.wooribank.com/pot/Dream?withyou=CMBBS0086&cc=c006244:c006294
	var LoanCalc = {
		// 이자 계산
		getInterest: function (loanType, i, loan, rate, payBalance) {
			var interest = 0;
			if (loanType == 3) {					//원금만기일시상환
				interest = loan * rate / 12;
			} else if (loanType == 2) {				//원금균등상환
				if (i == 0) {
					interest = loan * rate / 12;
				} else {
					interest = payBalance * rate / 12;
				}
			} else if (loanType == 1) {				//원리금균등상환
				if (i == 0) {
					interest = loan * rate / 12;
				} else {
					interest = payBalance * rate / 12;
				}
			}
			return interest;
		},
		// 납입원금 계산
		getPrincipal: function (loanType, i, period, loan, term, repayment, interest) {
			var principal = 0;
			if (loanType == 3) {	//원금만기일시상환
				if (i == (period - 1)) {	//마지막라인인경우
					principal = loan;
				}
			} else if (loanType == 2) {	//원금균등상환
				principal = loan / (period - term);
			} else if (loanType == 1) {	//원리금균등상환
				principal = repayment - interest;
			}
			return principal;
		},
		// 상환금 계산
		getRepayment: function (loanType, principal, interest, rate, period, term, loan, i) {
			var repayment = 0;
			if (loanType == 3) {	//원금만기일시상환
				repayment = principal + interest;
			} else if (loanType == 2) {	//원금균등상환
				repayment = principal + interest;
			} else if (loanType == 1) {	//원리금균등상환
				if (i >= term) {	//거치기간 후부터 계산
					repayment = (loan * rate / 12) * (Math.pow((1 + rate / 12), (period - term)));
					repayment = repayment / (Math.pow((1 + rate / 12), (period - term)) - 1);
				} else {
					repayment = principal + interest;
				}
			}
			return repayment;
		}
	};

	$.extend(true, $, {
		'app': {
			'loan': {
				'vm': {
					'showResult': ko.observable(false),
					'money': ko.observable(''),
					'rate': ko.observable(''),
					'period': ko.observable(''),
					'term': ko.observable(0),
					'type': ko.observable(1),
					'rows': ko.observableArray([]),
					'loanMonth': ko.observable(0),	// 월평균이자(원)
					'loanRateAmt': ko.observable(0),	// 총 이자액(원)
					'loanTotalAmt': ko.observable(0)	// 원금 및 총이자액 합계(원)
				},
				'addHistory': function () {
					var vm = $.app.loan.vm;
					var key = vm.type() + '-' + vm.money() + '-' + vm.rate() + '-' + vm.period() + '-' + vm.term();
					var history = store.get('loan-history');
					if ($.isArray(history)) {
						for (var i = history.length - 1; i >= 0; i--) {
							if (history[i].key == key) {
								history.splice(i, 1);
								console.log('duplicate remove!');
							}
						}
					} else {
						history = [];
					}
					var row = {
						date: new Date(),
						key: key,
						money: parseFloat(vm.money()),
						type: parseInt(vm.type()),
						rate: parseFloat(vm.rate()),
						period: parseInt(vm.period()),
						loanMonth: parseInt(vm.loanMonth().replace(',', '')),
						loanRateAmt: parseInt(vm.loanRateAmt().replace(',', '')),
						loanTotalAmt: parseInt(vm.loanTotalAmt().replace(',', ''))
					};
					history.unshift(row);
					row = null;
					if (history.length > 100) {
						history = history.slice(0, 100);
					}
					store.set('loan-history', history);
					history = null;
					vm = null;
					key = null;
				},
				'calc': function () {
					var loanType = parseFloat($.app.loan.vm.type()) || 0;
					var loan = parseFloat($.app.loan.vm.money()) || 0;	// 대출원금
					var loanPeriod = parseFloat($.app.loan.vm.period()) || 0; // 대출기간
					var loanTerm = parseFloat($.app.loan.vm.term()) || 0;	// 거치기간
					var loanRate = (parseFloat($.app.loan.vm.rate()) || 0) / 100;	//금리 계산위해 소수점값으로 변경;	// 대출금리

					if (loan <= 0) {
						alertify.error("대출금을 입력해 주세요.");
						$.app.util.focus($('#pMoney'));
						return;
					} else if (loan > 1000000000) {
						alertify.error("대출금을 10억 이하로 입력해 주세요.");
						$.app.util.focus($('#pMoney'));
						return;
					}
					if (loanRate <= 0) {
						alertify.error("대출금리를 입력해 주세요.");
						$.app.util.focus($('#pRate'));
						return;
					} else if (loanRate > 30) {
						alertify.error("대출금리를 30%이하로 입력해 주세요.");
						$.app.util.focus($('#pRate'));
						return;
					}
					if (loanPeriod <= 0) {
						alertify.error("대출기간을 입력해 주세요.");
						$.app.util.focus($('#pPeriod'));
						return;
					} else if (loanPeriod > 420) {
						alertify.error("대출기간을 420개월 이하로 입력해 주세요.");
						$.app.util.focus($('#pPeriod'));
						return;
					}
					if (loanPeriod <= loanTerm) {
						alertify.error("거치기간이 대출기간보다 크거나 같을 수 없습니다.");
						$.app.util.focus($('#pTerm'));
						return;
					}

					//계산결과
					var monthlyLoan = 0;	//월상환금
					var totalInterest = 0;		//총이자
					var principalNinterest;		//원금및이자
					var interest;	//이자
					var principal = 0;	//납입원금
					var repayment = 0;	//상환금
					var principalTotal = 0;	//납입원금 누계
					var payBalance = loan;	//잔금
					var rows = [];
					for (var i = 0; i < loanPeriod; i++) {
						interest = LoanCalc.getInterest(loanType, i, loan, loanRate, payBalance);
						totalInterest = totalInterest + interest;
						if (loanType == 1) {
							repayment = LoanCalc.getRepayment(loanType, principal, interest, loanRate, loanPeriod, loanTerm, loan, i);
							if (i >= loanTerm) {	//거치기간 후부터 계산
								principal = LoanCalc.getPrincipal(loanType, i, loanPeriod, loan, loanTerm, repayment, interest);
							}
						} else {
							if (i >= loanTerm) {	//거치기간 후부터 계산
								principal = LoanCalc.getPrincipal(loanType, i, loanPeriod, loan, loanTerm, repayment, interest);
							}
							repayment = LoanCalc.getRepayment(loanType, principal, interest, loanRate, loanPeriod, loanTerm, loan, i);
						}
						principalTotal = principalTotal + principal;
						payBalance = loan - principalTotal;

						rows.push({
							'num': (i + 1),
							'payments': $.app.util.printNumber(Math.round(repayment)),
							'principal': $.app.util.printNumber(Math.round(principal)),
							'interest': $.app.util.printNumber(Math.round(interest)),
							'total': $.app.util.printNumber(Math.round(principalTotal)),
							'balance': $.app.util.printNumber(Math.round(payBalance))
						});
					}
					$.app.loan.vm.rows(rows);

					if (loanType == 3) {
						monthlyLoan = totalInterest / loanPeriod;
					} else if (loanType == 2) {
						monthlyLoan = loan / (loanPeriod - loanTerm);
					} else if (loanType == 1) {
						monthlyLoan = loan * loanRate / 12;
						monthlyLoan = monthlyLoan * (Math.pow((1 + loanRate / 12), (loanPeriod - loanTerm)));
						monthlyLoan = monthlyLoan / (Math.pow((1 + loanRate / 12), (loanPeriod - loanTerm)) - 1);
					}
					principalNinterest = loan + totalInterest;
					$.app.loan.vm.loanMonth($.app.util.printNumber(Math.round(monthlyLoan)));
					$.app.loan.vm.loanRateAmt($.app.util.printNumber(Math.round(totalInterest)));
					$.app.loan.vm.loanTotalAmt($.app.util.printNumber(Math.round(principalNinterest)));

					$.app.loan.vm.showResult(true);
					$('body').scrollTo('.dResult');

					$.app.loan.addHistory();
				},
				'reset': function () {
					$.app.loan.vm.showResult(false);
					$.app.loan.vm.money(0);
					$.app.loan.vm.rate(0);
					$.app.loan.vm.period(0);
					$.app.loan.vm.term(0);
				}
			}
		}
	});
	$.app.loan.vm.type.subscribe(function () {
		$.app.loan.calc();
	});
	$.app.loan.vm.typeText = ko.computed(function () {
		switch ($.app.loan.vm.type()) {
			case 1 :
				return '원리금균등상환';
			case 2 :
				return '원금균등상환';
			case 3 :
				return '원금만기일시상환';
		}
	});
	$.app.loan.vm.summaryText = ko.computed(function () {
		switch ($.app.loan.vm.type()) {
			case 1 :
				return '월상환금';
			case 2 :
				return '월납입원금';
			case 3 :
				return '월평균이자';
		}
	});

	$.app.loan.vm.moneyText = ko.computed(function () {
		return $.app.util.moneyKorean($.app.loan.vm.money());
	});

	$(function () {
		var form = $('#cWriteForm');
		ko.applyBindings($.app.loan.vm, form.get(0));
		form = null;

		$.ripple(".btn, .nav-tabs > li", {});

		$(".cCheckboxLabel").labelauty();
	});
})();