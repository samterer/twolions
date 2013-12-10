package br.com.maboo.node.nodemenubeta.tab;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import br.com.maboo.node.nodemenubeta.adapter.ListFriendAdapter;
import br.com.maboo.node.nodemenubeta.transaction.TransactionCircle;

import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.friend.BaseListElement;
import com.facebook.model.GraphUser;

public class FragmentFriend extends TransactionCircle {

	private String TAG = "FragmentTab2";

	private ArrayList<BaseListElement> itens;

	private ListView listview_log;

	private View view;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		//
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i(TAG, "onCreateView...");

		itens = new ArrayList<BaseListElement>();

		// Get the view from fragmenttab2.xml
		view = inflater.inflate(R.layout.fragmentfriend, container, false);

		montaTela(savedInstanceState);

		return view;
	}

	public void montaTela(Bundle savedInstanceState) {

		Log.i(TAG, "montaTela...");

		listview_log = (ListView) view.findViewById(R.id.list_friend);
		listview_log.setVisibility(View.INVISIBLE);

		Log.i(TAG, "startTransaction...");

		update();


	}

	public void update() {

		Log.i(TAG, "update...");

		Log.i(TAG, "execute...");

		Request request = Request.newMyFriendsRequest(
				Session.getActiveSession(),
				new Request.GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {

						Log.i(TAG, "onCompleted...");

						for (final GraphUser g : users) {

							URL bitmapURL;
							Bitmap friendBitmap = null;
							try {
								bitmapURL = new URL(
										"https://graph.facebook.com/"
												+ g.getId() + "/picture?width="
												+ 40 + "&height=" + 40);
								friendBitmap = BitmapFactory
										.decodeStream(bitmapURL
												.openConnection()
												.getInputStream());
							} catch (Exception e) {
								// Log.i(TAG, e.toString());
							}

							// convert bitmap into imageView
							Drawable d = null;
							d = new BitmapDrawable(getResources(), friendBitmap);

							// Log.i(TAG, d.toString());

							itens.add(new BaseListElement(d, g.getId(), g
									.getName(), g.getBirthday()) {

								public OnClickListener getOnClickListener() {
									//Log.i(TAG, "click in -> " + g.getName());
									return null;
								}
							});
							

							listview_log
									.setAdapter(new ListFriendAdapter(FragmentFriend.this, itens));
							listview_log.setVisibility(View.VISIBLE);
							
							ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
							progressBar.setVisibility(View.INVISIBLE);

						}

					}
				});
		request.executeAsync();

	}

}
