package br.com.twolions.core;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import br.com.twolions.R;
import br.com.twolions.screens.SettingsScreen;
import br.com.twolions.sql.SqlScript;
import br.com.twolions.transaction.Transaction;
import br.com.twolions.transaction.TransactionTask;
import br.com.twolions.util.AndroidUtils;
import br.com.twolions.util.Constants;

public class MabooActivity extends ActivityCircle {

	private TransactionTask task;

	public static SqlScript sqlScript;

	/******************************************************************************
	 * ESTADOS
	 ******************************************************************************/

	protected void onDestroy() {
		super.onDestroy();

		if (task != null) {

			boolean executando = task.getStatus().equals(
					AsyncTask.Status.RUNNING);

			if (executando) {

				executando = false;

				task.cancel(true);
				task.closedProgress();

			}
		}

	}

	public void onPause() {
		super.onPause();

		// fecha conexao
		if (sqlScript != null) {

			Log.i(Constants.LOG_BASE, this.getString(R.string.a_f_db));

			sqlScript.fechar();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i(Constants.LOG_BASE, this.getString(R.string.a_c_db));

		// abre base
		sqlScript = new SqlScript(this);

	}

	protected void onActivityResult(final int codigo, final int codigoRetorno,
			final Intent it) {
		super.onActivityResult(codigo, codigoRetorno, it);

		sqlScript = new SqlScript(this);

	}

	/******************************************************************************
	 * SERVICES
	 ******************************************************************************/

	protected void alert(int menssage) {

		AndroidUtils.alertDialog(this, menssage,
				this.getString(R.string.app_name));

	}

	// Inicia a thread
	public void startTransaction(Transaction transaction) {

		boolean dbOk = AndroidUtils.isConnectionDB(this);

		if (dbOk) {

			// abre base
			sqlScript = new SqlScript(this);

			// Inicia a transção
			task = new TransactionTask(this, transaction, R.string.aguarde);
			task.execute();

		} else {

			// Não existe conexão
			AndroidUtils.alertDialog(this,
					R.string.erro_conexao_db_indisponivel, "" + R.string.t_e_c);
		}
	}

	/******************************************************************************
	 * MENU
	 ******************************************************************************/

	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.menu_inf, menu);

		// setMenuBackground();

		return true;
	}

	protected void setMenuBackground() {

		// Log.d(TAG, "Enterting setMenuBackGround");
		getLayoutInflater().setFactory(new Factory() {

			public View onCreateView(String name, Context context,
					AttributeSet attrs) {

				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {

					try { // Ask our inflater to create the view
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);

						/*
						 * The background gets refreshed each time a new item is
						 * added the options menu. So each time Android applies
						 * the default background we need to set our own
						 * background. This is done using a thread giving the
						 * background change as runnable object
						 */
						new Handler().post(new Runnable() {
							public void run() {

								view.setBackgroundResource(R.color.amarelo);
							}
						});
						return view;
					} catch (InflateException e) {
					} catch (ClassNotFoundException e) {
					}
				}
				return null;
			}
		});
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.set) {

			Intent it = new Intent(this, SettingsScreen.class);

			startActivity(it);

			return true;
		}

		if (item.getItemId() == R.id.about) {

			Toast.makeText(this, "Open about screen", Toast.LENGTH_SHORT)
					.show();

			return true;
		}

		return false;
	}

	/******************************************************************************
	 * CLICK\TOUCH
	 ******************************************************************************/

}