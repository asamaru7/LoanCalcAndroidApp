package net.asamaru.calc.data;

import net.asamaru.bootstrap.Advisor;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class HistoryWorker {
	static public void add(String key, int money, int type, float rate, int period, int term, int loanMonth, int loanRateAmt, int loanTotalAmt) {
		Realm realm = Realm.getInstance(Advisor.getAppContext());
		realm.beginTransaction();
		History history = realm.createObject(History.class);
		history.setKey(key);
		history.setDate(new Date());
		history.setMoney(money);
		history.setType(type);
		history.setRate(rate);
		history.setPeriod(period);
		history.setTerm(term);
		history.setLoanMonth(loanMonth);
		history.setLoanRateAmt(loanRateAmt);
		history.setLoanTotalAmt(loanTotalAmt);
		realm.commitTransaction();
	}

	static public void remove(String key) {
		Realm realm = Realm.getInstance(Advisor.getAppContext());
		realm.beginTransaction();
		RealmResults<History> result = realm.where(History.class).equalTo("key", key).findAll();
		result.clear();
		realm.commitTransaction();
	}

	//	static public History[] getList() {
	static public RealmResults<History> getList() {
		Realm realm = Realm.getInstance(Advisor.getAppContext());
		RealmQuery<History> query = realm.where(History.class);
		return query.findAll();
//		RealmResults<History> list = query.findAll();
//		History[] rtn = new History[list.size()];
//		for (int i = 0, iCnt = list.size(); i < iCnt; i++) {
//			rtn[i] = list.get(i);
//		}
//		return rtn;
	}

	static public void removeAll() {
		Realm realm = Realm.getInstance(Advisor.getAppContext());
		realm.beginTransaction();
		RealmResults<History> result = realm.where(History.class).findAll();
		result.clear();
		realm.commitTransaction();
	}
}
