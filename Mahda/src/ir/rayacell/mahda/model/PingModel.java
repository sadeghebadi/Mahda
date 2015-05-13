package ir.rayacell.mahda.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class PingModel extends BaseModel {


	public PingModel(long commandid, String phonenumber, String commandtype,
			String ipaddress , String ssid , String password) {
		super(commandid, phonenumber, commandtype);
	}
}
