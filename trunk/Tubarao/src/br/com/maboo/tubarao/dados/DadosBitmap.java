package br.com.maboo.tubarao.dados;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import br.com.maboo.tubarao.R;

public class DadosBitmap {

	private Resources res;

	public DadosBitmap(Resources res) {
		this.res = res;
	}

	/**
	 * Lista de sprites do tubarao normal
	 * 
	 * @return
	 */
	public Bitmap[] getImgTubNormal() {

		Bitmap[] bs_tubarao = new Bitmap[] {
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.tub125x115_right_teste)),
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.tub125x115_left)) };
		return bs_tubarao;
	}
	
/*	public Bitmap[] getMoveRight() {
		Bitmap[] bs_tubarao = new Bitmap[] {
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.tub125x115_right_1)),
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.tub125x115_right_2),
						BitmapFactory.decodeStream(res
								.openRawResource(R.drawable.tub125x115_right_3),
								BitmapFactory.decodeStream(res
										.openRawResource(R.drawable.tub125x115_right_4)) };
		return bs_tubarao;
	}
	
	public Bitmap[] getMoveLeft() {

		Bitmap[] bs_tubarao = new Bitmap[] {
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.tub125x115_left_1)),
				BitmapFactory.decodeStream(res
						.openRawResource(R.drawable.tub125x115_left_2),
						BitmapFactory.decodeStream(res
								.openRawResource(R.drawable.tub125x115_left_3),
								BitmapFactory.decodeStream(res
										.openRawResource(R.drawable.tub125x115_left_4)) };
		return bs_tubarao;
	}*/

	/**
	 * Lista de sprites dos objetos
	 * 
	 * @return
	 */
	public Bitmap getImgObjetos(int indice) {

		switch (indice) {
		case PNEU:
			return BitmapFactory.decodeStream(res
					.openRawResource(R.drawable.obj_pneu46x42));

		case GARRAFA:
			return BitmapFactory.decodeStream(res
					.openRawResource(R.drawable.obj_garrafa16x42));

		case LATA:
			return BitmapFactory.decodeStream(res
					.openRawResource(R.drawable.obj_lata42x38));

		case BARRIL:
			return BitmapFactory.decodeStream(res
					.openRawResource(R.drawable.obj_lata42x38));

		}
		// no caso de erro, retorna a imagem da lata
		return BitmapFactory.decodeStream(res
				.openRawResource(R.drawable.obj_lata42x38));
	}

	/**
	 * 0: pneu | 1: garrafa | 2: lata | 3: barril
	 */
	private final static int PNEU = 0;
	private final static int GARRAFA = 1;
	private final static int LATA = 2;
	private final static int BARRIL = 3;

	public static final int[][] TIPO_OBJETOS_POR_NIVEL = {
			{ GARRAFA, LATA },
			{ GARRAFA, LATA, PNEU, BARRIL, LATA },
			{ GARRAFA, LATA, PNEU, BARRIL },
			};

	/**
	 * O peso dos objetos influencia na queda
	 * 
	 * 0: pneu | 1: garrafa | 2: lata | 3: barril
	 * 
	 */
	public static final int[] PESO_OBJETOS = { 2, 1, 1, 2 };

	/**
	 * Pontuação que cada objeto pode fornecer
	 * 
	 * 
	 * 0: pneu | 1: garrafa | 2: lata | 3: barril
	 * 
	 */
	public static final int[] PONTUACAO_OBJETOS = { 2, 1, 1, 3 };

}
