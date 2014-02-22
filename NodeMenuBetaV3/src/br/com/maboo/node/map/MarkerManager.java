package br.com.maboo.node.map;

import java.util.Date;

import android.app.Activity;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import br.com.maboo.node.R;
import br.com.maboo.node.chat.ChatActivity;
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

	private GoogleMap map;
	private Activity act;
	private View view;

	private Location loc;

	// posicao inicial do mapa
	public void initPointManager(final GoogleMap map,
			final FragmentActivity act, final View view) {
		this.map = map;
		this.view = view;

		// localização do usuario
		loc = map.getMyLocation();

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
				AndroidUtils.toast(act.getApplicationContext(), "click here: "
						+ marker.getTitle());

				new MoveCamera(map, new LatLng(marker.getPosition().latitude,
						marker.getPosition().longitude), 0);
				return false;
			}
		});

		// click em um node depois de ver os detalhes dele
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			public void onInfoWindowClick(Marker marker) {

				SecondClickOnMarker mco = new SecondClickOnMarker(act,
						ChatActivity.class);
				// local é o valor da chave, semprer olha a Activity que vai
				// receber essa intent, para saber o que a chave "local" vai
				// tratar
				mco.goTo("local", marker.getTitle());

			}

		});

		// long click, permite criar um novo node
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {

				// Convert LatLng to Location
				Location location = new Location("Test");
				location.setLatitude(point.latitude);
				location.setLongitude(point.longitude);
				location.setTime(new Date().getTime()); // Set time as current
														// Date

				(new GetAddressTask(act, view)).execute(location);

			}
		});

		// listener dos bts da barra da informação
		ImageView btPrivate = (ImageView) view.findViewById(R.id.btPrivate);
		btPrivate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				criaNodePrivado(v);
			}
		});
		ImageView btPublic = (ImageView) view.findViewById(R.id.btPublic);
		btPublic.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				criaNodePublico(v);
			}
		});
	}

	/*******************************************************************************
	 * seleciona um dos chats
	 *******************************************************************************/
	public void criaNodePublico(View v) {
		Log.i(TAG, "cria node publico...");
	}

	public void criaNodePrivado(View v) {
		Log.i(TAG, "cria node privado...");
	}
}
