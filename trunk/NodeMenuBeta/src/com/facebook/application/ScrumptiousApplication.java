package com.facebook.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.facebook.friend.BaseListElement;
import com.facebook.model.GraphUser;

public class ScrumptiousApplication extends Application{

	private List<GraphUser> selectedUsers;
	
	private ArrayList<BaseListElement> itens;
	
	public List<GraphUser> getSelectedUsers() {
	    return selectedUsers;
	}

	public void setSelectedUsers(List<GraphUser> users) {
	    selectedUsers = users;
	}

	public ArrayList<BaseListElement> getItens() {
		return itens;
	}
	
	
}
