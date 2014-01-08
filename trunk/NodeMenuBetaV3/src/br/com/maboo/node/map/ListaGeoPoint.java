package br.com.maboo.node.map;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class ListaGeoPoint {

	private ArrayList<GeoPointVO> list;

	public ListaGeoPoint() {
		recuperaLista();
	}

	/*******************************************************************************
	 * recupera a lista de nodes
	 *******************************************************************************/
	private void recuperaLista() {

		// list de edereços
		ArrayList<LatLng> temp_list = new ArrayList<LatLng>();
		temp_list.add(new LatLng(-22.9063648, -47.0615741)); // Campinas
		temp_list.add(new LatLng(41.889, -87.622)); // Chicago
		temp_list.add(new LatLng(45.5454470, -73.6390760)); // Canada
		temp_list.add(new LatLng(61.5240100, 105.3187560)); // Russia
		temp_list.add(new LatLng(-23.5489433, -46.6388182)); // São Paulo

		// lista de descrições
		String[] descs = { "Campinas", "Chicago", "Canada", "Russia",
				"São Paulo" };

		// cria lista de pontos baseada na lista recuperada
		list = new ArrayList<GeoPointVO>();
		for (int j = 0; j < temp_list.size(); j++) {
			GeoPointVO g = new GeoPointVO(temp_list.get(j), j, descs[j]);
			list.add(g);
		}

	}

	public ArrayList<GeoPointVO> getList() {
		return list;
	}

}
