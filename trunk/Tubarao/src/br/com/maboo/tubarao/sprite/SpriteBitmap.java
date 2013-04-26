package br.com.maboo.tubarao.sprite;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import br.com.maboo.tubarao.layer.LayerBitmap;

public class SpriteBitmap extends LayerBitmap {

	protected Bitmap[] arrayBitmaps = null;

	int peso = 0;

	private Rect rectangleCollision = null;

	private int widthCollision = 0;
	private int heightCollision = 0;
	private int xCollision = 0;
	private int yCollision = 0;

	private int indexImage = 0;

	// verifica se o obj esta sendo selecionado
	private boolean isTouch = false;
	private boolean selecionado = false;

	// identificador do sprite
	private String nome;

	// identificados em inteiro do sprite
	private int id;

	/**
	 * nextFrame
	 * 
	 * Exibe a próxima imagem da animação.
	 * */
	public void nextImage() {

		indexImage = (indexImage + 1 < arrayBitmaps.length) ? indexImage = indexImage + 1
				: 0;

		setImage(arrayBitmaps[indexImage]);
	}

	/**
	 * setImage, altera a imagem do sprite mantendo os valores de linhas e
	 * colunas.
	 * 
	 * @param bitmap
	 *            : nova imagem.
	 * */
	public void setImage(Bitmap bitmap) {
		super.setImage(bitmap);

		// após alterar imagem, redefine o retangulo de colisão.
		defineCollisionRectangle();
	}

	/**
	 * Define o retangulo de colisão.
	 * 
	 * Valor definido dentro dos valores da imagem.
	 * 
	 * @param xCollision
	 * @param yCollision
	 * @param widthCollision
	 * @param heightCollision
	 * 
	 *            Para voltar ao valor defalult de colisão basta passar '0' em
	 *            todos os paramentros.
	 * 
	 * */
	public void defineCollisionRectangle(int xCollision, int yCollision,
			int widthCollision, int heightCollision) {

		this.xCollision = xCollision;
		this.yCollision = yCollision;
		this.widthCollision = widthCollision;
		this.heightCollision = heightCollision;

		defineCollisionRectangle();
	}

	/**
	 * Atualiza o valor do retangulo de colisão
	 * */
	private void defineCollisionRectangle() {

		rectangleCollision = new Rect(x + xCollision, y + yCollision,
				(x + width) - widthCollision, (y + height) - heightCollision);
	}

	/**
	 * collidesWith
	 * 
	 * @param spriteBitmap
	 *            : sprite com que vai ser verificado se há colisão.
	 * @param pxInvisible
	 *            : se false, considera todo o retangulo da imagem e true
	 *            desconsidera pxs invisiveis.
	 * */
	public boolean collidesWith(SpriteBitmap spriteBitmap, boolean pxInvisible) {

		boolean retorno = false;

		// há intersecção
		if (visible
				&& getRectangleCollision().intersect(
						spriteBitmap.getRectangleCollision())) {
			// houve colisão com o retangulo
			retorno = true;

			if (pxInvisible) {
				// colisão por px.
				retorno = collidesWithPixel(spriteBitmap);
			}
		}
		return retorno;
	}

	/**
	 * collidesWithPixel
	 * 
	 * colisão por pixel, ou colisão perfeita.
	 * 
	 * @param spriteBitmap
	 * 
	 *            sprite : com que vai verificar a colisão
	 * */
	private boolean collidesWithPixel(SpriteBitmap spriteBitmap) {

		Bitmap b1 = this.originalBitmap;
		Bitmap b2 = spriteBitmap.originalBitmap;

		Rect rectIntersect = new Rect();

		rectIntersect.setIntersect(this.getRectangleCollision(),
				spriteBitmap.getRectangleCollision());

		int left = rectIntersect.left;
		int top = rectIntersect.top;
		int right = rectIntersect.right;
		int bottom = rectIntersect.bottom;

		int aX, aY, bX, bY;

		for (int i = left; i < right; i++) {
			for (int j = top; j < bottom; j++) {

				// obtém o x e y da da intersecção na imagem A
				aX = i - this.x;
				aY = j - this.y;
				// obtém o x e y da da intersecção na imagem B
				bX = i - spriteBitmap.getX();
				bY = j - spriteBitmap.getY();

				// descobrir o motivo de que em alguns casos os indices de x e y
				// estão fora da imagens.
				try {
					int corA = b1.getPixel(aX, aY);
					int corB = b2.getPixel(bX, bY);

					if (corA > 0 && corB > 0) {
						return true;
					}
				} catch (Exception e) {
					break;
				}
			}
		}
		return false;
	}

	/**
	 * @return the rectangleCollision
	 */
	public Rect getRectangleCollision() {

		// antes de obter o retangulo de colisão é atualizado o valor.
		defineCollisionRectangle();

		return rectangleCollision;
	}

	/**
	 * @param indiceImage
	 *            the currentImage to set
	 */
	public void setImage(int indiceImage) {
		this.indexImage = indiceImage;
		setImage(arrayBitmaps[indiceImage]);
	}

	/**
	 * @return the currentImage
	 */
	public int getIndexImage() {
		return indexImage;
	}

	/**
	 * @return the arrayBitmaps
	 */
	public Bitmap[] getArrayBitmaps() {
		return arrayBitmaps;
	}

	/**
	 * @param arrayBitmaps
	 *            the arrayBitmaps to set
	 */
	public void setArrayBitmaps(Bitmap[] arrayBitmaps) {
		this.arrayBitmaps = arrayBitmaps;

		indexImage = 0;

		// atualiza imagem que está sendo visualizada
		setImage(arrayBitmaps[0]);
	}

	public void setOriginalBitmap(Bitmap d) {
		this.originalBitmap = d;
	}

	public Bitmap getOriginalBitmap() {
		return this.originalBitmap;
	}

	/**
	 * Retorna true caso o usuario tenha clicado sobre o obj do tipo
	 * SpriteBitmap(Bitmap)
	 */
	public boolean isTouch(float touchX, float touchY) {
		Drawable drawable = new BitmapDrawable(originalBitmap);

		if (drawable.copyBounds().contains((int) touchX, (int) touchY)) {
			return true;
		} else {
			return false;
		}
	}

	public void setTouch(boolean isTouch) {
		this.isTouch = isTouch;
	}

	public boolean isTouch() {
		return isTouch;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVelocidade() {
		return peso;
	}

	public void setVelocidade(int velocidade) {
		this.peso = velocidade;
	}

}
