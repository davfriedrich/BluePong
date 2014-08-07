package de.fh_kl.bluepong.util;

import de.fh_kl.bluepong.constants.Constants;

public class RelativeSizeProvider implements Constants {
	
	int width;
	int height;
	
	public RelativeSizeProvider(int viewWidth, int viewHeight){
		width = viewWidth;
		height = viewHeight;
	}
	
	public int getBallSize() {
		return (int) (height * BALL_SIZE_RATIO);
	}
	
	public int getBallSpeed() {
		return (int) (width * BALL_SPEED_RATIO);
	}
	
	public int getPaddleWidth() {
		return (int) (width * PADDLE_WIDTH_RATIO);
	}
	
	public int getPaddleHeight() {
		return (int) (height * PADDLE_HEIGHT_RATIO);
	}
	
	public int getPaddleSpeed() {
		return (int) (width * PADDLE_SPEED_RATIO);
	}
	
	public int getPaddlePadding() {
		return (int) (height * PADDLE_PADDING_RATIO);
	}
	
	public int getWallThicknessTraining() {
		return (int) (height * WALL_THICKNESS_TRAINING);
	}

    public int getTextSize() {
        return (int) (width * TEXT_SIZE_RATIO);
    }

}
