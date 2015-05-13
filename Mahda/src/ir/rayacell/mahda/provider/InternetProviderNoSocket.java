package ir.rayacell.mahda.provider;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.MainActivity;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.dao.OrderDao;
import ir.rayacell.mahda.fragment.DeviceInfoFragment;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.manager.OrderManager;
import ir.rayacell.mahda.model.Orders;
import ir.rayacell.mahda.param.BaseParam;
import ir.rayacell.mahda.security.EncryptDecrypt;
import ir.rayacell.mahda.services.ServerInit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.StringCallback;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;

public class InternetProviderNoSocket extends BaseProvider {
	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	public InternetProviderNoSocket(ProviderManager providerManager,
			MainActivity activity) {
		super(providerManager, activity);
//
//		IntentFilter mActionIntentFilter = new IntentFilter(
//				Constants.BROADCAST_ACTION);
//		ResponseReceiver mResponseReceiver = new ResponseReceiver();
//		LocalBroadcastManager.getInstance(activity).registerReceiver(
//				mResponseReceiver, mActionIntentFilter);
	}
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	@Override
	public boolean connect(BaseParam param) {
//		if(isMyServiceRunning(ir.rayacell.mahda.services.ServerService.class)){
//			Intent server_intent = new Intent(activity,
//					ir.rayacell.mahda.services.ServerService.class);
//			activity.stopService(server_intent);
//		}
//		System.out.println("ip:" + NetworkManager.getIpAddress());
//		Intent server_intent = new Intent(activity,
//				ir.rayacell.mahda.services.ServerService.class);
//		server_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		
//		activity.startService(server_intent);
		new ServerInit().init();

		return false;
	}

	@Override
	public synchronized boolean send(BaseParam param) {
		System.out.println(param.getCommand_type() + "  wifi  "
				+ "==============================");
		new ServerInit().init();
		MyClientTask myClientTask = new MyClientTask(NetworkManager.dstAddress,
				NetworkManager.dstPort,
				/*
				 * param.getCommand_type() + "     " + param.getPhone_number() +
				 * "     " +
				 *//*
					 * ((VoiceRecordParam) param).getDate_and_time() +
					 * " $$$$$$$$$$$$$$$$$$$$$$$$$$"
					 */
				(param.getCommand()));
		if (isValid(param)) {
			myClientTask.execute();

		} else if (OrderManager.checker(param.getCommand_type(), param
				.getCommand().split("\\*")[4]))
			myClientTask.execute();
		else {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(activity,
							"درحال حاضر نمیتوانید این فرمان را ارسال کنید",
							4000).show();
				}
			});
		}
		return false;
	}

	private boolean isValid(BaseParam param) {
		if (param.getCommand_type().equals("0")
				|| param.getCommand_type().equals("1")|| param.getCommand_type().equals("5")|| param.getCommand_type().equals("10")|| param.getCommand_type().equals("11")|| param.getCommand_type().equals("12")|| param.getCommand_type().equals("14")|| param.getCommand_type().equals("15")|| param.getCommand_type().equals("6")){
			return true;
		}
		return false;
	}

	@Override
	public void recieve(BaseParam param) {
		mProviderManager.recieve(param);

	}

//	public class ResponseReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Bundle bundle = intent.getExtras();
//			if (bundle != null) {
//				String response = bundle.getString(Constants.RESULT_KEY);
//				// Toast.makeText(App.getContext(), response + "!!!!!!!!!!!!!!",
//				// Toast.LENGTH_LONG).show();
//				Log.d("receiver", response + "!!!!!!!!!!!!!!");
//				// Container.getTestFragment().updateView(response);
//				BaseParam param = new BaseParam(0, null, null);
//				param.mCommand = response;
//				recieve(param);
//			}
//		}
//
//	}

	public class  MyClientTask extends AsyncTask<Void, Void, Void> {

		String dstAddress;
		int dstPort;
		String response = "";
		String msgToServer;
		public ProgressDialog mProgressDialog;

		private void getFile(String url, final String filename) {
			Container.BASEACTIVITY.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mProgressDialog = new ProgressDialog(Container.BASEACTIVITY);
					mProgressDialog.setMessage("در حال دریافت فایل ...");
					mProgressDialog
							.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					mProgressDialog.setCancelable(false);
				}
			});

			AsyncHttpGet ahg = new AsyncHttpGet(url);
			ahg.addHeader(
					"file",
					filename.substring(filename.lastIndexOf("/"),
							filename.length()));
			AsyncHttpClient.getDefaultInstance().executeFile(ahg, filename,
					new AsyncHttpClient.FileCallback() {

						@Override
						public void onProgress(AsyncHttpResponse response,
								final long downloaded, final long total) {
							Container.BASEACTIVITY
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											if (total == 0) {
												mProgressDialog.dismiss();
												return;

											}
											if (!mProgressDialog.isShowing()) {
												mProgressDialog.show();
											}
											mProgressDialog
													.setProgress((int) ((downloaded * 100) / total));
										}
									});
							super.onProgress(response, downloaded, total);
						}

						@Override
						public void onCompleted(Exception e,
								AsyncHttpResponse response, File result) {
							if (e != null) {
								e.printStackTrace();
								return;
							}
							Bitmap bitmap = BitmapFactory.decodeFile(filename);
							mProgressDialog.cancel();
							// result.delete();

							if (bitmap == null)
								return;
							BitmapDrawable bd = new BitmapDrawable(bitmap);
						}
					});
		}

		MyClientTask(String addr, int port, String msgTo) {
			dstAddress = addr;
			dstPort = port;
			msgToServer = msgTo;
		}

		// private void getDownload(Socket sock ,int port ,String filename ,
		// DataOutputStream dos , int size) throws Exception{
		// int bytesRead;
		// int current = 0;
		// FileOutputStream fos = null;
		// BufferedOutputStream bos = null;
		// try {
		// sock = new Socket(NetworkManager.dstAddress, port);
		// System.out.println("Connecting...");
		//
		// // receive file
		// byte [] mybytearray = new byte [size];
		// InputStream is = sock.getInputStream();
		// fos = new
		// FileOutputStream(Environment.getExternalStorageDirectory()+"/mahda/"+filename);
		// bos = new BufferedOutputStream(fos);
		// bytesRead = is.read(mybytearray,0,mybytearray.length);
		// current = bytesRead;
		//
		// do {
		// bytesRead =
		// is.read(mybytearray, current, (mybytearray.length-current));
		// if(bytesRead >= 0) current += bytesRead;
		// } while(bytesRead > -1);
		//
		// // bos.write(mybytearray, 0 , current);
		// bos.write(current);
		// bos.flush();
		// System.out.println("File " + filename
		// + " downloaded (" + current + " bytes read)");
		// }
		// finally {
		// if (fos != null) fos.close();
		// if (bos != null) bos.close();
		// if (sock != null) sock.close();
		// }
		// }
		private String[] parseMessage(String message) {
			String[] msg = message.split("\\*");
			return msg;
		}
		@Override
		protected synchronized Void doInBackground(Void... arg0) {
//			Socket socket = null;
//			DataOutputStream dataOutputStream = null;
//			DataInputStream dataInputStream = null;
			try {
//				socket = new Socket(dstAddress, dstPort);
//				dataOutputStream = new DataOutputStream(
//						socket.getOutputStream());
//				dataInputStream = new DataInputStream(socket.getInputStream());
				if (msgToServer != null) {
					String[] msg = parseMessage(msgToServer);
					if (Integer.parseInt(msg[2]) == 11) {
						try {
//							msgToServer = EncryptDecrypt.encrypt(msgToServer);
//							dataOutputStream.writeUTF(msgToServer);
							getFile("http://" + dstAddress + ":" + 5001 + "/",
									DEFAULT_STORAGE_LOCATION + "/"
											+ NetworkManager.clientPhoneNumber
											+ "/" + msg[4]);
						} catch (Exception e) {
						}
					} else {
						long id = createOrder(msgToServer);
						msgToServer = EncryptDecrypt.encrypt(msgToServer+ id);
						AsyncHttpGet ahg = new AsyncHttpGet("http://" + dstAddress + ":" + 5001 + "/execute");
						ahg.addHeader(
								"orderStr",msgToServer);
						AsyncHttpClient.getDefaultInstance().executeString(ahg, new StringCallback() {
							
							@Override
							public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
								try {
									response = EncryptDecrypt.decrypt(result);
								} catch (Exception e1) {
								}
								Container.BASEACTIVITY.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(Container.BASEACTIVITY,
												"دستور دریافت شد", 2000).show();
									}
								});
								updateOrder(response);
							}
						});
//						dataOutputStream.writeUTF(msgToServer );
						Container.BASEACTIVITY.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(Container.BASEACTIVITY,
										"دستور ارسال شد", 2000).show();
							}
						});

					}
				}

//				response = dataInputStream.readUTF();
				// TODO
				
			} catch (Exception e) {
				// e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
				// e.printStackTrace();
				response = "IOException: " + e.toString();
				Container.activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(DeviceInfoFragment.btn_get_status != null){
							DeviceInfoFragment.btn_get_status.setProgress(-1);
						}
						Toast.makeText(activity,
								"اینترنت مشتری به مشکل برخورده است.",
								Toast.LENGTH_LONG).show();
						DeviceInfoFragment.PROGRESSCONNECTION = -1;

					}
				});
			} finally {
//				if (socket != null) {
//					try {
//						socket.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//
//				if (dataOutputStream != null) {
//					try {
//						dataOutputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if (dataInputStream != null) {
//					try {
//						dataInputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
			}
			return null;
		}

		private long createOrder(String msg) {
			OrderDao dao = new OrderDao(App.getContext());
			String[] orderStr = msg.split("\\*");
			long id = 0;
			Orders order;
			if (orderStr[2].equals("1") || orderStr[2].equals("0")
					|| orderStr[2].equals("6") || orderStr[2].equals("11")
					|| orderStr[2].equals("10") || orderStr[2].equals("12") ||orderStr[2].equals("15")) {
				// order = new Orders(orderStr[2],orderStr[3],orderStr[4],new
				// Date(System.currentTimeMillis()).toGMTString() , 0);
			} else if (orderStr[2].equals("10")) {
				String date = orderStr[4];
				String[] darr = date.split("_");
				date = darr[2] + "-" + darr[3] + "-" + darr[4] + "-" + darr[5]
						+ "-" + darr[6];
				order = new Orders(orderStr[2], orderStr[3],
						NetworkManager.dstAddress, date, 0);
				id = dao.create(order);
			}else if(orderStr[2].equals("4")){
				Calendar c = Calendar.getInstance();
				System.out.println("Current time => " + c.getTime());

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
				String date = orderStr[4];
				order = new Orders(orderStr[2], orderStr[3],
						NetworkManager.dstAddress, date, Integer.parseInt(orderStr[5])*Integer.parseInt(orderStr[6])+"", 0);
				id = dao.create(order);
			} else {
				// Date cDate = new Date(System.currentTimeMillis());
				// String date =
				// cDate.getYear()+1900+"-"+cDate.getMonth()+1+"-"+cDate.getDay()+"-"+cDate.getHours()+"-"+cDate.getMinutes();
				Calendar c = Calendar.getInstance();
				System.out.println("Current time => " + c.getTime());

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
				String date = orderStr[4];
				order = new Orders(orderStr[2], orderStr[3],
						NetworkManager.dstAddress, date, orderStr[5], 0);
				id = dao.create(order);

			}
			return id;
		}

		private void updateOrder(String massage) {
			OrderDao dao = new OrderDao(App.getContext());
			long id = 0;
			try {
				id = Long.parseLong(massage);
			} catch (Exception e) {
			}

			Orders newObject = dao.read(id);
			if (newObject == null || newObject.type.equals("6")) {
				return;
			}
			newObject.status = 1;
			dao.update(id, newObject);
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.d("tag", response + "((((((((((((((((((((((((((((((((((((((");
			// Container.getTestFragment().updateView(response);
			super.onPostExecute(result);
		}
	}
}
