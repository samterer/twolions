package br.com.maboo.neext.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import br.com.maboo.neext.R;

/**
 * Exemplo de Activity que cria uma notifica��o
 * 
 * @author ricardo
 * 
 */
public class NotificationCreate {

	//private NotificationManager nm;
	private Context context;
	
	private CharSequence tickerText;
	private CharSequence titulo;
	private CharSequence mensagem; 
	
	public NotificationCreate(final Context context,
			final CharSequence tickerText, final CharSequence titulo,
			final CharSequence mensagem) {

		// Texto com a chamada para a notifica��o (barra de status)
		this.tickerText = tickerText;

		// Detalhes da mensagem, quem enviou e o texto
		this.titulo = titulo;
		this.mensagem = mensagem;

		this.context = context;

	}

	// Exibe a notifica��o
	public void criarNotificacao(Class<?> activity) {

		// Recupera o servi�o do NotificationManager
		NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		
		Notification n = new Notification(R.drawable.ic_launcher, tickerText, System.currentTimeMillis());

		// PendingIntent para executar a Activity se o usu�rio selecionar a
		// notifica��o
		PendingIntent p = PendingIntent.getActivity(context, 0, new Intent(context, activity), 0);

		// Informa��es
		n.setLatestEventInfo(context, titulo, mensagem, p);

		// Precisa de permiss�o: <uses-permission
		// android:name="android.permission.VIBRATE" />
		// espera 100ms e vibra por 250ms, depois espera por 100 ms e vibra por
		// 500ms.
		n.vibrate = new long[] { 100, 250, 100, 500 };

		// id (numero �nico) que identifica esta notifica��o
		nm.notify(ConstantsNotify.NOTIFICATION_ID, n);
	}
}
