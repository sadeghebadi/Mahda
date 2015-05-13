package ir.rayacell.mahdaclient.manager;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.Direct3GModel;
import ir.rayacell.mahdaclient.model.Direct3GResponseModel;
import ir.rayacell.mahdaclient.model.DirectWIFIModel;
import ir.rayacell.mahdaclient.model.DirectWIFIResponseModel;
import ir.rayacell.mahdaclient.model.OrderStatusModel;
import ir.rayacell.mahdaclient.model.OrderStatusResponseModel;
import ir.rayacell.mahdaclient.model.PingModel;
import ir.rayacell.mahdaclient.model.PingResponseModel;
import ir.rayacell.mahdaclient.model.RestartModel;
import ir.rayacell.mahdaclient.model.RestartResponseModel;
import ir.rayacell.mahdaclient.model.ShowOlineMapResponceModel;
import ir.rayacell.mahdaclient.model.ShowOnlineMapModel;
import ir.rayacell.mahdaclient.model.StatusModel;
import ir.rayacell.mahdaclient.model.StatusResponceModel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Utils implements WifiApManager.WifiStateListener {

	public static final String END_SYMBOL = "\r\n";
	private static final String LOG_TAG = "WifiShareActivity";
	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);
	public static final String ACTION_UPDATE_RECEIVER = "action_update_receiver";
	public static final String EXTRA_DATA = "extra_data";

	private static WifiApManager mWifiApManager;

	private static boolean mWifiApEnable;

	public static BaseModel statusMaker(StatusModel model) {
		long command_id = model.getCommand_id();
		String commandType = model.getCommand_type();
		String phone_number = model.getPhone_number();
		String iMEI = Utils.getIMEI();
		String serial_number = Utils.getSimSerial();
		String latitude = "";
		String longitude = "";
		String battery_level = Utils.batteryLevel();
		String memory_total = Utils.TotalMemory();
		String memory_left = Utils.FreeMemory();
		int wifi_state = Utils.wifiStatus();
		String iPaddress = NetworkManager.getIpAddress();
		String time = model.getCurrent_time();
		setTime(time);
		BaseModel status_infomodel = new StatusResponceModel(command_id,
				phone_number, commandType, iMEI, serial_number, latitude,
				longitude, battery_level, memory_total, memory_left,
				wifi_state, iPaddress);

		return status_infomodel;
	}

	public static String getIMEI() {
		TelephonyManager mngr = (TelephonyManager) App.getContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mngr.getDeviceId();
		return imei;
	}

	public static String getSimSerial() {
		return android.os.Build.SERIAL;
	}

	/*************************************************************************************************
	 * Returns size in bytes.
	 * 
	 * If you need calculate external memory, change this: StatFs statFs = new
	 * StatFs(Environment.getRootDirectory().getAbsolutePath()); to this: StatFs
	 * statFs = new
	 * StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
	 **************************************************************************************************/
	static long GB = 1073741824;

	@SuppressLint("NewApi")
	public static String TotalMemory() {
		// StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
		// .getAbsolutePath());
		StatFs statFs = new StatFs(DEFAULT_STORAGE_LOCATION);

		long Total = statFs.getTotalBytes();
		String total = "" + (float) Total / GB;
		return total.substring(0, 5);
	}

	@SuppressLint("NewApi")
	public static String FreeMemory() {
		// StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
		// .getAbsolutePath());
		StatFs statFs = new StatFs(DEFAULT_STORAGE_LOCATION);

		long Free = statFs.getFreeBytes();
		String free = "" + (float) Free / GB;
		return free.substring(0, 5);
	}

	/**
	 ********************************************************************************************** 
	 * battery level
	 *********************************************************************************************/
	public static String batteryLevel() {
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = App.getContext().registerReceiver(null, ifilter);
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = level / (float) scale;
		String battery = "" + batteryPct;

		return battery;
	}

	public static int wifiStatus() {
		ConnectivityManager cm = (ConnectivityManager) App.getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return 1;
		} else {
			return 0;
		}
	}

	public static Location location;

	public static double[] getLocation2() {

		LocationManager locationManager;
		boolean isGPSEnabled;
		boolean isNetworkEnabled;
		boolean canGetLocation;
		try {
			locationManager = (LocationManager) App.getContext()
					.getSystemService(App.getContext().LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				canGetLocation = true;
				double latitude;
				double longitude;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, 1, 1,
							new LocationListener() {

								@Override
								public void onStatusChanged(String arg0,
										int arg1, Bundle arg2) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onProviderEnabled(String arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onProviderDisabled(String arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLocationChanged(Location arg0) {
									location = arg0;
								}
							});
					Log.d("Network", "Network Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, 1, 1,
								new LocationListener() {

									@Override
									public void onStatusChanged(String arg0,
											int arg1, Bundle arg2) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onProviderEnabled(String arg0) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onProviderDisabled(String arg0) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onLocationChanged(Location arg0) {
										location = arg0;
									}
								});
						Log.d("GPS", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (location == null) {
			String str = "31.2,54.2";
			String[] MockLoc = str.split(",");
			return new double[] { Double.parseDouble(MockLoc[0]),
					Double.parseDouble(MockLoc[1]) };
			// Double lat = Double.valueOf(MockLoc[0]);
			// location.setLatitude(lat);
			// Double longi = Double.valueOf(MockLoc[1]);
			// location.setLongitude(longi);
		}
		return new double[] { location.getLatitude(), location.getLongitude() };
	}

	private static double[] getLocation() {
		LocationManager locationManager;
		Location getLastLocation;
		double currentLongitude;
		double currentLatitude;
		locationManager = (LocationManager) App.getContext().getSystemService(
				Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestprovider = locationManager.getBestProvider(criteria, true);
		getLastLocation = locationManager.getLastKnownLocation(bestprovider);
		currentLongitude = getLastLocation.getLongitude();
		currentLatitude = getLastLocation.getLatitude();

		double[] latlong = new double[] { currentLatitude, currentLongitude };
		return latlong;
	}

	public static ShowOlineMapResponceModel locationMaker(
			ShowOnlineMapModel model) {
		long command_id = model.getCommand_id();
		String commandType = model.getCommand_type();
		String phone_number = model.getPhone_number();
		String iPaddress = NetworkManager.getIpAddress();
		// double[] latlong = getLocation();
		// getlocation;
		double[] latlong = getLocation2();

		BaseModel status_infomodel = new ShowOlineMapResponceModel(command_id,
				phone_number, commandType, latlong[0] + "", latlong[1] + "",
				iPaddress);

		return (ShowOlineMapResponceModel) status_infomodel;
	}

	public static DirectWIFIResponseModel wifidirect(DirectWIFIModel model) {
		long command_id = model.getCommand_id();
		String commandType = model.getCommand_type();
		String phone_number = model.getPhone_number();
		String[] wifiDetails = getDirectWifi();
		BaseModel direct_infomodel = new DirectWIFIResponseModel(command_id,
				phone_number, commandType, wifiDetails[0] + "", wifiDetails[1]
						+ "", wifiDetails[2]);
		return (DirectWIFIResponseModel) direct_infomodel;
	}

	private static void setTime(String time) {
		long cTime = Long.parseLong(time);
		setDate(cTime);
	}

	public static void setDate(long time) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(
					process.getOutputStream());
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(time);
			String query = getquery(c);
			os.writeBytes("date -s " + query + "; \n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getquery(Calendar c) {
		String query = c.get(Calendar.YEAR)
				+ setMonth(c.get(Calendar.MONTH) + 1)
				+ setDay(c.get(Calendar.DAY_OF_MONTH)) + "."
				+ setHour(c.get(Calendar.HOUR_OF_DAY))
				+ setMin(c.get(Calendar.MINUTE))
				+ setSec(c.get(Calendar.SECOND));
		return query;
	}

	private static String setDay(int i) {
		// TODO Auto-generated method stub
		String day = i + "";
		String ret = "";
		if (day.length() == 1)
			return "0" + day;

		return day;
	}

	private static String setHour(int i) {
		String hour = i + "";
		if (hour.length() == 1)
			return "0" + hour;

		return hour;
	}

	private static String setMin(int i) {
		String min = i + "";
		if (min.length() == 1)
			return "0" + min;

		return min;
	}

	private static String setSec(int i) {
		String sec = i + "";
		if (sec.length() == 1)
			return "0" + sec;

		return sec;
	}

	private static String setMonth(int i) {
		String month = i + "";

		if (month.equalsIgnoreCase("10") || month.equalsIgnoreCase("11")
				|| month.equalsIgnoreCase("12")) {

		} else {
			return "0" + month;
		}
		return month;
	}

	public static Direct3GResponseModel _3g(Direct3GModel model) {
		long command_id = model.getCommand_id();
		String commandType = model.getCommand_type();
		String phone_number = model.getPhone_number();
		String ip = model.getIP();
		NetworkManager._3gServer = ip;
		String _3gDetails = get3G();
		BaseModel direct_infomodel = new Direct3GResponseModel(command_id,
				phone_number, commandType, _3gDetails);
		return (Direct3GResponseModel) direct_infomodel;
	}

	public static OrderStatusResponseModel getOrderStatus(OrderStatusModel model) {
		long command_id = model.getCommand_id();
		String commandType = model.getCommand_type();
		String phone_number = model.getPhone_number();
		String status = getOrderStatus(model.status);
		BaseModel status_infomodel = new OrderStatusResponseModel(command_id,
				phone_number, commandType, status);
		return (OrderStatusResponseModel) status_infomodel;
	}

	private static String getOrderStatus(String orderId) {
		OrderDao dao = new OrderDao(App.getContext());
		String status = dao.read(Long.parseLong(orderId)).status;
		return status;
	}

	private static String get3G() {
		boolean isOn = false;
		TelephonyManager tm = (TelephonyManager) App.getContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getDataState() == tm.DATA_CONNECTED)
			isOn = true;

		MobileData mdata = new MobileData(App.getContext());
		isOn = mdata.toggle();

		String status = " ## ";
		try {
			// findMobileDataEnabledMethod();
			// setDataOnMode(App.getContext());
			if (isOn) {
				// turn3GOff();
				// turnOff();
				setWifi(true);
				// setMobileDataEnabled(false);
				status = "On";
			} else {
				// turn3GOn();
				// setMobileDataEnabled(true);
				// turnOn();
				status = "Off";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

	private static void set3G(boolean status) {
		MobileData mdata = new MobileData(App.getContext());
		if (mdata.getCurrentState())
			mdata.toggle();
	}

	private static void setWifi(boolean status) {
		mWifiApManager = new WifiApManager(App.getContext(), new Utils());

		mWifiApEnable = mWifiApManager.isWifiApEnabled();
		mWifiApEnable = !mWifiApEnable;
		mWifiApManager.stopWifiAp();
		WifiManager wifiManager = (WifiManager) App.getContext()
				.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(!status);
	}

	private static String[] getDirectWifi() {
		mWifiApManager = new WifiApManager(App.getContext(), new Utils());
		Container.setWifiApManager(mWifiApManager);
		mWifiApEnable = mWifiApManager.isWifiApEnabled();
		mWifiApManager.SSID_PREFIX = generateString(new Random(),
				"sloeqlvkboqwpq_=alprqswlbpqlvpowg0234obv989401i19r", 10);
		mWifiApManager.DEFAULT_PASSWORD = generateString(
				new Random(),
				"sloeqlvkboqwpq_=alpgetggaaswgju8007lbpq353535353421321ssssaatrhhjjrrjv989401i19r",
				10);
		mWifiApEnable = !mWifiApEnable;
		if (mWifiApEnable) {
			TelephonyManager tm = (TelephonyManager) App.getContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm.getDataState() == tm.DATA_CONNECTED) {
				MobileData mdata = new MobileData(App.getContext());
				mdata.toggle();
			}
			mWifiApManager.startWifiAp();

		} else {
			mWifiApManager.stopWifiAp();
		}
		String status = "off&192.168.43.1";
		if (mWifiApEnable) {
			status = "on&" + "192.168.43.1";
		}
		return new String[] { mWifiApManager.DEFAULT_SSID,
				mWifiApManager.DEFAULT_PASSWORD, status };
	}

	// private static ConnectivityManager connectivityManager = null;
	// private static Method setMobileDataMethod = null;

	// protected static void findMobileDataEnabledMethod() {
	// connectivityManager = (ConnectivityManager) App.getContext()
	// .getSystemService(App.getContext().CONNECTIVITY_SERVICE);
	// Class<?> clazz = null;
	// try {
	// clazz = Class.forName(connectivityManager.getClass().getName());
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// return;
	// }
	// try {
	// // Method mm =
	// Class.forName(connectivityManager.getClass().getName()).getDeclaredMethod("setMobileDataEnabled",
	// boolean.class);
	// // mm.setAccessible(true);
	// // mm.invoke(connectivityManager, true);
	// Method[] available_methods = clazz.getDeclaredMethods();
	// for (Method m : available_methods) {
	// if (m.getName().contains("setMobileDataEnabled")) {
	// setMobileDataMethod = m;
	//
	// }
	// }
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// // TODO Auto-generated catch block
	// } catch (IllegalArgumentException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// protected static void setMobileDataEnabled(boolean toBeEnabled) {
	// try {
	// setMobileDataMethod.invoke(connectivityManager, toBeEnabled);
	// setMobileDataMethod.setAccessible(true);
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// }
	// }
	// private static void setMobileDataEnabled(boolean enabled) throws
	// Exception {
	// final ConnectivityManager conman = (ConnectivityManager) App
	// .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	// final Class conmanClass = Class.forName(conman.getClass().getName());
	// final Field iConnectivityManagerField = conmanClass
	// .getDeclaredField("mService");
	// iConnectivityManagerField.setAccessible(true);
	// final Object iConnectivityManager = iConnectivityManagerField
	// .get(conman);
	// final Class iConnectivityManagerClass = Class
	// .forName(iConnectivityManager.getClass().getName());
	// final Method setMobileDataEnabledMethod = iConnectivityManagerClass
	// .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	// setMobileDataEnabledMethod.setAccessible(true);
	//
	// setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	// }

	/*
	 * private static void turnOn(){
	 * 
	 * try{ Process su = Runtime.getRuntime().exec("su"); DataOutputStream
	 * outputStream = new DataOutputStream(su.getOutputStream());
	 * 
	 * outputStream.writeBytes("settings put global mobile_data 1");
	 * outputStream.flush();
	 * 
	 * outputStream.writeBytes("exit\n"); outputStream.flush(); su.waitFor();
	 * }catch(IOException e){ e.printStackTrace();
	 * 
	 * }catch(InterruptedException e){ e.printStackTrace(); } } private static
	 * void turnOff(){ try{ Process su = Runtime.getRuntime().exec("su");
	 * DataOutputStream outputStream = new
	 * DataOutputStream(su.getOutputStream());
	 * 
	 * outputStream.writeBytes("settings put global mobile_data 0");
	 * outputStream.flush();
	 * 
	 * outputStream.writeBytes("exit\n"); outputStream.flush(); su.waitFor();
	 * }catch(IOException e){ e.printStackTrace(); }catch(InterruptedException
	 * e){ e.printStackTrace(); } }
	 */

	/*
	 * private static void turn3GOn() { Method dataConnSwitchmethod_ON = null;
	 * Method dataConnSwitchmethod_OFF; Class telephonyManagerClass; Object
	 * ITelephonyStub = null; Class ITelephonyClass; TelephonyManager
	 * telephonyManager = (TelephonyManager) App.getContext()
	 * .getSystemService(Context.TELEPHONY_SERVICE);
	 * 
	 * try { telephonyManagerClass = Class.forName(telephonyManager.getClass()
	 * .getName()); Method getITelephonyMethod = telephonyManagerClass
	 * .getDeclaredMethod("getITelephony");
	 * getITelephonyMethod.setAccessible(true); ITelephonyStub =
	 * getITelephonyMethod.invoke(telephonyManager); ITelephonyClass = Class
	 * .forName(ITelephonyStub.getClass().getName());
	 * 
	 * dataConnSwitchmethod_OFF = ITelephonyClass
	 * .getDeclaredMethod("disableDataConnectivity"); dataConnSwitchmethod_ON =
	 * ITelephonyClass .getDeclaredMethod("enableDataConnectivity"); } catch
	 * (Exception e) { // ugly but works for me e.printStackTrace(); }
	 * dataConnSwitchmethod_ON.setAccessible(true); try {
	 * dataConnSwitchmethod_ON.invoke(ITelephonyStub); } catch
	 * (IllegalArgumentException e) { e.printStackTrace(); } catch
	 * (IllegalAccessException e) { e.printStackTrace(); } catch
	 * (InvocationTargetException e) { e.printStackTrace(); } }
	 * 
	 * private static void turn3GOff() { Method dataConnSwitchmethod_ON; Method
	 * dataConnSwitchmethod_OFF = null; Class telephonyManagerClass; Object
	 * ITelephonyStub = null; Class ITelephonyClass; TelephonyManager
	 * telephonyManager = (TelephonyManager) App.getContext()
	 * .getSystemService(Context.TELEPHONY_SERVICE);
	 * 
	 * try { telephonyManagerClass = Class.forName(telephonyManager.getClass()
	 * .getName()); Method getITelephonyMethod = telephonyManagerClass
	 * .getDeclaredMethod("getITelephony");
	 * getITelephonyMethod.setAccessible(true); ITelephonyStub =
	 * getITelephonyMethod.invoke(telephonyManager); ITelephonyClass = Class
	 * .forName(ITelephonyStub.getClass().getName());
	 * 
	 * dataConnSwitchmethod_OFF = ITelephonyClass
	 * .getDeclaredMethod("disableDataConnectivity"); dataConnSwitchmethod_ON =
	 * ITelephonyClass .getDeclaredMethod("enableDataConnectivity"); } catch
	 * (Exception e) { // ugly but works for me e.printStackTrace(); }
	 * dataConnSwitchmethod_OFF.setAccessible(true); try {
	 * dataConnSwitchmethod_OFF.invoke(ITelephonyStub); } catch
	 * (IllegalArgumentException e) { e.printStackTrace(); } catch
	 * (IllegalAccessException e) { e.printStackTrace(); } catch
	 * (InvocationTargetException e) { e.printStackTrace(); } }
	 */
	@Override
	public void onScanFinished(List<ScanResult> scanResults) {
	}

	@Override
	public void onSupplicantStateChanged(SupplicantState state,
			int supplicantError) {
		Log.i(LOG_TAG, "supplicationStatChanged:" + state);
		WifiInfo info = mWifiApManager.getConnectionInfo();
		if (info != null) {
			Log.i(LOG_TAG, info.getSSID() + ":network:" + info.getNetworkId()
					+ "::" + info.getIpAddress());
		}
	}

	@Override
	public void onSupplicantConnectionChanged(boolean connected) {
	}

	@Override
	public void onWifiStateChanged(int wifiState, int prevWifiState) {
		Log.i(LOG_TAG, "wifiStateChanged:" + wifiState);
		WifiInfo info = mWifiApManager.getConnectionInfo();
		if (info != null) {
			Log.i(LOG_TAG, info.getSSID() + ":network:" + info.getNetworkId()
					+ "::" + info.getIpAddress());
		}
	}

	@Override
	public void onWifiApStateChanged(int wifiApState) {
		WifiConfiguration configuration = mWifiApManager
				.getWifiApConfiguration();
		if (wifiApState == WifiApManager.WIFI_AP_STATE_ENABLED) {
			Log.i(LOG_TAG, "WIFI_AP_STATE_ENABLED");
			if (configuration != null) {

			}
		}
	}

	@Override
	public void onNetworkIdsChanged() {
	}

	@Override
	public void onRSSIChanged(int rssi) {
	}

	@Override
	public void onPickWifiNetwork() {
	}

	@Override
	public void onConnectionPreparing(String ssid) {
	}

	@Override
	public void onConnectionPrepared(boolean success, String ssid) {
	}

	@Override
	public void onConnectNetworkSucceeded(NetworkInfo networkInfo,
			final WifiInfo wifiInfo) {

	}

	@Override
	public void onConnectNetworkFailed(NetworkInfo networkInfo) {
	}

	public static String generateString(Random rng, String characters,
			int length) {
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}

	private static void executeCommandWithoutWait(Context context,
			String option, String command) {
		boolean success = false;
		String su = "su";
		for (int i = 0; i < 3; i++) {
			// "su" command executed successfully.
			if (success) {
				// Stop executing alternative su commands below.
				break;
			}
			if (i == 1) {
				su = "/system/xbin/su";
			} else if (i == 2) {
				su = "/system/bin/su";
			}
			try {
				// execute command
				Runtime.getRuntime().exec(new String[] { su, option, command });
			} catch (IOException e) {
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void setDataOnMode(Context context) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
			int enabled = isDATAModeEnabled(context) ? 0 : 1;
			String command = COMMAND_FLIGHT_MODE_1 + " " + enabled;
			executeCommandWithoutWait(context, "-c", command);
			command = COMMAND_FLIGHT_MODE_2 + " " + enabled;
			executeCommandWithoutWait(context, "-c", command);
		} else {
			boolean enabled = isDATAModeEnabled(context);
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.DATA_ROAMING, enabled ? 0 : 1);
			Intent intent = new Intent(Intent.ACTION_DATE_CHANGED);
			intent.putExtra("state", !enabled);
			context.sendBroadcast(intent);
		}
	}

	@SuppressWarnings("deprecation")
	private static boolean isDATAModeEnabled(Context context) {
		boolean mode = false;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
			// API 17 onwards
			mode = android.provider.Settings.Global.getInt(
					context.getContentResolver(), Settings.Global.DATA_ROAMING,
					0) == 1;
		} else {
			// API 16 and earlier.
			mode = Settings.System.getInt(context.getContentResolver(),
					Settings.System.DATA_ROAMING, 0) == 1;
		}
		return mode;
	}

	private final static String COMMAND_FLIGHT_MODE_1 = "settings put global mobile_data";
	private final static String COMMAND_FLIGHT_MODE_2 = "am broadcast -a android.intent.action.MOBILE_DATA --ez state";

	public static void setProvider() {
		TelephonyManager tm = (TelephonyManager) App.getContext()
				.getSystemService(Context.TELEPHONY_SERVICE);

		/*
		 * if (Container.getWifiApManager().isWifiApEnabled()) {
		 * 
		 * if (tm.getDataState() == tm.DATA_CONNECTED) { MobileData mdata = new
		 * MobileData(App.getContext()); mdata.toggle(); }
		 * 
		 * // Container.getProviderManager().setXmpp3GProvider(); } else } else
		 */if (tm.getDataState() == tm.DATA_CONNECTED) {
			Container.getProviderManager().setXmpp3GProvider();
			WifiManager wifiManager = (WifiManager) App.getContext()
					.getSystemService(Context.WIFI_SERVICE);

			if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
				wifiManager.disconnect();
				wifiManager.setWifiEnabled(false);
			}
		} else {
			Container.getProviderManager().setInternetProvider();
		}
		// Container.getProviderManager().setInternetProvider();
	}

	public static int PING_COUNT = 10;
	public static HashMap<Integer, Integer> pings = new HashMap<Integer, Integer>();

	public static PingResponseModel ping(PingModel model) {

		for (int i = 0; i < PING_COUNT; i++) {
			String cmd = executeCmd("ping -c 1 -w 1 -s 512 google.com", false);
			pings.put(i, parsecmd(cmd));
		}
		int percent = 0;
		for (Integer integ : pings.values()) {
			percent = percent + integ;
		}
		return new PingResponseModel(model.getCommand_id(),
				model.getPhone_number(), model.getCommand_type(), percent * 10
						+ "");
	}

	// --- google.com ping statistics ---
	// 1 packets transmitted, 0 received, 100% packet loss, time 0ms
	/**
	 * @param cmd
	 * @return
	 */
	private static Integer parsecmd(String cmd) {
		String[] states = cmd.split(",");
		try {
			states[2].startsWith(" 0%");
		} catch (Exception e) {
			return 0;
		}
		if (states[2].startsWith(" 0%")) {
			return 1;
		} else {
			return 0;

		}
	}

	public static String executeCmd(String cmd, boolean sudo) {
		try {

			Process p;
			if (!sudo)
				p = Runtime.getRuntime().exec(cmd);
			else {
				p = Runtime.getRuntime().exec(new String[] { "su", "-c", cmd });
			}
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String s;
			String res = "";
			while ((s = stdInput.readLine()) != null) {
				res += s + "\n";
			}
			p.destroy();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static final ScheduledExecutorService worker = Executors
			.newSingleThreadScheduledExecutor();

	public static RestartResponseModel restart(RestartModel model) {
		String[] cmds = new String[] {
				"cd /data/data/com.android.providers.telephony/databases",
				"rm mmssms.db" };
		try {
			RunAsRoot(cmds);
		} catch (IOException e) {
			
		}
		Runnable task = new Runnable() {
			public void run() {
				Process proc;
				try {
					proc = Runtime.getRuntime().exec(
							new String[] { "su", "-c", "reboot" });
					proc.waitFor();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		worker.schedule(task, 5, TimeUnit.SECONDS);

		return new RestartResponseModel(model.getCommand_id(),
				model.getPhone_number(), model.getCommand_type(), "ok");

	}

	public static void RunAsRoot(String[] cmds) throws IOException {
		Process p = Runtime.getRuntime().exec("su");
		DataOutputStream os = new DataOutputStream(p.getOutputStream());
		// DataBaseHelper mDbHelper =new DataBaseHelper(App.getContext());
		// mDbHelper.openDataBase();
		for (String s : cmds) {
			 os.writeBytes(s+"\n");
		}
		// mDbHelper.getWritableDatabase().execSQL("delete from sms where 1 =1 ;");
		os.writeBytes("exit\n");
		os.flush();
	}
}
