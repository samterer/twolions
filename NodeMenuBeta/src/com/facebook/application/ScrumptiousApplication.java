package com.facebook.application;

import java.util.List;

import android.app.Application;

import com.facebook.friend.FriendElement;

public class ScrumptiousApplication extends Application {

	private List<FriendElement> itens;

	public List<FriendElement> getItens() {
		return itens;
	}

	public void setItens(List<FriendElement> itens) {
		this.itens = itens;
	}

	

}
