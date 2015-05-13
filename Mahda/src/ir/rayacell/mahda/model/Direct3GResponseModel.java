package ir.rayacell.mahda.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class Direct3GResponseModel extends BaseModel {
	@Column(type = ColumnType.TEXT)
	private String status;
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public Direct3GResponseModel(long commandid, String phonenumber, String commandtype  , String status) {
		super(commandid, phonenumber, commandtype);
		this.status = status;
	}
}
