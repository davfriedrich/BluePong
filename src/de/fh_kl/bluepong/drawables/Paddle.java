package de.fh_kl.bluepong.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Paddle implements DrawableObject {
	
	private int width;
	private int height;
	private int speed;
	private Color color;
	
	
	public Paddle(int width, int height, int speed) {
		super();
		this.width = width;
		this.height = height;
		this.speed = speed;
	}

	@Override
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.CYAN);
		paint.setStyle(Style.FILL);
		canvas.drawRect(100,  width, 100, height, paint);
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
