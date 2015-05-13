package ir.rayacell.mahda.dao;

import com.google.android.gms.maps.model.LatLng;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.model.device;
import ir.rayacell.mahda.model.deviceInfo;
import android.content.Context;
import android.widget.Toast;
import apl.vada.lib.db.SQLiteDAO;

public class DeviceInfoDao extends SQLiteDAO<deviceInfo>{

	public DeviceInfoDao( Context context) {
		super(deviceInfo.class, context,"mahda.sqlite");
	}

	public void updateDeviceInfo(deviceInfo tmp) {
		DeviceInfoDao dao = new DeviceInfoDao(App.getContext());
		dao.update(tmp._id, tmp);
//		adapter.notifyDataSetInvalidated();
	}
	public deviceInfo loadDeviceFromDB2(String phonenumber) {
		DeviceInfoDao dao = new DeviceInfoDao(App.getContext());
		if(dao.readAllAsc().size() == 0){
			return null;
		}
		deviceInfo d = dao.readWhere("phonenumber", phonenumber);
		return d;
	}
	public deviceInfo loadDeviceFromDB() {
		DeviceInfoDao dao = new DeviceInfoDao(App.getContext());
		if(dao.readAllAsc().size() == 0){
			return null;
		}
		deviceInfo d = dao.readAllAsc().get(0);
		return d;
	}

	public void insertIntoDB(String phonenumber, int PROGRESS3G, int PROGRESSCONNECTION ,int PROGRESSDIRECT ) {
		DeviceInfoDao dao = new DeviceInfoDao(App.getContext());
		deviceInfo info = dao.readWhere("phonenumber", phonenumber);
		if(info == null){
			deviceInfo d = new deviceInfo(PROGRESS3G,PROGRESSCONNECTION,PROGRESSDIRECT,phonenumber);
//			d.phonenumber = phonenumber;
//			d.PROGRESS3G = PROGRESS3G;
//			d.PROGRESSCONNECTION = PROGRESSCONNECTION;
//			d.PROGRESSDIRECT = PROGRESSDIRECT;
			
			dao.create(d);
		}else{
			deviceInfo d = new deviceInfo(PROGRESS3G,PROGRESSCONNECTION,PROGRESSDIRECT,phonenumber);
			d._id = info._id;
//			info.phonenumber = phonenumber;
//			info.setPROGRESS3G(PROGRESS3G);
//			info.PROGRESSCONNECTION = PROGRESSCONNECTION;
//			info.PROGRESSDIRECT = PROGRESSDIRECT;
			dao.updateDeviceInfo(d);
			
		}
		
		
	}

}
