package de.fh_kl.bluepong.drawables;

import android.graphics.Canvas;

public interface DrawableObject {
	
	public void draw(Canvas canvas);
	
	public int getHeight();
	public int getWidth();
	
	public int[] getPosition();

}
