package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class OrderStatusModel extends BaseModel {

	public String status;
	public OrderStatusModel(long commandid, String phonenumber, String commandtype  , String status) {
		super(commandid, phonenumber, commandtype);
		this.status  = status;
	}
}
