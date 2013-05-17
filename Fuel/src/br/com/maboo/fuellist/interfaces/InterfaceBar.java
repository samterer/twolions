package br.com.maboo.fuellist.interfaces;

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
	public void btBarUpLeft(View v);

	// botao superior direito da barra
	// add car
	// add register
	// save car
	// save register
	public void btBarUpRight(View v);

	// botao inferior central da barra
	// open settings
	// open report
	public void btBarDown(View v);

}
