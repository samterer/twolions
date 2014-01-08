package br.com.maboo.node.map;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import br.com.maboo.node.R;

import com.facebook.scrumptious.auxiliar.FaceUserVO;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * classe que controla os pontos no mapa
 * 
 * @author jeff
 * 
 */
public class GeoPointManager {

	private String TAG = "GeoPointManager";

	private GoogleMap map;


	// posicao inicial do mapa
	public void initPointManager(final GoogleMap map) {
		this.map = map;

		map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick() {

				Location loc = map.getMyLocation();

				// cria marcador do usuario (icone do user)
				map.addMarker(new MarkerOptions().position(
						new LatLng(loc.getLatitude(), loc
								.getLongitude())).title(
						"It's Me! " + FaceUserVO.user_name));

				// lança posicao inicial
				new MoveCamera(map, new LatLng(loc.getLatitude(), loc
						.getLongitude()));
				return false;
			}
		});

		// cria os nodes no mapa (será numa task separada)
		criaPonto();

	}

	/*******************************************************************************
	 * Cria os pontos
	 *******************************************************************************/
	private void criaPonto() {

		// Toast.makeText(act.getApplicationContext(), "criaPonto...",
		// Toast.LENGTH_SHORT).show();

		// recupera a lista de pontos
		ArrayList<GeoPointVO> list = new ListaGeoPoint().getList();

		for (int i = 0; i < list.size(); i++) {

			GeoPointVO g = list.get(i);

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
