package ir.rayacell.mahda.provider;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import ir.rayacell.mahda.App;
import ir.rayacell.mahda.MainActivity;
import ir.rayacell.mahda.dao.OrderDao;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.Orders;
import ir.rayacell.mahda.param.BaseParam;
import ir.rayacell.mahda.security.EncryptDecrypt;
import ir.rayacell.mahda.util.Util;

public class SMSProvider extends BaseProvider {

	public SMSProvider(ProviderManager providerManager, MainActivity activity) {
		super(providerManager, activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean connect(BaseParam param) {
		// sendSms(activity, param.getPhone_number(), param.mCommand);

		return false;
	}

	private long createOrder(String msg) {
		OrderDao dao = new OrderDao(App.getContext());
		String[] orderStr = msg.split("\\*");
		long id = 0;
		Orders order;
		if (orderStr[2].equals("1") || orderStr[2].equals("0")
				|| orderStr[2].equals("6") || orderStr[2].equals("11")
				|| orderStr[2].equals("10") || orderStr[2].equals("12") ||orderStr[2].equals("15")||orderStr[2].equals("14")||orderStr[2].equals("13")||orderStr[2].equals("16")||orderStr[2].equals("17")) {
			// order = new Orders(orderStr[2],orderStr[3],orderStr[4],new
			// Date(System.currentTimeMillis()).toGMTString() , 0);
		} else if (orderStr[2].equals("10")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
			String date = orderStr[4];
			String[] darr = date.split("_");
			date = darr[2] + "-" + darr[3] + "-" + darr[4] + "-" + darr[5]
					+ "-" + darr[6];
			order = new Orders(orderStr[2], orderStr[3],
					NetworkManager.dstAddress, date, 0);
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

	@Override
	public boolean send(BaseParam param) {
		// TODO Auto-generated method stub
		System.out.println(param.getCommand_type() + "  sms  "
				+ "==============================");

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(activity, 0,
				new Intent(DELIVERED), 0);
		SmsManager sms = SmsManager.getDefault();
		// 9370131836
		// sends ip address of server as message
		// String temp = "hi";
		String params = param
				.getCommand();
//		params = EncryptDecrypt.cipher(params, 4);
//		params = new SimpleCrypto().enc(params);
		//TODO
//		try {
//			params = EncryptDecrypt.encrypt(params);
//		} catch (InvalidKeyException e) {
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
//		} catch (IllegalBlockSizeException e) {
//			e.printStackTrace();
//		} catch (BadPaddingException e) {
//			e.printStackTrace();
//		}
//		sms.sendDataMessage(param.getPhone_number(), null, (short) 56889, params.getBytes()/* message.getBytes() *//*
//																 * NetworkManager.
//																 * getIpAddress
//																 * ()
//																 *//*
//																	 * getIPAddress(
//																	 * true)
//																	 * .getBytes
//																	 * ()
//																	 */,
//				sentPI, deliveredPI);
		String paramsB = Util.stringToBinary(params);
		sms.sendTextMessage(param.getPhone_number(), null , paramsB  , sentPI, deliveredPI);
		createOrder(param.getCommand());
//		TelephonyManager tm = (TelephonyManager) App.getContext()
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		if (tm.getDataState() == tm.DATA_CONNECTED) {
//			Container.getProviderManager().setXmpp3GProvider();
//		}
		Container.BASEACTIVITY.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(Container.BASEACTIVITY, "دستور ارسال شد", 2000)
						.show();
			}
		});
		return false;
	}

	@Override
	public void recieve(BaseParam param) {
		// TODO Auto-generated method stub
		mProviderManager.recieve(param);
	}

	// public void sendSms(Context context, String phoneNumber, String message)
	// {
	// String SENT = "SMS_SENT";
	// String DELIVERED = "SMS_DELIVERED";
	//
	// PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
	// new Intent(SENT), 0);
	//
	// PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
	// new Intent(DELIVERED), 0);
	// SmsManager sms = SmsManager.getDefault();
	// // 9370131836
	// // sends ip address of server as message
	// // String temp = "hi";
	// sms.sendDataMessage(phoneNumber, null, (short) 56889,
	// message.getBytes()/* message.getBytes() *//*
	// * NetworkManager.
	// * getIpAddress()
	// *//*
	// * getIPAddress(true)
	// * .getBytes()
	// */, sentPI,
	// deliveredPI);
	// }

	// public void onReceive(BaseParam param) {
	// recieve(param);
	// }
}
