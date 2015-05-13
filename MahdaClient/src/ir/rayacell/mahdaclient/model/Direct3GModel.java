package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class Direct3GModel extends BaseModel {

	private String IP;
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public Direct3GModel(long commandid, String phonenumber, String commandtype ,String ipAd) {
		super(commandid, phonenumber, commandtype);
		this.IP = ipAd;
		
	}
}
