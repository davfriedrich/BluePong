package de.fh_kl.bluepong.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;

public class Paddle implements DrawableObject {

    private boolean human;
	private int width;
	private int height;
	private int speed;
	private int goTo;
    private int score = 0;
	private Color color;
	private Rect paddle;
	private Rect touchbox;
	
	
	public Paddle(int width, int height, int speed) {
		super();
		this.width = width;
		this.height = height;
		this.speed = speed;
        human = false;
		paddle = new Rect(0,  0, width, height);
	}

    public Paddle(int width, int height, int speed, boolean isHuman) {
        super();
        this.width = width;
        this.height = height;
        this.speed = speed;
        human = isHuman;
        paddle = new Rect(0,  0, width, height);
    }

	@Override
	public void draw(Canvas canvas) {
		
		Paint paint = new Paint();
		paint.setColor(Color.CYAN);
		paint.setStyle(Style.FILL);
		
		canvas.drawRect(paddle, paint);
	}
	
	public void move() {
		move(speed);
	}
	
	public void move(int speed){
		int paddleCenter = paddle.centerX();
		
		int dx = paddleCenter - goTo;
		
		if (dx > 0) {
			paddle.offset(-Math.min(Math.abs(dx), speed), 0);
		} else {
			paddle.offset(Math.min(Math.abs(dx), speed), 0);
		}
	}

    public int getScore() {
        return score;
    }

    public int incrementScore() {
        return incrementScore(1);
    }

    public int incrementScore(int i) {
        return score += i;
    }
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public Point getPosition() {
		return new Point(paddle.centerX(), paddle.centerY());
	}

    public int getXPosition() {
        return paddle.centerX();
    }
	
	public void setPosition(Point newPosition) {
		paddle.offsetTo(newPosition.x - width/2, newPosition.y - height/2);
		
		//prevents paddle from moving around
		goTo = paddle.centerX();
	}
	
	public Rect getPaddle() {
		return paddle;
	}

	public void setTouchBox(Rect touchbox) {
		this.touchbox = touchbox;
	}
	
	public boolean touchInTouchbox(Point p){
		return touchbox.contains(p.x, p.y);
	}

	public void goTo(int goTo) {
		this.goTo = goTo;
	}

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

    public boolean isHuman() {
        return isHuman();
    }

	

}
