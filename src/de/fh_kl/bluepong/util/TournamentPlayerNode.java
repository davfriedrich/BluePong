package de.fh_kl.bluepong.util;

public class TournamentPlayerNode {
	
	String player;
	TournamentPlayerNode prev;
	TournamentPlayerNode next;
	int value;
	boolean inGame;
	
	public TournamentPlayerNode(String player, TournamentPlayerNode prev, TournamentPlayerNode next, int value){
		this.player = player;		
		this.prev = prev;
		this.next = next;
		this.value = value;
		inGame = true;
	}
	
	public TournamentPlayerNode(String player, TournamentPlayerNode prev, int value){
		this.player = player;		
		this.prev = prev;
		this.next = null;
		this.value = value;
		inGame = true;
	}
	
	public TournamentPlayerNode(String player, int value){
		this.player = player;		
		this.prev = null;
		this.next = null;
		this.value = value;
		inGame = true;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public int getValue(){
		return value;
	}

	public TournamentPlayerNode getPrev() {
		return prev;
	}

	public void setPrev(TournamentPlayerNode prev) {
		this.prev = prev;
	}

	public TournamentPlayerNode getNext() {
		return next;
	}

	public void setNext(TournamentPlayerNode next) {
		this.next = next;
	}
	
	public void setOutOfGame(){
		inGame = false;
	}
	
	public boolean getInGame(){
		return inGame;
	}
	

}
