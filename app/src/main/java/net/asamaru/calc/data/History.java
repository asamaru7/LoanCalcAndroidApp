package net.asamaru.calc.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class History extends RealmObject {

	@PrimaryKey
	private String key;
	private Date date;
	private int type;
	private int money;
	private float rate;
	private int period;
	private int term;
	private int loanMonth;
	private int loanRateAmt;
	private int loanTotalAmt;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public int getLoanMonth() {
		return loanMonth;
	}

	public void setLoanMonth(int loanMonth) {
		this.loanMonth = loanMonth;
	}

	public int getLoanRateAmt() {
		return loanRateAmt;
	}

	public void setLoanRateAmt(int loanRateAmt) {
		this.loanRateAmt = loanRateAmt;
	}

	public int getLoanTotalAmt() {
		return loanTotalAmt;
	}

	public void setLoanTotalAmt(int loanTotalAmt) {
		this.loanTotalAmt = loanTotalAmt;
	}
}