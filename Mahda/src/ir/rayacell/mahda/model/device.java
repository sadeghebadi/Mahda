package ir.rayacell.mahda.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;


public class device {
	@Column(type = ColumnType.INTEGER, isPrimaryKey = true , isAutoincrement = true)
	public int _id;
	@Column(type = ColumnType.TEXT)
	public  String name;
	@Column(type = ColumnType.TEXT)
	public  String number;
	@Column(type = ColumnType.TEXT)
	public  String lat;
	@Column(type = ColumnType.TEXT)
	public  String lng;
	@Column(type = ColumnType.TEXT)
	public String imei;
}
