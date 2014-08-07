package de.fh_kl.bluepong.drawables;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import de.fh_kl.bluepong.MainActivity;
import de.fh_kl.bluepong.constants.Constants;

public class Ball implements DrawableObject, Constants {

	private int size;
	private int speed;
	private Point goTo;
	private Color color;
	private Rect ball;
	private double angle;
	
	private static Random random = new Random(System.currentTimeMillis());
	
	public Ball(int size, int speed) {
		this.size = size;
		this.speed = speed;
		ball = new Rect(0, 0, size, size);		
		setAngle(randomAngle());
	}
	
	private double randomAngle() {
		
		double angle = Math.PI/2 + random.nextInt(2)*Math.PI + random.nextGaussian()*Math.PI;
		return angle; 
	}

	public Point nextPosition() {
		int x = (int) (speed * Math.cos(angle));
		int y = (int) (speed * Math.sin(angle));
		
		Point currentPosition = getPosition();
		
		return new Point(currentPosition.x + x, currentPosition.y + y);
	}
	
	public void move() {
		ball.offsetTo(goTo.x - size/2, goTo.y - size/2);
	}
	
	@Override
	public void draw(Canvas canvas) {

		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		
		canvas.drawRect(ball, paint);
	}

	@Override
	public int getHeight() {
		return size;
	}

	@Override
	public int getWidth() {
		return size;
	}

	@Override
	public Point getPosition() {
		return new Point(ball.centerX(), ball.centerY());
	}

	@Override
	public void setPosition(Point newPosition) {
		ball.offsetTo(newPosition.x - size/2, newPosition.y - size/2);
	}
	
	public void goTo(Point goTo) {
		this.goTo = goTo;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double angle) {

		this.angle = checkAndCorrectAngle(normalizeAngle(angle));
	}

    private double normalizeAngle(double angle) {
        return (angle %= 2*Math.PI) >= 0 ? angle : (angle + 2*Math.PI);
    }

    private double checkAndCorrectAngle(double angle) {

        double resultAngle = angle;

        if (resultAngle < Math.PI/8) {
            resultAngle = Math.PI / 8;
        } else if (resultAngle > 7*Math.PI/8 && resultAngle < 9*Math.PI/8) {
            if (resultAngle > Math.PI) {
                resultAngle = 9 * Math.PI/8;
            } else {
                resultAngle = 7*Math.PI/8;
            }
        } else if (resultAngle > 15*Math.PI/8) {
            resultAngle = 15*Math.PI/8;
        }

        return resultAngle;
    }

    public void addSpin(int direction) {

        Log.v("BallAngle", "before spin " + getAngle());

        setAngle(getAngle() + direction*PADDLE_SPEED_RATIO*2*Math.PI);
        Log.v("BallAngle", "before spin " + getAngle());
    }

}
