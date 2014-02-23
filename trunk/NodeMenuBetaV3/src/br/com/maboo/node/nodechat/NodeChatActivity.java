package br.com.maboo.node.nodechat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import br.com.maboo.node.R;

public class NodeChatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_node);

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
