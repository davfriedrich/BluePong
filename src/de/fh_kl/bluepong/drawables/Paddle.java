package de.fh_kl.bluepong.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * represents a pong paddle
 */
public class Paddle {

    private String name;
	private int width;
	private int height;
	private int speed;
	private int goTo;
    private int score = 0;
    private Paint paint;
	private Color color;
	private Rect paddle;
	private Rect touchbox;

    /**
     * Constructor for paddle
     *
     * @param width width of the paddle
     * @param height height of the paddle
     * @param speed speed of the paddle
     */
    public Paddle(int width, int height, int speed) {
        this.width = width;
        this.height = height;
        this.speed = speed;
        paddle = new Rect(0,  0, width, height);
        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Style.FILL);
    }

    /**
     * draws paddle to given canvas
     * @param canvas the canvas to draw on
     */
	public void draw(Canvas canvas) {
		
		canvas.drawRect(paddle, paint);
	}

    /**
     * moves paddle to {@code goTo}
     */
	public void move() {
		move(speed);
	}

    /**
     * moves paddle to {@code goTo} with speed {@code speed}
     * @param speed speed of the paddle
     */
	public void move(int speed){
		int paddleCenter = paddle.centerX();
		
		int dx = paddleCenter - goTo;
		
		if (dx > 0) {
			paddle.offset(-Math.min(Math.abs(dx), speed), 0);
		} else {
			paddle.offset(Math.min(Math.abs(dx), speed), 0);
		}
	}

    /**
     * returns current score of this paddle/player
     * @return current score
     */
    public int getScore() {
        return score;
    }

    /**
     * increments score by 1
     */
    public void incrementScore() {
        incrementScore(1);
    }

    /**
     * increments score by {@code i}
     * @param i value to add to the score
     */
    public void incrementScore(int i) {
        score += i;
    }

    /**
     * returns name of the paddle/player
     * @return name of the paddle/player
     */
    public String getName() {
        return name;
    }

    /**
     *  sets name of the paddle/player
     * @param name name of the paddle/player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets X coordinate of the paddles current position
     * @return X coordinate of the paddles current position
     *
     * Y coordinate is irrelevant because it never changes
     */
    public int getXPosition() {
        return paddle.centerX();
    }

    /**
     * moves paddle to new position
     * @param x X coordinate of the new position
     *
     * Y coordinate is irrelevant because it never changes
     */
    public void setXPosition(int x) {
        setPosition(new Point(x, paddle.centerY()));
    }

    /**
     * moves paddle to new position
     * @param newPosition new position as {@link android.graphics.Point}
     */
    // used to initialize paddle position (top or bottom of the screen)
	public void setPosition(Point newPosition) {
		paddle.offsetTo(newPosition.x - width/2, newPosition.y - height/2);
		
		//prevents paddle from moving sideways due to old values in goTo
		goTo = paddle.centerX();
	}

    /**
     * returns {@link android.graphics.Rect} of the paddle
     * @return {@link android.graphics.Rect} of the paddle;
     */
	public Rect getPaddle() {
		return paddle;
	}

    /**
     * sets touchbox for the paddle
     * @param touchbox touchbox of the paddle as {@link android.graphics.Rect}
     *
     * this is the area of touch events which control the paddle
     */
	public void setTouchBox(Rect touchbox) {
		this.touchbox = touchbox;
	}

    /**
     * checks if a {@link android.graphics.Point}  is located in the paddles touchbox
     * @param p {@link android.graphics.Point} to check
     * @return {@code true} if {@code p} is located in {@code touchbox}, {@code false} if not
     */
	public boolean touchInTouchbox(Point p){
		return touchbox.contains(p.x, p.y);
	}

    /**
     * sets next destination of the paddle
     * @param goTo X coordinate to go to
     */
	public void goTo(int goTo) {
		this.goTo = goTo;
	}

    /**
     * returns the direction as int in which the paddle is moving
     * @return the direction as int in which the paddle is moving.
     *
     * -1 for left.
     * 0 for not moving.
     * 1 for right.
     */
    public int getDirection() {

        if (goTo == getXPosition()) {
            return 0;
        } else {
            if (goTo < getXPosition()) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
