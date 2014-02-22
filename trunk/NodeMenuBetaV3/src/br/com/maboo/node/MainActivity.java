package br.com.maboo.node;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import br.com.maboo.node.adapter.MenuListAdapter;
import br.com.maboo.node.fragment.FragmentAbout;
import br.com.maboo.node.fragment.FragmentFriends;
import br.com.maboo.node.fragment.FragmentLogout;
import br.com.maboo.node.fragment.FragmentMap;
import br.com.maboo.node.fragment.FragmentProfile;
import br.com.maboo.node.fragment.FragmentSettings;
import br.com.maboo.node.sessao.Sessao;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.scrumptious.auxiliar.FaceUserVO;

public class MainActivity extends SherlockFragmentActivity {

	// Declare Variables
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	MenuListAdapter mMenuAdapter;

	// numero dos itens
	private final int PROFILE = 0;
	private final int MAP = 1;
	private final int FRIENDS = 2;
	private final int SETTINGS = 3;
	private final int HELP = 4;
	private final int LOGOUT = 5;

	// tamanho do menu (baseado no valor do ultimo item)
	private final int TAM_MENU = LOGOUT + 1;

	// titulo no menu lateral
	String[] title;
	// subtitulo no menu lateral
	String[] subtitle;
	int[] icon;
	Fragment[] fragments;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from drawer_main.xml
		setContentView(R.layout.drawer_main);

		initMenu();

		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.listview_drawer);

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Pass string arrays to MenuListAdapter
		mMenuAdapter = new MenuListAdapter(MainActivity.this, title, subtitle,
				icon);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(mMenuAdapter);

		// Capture listview menu item click
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// set default color node in bar
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ff7d43")));

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				// Set the title on the action when drawer open
				getSupportActionBar().setTitle(mDrawerTitle);
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(1);
		}

	}

	private void initMenu() {

		// Get the Title
		mTitle = mDrawerTitle = getTitle();

		// fragments
		fragments = new Fragment[TAM_MENU];

		// Generate title
		title = new String[TAM_MENU];

		// Generate subtitle
		// subtitle = new String[] { "noob", "Node map" };
		subtitle = new String[TAM_MENU];

		// Generate icon
		// icon = new int[] { R.drawable.action_about, R.drawable.location_map
		// };
		icon = new int[TAM_MENU];

		// cria os fragments (paginas no menu lateral
		// profile
		String name_user = FaceUserVO.user_name;
		title[PROFILE] = name_user;
		subtitle[PROFILE] = "noob";
		icon[PROFILE] = R.drawable.fbprofile;
		fragments[PROFILE] = new FragmentProfile();

		// maps
		title[MAP] = "Map";
		subtitle[MAP] = "node map";
		icon[MAP] = R.drawable.location_map;
		fragments[MAP] = new FragmentMap();

		// settings
		title[FRIENDS] = "Friends";
		subtitle[FRIENDS] = "call your friends";
		icon[FRIENDS] = R.drawable.action_friends;
		fragments[FRIENDS] = new FragmentFriends();

		// settings
		title[SETTINGS] = "Settings";
		subtitle[SETTINGS] = "edit settings";
		icon[SETTINGS] = R.drawable.action_settings;
		fragments[SETTINGS] = new FragmentSettings();

		// help
		title[HELP] = "About";
		subtitle[HELP] = "who does this";
		icon[HELP] = R.drawable.action_about;
		fragments[HELP] = new FragmentAbout();

		// logout app
		title[LOGOUT] = "Logout";
		subtitle[LOGOUT] = "logout";
		icon[LOGOUT] = R.drawable.content_backspace;
		fragments[LOGOUT] = new FragmentLogout();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	// ListView click listener in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	// Select a item in navigation drawer
	private void selectItem(int position) {

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		// Locate Position
		ft.setCustomAnimations(R.anim.frag_slide_in, R.anim.frag_slide_out);

		ft.replace(R.id.content_frame, fragments[position]);
		ft.commit();
		mDrawerList.setItemChecked(position, true);

		// Get the title followed by the position
		setTitle(title[position]);

		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/*******************************************************************************
	 * onBackPressed
	 *******************************************************************************/
	private Toast backtoast;

	@Override
	public void onBackPressed() {

		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0) {
			// If there are back-stack entries, leave the FragmentActivity
			// implementation take care of them.
			manager.popBackStack();

		} else {

			// verifica a tela que o usuario esta (momento)
			if (Sessao.TELA == 1) {

				hideInfoBar();

				// muda estado da tela
				Sessao.TELA = 0;
				return;
			}

			// regra de saida do app
			if (backtoast != null
					&& backtoast.getView().getWindowToken() != null) {
				this.finish();
			} else {
				backtoast = Toast.makeText(this, "Press back to exit",
						Toast.LENGTH_SHORT);
				backtoast.show();
			}

			// super.getActivity().onBackPressed();

			// Otherwise, ask user if he wants to leave :)
			// super.onBackPressed();

		}
	}

	/**
	 * esconde a barra de informacao do endereço
	 */
	private void hideInfoBar() {

		// anime down
		Animation barDown = AnimationUtils.loadAnimation(this, R.anim.bar_down);

		// esconde a info bar
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.bar_map_info);
		if (rl.getVisibility() == View.VISIBLE) {
			rl.startAnimation(barDown);
			rl.setVisibility(View.INVISIBLE);
		}

	}
}
