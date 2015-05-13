package ir.rayacell.mahdaclient.services;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.model.Orders;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
@SuppressWarnings("deprecation")
public class MotionVideoRecorderService extends Service {
	private static final String TAG = "RecorderService";
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private static Camera mServiceCamera;
	private boolean mRecordingStatus;
	private MediaRecorder mMediaRecorder;
	private File mVideoFile;
	private int duration;
	private int quality;
	private String mCurrentVideoPath;
//	private OrderDao dao;
//	private String orderId;
	private Orders order;
	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	@Override
	public void onCreate() {
		mRecordingStatus = false;
		mServiceCamera = MotionVideoRecordStarter.mCamera;
		mSurfaceView = MotionVideoRecordStarter.mSurfaceView;
		mSurfaceHolder = MotionVideoRecordStarter.mSurfaceHolder;

		super.onCreate();
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
		quality = intent.getExtras().getInt(
				App.getContext().getResources().getString(R.string.quality));
//		orderId = intent.getExtras().getString("orderId");
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

	private Orders findOrder(OrderDao dao, String orderId2) {
		Orders order = dao.read(Long.parseLong(orderId2));

		return order == null ? null : order;
	}

	
	public boolean startRecording() {
		try {
			try {
				mVideoFile = createVideoFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Toast.makeText(getBaseContext(), "Recording Started",
					Toast.LENGTH_SHORT).show();

			mServiceCamera = Camera.open();
			Camera.Parameters params = mServiceCamera.getParameters();
			mServiceCamera.setParameters(params);
			Camera.Parameters p = mServiceCamera.getParameters();

			final List<Size> listSize = p.getSupportedPreviewSizes();
			Size mPreviewSize = listSize.get(1);
			Log.v(TAG, "use: width = " + mPreviewSize.width + " height = "
					+ mPreviewSize.height);
			Log.d(TAG, "use: width = " + mPreviewSize.width + " height = "
					+ mPreviewSize.height);
			p.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			p.setPreviewFormat(PixelFormat.YCbCr_420_SP);
			mServiceCamera.setParameters(p);

			try {
				mServiceCamera.setPreviewDisplay(mSurfaceHolder);
				mServiceCamera.startPreview();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}

			mServiceCamera.unlock();

			mMediaRecorder = new MediaRecorder();
			mMediaRecorder.setCamera(mServiceCamera);
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
			mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			switch (quality) {	
			case 0:
				mMediaRecorder.setProfile(CamcorderProfile
						.get(1));
				break;
			case 1:
				mMediaRecorder.setProfile(CamcorderProfile
						.get(CamcorderProfile.QUALITY_480P));
				break;
			case 2:
				mMediaRecorder.setProfile(CamcorderProfile
						.get(CamcorderProfile.QUALITY_LOW));
				break;
			}
			// .get(CamcorderProfile.QUALITY_1080P));
			mMediaRecorder.setOutputFile(mVideoFile.getAbsolutePath());

			// mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			// mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			// mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
			// mMediaRecorder.setVideoFrameRate(30);

			// mMediaRecorder.setVideoSize(mPreviewSize.width,
			// mPreviewSize.height);
			mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

			mMediaRecorder.prepare();
			mMediaRecorder.start();

			mRecordingStatus = true;
//			dao = new OrderDao(App.getContext());
//			order = findOrder(dao, orderId);
//			order.status = "3";
//			dao.update(Long.parseLong(orderId), order);
			new CountDownTimer(duration * 60 * 1000, duration * 60 * 1000) {

				@Override
				public void onFinish() {

					stopSelf();
				}

				@Override
				public void onTick(long arg0) {
				}

			}.start();

			return true;
		} catch (IllegalStateException e) {
			Log.d(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public void stopRecording() {
//		order.status = "4";
//		dao.update(Long.parseLong(orderId), order);
		// Toast.makeText(getBaseContext(), "Recording Stopped",
		// Toast.LENGTH_SHORT).show();
		try {
			mServiceCamera.reconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediaRecorder.stop();
		mMediaRecorder.reset();

		mServiceCamera.stopPreview();
		mMediaRecorder.release();

		mServiceCamera.release();
		mServiceCamera = null;
		
		if(MotionVideoRecorderTimerManager.pService == null)
			return;
		MotionVideoRecorderTimerManager.pService.create();
	}

	private File createVideoFile() throws IOException {
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

		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
				.format(new Date());
		String videoFileName = "MAHDA_" + "MOTIONVideo_" + timeStamp + "_NO"+MotionVideoRecorderTimerManager.countViD;
		MotionVideoRecorderTimerManager.countViD++;
		// File storageDir = Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		// File video = File.createTempFile(videoFileName, /* prefix */
		// ".mp4", /* suffix */
		// dir /* directory */
		// );

		try {
			return new File(dir ,videoFileName +/* prefix */
					".mp4" /* suffix */
					 /* directory */
			);
		} catch (Exception e) {
			return null;
		}
		// Save a file: path for use with ACTION_VIEW intents
		// mCurrentVideoPath = "file:" + video.getAbsolutePath();
		// return video;
	}
}
