package ir.rayacell.mahdaclient.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.model.Orders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CapturePhotoStarter extends BroadcastReceiver implements
		Camera.AutoFocusCallback, Camera.PictureCallback ,		SurfaceHolder.Callback {
	public static SurfaceView mSurfaceView;
	public static SurfaceHolder mSurfaceHolder;
	public static Camera mCamera;
	public boolean mStartRecording = true;

	private int delay;
	private int count;
	private SurfaceView cameraview;
	Handler handler = new Handler();
	List<Uri> pictureURIs;
	ARManager arManager;
	Uri pictureURI;
	Format dateInFilename = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	int[] maxCameraViewSize;
	int picturesToTake = 1;
	int currentPictureID = 0;
	int pictureTimer = 0;
	// private static Camera mCamera;
	//
	// public static void setCamera(){
	// if (mCamera !=) {
	//
	// }
	// }
	private OrderDao dao;
	private Orders order;

	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	private Orders findOrder(OrderDao dao, String orderId2) {
		Orders order = dao.read(Long.parseLong(orderId2));
		return order == null ? null : order;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("in receiver", "in receiver ))))))))))))))))))))");
		// Intent service = new Intent(Container.activity,
		// VoiceRecordService.class);
		delay = intent.getExtras().getInt(
				App.getContext().getResources().getString(R.string.delay));
		count = intent.getExtras().getInt(
				App.getContext().getResources().getString(R.string.count));

		String orderId = intent.getExtras().getString("orderId");
		dao = new OrderDao(App.getContext());
		order = findOrder(dao, orderId);
		order.status = "4";
		dao.update(Long.parseLong(orderId), order);
//		picturesToTake = count;
//		cameraview = (SurfaceView) Container.activity
//				.findViewById(R.id.sv_camera);
		
		
		View v = Container.getVideoRecorderView();
		mSurfaceView = (SurfaceView) v;
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		//test
		Intent service = new Intent(Container.activity,
				CapturePhotoService.class);
		service.putExtra(
				App.getContext().getResources().getString(R.string.delay),
				intent.getExtras().getInt("delay"));
		service.putExtra(
				App.getContext().getResources().getString(R.string.count),
				intent.getExtras().getInt("count"));
		service.putExtra(
				"orderId",
				intent.getExtras().getString("orderId"));
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Container.activity.startService(service);
		//test end
//		arManager = ARManager.createAndSetupCameraView(Container.activity,
//				cameraview, null);
//		arManager.setCameraOpenedCallback(new Runnable() {
//			public void run() {
//				cameraOpened();
//			}
//		});
//
//		arManager.setCameraStartedCallback(new Runnable() {
//			public void run() {
//				cameraPreviewStarted();
//			}
//		});
//		savePicture();
	}

	// callback from ARManager
	public void cameraOpened() {
		if (maxCameraViewSize == null) {
			maxCameraViewSize = new int[] { cameraview.getWidth(),
					cameraview.getHeight() };
		}
		arManager.setPreferredPreviewSize(maxCameraViewSize[0],
				maxCameraViewSize[1]);
		CameraUtils.setLargestCameraSize(arManager.getCamera());
		// statusTextField.setText(arManager.getCamera().getParameters().getPictureSize().width+"");
		// if (!flashButtonConfigured) {
		// configureFlashButton();
		// flashButtonConfigured = true;
		// }
	}

	public void cameraPreviewStarted() {
		// resize camera view to scaled size of preview image
		Camera.Size size = arManager.getCamera().getParameters()
				.getPreviewSize();
		int[] scaledWH = AndroidUtils.scaledWidthAndHeightToMaximum(size.width,
				size.height, maxCameraViewSize[0], maxCameraViewSize[1]);
		cameraview.setLayoutParams(new FrameLayout.LayoutParams(scaledWH[0],
				scaledWH[1], Gravity.CENTER));
	}

	public void savePicture() {
		if (this.delay == 0) {
			savePictureNow();
		} else {
			savePictureAfterDelay(this.delay);
		}
	}

	void savePictureAfterDelay(int delay) {
		pictureTimer = delay;
		// updateTimerMessage();
		currentPictureID++;
		handler.postDelayed(makeDecrementTimerFunction(currentPictureID), 1000);
		// beepType = RAND.nextInt(numBeepTypes);

		// updateButtons(false);
	}
	

	public void savePictureNow() {
		pictureURIs = new ArrayList<Uri>();
		// statusTextField.setText("Taking picture...");
		int b = CameraUtils.numberOfCameras();
		int a = arManager.getCameraId();
		
		if (arManager.getCamera() == null) {
			arManager.startCamera();
			arManager.getCamera().startPreview();
			arManager.getCamera().autoFocus(this);
		} else {
			arManager.getCamera().autoFocus(this);
		}
	
	}

	Runnable makeDecrementTimerFunction(final int pictureID) {
		return new Runnable() {
			public void run() {
				decrementTimer(pictureID);
			}
		};
	}

	public void decrementTimer(final int pictureID) {
		if (pictureID != this.currentPictureID) {
			return;
		}
		boolean takePicture = (pictureTimer == 1);
		--pictureTimer;
		if (takePicture) {
			savePictureNow();
			// playTimerBeep();
		} else if (pictureTimer > 0) {
			// updateTimerMessage();
			handler.postDelayed(makeDecrementTimerFunction(pictureID), 1000);
			if (pictureTimer < 3) {
				// playTimerBeep();
			}
		}
	}

	public void onAutoFocus(boolean success, Camera camera) {
		camera.takePicture(null, null, this);
	}

	public void onPictureTaken(byte[] data, final Camera camera) {
		try {
			camera.reconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		int pictureNum = (picturesToTake > 1) ? pictureURIs.size() + 1 : 0;
		pictureURI = saveImageData(data, pictureNum);
		// statusTextField.setText("");
		// updateButtons(true);
		camera.startPreview();

		if (pictureURI != null) {
			pictureURIs.add(pictureURI);
			if (pictureURIs.size() >= picturesToTake) {
				if (picturesToTake == 1) {
					// ViewImageActivity.startActivityWithImageURI(this,
					// pictureURI, "image/jpeg");
				} else {
					// ViewImageGridActivity.startActivityWithImageURIs(this,
					// pictureURIs);
				}
			} else {
				// OG Droid and possibly other phones often hang if we call
				// takePicture directly instead of autoFocus
				handler.postDelayed(new Runnable() {
					public void run() {
						camera.autoFocus(CapturePhotoStarter.this);
					}
				}, delay * 1000 /* 100 */);
			}
			// send the same NEW_PICTURE broadcast that the standard camera app
			// does
			try {
				Intent newPictureIntent = new Intent(
						"android.hardware.action.NEW_PICTURE");
				newPictureIntent.setDataAndType(pictureURI, "image/jpeg");
				Container.activity.sendBroadcast(newPictureIntent);
			} catch (Exception ex) {
				Log.e("CamTimer", "Error broadcasting new picture", ex);
			}
//			if (pictureNum == count) {
//				arManager.getCamera().setAutoFocusMoveCallback(null);
//				camera.stopPreview();
//				camera.release();
//			}
		}
	}

	Uri saveImageData(byte[] data, int pictureNum) {
		try {
			File dir = new File(DEFAULT_STORAGE_LOCATION);
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
			String filename = String.format("MAHDA_" + "PHOTO_"
					+ dateInFilename.format(new Date()));
			if (pictureNum > 0)
				filename += ("_No." + pictureNum);
			filename += ".jpg";

			String path = DEFAULT_STORAGE_LOCATION + File.separator + filename;
			FileOutputStream out = new FileOutputStream(path);
			out.write(data);
			out.close();

			// AndroidUtils.scanSavedMediaFile(this, path);
			// Toast.makeText(this, getString(R.string.savedPictureMessage),
			// Toast.LENGTH_SHORT).show();

			return Uri.fromFile(new File(path));
		} catch (Exception ex) {
			// Toast.makeText(this,
			// "Error saving picture: " + ex.getClass().getName(),
			// Toast.LENGTH_LONG).show();
			return null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
}