package br.com.twolions.interfaces;

import android.view.View;

public interface InterfaceBar {

	/*
	 * Organiza os botoes que vão aparecer ou não
	 */
	public void organizeBt();

	// botao superior esquerdo da barra
	// cancel
	// help
	// select vehicle
	public void btBarLeft(View v);

	// botao superior direito da barra
	// add car
	// add register
	// save car
	// save register
	public void btBarRight(View v);

}
