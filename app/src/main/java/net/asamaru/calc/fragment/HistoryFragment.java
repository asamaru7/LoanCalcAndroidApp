package net.asamaru.calc.fragment;

import com.x5.template.Chunk;

import net.asamaru.calc.R;
import net.asamaru.calc.data.History;
import net.asamaru.calc.data.HistoryWorker;

import org.androidannotations.annotations.EFragment;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.RealmResults;

@EFragment
public class HistoryFragment extends WebViewAssetFragment {
	public HistoryFragment() {
		super();
		path = "history.html";
		addJs(new String[]{
				"res/ripplejs/ripple.min.js",
				"res/alertify/js/alertify.js",
				"js/history.js"});
		addCss(new String[]{
				"res/ripplejs/ripple.min.css",
				"res/alertify/css/alertify.core.css",
				"res/alertify/css/alertify.bootstrap.css",
				"css/history.css"});
	}

	@Override
	protected JavaScriptInterface getJavaScriptInterface() {
		return new JavaScriptInterface(this, webView);
	}

	@Override
	protected String readText(String file) throws IOException {
		Chunk c = new Chunk();
		c.append(super.readText(file));

//		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		final DecimalFormat df = new DecimalFormat("#,###");
		final DecimalFormat dff = new DecimalFormat("#,###.00");

		RealmResults<History> list = HistoryWorker.getList();
		List<Map<String, String>> histories = new ArrayList<>();

		for (int i = 0, iCnt = list.size(); i < iCnt; i++) {
			final History item = list.get(i);
			histories.add(new HashMap<String, String>() {{
				put("key", item.getKey());
				put("date", dateFormat.format(item.getDate()));
				put("type", typeText(item.getType()));
				put("suumaryText", summaryText(item.getType()));
				put("money", df.format(item.getMoney()));
				put("rate", dff.format(item.getRate()));
				put("period", df.format(item.getPeriod()));
				put("term", ((item.getTerm() > 0) ? df.format(item.getTerm()) : "-"));
				put("loanMonth", df.format(item.getLoanMonth()));
				put("loanRateAmt", df.format(item.getLoanRateAmt()));
				put("loanTotalAmt", df.format(item.getLoanTotalAmt()));
			}});
		}
		c.set("histories", histories);

		String[] keyValues = getResources().getStringArray(R.array.i18n_history);
		String[] items;
		for (String keyValue : keyValues) {
			items = keyValue.split(":", 2);
			if (items.length >= 2) {
				c.set(items[0], items[1]);
			}
		}
		return c.toString();
//		return super.readText(file);
	}

	static private String typeText(int type) {
		switch (type) {
			case 1:
				return "원리금균등상환";
			case 2:
				return "원금균등상환";
			case 3:
				return "원금만기일시상환";
		}
		return "";
	}

	static private String summaryText(int type) {
		switch (type) {
			case 1:
				return "월상환금";
			case 2:
				return "월납입원금";
			case 3:
				return "월평균이자";
		}
		return "";
	}
}
