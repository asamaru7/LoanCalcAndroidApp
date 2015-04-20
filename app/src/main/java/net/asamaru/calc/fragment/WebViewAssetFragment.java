package net.asamaru.calc.fragment;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EFragment;

@EFragment
public class WebViewAssetFragment extends net.asamaru.bootstrap.fragment.WebViewAssetFragment {
	@AfterInject
	void afterInject() {
		addJs("js/base.js");
		addCss("css/base.css");
	}
}
