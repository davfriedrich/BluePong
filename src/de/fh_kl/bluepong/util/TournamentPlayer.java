package de.fh_kl.bluepong.util;

import java.util.Random;

public class TournamentPlayer {
	
	TournamentPlayerNode root;
	
	public TournamentPlayer(){
		
	}
	
	public void insert (String player, int value){
		if (root == null){
			root = new TournamentPlayerNode(player, value);
			return;
		}
		TournamentPlayerNode tmp = root;
		while(tmp.getValue() > value){
			tmp = tmp.getNext();
			if(tmp == null){
				
			}
		}
	}

}
