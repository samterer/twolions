package br.com.maboo.node.nodemenubeta.tab;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.com.maboo.node.nodemenubeta.adapter.ListFriendAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidbegin.menuviewpagertutorial.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.friend.BaseListElement;
import com.facebook.model.GraphUser;

public class FragmentFriend extends SherlockFragment {

	private String TAG = "FragmentTab2";

	private ArrayList<BaseListElement> itens;
	private List<GraphUser> graphUsers;

	private ListView listview_log;

	private View view;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		itens = new ArrayList<BaseListElement>();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view from fragmenttab2.xml
		view = inflater.inflate(R.layout.fragmentfriend, container, false);

		montaTela(savedInstanceState);

		return view;
	}

	private void montaTela(Bundle savedInstanceState) {

		Log.i(TAG, "montaTela...");

		listview_log = (ListView) view.findViewById(R.id.list_friend);
		listview_log.setVisibility(View.INVISIBLE);

		// Bind onclick event handler
		listview_log.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView t = (TextView) view.findViewById(R.id.id);

				sendToProfilePage(t.getText().toString());
			}
		});

		montaLista();

	}

	private void sendToProfilePage(String id) {
		String url = "http://www.facebook.com/" + id;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	private void montaLista() {

		Log.i(TAG, "montaLista...");

		Request request = Request.newMyFriendsRequest(
				Session.getActiveSession(),
				new Request.GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> users,
							Response response) {

						Log.i(TAG, "onCompleted...");
						graphUsers = users;

						criaLista();

					}
				});
		request.executeAsync();

	}

	private void criaLista() {
		for (final GraphUser g : graphUsers) {

			Log.i(TAG, "get frind: " + g.getFirstName());

			/*
			 * URL bitmapURL; Bitmap friendBitmap = null; try { bitmapURL = new
			 * URL( "https://graph.facebook.com/" + g.getId() +
			 * "/picture?width=" + 40 + "&height=" + 40); friendBitmap =
			 * BitmapFactory .decodeStream(bitmapURL .openConnection()
			 * .getInputStream()); } catch (Exception e) { e.printStackTrace();
			 * }
			 * 
			 * // convert bitmap into imageView
			 */
			Drawable d = null;
			// d = new BitmapDrawable(getResources(), friendBitmap);

			try {
				itens.add(new BaseListElement(d, g.getId().toString(), g
						.getName().toString().toString(), "noob") {
				});
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

		}

		listview_log.setAdapter(new ListFriendAdapter(FragmentFriend.this,
				itens));
		listview_log.setVisibility(View.VISIBLE);

		ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);

	}

}
