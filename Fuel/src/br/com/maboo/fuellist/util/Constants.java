package br.com.maboo.fuellist.util;

public class Constants {

	// authority
	public static final String AUTHORITY = "br.com.maboo.fuellist";

	// version of current data base
	public static final int DB_VERSION = 10;

	// db
	public static final String DB_NAME = "bd_fuellist";

	// tables
	public static final String TB_CARRO = "Carro";

	public static final String TB_ITEM_LOG = "ItemLog";

	// db for scripts
	public static final String DEFAULT_SORT_ORDER = "_id ASC";

	// log i
	public static final String LOG_APP = "appLog";

	public static final String LOG_BASE = "base";

	// types of itemLog
	public static final int FUEL = 0;
	public static final int EXPENSE = 1;
	public static final int NOTE = 2;
	public static final int REPAIR = 3;
}
