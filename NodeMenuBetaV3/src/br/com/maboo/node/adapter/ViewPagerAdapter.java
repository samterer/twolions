package br.com.maboo.node.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import br.com.maboo.node.tab.TabFriend;
import br.com.maboo.node.tab.TabProfile;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 2;
	private String titles[] = new String[] { "MyNode", "NodeFriends" };

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		
		Log.i("ViewPagerAdapter", "Carregando tab: "+position);
		
		switch (position) {

			// Open FragmentTab1.java
		case 0:
			TabProfile fragmenttab1 = new TabProfile();
			return fragmenttab1;

			// Open FragmentTab2.java
		case 1:
			TabFriend fragmenttab2 = new TabFriend();
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