package com.facebook.friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public abstract class FriendElement {

	private ImageView icon;
	private String id;
	private String nome;
	private String text;

	public static final String FRIENDS_KEY = "friends";

	private String urlPic;

	public String getUrlPic() {
		return urlPic;
	}

	public void setUrlPic(String urlPic) {
		this.urlPic = urlPic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;

	}

	public ImageView getIcon() {
		return icon;
	}

	public void setIcon(ImageView icon) {
		this.icon = icon;
	}

	public FriendElement(ImageView icon, String id, String nome, String text, String urlPic) {
		super();
		this.icon = icon;
		this.id = id;
		this.nome = nome;
		this.text = text;
		this.urlPic = urlPic;
	}

	public void onActivityResult(Intent data) {
	}

	public void onSaveInstanceState(Bundle bundle) {
	}

	public boolean restoreState(Bundle savedState) {
		return false;
	}

}
