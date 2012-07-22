package br.com.twolions.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import br.com.twolions.R;
import br.com.twolions.core.ActivityCircle;

public class MenuScreen extends ActivityCircle implements OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.menu);

	}

	public void callPlay(View v) {

		startActivity(new Intent(this, QuestScreen.class));

	}

	public void callAbout(View v) {

		startActivity(new Intent(this, AboutScreen.class));

	}

	public void callRanking(View v) {

		startActivity(new Intent(this, RankingScreen.class));

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
