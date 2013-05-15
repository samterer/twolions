package br.com.maboo.here.overlay;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import br.com.maboo.here.screen.LongMarketActivity;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ImagesOverlay extends ItemizedOverlay<OverlayItem> {

	// private final List<OverlayItem> overlays;

	private List<MarketOverlayItem> overlays;

	private final Context context;

	private Drawable drawable;

	public ImagesOverlay(Context context, List<MarketOverlayItem> overlays,
			Drawable drawable) {
		super(drawable);

		this.context = context;

		this.overlays = overlays;

		setDrawable(drawable);

		init(context);

	}

	private void init(Context context) {

		configList();

	}

	private void configList() {

		// define imagens list
		boundCenterBottom(getDrawable());

		// init events in list (draw imagens in map)
		populate();

	}

	@Override
	protected OverlayItem createItem(int i) {

		return overlays.get(i);
	}

	@Override
	public int size() {

		return overlays.size();

	}

	/*******************************************
	 * KEY EVENTS
	 *******************************************/
	protected boolean onTap(int i) {
		MarketOverlayItem m = overlays.get(i);

		String text = m.getTitle();

		long lat = m.getMarket().getLatitude();
		long lon = m.getMarket().getLongitude();

		context.startActivity(new Intent(this.context, LongMarketActivity.class));

		// Toast.makeText(context, text + " lat: " + lat + " / lon: " +
		// lon,Toast.LENGTH_SHORT).show();

		return true;

	}

	/*******************************************
	 * GET\SET
	 *******************************************/

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	/*********************************************
	 * VISIBILITY
	 *********************************************/
	private boolean mVisible = true;

	public void setVisible(boolean value) {
		mVisible = value;
	}

	public boolean isVisible() {
		return mVisible;
	}

	@Override
	// desenha overlay na tela
	public void draw(android.graphics.Canvas canvas, MapView mapView,
			boolean shadow) {

		// verifica se o item esta em uma posição dentro da screen atual

		// verifica se o overlay esta como setVisible(true)
		if (mVisible) {
			super.draw(canvas, mapView, shadow);
		}
	}

}
