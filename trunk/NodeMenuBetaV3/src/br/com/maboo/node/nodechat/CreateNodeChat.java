package br.com.maboo.node.nodechat;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.maboo.node.MainActivity;
import br.com.maboo.node.R;
import br.com.maboo.node.map.AddressStatic;
import br.com.maboo.node.sessao.TelaSessao;
import br.com.maboo.node.util.ImageLoader;

import com.facebook.friend.FriendElement;
import com.google.android.gms.maps.GoogleMap;

public class CreateNodeChat {

	private String TAG = "CreateNodeChat";

	// extend datas
	private Activity mActivity;
	private View mView;

	// amigos convidados para o node
	private FriendElement[] mList;

	public void initInstance(final GoogleMap map, final Activity activity,
			final View view) {

		this.mView = view;
		this.mActivity = activity;

		// inicia as classes principais
		initMethods();

		// muda tela na sessao
		TelaSessao.TELA = TelaSessao.CRIACAO_NODE;
	}

	public void initMethods() {
		// image mView from layout
		ImageView imgView = (ImageView) mView.findViewById(R.id.imgMap);

		// desenha o mapa
		drawMapImage(getImageMap(), R.drawable.loader, imgView);

		// interaçoes com a tela
		// clicks and other things
		interactive();

	}

	/**
	 * veririca interações com a tela
	 */
	private void interactive() {
		// check mark for private node
		final CheckedTextView check = (CheckedTextView) mView
				.findViewById(R.id.checkText);
		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckedTextView) v).isChecked()) {
					check.setChecked(false);
				} else {
					check.setChecked(true);
				}

			}
		});

		// text para inserção de amigos
		final TextView title = (TextView) mView.findViewById(R.id.textCheck);
		title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// verifica se o check private foi selecionado
				if (check.isChecked()) {
					Log.i(TAG, "selecione amigos...");
				}
			}
		});

		// slide for hide screen create chat
		final LinearLayout createChat = (LinearLayout) mView
				.findViewById(R.id.create_node_chat);
		createChat.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					((MainActivity) mActivity).onBackPressed();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * get image of map from url
	 */
	private String getImageMap() {

		String lat = String.valueOf(AddressStatic.address.getLatitude());
		String lon = String.valueOf(AddressStatic.address.getLongitude());
		String zoom = "15";

		// widith screen
		int displayW = 640;
		int displayH = 640;

		String url = "http://maps.googleapis.com/maps/api/staticmap?center="
				+ lat + "," + lon + "&zoom=" + zoom + "&size=" + displayW + "x"
				+ displayH + "&sensor=false&maptype=normal";

		Log.i(TAG, "get image map from url: " + url);

		return url;
	}

	/*
	 * desenha uma imagem do mapa no cabeçalho
	 * 
	 * whenever you want to load an image from url call DisplayImage function
	 * url - image url to load loader - loader image, will be displayed before
	 * getting image image - ImageView
	 */
	private void drawMapImage(String url, int loader, ImageView imgView) {

		// ImageLoader class instance
		ImageLoader imgLoader = new ImageLoader(
				mActivity.getApplicationContext());

		imgLoader.DisplayImage(url, loader, imgView);
	}
}
