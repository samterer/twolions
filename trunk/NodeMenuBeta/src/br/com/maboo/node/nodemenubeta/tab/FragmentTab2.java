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
import br.com.maboo.node.nodemenubeta.adapter.ListAdapter;
import br.com.maboo.node.nodemenubeta.transaction.Transaction;
import br.com.maboo.node.nodemenubeta.transaction.TransactionCircle;

import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.friend.BaseListElement;
import com.facebook.model.GraphUser;

public class FragmentTab2 extends TransactionCircle implements Transaction {

	private ArrayList<BaseListElement> itens;

	private ListView listview_log;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		itens = new ArrayList<BaseListElement>();

		Log.i("appLog", "onCreate...");
		
		startTransaction(this);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i("appLog", "onCreateView...");

		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.fragmenttab2, container, false);

		listview_log = (ListView) view.findViewById(R.id.list_friend);
		listview_log.setAdapter(new ListAdapter(this, itens));

		return view;
	}

	@Override
	public void execute() throws Exception {
		
		Log.i("appLog", "execute...");

		Request request = Request.newMyFriendsRequest(
				Session.getActiveSession(),
				new Request.GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {
						
						Log.i("appLog", "onCompleted...");

						for (final GraphUser graphUser : users) {
							
							Log.i("appLog", "graphUser: "+graphUser.getFirstName());

							URL bitmapURL;
							Bitmap friendBitmap = null;
							try {
								bitmapURL = new URL(
										"https://graph.facebook.com/"
												+ graphUser.getId()
												+ "/picture?width=" + 40
												+ "&height=" + 40);
								friendBitmap = BitmapFactory
										.decodeStream(bitmapURL
												.openConnection()
												.getInputStream());
							} catch (Exception e) {
								Log.i("appLog", e.toString());
							}

							// convert bitmap into imageView
							Drawable d = new BitmapDrawable(getResources(),
									friendBitmap);

							itens.add(new BaseListElement(d, graphUser
									.getName(), graphUser.getUsername(),
									Integer.valueOf(2).intValue()) {

								public OnClickListener getOnClickListener() {
									Log.i("appLog",
											"click in -> "
													+ graphUser.getName());
									return null;
								}
							});
						}

					}
				});
		request.getCallback();

	}

}
