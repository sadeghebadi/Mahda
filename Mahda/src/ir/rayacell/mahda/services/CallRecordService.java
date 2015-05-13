package ir.rayacell.mahda.services;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.manager.NetworkManager;

import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.os.IBinder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;

//import java.security.KeyPairGenerator;
//import java.security.KeyPair;
//import java.security.Key;

public class CallRecordService extends Service implements
		MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {
	private static final String TAG = "CallRecorder";

	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);
	private static final int RECORDING_NOTIFICATION_ID = 1;

	private MediaRecorder recorder = null;
	private boolean isRecording = false;
	private File recording = null;;

	/*
	 * private static void test() throws java.security.NoSuchAlgorithmException
	 * { KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
	 * kpg.initialize(2048); KeyPair kp = kpg.genKeyPair(); Key publicKey =
	 * kp.getPublic(); Key privateKey = kp.getPrivate(); }
	 */

	private File makeOutputFile(/* SharedPreferences prefs */) {
		File dir = new File(DEFAULT_STORAGE_LOCATION + "/"
				+ NetworkManager.clientPhoneNumber);

		// test dir for existence and writeability
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				return null;
			}
		} else {
			if (!dir.canWrite()) {
				return null;
			}
		}

		// test size

		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
				.format(new Date());
		String prefix = App.getContext().getResources()
				.getString(R.string.app_name)
				+ "_CallRecord_" + timeStamp;
		// create suffix based on format
		String suffix = "";
		suffix = ".3gpp";

		try {
			return File.createTempFile(prefix, suffix, dir);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		recorder = new MediaRecorder();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		if (isRecording)
			return;

		Context c = getApplicationContext();
		recording = makeOutputFile(/* prefs */);
		if (recording == null) {
			recorder = null;
			return; // return 0;
		}
		try {
			// These calls will throw exceptions unless you set the
			// android.permission.RECORD_AUDIO permission for your app
			recorder.reset();
			recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(recording.getAbsolutePath());

			recorder.setOnInfoListener(this);
			recorder.setOnErrorListener(this);

			try {
				recorder.prepare();
			} catch (java.io.IOException e) {
				recorder = null;
				return; // return 0; //START_STICKY;
			}
			recorder.start();
			isRecording = true;
		} catch (java.lang.Exception e) {
			recorder = null;
		}
		return; // return 0; //return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != recorder) {
			isRecording = false;
			recorder.release();
		}
	}

	// methods to handle binding the service
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return false;
	}

	@Override
	public void onRebind(Intent intent) {
	}

	// MediaRecorder.OnInfoListener
	@Override
	public void onInfo(MediaRecorder mr, int what, int extra) {
		isRecording = false;
	}

	// MediaRecorder.OnErrorListener
	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		isRecording = false;
		mr.release();
	}

}
