package br.com.maboo.node.nodemenubeta.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import br.com.maboo.node.nodemenubeta.tab.FragmentTab1;
import br.com.maboo.node.nodemenubeta.tab.FragmentTab2;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 2;
	private String titles[] = new String[] { "Tab1", "Tab2" };

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

			// Open FragmentTab1.java
		case 0:
			FragmentTab1 fragmenttab1 = new FragmentTab1();
			return fragmenttab1;

			// Open FragmentTab2.java
		case 1:
			FragmentTab2 fragmenttab2 = new FragmentTab2();
			return fragmenttab2;
		}
		return null;
	}

	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

}