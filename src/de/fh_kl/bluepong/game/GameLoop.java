package de.fh_kl.bluepong.game;

import java.util.concurrent.Semaphore;

public class GameLoop extends Thread {
	
	GameEngine engine;
	Semaphore sem;
	
	public GameLoop(GameEngine gameEngine, Semaphore semaphore) {
		engine = gameEngine;
		sem = semaphore;
	}
	
	public void run(){
		
		while (engine.isRunning()) {
			
			try {
				sem.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			long startTime = System.currentTimeMillis();
			
			
			engine.gameLogic();	
			
			engine.draw();
			
			
			long stopTime = System.currentTimeMillis();
			
			long dTime = stopTime - startTime;
			
			sem.release();

			if (dTime < 33) {
				try {
					sleep(33 - dTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
		engine.setDestroyed(true);
		engine.newRound();
	}

}
