package com.niebiao.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.crypto.MacSpi;
import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.PrivateCredentialPermission;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import com.niebiao.mobilesafe.utils.StreamTools;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;


public class SpalashAtivity extends Activity {
	 protected static final String TAG = "SpalashAtivity";
	 
	private TextView versionName;
	private String description;
	private String apkurl;
	private TextView updateInfo;
	private static final int  GO_HOME=0;
	protected static final int SHOW_UPDATE_DIALOG = 1;
	protected static final int URL_ERROR = 2;
	protected static final int IO_ERROR = 3;
	protected static final int JSON_ERROR = 4;
	SharedPreferences sp;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				Toast.makeText(SpalashAtivity.this, "目前为最新版本", 1).show();
				enterHome();
				break;
			case SHOW_UPDATE_DIALOG:
				Log.i(TAG, "显示升级对话框");
				showUpdateDialog();
				break;
			case URL_ERROR:
				enterHome();
				Toast.makeText(SpalashAtivity.this, "URL出错", 0).show();
				break;
			case IO_ERROR:
				enterHome();
				Toast.makeText(SpalashAtivity.this, "IO出错", 0).show();
				break;
			case JSON_ERROR:
				enterHome();
				Toast.makeText(SpalashAtivity.this, "JSON出错", 0).show();
				break;
			}
		}

	};

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalaictivity);
        TextView versionName = (TextView) findViewById(R.id.tv_splash_version);
        versionName.setText("版本:"+getVersionName());
       updateInfo = (TextView) findViewById(R.id.tv_update_info);
       sp=getSharedPreferences("config",MODE_PRIVATE);
		boolean isUpdate =sp.getBoolean("isUpdate", false);
		   //传建快捷图标
	       installShortCut();
		//拷贝数据库  db.dao.NumberAdderssQueryUtils
		copyDB();
		if(isUpdate){
        //检查升级
        checkUpdate();
		}else {
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
		}
        //跳转到其他界面之前，界面有朦胧的动画效果
        AlphaAnimation aa= new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(500);//朦胧0.5秒的时间
        findViewById(R.id.layout_spalash).startAnimation(aa);
        
     
    }
  //传建快捷图标
    private void installShortCut() {
    	if (sp.getBoolean("createshortcut", false)) {
			return;
		}
    	Editor editor=sp.edit();
    	Intent intent=new Intent();
    	intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    	intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机卫士");
    	intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
    	Intent shortcutIntent=new Intent();
    	shortcutIntent.setAction("android.intent.action.MAIN");
    	shortcutIntent.addCategory("android.intent.category.LAUNCHER");
    	shortcutIntent.setClassName(getPackageName(), "com.niebiao.mobilesafe.SpalashAtivity"); //入口activity
    	intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
    	sendBroadcast(intent);
    	editor.putBoolean("createshortcut", true);
    	editor.commit();
	}
	//拷贝数据库
    private void copyDB() {
    	InputStream is = null;
    	FileOutputStream fos=null;
    	try {
    		File file = new File(getFilesDir(),"address.db");
    		//假如文件拷贝过就不在拷贝
    		if (file.exists()&&file.length()>0) {
				Log.i(TAG, "文件已存在");
			}
    		else {
    			is = getAssets().open("address.db");
    			fos = new FileOutputStream(file);
    			byte[] buffer= new byte[1024];
    			int len=0;
    			while((len=is.read(buffer))!=-1){
    				fos.write(buffer, 0, len);
    			}
    			is.close();
				fos.close();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void checkUpdate() {
		new Thread(new Runnable() {
			

			@Override
			public void run() {
				//URL
					Message mes = Message.obtain();
					long startTime = System.currentTimeMillis();
						try {
							URL url  = new URL(getString(R.string.serverurl));
							HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("GET");
							connection.setConnectTimeout(4000);
							int response = connection.getResponseCode();
							if (response==200) {
								InputStream is =  connection.getInputStream();
								String result = StreamTools.readFromStream(is);
								Log.i(TAG,"联网成功了！"+ result);
								//成功获取信息，开始解析json
								JSONObject obj = new JSONObject(result);
								String verson;
									verson = (String) obj.get("verson");
									description = (String) obj.get("description");
									apkurl = (String) obj.get("apkurl");
								Log.i(TAG,"verson"+verson);
									//版本校验
									if (getVersionName().equals(verson)) {
										//没用版本跟新，进入主页面
										
											mes.what=GO_HOME;
									}else {
										//有版本跟新，弹出对话框
											mes.what=SHOW_UPDATE_DIALOG;
									}
							}
						} catch (MalformedURLException e) {
							mes.what=URL_ERROR;
							e.printStackTrace();
						}  catch (IOException e) {
							mes.what=IO_ERROR;
							e.printStackTrace();
						} catch (JSONException e) {
							mes.what=JSON_ERROR;
							e.printStackTrace();
						}finally{
							//跳转到其他界面的时间为2秒
							long endTime = System.currentTimeMillis();
							long dTime = endTime-startTime;
							if (dTime<2000) {
								try {
									Thread.sleep(2000-dTime);
									handler.sendMessage(mes);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						}
					}

		}).start();
		
	}
    private void showUpdateDialog() {
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("提示升级");
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				//当点击在其他非dialog的地方或返回键时
				enterHome();
				dialog.dismiss();
			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("马上升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载APK，替换安装
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {//sdk是否存在
					//afnal
					FinalHttp finalHttp = new FinalHttp();
					Log.i(TAG, "apkurl:"+apkurl);
					finalHttp.download(apkurl, 
							//存储路径
							Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk",
							new AjaxCallBack<File>() {
								
								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									t.printStackTrace();
									Toast.makeText(SpalashAtivity.this, "下载失败", 1).show();
									super.onFailure(t, errorNo, strMsg);
								}

								@Override
								public void onLoading(long count, long current) {
									super.onLoading(count, current);
									int pregress = (int) (current*100/count);
									updateInfo.setText("下载进度："+pregress+"%");
								}

								@Override
								public void onSuccess(File t) {
									super.onSuccess(t);
									installAPK(t);
								}

								private void installAPK(File t) {
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");        				//动作
									intent.addCategory("android.intent.category.DEFAULT");   //附加信息
									intent.setDataAndType(Uri.fromFile(t),                              // 数据类型
											"application/vnd.android.package-archive");
									startActivity(intent);                                                              //启动
								}
						
					});
				}else {
					Toast.makeText(SpalashAtivity.this, "对不起，请检查sdcar是否存在", 0).show();
					return;
				}
			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();
				
			}
		});
		builder.show();//很重要，不然不会显示
	} 
    

	private void enterHome() {
		Intent intent = new Intent(SpalashAtivity.this,HomeActivity.class);
		startActivity(intent);
		finish();//关闭当前页面
	}
	
    
	/*
     * 动态获取版本号
     */
    private String getVersionName (){
    	  PackageManager pm = getPackageManager();
    	  try {
			PackageInfo pi=pm.getPackageInfo(getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    }
  
}
