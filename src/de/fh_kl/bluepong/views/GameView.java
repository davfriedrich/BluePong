package de.fh_kl.bluepong.views;

import de.fh_kl.bluepong.drawables.Paddle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView{

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Canvas canvas = holder.lockCanvas();
				onDraw(canvas);
				holder.unlockCanvasAndPost(canvas);
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private SurfaceHolder holder;
	
//	public GameView(Context context) {
//		super(context);
//
//		holder = getHolder();
//		holder.addCallback(new SurfaceHolder.Callback() {
//			
//			@Override
//			public void surfaceDestroyed(SurfaceHolder holder) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void surfaceCreated(SurfaceHolder holder) {
//				Canvas canvas = holder.lockCanvas();
//				draw(canvas);
//				holder.unlockCanvasAndPost(canvas);
//				
//			}
//			
//			@Override
//			public void surfaceChanged(SurfaceHolder holder, int format, int width,
//					int height) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	}
	
	@Override
	protected void onDraw(Canvas canvas) {
    
		canvas.drawColor(Color.CYAN);
	}


}
