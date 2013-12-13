package br.com.maboo.node.nodemenubeta.adapter;

import java.io.Serializable;
import java.util.List;

import com.facebook.friend.FriendElement;

public class ListFriend implements Serializable {
	private static final long serialVersionUID = -2251881666082662021L;
	public static final String KEY = "itemfriend";
	public List<FriendElement> friends;

	public ListFriend(List<FriendElement> itens) {
		this.friends = itens;
	}
}
