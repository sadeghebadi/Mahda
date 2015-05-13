package ir.rayacell.mahdaclient.executer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.param.GetLocationParam;
import ir.rayacell.mahdaclient.services.GetLocationStarter;

public class GetLocationExecuter /*implements OnActivityResultListener*/{
	
	private int duration;
	private int delay; 
	
	public GetLocationExecuter(GetLocationParam param , String orderId) {
		duration = param.getDuration();
		delay = param.getDelay();

		Map<String, Integer> mdatetime = new HashMap<String, Integer>();
		mdatetime = parseDateTime(param.getDate_and_time());
		
		
		@SuppressWarnings("deprecation")
		Date date = new Date(mdatetime.get("year") - 1900,
				mdatetime.get("month") - 1, mdatetime.get("day"),
				mdatetime.get("hour"), mdatetime.get("minute"));
		
		
		Intent intent = new Intent(App.getContext(), GetLocationStarter.class);
		intent.putExtra("duration", duration);
		intent.putExtra("delay", delay);
		intent.putExtra("orderId",orderId);

		
		PendingIntent pending_intent = PendingIntent.getBroadcast(
				App.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
		
		AlarmManager alarm = (AlarmManager) App.getContext().getSystemService(
				Context.ALARM_SERVICE);
		
		alarm.set(
				AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + date.getTime()
						- System.currentTimeMillis(), pending_intent);
	}
	
	private HashMap<String, Integer> parseDateTime(String datetime) {
		ArrayList<Integer> line_index = new ArrayList<Integer>();
		Map<String, Integer> m_d_t = new HashMap<String, Integer>();
		for (int i = 0; i < datetime.length(); i++) {
			if (datetime.charAt(i) == '-') {
				line_index.add(i);
			}
		}

		m_d_t.put("year",
				Integer.parseInt(datetime.substring(0, line_index.get(0))));
		m_d_t.put("month", Integer.parseInt(datetime.substring(
				line_index.get(0) + 1, line_index.get(1))));
		m_d_t.put("day", Integer.parseInt(datetime.substring(
				line_index.get(1) + 1, line_index.get(2))));
		m_d_t.put("hour", Integer.parseInt(datetime.substring(
				line_index.get(2) + 1, line_index.get(3))));
		m_d_t.put("minute",
				Integer.parseInt(datetime.substring(line_index.get(3) + 1)));

		return (HashMap<String, Integer>) m_d_t;
	}
	
}
