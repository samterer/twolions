package br.com.maboo.node.chat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import br.com.maboo.node.R;

public class ChatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		// Para recuperar os dados do Bundle em outra Activity
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String datas = extras.getString("local");
			if (datas != null) { // se tiver dados no pacote
				TextView text = (TextView) findViewById(R.id.text);
				text.setText("Vamos falar de " + datas.toString());
			}
		}

	}

}
