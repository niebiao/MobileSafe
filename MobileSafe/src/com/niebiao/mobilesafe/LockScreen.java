package com.niebiao.mobilesafe;

import com.niebiao.mobilesafe.receiver.MyAdmin;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


@SuppressLint("ShowToast")
public class LockScreen extends Activity {
	//设备策略服务
	private DevicePolicyManager dpm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        dpm=(DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        
        
    }
    public void lockscreen(View view){
    	ComponentName componentName= new ComponentName(this,MyAdmin.class);
    	if (dpm.isAdminActive(componentName)) {
    		dpm.lockNow();    
        	dpm.resetPassword("", 0);//空为 无密码
        	//清除SD卡数据
        //	dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        	//恢复出厂
        	//dpm.wipeData(0);
		}else {
			Toast.makeText(this, "请先点击激活安全权限按钮", 1).show();
		}
    	
    }
    //用代码开启管理员权限
    public void openAdmin(View view){
    	 // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器   
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);   
        //我yao激活谁
       ComponentName componentName= new ComponentName(this,MyAdmin.class);
      intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);   
        //描述(additional explanation)   
      intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "您必须开启此权限才可以使用一键锁屏");   
      startActivity(intent);   
    		
    }
    //卸载
    public void uninstall(View view){
    	ComponentName componentName= new ComponentName(this,MyAdmin.class);
    	dpm.removeActiveAdmin(componentName);
    	//卸载应用
//    	Intent intent = new Intent();
//    	intent.setAction("");
    	 Uri packageUri = Uri.parse("package:"+LockScreen.this.getPackageName());
         Intent intent = new Intent(Intent.ACTION_DELETE,packageUri);
         startActivity(intent);
    }
}
