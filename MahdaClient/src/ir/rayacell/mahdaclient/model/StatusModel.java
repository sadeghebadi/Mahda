package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class StatusModel extends BaseModel {

	@Column(type = ColumnType.TEXT)
	private String IPaddress;

	public StatusModel(long commandid, String phonenumber, String commandtype,
			String ipaddress, String current_time) {
		super(commandid, phonenumber, commandtype);
		this.IPaddress = ipaddress;
		this.current_time = current_time;
	}

	public String getIp_Address() {
		return IPaddress;
	}

	public String current_time;

	public String getCurrent_time() {
		return current_time;
	}

}
