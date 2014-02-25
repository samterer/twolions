package br.com.maboo.node.map;

import java.util.Date;

import android.app.Activity;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import br.com.maboo.node.R;
import br.com.maboo.node.nodechat.CreateNodeChatActivity;
import br.com.maboo.node.nodechat.NodeChatActivity;
import br.livroandroid.utils.AndroidUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * classe que controla os pontos no mapa
 * 
 * @author jeff
 * 
 */
public class MarkerManager {

	private String TAG = "MarkerManager";

	// extend datas
	private GoogleMap map;
	private Activity mActivity;
	private View mView;

	// localizacao do usuario
	private Location loc;

	// animacoes
	Animation barUp;
	Animation barDown;

	// posicao inicial do mapa
	public void initPointManager(final GoogleMap map,
			final FragmentActivity activity, final View view) {

		this.map = map;
		this.mView = view;
		this.mActivity = activity;

		// localização do usuario
		loc = map.getMyLocation();

		// animacoes
		initAnimations();

		// interações com a tela
		interactive(activity);

	}

	/**
	 * carrega animacoes
	 */
	private void initAnimations() {
		barUp = AnimationUtils.loadAnimation(mActivity, R.anim.bar_up);
		barDown = AnimationUtils.loadAnimation(mActivity, R.anim.bar_down);
	}

	/**
	 * veririca interações com a tela
	 */
	private void interactive(final Activity activity) {
		map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick() {
				// lança posicao inicial
				new MoveCamera(map, new LatLng(loc.getLatitude(), loc
						.getLongitude()), 0);

				return false;
			}
		});

		// clique em um node pela primeira vez
		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				AndroidUtils.toast(activity.getApplicationContext(),
						"click here: " + marker.getTitle());

				new MoveCamera(map, new LatLng(marker.getPosition().latitude,
						marker.getPosition().longitude), 0);
				return false;
			}
		});

		// click em um node depois de ver os detalhes dele
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			public void onInfoWindowClick(Marker marker) {

				SecondClickOnMarker mco = new SecondClickOnMarker(activity,
						NodeChatActivity.class);
				// local é o valor da chave, o programador de sempre olhar a
				// Activity que vai
				// receber essa intent, para saber o que a chave "local" vai
				// tratar
				mco.goTo("local", marker.getTitle());

			}

		});

		// long click, permite criar um novo node
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {

				// Convert LatLng to Location for send to GetAddressTask
				Location location = new Location("Test");
				location.setLatitude(point.latitude);
				location.setLongitude(point.longitude);
				location.setTime(new Date().getTime()); // Set time as current
														// Date

				(new GetAddressTask(activity, mView)).execute(location);

			}
		});

		// listener do botao da barra de info
		final ImageView icone = (ImageView) mView.findViewById(R.id.iconeBar);
		icone.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// esconde a info bar
				hideInfoBar();

				// exibe o layout de criação do node
				criaNode(v);

			}
		});

	}

	/*******************************************************************************
	 * hide info bar
	 *******************************************************************************/
	private void hideInfoBar() {
		LinearLayout llayout = (LinearLayout) mView
				.findViewById(R.id.bar_map_info);
		if (llayout.getVisibility() == View.VISIBLE) {
			llayout.startAnimation(barDown);
			llayout.setVisibility(View.INVISIBLE);
		}
	}

	/*******************************************************************************
	 * cria um novo node
	 *******************************************************************************/
	public void criaNode(View v) {
		// exibe a tela de criação do node
		LinearLayout lLayout = (LinearLayout) mView
				.findViewById(R.id.activity_create_chat_node);
		lLayout.startAnimation(barUp);
		lLayout.setVisibility(View.VISIBLE);

		// init classe de criação de node
		new CreateNodeChatActivity().initInstance(map, mActivity, mView);
	}

}
