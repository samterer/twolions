package br.com.maboo.node;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import br.com.maboo.node.screens.MapScreen;
import br.com.maboo.node.screens.ProfileScreen;
import br.com.maboo.node.screens.ShareScreen;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Fragment fragment = getScreen(position);
		Bundle args = new Bundle();
		args.putInt("section_number", position + 1);
		fragment.setArguments(args);
		return fragment;
	}

	public int getCount() {
		// Show 3 total pages.
		return 3;
	}

	public Fragment getScreen(int position) {
		switch (position) {
		case 1:
			return new ProfileScreen();

		case 2:
			return new ShareScreen();

		default:
			return new MapScreen();

		}
	}

	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		
		Log.i("appLog","retornando titulo correspondente a posição: "+position);
		
		switch (position) {
		case 0:
			return "map".toUpperCase(l);
		case 1:
			return "profile".toUpperCase(l);
		case 2:
			return "share".toUpperCase(l);
		}
		return null;
	}
}
