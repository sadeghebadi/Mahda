package ir.rayacell.mahdaclient.manager;

import ir.rayacell.mahdaclient.App;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.format.Formatter;

public class NetworkManager {
	public static String dstAddress/* ="192.168.1.101" */;
	public static String _3gServer;
	public static int dstPort = 8085;
	private static String serverPhoneNumber;

	public static void setServerNumber(String servernumber) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(App.getContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("ServerNumber", servernumber);
		editor.apply();
		serverPhoneNumber = servernumber;
	}
	public static void set3Gserver(String ip){
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(App.getContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("_3gServer", ip);
		editor.apply();
	}
	public static String get3GServer(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
		String _3gtserver = preferences.getString("_3gServer","");
		return _3gtserver;
	}
	public static String getServerNumber() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
		String serverPhoneNumber = preferences.getString("ServerNumber","");
		return  serverPhoneNumber;
	}

	public static void setIpAddress(String ip) {
		dstAddress = new String();
		dstAddress = ip;
	}

	@SuppressWarnings("deprecation")
	public static String getIpAddress() {
		String ip = "";

		WifiManager wm = (WifiManager) App.getContext().getSystemService(
				Context.WIFI_SERVICE);
		ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces
						.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface
						.getInetAddresses();
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress.nextElement();

					if (inetAddress.isSiteLocalAddress()) {
						ip /*
							 * += "SiteLocalAddress: " +
							 */= inetAddress.getHostAddress()/* + "\n" */;
					}

				}

			}

		} catch (SocketException e) {
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}

		return ip;
		// return "192.95.47.194";
	}
}
