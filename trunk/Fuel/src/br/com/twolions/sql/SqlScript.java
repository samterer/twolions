package br.com.twolions.sql;

import android.content.Context;
import br.com.twolions.util.Constants;

public class SqlScript extends DBConnection {

	// Nome do banco
	private static final String BASE_NAME = Constants.DB_NAME;

	// Controle de versão
	private static final int VERSAO_BANCO = Constants.DB_VERSION;

	// Classe utilitária para abrir, criar, e atualizar o banco de dados
	// private final SQLiteHelper dbHelper;

	private static String[] sqlScript = { "create table", "insert into" };

	// ///////////////
	// TABLE CAR
	// ///////////////
	public static final String TB_CARRO = Constants.TB_CARRO;

	// Script para fazer drop na tabela
	private static final String SCRIPT_DELETE_TB_CAR = "DROP TABLE IF EXISTS "
			+ TB_CARRO;

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_CREATE_TB_CAR = new String[] {
			sqlScript[0]
					+ " "
					+ TB_CARRO
					+ " ( _id integer primary key autoincrement, nome text not null,placa text not null,tipo text not null);",

			sqlScript[1] + " " + TB_CARRO
					+ "(nome,placa,tipo) values('Fiesta','JUH-8266','carro');",
			sqlScript[1] + " " + TB_CARRO
					+ "(nome,placa,tipo) values('Palio','JUH-8266','carro');" };

	// ///////////////
	// TABLE ITEM LOG
	// ///////////////
	public static final String TB_ITEM_LOG = Constants.TB_ITEM_LOG;

	// Script para fazer drop na tabela
	private static final String SCRIPT_DELETE_TB_ITEM_LOG = "DROP TABLE IF EXISTS "
			+ TB_ITEM_LOG;

	// Cria a tabela com o "_id" sequencial
	// type item
	// - fuel - 0
	// - expense - 1
	// - note - 2
	// - repair - 3
	private static final String[] SCRIPT_CREATE_TB_ITEM_LOG = new String[] {
			sqlScript[0]
					+ " "
					+ TB_ITEM_LOG
					+ " ( _id integer primary key autoincrement, id_car integer not null, date text not null, type integer not null, subject text, value_p text, value_u text, odometer text, text text)",

			sqlScript[1]
					+ " "
					+ TB_ITEM_LOG
					+ "(id_car,date,type,value_p,value_u,odometer) values('1','20:30','0','60.00','2.39','81456');",
			sqlScript[1]
					+ " "
					+ TB_ITEM_LOG
					+ "(id_car,date,type,subject,text) values('1','21:30','2','Pagar seguro','Não posso esquecer de pagar a merda do seguro. Valor R$178.');",
			sqlScript[1]
					+ " "
					+ TB_ITEM_LOG
					+ "(id_car,date,type,subject,value_p) values('2','14:30','1','Troca de oleo','25.00');",
			sqlScript[1]
					+ " "
					+ TB_ITEM_LOG
					+ "(id_car,date,type,subject,value_p) values('2','2:30','3','Troca de pneu','677.00');" };

	// Cria o banco de dados com um script SQL
	public SqlScript(final Context ctx) {
		super(ctx, BASE_NAME);

		// create
		final String[] script_tables_create = {
				// create table car and 1 register
				SqlScript.SCRIPT_CREATE_TB_CAR[0],
				SqlScript.SCRIPT_CREATE_TB_CAR[1],
				SqlScript.SCRIPT_CREATE_TB_CAR[2],

				// create fuel car and 1 register
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[0],
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[1],
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[2],
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[3],
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[4] };
		// delete
		final String[] script_tables_delete = {
				// delete table car
				SqlScript.SCRIPT_DELETE_TB_CAR,

				// delete table fuel
				SqlScript.SCRIPT_DELETE_TB_ITEM_LOG };

		// Criar utilizando um script SQL
		dbHelper = new SQLiteHelper(ctx, SqlScript.BASE_NAME,
				SqlScript.VERSAO_BANCO, script_tables_create,
				script_tables_delete);

		// abre o banco no modo escrita para poder alterar também
		db = dbHelper.getWritableDatabase();

	}

	// Fecha o banco
	public void fechar() {

		if (db != null) {
			db.close();
		}

		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
