package br.com.twolions.screen;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.twolions.R;
import br.com.twolions.core.ActivityCircle;
import br.com.twolions.quest.ListaQuest;
import br.com.twolions.util.Alert;
import br.com.twolions.util.OnReturnAlertListener;

public class QuestScreen extends ActivityCircle implements
		OnReturnAlertListener {

	private ListaQuest lq;

	Alert alert;

	private int nivel;

	// campo de resposta
	EditText responseText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.quest);

		init();

		prepareQuest();

	}

	private void init() {

		nivel = 0;

		alert = new Alert(this);

		lq = new ListaQuest();

	}

	// prepara quest
	private void prepareQuest() {

		TextView fig1 = (TextView) findViewById(R.id.figura1);
		TextView fig2 = (TextView) findViewById(R.id.figura2);
		TextView fig3 = (TextView) findViewById(R.id.figura3);

		// settar figuras
		try {
			fig1.setText(lq.FIGURAS[nivel][0]);

			fig2.setText(lq.FIGURAS[nivel][1]);

			fig3.setText(lq.FIGURAS[nivel][2]);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();

			alert.alertDialogError(this, "NÃO HÀ FILMES",
					"Acabaram as perguntas, por favor, efetue o update amanhã as 8h.");

		}

	}

	// corrige nome do filme
	public void verifyResponse(View v) {

		responseText = (EditText) findViewById(R.id.responseText);

		String resposta = responseText.getText().toString().toLowerCase()
				.trim();

		for (int i = 0; i < lq.RESPOSTA[nivel].length; i++) {

			if (resposta.equals(lq.RESPOSTA[nivel][i])) {

				Toast.makeText(this, "Resposta correta.", Toast.LENGTH_SHORT)
						.show();

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						prepareNextQuest();
					}
				}, 1000);

				break;
			} else {

				Log.i("appLog", "A resposta " + resposta + " não esta correta.");

				responseText.setText("");

			}

		}

	}

	// prepara proxima pergunta
	private void prepareNextQuest() {

		// incrementa nivel
		nivel++;

		prepareQuest();

		responseText.setText("");

	}

	// notificao (quando acabaram as perguntas)
	public void onReturn() {

		QuestScreen.this.finish();

	}

}
