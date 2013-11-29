package com.facebook.application;

import java.util.List;

import android.app.Application;

import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;

public class ScrumptiousApplication extends Application {

	private List<GraphUser> selectedUsers;

	private GraphPlace selectedPlace;

	public List<GraphUser> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<GraphUser> users) {
		selectedUsers = users;
	}

	public GraphPlace getSelectedPlace() {
		return selectedPlace;
	}

	public void setSelectedPlace(GraphPlace place) {
		this.selectedPlace = place;
	}
}
