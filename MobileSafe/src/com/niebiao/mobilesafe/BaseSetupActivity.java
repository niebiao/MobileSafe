package com.niebiao.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity{
	private GestureDetector detector;
	protected SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		detector = new GestureDetector(BaseSetupActivity.this, new SimpleOnGestureListener() {

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if ((e2.getRawX() - e1.getRawX())> 200) {
					//访问上一页，手指从左往右
					showBack();
					return true;
					
				}
				if ((e1.getRawX() - e2.getRawX())> 200) {
					//访问下一页
					showNext();
					return true;
				}
//				//屏蔽横竖滑动
//				if (Math.abs(e2.getRawY()-e1.getRawY())>100) {
//					Toast.makeText(BaseSetupActivity.this, "请水平滑动", 0).show();
//					return true;
//				}
				if(Math.abs((e2.getRawY() - e1.getRawY())) > 100){
					Toast.makeText(getApplicationContext(), "请水平滑动", 0).show();
					
					return true;
				}
				//屏蔽x轴滑动很慢，如在裤兜里被滑动
				if (Math.abs(velocityX)<200) {
					Toast.makeText(BaseSetupActivity.this, "滑动t太慢", 0).show();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});
		
	}
	public abstract void showNext();
//	public void next(View view) {
//		showNext();
//	}

	public abstract void showBack();
//	public void showBack(View view) {
//		showBack();
//	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
}
