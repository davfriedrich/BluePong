package de.fh_kl.bluepong.constants;

import java.util.UUID;

public interface Constants {
	final static String GAME_MODE = "GameMode";
	final static int TRAINING_MODE = 0;
	final static int SINGLE_MODE = 1;
	final static int MULTITOUCH_MODE = 2;
	final static int BLUETOOTH_MODE = 3;
	final static int TOURNAMENT_MODE = 4;
	final static int TOURNAMENT_MODE_AI = 5;

	final static String TOURNAMENT_AI = "TournamentAi";
	final static String TOURNAMENT_PLAYER = "TournamentPlayer";	
	final static String PLAYER_NAMES = "PlayerNames";
	final static String WINNER = "Winner";
	final static String BLUETOOTH_SERVICE = "BluetoothService";

    final static int SCORE_LIMIT = 1;
	
	static double PADDLE_HEIGHT_RATIO = 0.03;
	static double PADDLE_PADDING_RATIO = 0.08;
	static double PADDLE_SPEED_RATIO = 0.027;
	static double PADDLE_WIDTH_RATIO = 0.04;

	static double BALL_SPEED_RATIO = 0.004;
	static double BALL_SIZE_RATIO = 0.004;

	static double WALL_THICKNESS_TRAINING = 0.04;

	static double SCORE_SIZE_RATIO = 0.08;
	static double MENU_TEXT_SIZE_RATIO = 0.1;
	static double PLAYER_TEXT_SIZE_RATIO = 0.04;

	
	static int FPS = 30;




    final static String BALL_SPEED_SETTING = "ballSpeedSetting";
    final static String BALL_SPEED_INCREASE_SETTING = "ballSpeedIncreaseSetting";
    final static String BALL_SIZE_SETTING = "ballSizeSetting";

    final static String PADDLE_SPEED_SETTING = "paddleSpeedSetting";
    final static String PADDLE_SIZE_SETTING = "paddleSizeSetting";

    final static String AI_HANDICAP_SETTING = "aiHandicapSetting";


    final static String BLUETOOTH_UUID = "39f75160-1fc7-11e4-8c21-0800200c9a66";
    final static String BLUETOOTH_SERVER_NAME = "BluePong Server";

}
