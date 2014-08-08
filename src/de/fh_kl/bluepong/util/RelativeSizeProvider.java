package de.fh_kl.bluepong.util;

import android.content.SharedPreferences;
import android.graphics.Point;
import de.fh_kl.bluepong.constants.Constants;

public class RelativeSizeProvider implements Constants {
	
	int width;
	int height;
    SharedPreferences prefs;
    int ballSpeedSetting, ballSizeSetting, paddleSpeedSetting, paddleSizeSetting;
	
	public RelativeSizeProvider(int viewWidth, int viewHeight, SharedPreferences preferences){
		width = viewWidth;
		height = viewHeight;
        prefs = preferences;

        readPreferences();
	}

    private void readPreferences() {
        ballSpeedSetting = prefs.getInt(BALL_SPEED_SETTING, 4) + 1;
        ballSizeSetting = prefs.getInt(BALL_SIZE_SETTING, 4) + 1;
        paddleSpeedSetting = prefs.getInt(PADDLE_SPEED_SETTING, 4) + 1;
        paddleSizeSetting= prefs.getInt(PADDLE_SIZE_SETTING, 4) + 1;
    }

    public int getBallSize() {
		return (int) (height * BALL_SIZE_RATIO * ballSizeSetting);
	}
	
	public int getBallSpeed() {
		return (int) (width * BALL_SPEED_RATIO * ballSpeedSetting);
	}

    public Point getCenterPoint() { return new Point(width/2, height/2); }
	
	public int getPaddleWidth() {
		return (int) (width * PADDLE_WIDTH_RATIO * paddleSizeSetting);
	}
	
	public int getPaddleHeight() {
		return (int) (height * PADDLE_HEIGHT_RATIO);
	}
	
	public int getPaddleSpeed() {
		return (int) (width * PADDLE_SPEED_RATIO * paddleSpeedSetting);
	}
	
	public int getPaddlePadding() {
		return (int) (height * PADDLE_PADDING_RATIO);
	}
	
	public int getWallThicknessTraining() {
		return (int) (height * WALL_THICKNESS_TRAINING);
	}

    public int getScoreSize() {
        return (int) (width * SCORE_SIZE_RATIO);
    }

    public int getMenuSize() {
        return (int) (width * SCORE_SIZE_RATIO);
    }
    public int getPlayerNameSize() {
        return (int) (width * PLAYER_TEXT_SIZE_RATIO);
    }

}
