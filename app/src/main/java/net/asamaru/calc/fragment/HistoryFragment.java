package net.asamaru.calc.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.x5.template.Chunk;

import net.asamaru.bootstrap.Advisor;
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
	static String language;

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

		language = Advisor.getResources().getString(R.string.language);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		MenuItem item1 = menu.add(0, 0, 0, "Clear");
		item1.setIcon(new IconDrawable(this.getActivity(), Iconify.IconValue.fa_trash_o).colorRes(android.R.color.white).actionBarSize());
		item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_IF_ROOM);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String lTitle = "이력 모두 삭제";
		String lMessage = "계산 이력을 모두 삭제하시겠습니까?";
		String lBtnDelete = "삭제";
		String lBtnCancel = "취소";
		if (!"ko".equals(language)) {
			lTitle = "Clear History";
			lMessage = "Are you sure you want to delete all the calculation history?";
			lBtnDelete = "Delete";
			lBtnCancel = "Cancel";
		}
		CharSequence title = item.getTitle();
		if ((title != null) && (title.toString().equals("Clear"))) {
			new AlertDialog.Builder(this.getActivity())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(lTitle)
					.setMessage(lMessage)
					.setPositiveButton(lBtnDelete, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							HistoryWorker.removeAll();
							loadHtml();
						}
					})
					.setNegativeButton(lBtnCancel, null)
					.show();
		}
		return super.onOptionsItemSelected(item);
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
				put("id", item.getKey().replaceAll("[\\.:]", "_"));
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
		if ("ko".equals(language)) {
			switch (type) {
				case 1:
					return "원리금균등상환";
				case 2:
					return "원금균등상환";
				case 3:
					return "원금만기일시상환";
			}
		} else {
			switch (type) {
				case 1:
					return "P&I with equal payments";
				case 2:
					return "P with equal payments";
				case 3:
					return "Bullet repayment";
			}
		}
		return "";
	}

	static private String summaryText(int type) {
		if ("ko".equals(language)) {
			switch (type) {
				case 1:
					return "월상환금";
				case 2:
					return "월납입원금";
				case 3:
					return "월평균이자";
			}
		} else {
			switch (type) {
				case 1:
					return "Monthly Payments";
				case 2:
					return "Monthly Principal Payment";
				case 3:
					return "Monthly Average Interest";
			}
		}
		return "";
	}
}
