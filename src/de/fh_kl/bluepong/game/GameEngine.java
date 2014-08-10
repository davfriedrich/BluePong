package de.fh_kl.bluepong.game;

import java.util.concurrent.Semaphore;

import android.content.SharedPreferences;
import android.graphics.*;
import android.util.Log;
import de.fh_kl.bluepong.GameActivity;
import de.fh_kl.bluepong.R;
import de.fh_kl.bluepong.constants.Constants;
import de.fh_kl.bluepong.drawables.Ball;
import de.fh_kl.bluepong.drawables.Paddle;
import de.fh_kl.bluepong.util.BluetoothService;
import de.fh_kl.bluepong.util.RelativeSizeProvider;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Class to handle the game process
 */
public class GameEngine implements OnTouchListener, Constants {

    private GameActivity gameActivity;
	private SurfaceView view;
	private SurfaceHolder holder;
	private RelativeSizeProvider sizeProvider;
    private SharedPreferences prefs;
    private BluetoothService bluetoothService;

    private int totalWidth;
    private int totalHeight;

    private double widthRatio;
    private double heightRatio;

    private Paint paint;

    // the touchbox to start and pause the game
    private Rect controlTouchBox;

    private Rect topWall;

    private Paddle player1;
    private Paddle player2;

    private Ball ball;

    // game state flags
	private boolean running = false;
	private boolean started = false;
	private boolean paused = false;
	private boolean destroyed = false;
	private boolean winnable = true;

	private int serve;

    private GameLoop gameLoop;

    // semaphores to synchronize with game thread
    private Semaphore pauseSemaphore;
    private Semaphore aliveSemaphore;

    private int gameMode;
    
    private int aiHandicap;
    private boolean ballSpeedIncrease;

    /**
     * standard constructor
     * @param gameActivity Activity from which the engine is called
     * @param view view which display the game
     * @param preferences game settings
     * @param gameMode the game mode
     */
    public GameEngine(GameActivity gameActivity, SurfaceView view, SharedPreferences preferences, int gameMode) {

        init(gameActivity, view, preferences, gameMode);

        if (gameMode == BLUETOOTH_MODE) {
            bluetoothService = BluetoothService.getInstance();
        }

		initPaddles();

        initBall();

        draw();
	}

    /**
     * Constructor for tournament mode.
     * see {@link de.fh_kl.bluepong.game.GameEngine#GameEngine(de.fh_kl.bluepong.GameActivity, android.view.SurfaceView, android.content.SharedPreferences, int)}
     * @param playerNames player names as array
     */
    public GameEngine(GameActivity gameActivity, SurfaceView view, SharedPreferences preferences, int gameMode, String playerNames[]) {

    	init(gameActivity, view, preferences, gameMode);

        initPaddles(playerNames);

        initBall();

        draw();
	}

    /**
     * Constructor for bluetooth mode.
     * see {@link de.fh_kl.bluepong.game.GameEngine#GameEngine(de.fh_kl.bluepong.GameActivity, android.view.SurfaceView, android.content.SharedPreferences, int)}
     * @param displayRatio ratio between opponent screen size and own screen size as array for width and height
     */
    public GameEngine(GameActivity gameActivity, SurfaceView view, SharedPreferences preferences, int gameMode, double[] displayRatio) {

        widthRatio = displayRatio[0];
        heightRatio = displayRatio[1];

        init(gameActivity, view, preferences, gameMode);

        bluetoothService = BluetoothService.getInstance();

        initPaddles();

        initBall();

        draw();
    }

    /**
     * basic setup for the game
     */
    private void init(GameActivity gameActivity, SurfaceView view, SharedPreferences preferences, int gameMode) {
        this.gameActivity = gameActivity;

        this.gameMode = gameMode;

        this.view = view;
        holder = view.getHolder();

        totalWidth = view.getWidth();
        totalHeight = view.getHeight();

        prefs = preferences;

        readPreferences();

        sizeProvider = new RelativeSizeProvider(totalWidth, totalHeight, preferences);

        paint = new Paint();
        paint.setStyle(Style.FILL);
        Typeface team401 = Typeface.createFromAsset(gameActivity.getAssets(), "fonts/Team401.ttf");
        paint.setTypeface(team401);

        controlTouchBox = new Rect(totalWidth/4 * 1, totalHeight/5 * 2, totalWidth/4 * 3, totalHeight/5 * 3);

        pauseSemaphore = new Semaphore(1);
        aliveSemaphore = new Semaphore(1);

        gameLoop = new GameLoop(this, pauseSemaphore, aliveSemaphore);
    }

    /**
     * reads game settings
     * */
    private void readPreferences() {
        ballSpeedIncrease = prefs.getBoolean(BALL_SPEED_INCREASE_SETTING, true);
        aiHandicap = prefs.getInt(AI_HANDICAP_SETTING, 4) + 1;
    }

    /**
     * initializes paddles with player names
     * see {@link GameEngine#initPaddles()}
     * @param playerNames player names as array
     */
    private void initPaddles(String[] playerNames) {
        initPaddles();
        player1.setName(playerNames[0]);
        player2.setName(playerNames[1]);
    }

    /**
     *  initializes paddles depending on game mode and sets their position (top/bottom)
     */
	private void initPaddles(){

		player1 = new Paddle(sizeProvider.getPaddleWidth(), sizeProvider.getPaddleHeight(), sizeProvider.getPaddleSpeed());
		Point p1StartPosition = new Point(totalWidth/2, totalHeight - sizeProvider.getPaddleHeight()/2 - sizeProvider.getPaddlePadding());
		player1.setPosition(p1StartPosition);
		player1.setTouchBox(new Rect(0, totalHeight / 4 * 3, totalWidth, totalHeight));

        if (gameMode != TRAINING_MODE) {

            int paddleSpeed = sizeProvider.getPaddleSpeed();
            if (gameMode == SINGLE_MODE || gameMode == TOURNAMENT_MODE_AI) {
                paddleSpeed /= aiHandicap;
            }

			player2 = new Paddle(sizeProvider.getPaddleWidth(), sizeProvider.getPaddleHeight(), paddleSpeed);
			Point p2StartPosition = new Point(totalWidth/2, sizeProvider.getPaddleHeight()/2 + sizeProvider.getPaddlePadding());
			player2.setPosition(p2StartPosition);

            if (gameMode != BLUETOOTH_MODE) {
                player2.setTouchBox(new Rect(0, 0, totalWidth, totalHeight / 4));
            }
		}
	}

    /**
     * initializes ball and sets it to screen center
     */
	private void initBall() {

        ball = new Ball(sizeProvider.getBallSize(), sizeProvider.getBallSpeed());
		ball.setPosition(sizeProvider.getCenterPoint());
	}

    /**
     * method containing the steps executed per tick
     */
	public void gameLogic() {

		player1.move();

        switch (gameMode) {
            case TRAINING_MODE:
                ball.setPosition(moveBall());
                break;
            case SINGLE_MODE:
            case TOURNAMENT_MODE_AI:
                aiMove(player2);
                ball.setPosition(moveBall());
                break;
            case MULTITOUCH_MODE:
            case TOURNAMENT_MODE:
                player2.move();
                ball.setPosition(moveBall());
                break;
            case BLUETOOTH_MODE:

                // checks if own position can be send to opponent
                // if not: bluetooth connection is not established -> stop game
                if (!bluetoothService.sendPosition(player1.getXPosition())) {
                    drawConnectionLost();
                    stop();
                    break;
                }

                // get position of opponent and draw it
                player2.setXPosition((int) (totalWidth - bluetoothService.getOpponentPosition() * widthRatio));

                // if we host the game we define the ball position and send it to the opponent
                if (bluetoothService.isServer()) {
                    Point goTo = moveBall();
                    bluetoothService.sendBallPosition(goTo);
                    ball.setPosition(goTo);
                } else {
                // if we don't host the game synchronize with host to check if ball was out to set the scores
                // then get the ball position from opponent
                    boolean isOut = bluetoothService.receiveIsBallOut();
                    if (isOut) {
                        if (ball.getPosition().y > totalHeight / 2) {
                            player2.incrementScore();
                        } else {
                            player1.incrementScore();
                        }
                    }
                    Point goTo = bluetoothService.getBallPosition();
                    goTo.x = (int) (totalWidth - goTo.x * widthRatio);
                    goTo.y = (int) (totalHeight - goTo.y * heightRatio);
                    ball.setPosition(goTo);
                }
                break;
        }
    }

    /**
     * ai to move a paddle
     * @param p paddle to move
     */
    private void aiMove(Paddle p) {
        p.goTo(ball.getPosition().x);
        p.move();
    }

    /**
     * method to determine the next position of the ball
     * handles collisions with walls and paddles
     *
     * @return next position of the ball as {@link android.graphics.Point}
     */
    private Point moveBall() {

		Point nextPosition = ball.nextPosition();
		int x = nextPosition.x;
		int y = nextPosition.y;

        boolean newRound = false;

		// collision with left wall
		if (x - ball.getWidth()/2 < 0) {

			int dx = Math.abs(x - ball.getWidth()/2);

			x = dx + ball.getWidth()/2;

			ball.setAngle((Math.PI - ball.getAngle()));
		}

        // collision with right wall?
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
			Rect p2Paddle = player2.getPaddle();
			if ((y - ball.getHeight()/2 < p2Paddle.bottom)
                    && (x + ball.getWidth()/2 >= p2Paddle.left)
                    && (x - ball.getWidth()/2 <= p2Paddle.right)
                    && winnable) {

				int dy = Math.abs(p2Paddle.bottom - (y - ball.getHeight()/2));

				y = p2Paddle.bottom + (dy + ball.getHeight()/2);

				ball.setAngle((-1 * ball.getAngle()));

                if (player2.getDirection() != 0) {

                    ball.addSpin(player2.getDirection());
                }

                if (ballSpeedIncrease) {
                    ball.accelerate();
                }
            // if ball missed paddle handle collisions with side of paddle
			} else if (y - ball.getHeight()/2 < p2Paddle.bottom){

                // player2 can't win this round anymore
				winnable = false;

                // collision with right side of paddle?
				if (x - ball.getWidth()/2 < p2Paddle.right && x > p2Paddle.centerX() && !(y + ball.getHeight()/2 < p2Paddle.top)) {

					int dx = Math.abs(p2Paddle.right - (x - ball.getWidth()/2));

					x = p2Paddle.right + dx + ball.getWidth()/2;

					ball.setAngle((Math.PI - ball.getAngle()));
				}

                // collision with left side of paddle?
				if (x + ball.getWidth()/2 > p2Paddle.left && x < p2Paddle.centerX() && !(y + ball.getHeight()/2 < p2Paddle.top)) {

					int dx = Math.abs(p2Paddle.left - (x + ball.getWidth()/2));

					x = p2Paddle.left - (dx + ball.getWidth()/2);

					ball.setAngle((Math.PI - ball.getAngle()));
				}
			}
            // is ball out of game?
			if (y + ball.getHeight()/2 < 0) {
                player1.incrementScore();
				running = false;
				serve = 1;
                newRound = true;
			}
		}

		// collision with paddle 1?
		Rect p1Paddle = player1.getPaddle();
		if ((y + ball.getHeight()/2 > p1Paddle.top)
                && (x + ball.getWidth()/2 >= p1Paddle.left)
                && (x - ball.getWidth()/2 <= p1Paddle.right)
                && winnable) {

			int dy = Math.abs(p1Paddle.top - (y + ball.getHeight()/2));

			y = p1Paddle.top - (dy + ball.getHeight()/2);

            ball.setAngle((-1 * ball.getAngle()));

            if (player1.getDirection() != 0) {

                ball.addSpin(-1* player1.getDirection());
            }

            if (ballSpeedIncrease) {
                ball.accelerate();
            }
        // if ball missed paddle handle collisions with side of paddle
		} else if (y + ball.getHeight()/2 > p1Paddle.top){

            // player1 can't win this round anymore
			winnable = false;

            // collision with right side of paddle?
			if (x - ball.getWidth()/2 < p1Paddle.right && x > p1Paddle.centerX() && !(y - ball.getHeight()/2 > p1Paddle.bottom)) {

				int dx = Math.abs(p1Paddle.right - (x - ball.getWidth()/2));

				x = p1Paddle.right + dx + ball.getWidth()/2;

				ball.setAngle((Math.PI - ball.getAngle()));
			}

            // collision with left side of paddle?
			if (x + ball.getWidth()/2 > p1Paddle.left && x < p1Paddle.centerX() && !(y - ball.getHeight()/2 > p1Paddle.bottom)) {

				int dx = Math.abs(p1Paddle.left - (x + ball.getWidth()/2));

				x = p1Paddle.left - (dx + ball.getWidth()/2);

				ball.setAngle((Math.PI - ball.getAngle()));
			}
		}

        // is ball out of game?
        if (y - ball.getHeight()/2 > totalHeight) {

            if (gameMode != TRAINING_MODE) {
                player2.incrementScore();
            }
			running = false;
			serve = 2;
            newRound = true;
		}

        // collision with wall directly after paddle collision?
        // left wall
        if (x - ball.getWidth()/2 < 0) {
            int dx = Math.abs(x - ball.getWidth()/2);
            x = dx + ball.getWidth()/2;
            ball.setAngle((Math.PI - ball.getAngle()));
        }
        // right wall
        if (x + ball.getWidth()/2 > totalWidth) {
            int dx = Math.abs(totalWidth - (x + ball.getWidth()/2));
            x = totalWidth - (dx + ball.getWidth()/2);
            ball.setAngle((Math.PI - ball.getAngle()));
        }

        if (gameMode == BLUETOOTH_MODE) {
            // if we host the game send whether the ball is out or not
            if (bluetoothService.isServer()) {
                Point newPos = new Point(x, y);
                bluetoothService.sendIsBallOut(newRound);
                return newPos;
            } else {
            // if we don't host the game receive the ball position
                Point newPos = bluetoothService.getBallPosition();
                newPos.x = (int) (totalWidth - newPos.x * widthRatio);
                newPos.y = (int) (totalHeight - newPos.y * heightRatio);
                return newPos;
            }
        }
        return new Point(x, y);
	}


    public void draw() {
        draw(false);
    }
    /**
     * method to draw the game
     */
	public void draw(boolean drawWinnerScreen) {
		Canvas canvas = holder.lockCanvas();

		// "clears" the screen
		canvas.drawColor(Color.BLACK);

        drawPlayingField(canvas);

		player1.draw(canvas);

		if (gameMode != TRAINING_MODE) {
			player2.draw(canvas);
            drawScore(canvas);
		}

        if (gameMode >= TOURNAMENT_MODE) {
            drawPlayerNames(canvas);
        }

		ball.draw(canvas);

        if (!started) {
            drawStartScreen(canvas);
        }

        if (paused) {
            drawPauseScreen(canvas);
        }

        if(drawWinnerScreen) {
            drawWinnerScreen(canvas);
        }

		holder.unlockCanvasAndPost(canvas);
	}

    /**
     * method to draw the player names
     * @param canvas canvas to draw to
     */
    private void drawPlayerNames(Canvas canvas) {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(sizeProvider.getPlayerNameSize());
        paint.setColor(Color.parseColor("#ff31c2ff"));

        canvas.drawText(player1.getName(), totalWidth / 2, totalHeight - paint.descent(), paint);

        canvas.save();
        canvas.rotate(180, totalWidth/2, 0);
        canvas.drawText(player2.getName(), totalWidth/2, - paint.descent(), paint);
        canvas.restore();
    }

    /**
     * draws the start message
     * @param canvas canvas to draw to
     */
    private void drawStartScreen(Canvas canvas) {
        drawCenterText(canvas, gameActivity.getString(R.string.StartScreenString));
    }

    /**
     * draws the pause message
     * @param canvas canvas to draw to
     */
    private void drawPauseScreen(Canvas canvas) {
        drawCenterText(canvas, gameActivity.getString(R.string.PauseScreenString));
    }

    /**
     * draws text to the center of the screen
     * @param canvas canvas to draw to
     * @param text text to draw
     */
    private void drawCenterText(Canvas canvas, String text) {
        drawCenterText(canvas, text, sizeProvider.getMenuSize());
    }

    /**
     * draws text to the center of the screen
     * @param canvas to draw to
     * @param text text to draw
     * @param textSize text size of the {@code text}
     */
    private void drawCenterText(Canvas canvas, String text, int textSize) {
        drawCenterText(canvas, text, textSize, Color.GREEN);
    }

    /**
     * draws text to the center of the screen
     * @param canvas to draw to
     * @param text text to draw
     * @param textSize text size of the {@code text}
     * @param color text color of the {@code text}
     */
    private void drawCenterText(Canvas canvas, String text, int textSize, int color) {
        paint.setColor(color);
        paint.setTextSize(textSize);

        int x = totalWidth/2;
        int y = totalHeight/2;

        paint.setTextAlign(Paint.Align.CENTER);

        canvas.save();
        canvas.rotate(90, x, y);
        canvas.drawText(text, x, y, paint);
        canvas.restore();
    }

    /**
     * draws the playing field
     * @param canvas canvas to draw to
     */
    private void drawPlayingField(Canvas canvas) {

		paint.setColor(Color.WHITE);

		if (gameMode == TRAINING_MODE) {

            topWall = new Rect(0,
							totalHeight/2 - sizeProvider.getWallThicknessTraining() - sizeProvider.getBallSize()/2,
							totalWidth,
							totalHeight/2 - sizeProvider.getBallSize()/2);
			canvas.drawRect(topWall, paint);
		} else {
            // set special Paint settings for dashed middle-line
			paint.setStyle(Style.STROKE);
			paint.setPathEffect(new DashPathEffect(new float[] {10,10}, 5));
			canvas.drawLine(0, totalHeight/2, totalWidth, totalHeight/2, paint);
            // unset special Paint settings
            paint.setStyle(Style.FILL);
            paint.setPathEffect(null);
		}
	}

    /**
     * draws winner message
     */
    private void drawWinnerScreen(Canvas canvas) {

        String winningPlayer = (checkForWinner() == 0 ? player1.getName() : player2.getName());

        drawCenterText(canvas, winningPlayer + " " + gameActivity.getString(R.string.WinScreenString), sizeProvider.getPlayerNameSize());
    }

    /**
     * draws connection lost message
     */
    private void drawConnectionLost(){
        Canvas canvas = holder.lockCanvas();

        drawCenterText(canvas, gameActivity.getString(R.string.bluetoothConnectionLost), sizeProvider.getPlayerNameSize(), Color.RED);

        holder.unlockCanvasAndPost(canvas);
    }

    /**
     * draws score
     * @param canvas
     */
    private void drawScore(Canvas canvas) {

        int x = totalWidth/5 * 4;

        int yp1 = totalHeight/2 + totalHeight/20;
        int yp2 = totalHeight/2 - totalHeight/20;

        paint.setColor(Color.YELLOW);
        paint.setTextSize(sizeProvider.getScoreSize());

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.save();
        canvas.rotate(90, x, yp1);
        canvas.drawText("" + player1.getScore(), x, yp1, paint);
        canvas.restore();

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.save();
        canvas.rotate(90, x, yp2);
        canvas.drawText("" + player2.getScore(), x, yp2, paint);
        canvas.restore();
    }

    /**
     * starts the game thread
     */
    public void start() {
        running = true;
        started = true;
        gameLoop.start();
    }

    /**
     * stops the game thread
     */
    public void stop() {

        if (!destroyed) {
            running = false;
            destroyed = true;

            pauseSemaphore.release();

            try {
                aliveSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * pauses the game
     */
    public void pause() {

        if (!destroyed) {
            try {
                pauseSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            paused = true;
            draw();
        }
    }

    /**
     * resumes the game when paused
     */
    public void resume() {
        pauseSemaphore.release();
        paused = false;
    }

    /**
     * return the {@code running} flag
     * @return the {@code running} flag
     */
	public boolean isRunning() {
		return running;
	}

    /**
     * sets destroyed flag
     * @param isDestroyed
     */
    public void setDestroyed(boolean isDestroyed) {
        destroyed = isDestroyed;
    }

    /**
     * start a new round
     */
	public void newRound(){

        if (gameMode >= TOURNAMENT_MODE) {
            int winner = checkForWinner();

            if (winner < 0) {
                continueGame();
            } else {
//                drawWinnerScreen(winner);
                draw(true);
            }
        }else{
            continueGame();
        }
	}

    /**
     * prepare and start new round
     */
    private void continueGame() {
        resetBall();
        draw();

        winnable = true;
        destroyed = false;
        paused = false;

        gameLoop = new GameLoop(this, pauseSemaphore, aliveSemaphore);
        start();
    }

    /**
     * check if a player has won
     * @return the player who won. 0 = player1. 1 = player2. -1 = no winner
     */
    public int checkForWinner() {

        if (player1.getScore() == SCORE_LIMIT) {
            return 0;
        } else if (player2.getScore() == SCORE_LIMIT) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * resets the balls position and speed and sets the new angle
     */
    public void resetBall(){
		ball.setPosition(sizeProvider.getCenterPoint());
        ball.resetSpeed();

		if(serve == 1 || serve == 2){
            ball.serve(serve);
        } else {

            ball.randomAngle();
        }
	}

    /**
     * onTouchListener
     */
	@Override
	public boolean onTouch(View v, MotionEvent event) {

        // get all pointers and iterate over them
		for (int i = 0; i < event.getPointerCount(); i++) {
			int px = (int) event.getX(i);
			int py = (int) event.getY(i);

			Point touch = new Point(px, py);

            // check if touchEvent is valid player1 command
			if (player1.touchInTouchbox(touch)) {
				player1.goTo(px);
			}

            // in multiplayer check if touchEvent is valid player2 command
            if (gameMode != TRAINING_MODE && gameMode != BLUETOOTH_MODE) {
                if (player2.touchInTouchbox(touch)) {
                    player2.goTo(px);
                }
            }

            // handle touchEvents in controlTouchBox
            // start, pause, resume the game
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				if (controlTouchBox.contains(px, py)) {
					if (!running && !destroyed) {
						start();
					} else if (running && !destroyed && !paused) {
                        pause();
					} else if (running && !destroyed && paused) {
						resume();
					} else if (!running && !paused) {
                        gameActivity.endRound(checkForWinner());
                    } else if (!running && destroyed) {
                        gameActivity.finish();
                    }
				}
			}

		}
		return true;
	}

}
