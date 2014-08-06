package de.fh_kl.bluepong.game;

import de.fh_kl.bluepong.constants.Constants;
import de.fh_kl.bluepong.drawables.Paddle;
import de.fh_kl.bluepong.util.RelativeSizeProvider;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameEngine implements OnTouchListener, Constants {
	
	
	private SurfaceView view;
	private SurfaceHolder holder;
	private RelativeSizeProvider sizeProvider;

	int totalWidth;
	int totalHeight;
	
	Paddle p1;
	Paddle p2;
	
	private boolean running = false;
	GameLoop gameLoop;
	
	
	public GameEngine(SurfaceView view, RelativeSizeProvider relativeSizeProvider) {
		
		sizeProvider = relativeSizeProvider;
		
		this.view = view;
		holder = view.getHolder();
		
		totalWidth = view.getWidth();
		totalHeight = view.getHeight();
		
		gameLoop = new GameLoop(this);
		
		init();	
	}
	
	private void init() {
		
		initPaddles();
		
		initialDraw();
		
		running = true;
		
		gameLoop.start();
	}
	
	private void initPaddles(){
		
		
		p1 = new Paddle(sizeProvider.getPaddleWidth(), sizeProvider.getPaddleHeight(), sizeProvider.getPaddleSpeed());
		Point p1StartPosition = new Point(totalWidth/2, totalHeight - sizeProvider.getPaddleHeight()/2 - sizeProvider.getPaddlePadding());
		p1.setPosition(p1StartPosition);
		p1.setTouchBox(new Rect(0, totalHeight/4 * 3, totalWidth, totalHeight));
		
	}
	
	private void initialDraw() {
		
		Canvas canvas = holder.lockCanvas();

		p1.draw(canvas);
		
		drawPlayfield(canvas);
		
		holder.unlockCanvasAndPost(canvas);
	}
	
	public void gameLogic() {
		p1.move();
	}
	
	public void draw() {
		Canvas canvas = holder.lockCanvas();
		
		// "clears" the screen
		canvas.drawColor(Color.BLACK);
		
		// paddles
		p1.draw(canvas);
		
		// ball
		
		// playfield
		drawPlayfield(canvas, true);
		
		holder.unlockCanvasAndPost(canvas);
	}
	
	private void drawPlayfield(Canvas canvas) {
		drawPlayfield(canvas, false);
	}
	
	private void drawPlayfield(Canvas canvas, boolean training) {
		
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		
		if (training) {
			canvas.drawRect(0, totalHeight/2 - sizeProvider.getWallThicknessTraining(), totalWidth, totalHeight/2, paint);			
		} else {
			
		}
		
		
	}
	
	public void stop() {
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		for (int i = 0; i < event.getPointerCount(); i++) {
			int px = (int) event.getX(i);
			int py = (int) event.getY(i);
			
			Point touch = new Point(px, py);
			
			if (p1.touchInTouchbox(touch)) {
				p1.goTo(px);
			}
			
		} 
		
		return true;
	}

}
