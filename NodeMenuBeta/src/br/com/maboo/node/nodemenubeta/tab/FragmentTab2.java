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
import br.com.maboo.node.nodemenubeta.adapter.ListFriendAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.friend.BaseListElement;
import com.facebook.model.GraphUser;

public class FragmentTab2 extends SherlockFragment {

	private String TAG = "FragmentTab2";

	private ArrayList<BaseListElement> itens;

	private ListView listview_log;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Log.i(TAG, "onCreate...");

		itens = new ArrayList<BaseListElement>();

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i(TAG, "onCreateView...");

		// Get the view from fragmenttab2.xml
		final View view = inflater.inflate(R.layout.fragmenttab2, container,
				false);

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
												+ g.getId()
												+ "/picture?width=" + 40
												+ "&height=" + 40);
								friendBitmap = BitmapFactory
										.decodeStream(bitmapURL
												.openConnection()
												.getInputStream());
							} catch (Exception e) {
								// Log.i(TAG, e.toString());
							}

							// convert bitmap into imageView
							Drawable d = new BitmapDrawable(getResources(),
									friendBitmap);

							itens.add(new BaseListElement(d, g.getId(),
									g.getName(), g
											.getBirthday()) {

								public OnClickListener getOnClickListener() {
									Log.i(TAG,
											"click in -> "
													+ g.getName());
									return null;
								}
							});
						}

						// Log.i(TAG, "verificando a lista...");
						// for (final BaseListElement b : itens) {
						//
						// Log.i(TAG, "graphUser: " + b.getText1());
						// }

						listview_log = (ListView) view
								.findViewById(R.id.list_friend);
						listview_log.setAdapter(new ListFriendAdapter(
								FragmentTab2.this, itens));

					}
				});
		request.executeAsync();

		return view;
	}

}
