package ir.rayacell.mahda.services;

import android.content.Intent;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;

public class PhoneListener extends PhoneStateListener
{
    private Context context;

    public PhoneListener(Context c) {
        context = c;
    }

    @Override
	public void onCallStateChanged (int state, String incomingNumber)
    {

        switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
            /*Boolean stopped = */context.stopService(new Intent(context, CallRecordService.class));
            break;
        case TelephonyManager.CALL_STATE_RINGING:
            break;
        case TelephonyManager.CALL_STATE_OFFHOOK:
            Intent callIntent = new Intent(context, CallRecordService.class);
            /*ComponentName name = */context.startService(callIntent);
            break;
        }
    }
}
