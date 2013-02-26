package br.com.maboo.neext.screens;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import br.com.maboo.neext.R;
import br.com.maboo.neext.adapters.ListAdapter;
import br.com.maboo.neext.core.NeextActivity;
import br.com.maboo.neext.dao.ItemNoteDAO;
import br.com.maboo.neext.interfaces.InterfaceBar;
import br.com.maboo.neext.modelobj.ItemNote;
import br.com.maboo.neext.modelobj.ListNote;
import br.com.maboo.neext.transaction.Transaction;
import br.com.maboo.neext.util.Constants;
import br.com.maboo.neext.util.NotificationCreate;
import br.com.maboo.neext.util.NotificationViewer;

public class ListScreen extends NeextActivity implements InterfaceBar, OnItemClickListener,
		Transaction {

	private String TAG = Constants.LOG_APP;

	protected static final int INSERIR_EDITAR = 1;

	public static ItemNoteDAO dao;

	private List<ItemNote> itens;

	private ListView listview_log;

	private Long id_item;
	private String typeColor = Constants.CREATE_DEFAULT_COLOR;
	
	private Context context;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		dao = new ItemNoteDAO(this);
		
		this.context = getApplicationContext();

		montaTela(icicle);

		organizeBt();

	}

	/****************************************************************
	 * ESTADO
	 ****************************************************************/
	@SuppressWarnings("unchecked")
	private void montaTela(Bundle icicle) {

		setContentView(R.layout.list_layout);

		listview_log = (ListView) findViewById(R.id.listview_log);
		listview_log.setAdapter(new ListAdapter(this, itens));
		
		registerForContextMenu(listview_log);

		itens = (List<ItemNote>) getLastNonConfigurationInstance();

		effect(); // effect for opening

		Log.i(TAG, "Lendo estado: getLastNonConfigurationInstance()");

		if (icicle != null) {

			ListNote lista = (ListNote) icicle.getSerializable(ListNote.KEY);

			Log.i(TAG, "Lendo estado: savedInstanceState(carros)");

			this.itens = lista.itens;

		}

		if (itens != null) { // Atualiza o ListView diretamente

			listview_log.setAdapter(new ListAdapter(this, itens));

		} else {

			startTransaction(this);

		}

	}

	public void execute() throws Exception {

		this.itens = getItens();

	}

	public Object onRetainNonConfigurationInstance() {
		Log.i(TAG, "Salvando Estado: onRetainNonConfigurationInstance()");

		return itens;
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Log.i(TAG, "Salvando Estado: onSaveInstanceState(bundle)");

		// Salvar o estado da tela
		outState.putSerializable(ListNote.KEY, new ListNote(itens));
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	protected void onActivityResult(int codigo, int codigoRetorno, Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		// atualiza a lista na tela
		update();

	}

	// atualiza a lista na tela
	public void update() {

		// Pega a lista de carros e exibe na tela
		itens = getItens();

		listview_log.setAdapter(new ListAdapter(this, itens));

		//effect(); // efeito alpha
		
		listview_log.setOnItemClickListener(this);

	}

	/****************************************************************
	 * SERVICES
	 ****************************************************************/

	public void effect() {
		// LayoutAnimationController controller =
		// AnimationUtils.loadLayoutAnimation(this,
		// R.anim.anime_slide_to_right);

		// listview_log.setLayoutAnimation(controller);
	}

	// recupera toda a lista de itens da base
	private List<ItemNote> getItens() {
		List<ItemNote> list = null;

		try {

			list = dao.listarItemNotes();

		} catch (NullPointerException e) {

			e.printStackTrace();

		}

		return list;
	}

	/**
	 * Abre a tela apenas de visualizaçãod o item
	 */
	private void openViewItem() {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, ViewItemScreen.class);

		// id do item
		it.putExtra(ItemNote._ID, id_item);

		// Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
		
		overridePendingTransition(R.anim.scale_in, R.anim.anime_slide_to_left);

	}

	// pede pro usuario a confirmação para deletar realmente o item
	public void deleteConConfirm() {
		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		alerta.setIcon(R.drawable.iconerror);
		alerta.setMessage(R.string.a_dt_item);
		// Método executado se escolher Sim
		alerta.setPositiveButton(R.string.a_y,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						deleteItem();
					}
				});
		// Método executado se escolher Não
		alerta.setNegativeButton(R.string.a_n,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//
					}
				});
		// Exibe o alerta de confirmação
		alerta.show();
	}

	// delete item
	public void deleteItem() {
		if (id_item != null) {
			excluirItem(id_item);

			// OK
			setResult(RESULT_OK);

			// atualiza a lista na tela
			update();
		}

	}
	
	// compartilhar a nota
	private void shareIt(ItemNote itemNote) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		
		sharingIntent.setType("text/plain");
		
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, itemNote.getSubject());
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, itemNote.getText());
		
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	// passe o id do item que será excluido
	protected void excluirItem(long id) {
		dao.deletar(id);
	}


	/****************************************************************
	 * TOUCH
	 ****************************************************************/
	public void onItemClick(final AdapterView<?> parent, View view,
			final int pos, final long id) {

		view.setTag(pos);

		// get the row the clicked button is in
		ItemNote itemNote = itens.get(pos);
		id_item = itemNote.getId();

		// open list item log
		openViewItem();

	}

	public boolean dispatchTouchEvent(MotionEvent event) {
		// View v = getCurrentFocus();

		boolean ret = super.dispatchTouchEvent(event);

		try {

			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	public void organizeBt() {
		
		// insere a imagem no bt
		// bt de criação de item
		ImageView bt = (ImageView) findViewById(R.id.bt_right_down);
		bt.setImageDrawable(getResources().getDrawable(R.drawable.bt_add));
		// align pandding
		bt.setPadding(175, 0, 0, 0);

		listeningGesture();

		registerForContextMenu(listview_log);
	}
	
	// bloqueio do bt de menu (Default do android)
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	 }


	/******************************************************************************
	 * SUB MENU
	 ******************************************************************************/

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.listview_log) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

			ItemNote item = itens.get(info.position);

			menu.setHeaderTitle(item.getSubject());

			String[] menuItems = getResources().getStringArray(R.array.menu);

			for (int i = 0; i < menuItems.length; i++) {
				if(i == 0) { // montao bt check ou uncheck
					if(item.isCheck()) {
						menu.add(Menu.NONE, i, i, "Uncheck");
					} else {
						menu.add(Menu.NONE, i, i, "Check");
					}
				} else {
					menu.add(Menu.NONE, i, i, menuItems[i]);
				}
			}
			
			//Toast.makeText(this, "item ["+item.toString()+"]", Toast.LENGTH_LONG).show();
			
		}

	}
	
	// botões do menu individual dos itens
	private static final int CHECK_OR_UNCHECK = 0;
	private static final int NOTIFYNOW = 1;
	private static final int EDIT = 2;
	private static final int DELETE = 3;
	private static final int SHARE = 4;
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		ItemNote itemNote = itens.get(info.position);
		id_item = itemNote.getId();

		int menuItemIndex = item.getItemId();

		switch (menuItemIndex) {
		case CHECK_OR_UNCHECK:

			checkOrUncheckItem(itemNote);
			
			break;
		case NOTIFYNOW:
			
			notifyNow(itemNote);
			
			break;
		case EDIT:
			
			// Cria a intent para abrir a tela de editar
			Intent it = new Intent(this, FormItemScreen.class);

			// id do itemRequest
			it.putExtra(ItemNote._ID, id_item);

			// type os task
			int T_KEY = Constants.EDITAR;
			it.putExtra("T_KEY", T_KEY);

			// Abre a tela de edição
			startActivityForResult(it, T_KEY);

			overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
			
			break;
		case DELETE:

			deleteConConfirm();
			
			break;
		case SHARE:

			shareIt(itemNote);
			
			break;
		}

		return true;
	}
	
	/******************************************************************************
	 * ALERTS
	 ******************************************************************************/
	private void notifyNow(ItemNote item) {
		StringBuffer text = new StringBuffer(); // corpo do note
		for (int i = 0; i < item.getText().length(); i++) { // sinopse do
																	// corpo da
																	// notificação
			text.append(item.getText().charAt(i));
		}

		// cria notificação
		NotificationCreate nc = new NotificationCreate(getApplicationContext(),
				"Don't forget.", item.getSubject(), text);
		nc.criarNotificacao(NotificationViewer.class);
	}

	/******************************************************************************
	 * CHECKED AND UNCHECKED
	 ******************************************************************************/
	
	private void checkOrUncheckItem(ItemNote item) {	
		
		if(!item.isCheck()) {
		
			item.setCheck(true);	
			
		} else {
			
			item.setCheck(false);
			
		}
		
		if (item != null) { 			// É uma atualização (pra não ter erro)
			item.setId(item.getId());
		}
		
		dao.atualizar(item); // atualiza item
		
		update(); 		// atualiza a lista na tela
		
	}
	
	/******************************************************************************
	 * BUTTONS
	 ******************************************************************************/
	public void btBarUpLeft(View v) {
		// TODO Auto-generated method stub	
	}

	public void btBarUpRight(View v) {
		
		startActivity(new Intent(this, AboutScreen.class));

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
		
	}

	public void btBarDownLeft(View v) {
		// TODO Auto-generated method stub
		
	}

	public void btBarDownRight(View v) {
		
		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// tipo de tarefa
		int T_KEY = Constants.INSERIR;
		it.putExtra("T_KEY", T_KEY);

		// passa a color do item definida pelo usuario		
		it.putExtra("color", typeColor);

		// Abre a tela de edição
		startActivityForResult(it, T_KEY);

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
		
	}
	
	public void btBarDownCenter(View v) {
		// TODO Auto-generated method stub
		
	}

	/******************************************************************************
	 * GESTURE
	 ******************************************************************************/
	private int REL_SWIPE_MIN_DISTANCE;
	private int REL_SWIPE_MAX_OFF_PATH;
	private int REL_SWIPE_THRESHOLD_VELOCITY;

	private static int position = 0;
	//private static View element;
	
	private void listeningGesture() {

		// As paiego pointed out, it's better to use density-aware measurements.
		DisplayMetrics dm = getResources().getDisplayMetrics();
		REL_SWIPE_MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_MAX_OFF_PATH = (int) (250.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_THRESHOLD_VELOCITY = (int) (200.0f * dm.densityDpi / 160.0f + 0.5);

		final GestureDetector gestureDetector = new GestureDetector(
				new MyGestureDetector());

		View.OnTouchListener gestureListener = new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent e) {

				Log.i(TAG, "onTouch > onTouch!");

				position = listview_log.pointToPosition((int) e.getX(),
						(int) e.getY());

				Log.i(TAG, "onTouch > [position: " + position + "]");

				//element = v;

				return gestureDetector.onTouchEvent(e);

			}

		};

		listview_log.setOnTouchListener(gestureListener);

	}
	
	private void onLTRFling() {

	//	Log.i(TAG, "onLTRFling > Left-to-right fling in position[" + position	+ "]");

		try {
			
			 Toast.makeText(this, "Left-to-right fling in position[" +
			 position + "]", Toast.LENGTH_SHORT).show();
			

			ItemNote item = itens.get(position);

			id_item = item.getId();
			

		} catch (Exception e) {
		//	Log.i(TAG, "! element esta null");
			e.printStackTrace();
		}

	}

	private void onRTLFling() {

	//	Log.i(TAG, "onRTLFling > Right-to-left fling in position[" + position + "]");

		try {
			
			// Toast.makeText(this, "Right-to-left fling in position[" +position + "]", Toast.LENGTH_SHORT).show();
			

			ItemNote item = itens.get(position);
			id_item = item.getId();
			
			// get the row the clicked button is in
			//ItemNote itemNote = itens.get(pos);
			//id_item = itemNote.getId();

			// open list item log
			openViewItem();

		} catch (Exception e) {
			e.printStackTrace();
			//Log.i(TAG, "! element esta null");
		}
	}

	class MyGestureDetector extends SimpleOnGestureListener {

		// Detect a single-click and call my own handler.
		public boolean onSingleTapUp(MotionEvent e) {

			// ListView lv = listview_log;
			int pos = listview_log.pointToPosition((int) e.getX(),
					(int) e.getY());

			if (pos < 0) { // as vezes a position na list retornava a mesma
							// posição mas negativo
				pos = pos * (-1);
			}

			return false;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH)
				return false;

			if (e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {

				onRTLFling();

			} else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {

				//onLTRFling();
			}

			return false;
		}

	}



	

}
