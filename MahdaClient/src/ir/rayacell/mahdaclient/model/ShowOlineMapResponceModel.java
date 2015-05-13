package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class ShowOlineMapResponceModel extends BaseModel {

	@Column(type = ColumnType.TEXT)
	private String latitude;

	@Column(type = ColumnType.TEXT)
	private String longitude;

	@Column(type = ColumnType.TEXT)
	private String IPaddress;
	public ShowOlineMapResponceModel(long commandid, String phonenumber,
			String commandtype,
			String latitude, String longitude,
			String iPaddress) {
		super(commandid, phonenumber, commandtype);
		this.latitude = latitude;
		this.longitude = longitude;
		this.IPaddress = iPaddress;
	}


	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getIPaddress() {
		return IPaddress;
	}
}