package ir.rayacell.mahdaclient.executer;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.param.CapturePhotoParam;
import ir.rayacell.mahdaclient.param.MotionCaptureParam;
import ir.rayacell.mahdaclient.services.CapturePhotoService;
import ir.rayacell.mahdaclient.services.CapturePhotoStarter;
import ir.rayacell.mahdaclient.services.MotionCapturePhotoService;
import ir.rayacell.mahdaclient.services.MotionCapturePhotoStarter;
import ir.rayacell.mahdaclient.services.VideoRecordStarter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class MotionCaptureExecuter /*implements OnActivityResultListener*/{
	
	private int delay;
	private int count;
	private String orderTime;

	@SuppressWarnings("deprecation")
	public MotionCaptureExecuter(MotionCaptureParam param , String orderId) {
		delay = param.getDelay();
		count = param.getCount();
		orderTime = param.getOrderTime();
		Map<String, Integer> mdatetime = new HashMap<String, Integer>();
		mdatetime = parseDateTime(param.getDate_and_time());
		@SuppressWarnings("deprecation")
		
		Date date = new Date(mdatetime.get("year") - 1900,
				mdatetime.get("month") - 1, mdatetime.get("day"),
				mdatetime.get("hour"), mdatetime.get("minute"));

		Intent intent = new Intent(App.getContext(), MotionCapturePhotoStarter.class);
		final int _id = (int) System.currentTimeMillis();

		//new
//		Intent intent = new Intent(App.getContext(), CapturePhotoService.class);
		intent.putExtra("delay", delay);
		intent.putExtra("count", count);
		intent.putExtra("orderTime", orderTime);
		intent.putExtra("orderId",orderId);
		Container.activity.startService(intent);
		PendingIntent pending_intent = PendingIntent.getBroadcast(
				App.getContext(),_id, intent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarm = (AlarmManager) App.getContext().getSystemService(
				Context.ALARM_SERVICE);

//		Log.d("calendar", date.getTime() + "#$$#$#$#$#$#$#$#$#$#$#$#$#$#");
//		Log.d("curent", System.currentTimeMillis() + "  %%%%% current");
//		Log.d("interval", date.getTime() - System.currentTimeMillis()
//				+ " %%%%%%%% interval");

		alarm.set(
				AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + date.getTime()
						- System.currentTimeMillis(), pending_intent);
		//end new
//		PendingIntent pending_intent = PendingIntent.getBroadcast(
//				App.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//		AlarmManager alarm = (AlarmManager) App.getContext().getSystemService(
//				Context.ALARM_SERVICE);
//		Log.d("calendar", date.getTime() + "#$$#$#$#$#$#$#$#$#$#$#$#$#$#");
//		Log.d("curent", System.currentTimeMillis() + "  %%%%% current");
//		Log.d("interval", date.getTime() - System.currentTimeMillis()
//				+ " %%%%%%%% interval");
//		alarm.set(
//				AlarmManager.ELAPSED_REALTIME_WAKEUP,
//				SystemClock.elapsedRealtime() + date.getTime()
//						- System.currentTimeMillis(), pending_intent);
	}

	private HashMap<String, Integer> parseDateTime(String datetime) {
		ArrayList<Integer> line_index = new ArrayList<Integer>();
		Map<String, Integer> m_d_t = new HashMap<String, Integer>();
		for (int i = 0; i < datetime.length(); i++) {
			if (datetime.charAt(i) == '-') {
				line_index.add(i);
			}
		}

		m_d_t.put("year",
				Integer.parseInt(datetime.substring(0, line_index.get(0))));
		m_d_t.put("month", Integer.parseInt(datetime.substring(
				line_index.get(0) + 1, line_index.get(1))));
		m_d_t.put("day", Integer.parseInt(datetime.substring(
				line_index.get(1) + 1, line_index.get(2))));
		m_d_t.put("hour", Integer.parseInt(datetime.substring(
				line_index.get(2) + 1, line_index.get(3))));
		m_d_t.put("minute",
				Integer.parseInt(datetime.substring(line_index.get(3) + 1)));

		return (HashMap<String, Integer>) m_d_t;
	}
//	static final int REQUEST_IMAGE_CAPTURE = 1;
//	private ImageView mImageView;
//	String mCurrentPhotoPath;
//	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
//			.getResources().getString(R.string.default_location);
//	private SurfaceView cameraview;
//	
//	private int delay;
//	private int count;
//	
//	public PhotoCaptureExecuter(/*View v*/BaseParam param) {
////		mImageView = (ImageView)v;
////		
//		delay = ((CapturePhotoParam)param).getDelay();
//		count = ((CapturePhotoParam)param).getCount();
//		cameraview = (SurfaceView)Container.activity.findViewById(R.id.sv_camera);
//		
//	}
//	
//	public void takePhoto(){
//		dispatchTakePictureIntent();
//		Toast.makeText(App.getContext(), "photo taken", Toast.LENGTH_SHORT).show();
//	}
//	
//	private void dispatchTakePictureIntent() {
//	    Intent takePictureIntent = new Intent(/*MediaStore.ACTION_IMAGE_CAPTURE*/"android.hardware.action.NEW_PICTURE");
////	    if (takePictureIntent.resolveActivity(Container.activity.getPackageManager()) != null) {
//	    	
//	    	File photofile = null;
//	    	try{
//	    		photofile = createImageFile();
//	    	}catch(IOException e){
//	    		
//	    	}
//	    	if (photofile != null) {
////				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
//////				Container.activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
////				Container.activity.sendBroadcast(takePictureIntent);
//	    		Intent newPictureIntent = new Intent(
//						"android.hardware.action.NEW_PICTURE");
//				newPictureIntent.setDataAndTypeAndNormalize(pictureURI, "image/jpeg");
//				Container.activity.sendBroadcast(newPictureIntent);
//			}
////	    }
//	}
//
////	public boolean onActivityResult(int requestCode, int resultCode	, Intent data) {
////		mImageView.setImageResource(R.drawable.ic_launcher);
////		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Container.activity.RESULT_OK) {
////	        Bundle extras = data.getExtras();
////	        Bitmap imageBitmap = (Bitmap) extras.get("data");
////	        mImageView.setImageBitmap(imageBitmap);
////	        return true;
////		}
////		
////		return false;
////	}
////	
//	
//
//	private File createImageFile() throws IOException {
//		
//		File dir = new File(DEFAULT_STORAGE_LOCATION);
//
//		// test dir for existence and writeability
//		if (!dir.exists()) {
//			try {
//				dir.mkdirs();
//			} catch (Exception e) {
//				return null;
//			}
//		} else {
//			if (!dir.canWrite()) {
//				return null;
//			}
//		}
//
//		// test size
//
//		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
//				.format(new Date());
//		String prefix = "MAHDA_"
//				+ "PHOTO_" + timeStamp + "_";
//		// create suffix based on format
//		String suffix = "";
//		suffix = ".jpg";
//
//		try {
//			return File.createTempFile(prefix, suffix, dir);
//		} catch (IOException e) {
//			return null;
//		}
//	    // Create an image file name
//
////		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
////	    String imageFileName = "JPEG_"+ "behzad_" + timeStamp + "_";
////	    File storageDir = new File("/sdcard/MAHDA");/*Environment.getExternalStoragePublicDirectory(
////	            Environment.DIRECTORY_PICTURES);*/
////	    
////	    File image = File.createTempFile(
////	        imageFileName,  /* prefix */
////	        ".jpg",         /* suffix */
////	        storageDir      /* directory */
////	    );
////
////	    // Save a file: path for use with ACTION_VIEW intents
////	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
////	    return image;
//	}
//	
}
