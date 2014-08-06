package de.fh_kl.bluepong.game;

public class GameLoop extends Thread {
	
	
	GameEngine engine;
	
	public GameLoop(GameEngine gameEngine) {
		engine = gameEngine;
	}
	
	public void run(){
		
		while (engine.isRunning()) {

			long startTime = System.currentTimeMillis();
			
			
			engine.gameLogic();	
			
			engine.draw();
			
			
			long stopTime = System.currentTimeMillis();
			
			long dTime = stopTime - startTime;
			
			if (dTime < 33) {
				try {
					sleep(33 - dTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

}
