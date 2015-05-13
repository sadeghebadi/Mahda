package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class BaseModel {

	@Column(type = ColumnType.NUMERIC, isAutoincrement = true, isPrimaryKey = true)
	private long command_id;

	@Column(type = ColumnType.NUMERIC)
	private String phone_number;

	@Column(type = ColumnType.TEXT)
	private String command_type;

	public BaseModel(long commandid, String phonenumber, String commandtype) {
		this.command_id = commandid;
		this.phone_number = phonenumber;
		this.command_type = commandtype;
	}

	public long getCommand_id() {
		return command_id;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public String getCommand_type() {
		return command_type;
	}
	
	public void setPhoneNumber(String phonenumber){
		this.phone_number = phonenumber;
	}

}
