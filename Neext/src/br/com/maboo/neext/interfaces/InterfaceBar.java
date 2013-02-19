package br.com.maboo.neext.interfaces;

import android.view.View;

public interface InterfaceBar {

	/*
	 * Organiza os botoes que vão aparecer ou não
	 */
	public void organizeBt();

	// botao superior esquerdo da barra
	public void btBarUpLeft(View v);

	// botao superior direito da barra
	public void btBarUpRight(View v);
	
	// botao inferior esquerdo da barra
	public void btBarDownLeft(View v);

	// botao inferior central da barra
	public void btBarDownCenter(View v);
	
	// botao inferior direito da barra
	public void btBarDownRight(View v);

}
