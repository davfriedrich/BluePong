package de.fh_kl.bluepong.game;

import de.fh_kl.bluepong.constants.Contants;
import de.fh_kl.bluepong.drawables.Paddle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameEngine implements OnTouchListener, Contants {
	
	private SurfaceHolder holder;
	
	Paddle p1;
	Paddle p2;
	
	
	public GameEngine(SurfaceHolder holder) {
		this.holder = holder;
		init();
	}
	
	private void init(){
		p1 = new Paddle(PADDLE_WIDTH, PADDLE_HEIGHT, 1, 0, 0);
		
		Canvas canvas = holder.lockCanvas();
		p1.draw(canvas);
		holder.unlockCanvasAndPost(canvas);
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		float x = event.getX(0);
		float y = event.getY(0);
		
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		
		Canvas canvas = holder.lockCanvas();
		canvas.drawCircle(x, y, 20, paint);
		holder.unlockCanvasAndPost(canvas);
		
		Log.v("gameengine", "" + x);
		return true;
	}

}
