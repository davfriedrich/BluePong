package de.fh_kl.bluepong.util;

import java.util.Random;

public class TournamentPlayer {
	
	TournamentPlayerNode root;
	TournamentPlayerNode currentPlayer1;
	TournamentPlayerNode currentPlayer2;
	
	public TournamentPlayer(){
		root = null;
		currentPlayer1 = null;
		currentPlayer2 = null;
	}
	
	public void insert (String player, int value){
		if (root == null){
			root = new TournamentPlayerNode(player, value);
			return;
		}
		TournamentPlayerNode tmp = root;
		TournamentPlayerNode lastTmp = null;
		if(tmp.getValue() < value){
			root = new TournamentPlayerNode(player, value);
			tmp.setPrev(root);
			root.setNext(tmp);
			return;
			
		}
		while(tmp.getValue() >= value){
			lastTmp = tmp;
			tmp = tmp.getNext();
			if(tmp == null){
				tmp = new TournamentPlayerNode(player, lastTmp, value);
				lastTmp.setNext(tmp);
				return;
			}
		}
		TournamentPlayerNode newNode = new TournamentPlayerNode(player, lastTmp, tmp, value);
		lastTmp.setNext(newNode);
		tmp.setPrev(newNode);
	}
	
	public String[] getNext(){
		if(currentPlayer1 == null){
			currentPlayer1 = root;
		}else{
			currentPlayer1 = currentPlayer2.getNext();
		}
		currentPlayer2 = currentPlayer1.getNext();
		if(currentPlayer2 == null){
			return new String[]{currentPlayer1.getPlayer(), null};
		}
		return new String[]{currentPlayer1.getPlayer(), currentPlayer2.getPlayer()};
	}

	public void setWinner(int lastWinnerIndex) {
		if(lastWinnerIndex == 1){
			currentPlayer1.setOutOfGame();
		}else if(currentPlayer2 != null){
			currentPlayer2.setOutOfGame();
		}
	}

	public int clean() {
		TournamentPlayerNode tmp = root;
		TournamentPlayerNode tmp2 = null;
		int count = 0;
		while(tmp != null){
			tmp2 = tmp.getNext();
			if(!tmp.getInGame()){
				remove(tmp);
				count++;
			}
			tmp = tmp2;
		}
		currentPlayer1 = null;
		currentPlayer2 = null;
		return count;
	}
	
	private void remove(TournamentPlayerNode tmp){
		if(tmp == root){
			root = tmp.getNext();
			root.setPrev(null);
			tmp.setNext(null);
			return;
		}
		TournamentPlayerNode tmp2 = tmp.getNext();
		if(tmp2  == null){
			tmp2 = tmp.getPrev();
			tmp2.setNext(null);
			tmp.setPrev(null);
			return;
		}
		tmp.getPrev().setNext(tmp2);
		tmp2.setPrev(tmp.getPrev());
		tmp.setPrev(null);
		tmp.setNext(null);
	}

	public String getWinner() {
		return root.getPlayer();
	}

	
	

}
