package de.fh_kl.bluepong.constants;

public interface Constants {
	final static String GAME_MODE = "GameMode";
	final static int TRAINING_MODE = 0;
	final static int SINGLE_MODE = 1;
	final static int MULTITOUCH_MODE = 2;
	final static int BLUETOOTH_MODE = 3;
	
	final static String TOURNAMENT_PLAYER = "TournamentPlayer";	
	
	static double PADDLE_HEIGHT_RATIO = 0.04;
	static double PADDLE_PADDING_RATIO = 0.04;
	static double PADDLE_SPEED_RATIO = 0.027;
	static double PADDLE_WIDTH_RATIO = 0.04;

	static double BALL_SPEED_RATIO = 0.004;
	static double BALL_SIZE_RATIO = 0.004;

	static double WALL_THICKNESS_TRAINING = 0.04;

	static double TEXT_SIZE_RATIO = 0.16;

	
	static int FPS = 30;




    final static String BALL_SPEED_SETTING = "ballSpeedSetting";
    final static String BALL_SPEED_INCREASE_SETTING = "ballSpeedIncreaseSetting";
    final static String BALL_SIZE_SETTING = "ballSizeSetting";

    final static String PADDLE_SPEED_SETTING = "paddleSpeedSetting";
    final static String PADDLE_SIZE_SETTING = "paddleSizeSetting";

    final static String AI_HANDICAP_SETTING = "aiHandicapSetting";

}
