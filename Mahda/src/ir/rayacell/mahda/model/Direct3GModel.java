package ir.rayacell.mahda.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class Direct3GModel extends BaseModel {


	public Direct3GModel(long commandid, String phonenumber, String commandtype,
			String ipaddress , String ssid , String password) {
		super(commandid, phonenumber, commandtype);
	}
}
