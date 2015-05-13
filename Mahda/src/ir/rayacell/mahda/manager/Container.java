package ir.rayacell.mahda.manager;

import ir.rayacell.mahda.DeviceActivity;
import ir.rayacell.mahda.MainActivity;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.fragment.TestFragment;
import ir.rayacell.mahda.provider.ProviderManager;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;

public class Container {
	private static ProviderManager mProviderManagerInstance;
	public static MainActivity activity;
	public static HashMap<Integer, String> orderMap = new HashMap<Integer, String>();
	public static HashMap<Integer, String> orderStatusMap = new HashMap<Integer, String>();
	// private static Manager mManager;
	public static DeviceActivity BASEACTIVITY;
	public static TestFragment mTestFragmentInstance;
	private static XMPPConnection xmppConnection;
	private static Chat nchat;
	private static FileTransferManager FTM;
	private static Manager managerInstance;
	public static void setManager(Manager manager){
		if(managerInstance == null){
			managerInstance = manager;
		}
	}
	public static Manager getManager(){
		return managerInstance;
	}
	public static void setChat(Chat chat) {
		nchat = chat;
	}

	public static Chat getChat() {
		return nchat;
	}

	public static void setXmppConnection(XMPPConnection connection) {
		if (xmppConnection == null)
			xmppConnection = connection;
	}

	public static XMPPConnection getXmppConnection() {
		return xmppConnection;
	}

	// private static MapView mMapView;
	// private static GoogleMap mGoogleMap;
	//
	// public static MapView getMapView(){
	// return mMapView;
	// }
	//
	// public static void setMapView(MapView map){
	// mMapView = map;
	// }

	public static void initOrderMap() {
		String[] orderStatus = activity.getResources().getStringArray(
				R.array.orderstatus);
		String[] orders = activity.getResources()
				.getStringArray(R.array.orders);
		for (int i = 0; i < orders.length; i++) {
			orderMap.put(i + 1, orders[i]);
		}
		for (int i = 0; i < orderStatus.length; i++) {
			orderStatusMap.put(i, orderStatus[i]);
		}

	}

	public static ProviderManager getProviderManager() {
		if (mProviderManagerInstance == null) {
			mProviderManagerInstance = new ProviderManager(activity);
			return mProviderManagerInstance;
		}
		return mProviderManagerInstance;
	}

	public static void setTestFragment(TestFragment fragment) {
		if (mTestFragmentInstance == null)
			mTestFragmentInstance = fragment;
	}

	public static TestFragment getTestFragment() {
		return mTestFragmentInstance;
	}

	public static void setFTM(FileTransferManager manager) {
		if (FTM == null)
			FTM = manager;
	}
	public static FileTransferManager getFTM() {
		// TODO Auto-generated method stub
		return FTM;
	}

	// public static void setGoogleMap(GoogleMap map) {
	// mGoogleMap = map;
	// }
	//
	// public static GoogleMap getGoogleMap(){
	// return mGoogleMap;
	// }
}
