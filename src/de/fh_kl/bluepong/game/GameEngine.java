package de.fh_kl.bluepong.game;

import de.fh_kl.bluepong.constants.Constants;
import de.fh_kl.bluepong.drawables.Ball;
import de.fh_kl.bluepong.drawables.Paddle;
import de.fh_kl.bluepong.util.RelativeSizeProvider;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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
	
	Ball ball;
	
	private boolean running = false;
	private boolean winnable = true;
	GameLoop gameLoop;
	int gameMode;
	
	
	public GameEngine(SurfaceView view, RelativeSizeProvider relativeSizeProvider, int gameMode) {
		
		this.gameMode = gameMode;
		
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
		
		initBall();
		
		initialDraw();
		
		running = true;
		
		gameLoop.start();
	}
	
	private void initPaddles(){
		
		p1 = new Paddle(sizeProvider.getPaddleWidth(), sizeProvider.getPaddleHeight(), sizeProvider.getPaddleSpeed());
		Point p1StartPosition = new Point(totalWidth/2, totalHeight - sizeProvider.getPaddleHeight()/2 - sizeProvider.getPaddlePadding());
		p1.setPosition(p1StartPosition);
		p1.setTouchBox(new Rect(0, totalHeight/4 * 3, totalWidth, totalHeight));
		
		if (gameMode > TRAINING_MODE) {
			p2 = new Paddle(sizeProvider.getPaddleWidth(), sizeProvider.getPaddleHeight(), sizeProvider.getPaddleSpeed());
			Point p2StartPosition = new Point(totalWidth/2, sizeProvider.getPaddleHeight()/2 + sizeProvider.getPaddlePadding());
			p2.setPosition(p2StartPosition);
			p2.setTouchBox(new Rect(0, 0, totalWidth, totalHeight/4));
		}
	}
	
	private void initBall() {
		
		ball = new Ball(sizeProvider.getBallSize(), sizeProvider.getBallSpeed());
		Point ballStartPosition = new Point(totalWidth/2, totalHeight/2);
		ball.setPosition(ballStartPosition);
	}
	
	private void initialDraw() {
		
		Canvas canvas = holder.lockCanvas();

		p1.draw(canvas);
		
		if (gameMode > TRAINING_MODE) {
			p2.draw(canvas);
			drawPlayfield(canvas, false);
		} else {
			drawPlayfield(canvas, true);
		}
		
		ball.draw(canvas);
		
		
		holder.unlockCanvasAndPost(canvas);
	}
	
	public void gameLogic() {
		p1.move();
		p2.move();
		moveBall();
	}
	
	private void moveBall() {
		
		Point nextPosition = ball.nextPosition();
		int x = nextPosition.x;
		int y = nextPosition.y;
		
		ball.goTo(nextPosition);
		
		int goToX, goToY;
		
		
		// collision with wall?
		if (x - ball.getWidth()/2 < 0) {
			
			int dx = Math.abs(x - ball.getWidth()/2);
			
			goToX = dx + ball.getWidth()/2;
			
			ball.goTo(new Point(goToX, y));
			ball.setAngle((Math.PI - ball.getAngle()) % (2*Math.PI));
		}
		
		
		if (x + ball.getWidth()/2 > totalWidth) {
			
			int dx = Math.abs(totalWidth - (x + ball.getWidth()/2));
			
			goToX = totalWidth - (dx + ball.getWidth()/2);
			
			ball.goTo(new Point(goToX, y));
			ball.setAngle((Math.PI - ball.getAngle()) % (2*Math.PI));
		}
		

		if (gameMode == TRAINING_MODE) {
			// collision with top wall?
			if (y - ball.getHeight()/2 < totalHeight/2) {

				int dy = Math.abs(totalHeight/2 - (y - ball.getHeight()/2));

				goToY = totalHeight/2 + dy + ball.getHeight()/2;

				ball.goTo(new Point(x, goToY));
				ball.setAngle((-1 * ball.getAngle()) % (2*Math.PI));
			}
		} else {
			// collision with paddle 2?
			Rect p2Paddle = p2.getPaddle();
			if ((y - ball.getHeight()/2 < p2Paddle.bottom) && (x + ball.getWidth()/2 >= p2Paddle.left) && (x - ball.getWidth()/2 <= p2Paddle.right) && winnable) {
				
				int dy = Math.abs(p2Paddle.bottom - (y - ball.getHeight()/2));
				
				goToY = p2Paddle.bottom + (dy + ball.getHeight()/2);
				
				ball.goTo(new Point(x, goToY));
				ball.setAngle((-1 * ball.getAngle()) % (2*Math.PI));
			
			} else if (y - ball.getHeight()/2 < p2Paddle.bottom){
				
				winnable = false;
				
				if (x - ball.getWidth()/2 < p2Paddle.right && x > p2Paddle.centerX()) {
					
					int dx = Math.abs(p2Paddle.right - (x - ball.getWidth()/2));
					
					goToX = p2Paddle.right + dx + ball.getWidth()/2;
					
					ball.goTo(new Point(goToX, y));
					ball.setAngle((Math.PI - ball.getAngle()) % (2*Math.PI));
				}
				
				
				if (x + ball.getWidth()/2 > p2Paddle.left && x < p2Paddle.centerX()) {
					
					int dx = Math.abs(p2Paddle.left - (x + ball.getWidth()/2));
					
					goToX = p2Paddle.left - (dx + ball.getWidth()/2);
					
					ball.goTo(new Point(goToX, y));
					ball.setAngle((Math.PI - ball.getAngle()) % (2*Math.PI));
				}
			}	
				
			if (y + ball.getHeight()/2 < 0) {
				running = false;
			}
		}
		
		// collision with paddle 1?
		Rect p1Paddle = p1.getPaddle();
		if ((y + ball.getHeight()/2 > p1Paddle.top) && (x + ball.getWidth()/2 >= p1Paddle.left) && (x - ball.getWidth()/2 <= p1Paddle.right) && winnable) {
			
			int dy = Math.abs(p1Paddle.top - (y + ball.getHeight()/2));
			
			goToY = p1Paddle.top - (dy + ball.getHeight()/2);
			
			ball.goTo(new Point(x, goToY));
			ball.setAngle((-1 * ball.getAngle()) % (2*Math.PI));
		
		} else if(y + ball.getHeight()/2 > p1Paddle.top){
			
			winnable = false;
			
			if (x - ball.getWidth()/2 < p1Paddle.right && x > p1Paddle.centerX()) {
				
				int dx = Math.abs(p1Paddle.right - (x - ball.getWidth()/2));
				
				goToX = p1Paddle.right + dx + ball.getWidth()/2;
				
				ball.goTo(new Point(goToX, y));
				ball.setAngle((Math.PI - ball.getAngle()) % (2*Math.PI));
			}
			
			
			if (x + ball.getWidth()/2 > p1Paddle.left && x < p1Paddle.centerX()) {
				
				int dx = Math.abs(p1Paddle.left - (x + ball.getWidth()/2));
				
				goToX = p1Paddle.left - (dx + ball.getWidth()/2);
				
				ball.goTo(new Point(goToX, y));
				ball.setAngle((Math.PI - ball.getAngle()) % (2*Math.PI));
			}
		}	
			
		if (y - ball.getHeight()/2 > totalHeight) {
			running = false;
		}
		
		
		ball.move();
	}

	public void draw() {
		Canvas canvas = holder.lockCanvas();
		
		// "clears" the screen
		canvas.drawColor(Color.BLACK);
		
		// paddles
		p1.draw(canvas);
		
		if (gameMode > TRAINING_MODE) {
			p2.draw(canvas);
			// playfield
			drawPlayfield(canvas, false);
		} else {

			drawPlayfield(canvas, true);
		}
		
		// ball
		ball.draw(canvas);
		
		
		
		holder.unlockCanvasAndPost(canvas);
	}
	
	
	private void drawPlayfield(Canvas canvas, boolean training) {
		
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		
		if (training) {
			canvas.drawRect(0, 
							totalHeight/2 - sizeProvider.getWallThicknessTraining(), 
							totalWidth, 
							totalHeight/2,
							paint);			
		} else {
			
			paint.setStyle(Style.STROKE);
			paint.setPathEffect(new DashPathEffect(new float[] {10,10}, 5));
			canvas.drawLine(0, totalHeight/2, totalWidth, totalHeight/2, paint);
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
			
			if (p2.touchInTouchbox(touch)) {
				p2.goTo(px);
			}
			
		} 
		
		return true;
	}

}
