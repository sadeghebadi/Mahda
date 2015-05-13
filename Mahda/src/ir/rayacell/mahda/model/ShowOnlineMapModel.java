package ir.rayacell.mahda.model;

import com.google.android.gms.maps.model.LatLng;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;
import ir.rayacell.mahda.model.BaseModel;

public class ShowOnlineMapModel extends BaseModel {

	private LatLng location;
	public LatLng getLocation() {
		return location;
	}
	public void setLocation(LatLng location) {
		this.location = location;
	}
	public ShowOnlineMapModel(long commandid, String phonenumber, String commandtype,
			String latlng) {
		super(commandid, phonenumber, commandtype);
		this.location = new LatLng(Double.parseDouble(latlng.split("\\$")[0]), Double.parseDouble(latlng.split("\\$")[1]));
	}


	

}
