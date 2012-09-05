package br.com.twolions.sql;

import android.content.Context;
import br.com.twolions.dao.ManagerDAO;

public class SqlFuelScript extends ManagerDAO {

	// Nome do banco
	private static final String NOME_BANCO = "bd_itsmycar";

	// Controle de vers�o
	private static final int VERSAO_BANCO = 1;

	// Classe utilit�ria para abrir, criar, e atualizar o banco de dados
	private SQLiteHelper dbHelper;

	// ///////////////
	// TABLE CAR
	// ///////////////
	public static final String TB_CAR = "car";

	// Script para fazer drop na tabela
	private static final String SCRIPT_DELETE_TB_CAR = "DROP TABLE IF EXISTS car";

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_CREATE_TB_CAR = new String[]{
			"create table car ( _id integer primary key autoincrement, nome text not null,placa text not null,tipo text not null);",

			"insert into car(nome,placa,tipo) values('Fiesta','JUH-8266','carro');"};

	// ///////////////
	// TABLE FUEL
	// ///////////////
	public static final String TB_FUEL = "fuel";

	// Script para fazer drop na tabela
	private static final String SCRIPT_DELETE_TB_FUEL = "DROP TABLE IF EXISTS fuel";

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_CREATE_TB_FUEL = new String[]{
			"create table fuel ( _id integer primary key autoincrement, id_car integer primary key not null, date text not null,value_u text not null,value_p text not null,odometer text not null)",

			"insert into fuel(id_car,date,value_u,value_p,odometer) values('0','2012-09-04','2.45','25.56','85040');"};

	// ///////////////
	// TABLE NOTE
	// ///////////////
	public static final String TB_NOTE = "note";

	// Script para fazer drop na tabela
	private static final String SCRIPT_DELETE_TB_NOTE = "DROP TABLE IF EXISTS note";

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_CREATE_TB_NOTE = new String[]{
			"create table fuel ( _id integer primary key autoincrement, id_car integer primary key not null, date text not null,subject text not null,text text not null)",

			"insert into fuel(id_car,date,subject,text) values('0','2012-05-04','primeiro note','Testanto o meu primeiro app');"};

	// ///////////////
	// TABLE EXPENSE
	// ///////////////
	public static final String TB_EXPENSE = "expense";

	// Script para fazer drop na tabela
	private static final String SCRIPT_DELETE_TB_EXPENSE = "DROP TABLE IF EXISTS expense";

	// Cria a tabela com o "_id" sequencial
	private static final String[] SCRIPT_CREATE_TB_EXPENSE = new String[]{
			"create table fuel ( _id integer primary key autoincrement, id_car integer primary key not null, date text not null,subject text not null,value text not null,tipo text not null)",

			"insert into fuel(id_car,date,subject,value,tipo) values('0','2012-09-04','troca de oleo','150','repair');",
			"insert into fuel(id_car,date,subject,value,tipo) values('0','2012-10-14','shop dom pedro','4','expense'),"};

	// Cria o banco de dados com um script SQL
	public SqlFuelScript(Context ctx) {
		super(ctx, NOME_BANCO);

		// create
		String[] script_tables_create = {
				// create table car and 1 register
				SqlFuelScript.SCRIPT_CREATE_TB_CAR[0],
				SqlFuelScript.SCRIPT_CREATE_TB_CAR[1],

				// create fuel car and 1 register
				SqlFuelScript.SCRIPT_CREATE_TB_FUEL[0],
				SqlFuelScript.SCRIPT_CREATE_TB_FUEL[1],

				// create table note and 1 register
				SqlFuelScript.SCRIPT_CREATE_TB_NOTE[0],
				SqlFuelScript.SCRIPT_CREATE_TB_NOTE[1],

				// create table expense and 1 register
				SqlFuelScript.SCRIPT_CREATE_TB_EXPENSE[0],
				SqlFuelScript.SCRIPT_CREATE_TB_EXPENSE[1]};
		// delete
		String[] script_tables_delete = {
				// delete table car
				SqlFuelScript.SCRIPT_DELETE_TB_CAR,

				// delete table fuel
				SqlFuelScript.SCRIPT_DELETE_TB_FUEL,

				// delete table note
				SqlFuelScript.SCRIPT_DELETE_TB_NOTE,

				// delete table expense
				SqlFuelScript.SCRIPT_DELETE_TB_EXPENSE};

		// Criar utilizando um script SQL
		dbHelper = new SQLiteHelper(ctx, SqlFuelScript.NOME_BANCO,
				SqlFuelScript.VERSAO_BANCO, script_tables_create,
				script_tables_delete);

		// abre o banco no modo escrita para poder alterar tamb�m
		db.setDb(dbHelper.getWritableDatabase());
	}
	// Fecha o banco
	public void fechar() {
		super.db.getInstance().close();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
