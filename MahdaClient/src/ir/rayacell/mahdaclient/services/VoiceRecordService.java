package ir.rayacell.mahdaclient.services;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.model.Orders;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class VoiceRecordService extends Service {
	private static final String TAG = "RecorderService";
	private boolean mRecordingStatus;

	private String LOG_TAG = "AudioRecordTest";
	private MediaRecorder mRecorder;
	private File mVoiceFile;
	private boolean mStartRecording = true;
	String mCurrentVoicePath;
	private int duration;
	private String orderId;
	private OrderDao dao;
	private Orders order;
	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	@Override
	public void onCreate() {

		super.onCreate();
		// startRecording();
		Log.d("in voice record service",
				"voice record service /////////////////////////////");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		duration = intent.getExtras().getInt(
				App.getContext().getResources().getString(R.string.duration));
		orderId = intent.getExtras().getString("orderId");
		if (mRecordingStatus == false) {
			startRecording();
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		stopRecording();
		mRecordingStatus = false;

		super.onDestroy();
	}

	private void startRecording() {
		try {
			mVoiceFile = createVoiceFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mRecorder.setOutputFile(mVoiceFile.getAbsolutePath());
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
		try {
			mRecorder.start();
		} catch (Exception e) {

		}
		// update db command start;
		dao = new OrderDao(App.getContext());
		order = findOrder(dao, orderId);
		order.status = "3";
		dao.update(Long.parseLong(orderId), order);
		new CountDownTimer(duration * 60 * 1000, duration * 60 * 1000) {
			@Override
			public void onFinish() {
				// update db finish
				order.status = "4";
				dao.update(Long.parseLong(orderId), order);
				stopSelf();

			}

			@Override
			public void onTick(long arg0) {
			}
		}.start();
	}

	private Orders findOrder(OrderDao dao, String orderId2) {
		Orders order = dao.read(Long.parseLong(orderId2));
		return order == null ? null : order;
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		this.stopSelf();
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
	}

	private File createVoiceFile() throws IOException {

		// Create an image file name
		File dir = new File(DEFAULT_STORAGE_LOCATION);

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

		// Create an image file name

		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
				.format(new Date());
		String voiceFileName = "MAHDA_" + "Voice_" + timeStamp + "_";
		// File storageDir = Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		// File voice = File.createTempFile(voiceFileName, /* prefix */
		// ".3gp", /* suffix */
		// dir /* directory */
		// );

		// Save a file: path for use with ACTION_VIEW intents
		// mCurrentVoicePath = "file:" + voice.getAbsolutePath();
		try {
			return File.createTempFile(voiceFileName, /* prefix */
					".3gp", /* suffix */
					dir /* directory */
			);
		} catch (IOException e) {
			return null;
		}
	}

	public VoiceRecordService() {
		// mFileName =
		// Environment.getExternalStorageDirectory().getAbsolutePath();
		// mFileName += "/audiorecordtest.3gp";
	}

	// public String onRecord() {
	// if (mStartRecording) {
	// startRecording();
	// mStartRecording = !mStartRecording;
	// return "Stop recording";
	// }
	// stopRecording();
	// mStartRecording = !mStartRecording;
	// return "Start recording";
	// }
}
