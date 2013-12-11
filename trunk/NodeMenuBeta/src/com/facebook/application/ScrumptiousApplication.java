package com.facebook.application;

import java.util.List;

import android.app.Application;

import com.facebook.friend.BaseListElement;

public class ScrumptiousApplication extends Application {

	private List<BaseListElement> itens;

	public List<BaseListElement> getItens() {
		return itens;
	}

	public void setItens(List<BaseListElement> itens) {
		this.itens = itens;
	}

	

}
