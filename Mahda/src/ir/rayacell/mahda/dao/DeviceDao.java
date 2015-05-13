package ir.rayacell.mahda.dao;

import com.google.android.gms.maps.model.LatLng;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.model.device;
import android.content.Context;
import android.widget.Toast;
import apl.vada.lib.db.SQLiteDAO;

public class DeviceDao extends SQLiteDAO<device>{

	public DeviceDao( Context context) {
		super(device.class, context,"mahda.sqlite");
	}

	public void updateDevice(device tmp) {
		DeviceDao dao = new DeviceDao(App.getContext());
		dao.update(tmp._id, tmp);
//		adapter.notifyDataSetInvalidated();
	}

	public device loadDeviceFromDB(int position) {
		DeviceDao dao = new DeviceDao(App.getContext());
		device d = dao.readAllAsc().get(position);
//		adapter.notifyDataSetInvalidated();
		return d;
	}
	public void deleteDeviceFromDB(int position){
		DeviceDao dao = new DeviceDao(App.getContext());
		device d =loadDeviceFromDB(position);
		dao.deleteWhere("number", d.number);
	}
	public void insertIntoDB(String name, String number, LatLng latlng) {
		DeviceDao dao = new DeviceDao(App.getContext());
		device d = new device();
		d.name = name;
		d.number = number;
		d._id = dao.readAllAsc().size();
		d.lat = latlng.latitude + "";
		d.lng = latlng.longitude + "";
		try {
			dao.create(d);
		} catch (Exception e) {
			Toast.makeText(App.getContext(), "این شماره پیش از ثبت شده است", 4000).show();
		}
	}
	
	

}
