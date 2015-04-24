package net.asamaru.calc.fragment;

import com.x5.template.Chunk;

import net.asamaru.calc.R;

import org.androidannotations.annotations.EFragment;

import java.io.IOException;

@EFragment
public class LoanFragment extends WebViewAssetFragment {
	public LoanFragment() {
		super();
		path = "loan.html";
		addJs(new String[]{
				"res/knockout/knockout-3.3.0.js",
				"res/ripplejs/ripple.min.js",
				"res/jquery-labelauty/jquery-labelauty.js",
				"res/alertify/js/alertify.js",
				"res/jquery-scrollTo/jquery.scrollTo.min.js",
				"js/loan.js"});
		addCss(new String[]{
				"res/ripplejs/ripple.min.css",
				"res/jquery-labelauty/jquery-labelauty.css",
				"res/alertify/css/alertify.core.css",
				"res/alertify/css/alertify.bootstrap.css",
				"css/loan.css"});
	}

	@Override
	protected String readText(String file) throws IOException {
		Chunk c = new Chunk();
		c.append(super.readText(file));

		String[] keyValues = getResources().getStringArray(R.array.i18n_loan);
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
