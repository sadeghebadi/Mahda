/**
 * 
 */
package ir.rayacell.mahdaclient.services;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.motion_detection.data.GlobalData;
import ir.rayacell.mahdaclient.motion_detection.data.Preferences;
import ir.rayacell.mahdaclient.motion_detection.detection.AggregateLumaMotionDetection;
import ir.rayacell.mahdaclient.motion_detection.detection.IMotionDetection;
import ir.rayacell.mahdaclient.motion_detection.detection.LumaMotionDetection;
import ir.rayacell.mahdaclient.motion_detection.detection.RgbMotionDetection;
import ir.rayacell.mahdaclient.motion_detection.image.ImageProcessing;

import com.google.android.gms.internal.cm;
import com.google.android.gms.internal.de;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author rayacell May 5, 2015
 */
public class MotionCapturePreService implements SensorEventListener {
	public Intent intent;
	public static int count;
	public static int delay;
	public static String orderId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	public MotionCapturePreService(Intent intent) {
		create();
		// tick(orderTime)
		// ontick
		// onfinish pause()
		delay = intent.getExtras().getInt("delay");
		count = intent.getExtras().getInt("count");
		orderId = intent.getExtras().getString("orderId");
		this.intent = intent;

		// Container.activity.startService(intent);

		// get serfaces from starter and pass theases to service;
		//
	}

	// motion detected:: start MotionCapturePhotoService Intent(Delay , Count)
	// Start tick by orderTime

	private static final String TAG = "MotionCapturePreService";
	private static final AtomicBoolean computing = new AtomicBoolean(false);

	private static final float grav[] = new float[3]; // Gravity (a.k.a
														// accelerometer data)
	private static final float mag[] = new float[3]; // Magnetic

	private static final float gravThreshold = 0.5f;
	private static final float magThreshold = 1.0f;

	private static SensorManager sensorMgr = null;
	private static List<Sensor> sensors = null;
	private static Sensor sensorGrav = null;
	private static Sensor sensorMag = null;

	private static float prevGrav = 0.0f;
	private static float prevMag = 0.0f;

	// merged
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private Camera camera = null;
	private static boolean inPreview = false;
	private static long mReferenceTime = 0;
	private static IMotionDetection detector = null;

	private static volatile AtomicBoolean processing = new AtomicBoolean(false);

	public void create() {
		start();
		// camera = Camera.open();
		// preview = MotionCapturePhotoStarter.mSurfaceView;
		preview = (SurfaceView) Container.activity.getWindow().getDecorView()
				.findViewById(R.id.sv_camera);
		previewHolder = preview.getHolder();
		camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		camera.setParameters(parameters);
		Camera.Parameters p = camera.getParameters();

		final List<Size> listSize = p.getSupportedPreviewSizes();
		Size mPreviewSize = listSize.get(1);
		p.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		p.setPreviewFormat(PixelFormat.YCbCr_420_SP);
		camera.setParameters(p);
		try {
			camera.setPreviewDisplay(previewHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();
		inPreview = true;
		// previewHolder.addCallback(surfaceCallback);
		// try {
		// camera.setPreviewDisplay(previewHolder);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		previewHolder.setSizeFromLayout();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				if (camera != null && previewCallback != null) {
					camera.setPreviewCallback(previewCallback);
				}

			}
		}, 3000);

		/*
		 * Camera.Parameters params = camera.getParameters();
		 * camera.setParameters(params); Camera.Parameters p =
		 * camera.getParameters();
		 * 
		 * final List<Size> listSize = p.getSupportedPreviewSizes(); Size
		 * mPreviewSize = listSize.get(1); Log.v(TAG, "use: width = " +
		 * mPreviewSize.width + " height = " + mPreviewSize.height); Log.d(TAG,
		 * "use: width = " + mPreviewSize.width + " height = " +
		 * mPreviewSize.height); p.setPreviewSize(mPreviewSize.width,
		 * mPreviewSize.height); p.setPreviewFormat(PixelFormat.YCbCr_420_SP);
		 * camera.setParameters(p);
		 */

		/*
		 * try { camera.setPreviewDisplay(previewHolder); camera.startPreview();
		 * } catch (IOException e) { Log.e(TAG, e.getMessage());
		 * e.printStackTrace(); }
		 */

		if (Preferences.USE_RGB) {
			detector = new RgbMotionDetection();
		} else if (Preferences.USE_LUMA) {
			detector = new LumaMotionDetection();
		} else {
			// Using State based (aggregate map)
			detector = new AggregateLumaMotionDetection();
		}
	}

	public void pause() {
		if (camera == null) {
			return;
		}
		camera.setPreviewCallback(null);
		if (inPreview)
			camera.stopPreview();
		inPreview = false;
		camera.release();
		camera = null;
	}

	private PreviewCallback previewCallback = new PreviewCallback() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onPreviewFrame(final byte[] data, Camera cam) {
			Log.d("onPreviewFrame", "onPreviewFrame Created ????");

			if (data == null)
				return;
			final Camera.Size size = cam.getParameters().getPreviewSize();
			if (size == null)
				return;

			if (!GlobalData.isPhoneInMotion()) {
				Log.d(TAG, "startThread");

				thread = new DetectionThread(data, size.width, size.height);
				thread.start();

			}
		}
	};
	public DetectionThread thread;
	private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {

				Log.d("surfaceCreated", "surface Created ????");
				camera.setPreviewDisplay(previewHolder);
				camera.setPreviewCallback(previewCallback);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.d("surfaceChanged", "surface surfaceChanged ????");

			// Camera.Size size = getBestPreviewSize(width, height, parameters);
			// if (size != null) {
			// parameters.setPreviewSize(size.width, size.height);
			// Log.d(TAG, "Using width=" + size.width + " height="
			// + size.height);
			// }

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// Ignore
		}
	};

	private static Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea)
						result = size;
				}
			}
		}

		return result;
	}

	private final class DetectionThread extends Thread {

		private byte[] data;
		private int width;
		private int height;

		public DetectionThread(byte[] data, int width, int height) {
			this.data = data;
			this.width = width;
			this.height = height;
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			if (!processing.compareAndSet(false, true))
				return;

			// Log.d(TAG, "BEGIN PROCESSING...");
			try {
				// Previous frame
				int[] pre = null;
				if (Preferences.SAVE_PREVIOUS)
					pre = detector.getPrevious();

				// Current frame (with changes)
				// long bConversion = System.currentTimeMillis();
				int[] img = null;
				if (Preferences.USE_RGB) {
					img = ImageProcessing.decodeYUV420SPtoRGB(data, width,
							height);
				} else {
					img = ImageProcessing.decodeYUV420SPtoLuma(data, width,
							height);
				}
				int[] org = null;
				if (Preferences.SAVE_ORIGINAL && img != null)
					org = img.clone();
				Log.d(TAG, "inThread");
				if (img != null && detector.detect(img, width, height)) {
					long now = System.currentTimeMillis();
					mReferenceTime = now;
					Log.d(TAG, "startThread detected");
					Intent phService = new Intent(Container.activity,
							MotionCapturePhotoService.class);
					Log.d("Start Capturing", "started by Motion detection");

					phService.putExtra("delay", delay);
					phService.putExtra("count", count);
					phService.putExtra("orderId", orderId);
					pause();
					Container.activity.startService(phService);
					// startservice
					// stop self() puase()

					// The delay is necessary to avoid taking a picture while in
					// the
					// middle of taking another. This problem can causes some
					// phones
					// to reboot.
					// long now = System.currentTimeMillis();
					// if (now > (mReferenceTime + Preferences.PICTURE_DELAY)) {
					// mReferenceTime = now;
					//
					// Bitmap previous = null;
					// if (Preferences.SAVE_PREVIOUS && pre != null) {
					// if (Preferences.USE_RGB)
					// previous = ImageProcessing.rgbToBitmap(pre,
					// width, height);
					// else
					// previous = ImageProcessing.lumaToGreyscale(pre,
					// width, height);
					// }
					//
					// Bitmap original = null;
					// if (Preferences.SAVE_ORIGINAL && org != null) {
					// if (Preferences.USE_RGB)
					// original = ImageProcessing.rgbToBitmap(org,
					// width, height);
					// else
					// original = ImageProcessing.lumaToGreyscale(org,
					// width, height);
					// }
					//
					// Bitmap bitmap = null;
					// if (Preferences.SAVE_CHANGES) {
					// if (Preferences.USE_RGB)
					// bitmap = ImageProcessing.rgbToBitmap(img,
					// width, height);
					// else
					// bitmap = ImageProcessing.lumaToGreyscale(img,
					// width, height);
					// }
					//
					// Log.i(TAG, "Saving.. previous=" + previous
					// + " original=" + original + " bitmap=" + bitmap);
					// Looper.prepare();
					// new SavePhotoTask().execute(previous, original, bitmap);
					// } else {
					// }
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				processing.set(false);
			}
			// Log.d(TAG, "END PROCESSING...");

			processing.set(false);
		}
	};

	public void start() {

		try {
			sensorMgr = (SensorManager) Container.activity
					.getSystemService(Container.activity.SENSOR_SERVICE);

			sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
			if (sensors.size() > 0)
				sensorGrav = sensors.get(0);

			sensors = sensorMgr.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
			if (sensors.size() > 0)
				sensorMag = sensors.get(0);

			sensorMgr.registerListener(this, sensorGrav,
					SensorManager.SENSOR_DELAY_NORMAL);
			sensorMgr.registerListener(this, sensorMag,
					SensorManager.SENSOR_DELAY_NORMAL);
		} catch (Exception ex1) {
			try {
				if (sensorMgr != null) {
					sensorMgr.unregisterListener(this, sensorGrav);
					sensorMgr.unregisterListener(this, sensorMag);
					sensorMgr = null;
				}
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
	}

	public void stop() {

		try {
			try {
				sensorMgr.unregisterListener(this, sensorGrav);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				sensorMgr.unregisterListener(this, sensorMag);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			sensorMgr = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onSensorChanged(SensorEvent evt) {
		if (!computing.compareAndSet(false, true))
			return;

		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			grav[0] = evt.values[0];
			grav[1] = evt.values[1];
			grav[2] = evt.values[2];
		} else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			mag[0] = evt.values[0];
			mag[1] = evt.values[1];
			mag[2] = evt.values[2];
		}

		float gravity = grav[0] + grav[1] + grav[2];
		float magnetic = mag[0] + mag[1] + mag[2];

		float gravDiff = Math.abs(gravity - prevGrav);
		float magDiff = Math.abs(magnetic - prevMag);
		// Log.i(TAG, "gravDiff="+gravDiff+" magDiff="+magDiff);

		if ((Float.compare(prevGrav, 0.0f) != 0 && Float.compare(prevMag, 0.0f) != 0)
				&& (gravDiff > gravThreshold || magDiff > magThreshold)) {
			GlobalData.setPhoneInMotion(true);
		} else {
			GlobalData.setPhoneInMotion(false);
		}

		prevGrav = gravity;
		prevMag = magnetic;

		computing.set(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (sensor == null)
			throw new NullPointerException();

		if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
				&& accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
			Log.e(TAG, "Compass data unreliable");
		}
	}

}
