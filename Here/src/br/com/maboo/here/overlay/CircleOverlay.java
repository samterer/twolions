package br.com.maboo.here.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Simples Overlay que recebe as coordenadas Latitude e Longitude
 * 
 * e desenha um c�rculo
 * 
 * 
 */
public class CircleOverlay extends Overlay {
	// Constante android.graphics.Color.?
	private int cor;
	private Paint paint = new Paint();
	private GeoPoint geoPoint;

	public CircleOverlay(GeoPoint geoPoint, int cor) {
		this.geoPoint = geoPoint;
		this.cor = cor;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);

		if (geoPoint != null) {
			paint.setColor(cor);

			// Converte as coordenadas para pixels
			Point ponto = mapView.getProjection().toPixels(geoPoint, null);
			canvas.drawCircle(ponto.x, ponto.y, 5, paint);
		}
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		Point ponto = mapView.getProjection().toPixels(this.geoPoint, null);

		// Cria o ret�ngulo
		RectF recf = new RectF(ponto.x - 5, ponto.y - 5, ponto.x + 5,
				ponto.y + 5);

		// Converte para ponto em pixels
		Point newPoint = mapView.getProjection().toPixels(geoPoint, null);

		// Verifica se o ponto est� contido no ret�ngulo
		boolean ok = recf.contains(newPoint.x, newPoint.y);
		if (ok) {
			Toast.makeText(mapView.getContext(),
					"Clicou sobre o Overlay: " + geoPoint, Toast.LENGTH_SHORT)
					.show();
			return true;
		}
		return super.onTap(geoPoint, mapView);
	}

}
