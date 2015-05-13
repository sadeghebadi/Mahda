package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class DirectWIFIModel extends BaseModel {


	public DirectWIFIModel(long commandid, String phonenumber, String commandtype ) {
		super(commandid, phonenumber, commandtype);
	}
}
