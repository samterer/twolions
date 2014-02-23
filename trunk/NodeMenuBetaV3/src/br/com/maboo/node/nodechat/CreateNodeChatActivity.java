package br.com.maboo.node.nodechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.maboo.node.R;
import br.com.maboo.node.map.AddressStatic;
import br.com.maboo.node.util.ImageLoader;

import com.facebook.friend.FriendElement;
import com.google.android.gms.maps.GoogleMap;

public class CreateNodeChatActivity {

	private String TAG = "CreateNodeChatActivity";

	// extend datas
	private GoogleMap map;
	private Activity mActivity;
	private View view;

	// tipo da interação
	private int TIPO;

	// amigos convidados para o ndoe
	private FriendElement[] mList;

	public void initInstance(final GoogleMap map, final Activity activity,
			final View view) {
		this.map = map;
		this.view = view;
		this.mActivity = activity;

		// inicia as classes principais
		initMethods();
	}

	public void initMethods() {
		// esconde a actionBar
		// getActionBar().hide();

		// seta o layout
		// setContentView(R.layout.activity_create_chat_node);

		// receive a extras
		init();

		// image view from layout
		ImageView imgView = (ImageView) view.findViewById(R.id.imgMap);

		// desenha o mapa
		drawMapImage(getImageMap(), R.drawable.loader, imgView);

		// interaçoes com a tela
		// clicks and other things
		interactive();

	}

	/**
	 * verifica atividade
	 */
	private void init() {
		Intent in = mActivity.getIntent();
		if (in != null) {
			Bundle b = in.getExtras();
			if (b != null) {
				TIPO = (int) b.getInt("tipo");
			}
		}
	}

	/**
	 * veririca interações com a tela
	 */
	private void interactive() {
		// check mark for private node
		final CheckedTextView check = (CheckedTextView) view
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
		final TextView title = (TextView) view.findViewById(R.id.textCheck);
		title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// verifica se o check private foi selecionado
				if (check.isChecked()) {
					Log.i(TAG, "selecione amigos...");
				}

			}
		});
	}

	/**
	 * get image of map from url
	 */
	private String getImageMap() {

		String lat = String.valueOf(AddressStatic.address.getLatitude());
		String lon = String.valueOf(AddressStatic.address.getLongitude());
		String zoom = "17";

		// widith screen
		int displayW = 900;
		int displayH = 340;

		String url = "http://maps.googleapis.com/maps/api/staticmap?center="
				+ lat + "," + lon + "&zoom=" + zoom + "&size=" + displayW + "x"
				+ displayH + "&sensor=false&maptype=normal";

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
