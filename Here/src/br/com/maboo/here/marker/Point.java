package br.com.maboo.here.marker;

public class Point {

	private int id = 0;

	private String nome = "";

	private int latitude = 0;

	private int longitude = 0;

	/***************************************
	 * GET\SET
	 ***************************************/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

}
