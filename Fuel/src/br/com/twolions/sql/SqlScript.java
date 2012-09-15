package br.com.twolions.sql;

import android.content.Context;
import br.com.twolions.dao.CarroDAO;

public class SqlScript extends CarroDAO {

	// Nome do banco
	private static final String NOME_BANCO = "bd_itsmycar";

	// Controle de versão
	private static final int VERSAO_BANCO = 8;

	// Classe utilitária para abrir, criar, e atualizar o banco de dados
	private SQLiteHelper dbHelper;

	private static String[] sqlScript = {"create table", "insert into"};

	// ///////////////
	// TABLE CAR
	// ///////////////
	public static final String TB_CARRO = "Carro";

	// Script para fazer drop na tabela
	private static final String SCRIPT_DELETE_TB_CAR = "DROP TABLE IF EXISTS "
			+ TB_CARRO;

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_CREATE_TB_CAR = new String[]{
			sqlScript[0]
					+ " "
					+ TB_CARRO
					+ " ( _id integer primary key autoincrement, nome text not null,placa text not null,tipo text not null);",

			sqlScript[1] + " " + TB_CARRO
					+ "(nome,placa,tipo) values('Fiesta','JUH-8266','carro');"};

	// ///////////////
	// TABLE ITEM LOG
	// ///////////////
	public static final String TB_ITEM_LOG = "ItemLog";

	// Script para fazer drop na tabela
	private static final String SCRIPT_DELETE_TB_ITEM_LOG = "DROP TABLE IF EXISTS "
			+ TB_ITEM_LOG;

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_CREATE_TB_ITEM_LOG = new String[]{
			sqlScript[0]
					+ " "
					+ TB_ITEM_LOG
					+ " ( "
					+ "_id integer primary key autoincrement, id_car integer not null, "
					+ "date text not null,                     type integer not null,"
					+ "subject text not null,                  value_p text not null,"
					+ "value_u text not null,                  odometer text not null,"
					+ "text text not null)",

			sqlScript[1]
					+ " "
					+ TB_ITEM_LOG
					+ "(id_car,date,type,subject,value_p,value_u,odometer,text) values('1','2012-09-04','0','','60.00','2.39','81456','');"};

	// Cria o banco de dados com um script SQL
	public SqlScript(Context ctx) {
		super(ctx);

		// create
		String[] script_tables_create = {
				// create table car and 1 register
				SqlScript.SCRIPT_CREATE_TB_CAR[0],
				SqlScript.SCRIPT_CREATE_TB_CAR[1],

				// create fuel car and 1 register
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[0],
				SqlScript.SCRIPT_CREATE_TB_ITEM_LOG[1]};
		// delete
		String[] script_tables_delete = {
				// delete table car
				SqlScript.SCRIPT_DELETE_TB_CAR,

				// delete table fuel
				SqlScript.SCRIPT_DELETE_TB_ITEM_LOG};

		// Criar utilizando um script SQL
		dbHelper = new SQLiteHelper(ctx, SqlScript.NOME_BANCO,
				SqlScript.VERSAO_BANCO, script_tables_create,
				script_tables_delete);

		// abre o banco no modo escrita para poder alterar também
		db = dbHelper.getWritableDatabase();
	}
	// Fecha o banco
	public void fechar() {
		super.fechar();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
