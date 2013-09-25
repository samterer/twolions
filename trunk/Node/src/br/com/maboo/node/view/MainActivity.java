package br.com.maboo.node.view;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.SpinnerAdapter;
import br.com.maboo.node.R;

public class MainActivity extends Activity {
	ViewPager mViewPager;
	private ActionBar.TabListener tabListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// show the given tab
				// mViewPager.setCurrentItem(tab.getPosition());
			}

			public void onTabUnselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// hide the given tab
			}

			public void onTabReselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// probably ignore this event
			}
		};

		// Add 3 tabs, specifying the tab's text and TabListener
		for (int i = 0; i < 3; i++) {
			actionBar.addTab(getTab(i, actionBar));
		}

		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
				R.array.action_list,
				android.R.layout.simple_spinner_dropdown_item);

		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				// TODO
				return false;
			}
		};

		if (savedInstanceState != null) {
			int savedIndex = savedInstanceState.getInt("SAVED_INDEX");
			getActionBar().setSelectedNavigationItem(savedIndex);

			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			getActionBar().setListNavigationCallbacks(mSpinnerAdapter,
					navigationListener);
		}

	}

	private Tab getTab(int pos, ActionBar actionBar) {

		Tab tab = null;
		tab = actionBar.newTab();

		switch (pos) {
		case 0:

			// tab.setText("Map");
			tab.setIcon(R.drawable.ic_action_locate);
			tab.setTabListener(new TabListener<MapView>(this, "Map",
					MapView.class));
			break;
		case 1:

			// tab.setText("Profile");
			tab.setIcon(R.drawable.ic_action_user);
			tab.setTabListener(new TabListener<ProfileView>(this, "Profile",
					ProfileView.class));
			break;
		case 2:

			// tab.setText("Share");
			tab.setIcon(R.drawable.ic_action_share);
			tab.setTabListener(new TabListener<ShareView>(this, "Share",
					ShareView.class));
			break;
		}

		return tab;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("SAVED_INDEX", getActionBar()
				.getSelectedNavigationIndex());
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

}
