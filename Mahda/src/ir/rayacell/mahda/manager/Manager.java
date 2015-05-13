package ir.rayacell.mahda.manager;

import ir.rayacell.mahda.LiveMap;
import ir.rayacell.mahda.MapUpdater;
import ir.rayacell.mahda.fragment.DeviceInfoFragment;
import ir.rayacell.mahda.fragment.DownloadFragment;
import ir.rayacell.mahda.fragment.HistoryFragment;
import ir.rayacell.mahda.model.BaseModel;
import ir.rayacell.mahda.model.Command;
import ir.rayacell.mahda.model.DeleteDownloadModel;
import ir.rayacell.mahda.model.Direct3GResponseModel;
import ir.rayacell.mahda.model.DirectWIFIModel;
import ir.rayacell.mahda.model.DirectWIFIResponseModel;
import ir.rayacell.mahda.model.FileModel;
import ir.rayacell.mahda.model.OrderStatusResponseModel;
import ir.rayacell.mahda.model.PingResponseModel;
import ir.rayacell.mahda.model.ShowOnlineMapModel;
import ir.rayacell.mahda.model.StatusResponceModel;
import ir.rayacell.mahda.param.BaseParam;
import ir.rayacell.mahda.param.CaptureMotionPhotoParam;
import ir.rayacell.mahda.param.CapturePhotoParam;
import ir.rayacell.mahda.param.DeleteDownloadParam;
import ir.rayacell.mahda.param.FlightModeParam;
import ir.rayacell.mahda.param.GetFileListParam;
import ir.rayacell.mahda.param.GetlocationParam;
import ir.rayacell.mahda.param.OrderStatusParam;
import ir.rayacell.mahda.param.PingParam;
import ir.rayacell.mahda.param.RestartParam;
import ir.rayacell.mahda.param.ShowOnlineMapParam;
import ir.rayacell.mahda.param.VideoMotionRecordParam;
import ir.rayacell.mahda.param.VideoRecordParam;
import ir.rayacell.mahda.param.VoiceRecordParam;
import ir.rayacell.mahda.param.WifiParam;
import ir.rayacell.mahda.param._3GParam;
import ir.rayacell.mahda.param.statusParam;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

public class Manager {

	private static Fragment deviceInfo_fragment;
	private static Fragment history_fragment;

	private static Fragment download_fragment;
	private static MapUpdater updater;

	public static void controll(final BaseParam param) {
		BaseModel model = baseParser(param.getCommand());

		switch (Integer.parseInt(model.getCommand_type())) {
		// set connection command
		case 0:
			BaseModel setconnectionmodel = stringParser(param.getCommand());
			StatusResponceModel statusresponcemodel = (StatusResponceModel) setconnectionmodel;
			if (statusresponcemodel.getWifi_state() == 1) {
				NetworkManager.setIpAddress(statusresponcemodel.getIPaddress());
				Container.getProviderManager().setInternetProvider();
			}
			if (deviceInfo_fragment != null)
				((DeviceInfoFragment) deviceInfo_fragment)
						.updateView(setconnectionmodel);
			break;
		// status command
		case 1:
			BaseModel statusmodel = stringParser(param.getCommand());
			if (deviceInfo_fragment != null && statusmodel != null)

				((DeviceInfoFragment) deviceInfo_fragment)
						.updateView(statusmodel);
			break;

		// get files list command
		case 6:
			BaseModel basefilelistmodel = baseParser(param.getCommand());
			FileModel filelistmodel = new FileModel(
					basefilelistmodel.getCommand_id(),
					basefilelistmodel.getPhone_number(),
					basefilelistmodel.getCommand_type(), param.getCommand());
			if (download_fragment != null)
				((DownloadFragment) download_fragment)
						.updateView(filelistmodel);
			break;
		// delete file command
		case 10:
			BaseModel deletedfilelistmodel = baseParser(param.getCommand());
			FileModel mydeletedfilelistmodel = new FileModel(
					deletedfilelistmodel.getCommand_id(),
					deletedfilelistmodel.getPhone_number(),
					deletedfilelistmodel.getCommand_type(), param.getCommand());
			if (download_fragment != null)
				((DownloadFragment) download_fragment)
						.updateView(mydeletedfilelistmodel);
			break;
		// download file command
		case 11:
			BaseModel downloadfilelistmodel = baseParser(param.getCommand());
			FileModel mydownloadfilelistmodel = new FileModel(
					downloadfilelistmodel.getCommand_id(),
					downloadfilelistmodel.getPhone_number(),
					downloadfilelistmodel.getCommand_type(), param.getCommand());
			if (download_fragment != null)
				((DownloadFragment) download_fragment)
						.updateView(mydownloadfilelistmodel);
			break;
		case 12:
			BaseModel showOnlineMap = mapParser(param.getCommand());
			ShowOnlineMapModel showOnlineMapModel = (ShowOnlineMapModel) showOnlineMap;
			if (updater != null)
				((LiveMap) updater).updateMap(showOnlineMapModel);
			break;
		case 13:
			BaseModel direct = directParser(param.getCommand());
			DirectWIFIResponseModel directModel = (DirectWIFIResponseModel) direct;
			if (deviceInfo_fragment != null)
				((DeviceInfoFragment) deviceInfo_fragment)
						.updateConnection(directModel);
			break;

		case 14:
			BaseModel direct3g = direct3GParser(param.getCommand());
			Direct3GResponseModel direct3gModel = (Direct3GResponseModel) direct3g;
			if (deviceInfo_fragment != null)
				((DeviceInfoFragment) deviceInfo_fragment)
						.update3GConnection(direct3gModel);
			break;
		case 15:
			BaseModel orderstatus = orderStatusParser(param.getCommand());
			OrderStatusResponseModel orderstatusResponseModel = (OrderStatusResponseModel) orderstatus;
			if (history_fragment != null)
				((HistoryFragment) history_fragment)
						.updateOrder(orderstatusResponseModel);
			break;
		case 16:
			
			BaseModel ping = pingParser(param.getCommand());
			PingResponseModel pingModel = (PingResponseModel) ping;
			if (deviceInfo_fragment != null)
				((DeviceInfoFragment) deviceInfo_fragment)
						.updatePing(pingModel);
			break;
		default:
			break;
		}
	}

	// uses only sms to provide connection in the beginning and then return
	// status of client
	public static void setConnection(BaseModel model, Fragment fragment) {
		deviceInfo_fragment = fragment;
		statusParam param = new statusParam(model);
		Container.getProviderManager().setSmsProvider();
		Container.getProviderManager().send(param);
	}

	// only brings back status of client.
	public static void getStatus(BaseModel model, Fragment fragment) {
		deviceInfo_fragment = fragment;
		statusParam param = new statusParam(model);
		Container.getProviderManager().send(param);
	}

	// WIFI DIRECT
	public static void turnWifiOnOff(BaseModel model, Fragment fragment) {
		deviceInfo_fragment = fragment;
		WifiParam param = new WifiParam(model);
		Container.getProviderManager().setSmsProvider();
		Container.getProviderManager().send(param);
	}

	public static void turn3GOnOff(BaseModel model, Fragment fragment) {
		deviceInfo_fragment = fragment;
		_3GParam param = new _3GParam(model);
		Container.getProviderManager().setSmsProvider();
		Container.getProviderManager().send(param);
	}
	public static void restart(BaseModel model, Fragment fragment) {
		deviceInfo_fragment = fragment;
		RestartParam param = new RestartParam(model);
		Container.getProviderManager().setSmsProvider();
		Container.getProviderManager().send(param);
	}
	public static void ping(BaseModel model, Fragment fragment) {
		deviceInfo_fragment = fragment;
		PingParam param = new PingParam(model);
		Container.getProviderManager().setSmsProvider();
		Container.getProviderManager().send(param);
	}
	public static void soundRecord(Command command) {
		VoiceRecordParam param = new VoiceRecordParam(command);
		Container.getProviderManager().send(param);
		// TODO
		// add command to database
	}

	public static void orderStatus(Command command, Fragment fragment) {
		history_fragment = fragment;
		OrderStatusParam param = new OrderStatusParam(command);
		Container.getProviderManager().send(param);
		// TODO
		// add command to database
	}

	public static void videoRecord(Command command) {
		VideoRecordParam param = new VideoRecordParam(command);
		Container.getProviderManager().send(param);
		// TODO
		// add command to database
	}
	public static void videoMotoinRecord(Command command){
		VideoMotionRecordParam param = new VideoMotionRecordParam(command);
		Container.getProviderManager().send(param);
	}
	public static void capturePhoto(Command command) {
		CapturePhotoParam param = new CapturePhotoParam(command);
		Container.getProviderManager().send(param);
		// TODO
		// add command to database
	}
	public static void captureMotionPhoto(Command command){
		CaptureMotionPhotoParam param = new CaptureMotionPhotoParam(command);
		Container.getProviderManager().send(param);
	}
	public static void flightMode(Command command) {
		FlightModeParam param = new FlightModeParam(command);
		Container.getProviderManager().send(param);
		// TODO
		// add command to database
	}

	public static void callDevice(BaseModel model) {
		// CallDeviceParam param = new CallDeviceParam(model);
		Uri tel = Uri.parse("tel:" + model.getPhone_number());
		Intent callIntent = new Intent(Intent.ACTION_CALL, tel);

		if (callIntent.resolveActivity(Container.activity.getPackageManager()) != null) {
			Container.activity.startActivity(callIntent);
		}
	}

	public static void getLocation(Command command) {
		GetlocationParam param = new GetlocationParam(command);
		Container.getProviderManager().send(param);
		// TODO
		// add command to database
	}

	public static void showOnlineMap(BaseModel model, MapUpdater updater) {
		Manager.updater = updater;
		ShowOnlineMapParam param = new ShowOnlineMapParam(model);
		Container.getProviderManager().send(param);
	}

	public static void getFileList(BaseModel model, Fragment fragment) {
		download_fragment = fragment;
		GetFileListParam param = new GetFileListParam(model);
		Container.getProviderManager().send(param);
	}

	// public static void getLocalFileList(BaseModel model, Fragment fragment) {
	// download_fragment = fragment;
	// GetFileListParam param = new GetFileListParam(model);
	// Container.getProviderManager().send(param);
	// }
	public static void deleteFile(BaseModel model, Fragment fragment) {
		download_fragment = fragment;
		DeleteDownloadParam param = new DeleteDownloadParam(
				(DeleteDownloadModel) model);
		Container.getProviderManager().send(param);
	}

	public static void downloadFile(BaseModel model, Fragment fragment) {
		download_fragment = fragment;
		DeleteDownloadParam param = new DeleteDownloadParam(
				(DeleteDownloadModel) model);
		Container.getProviderManager().send(param);
	}

	private static BaseModel stringParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}
		try {
			long command_id = Long.parseLong(received_command.substring(
					star_index.get(0) + 1, star_index.get(1)));
			String commandType = received_command.substring(
					star_index.get(1) + 1, star_index.get(2));
			String phone_number = received_command.substring(
					star_index.get(2) + 1, star_index.get(3));
			String iMEI = received_command.substring(star_index.get(3) + 1,
					star_index.get(4));
			String serial_number = received_command.substring(
					star_index.get(4) + 1, star_index.get(5));
			String latitude = received_command.substring(star_index.get(5) + 1,
					star_index.get(6));
			String longitude = received_command.substring(
					star_index.get(6) + 1, star_index.get(7));
			String battery_level = received_command.substring(
					star_index.get(7) + 1, star_index.get(8));
			String memory_total = received_command.substring(
					star_index.get(8) + 1, star_index.get(9));
			String memory_left = received_command.substring(
					star_index.get(9) + 1, star_index.get(10));
			int wifi_state = Integer.parseInt(received_command.substring(
					star_index.get(10) + 1, star_index.get(11)));
			String iPaddress = received_command.substring(
					star_index.get(11) + 1, star_index.get(12));

			BaseModel model = new StatusResponceModel(command_id, phone_number,
					commandType, iMEI, serial_number, latitude, longitude,
					battery_level, memory_total, memory_left, wifi_state,
					iPaddress);
			return model;
		} catch (Exception e) {
			return null;
		}

	}

	public static BaseModel direct3GParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}

		long commandid = Long.parseLong((String) (received_command.subSequence(
				star_index.get(0) + 1, star_index.get(1))));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String status = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		BaseModel model = new Direct3GResponseModel(commandid, phonenumber,
				commandType, status);
		return model;
	}
	public static BaseModel pingParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}

		long commandid = Long.parseLong((String) (received_command.subSequence(
				star_index.get(0) + 1, star_index.get(1))));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String status = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		BaseModel model = new PingResponseModel(commandid, phonenumber,
				commandType, status);
		return model;
	}

	public static BaseModel orderStatusParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}

		long commandid = Long.parseLong((String) (received_command.subSequence(
				star_index.get(0) + 1, star_index.get(1))));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String status = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		BaseModel model = new OrderStatusResponseModel(commandid, phonenumber,
				commandType, status);
		return model;
	}

	public static BaseModel directParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}

		long commandid = Long.parseLong((String) (received_command.subSequence(
				star_index.get(0) + 1, star_index.get(1))));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String ssid = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		String password = received_command.substring(star_index.get(4) + 1,
				star_index.get(5));
		String status = received_command.substring(star_index.get(5) + 1,
				star_index.get(6));
		BaseModel model = new DirectWIFIResponseModel(commandid, phonenumber,
				commandType, ssid, password, status);
		return model;
	}

	public static BaseModel mapParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}

		long commandid = Long.parseLong((String) (received_command.subSequence(
				star_index.get(0) + 1, star_index.get(1))));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String location = received_command.substring(star_index.get(3) + 1,
				star_index.get(4))
				+ "$"
				+ received_command.substring(star_index.get(4) + 1,
						star_index.get(5));
		BaseModel model = new ShowOnlineMapModel(commandid, phonenumber,
				commandType, location);
		return model;
	}

	public static BaseModel baseParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}

		long commandid = Long.parseLong((String) (received_command.subSequence(
				star_index.get(0) + 1, star_index.get(1))));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		BaseModel model = new BaseModel(commandid, phonenumber, commandType);
		return model;
	}
}
