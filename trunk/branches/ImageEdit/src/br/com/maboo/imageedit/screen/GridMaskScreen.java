package br.com.maboo.imageedit.screen;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import br.com.maboo.imageedit.R;
import br.com.maboo.imageedit.adapter.GridMaskAdapter;
import br.com.maboo.imageedit.camera.MakePhoto;
import br.com.maboo.imageedit.core.ActivityCircle;
import br.com.maboo.imageedit.core.Transaction;
import br.com.maboo.imageedit.model.Mask;

public class GridMaskScreen extends ActivityCircle implements
		OnItemClickListener, Transaction {

	private List<Mask> itens;
	private GridView gridView;

	private final static String TAG = "appLog";
	private final static int QTD_MASK = 4;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mountScreen(icicle);
	}

	// carrega tela
	public void mountScreen(Bundle icicle) {

		setContentView(R.layout.layout_grid);

		gridView = (GridView) findViewById(R.id.list);

		if (itens != null) { // Atualiza o ListView diretamente
			gridView.setAdapter(new GridMaskAdapter(this, itens));
		} else {
			startTransaction(this);
		}
	}

	public void execute() throws Exception {
		this.itens = getItens();
	}

	// atualiza a lista na tela
	public void update() {
		Log.i(TAG, "update...");

		itens = getItens();

		gridView.setAdapter(new GridMaskAdapter(this, itens));
		gridView.setOnItemClickListener(this);

	}

	/****************************************************************
	 * SERVICES
	 ****************************************************************/

	// carrega imagens
	private List<Mask> getItens() {
		Log.i(TAG, "getItens...");

		List<Mask> list = new ArrayList<Mask>();

		try {
			for (int i = 0; i < QTD_MASK; i++) {
				Mask m = new Mask();

				m.setDrawableId(R.drawable.ic_launcher);
				// Log.i(TAG, "put: " + m.getDrawableId());

				m.setName("img" + i);
				// Log.i(TAG, "img: " + m.getName());

				list.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/****************************************************************
	 * TOUCH
	 ****************************************************************/
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

		v.setTag(pos);

		// go to screen of camera
		Intent intent = new Intent(GridMaskScreen.this, MakePhoto.class);
		intent.putExtra("id", itens.get(pos).getDrawableId());
		startActivity(intent);

		// transition from splash to main menu
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);

	}
}
