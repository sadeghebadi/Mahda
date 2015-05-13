package ir.rayacell.mahda.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class PingResponseModel extends BaseModel {
	@Column(type = ColumnType.TEXT)
	private String status;
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public PingResponseModel(long commandid, String phonenumber, String commandtype  , String status) {
		super(commandid, phonenumber, commandtype);
		this.status = status;
	}
}
