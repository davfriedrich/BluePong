package de.fh_kl.bluepong.constants;

public interface Constants {
	final static String GAME_MODE = "GameMode";
	final static int TRAINING_MODE = 0;
	final static int SINGLE_MODE = 1;
	final static int MULTITOUCH_MODE = 2;
	final static int BLUETOOTH_MODE = 3;
	
	
	static double PADDLE_WIDTH_RATIO = 0.2;
	static double PADDLE_HEIGHT_RATIO = 0.04;
	static double PADDLE_PADDING_RATIO = 0.04;
	static double PADDLE_SPEED_RATIO = 0.027;
	
	static double BALL_SIZE_RATIO = 0.02;
	static double BALL_SPEED_RATIO = 0.02;
	
	static double WALL_THICKNESS_TRAINING = 0.04;
	
	static int FPS = 30;

}
