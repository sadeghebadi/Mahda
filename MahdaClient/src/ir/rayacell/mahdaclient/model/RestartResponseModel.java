package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class RestartResponseModel extends BaseModel {
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public RestartResponseModel(long commandid, String phonenumber, String commandtype , String status) {
		super(commandid, phonenumber, commandtype);
		this.status = status;
	}
}
