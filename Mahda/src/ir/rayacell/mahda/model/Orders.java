package ir.rayacell.mahda.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class Orders {

	public Orders(String type, String number) {
		this.number = number;
		this.type = type;
	}

	public Orders() {

	}

	public Orders(String type, String number, String ip, String date,
			String duration, int status) {
		this.type = type;
		this.duration = duration;
		this.number = number;
		this.date = date;
		this.ip = ip;
	}
	public Orders(String type, String number, String ip, String date,
			String duration, int status ,String orderTime) {
		this.type = type;
		this.orderTime = orderTime;
		this.duration = duration;
		this.number = number;
		this.date = date;
		this.ip = ip;
	}
	public Orders(String type, String number, String ip, String date, int status) {
		this.type = type;
		this.number = number;
		this.date = date;
		this.ip = ip;
	}

	@Column(type = ColumnType.INTEGER, isPrimaryKey = true, isAutoincrement = true)
	public int _id;
	@Column(type = ColumnType.TEXT)
	public String orderTime;
	@Column(type = ColumnType.TEXT)
	public String type;
	@Column(type = ColumnType.TEXT)
	public String ip;
	@Column(type = ColumnType.TEXT)
	public String number;
	@Column(type = ColumnType.TEXT)
	public String date;
	@Column(type = ColumnType.TEXT)
	public String duration;
	@Column(type = ColumnType.INTEGER)
	public int status;

}
