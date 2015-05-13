package ir.rayacell.mahdaclient.manager;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;

import android.view.View;
import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.MainActivity;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.executer.AirplaneModeExecuter;
import ir.rayacell.mahdaclient.executer.CallServerExecuter;
import ir.rayacell.mahdaclient.executer.PhotoCaptureExecuter;
import ir.rayacell.mahdaclient.executer.VideoRecordExecuter;
import ir.rayacell.mahdaclient.executer.VoiceRecordExecuter;
import ir.rayacell.mahdaclient.provider.ProviderManager;
import ir.rayacell.mahdaclient.provider.Xmpp3GProvider;

public class Container {
	private static ProviderManager mProviderManagerInstance;
	public static MainActivity activity;
//	public static VoiceRecordExecuter mVoiceRecorderExecuter;
//	public static VideoRecordExecuter mVideoRecorderExecuter;
	public static PhotoCaptureExecuter mPhotoCaptureExecuter;
	public static CallServerExecuter mCallServerExecuter;
	public static AirplaneModeExecuter mAirplaneModeExecuter;
	public static View surfaceview;
	private static Xmpp3GProvider xmpp3gProvider;
	
private static XMPPConnection xmppConnection;
private static Chat nchat;
	
public static void setChat(Chat chat){
	nchat = chat;
}
public static Chat getChat(){
	return nchat;
}
public static void setXmpp3GProvider (Xmpp3GProvider provider){
	if(xmpp3gProvider == null){
		xmpp3gProvider = provider;
	}
}
public static Xmpp3GProvider getXmpp3gProvider(){
	if(xmpp3gProvider == null){
		xmpp3gProvider = new Xmpp3GProvider(mProviderManagerInstance, activity);
	}
	return xmpp3gProvider;
}

	public static void setXmppConnection(XMPPConnection connection){
		xmppConnection = connection;
	}
	public static XMPPConnection getXmppConnection(){
		return xmppConnection;
	}
	public static AirplaneModeExecuter getAirplaneModeExecuter(){
		if (mAirplaneModeExecuter == null) {
			mAirplaneModeExecuter = new AirplaneModeExecuter();
			return mAirplaneModeExecuter;
		}
		return mAirplaneModeExecuter;
	}
	
	public static CallServerExecuter getCallServerExecuter(){
		if (mCallServerExecuter == null) {
			mCallServerExecuter = new CallServerExecuter();
			return mCallServerExecuter;
		}
		return mCallServerExecuter;
	}

	public static ProviderManager getProviderManager() {
		if (mProviderManagerInstance == null) {
			mProviderManagerInstance = new ProviderManager(activity);
			return mProviderManagerInstance;
		}
		return mProviderManagerInstance;
	}

//	public static VoiceRecordExecuter getVoiceRecordExecuter() {
//		if (mVoiceRecorderExecuter == null) {
//			mVoiceRecorderExecuter = new VoiceRecordExecuter();
//		}
//		return mVoiceRecorderExecuter;
//	}

	public static View getVideoRecorderView() {
//		if (mVideoRecorderExecuter == null) {
//			mVideoRecorderExecuter = new VideoRecordExecuter(activity
//					.getWindow().getDecorView().findViewById(R.id.sv_camera));
//		}
		surfaceview = activity.getWindow().getDecorView().findViewById(R.id.sv_camera);
		return surfaceview;
	}

	public static PhotoCaptureExecuter getPhotoCaptureExecuter() {
//		if (mPhotoCaptureExecuter == null) {
//			mPhotoCaptureExecuter = new PhotoCaptureExecuter(activity
//					.getWindow().getDecorView().findViewById(R.id.iv_photos));
//		}
		return mPhotoCaptureExecuter;
	}
	public static WifiApManager getWifiApManager(){
		if(apManagerInstance == null){
			apManagerInstance = new WifiApManager(App.getContext(), new Utils());
		}
		return apManagerInstance;
	}
	public static void setWifiApManager(WifiApManager mWifiApManager) {
		apManagerInstance = mWifiApManager;
	}
	private static WifiApManager apManagerInstance;
}
