package de.fh_kl.bluepong.game;

import java.util.concurrent.Semaphore;

public class GameLoop extends Thread {
	
	GameEngine engine;
	Semaphore pauseSemaphore;
    private Semaphore aliveSemaphore;

    public GameLoop(GameEngine gameEngine, Semaphore semaphore, Semaphore aliveSemaphore) {
		engine = gameEngine;
        pauseSemaphore = semaphore;
        this.aliveSemaphore = aliveSemaphore;
    }
	
	public void run(){

        try {
            aliveSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (engine.isRunning()) {
			
			try {
                pauseSemaphore.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			long startTime = System.currentTimeMillis();
			
			
			engine.gameLogic();	
			
			engine.draw();
			
			
			long stopTime = System.currentTimeMillis();
			
			long dTime = stopTime - startTime;
			
			pauseSemaphore.release();

			if (dTime < 33) {
				try {
					sleep(33 - dTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
//		engine.setDestroyed(true);

        aliveSemaphore.release();

		engine.newRound();
	}

}
