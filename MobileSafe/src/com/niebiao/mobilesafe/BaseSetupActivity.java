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
					//������һҳ����ָ��������
					showBack();
					return true;
					
				}
				if ((e1.getRawX() - e2.getRawX())> 200) {
					//������һҳ
					showNext();
					return true;
				}
//				//���κ�������
//				if (Math.abs(e2.getRawY()-e1.getRawY())>100) {
//					Toast.makeText(BaseSetupActivity.this, "��ˮƽ����", 0).show();
//					return true;
//				}
				if(Math.abs((e2.getRawY() - e1.getRawY())) > 100){
					Toast.makeText(getApplicationContext(), "��ˮƽ����", 0).show();
					
					return true;
				}
				//����x�Ử�����������ڿ㶵�ﱻ����
				if (Math.abs(velocityX)<200) {
					Toast.makeText(BaseSetupActivity.this, "����t̫��", 0).show();
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
