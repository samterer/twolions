package br.com.maboo.neext.screens;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ListScreen extends NeextActivity implements InterfaceBar, OnItemClickListener,
		Transaction {

	private String TAG = Constants.LOG_APP;

	protected static final int INSERIR_EDITAR = 1;

	public static ItemNoteDAO dao;

	private List<ItemNote> itens;

	private ListView listview_log;

	private Long id_item;
	private String typeColor = Constants.CREATE_DEFAULT_COLOR;
	
	private MenuDialog customMenuDialog; // menu de cores

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		dao = new ItemNoteDAO(this);

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
		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(this, R.anim.anime_slide_to_right);

		listview_log.setLayoutAnimation(controller);
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

	// delete car
	public void deleteItem() {
		if (id_item != null) {
			excluirItem(id_item);

			// OK
			setResult(RESULT_OK);

			// atualiza a lista na tela
			update();
		}

	}

	// passe o id do item que será excluido
	protected void excluirItem(long id) {
		dao.deletar(id);
	}

	// abre a tela de criação de item com o subject inserido (se houver um)
	public void createItem(View v) {

		// Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, FormItemScreen.class);

		// tipo de tarefa
		int T_KEY = Constants.INSERIR;
		it.putExtra("T_KEY", T_KEY);

		// passa o subject pré inserido pelo usuario
		EditText add_subject = (EditText) findViewById(R.id.add_subject);
		String subj = add_subject.getText().toString();

		it.putExtra("subj", subj);
		
		// passa a color do item definida pelo usuario		
		it.putExtra("color", typeColor);

		// Abre a tela de edição
		startActivityForResult(it, T_KEY);

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
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

		// bt rigt
		final ImageView bt_right = (ImageView) findViewById(R.id.bt_right);
		bt_right.setImageResource(R.drawable.bt_about);

		registerForContextMenu(listview_log);
	}

	public void btBarLeft(View v) {
		// TODO Auto-generated method stub

	}

	public void btBarRight(View v) {

		startActivity(new Intent(this, AboutScreen.class));

		overridePendingTransition(R.anim.scale_in, R.anim.scale_out);

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
	private static final int EDIT = 1;
	private static final int DELETE = 2;
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
		}

		return true;
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
	 * CHANGE COLOR
	 ******************************************************************************/
	
	public void changeToColor(String color) {
		
		Toast.makeText(this, "Change to color [#"+color+"]", Toast.LENGTH_SHORT).show();
		
		// muda cor da tela
		// color of item
		int newcolor = Color
				.parseColor("#" + color);
			
		ImageView bt_color = (ImageView) findViewById(R.id.bt_color); // bt de color do item a ser criado
		bt_color.setBackgroundColor(newcolor);
		bt_color.setTag(color);
		
		typeColor = bt_color.getTag().toString();
		
		// closed menu for select item
		customMenuDialog.dismiss();
		
	}
	
	
	public void btEditColor(View v) {

		if (customMenuDialog == null) { // instancia o menu apenas uma vez

			customMenuDialog = new MenuDialog(this);

		}

		if (!customMenuDialog.isShowing()) {
			customMenuDialog.show();
		}

	}

	private class MenuDialog extends AlertDialog {
		public MenuDialog(Context context) {
			super(context);

			View cus_menu = getLayoutInflater().inflate(R.layout.custom_menu,
					null);

			setView(cus_menu);

		}
		
		// lista de cores
		// 1 - laranja FFA500
		// 2 - azul claro 45c4ff
		// 3 - verde claro 39bf2b
		// 4 - roxo a6a6ed
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			LinearLayout layout_menu = (LinearLayout) findViewById(R.id.layout_menu);
			
			for (int i = 0; i < 4; i++) {
				final ImageView imgV = (ImageView) layout_menu.getChildAt(i);
				imgV.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						changeToColor(imgV.getTag().toString());

					}

				});
				
			}

		}

		/**
		 * Verifica onde foi o clique do usuario, se foi no menu de item (ok),
		 * se não (fecha o menu)
		 */
		public boolean onTouchEvent(MotionEvent event) {

			// I only care if the event is an UP action
			if (event.getAction() == MotionEvent.ACTION_UP) {

				// create a rect for storing the window rect
				Rect r = new Rect(0, 0, 0, 0);

				// retrieve the windows rect
				this.getWindow().getDecorView().getHitRect(r);

				// check if the event position is inside the window rect
				boolean intersects = r.contains((int) event.getX(),
						(int) event.getY());

				// if the event is not inside then we can close the activity
				if (!intersects) {

					// close the activity
					customMenuDialog.dismiss();

					// notify that we consumed this event
					return true;
				}
			}
			// let the system handle the event
			return super.onTouchEvent(event);
		}

	}
	
	

}
