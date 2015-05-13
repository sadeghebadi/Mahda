package ir.rayacell.mahda.manager;


import ir.rayacell.mahda.App;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

public class NetworkManager {
	public static String dstAddress/* = "192.168.1.110"*/;
	public static int dstPort = 8849;
	public static String _3gServer;
	public static String clientPhoneNumber /*= "9302964951"*/;
	
	public static void setIpAddress(String ip){
		dstAddress = new String();
		dstAddress = ip;
	}
	@SuppressWarnings("deprecation")
	public static String getIpAddress() {
		String ip = "";
		
		WifiManager wm = (WifiManager) App.getContext().getSystemService(Context.WIFI_SERVICE);
		ip = Formatter.formatIpAddress(wm.getConnectionInfo()
				.getIpAddress());
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
						ip /*+= "SiteLocalAddress: "
								+*/= inetAddress.getHostAddress()/* + "\n"*/;
					}

				}

			}

		} catch (SocketException e) {
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}

		return ip;
//		return "192.95.47.205";
	}
}
