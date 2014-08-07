package de.fh_kl.bluepong.game;

import java.util.Random;
import java.util.concurrent.Semaphore;

import android.content.SharedPreferences;
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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameEngine implements OnTouchListener, Constants {


	private SurfaceView view;
	private SurfaceHolder holder;
	private RelativeSizeProvider sizeProvider;
    private SharedPreferences prefs;

	int totalWidth;
	int totalHeight;

	Rect controlTouchBox;

    Rect topWall;

	Paddle p1;
	Paddle p2;

	Ball ball;

	private boolean running = false;
	private boolean paused = false;
	private boolean destroyed = false;
	private boolean winnable = true;

	private int serve;

	GameLoop gameLoop;
	Semaphore pauseSemaphore;
	Semaphore aliveSemaphore;

	int gameMode;

    private int aiHandicap;
    private boolean ballSpeedIncrease;

    public GameEngine(SurfaceView view, SharedPreferences preferences, int gameMode) {

		this.gameMode = gameMode;

		this.view = view;
		holder = view.getHolder();

		totalWidth = view.getWidth();
		totalHeight = view.getHeight();

        prefs = preferences;

        readPreferences();

		sizeProvider = new RelativeSizeProvider(totalWidth, totalHeight, preferences);

		controlTouchBox = new Rect(totalWidth/4 * 1, totalHeight/5 * 2, totalWidth/4 * 3, totalHeight/5 * 3);

		pauseSemaphore = new Semaphore(1);
		aliveSemaphore = new Semaphore(1);

		gameLoop = new GameLoop(this, pauseSemaphore, aliveSemaphore);

		init();
	}

    private void readPreferences() {
        ballSpeedIncrease = prefs.getBoolean(BALL_SPEED_INCREASE_SETTING, true);
        aiHandicap = prefs.getInt(AI_HANDICAP_SETTING, 4);
    }

    private void init() {

		initPaddles();

		initBall();

		initialDraw();

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

		if (gameMode == TRAINING_MODE) {
			drawPlayfield(canvas, true);
		} else {
			p2.draw(canvas);
			drawPlayfield(canvas, false);
            drawScore(canvas);
		}

		ball.draw(canvas);


		holder.unlockCanvasAndPost(canvas);
	}

	public void gameLogic() {
		p1.move();

        if (gameMode > TRAINING_MODE) {
            p2.move();
        }

		moveBall();
	}

	private void moveBall() {

		Point nextPosition = ball.nextPosition();
		int x = nextPosition.x;
		int y = nextPosition.y;


		// collision with wall?
		if (x - ball.getWidth()/2 < 0) {

			int dx = Math.abs(x - ball.getWidth()/2);

			x = dx + ball.getWidth()/2;

			ball.setAngle((Math.PI - ball.getAngle()));
		}


		if (x + ball.getWidth()/2 > totalWidth) {

			int dx = Math.abs(totalWidth - (x + ball.getWidth()/2));

			x = totalWidth - (dx + ball.getWidth()/2);

			ball.setAngle((Math.PI - ball.getAngle()));
		}


		if (gameMode == TRAINING_MODE) {
			// collision with top wall?

			if (y - ball.getHeight()/2 < topWall.bottom) {

				int dy = Math.abs(topWall.bottom - (y - ball.getHeight()/2));

				y = topWall.bottom + dy + ball.getHeight()/2;

				ball.setAngle((-1 * ball.getAngle()));
			}
		} else {
			// collision with paddle 2?
			Rect p2Paddle = p2.getPaddle();
			if ((y - ball.getHeight()/2 < p2Paddle.bottom) && (x + ball.getWidth()/2 >= p2Paddle.left) && (x - ball.getWidth()/2 <= p2Paddle.right) && winnable) {

				int dy = Math.abs(p2Paddle.bottom - (y - ball.getHeight()/2));

				y = p2Paddle.bottom + (dy + ball.getHeight()/2);

				ball.setAngle((-1 * ball.getAngle()));

                if (p2.getDirection() != 0) {

                    ball.addSpin(p2.getDirection());
                }

                if (ballSpeedIncrease) {
                    ball.accelerate();
                }

			} else if (y - ball.getHeight()/2 < p2Paddle.bottom){

				winnable = false;

				if (x - ball.getWidth()/2 < p2Paddle.right && x > p2Paddle.centerX()) {

					int dx = Math.abs(p2Paddle.right - (x - ball.getWidth()/2));

					x = p2Paddle.right + dx + ball.getWidth()/2;

					ball.setAngle((Math.PI - ball.getAngle()));
				}


				if (x + ball.getWidth()/2 > p2Paddle.left && x < p2Paddle.centerX()) {

					int dx = Math.abs(p2Paddle.left - (x + ball.getWidth()/2));

					x = p2Paddle.left - (dx + ball.getWidth()/2);

					ball.setAngle((Math.PI - ball.getAngle()));
				}
			}

			if (y + ball.getHeight()/2 < 0) {

                p1.incrementScore();
				running = false;
				serve = 1;
			}
		}

		// collision with paddle 1?
		Rect p1Paddle = p1.getPaddle();
		if ((y + ball.getHeight()/2 > p1Paddle.top) && (x + ball.getWidth()/2 >= p1Paddle.left) && (x - ball.getWidth()/2 <= p1Paddle.right) && winnable) {

			int dy = Math.abs(p1Paddle.top - (y + ball.getHeight()/2));

			y = p1Paddle.top - (dy + ball.getHeight()/2);

            ball.setAngle((-1 * ball.getAngle()));

            if (p1.getDirection() != 0) {

                ball.addSpin(-1*p1.getDirection());
            }

            if (ballSpeedIncrease) {
                ball.accelerate();
            }

		} else if(y + ball.getHeight()/2 > p1Paddle.top){

			winnable = false;

			if (x - ball.getWidth()/2 < p1Paddle.right && x > p1Paddle.centerX()) {

				int dx = Math.abs(p1Paddle.right - (x - ball.getWidth()/2));

				x = p1Paddle.right + dx + ball.getWidth()/2;

				ball.setAngle((Math.PI - ball.getAngle()));
			}


			if (x + ball.getWidth()/2 > p1Paddle.left && x < p1Paddle.centerX()) {

				int dx = Math.abs(p1Paddle.left - (x + ball.getWidth()/2));

				x = p1Paddle.left - (dx + ball.getWidth()/2);

				ball.setAngle((Math.PI - ball.getAngle()));
			}
		}

		if (y - ball.getHeight()/2 > totalHeight) {

            if (gameMode != TRAINING_MODE) {
                p2.incrementScore();
            }
			running = false;
			serve = 2;
		}

        // collision with wall directly after paddle collision?
        if (x - ball.getWidth()/2 < 0) {

            int dx = Math.abs(x - ball.getWidth()/2);

            x = dx + ball.getWidth()/2;

            ball.setAngle((Math.PI - ball.getAngle()));
        }


        if (x + ball.getWidth()/2 > totalWidth) {

            int dx = Math.abs(totalWidth - (x + ball.getWidth()/2));

            x = totalWidth - (dx + ball.getWidth()/2);

            ball.setAngle((Math.PI - ball.getAngle()));
        }

        ball.goTo(new Point(x, y));

		ball.move();
	}

	public void draw() {
		Canvas canvas = holder.lockCanvas();

		// "clears" the screen
		canvas.drawColor(Color.BLACK);

		// paddles
		p1.draw(canvas);

		if (gameMode == TRAINING_MODE) {
			// playfield
			drawPlayfield(canvas, true);
		} else {

			p2.draw(canvas);
			drawPlayfield(canvas, false);
            drawScore(canvas);
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

            topWall = new Rect(0,
							totalHeight/2 - sizeProvider.getWallThicknessTraining() - sizeProvider.getBallSize()/2,
							totalWidth,
							totalHeight/2 - sizeProvider.getBallSize()/2);
			canvas.drawRect(topWall, paint);
		} else {

			paint.setStyle(Style.STROKE);
			paint.setPathEffect(new DashPathEffect(new float[] {10,10}, 5));
			canvas.drawLine(0, totalHeight/2, totalWidth, totalHeight/2, paint);
		}
	}


    private void drawScore(Canvas canvas) {

        int x = totalWidth/5 * 4;

        int yp1 = totalHeight/2 + totalHeight/20;
        int yp2 = totalHeight/2 - totalHeight/20;

        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setTextSize(sizeProvider.getTextSize());

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.save();
        canvas.rotate(90, x, yp1);
        canvas.drawText("" + p1.getScore(), x, yp1, paint);
        canvas.restore();

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.save();
        canvas.rotate(90, x, yp2);
        canvas.drawText("" + p2.getScore(), x, yp2, paint);
        canvas.restore();
    }


	public void stop() {
		running = false;

        pauseSemaphore.release();

        try {
            aliveSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	public boolean isRunning() {
		return running;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public void newRound(){
		newBall();
        initialDraw();

		winnable = true;
		destroyed = false;
		paused = false;
		running = true;

		gameLoop = new GameLoop(this, pauseSemaphore, aliveSemaphore);
		gameLoop.start();
	}

	public void newBall(){
		Point ballStartPosition = new Point(totalWidth/2, totalHeight/2);
		ball.setPosition(ballStartPosition);

		if(serve == 1){
            ball.serve(serve);
		} else if (serve == 2) {
            ball.serve(serve);
		} else {
            ball.randomAngle();
        }
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

            if (gameMode > TRAINING_MODE) {
                if (p2.touchInTouchbox(touch)) {
                    p2.goTo(px);
                }
            }

			if(event.getAction() == MotionEvent.ACTION_DOWN){
				if (controlTouchBox.contains(px, py)) {
					if (!running && !destroyed) {
						running = true;
						gameLoop.start();
					} else if (running && !destroyed && !paused) {
						try {
							pauseSemaphore.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						paused = true;
					} else if (running && !destroyed && paused) {
						pauseSemaphore.release();
						paused = false;
					}
				}
			}

		}

		return true;
	}

}
