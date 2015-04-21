(function () {
	'use strict';

	var CalculateUtil = {
		/*
		 *	이자 계산
		 */
		getIza: function (loan_type, i, loan, loan_gumri, payBalance) {	//이자
			var iza_amt = 0;
			if (loan_type == 3) {					//원금만기일시상환
				iza_amt = loan * loan_gumri / 12;
			} else if (loan_type == 2) {				//원금균등상환
				if (i == 0) {
					iza_amt = loan * loan_gumri / 12;
				} else {
					iza_amt = payBalance * loan_gumri / 12;
				}
			} else if (loan_type == 1) {				//원리금균등상환
				if (i == 0) {
					iza_amt = loan * loan_gumri / 12;
				} else {
					iza_amt = payBalance * loan_gumri / 12;
				}
			}
			return iza_amt;
		},
		/*
		 *	납입원금 계산
		 */
		getOrgLoan: function (loan_type, i, loan_period, loan, loan_term, repayment, iza) {
			var org_loan = 0;
			if (loan_type == 3) {					//원금만기일시상환
				if (i == (loan_period - 1)) {	//마지막라인인경우
					org_loan = loan;
				}
			} else if (loan_type == 2) {				//원금균등상환
				//$I$8/($I$11-$I$14)
				org_loan = loan / (loan_period - loan_term);
			} else if (loan_type == 1) {				//원리금균등상환
				org_loan = repayment - iza;
			}
			return org_loan;
		},
		/*
		 *	상환금 계산
		 */
		getRepayment: function (loan_type, org_loan, iza, loan_gumri, loan_period, loan_term, loan, i) {
			var repayment = 0;
			if (loan_type == 3) {					//원금만기일시상환
				repayment = org_loan + iza;
			} else if (loan_type == 2) {				//원금균등상환
				repayment = org_loan + iza;
			} else if (loan_type == 1) {				//원리금균등상환
				if (i >= loan_term) {	//거치기간 후부터 계산
					//($I$8*$J$26/12*(1+$J$26/12)^($I$11-$I$14))/((1+$J$26/12)^($I$11-$I$14)-1)
					repayment = (loan * loan_gumri / 12) * (Math.pow((1 + loan_gumri / 12), (loan_period - loan_term)));
					repayment = repayment / (Math.pow((1 + loan_gumri / 12), (loan_period - loan_term)) - 1);
				} else {
					repayment = org_loan + iza;
				}
			}
			return repayment;
		}
	};

	$.extend(true, $, {
		'app': {
			'loan': {
				'vm': {
					'money': ko.observable(100000000),
					'rate': ko.observable(2.89),
					'month': ko.observable(36),
					'term': ko.observable(0),
					'type': ko.observable(1),
					'rows': ko.observableArray([]),
					'loanMonth': ko.observable(0),	// 월평균이자(원)
					'loanRateAmt': ko.observable(0),	// 총 이자액(원)
					'loanTotalAmt': ko.observable(0)	// 원금 및 총이자액 합계(원)
				},
				'calc': function () {
					var maxCost1 = 420; // 30년에서 35년으로 연장됨 2011.04.08

					// 원금만기일시상환 0 -> 3   원금균등상환 1 -> 2 원리금균등상환 2 -> 1
					var loan_type = parseFloat($.app.loan.vm.type()) || 0;
					var loan = parseFloat($.app.loan.vm.money()) || 0;	// 대출원금
					var loan_period = parseFloat($.app.loan.vm.month()) || 0; // 대출기간
					var loan_term = parseFloat($.app.loan.vm.term()) || 0;	// 거치기간
					var loan_gumri = (parseFloat($.app.loan.vm.rate()) || 0) / 100;	//금리 계산위해 소수점값으로 변경;	// 대출금리

					if (loan <= 0) {
						alert("대출원금을 입력해 주세요.");
						return;
					} else if (loan > 1000000000) {
						alert("대출원금을 10억 이하로 입력해 주세요.");
						return;
					}
					if (loan_period <= 0) {
						alert("대출기간을 입력해 주세요.");
						return;
					} else if (loan_period > maxCost1) {
						alert("대출기간을 " + maxCost1 + "개월 이하로 입력해 주세요.");
						return;
					}
					if (loan_period <= loan_term) {
						alert("거치기간이 대출기간보다 크거나 같을 수 없습니다.");
						return;
					}
					if (loan_gumri <= 0) {
						alert("대출금리를 입력해 주세요.");
						return;
					} else if (loan_gumri > 30) {
						alert("대출금리를 30%이하로 입력해 주세요.");
						return;
					}

					//계산결과
					var monthly_loan = 0;	//월상환금
					var total_iza = 0;		//총이자
					var loan_n_iza;		//원금및이자
					var iza;	//이자
					var org_loan = 0;	//납입원금
					var repayment = 0;	//상환금
					var org_loan_tot = 0;	//납입원금 누계
					var payBalance = loan;	//잔금
					var rows = [];
					for (var i = 0; i < loan_period; i++) {
						iza = CalculateUtil.getIza(loan_type, i, loan, loan_gumri, payBalance);
						total_iza = total_iza + iza;
						if (loan_type == 1) {
							repayment = CalculateUtil.getRepayment(loan_type, org_loan, iza, loan_gumri, loan_period, loan_term, loan, i);
							if (i >= loan_term) {	//거치기간 후부터 계산
								org_loan = CalculateUtil.getOrgLoan(loan_type, i, loan_period, loan, loan_term, repayment, iza);
							}
						} else {
							if (i >= loan_term) {	//거치기간 후부터 계산
								org_loan = CalculateUtil.getOrgLoan(loan_type, i, loan_period, loan, loan_term, repayment, iza);
							}
							repayment = CalculateUtil.getRepayment(loan_type, org_loan, iza, loan_gumri, loan_period, loan_term, loan, i);
						}
						org_loan_tot = org_loan_tot + org_loan;
						payBalance = loan - org_loan_tot;

						rows.push({
							'num': (i + 1),
							'payments': $.app.util.printNumber(Math.round(repayment)),
							'principal': $.app.util.printNumber(Math.round(org_loan)),
							'interest': $.app.util.printNumber(Math.round(iza)),
							'total': $.app.util.printNumber(Math.round(org_loan_tot)),
							'balance': $.app.util.printNumber(Math.round(payBalance))
						});
					}
					$.app.loan.vm.rows(rows);

					if (loan_type == 3) {
						monthly_loan = total_iza / loan_period;
					} else if (loan_type == 2) {
						monthly_loan = loan / (loan_period - loan_term);
					} else if (loan_type == 1) {
						monthly_loan = loan * loan_gumri / 12;
						monthly_loan = monthly_loan * (Math.pow((1 + loan_gumri / 12), (loan_period - loan_term)));
						monthly_loan = monthly_loan / (Math.pow((1 + loan_gumri / 12), (loan_period - loan_term)) - 1);
					}
					loan_n_iza = loan + total_iza;
					$.app.loan.vm.loanMonth($.app.util.printNumber(Math.round(monthly_loan)));
					$.app.loan.vm.loanRateAmt($.app.util.printNumber(Math.round(total_iza)));
					$.app.loan.vm.loanTotalAmt($.app.util.printNumber(Math.round(loan_n_iza)));
				},
				'reset': function () {
					$.app.loan.vm.money(0);
					$.app.loan.vm.rate(0);
					$.app.loan.vm.month(0);
					$.app.loan.vm.term(0);
					$.app.loan.vm.type(0);
				}
			}
		}
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

	$.app.loan.vm.moneyText = ko.computed(function () {
		return $.app.util.moneyKorean($.app.loan.vm.money());
	});

	// https://spot.wooribank.com/pot/Dream?withyou=CMBBS0086&cc=c006244:c006294

	$(function () {
		var form = $('#cWriteForm');
		ko.applyBindings($.app.loan.vm, form.get(0));
		form = null;

		$.ripple(".btn, .nav-tabs > li", {});

		$(".cCheckboxLabel").labelauty();
	});
})();