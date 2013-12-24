package br.com.maboo.node.map;

import java.util.ArrayList;

import android.app.Activity;
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

	// private TransacaoTask task;

	private Activity act;

	public GeoPointManager(Activity act) {

		this.act = act;
	}

	// posicao inicial do mapa
	public void initPointManager(final GoogleMap map) {
		this.map = map;

		map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick() {

				Location location = map.getMyLocation();

				map.addMarker(new MarkerOptions().position(
						new LatLng(location.getLatitude(), location
								.getLongitude())).title(
						"It's Me! " + FaceUserVO.user_name));

				LatLng latLng = new LatLng(location.getLatitude(), location
						.getLongitude());

				// lança posicao inicial
				new AnimeCamera(map, latLng);
				return false;
			}
		});

		// task = new TransacaoTask(act, this, R.string.wait);
		// task.execute();

		criaPonto();

	}

	/**
	 * Cria os pontos
	 */
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
