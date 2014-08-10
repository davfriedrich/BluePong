package de.fh_kl.bluepong.game;

import de.fh_kl.bluepong.constants.Constants;

import java.util.concurrent.Semaphore;

/**
 * Game thread
 */
public class GameLoop extends Thread {
	
	private GameEngine engine;
	private Semaphore pauseSemaphore;
    private Semaphore aliveSemaphore;

    /**
     * Constructor
     * @param gameEngine the game engine
     * @param pauseSemaphore semaphore to pause the thread
     * @param aliveSemaphore semaphore to ensure thread is stopped
     */
    public GameLoop(GameEngine gameEngine, Semaphore pauseSemaphore, Semaphore aliveSemaphore) {
		engine = gameEngine;
        this.pauseSemaphore = pauseSemaphore;
        this.aliveSemaphore = aliveSemaphore;
    }
	
	@Override
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
			
			pauseSemaphore.release();

            // delay between two frames
			long stopTime = System.currentTimeMillis();
			long dTime = stopTime - startTime;
			if (dTime < Constants.MSPF) {
				try {
					sleep(Constants.MSPF - dTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
		}

        engine.setDestroyed(true);

        aliveSemaphore.release();

		engine.newRound();
	}

}
