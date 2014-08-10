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

/**
 * Represents the pong ball
 */
public class Ball {

	private int size;
	private int speed, initialSpeed;
	private Color color;
	private Rect ball;
	private double angle;
	
	private static Random random = new Random(System.currentTimeMillis());

    /**
     * Constructor
     * @param size
     * @param speed
     */
	public Ball(int size, int speed) {
		this.size = size;
		this.speed = speed;
        initialSpeed = speed;
		ball = new Rect(0, 0, size, size);		
		randomAngle();
	}

    /**
     * sets angle depending on which player scored
     * @param i int of the player. 1 for player 1, 2 for player 2
     */
    public void serve(int i) {
        if (i == 2) {
            setAngle(Math.PI + random.nextDouble()*Math.PI);
        } else {
            setAngle(random.nextDouble()*Math.PI);
        }
    }

    /**
     * sets random angle
     */
	public void randomAngle() {
		
		double angle = Math.PI/2 + random.nextInt(2)*Math.PI + random.nextGaussian()*Math.PI;
		setAngle(angle);
	}

    /**
     * calculates next position of the ball
     * @return next position of the ball as a {@link android.graphics.Point}
     */
	public Point nextPosition() {
		int x = (int) (speed * Math.cos(angle));
		int y = (int) (speed * Math.sin(angle));
		
		Point currentPosition = getPosition();
		
		return new Point(currentPosition.x + x, currentPosition.y + y);
	}

    /**
     * draws ball to given canvas
     * @param canvas the canvas to draw on
     */
	public void draw(Canvas canvas) {

		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		
		canvas.drawRect(ball, paint);
	}

    /**
     * return the height of the ball
     * @return height of the ball
     */
	public int getHeight() {
		return size;
	}
    /**
     * return the width of the ball
     * @return width of the ball
     */
	public int getWidth() {
		return size;
	}

    /**
     * returns current position of the ball
     * @return curretn position as {@link android.graphics.Point}
     */
	public Point getPosition() {
		return new Point(ball.centerX(), ball.centerY());
	}

    /**
     * moves the ball to {@code newPosition}
     * @param newPosition position to move the ball to
     */
	public void setPosition(Point newPosition) {
		ball.offsetTo(newPosition.x - size/2, newPosition.y - size/2);
	}

    /**
     * return current angle of the balls movement
     * @return the current angle
     */
	public double getAngle() {
		return angle;
	}

    /**
     * sets the angle of the balls movement
     * @param angle new angle
     */
	public void setAngle(double angle) {

		this.angle = checkAndCorrectAngle(normalizeAngle(angle));
	}

    /**
     * transforms given {@code angle} to an angle between 0 and 2*PI
     * @param angle angle to transform
     * @return normalized angle
     */
    private double normalizeAngle(double angle) {
        return (angle %= 2*Math.PI) >= 0 ? angle : (angle + 2*Math.PI);
    }

    /**
     * takes given {@code angle} and changes it if to shallow
     * @param angle angle to check
     * @return corrected angle
     *
     * prevents ball from moving only horizontally
     */
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

    /**
     * adds some "spin" to the ball
     * @param direction direction of the spin.
     *
     * {@code direction}<0: left. {@code direction}>0: right
     */
    public void addSpin(int direction) {
        setAngle(getAngle() + direction*Constants.PADDLE_SPEED_RATIO*2*Math.PI);
    }

    /**
     * resets the balls speed to its initial value
     */
    public void resetSpeed() {
        speed = initialSpeed;
    }

    /**
     * speeds up the ball
     */
    public void accelerate() {
        accelerate(1);
    }

    /**
     * speeds up the ball by i
     * @param i
     */
    private void accelerate(int i) {
        speed += i;
    }

}
