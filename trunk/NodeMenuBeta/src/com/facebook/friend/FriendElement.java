package com.facebook.friend;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.facebook.model.GraphUser;

public abstract class FriendElement {

	private ImageView icon;
	private String id;
	private String nome;
	private String text;

	public List<GraphUser> selectedUsers;

	public static final String FRIENDS_KEY = "friends";

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

	public FriendElement(ImageView icon, String id, String nome, String text) {
		super();
		this.icon = icon;
		this.id = id;
		this.nome = nome;
		this.text = text;
	}

	public void onActivityResult(Intent data) {
	}

	public void onSaveInstanceState(Bundle bundle) {
	}

	public boolean restoreState(Bundle savedState) {
		return false;
	}

}
