package com.facebook.friend;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.facebook.model.GraphUser;

public abstract class BaseListElement {

	private Drawable icon;
	private String id;
	private String text1;
	private String text2;

	public List<GraphUser> selectedUsers;

	public static final String FRIENDS_KEY = "friends";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;

	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;

	}

	public Drawable getIcon() {
		return icon;
	}

	public BaseListElement(Drawable icon, String id, String text1, String text2) {
		super();
		this.icon = icon;
		this.id = id;
		this.text1 = text1;
		this.text2 = text2;
	}

	public void onActivityResult(Intent data) {
	}

	public void onSaveInstanceState(Bundle bundle) {
	}

	public boolean restoreState(Bundle savedState) {
		return false;
	}

}
