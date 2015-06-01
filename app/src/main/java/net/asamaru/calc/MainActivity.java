package net.asamaru.calc;

//import net.asamaru.bootstrap.activity.HtmlActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Subscribe;

import net.asamaru.bootstrap.Advisor;
import net.asamaru.bootstrap.activity.NavigationDrawerActivity;
import net.asamaru.calc.fragment.HistoryFragment_;
import net.asamaru.calc.fragment.LoanFragment;
import net.asamaru.calc.fragment.LoanFragment_;
import net.asamaru.calc.fragment.WebViewAssetFragment;
import net.asamaru.calc.fragment.WebViewAssetFragment_;

import org.androidannotations.api.builder.FragmentBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends NavigationDrawerActivity {
	final static private List<Menu> menus = Arrays.asList(
			(Menu) new FragmentMenu(Advisor.getResources().getString(R.string.loanCalc), LoanFragment_.class),
			(Menu) new FragmentMenu(Advisor.getResources().getString(R.string.calcHist), HistoryFragment_.class)
//			(Menu) new AssetMenu("계산 이력", "history.html")
	);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Advisor.getEventBus().register(this);

		final AdView adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
		AdSize adSize = adView.getAdSize();
		final int adHeight = adSize.getHeightInPixels(this);
		adView.setAdListener(new AdListener() {
								 public void onAdLoaded() {
									 runOnUiThread(new Runnable() {
										 public void run() {
											 if (adView.getParent() == null) {
												 View contents = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
												 ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contents.getLayoutParams();
												 params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin + adHeight);
												 contents.setLayoutParams(params);

												 addContentView(adView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
												 ((FrameLayout.LayoutParams) adView.getLayoutParams()).gravity = Gravity.BOTTOM;
											 }
										 }
									 });
								 }
							 }
		);
		adView.loadAd(new AdRequest.Builder()
				.addTestDevice("AB60482CB227CE6C1B52F429850D039E")    // nexus s
				.addTestDevice("B1EFBB39D34D357094674D8266376BC2")    // note 3
				.build());

		replaceFragmentByPosition(0);
	}

	@SuppressWarnings("unchecked")
	private void replaceFragmentByPosition(int position) {
		Menu menu = menus.get(position);
		if (menu instanceof AssetMenu) {
			WebViewAssetFragment fragment = WebViewAssetFragment_.builder().path(((AssetMenu) menu).path).build();
			fragment.addJs(((AssetMenu) menu).js);
			fragment.addCss(((AssetMenu) menu).css);
			replaceFragment(fragment);
		} else if (menu instanceof FragmentMenu) {
			try {
				String canonicalName = (((FragmentMenu) menu).fragmentClass).getCanonicalName();
				if (canonicalName.equals(LoanFragment_.class.getCanonicalName())) {
					replaceFragment(LoanFragment_.builder().build());
				} else if (canonicalName.equals(HistoryFragment_.class.getCanonicalName())) {
					replaceFragment(HistoryFragment_.builder().build());
				}
//				Method m = ((Class<?>) (((FragmentMenu) menu).fragmentClass)).getMethod("builder");
//				FragmentBuilder<?, Fragment> result = (FragmentBuilder<?, Fragment>) m.invoke(null);
//				replaceFragment(result.build());
			} catch (Exception e) {
				Logger.e(e);
			}
		}
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(menu.title);
		}
	}

	protected ListAdapter getListAdapter(ListView listView) {
		return new ArrayAdapter<Menu>(this, android.R.layout.simple_list_item_1, menus) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tv = (TextView) view.findViewById(android.R.id.text1);
				Menu menu = menus.get(position);
				tv.setText(menu.title);
				return view;
			}
		};
	}

	protected boolean onDrawerItemClick(AdapterView<?> parent, View view, int position, long id) {
		replaceFragmentByPosition(position);
		return true;
	}

	// event bus
	static public class ReCalcEvent {
		public String key;

		public ReCalcEvent(String key) {
			this.key = key;
		}
	}

	@Subscribe
	public void recalcLoan(final ReCalcEvent event) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LoanFragment loanFragment = LoanFragment_.builder().key(event.key).build();
				replaceFragment(loanFragment);
			}
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(android.view.Menu menu) {
//		if (Advisor.isDebugable()) {
//			MenuItem item1 = menu.add(0, 0, 0, "Reload");
////				item1.setAlphabeticShortcut('a');
//			item1.setIcon(android.R.drawable.btn_default_small);
//			item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_IF_ROOM);
//		}
//		return super.onCreateOptionsMenu(menu);
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		CharSequence title = item.getTitle();
//		if ((title != null) && (title.toString().equals("Reload"))) {
//			if (activeFragment instanceof WebViewAssetFragment) {
//				((WebViewAssetFragment) activeFragment).reloadHtml();
//			}
//		}
//		return super.onOptionsItemSelected(item);
//	}

	abstract static class Menu {
		String title;

		public Menu(String title) {
			this.title = title;
		}
	}

	static class AssetMenu extends Menu {
		String path;
		String[] js;
		String[] css;

		public AssetMenu(String title, String path) {
			super(title);
			this.path = path;
		}

		public AssetMenu(String title, String path, String[] js, String[] css) {
			this(title, path);
			this.js = js;
			this.css = css;
		}
	}

	static class FragmentMenu extends Menu {
		Class fragmentClass;

		public FragmentMenu(String title, Class fragmentClass) {
			super(title);
			this.fragmentClass = fragmentClass;
		}
	}
}


//public class MainActivity extends HtmlActivity {
//	@Override
//	protected String getAssetPath() {
//		return "loan.html";
//	}
//
////	@Override
////	protected String getUrl() {
////		return "http://www.naver.com";
////	}
//}