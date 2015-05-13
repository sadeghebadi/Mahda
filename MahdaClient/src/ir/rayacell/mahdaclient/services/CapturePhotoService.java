package ir.rayacell.mahdaclient.services;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.model.Frame;
import ir.rayacell.mahdaclient.model.Orders;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CapturePhotoService extends Service {
	private static final String TAG = "RecorderService";
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private static Camera mServiceCamera;
	private boolean mRecordingStatus;
	private MediaRecorder mMediaRecorder;
	private File mVideoFile;
	// private int duration;
	private String mCurrentVideoPath;
	private OrderDao dao;
	private String orderId;
	private Orders order;
	private int count;
	private int delay;
	public static final String DEFAULT_STORAGE_LOCATION_TMP = Environment
			.getExternalStorageDirectory() + "/tmp";
	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	@Override
	public void onCreate() {
		mRecordingStatus = false;
		mServiceCamera = CapturePhotoStarter.mCamera;
		mSurfaceView = CapturePhotoStarter.mSurfaceView;
		mSurfaceHolder = CapturePhotoStarter.mSurfaceHolder;

		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	int totalSecond = 0;
	private int pictureNum = 0;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		delay = intent.getExtras().getInt(
				App.getContext().getResources().getString(R.string.delay));
		count = intent.getExtras().getInt(
				App.getContext().getResources().getString(R.string.count));
		totalSecond = count * delay+1;
		orderId = intent.getExtras().getString("orderId");
		if (mRecordingStatus == false) {
			startRecording();
		}

		return START_STICKY;
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
			try{
				
			mServiceCamera = Camera.open();
			}catch (Exception e){
				return false;
			}
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
			mMediaRecorder.setProfile(CamcorderProfile
					.get(CamcorderProfile.QUALITY_720P));
			mMediaRecorder.setVideoFrameRate(30);
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
			dao = new OrderDao(App.getContext());
			order = findOrder(dao, orderId);
			order.status = "3";
			dao.update(Long.parseLong(orderId), order);
			new CountDownTimer(totalSecond * 1000, totalSecond * 1000) {

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

	private int getVideoDuration(String s_duration) {
		int duration = 0;
		try {
			duration = Integer.parseInt(s_duration); // in millisecs
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		duration *= 1000; // in microsecs
		return duration;
	}

	private void saveVideo() {
		String formattedFileCount;
		FileOutputStream fos;
		BufferedOutputStream bos;

//		NumberFormat fileCountFormatter = new DecimalFormat("00000");
//		int fileCount = 0;
		File jpegFile;

//		Bitmap lastbitmap = null;

		MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
		mRetriever.setDataSource(Environment.getExternalStorageDirectory()
				.getPath() + "/tmp/tmp.mp4");
		String lengthMsStr = mRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		int duration = getVideoDuration(lengthMsStr);

		ArrayList<Frame> frames = new ArrayList<Frame>();

		final long lenMs = Long.parseLong(lengthMsStr);
		int pace = 30;
		int j = 0;
		int delta_time = delay * 1000000;
		for (int i = delta_time ; i <= duration; i += delta_time) {
			Bitmap frame_orig = mRetriever.getFrameAtTime(i,
					mRetriever.OPTION_CLOSEST);
			if (frame_orig == null) {
				// setProgress(i, duration);
				continue;
			}
			pictureNum ++;
			Format dateInFilename = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

			Frame frame = new Frame();
			frame.setBm(frame_orig);
			frame.setTime(i);
			frames.add(frame);
			String filename = String.format("MAHDA_" + "PHOTO_"
					+ dateInFilename.format(new Date()));
			if (count > 0)
				filename += ("_No." + pictureNum);
			filename += ".jpg";
			String path = DEFAULT_STORAGE_LOCATION + File.separator + filename;
			
//			jpegFile = new File(Environment.getExternalStorageDirectory()
//					.getPath() + "/tmp/frame_" + i	 + ".jpg");
//			fileCount++;
			try {
				fos = new FileOutputStream(path);
				bos = new BufferedOutputStream(fos);
				frame.getBm().compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
//				SimpleCrypto crypto = new SimpleCrypto();
//				crypto.encryptFile(new File(path), new File(path+".enc"),Utils.getIMEI());
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
//			} catch (InvalidKeyException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			}

		}
		// return frames;
	}

	// for (int i = 0; i <= lenMs; i += pace) {
	// bArray.add(mRetriever.getFrameAtTime(i*1000,
	// MediaMetadataRetriever.OPTION_CLOSEST));
	// formattedFileCount = fileCountFormatter.format(fileCount);
	// lastbitmap = bArray.get(j);
	// j++;
	// // image is the bitmap
	// File dir = new File(DEFAULT_STORAGE_LOCATION);
	//
	// new File(dir,"frame_" + formattedFileCount + ".jpg");
	// jpegFile = new File(Environment.getExternalStorageDirectory()
	// .getPath() + "/tmp/frame_" + formattedFileCount + ".jpg");
	// try {
	// jpegFile.createNewFile();
	// } catch (IOException e1) {
	// }
	// fileCount++;
	// try {
	// fos = new FileOutputStream(jpegFile);
	// bos = new BufferedOutputStream(fos);
	// lastbitmap.compress(Bitmap.CompressFormat.JPEG, 15, bos);
	// bos.flush();
	// bos.close();
	// } catch (FileNotFoundException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }

	// }

	public void stopRecording() {
		order.status = "4";
		dao.update(Long.parseLong(orderId), order);
		Toast.makeText(getBaseContext(), "Recording Stopped",
				Toast.LENGTH_SHORT).show();
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
		saveVideo();
		deleteVideoFile();

	}

	// public void saveFrames(ArrayList<Bitmap> saveBitmapList) throws
	// IOException {
	// Random r = new Random();
	// int folder_id = r.nextInt(1000) + 1;
	//
	// String folder = Environment.getExternalStorageDirectory()
	// + "/videos/frames/" + folder_id + "/";
	// File saveFolder = new File(folder);
	// if (!saveFolder.exists()) {
	// saveFolder.mkdirs();
	// }
	//
	// int i = 1;
	// for (Bitmap b : saveBitmapList) {
	// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	// b.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
	//
	// File f = new File(saveFolder, ("frame" + i + ".jpg"));
	//
	// f.createNewFile();
	//
	// FileOutputStream fo = new FileOutputStream(f);
	// fo.write(bytes.toByteArray());
	//
	// fo.flush();
	// fo.close();
	//
	// i++;
	// }
	// Toast.makeText(getApplicationContext(), "Folder id : " + folder_id,
	// Toast.LENGTH_LONG).show();
	//
	// }

	private void deleteVideoFile() {
		File dir = new File(DEFAULT_STORAGE_LOCATION_TMP);
		String videoFileName = "tmp";
		new File(dir, videoFileName + ".mp4").delete();
	}

	private File createVideoFile() throws IOException {
		// Create an image file name
		File dir = new File(DEFAULT_STORAGE_LOCATION_TMP);

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

		// String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
		// .format(new Date());
		String videoFileName = "tmp";
		// File storageDir = Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		// File video = File.createTempFile(videoFileName, /* prefix */
		// ".mp4", /* suffix */
		// dir /* directory */
		// );

		return new File(dir, videoFileName + ".mp4");
		/* directory */
		// Save a file: path for use with ACTION_VIEW intents
		// mCurrentVideoPath = "file:" + video.getAbsolutePath();
		// return video;
	}

	@Override
	public void onDestroy() {
		stopRecording();
		mRecordingStatus = false;

		super.onDestroy();
	}
}
