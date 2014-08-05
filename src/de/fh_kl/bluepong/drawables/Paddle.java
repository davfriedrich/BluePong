package de.fh_kl.bluepong.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

public class Paddle implements DrawableObject {
	
	private int width;
	private int height;
	private int speed;
	private Color color;
	private Point position;
	
	
	public Paddle(int width, int height, int speed, int x, int y) {
		super();
		this.width = width;
		this.height = height;
		this.speed = speed;
		position = new Point(x, y);
	}

	@Override
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.CYAN);
		paint.setStyle(Style.FILL);
		
		
		
		canvas.drawRect(position.x,  position.y, width, height, paint);
	}
	
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int[] getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
