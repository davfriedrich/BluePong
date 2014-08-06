package de.fh_kl.bluepong.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

public interface DrawableObject {
	
	public void draw(Canvas canvas);
	
	public int getHeight();
	public int getWidth();
	
	public Point getPosition();
	public void setPosition(Point newPosition);

}
