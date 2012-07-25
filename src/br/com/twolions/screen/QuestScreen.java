package br.com.twolions.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.com.twolions.R;
import br.com.twolions.core.ActivityCircle;
import br.com.twolions.quest.ListaQuest;
import br.com.twolions.util.Alert;
import br.com.twolions.util.OnReturnAlertListener;

public class QuestScreen extends ActivityCircle implements
		OnReturnAlertListener {

	private final String TAG = getClassName();

	private ListaQuest lq;

	private Alert alert;

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

		// sorteio do nivel
		Random generator = new Random();
		int nivelDaVez = generator.nextInt(lq.FIGURAS.length);

		// list of quest word
		String[] wordQuests = lq.FIGURAS[nivelDaVez];

		// embaralha ordem das palavras
		ArrayList<String> fileList = new ArrayList<String>(wordQuests.length);

		for (int i = 0; i < wordQuests.length; i++) {

			Log.i(TAG, "Inserindo a palavra (" + wordQuests[i]
					+ ") na fileList");

			fileList.add(i, wordQuests[i]);
		}

		Log.i(TAG, "Embaralhando o fileList");
		Collections.shuffle(fileList);

		// array embaralhado
		String[] listQuests = new String[fileList.size()];

		for (int i = 0; i < fileList.size(); i++) {

			Log.i(TAG, "Inserindo a palavra (" + fileList.get(i)
					+ ") na listQuests");

			listQuests[i] = fileList.get(i);

		}

		// String[] listQuests = Collections.shuffle(lq.FIGURAS[nivel]);

		// settar figuras
		try {
			fig1.setText(listQuests[0]);

			fig2.setText(listQuests[1]);

			fig3.setText(listQuests[2]);

		} catch (ArrayIndexOutOfBoundsException e) {

			e.printStackTrace();

			alert.alertDialogError(this, "NÃO HÀ FILMES",
					"Acabaram as perguntas, por favor, efetue o update amanhã as 8h.");

		}

	}

	// corrige nome do filme
	TextView retorno;

	ProgressBar progressBar;

	public void verifyResponse(View v) {

		// texto do centro
		retorno = (TextView) findViewById(R.id.retorno);

		// progesso central
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		// texto digitado pelo usuario
		responseText = (EditText) findViewById(R.id.responseText);

		String resposta = responseText.getText().toString().toLowerCase()
				.trim();

		String[] aux = lq.RESPOSTA[nivel];

		for (int i = 0; i < aux.length; i++) {

			// resposta não preenchida
			if (resposta.equals("")) {

				// some com a barra de progresso
				progressBar.setVisibility(View.GONE);

				// Toast.makeText(this,
				// "Por favor, responda algo.",Toast.LENGTH_SHORT).show();

				retorno.setText("Por favor, responda algo.");
				retorno.setVisibility(View.VISIBLE);

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						
						// desaparece com a mensagem
						retorno.setText("");
						retorno.setVisibility(View.GONE);

						// volta a exibir a barra de progresso
						progressBar.setVisibility(View.VISIBLE);

					}
				}, 1500);

				continue;
			}

			if (resposta.equals(aux[i])) {

				Toast.makeText(this, "Resposta correta.", Toast.LENGTH_SHORT)
						.show();

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						prepareNextQuest();
					}
				}, 1500);

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
