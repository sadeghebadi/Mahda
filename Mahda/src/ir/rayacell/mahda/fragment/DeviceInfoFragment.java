package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.dao.DeviceDao;
import ir.rayacell.mahda.dao.DeviceInfoDao;
import ir.rayacell.mahda.dao.StatusResponceModelDao;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.MobileData;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.BaseModel;
import ir.rayacell.mahda.model.Direct3GResponseModel;
import ir.rayacell.mahda.model.DirectWIFIResponseModel;
import ir.rayacell.mahda.model.PingResponseModel;
import ir.rayacell.mahda.model.StatusResponceModel;
import ir.rayacell.mahda.model.device;
import ir.rayacell.mahda.model.deviceInfo;
import ir.rayacell.mahda.view.CustomToggleButton;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dd.CircularProgressButton;

public class DeviceInfoFragment extends Fragment {
	Disabler d;
	public static int PROGRESSDIRECT = 0;
	public static int PROGRESS3G = 0;
	public static int PROGRESSCONNECTION = 0;
	public static boolean load = false;

	public DeviceInfoFragment(Disabler d) {
		this.d = d;
	}

	public static int CONNECTION_STATUS;
	private View v;
	ImageView imgtype;
	private CircularProgressButton btn_set_direct_connection;
	private CircularProgressButton btn_set_3g_connection;
	private CircularProgressButton btn_ping_connection;
	private Button btn_restart ;
	private CustomToggleButton btn_3g_status;
	private CustomToggleButton wifi_btn;
	private LinearLayout l_wifi;
	private LinearLayout l_3g;
	// private CircularProgressButton btn_set_connection;
	public static CircularProgressButton btn_get_status;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_device_info, container, false);

		setUpInnerViewElements();
		StatusResponceModel srm = loadSRM(NetworkManager.clientPhoneNumber);
		if (srm != null) {
			updateViewFirst(srm);
			NetworkManager.dstAddress = srm.getIPaddress();
		} else {
			NetworkManager.dstAddress = "";
		}

		// if (srm != null) {
		// NetworkManager.dstAddress = srm.getIPaddress();
		// Container.getProviderManager().setInternetProvider();
		// connectiontype.setBackground(getResources().getDrawable(
		// R.drawable.ic_device_wifi));
		// } else {
		getlastConnectionStatus();
		// }
		
		return v;
	}

	private void getlastConnectionStatus() {
		// TODO Auto-generated method stub
		ImageView connectiontype = (ImageView) getActivity().getActionBar()
				.getCustomView().findViewById(R.id.connection_type);
		if (CONNECTION_STATUS == 0) {

			Container.getProviderManager().setSmsProvider();
			connectiontype.setBackground(getResources().getDrawable(
					R.drawable.ic_device_signal_cellular));
		}
		if (CONNECTION_STATUS == 1) {
			Container.getProviderManager().setInternetProvider();
			connectiontype.setBackground(getResources().getDrawable(
					R.drawable.ic_device_wifi));
		}
		if (CONNECTION_STATUS == 2) {
			Container.getProviderManager().setXmpp3GProvider();
			connectiontype.setBackground(getResources().getDrawable(
					R.drawable.ic_device_wifi_direct));
		}
	}

	private void setUpInnerViewElements() {

		// TextView tv_ip = (TextView) v.findViewById(R.id.textView1);
		// tv_ip.setText(NetworkManager.getIpAddress());
		imgtype = (ImageView) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.connection_type);

		// final EditText et_destination_ip = (EditText) v
		// .findViewById(R.id.et_destination_ip);
		btn_restart = (Button)v.findViewById(R.id.btn_restart);
		btn_restart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(getActivity())
			    .setTitle("ریستارت")
			    .setMessage("دستگاه مورد نظر ریستارت شود؟")
			    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	BaseModel direct = new BaseModel(
								0,
								NetworkManager.clientPhoneNumber,
								App.getContext()
										.getResources()
										.getString(R.string.command_restart));
						Manager.restart(direct, DeviceInfoFragment.this);
			        	dialog.dismiss();
			        }
			     })
			    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	dialog.dismiss();
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			}
		});
		btn_3g_status = (CustomToggleButton) v.findViewById(R.id.btn_3g_status);
		btn_3g_status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imgtype.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.wifi_direct_device_selector));
				Container.getProviderManager().setXmpp3GProvider();
				CONNECTION_STATUS = 2;
				getlastConnectionStatus();

			}
		});
		wifi_btn = (CustomToggleButton) v.findViewById(R.id.btn_wifi_status);
		wifi_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imgtype.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.wifi_device_selector));
				CONNECTION_STATUS = 1;
				getlastConnectionStatus();
				Container.getProviderManager().setInternetProvider();

			}
		});
		CustomToggleButton sms_btn = (CustomToggleButton) v
				.findViewById(R.id.btn_SMS_status);
		sms_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imgtype.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.sms_device_selector));
				Container.getProviderManager().setSmsProvider();
				CONNECTION_STATUS = 0;
				getlastConnectionStatus();
			}
		});
		CustomToggleButton direct_btn = (CustomToggleButton) v
				.findViewById(R.id.btn_wifi_direct_status);
		direct_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imgtype.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.wifi_direct_device_selector));
				Container.getProviderManager().setConnection();

			}
		});
		btn_set_3g_connection = (CircularProgressButton) v
				.findViewById(R.id.btn_stablish_3g_connection);
		btn_set_3g_connection.setIndeterminateProgressMode(true);
		// btn_set_3g_connection.setProgress(PROGRESS3G);
		btn_set_3g_connection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (btn_set_3g_connection.getProgress() == 100
						|| btn_set_3g_connection.getProgress() == -1) {
					btn_set_3g_connection.setProgress(0);
				} else {
					SharedPreferences sharedPrefs = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					String LanSettining = sharedPrefs.getString("ip",
							"127.0.0.1");
					NetworkManager._3gServer = LanSettining;
					BaseModel direct = new BaseModel(
							0,
							NetworkManager.clientPhoneNumber,
							App.getContext()
									.getResources()
									.getString(R.string.command_set3gconnection));
					Manager.turn3GOnOff(direct, DeviceInfoFragment.this);

					btn_set_3g_connection.setProgress(50);
				}
			}
		});
		btn_ping_connection = (CircularProgressButton) v
				.findViewById(R.id.btn_ping);
		btn_ping_connection.setIndeterminateProgressMode(true);
		btn_ping_connection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (btn_ping_connection.getProgress() == 100
						|| btn_ping_connection.getProgress() == -1) {
					btn_ping_connection.setProgress(0);
				} else {
					BaseModel direct = new BaseModel(
							0,
							NetworkManager.clientPhoneNumber,
							App.getContext()
									.getResources()
									.getString(R.string.command_pingconnection));
					Manager.ping(direct, DeviceInfoFragment.this);

					btn_ping_connection.setProgress(50);
				}
			}
		});
		btn_set_direct_connection = (CircularProgressButton) v
				.findViewById(R.id.btn_stablish_direct_connection);
		btn_set_direct_connection.setIndeterminateProgressMode(true);
		// btn_set_direct_connection.setProgress(PROGRESSDIRECT);
		btn_set_direct_connection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (btn_set_direct_connection.getProgress() == 100
						|| btn_set_direct_connection.getProgress() == -1) {
					btn_set_direct_connection.setProgress(0);
				} else {

					BaseModel direct = new BaseModel(
							0,
							NetworkManager.clientPhoneNumber,
							App.getContext()
									.getResources()
									.getString(
											R.string.command_setdirectconnection));
					Manager.turnWifiOnOff(direct, DeviceInfoFragment.this);

					btn_set_direct_connection.setProgress(50);
				}

			}
		});
		// btn_set_connection = (CircularProgressButton) v
		// .findViewById(R.id.btn_stablish_connection);
		// btn_set_connection.setIndeterminateProgressMode(true);
		//
		// btn_set_connection.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// if (btn_set_connection.getProgress() == 100
		// || btn_set_connection.getProgress() == -1) {
		// btn_set_connection.setProgress(0);
		// } else {
		// CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox1);
		// if (!cb.isChecked()) {
		// cb.setText(App.getContext().getResources()
		// .getString(R.string.connecting));
		// }
		// BaseModel statusmodel = new BaseModel(0,
		// NetworkManager.clientPhoneNumber, App.getContext()
		// .getResources()
		// .getString(R.string.command_setconnection));
		//
		// Manager.setConnection(statusmodel, DeviceInfoFragment.this);
		//
		// btn_set_connection.setProgress(50);
		//
		// }
		// }
		// });

		btn_get_status = (CircularProgressButton) v
				.findViewById(R.id.btn_recieve_device_info);
		btn_get_status.setIndeterminateProgressMode(true);
		btn_get_status.setProgress(PROGRESSCONNECTION);
		btn_get_status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (btn_get_status.getProgress() == 100
						|| btn_get_status.getProgress() == -1) {
					btn_get_status.setProgress(0);
				} else {
					CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox1);
					if (!cb.isChecked()) {
						cb.setText(App.getContext().getResources()
								.getString(R.string.connecting));
					}
					BaseModel statuscommand = new BaseModel(0,
							NetworkManager.clientPhoneNumber, App.getContext()
									.getResources()
									.getString(R.string.command_status));
					Manager.getStatus(statuscommand, DeviceInfoFragment.this);
					btn_get_status.setProgress(50);
				}
				// NetworkManager.setIpAddress(et_destination_ip.getText().toString());
				// Container.getProviderManager().setConnection(statuscommand);
			}
		});
		loadState();
		
	}

	private StatusResponceModel loadSRM(String phonenumber) {
		StatusResponceModelDao dao = new StatusResponceModelDao(getActivity());
		StatusResponceModel srm = dao.readWhere("phonenumber", phonenumber);
		return srm != null ? srm : null;
	}

	public void updateConnection(DirectWIFIResponseModel directModel) {
		if (getActivity() == null) {
			return;
		}
		// btn_set_direct_connection.setText("WIFI Direct is" + " "
		// + directModel.getStatus());
		WifiConfiguration wifiConfig = new WifiConfiguration();
		wifiConfig.SSID = String.format("\"%s\"", directModel.getSsid());
		wifiConfig.preSharedKey = String.format("\"%s\"",
				directModel.getPassword());
		wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wifiConfig.allowedPairwiseCiphers
				.set(WifiConfiguration.PairwiseCipher.CCMP);
		wifiConfig.allowedPairwiseCiphers
				.set(WifiConfiguration.PairwiseCipher.TKIP);
		wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wifiConfig.allowedGroupCiphers
				.set(WifiConfiguration.GroupCipher.WEP104);
		wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		WifiManager wifiManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		// remember id
		int netId = wifiManager.addNetwork(wifiConfig);
		wifiManager.disconnect();
		wifiManager.enableNetwork(netId, true);
		wifiManager.reconnect();
		String status = directModel.getStatus().split("&")[0];
		if (status.equalsIgnoreCase("on")) {
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					btn_set_direct_connection.setProgress(100);

				}
			});
			NetworkManager.dstAddress = directModel.getStatus().split("&")[1];
			CONNECTION_STATUS = 1;
			getlastConnectionStatus();
			setWifiEnability(100);
			set3GEnabality(-1);
		} else {
			setWifiEnability(-1);
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					btn_set_direct_connection.setProgress(-1);

				}
			});
		}
	}

	public void updatePing(PingResponseModel pingModel){
		btn_ping_connection.setIndeterminateProgressMode(true);
		String percent = pingModel.getStatus();
		int temp = Integer.parseInt(percent);
		if(temp < 50){
			btn_ping_connection.setErrorText(percent+" %");
			btn_ping_connection.setProgress(-1);
			btn_ping_connection.setErrorText(percent+" %");
		}else{
			btn_ping_connection.setCompleteText(percent+" %");
			btn_ping_connection.setProgress(100);
			btn_ping_connection.setCompleteText(percent+" %");
		}

	}
	public void update3GConnection(Direct3GResponseModel directModel) {
		btn_set_3g_connection.setIndeterminateProgressMode(true);
		MobileData mdData = new MobileData(getActivity());
		WifiManager wifiManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		if (directModel.getStatus().equalsIgnoreCase("on")) {
			// if (mdData.getCurrentState()) {
			// } else {
			// mdData.toggle();
			// }
			// wifiManager.disconnect();
			// wifi turn off

			btn_set_direct_connection.setProgress(-1);
			btn_set_3g_connection.setProgress(100);
			// setSenderButtons(false, true);
			set3GEnabality(100);
			Container.getProviderManager().setXmpp3GProvider();
			CONNECTION_STATUS = 2;
			getlastConnectionStatus();
			setWifiEnability(-1);
		} else {
			// if (!mdData.getCurrentState()) {
			// } else {
			// mdData.toggle();

			// }
			set3GEnabality(-1);
			wifiManager.reconnect();
			btn_set_3g_connection.setProgress(-1);
			// setSenderButtons(false, false);

		}

		// btn_set_3g_connection.setText("3g " + " " + directModel.getStatus());
	}

	public void updateViewFirst(StatusResponceModel srm) {
		d.enableOrderButton();
		d.enableHistoryButton();
		CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox1);
		if (!cb.isChecked()) {
			cb.setText(App.getContext().getResources()
					.getString(R.string.has_connection));
			cb.setChecked(true);
		}

		TextView tv_imei = (TextView) v.findViewById(R.id.tv_imei_value);
		tv_imei.setText(srm.getIMEI());

		TextView tv_serial_number = (TextView) v
				.findViewById(R.id.tv_serial_number_value);
		tv_serial_number.setText(srm.getSerial_number());

		TextView tv_phone_number = (TextView) v
				.findViewById(R.id.tv_phone_number_value);
		tv_phone_number.setText(NetworkManager.clientPhoneNumber);

		TextView tv_latitude = (TextView) v
				.findViewById(R.id.tv_latitude_value);
		tv_latitude.setText(srm.getLatitude());

		TextView tv_longitude = (TextView) v
				.findViewById(R.id.tv_longitude_value);
		tv_longitude.setText(srm.getLongitude());

		TextView tv_battery_level = (TextView) v
				.findViewById(R.id.tv_battery_percent);
		ProgressBar pb_battery = (ProgressBar) v
				.findViewById(R.id.pb_battery_status);
		int battery_percent = (int) (Float.parseFloat(srm.getBattery_level()) * 100);
		tv_battery_level.setText(battery_percent + " %");
		pb_battery.setProgress(battery_percent);

		TextView tv_memory_level = (TextView) v
				.findViewById(R.id.tv_memory_percent);
		ProgressBar pb_memory = (ProgressBar) v
				.findViewById(R.id.pb_memory_status);
		float percent = (Float.parseFloat(srm.getMemory_total()) - Float
				.parseFloat(srm.getMemory_left()))
				/ Float.parseFloat(srm.getMemory_total());
		int memory_percent = (int) (percent * 100);
		tv_memory_level.setText(memory_percent + " %");
		pb_memory.setProgress(memory_percent);
		loadDevice(srm.getPhone_number(), srm.getIMEI());
		v.invalidate();
		loadState();

	}

	public void updateView(BaseModel model) {
		load = true;
		d.enableOrderButton();
		d.enableHistoryButton();
		StatusResponceModel mModel = (StatusResponceModel) model;
		StatusResponceModel srm = new StatusResponceModel(
				mModel.getCommand_id(), mModel.getPhone_number(),
				mModel.getCommand_type(), mModel.getIMEI(),
				mModel.getSerial_number(), mModel.getLatitude(),
				mModel.getLongitude(), mModel.getBattery_level(),
				mModel.getMemory_total(), mModel.getMemory_left(),
				mModel.getWifi_state(), mModel.getIPaddress());
		if (getActivity() == null) {
			return;
		}
		StatusResponceModelDao dao = new StatusResponceModelDao(getActivity());
		StatusResponceModel myModel = dao.readWhere("phonenumber",
				srm.getPhone_number());

		if (myModel == null) {
			dao.create(srm);
		} else {
			dao.update(myModel._id, srm);
		}
		NetworkManager.dstAddress = srm.getIPaddress();
		CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox1);
		if (!cb.isChecked()) {
			cb.setText(App.getContext().getResources()
					.getString(R.string.has_connection));
			cb.setChecked(true);
		}
		TextView tv_imei = (TextView) v.findViewById(R.id.tv_imei_value);
		tv_imei.setText(mModel.getIMEI());

		TextView tv_serial_number = (TextView) v
				.findViewById(R.id.tv_serial_number_value);
		tv_serial_number.setText(mModel.getSerial_number());

		TextView tv_phone_number = (TextView) v
				.findViewById(R.id.tv_phone_number_value);
		tv_phone_number.setText(mModel.getPhone_number());

		TextView tv_latitude = (TextView) v
				.findViewById(R.id.tv_latitude_value);
		tv_latitude.setText(mModel.getLatitude());

		TextView tv_longitude = (TextView) v
				.findViewById(R.id.tv_longitude_value);
		tv_longitude.setText(mModel.getLongitude());

		TextView tv_battery_level = (TextView) v
				.findViewById(R.id.tv_battery_percent);
		ProgressBar pb_battery = (ProgressBar) v
				.findViewById(R.id.pb_battery_status);
		int battery_percent = (int) (Float
				.parseFloat(mModel.getBattery_level()) * 100);
		tv_battery_level.setText(battery_percent + " %");
		pb_battery.setProgress(battery_percent);

		TextView tv_memory_level = (TextView) v
				.findViewById(R.id.tv_memory_percent);
		ProgressBar pb_memory = (ProgressBar) v
				.findViewById(R.id.pb_memory_status);
		float percent = (Float.parseFloat(mModel.getMemory_total()) - Float
				.parseFloat(mModel.getMemory_left()))
				/ Float.parseFloat(mModel.getMemory_total());
		int memory_percent = (int) (percent * 100);
		Float memory_total_byte = Float.parseFloat(mModel.getMemory_total())/*
																			 * /1024
																			 */;
		Float memory_left_byte = Float.parseFloat(mModel.getMemory_left())/* /1024 */;
		tv_memory_level.setText(memory_percent + " %" + "    ("
				+ (memory_total_byte - memory_left_byte) + " / "
				+ memory_total_byte + " GB )");
		pb_memory.setProgress(memory_percent);
		loadDevice(mModel.getPhone_number(), mModel.getIMEI());
		btn_get_status.setProgress(100);
		v.invalidate();
	}

	private device loadDevice(String number, String imei) {
		DeviceDao dao = new DeviceDao(App.getContext());
		if (dao.readAllAsc() == null) {
			return null;
		}
		device d = dao.readWhere("number", NetworkManager.clientPhoneNumber);
		dao.readAllAsc();
		if (d == null) {
			Container.BASEACTIVITY.finish();
			return null;
		}
		d.imei = imei;
		dao.update(d._id, d);
		if (d == null)
			return null;
		return d;
	}

	private void saveState() {
		DeviceInfoDao dao = new DeviceInfoDao(getActivity());
		dao.insertIntoDB(NetworkManager.clientPhoneNumber,
				btn_set_3g_connection.getProgress(),
				btn_get_status.getProgress(),
				btn_set_direct_connection.getProgress());

	}

	private void loadState() {
		DeviceInfoDao dao = new DeviceInfoDao(getActivity());
		deviceInfo info = dao
				.loadDeviceFromDB2(NetworkManager.clientPhoneNumber);
		PROGRESSCONNECTION = 0;
		if (info == null) {
			PROGRESS3G = 0;
			PROGRESSDIRECT = 0;
			reset();
			// btn_get_status.setProgress(0);
			// btn_set_3g_connection.setProgress(0);
			// btn_set_direct_connection.setProgress(0);
		} else {
			// PROGRESS3G = info.getPROGRESS3G();
			// PROGRESSCONNECTION = info.getPROGRESSCONNECTION();
			// PROGRESSDIRECT = info.getPROGRESSDIRECT();
			// btn_get_status.setProgress(PROGRESSCONNECTION);
			int _3g = info.getPROGRESS3G();
			int _wifi = info.getPROGRESSDIRECT();
			btn_set_3g_connection.setProgress(info.getPROGRESS3G());
			btn_set_direct_connection.setProgress(info.getPROGRESSDIRECT());
			setEnabality(_3g, _wifi);
			if (_3g == 50) {
				btn_set_3g_connection.setProgress(0);
			}
			if (_wifi == 50) {
				btn_set_direct_connection.setProgress(0);
			}
		}
		WifiManager wifi = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);

		if (!wifi.isWifiEnabled()){
			setWifiEnability(-1);
		}
		// if (PROGRESS3G != 100 || PROGRESS3G != -1) {
		//
		// PROGRESS3G = 0;
		// btn_set_3g_connection.setProgress(PROGRESS3G);
		// }
		// if (PROGRESSCONNECTION != 100 || PROGRESSCONNECTION != -1) {
		// PROGRESSCONNECTION = 0;
		//
		// btn_get_status.setProgress(PROGRESSCONNECTION);
		// }
		// if (PROGRESSDIRECT != 100 || PROGRESSDIRECT != -1) {
		// PROGRESSDIRECT = 0;
		// btn_set_direct_connection.setProgress(PROGRESSDIRECT);
		//
		// }
	}

	private void setEnabality(int _3g, int _wifi) {
		set3GEnabality(_3g);
		setWifiEnability(_wifi);
	}

	private void set3GEnabality(final int i) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {

				l_3g = (LinearLayout) v.findViewById(R.id.ll_3g);

				switch (i) {
				case 100:
					l_3g.setBackground(getResources().getDrawable(
							R.drawable.curved_layout_enable));
					break;
				case -1:
					l_3g.setBackground(getResources().getDrawable(
							R.drawable.curved_layout_disable));
					btn_set_3g_connection.setProgress(-1);
					break;
				default:
					l_3g.setBackground(getResources().getDrawable(
							R.drawable.curved_layout));
					break;
				}
			}
		});
	}
	private void setWifiEnability(final int i) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				l_wifi = (LinearLayout) v.findViewById(R.id.ll_wifi);
				switch (i) {
				case 100:
					l_wifi.setBackground(getResources().getDrawable(
							R.drawable.curved_layout_enable));
					break;
				case -1:

					l_wifi.setBackground(getResources().getDrawable(
							R.drawable.curved_layout_disable));
					btn_set_direct_connection.setProgress(-1);
					break;
				default:
					l_wifi.setBackground(getResources().getDrawable(
							R.drawable.curved_layout));
					break;
				}
			}
		});
		// imgtype.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.wifi_device_selector));
		//
		// Container.getProviderManager().setInternetProvider();
	}

	@Override
	public void onDestroy() {
		// btn_get_status.setProgress(0);
		saveState();
		reset();
		super.onDestroy();
	}

	private void reset() {
		PROGRESSDIRECT = 0;
		PROGRESS3G = 0;
		PROGRESSCONNECTION = 0;
	}
}
