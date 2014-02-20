package br.com.maboo.node.map;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import br.com.maboo.node.R;
import br.com.maboo.node.chat.ChatActivity;
import br.livroandroid.utils.AndroidUtils;

import com.facebook.scrumptious.auxiliar.FaceUserVO;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * classe que controla os pontos no mapa
 * 
 * @author jeff
 * 
 */
public class MarkerManager {

	private String TAG = "GeoPointManager";

	private GoogleMap map;
	private Activity act;
	private View view;

	// posicao inicial do mapa
	public void initPointManager(final GoogleMap map, final Activity act,
			final View view) {
		this.map = map;
		this.view = view;

		final Location loc = map.getMyLocation();

		// cria marcador do usuario (icone do user)

		// map.addMarker(new MarkerOptions().position(
		// new LatLng(loc.getLatitude(), loc.getLongitude())).title(
		// "It's Me! " + FaceUserVO.user_name));

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

		// cria os nodes no mapa (será numa task separada)
		// criaPonto();

		// icone do usuario
		// criaIconUser(loc);
	}

	private void criaIconUser(Location loc) {

		map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mark))
				.title(FaceUserVO.user_name).snippet("estou...")
				.anchor(0.0f, 1.0f)
				.position(new LatLng(loc.getLatitude(), loc.getLongitude()))
				.flat(true));
	}

	/*******************************************************************************
	 * Cria os pontos
	 *******************************************************************************/
	private void criaPonto() {

		// Toast.makeText(act.getApplicationContext(), "criaPonto...",
		// Toast.LENGTH_SHORT).show();

		// recupera a lista de pontos
		ArrayList<MarkerVO> list = new ListaMarker().getList();

		for (int i = 0; i < list.size(); i++) {

			MarkerVO g = list.get(i);

			Log.i(TAG,
					"criando ponto...: " + g.getDesc() + " | lat,Lon:"
							+ g.getLatLng().latitude + ","
							+ g.getLatLng().longitude);

			map.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_mark))
					.title(g.getDesc()).snippet("teste...").anchor(0.0f, 1.0f)
					.position(g.getLatLng()).flat(true));
		}

	}

	public static int convertToPixels(Context context, int nDP) {
		final float conversionScale = context.getResources()
				.getDisplayMetrics().density;

		return (int) ((nDP * conversionScale) + 0.5f);

	}
}
