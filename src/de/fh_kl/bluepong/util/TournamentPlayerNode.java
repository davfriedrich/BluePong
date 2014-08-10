package de.fh_kl.bluepong.util;

/**
 * listnode of {@link de.fh_kl.bluepong.util.TournamentPlayer}
 */
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
        this(player, prev, null, value);
	}
	
	public TournamentPlayerNode(String player, int value){
		this(player, null, null, value);
	}

    /**
     * returns the name of this node/player
     * @return the name of this node/player
     */
	public String getPlayer() {
		return player;
	}

    /**
     * returns the value of this node/player
     * @return the value of this node/player
     */
	public int getValue(){
		return value;
	}

    /**
     * returns previous node
     * @return previous node
     */
	public TournamentPlayerNode getPrev() {
		return prev;
	}

    /**
     * sets previous node
     * @param prev previous node
     */
	public void setPrev(TournamentPlayerNode prev) {
		this.prev = prev;
	}

    /**
     * returns next node
     * @return next node
     */
	public TournamentPlayerNode getNext() {
		return next;
	}

    /**
     * sets next node
     * @param next next node
     */
	public void setNext(TournamentPlayerNode next) {
		this.next = next;
	}

    /**
     * marks node/player as loser
     */
	public void setOutOfGame(){
		inGame = false;
	}

    /**
     * returns whether node/player has not lost yet
     * @return whether node/player has not lost yet
     */
	public boolean isInGame(){
		return inGame;
	}
	

}
