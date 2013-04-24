package br.com.maboo.tubarao.sprite;

import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import br.com.maboo.tubarao.layer.Layer;

public class Sprite extends Layer {

	int velocidade = 4;

	private Rect rectangleCollision = null;

	private int indexImage = 0;

	private int widthCollision = 0;
	private int heightCollision = 0;
	private int xCollision = 0;
	private int yCollision = 0;

	private TimerTask timerTask;

	private Drawable currentDrawable;

	// movimentação do sprite
	private int moveRigthLeft = 0;
	private int moveUpDown = 0;

	// verifica se o obj esta sendo selecionado
	private boolean isTouch = false;
	private boolean selecionado = false;

	// identificador do sprite
	private String nome;

	// identificados em inteiro do sprite
	private int id;

	/**
	 * Construtor
	 * 
	 * @param currentDrawable
	 *            : imagem da animação
	 * 
	 *            Para sprites com apenas um frame.
	 * */
	public Sprite(Drawable drawable) {
		super(drawable);

		this.currentDrawable = drawable;
	}

	/**
	 * Construtor
	 * 
	 * @param view
	 *            : que contém a classe.
	 * @param currentDrawable
	 *            : array com imagens separadas em arquivos diferentes.
	 * 
	 * */

	public Sprite(Drawable drawable, int widthFrame, int heightFrame) {
		super(drawable, widthFrame, heightFrame);

		this.currentDrawable = getImage(0);
	}

	/**
	 * setImage, altera a imagem do sprite mantendo os valores de linhas e
	 * colunas.
	 * 
	 * @param currentDrawable
	 *            : nova imagem.
	 * */
	public void setImage(Drawable drawable) {
		super.setImage(drawable);

		// após alterar imagem, redefine o retangulo de colisão.
		defineCollisionRectangle();
	}

	/**
	 * setFrame
	 * 
	 * @param int
	 * 
	 *        retorna um determinado frame do drawables
	 */
	public void setFrame(int indexImage) {
		if (drawables != null && drawables.size() > 0) {
			setFrame(indexImage);
			setImage(getImage(indexImage));
		}
	}

	/**
	 * nextFrame
	 * 
	 * Exibe a próxima imagem da animação.
	 * */
	public void nextFrame() {

		indexImage = (indexImage + 1 < drawables.size()) ? indexImage = indexImage + 1
				: 0;

		setFrame(indexImage);

		setImage(getImage(indexImage));
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
	 * @param sprite
	 *            : sprite com que vai ser verificado se há colisão.
	 * @param pxInvisible
	 *            : se false, considera todo o retangulo da imagem e true
	 *            desconsidera pxs invisiveis.
	 * */
	public boolean collidesWith(Sprite sprite, boolean pxInvisible) {

		boolean retorno = false;

		// há intersecção
		if (visible
				&& getRectangleCollision().intersect(
						sprite.getRectangleCollision())) {
			// houve colisão com o retangulo
			retorno = true;

			if (pxInvisible) {
				// colisão por px.
				retorno = collidesWithPixel(sprite);
			}
		}
		return retorno;
	}

	/**
	 * collidesWithPixel
	 * 
	 * colisão por pixel, ou colisão perfeita.
	 * 
	 * @param sprite
	 * 
	 *            sprite : com que vai verificar a colisão
	 * */
	private boolean collidesWithPixel(Sprite sprite) {

		Bitmap b1 = ((BitmapDrawable) this.currentDrawable).getBitmap();
		Bitmap b2 = ((BitmapDrawable) sprite.currentDrawable).getBitmap();

		Rect rectIntersect = new Rect();

		rectIntersect.setIntersect(this.getRectangleCollision(),
				sprite.getRectangleCollision());

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
				bX = i - sprite.getX();
				bY = j - sprite.getY();

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
	 * move o sprite para a direita utilizando como limite o valor passado em
	 * limits Caso o valor passado seja menor que 0 o cursor não terá limites
	 * 
	 * @param pixelAmount
	 * @param limits
	 */
	public void moveRight(int pixelAmount, int limits) {

		if (limits < 0) {

			move(pixelAmount, 0);

		} else if (this.moveRigthLeft < limits) {

			move(pixelAmount, 0);

			this.moveRigthLeft += 1;

		}

		// Log.i("Sprite", "REGRA: this.moveRigthLeft < limits");

		// Log.i("Sprite", "this.moveRigthLeft: " + this.moveRigthLeft);
		// Log.i("Sprite", "limits: " + limits);
	}

	/**
	 * move o sprite para a esquerda utilizando como limite o valor passado em
	 * limits Caso o valor passado seja menor que 0 o cursor não terá limites
	 * 
	 * @param pixelAmount
	 * @param limits
	 */
	public void moveLeft(int pixelAmount, int limits) {

		if (limits < 0) {

			move(-pixelAmount, 0);

		} else if (this.moveRigthLeft > limits) {

			move(-pixelAmount, 0);

			this.moveRigthLeft -= 1;

		}

		// Log.i("Sprite", "REGRA: this.moveRigthLeft > limits");

		// Log.i("Sprite", "this.moveRigthLeft: " + this.moveRigthLeft);
		// Log.i("Sprite", "limits: " + limits);
	}

	/**
	 * move o sprite para a cima utilizando como limite o valor passado em
	 * limits Caso o valor passado seja menor que 0 o cursor não terá limites
	 * 
	 * @param pixelAmount
	 * @param limits
	 */

	public void moveUP(int pixelAmount, int limits) {

		if (limits < 0) {

			move(0, -pixelAmount);

		} else if (this.moveUpDown > limits) {

			move(0, -pixelAmount);

			this.moveUpDown -= 1;

		}

		// Log.i("Sprite", "REGRA: this.moveUpDown > limits");

		// Log.i("Sprite", "this.moveUpDown: " + this.moveUpDown);
		// Log.i("Sprite", "limits: " + limits);
	}

	/**
	 * move o sprite para a baixo utilizando como limite o valor passado em
	 * limits Caso o valor passado seja menor que 0 o cursor não terá limites
	 * 
	 * @param pixelAmount
	 * @param limits
	 */
	public void moveDown(int pixelAmount, int limits) {

		if (limits < 0) {

			move(0, pixelAmount);

		} else if (this.moveUpDown < limits) {

			move(0, pixelAmount);

			this.moveUpDown += 1;

		}

		// Log.i("Sprite", "REGRA: this.moveUpDown < limits");

		// Log.i("Sprite", "this.moveUpDown: " + this.moveUpDown);
		// Log.i("Sprite", "limits: " + limits);
	}

	public void setOriginalDrawable(Drawable d) {
		this.currentDrawable = d;
	}

	public Drawable getOriginalDrawable() {
		return this.currentDrawable;
	}

	/**
	 * Retorna true caso o usuario tenha clicado sobre o obj do tipo
	 * Sprite(Drawable)
	 */
	public boolean isTouch(float touchX, float touchY) {
		if (currentDrawable.copyBounds().contains((int) touchX, (int) touchY)) {
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
		return velocidade;
	}

	public void setVelocidade(int velocidade) {
		this.velocidade = velocidade;
	}

}
