package net.asamaru.calc;

//import net.asamaru.bootstrap.activity.HtmlActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.asamaru.bootstrap.Advisor;
import net.asamaru.bootstrap.activity.NavigationDrawerActivity;
import net.asamaru.calc.fragment.WebViewAssetFragment;
import net.asamaru.calc.fragment.WebViewAssetFragment_;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends NavigationDrawerActivity {
	final static private List<Menu> menus = Arrays.asList(
			(Menu) new AssetMenu("대출 이자", "loan.html",
					new String[]{
							"res/knockout/knockout-3.3.0.js",
							"res/ripplejs/ripple.min.js",
							"res/jquery-labelauty/jquery-labelauty.js",
							"res/alertify/js/alertify.js",
							"js/loan.js"},
					new String[]{
							"res/ripplejs/ripple.min.css",
							"res/jquery-labelauty/jquery-labelauty.css",
							"res/alertify/css/alertify.core.css",
							"res/alertify/css/alertify.bootstrap.css",
							"css/loan.css"}),
			(Menu) new AssetMenu("계산 이력", "area.html")
//			(Menu) new AssetMenu("예금/적금", "deposit.html"),
//			(Menu) new AssetMenu("연봉", "salary.html"),
//			(Menu) new AssetMenu("평형", "area.html")
	);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Advisor.isKorean();
		replaceFragmentByPosition(0);
	}

	private void replaceFragmentByPosition(int position) {
		Menu menu = menus.get(position);
		if (menu instanceof AssetMenu) {
			WebViewAssetFragment fragment = WebViewAssetFragment_.builder().path(((AssetMenu) menu).path).build();
			fragment.addJs(((AssetMenu) menu).js);
			fragment.addCss(((AssetMenu) menu).css);
			replaceFragment(fragment);
		}
		getSupportActionBar().setTitle(menu.title);
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


	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		if (Advisor.isDebugable()) {
			MenuItem item1 = menu.add(0, 0, 0, "Reload");
			{
				item1.setAlphabeticShortcut('a');
				item1.setIcon(android.R.drawable.btn_default_small);
				item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_IF_ROOM);
			}
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		CharSequence title = item.getTitle();
		if ((title != null) && (title.toString().equals("Reload"))) {
			if (activeFragment instanceof WebViewAssetFragment) {
				((WebViewAssetFragment) activeFragment).reloadHtml();
			}
		}
		return super.onOptionsItemSelected(item);
	}

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