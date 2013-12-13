package br.livroandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

/**
 * Utils para trabalhar com a orientation
 * 
 * http://developer.motorola.com/docstools/library/Handle_Screen_Rotation_Part_1
 * / http://developer.motorola.com/docstools/library/
 * Handle_Screen_Rotation_Part_2/
 * 
 * @author ricardo.lecheta.ext
 * 
 */
public class OrientacaoUtils {
	// Verifica qual a orientação da tela
	public static int getOrientacao(Context context) {
		int orientacao = context.getResources().getConfiguration().orientation;
		return orientacao;
	}

	// Verifica se a orientação da tela está na vertical
	public static boolean isVertical(Context context) {
		int orientacao = context.getResources().getConfiguration().orientation;
		boolean vertical = orientacao == Configuration.ORIENTATION_PORTRAIT;
		return vertical;
	}

	// Verifica se a orientação da tela está na horizontal
	public static boolean isHorizontal(Context context) {
		int orientacao = context.getResources().getConfiguration().orientation;
		boolean horizontal = orientacao == Configuration.ORIENTATION_LANDSCAPE;
		return horizontal;
	}

	// Solicita para executar a activity na vertical
	public static void setOrientacaoVertical(Activity context) {
		context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	// Solicita para executar a activity na horizontal
	public static void setOrientacaoHorizontal(Activity context) {
		context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
}
