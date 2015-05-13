/**
 * 
 */
package ir.rayacell.mahdaclient.services;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.dao.OrderDao;
import ir.rayacell.mahdaclient.model.Orders;
import android.content.Intent;
import android.os.Handler;

/**
 * @author rayacell May 5, 2015
 */
public class MotionVideoRecorderTimerManager {

	public volatile static MotionVideoRecorderPreService pService;
	private String orderId;
	private Orders order;
	private OrderDao dao;
	public static int countViD = 1;
	public MotionVideoRecorderTimerManager(Intent intent) {
		
		orderId = intent.getExtras().getString("orderId");
		dao = new OrderDao(App.getContext());
		order = findOrder(dao, orderId);
		order.status = "3";
		dao.update(Long.parseLong(orderId), order);
		
		pService = new MotionVideoRecorderPreService(intent);
		int ot = Integer.parseInt(intent.getExtras().getString("orderTime"));
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				order.status = "4";
				dao.update(Long.parseLong(orderId), order);
				
				pService.pause();
				pService.stop();
				pService = null;
			}
		}, ot * 60 * 1000);
	}
	private Orders findOrder(OrderDao dao, String orderId2) {
		Orders order = dao.read(Long.parseLong(orderId2));
		return order == null ? null : order;
	}
}
