package net.asamaru.calc.fragment;

import com.x5.template.Chunk;

import net.asamaru.calc.R;

import org.androidannotations.annotations.EFragment;

import java.io.IOException;

@EFragment
public class HistoryFragment extends WebViewAssetFragment {
	public HistoryFragment() {
		super();
		path = "history.html";
		addJs(new String[]{
				"res/underscore/underscore-min.js",
				"res/ripplejs/ripple.min.js",
				"res/alertify/js/alertify.js",
				"js/history.js"});
		addCss(new String[]{
				"res/ripplejs/ripple.min.css",
				"res/jquery-labelauty/jquery-labelauty.css",
				"res/alertify/css/alertify.core.css",
				"res/alertify/css/alertify.bootstrap.css",
				"css/history.css"});
	}

	@Override
	protected String readText(String file) throws IOException {
		Chunk c = new Chunk();
		c.append(super.readText(file));

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
}
