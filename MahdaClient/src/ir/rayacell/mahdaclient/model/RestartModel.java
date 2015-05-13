package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class RestartModel extends BaseModel {

	public RestartModel(long commandid, String phonenumber, String commandtype ) {
		super(commandid, phonenumber, commandtype);
		
	}
}
