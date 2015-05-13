package ir.rayacell.mahdaclient;

import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.Manager;
import ir.rayacell.mahdaclient.manager.MobileData;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.services.TaskChecker;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String text;

	// public boolean connect() {
	// System.out.println("ip:" + NetworkManager.getIpAddress());
	// Intent server_intent = new Intent(this, ServerService.class);
	// startService(server_intent);
	// return false;
	// }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new ServerInit().init();
		Container.activity = this;

		Window wind;
		wind = this.getWindow();
		wind.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		wind.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		wind.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
		// keep screen on

		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "INFO");
		wl.acquire();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		KeyguardLock kl = km.newKeyguardLock("name");
		kl.disableKeyguard();
		// ////////////

//		Manager.initialize();
//		Container.getProviderManager().setSmsProvider();
//		final EditText et_phonenumber = (EditText) findViewById(R.id.et_call_number);
//		Button btn_call = (Button) findViewById(R.id.btn_call);
//		btn_call.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View arg0) {
//				Container.getCallServerExecuter().startCall(
//						et_phonenumber.getText().toString());
//			}
//		});
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread paramThread,
					Throwable paramThrowable) {

				Intent in = new Intent(MainActivity.this, MainActivity.class);
				startActivity(in);
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
//		final Button btn_airplanemode = (Button) findViewById(R.id.btn_airplane_mode);
//		btn_airplanemode.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View arg0) {
//				String onbutton = Container.getAirplaneModeExecuter()
//						.onAirPlanePressed();
//				btn_airplanemode.setText(onbutton);
//			}
//		});
//
//		// final ImageView iv_photo = (ImageView) findViewById(R.id.iv_photos);
//		Button btn_photo = (Button) findViewById(R.id.btn_take_photo);
//		btn_photo.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View arg0) {
//				// Container.getPhotoCaptureExecuter().takePhoto();
//			}
//		});

		// ///////////////////////////////////
		// //////////////////////////////////
		// record voice
		// ///////////////////////////////////
		// //////////////////////////////////

		// final Button btn_audiorecord = (Button)
		// findViewById(R.id.btn_record_audio);
		// btn_audiorecord.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View arg0) {
		// String buttonname = Container.getVoiceRecordExecuter()
		// .onRecord();
		// btn_audiorecord.setText(buttonname);
		// }
		// });

		// final Button btn_videorecord = (Button)
		// findViewById(R.id.btn_record_video);
		// btn_videorecord.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View arg0) {
		// String buttonname = Container.getVideoRecorderExecuter()
		// .onRecord();
		// btn_videorecord.setText(buttonname);
		// }
		// });

//		System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
//		TextView tv_ip = (TextView) findViewById(R.id.textView1);
//		tv_ip.setText(NetworkManager.getIpAddress());
		startService(new Intent(this,TaskChecker.class));
//		final EditText et_dstip = (EditText) findViewById(R.id.et_ip);
//		Button btn_setip = (Button) findViewById(R.id.btn_setip);
//		btn_setip.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View arg0) {
//				NetworkManager.setIpAddress(et_dstip.getText().toString());
//				Container.getProviderManager().setConnection();
//			}
//		});
		// final EditText et_message = (EditText)
		// findViewById(R.id.et_send_message);
		// Button btn_send = (Button) findViewById(R.id.btn_send_message);
		// btn_send.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View arg0) {
		// Command command = new Command(1, "1234567890", App
		// .getContext().getResources()
		// .getString(R.string.command_voice_record), et_message
		// .getText().toString(), 0, 0, 0);
		// // Manager.soundRecord(command);
		// updateView(et_message.getText().toString());
		// }
		// });
		// BaseModel model = new Command(1, 111111111,
		// App.getContext().getResources().getString(R.string.command_voice_record),
		// "", 0, 0, 0);
		// Container.getProviderManager().mProvider.recieve(model);

		Utils.setProvider();
		//
//		SharedPreferences prefs = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		
//		String proString = prefs.getString("provider", 1+"");
//		/*if(proString.equals("1")){
//			Container.getProviderManager().setSmsProvider();
//		}else*/ if(proString.equals("2")){
//			Container.getProviderManager().setInternetProvider();
//		}else if(proString.equals("3")){
//			Container.getProviderManager().setXmpp3GProvider();
//		}else{
//			 Container.getProviderManager().setInternetProvider();
//
//		}
//		 Container.getProviderManager().setInternetProvider();
		// }
		// TelephonyManager tm = (TelephonyManager) App.getContext()
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// if (tm.getDataState() == tm.DATA_CONNECTED)
		// Container.getProviderManager().setXmpp3GProvider();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Container.getVoiceRecordExecuter().pause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void updateView(String s) {
		TextView tv = (TextView) findViewById(R.id.tv_show_recieved_message);
		tv.setMovementMethod(new ScrollingMovementMethod());
		text += s + "\n";
		tv.setText(text);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// if (requestCode == 1
	// && resultCode == RESULT_OK) {
	// // super.onActivityResult(requestCode, resultCode, data);
	// ImageView imv = (ImageView) findViewById(R.id.iv_photos);
	// Bundle extras = data.getExtras();
	// Bitmap imageBitmap = (Bitmap) extras.get("data");
	// imv.setImageBitmap(imageBitmap);
	// }
	// }
}
