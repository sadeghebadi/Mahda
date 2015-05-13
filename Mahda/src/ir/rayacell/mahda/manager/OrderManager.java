package ir.rayacell.mahda.manager;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.dao.OrderDao;
import ir.rayacell.mahda.model.Orders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.R.integer;

public class OrderManager {

	private static boolean isInRange(String dBDateStr, String duration,
			String date_time, int type) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date currentCommandTime = null;
		try {
			currentCommandTime = sdf.parse(date_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] dbArrayStartTime = getTime(dBDateStr);

		Calendar bBDateAfterDuration = Calendar.getInstance();
		bBDateAfterDuration.set(dbArrayStartTime[0], dbArrayStartTime[1] - 1,
				dbArrayStartTime[2], dbArrayStartTime[3], dbArrayStartTime[4],
				0);
		bBDateAfterDuration
				.add(Calendar.SECOND, Integer.parseInt(duration) + 5);

		Calendar dBDateBeforeDuration = Calendar.getInstance();
		dBDateBeforeDuration.set(dbArrayStartTime[0], dbArrayStartTime[1] - 1,
				dbArrayStartTime[2], dbArrayStartTime[3], dbArrayStartTime[4],
				0);
		dBDateBeforeDuration.add(Calendar.SECOND, -5);
		if (currentCommandTime.before(bBDateAfterDuration.getTime())
				&& currentCommandTime.after(dBDateBeforeDuration.getTime())) {
			return true;
		}
		// 2015-01-28-11-02 4
		return false;
	}

	private static int[] getTime(String date_time) {
		String[] arr = date_time.split("-");
		int[] time = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			time[i] = Integer.parseInt(arr[i]);
		}
		return time;
	}
	// refresh nashode bashe
	private static boolean checkRecord(String date_time) {
		OrderDao dao = new OrderDao(App.getContext());
		// momken ast refresh nashode bashe
		ArrayList<Orders> flightOrders = (ArrayList<Orders>) dao.readAllWhere(
				"type",
				App.getContext().getResources()
						.getString(R.string.command_voice_record));
		for (int i = 0; i < flightOrders.size(); i++) {
			boolean rm = true;
			if (!flightOrders.get(i).number
					.equalsIgnoreCase(NetworkManager.clientPhoneNumber)) {
				flightOrders.remove(i);
			}
			if (rm) {
				if (isInRange(flightOrders.get(i).date,
						Integer.parseInt(flightOrders.get(i).duration) * 60
								+ "", date_time, -1)) {
					return false;
				}
			}
		}
		return true;
	}
	// refresh nashode bashe
	private static boolean checkIsFlightMode(String date_time) {
		OrderDao dao = new OrderDao(App.getContext());
		// momken ast refresh nashode bashe
		ArrayList<Orders> flightOrders = (ArrayList<Orders>) dao.readAllWhere(
				"type",
				App.getContext().getResources()
						.getString(R.string.command_flight_mode));
		for (int i = 0; i < flightOrders.size(); i++) {
			boolean rm = true;
			if (!flightOrders.get(i).number
					.equalsIgnoreCase(NetworkManager.clientPhoneNumber)) {
				flightOrders.remove(i);
			}
			if (rm) {
				if (isInRange(flightOrders.get(i).date,
						Integer.parseInt(flightOrders.get(i).duration) * 60
								+ "", date_time, -1)) {
					return false;
				}
			}
		}
		return true;
	}
	private static boolean checkCameraVideo(String date_time) {
		OrderDao dao = new OrderDao(App.getContext());

		ArrayList<Orders> camOrders = (ArrayList<Orders>) dao.readAllWhere(
				"type",
				App.getContext().getResources()
						.getString(R.string.command_video_record));

		for (int i = 0; i < camOrders.size(); i++) {
			boolean rm = true;
			if (!camOrders.get(i).number
					.equalsIgnoreCase(NetworkManager.clientPhoneNumber)) {
				camOrders.remove(i);
				rm = false;
			}
			if (rm) {
				if (isInRange(camOrders.get(i).date,
						Integer.parseInt(camOrders.get(i).duration) * 60 + "",
						date_time, 0)) {
					return false;
				}
			}
		}
		return true;
	}
	private static boolean checkCameraPhotoAndVoice(String date_time) {
		OrderDao dao = new OrderDao(App.getContext());

		ArrayList<Orders> camOrders = (ArrayList<Orders>) dao.readAllWhere(
				"type",
				App.getContext().getResources()
						.getString(R.string.command_video_record));

		for (int i = 0; i < camOrders.size(); i++) {
			boolean rm = true;
			if (!camOrders.get(i).number
					.equalsIgnoreCase(NetworkManager.clientPhoneNumber)) {
				camOrders.remove(i);
				rm = false;
			}
			if (rm) {
				if (isInRange(camOrders.get(i).date,
						Integer.parseInt(camOrders.get(i).duration) * 60 + "",
						date_time, 0)) {
					return false;
				}
			}
		}
		camOrders = (ArrayList<Orders>) dao.readAllWhere(
				"type",
				App.getContext()
						.getResources()
						.getString(R.string.command_capture_photo, "number",
								NetworkManager.clientPhoneNumber));

		for (int i = 0; i < camOrders.size(); i++) {
			boolean rm = true;
			if (!camOrders.get(i).number
					.equalsIgnoreCase(NetworkManager.clientPhoneNumber)) {
				camOrders.remove(i);
				rm = false;
			}
			if (rm) {
				if (isInRange(camOrders.get(i).date,
						Integer.parseInt(camOrders.get(i).duration) + "",
						date_time, 1)) {
					return false;
				}
			}
		}
		camOrders = (ArrayList<Orders>) dao.readAllWhere(
				"type",
				App.getContext()
						.getResources()
						.getString(R.string.command_capture_motion_photo, "number",
								NetworkManager.clientPhoneNumber));

		for (int i = 0; i < camOrders.size(); i++) {
			boolean rm = true;
			if (!camOrders.get(i).number
					.equalsIgnoreCase(NetworkManager.clientPhoneNumber)) {
				camOrders.remove(i);
				rm = false;
			}
			if (rm) {
				if (isInRange(camOrders.get(i).date,
						Integer.parseInt(camOrders.get(i).duration) + "",
						date_time, 1)) {
					return false;
				}
			}
		} 
		camOrders = (ArrayList<Orders>) dao.readAllWhere(
				"type",
				App.getContext()
						.getResources()
						.getString(R.string.command_video_motion_record, "number",
								NetworkManager.clientPhoneNumber));

		for (int i = 0; i < camOrders.size(); i++) {
			boolean rm = true;
			if (!camOrders.get(i).number
					.equalsIgnoreCase(NetworkManager.clientPhoneNumber)) {
				camOrders.remove(i);
				rm = false;
			}
			if (rm) {
				if (isInRange(camOrders.get(i).date,
						Integer.parseInt(camOrders.get(i).orderTime)* 60 + "",
						date_time, 1)) {
					return false;
				}
			}
		}
		camOrders = (ArrayList<Orders>) dao.readAllWhere(
				"type",
				App.getContext()
						.getResources()
						.getString(R.string.command_voice_record, "number",
								NetworkManager.clientPhoneNumber));

		for (int i = 0; i < camOrders.size(); i++) {
			boolean rm = true;
			if (!camOrders.get(i).number
					.equalsIgnoreCase(NetworkManager.clientPhoneNumber)) {
				camOrders.remove(i);
				rm = false;
			}
			if (rm) {
				if (isInRange(camOrders.get(i).date,
						Integer.parseInt(camOrders.get(i).orderTime)* 60 + "",
						date_time, 1)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean checker(String orderId, String date_time) {
		if (orderId.equals(App.getContext().getResources()
				.getString(R.string.command_capture_photo))
				|| orderId.equals(App.getContext().getResources()
						.getString(R.string.command_video_record))|| orderId.equals(App.getContext().getResources()
								.getString(R.string.command_capture_motion_photo))|| orderId.equals(App.getContext().getResources()
										.getString(R.string.command_video_motion_record))|| orderId.equals(App.getContext().getResources()
												.getString(R.string.command_voice_record))) {
			return checkCameraPhotoAndVoice(date_time) && checkIsFlightMode(date_time);

		}
		boolean isInFlight = checkIsFlightMode(date_time);
		if(!isInFlight){
			Container.BASEACTIVITY.setActionBarForFlight(true);
		}
		else{
			Container.BASEACTIVITY.setActionBarForFlight(false);

		}
		return isInFlight;
	}
}
