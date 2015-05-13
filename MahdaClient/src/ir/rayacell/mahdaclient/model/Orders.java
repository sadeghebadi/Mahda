package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;


public class Orders {
	public Orders(String type, String number){
		this. number = number;
		this.type = type;
	}
	public Orders(){
		
	}
	public Orders(int _id , String type , String number ,String ip,String date , int status){
		this.type = type;
		this._id=_id;
		this.number = number;
		this.date = date;
		this.ip = ip;
	}
	@Column(type = ColumnType.INTEGER, isPrimaryKey = true  )
	public int _id;
	
	@Column(type = ColumnType.TEXT)
	public  String type;
	@Column(type = ColumnType.TEXT)
	public  String ip;
	@Column(type = ColumnType.TEXT)
	public  String number;
	@Column(type = ColumnType.TEXT)
	public  String date;
	@Column(type = ColumnType.TEXT)
	public String status;
	
	
}
