package net.asamaru.calc.fragment;

import android.app.Fragment;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import net.asamaru.calc.data.HistoryWorker;

public class JavaScriptInterface extends net.asamaru.bootstrap.lib.JavaScriptInterface {
	public JavaScriptInterface(Fragment fragment, WebView webview) {
		super(fragment, webview);
	}

	@SuppressWarnings("unused")
	@JavascriptInterface
	public void addHistory(String key, int money, int type, float rate, int period, int term, int loanMonth, int loanRateAmt, int loanTotalAmt) {
		HistoryWorker.add(key, money, type, rate, period, term, loanMonth, loanRateAmt, loanTotalAmt);
	}

	@SuppressWarnings("unused")
	@JavascriptInterface
	public void removeHistory(String key) {
		HistoryWorker.remove(key);
	}

	@SuppressWarnings("unused")
	@JavascriptInterface
	public void removeAllHistory() {
		HistoryWorker.removeAll();
	}
}
