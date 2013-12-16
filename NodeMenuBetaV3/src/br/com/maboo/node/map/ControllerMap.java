package br.com.maboo.node.map;

import com.google.android.gms.maps.GoogleMap;

/** 
 * class que controla os controles
 * @author jeff
 *
 */
public class ControllerMap {

	public ControllerMap(GoogleMap map) {
		map.setMyLocationEnabled(true);
	}

}
