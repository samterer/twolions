package br.com.maboo.neext.util;

import br.com.maboo.neext.R;

public class Constants {

	// authority
	public static final String AUTHORITY = "br.com.maboo.neext";

	// version of current data base
	public static final int DB_VERSION = 18; // se mudar a versão aqui ele
												// reseta o banco

	// db
	public static final String DB_NAME = "bd_neext";

	public static final String TB_ITEM_LOG = "ItemNote";

	// db for scripts
	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	// log i
	public static final String LOG_APP = "appLog";

	public static final String LOG_BASE = "base";

	// chaves do request
	public static final int INSERIR = 1;
	public static final int EDITAR = 2;
	public static final int VIEW = 3;

	// cor padrao do item, quando criado pelo botão de create na ListScreen
	public static final String CREATE_DEFAULT_COLOR = "9a6db0";

	// color default para texto
	public static int defaultColor = R.color.cinza_dark;

}
