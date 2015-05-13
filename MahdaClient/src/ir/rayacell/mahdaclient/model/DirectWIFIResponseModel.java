package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class DirectWIFIResponseModel extends BaseModel {
	@Column(type = ColumnType.TEXT)
	private String ssid;
	@Column(type = ColumnType.TEXT)
	private String password;
	@Column(type = ColumnType.TEXT)
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public DirectWIFIResponseModel(long commandid, String phonenumber, String commandtype , String ssid, String password , String status) {
		super(commandid, phonenumber, commandtype);
		this.ssid = ssid;
		this.password = password;
		this.status = status;
	}
}
