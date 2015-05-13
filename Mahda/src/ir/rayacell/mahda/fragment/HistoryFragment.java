package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.dao.DeviceDao;
import ir.rayacell.mahda.dao.OrderDao;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.DateTimeManager;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.Command;
import ir.rayacell.mahda.model.OrderStatusResponseModel;
import ir.rayacell.mahda.model.Orders;
import ir.rayacell.mahda.model.device;
import ir.rayacell.mahda.util.CalendarTool;

import java.util.Collections;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.StringCallback;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;

public class HistoryFragment extends Fragment {

	private View v;
	private ListView mListView;
	private String orderidd;
	public int orderPosition;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_history, container, false);
		updateView(v);
		return v;
	}

	private String getImei(String number) {
		DeviceDao dao = new DeviceDao(App.getContext());
		device d = dao.readWhere("number", number);
		return d.imei.substring(0, 3);

	}

	List<Orders> orders;
	private HistoryListAdapter adapter;

	public void updateView(View v) {
		mListView = (ListView) v.findViewById(R.id.lv_history);
		OrderDao dao = new OrderDao(App.getContext());
		orders = dao.readAllWhere("number", NetworkManager.clientPhoneNumber);
		mListView.setDivider(null);
		Container.initOrderMap();
		mListView.setDividerHeight(5);
		Collections.reverse(orders);
	 adapter = new HistoryListAdapter(orders);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public class HistoryListAdapter extends ArrayAdapter<Orders> {

		private List<Orders> orders;
		private Context mcontext;
		private TextView tv_ordertype;
		private TextView tv_status;
		private TextView tv_date;

		public HistoryListAdapter(List<Orders> orders) {
			super(App.getContext(), android.R.layout.simple_list_item_1, orders);
			// Collections.reverse(orders);
			this.orders = orders;
		}

		private String convertToPersian(String date) {

			String[] gDate = date.split("_");
			int year = Integer.parseInt(gDate[2]);
			int month = Integer.parseInt(gDate[3]);
			int day = Integer.parseInt(gDate[4]);
			CalendarTool ct = new CalendarTool(year, month, day);

			String returnStri = gDate[0] + "_" + gDate[1] + "_"
					+ ct.getIranianDate().replace("/", "_") + "_" + gDate[5]
					+ "_" + gDate[6] + "_" + gDate[7] + "_" + gDate[8];
			return returnStri;
		}

		private String convertToPersianTwo(String date) {

			String[] gDate = date.split("-");
			int year = Integer.parseInt(gDate[0]);
			int month = Integer.parseInt(gDate[1]);
			int day = Integer.parseInt(gDate[2]);
			int hour = Integer.parseInt(gDate[3]);
			int minute = Integer.parseInt(gDate[4]);
			CalendarTool ct = new CalendarTool(year, month, day);

			String returnStri = ct.getIranianDate().replace("/", "-");
			return returnStri + " " + hour + ":" + minute;
		}

		@Override
		public long getItemId(final int position) {
			return getItem(position).hashCode();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				ViewHolder holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.history_list_item,
						parent, false);
				holder.tv_ordertype = (TextView) convertView
						.findViewById(R.id.tv_type);
				holder.position = position;
				holder.tv_status = (TextView) convertView
						.findViewById(R.id.tv_status);
				holder.btn_delete_order = (ImageButton) convertView
						.findViewById(R.id.btn_delete_order);
				holder.tv_date = (TextView) convertView
						.findViewById(R.id.tv_date);
				holder.btn_refresh = (ImageButton) convertView
						.findViewById(R.id.btn__refresh);
				convertView.setTag(holder);
			}
			final ViewHolder holder = (ViewHolder) convertView.getTag();

			holder.position = position;
			holder.tv_ordertype.setText(Container.orderMap.get(Integer
					.parseInt(orders.get(holder.position).type)));
			// holder.tv_ordertype.setText(orders.get(holder.position).type);
			holder.tv_status.setText(Container.orderStatusMap.get(orders
					.get(holder.position).status));
			String dur = orders.get(holder.position).duration;
			if (orders.get(holder.position).duration == null) {
				dur = "";
			}
			holder.tv_date.setText(convertToPersianTwo(orders
					.get(holder.position).date) + "   (" + dur+")");

			// File mfile = mFiles.get(position);
			// String name = convertToPersian(mfile.getName());
			// tv_filename.setText(name);
			// tv_filesize.setText(mfile.length() / 1000 + "");
			holder.btn_delete_order.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					OrderDao dao = new OrderDao(App.getContext());
					dao.delete(orders.get(position)._id);
					orders.remove(position);
					notifyDataSetChanged();

				}
			});
			holder.btn_refresh.setOnClickListener(new OnClickListener() {

				public  String checkOrderId(String orderId) {
					int tmp = Integer.parseInt(orderId.charAt(1) + "")
							+ Integer.parseInt(orderId.charAt(2) + "");
					orderId = tmp + orderId.substring(3, orderId.length());
					return orderId;

				}
				@Override
				public void onClick(View arg0) {
					 orderidd=  orders.get(holder.position).date.replace(
							"-", "").substring(
							orders.get(holder.position).date
									.indexOf("-"),
							orders.get(holder.position).date
									.replace("-", "").length())
							+getImei(NetworkManager.clientPhoneNumber);
					 orderidd = checkOrderId(orderidd);
					 orderPosition = position;
					Command command = new Command(0,
							NetworkManager.clientPhoneNumber, App.getContext()
									.getResources()
									
									.getString(R.string.command_order_status),
							Integer.parseInt(orderidd)
						 );
					Manager.orderStatus(command, HistoryFragment.this);
					
	/*				Container.BASEACTIVITY.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AsyncHttpGet ahg = new AsyncHttpGet("http://"
									+ NetworkManager.dstAddress + ":" + 5001
									+ "/order");
							ahg.addHeader(
									"orderId",
									orders.get(holder.position).date.replace(
											"-", "").substring(
											orders.get(holder.position).date
													.indexOf("-"),
											orders.get(holder.position).date
													.replace("-", "").length())
											+ getImei(NetworkManager.clientPhoneNumber));
							AsyncHttpClient.getDefaultInstance().executeString(
									ahg, new StringCallback() {

										@Override
										public void onCompleted(Exception e,
												AsyncHttpResponse source,
												final String result) {
											// update order
											Container.BASEACTIVITY
													.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															if (orders != null && holder != null) {
																updateOrder(
																		orders.get(holder.position),
																		result);
																holder.tv_status
																		.setText(Container.orderStatusMap
																				.get(orders
																						.get(holder.position).status));
															}
														}

													});
										}
									});
						}
					});*/
				}
				// }
			});
			// ImageButton btn_donwload = (ImageButton) rowView
			// .findViewById(R.id.btn__);
			// btn_donwload.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			//
			// }
			// });
			// btn_donwload.setVisibility(View.VISIBLE);
			notifyDataSetChanged();
			return convertView;
		}

		
	}

	private static class ViewHolder {
		TextView tv_ordertype;
		ImageButton btn_delete_order;
		TextView tv_status;
		TextView tv_date;
		int position;
		ImageButton btn_refresh;
	}
	public void updateOrder(OrderStatusResponseModel model) {
		
		Orders order =orders.get(orderPosition);
		OrderDao dao = new OrderDao(App.getContext());
		try {
			order.status = Integer.parseInt(model.getStatus());
			dao.update(order._id, order);
			adapter.notifyDataSetChanged();

		} catch (Exception e) {
		}
	}
}
