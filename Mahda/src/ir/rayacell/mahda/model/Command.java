package ir.rayacell.mahda.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;
import ir.rayacell.mahda.model.BaseModel;

public class Command extends BaseModel {

	@Column(type = ColumnType.INTEGER)
	private int duration;

	@Column(type = ColumnType.INTEGER)
	private int repetition_count;

	@Column(type = ColumnType.INTEGER)
	private int delay;

	@Column(type = ColumnType.TEXT)
	private String date_and_time;

	@Column(type = ColumnType.INTEGER)
	private  int ordertime;
	

	public int getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(int ordertime) {
		this.ordertime = ordertime;
	}

	@Column(type = ColumnType.INTEGER)
	private int quality;
	
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public Command(long commandid, String phonenumber, String commandtype,
			String datetime, int duration, int repetition, int delay , int quality) {
		super(commandid, phonenumber, commandtype);
		this.date_and_time = datetime;
		this.duration = duration;
		this.repetition_count = repetition;
		this.delay = delay;
		this.quality = quality;
	}
	public Command(long commandid, String phonenumber, String commandtype,
			String datetime, int duration, int repetition, int delay , int quality,int ordertime) {
		super(commandid, phonenumber, commandtype);
		this.date_and_time = datetime;
		this.ordertime = ordertime;
		this.duration = duration;
		this.repetition_count = repetition;
		this.delay = delay;
		this.quality = quality;
	}
	public Command(long commandid, String phonenumber, String commandtype,
			String datetime  , String n_ll, int duration, int repetition, int delay  ,int ordertime) {
		super(commandid, phonenumber, commandtype);
		this.date_and_time = datetime;
		this.ordertime = ordertime;
		this.duration = duration;
		this.repetition_count = repetition;
		this.delay = delay;
	}
	public Command(long commandid, String phonenumber, String commandtype,
			String datetime , int duration, int repetition, int delay  ) {
		super(commandid, phonenumber, commandtype);
		this.date_and_time = datetime;
		this.duration = duration;
		this.repetition_count = repetition;
		this.delay = delay;
	}
	public Command(long commandid, String phonenumber, String commandtype , int repetition  ) {
		super(commandid, phonenumber, commandtype);
		this.repetition_count = repetition;
	}
	public Command(int commandid, String phonenumber, String commandtype,
			String datetime, int duration, int repetition, int delay,
			String ordertime) {
		
		super(commandid, phonenumber, commandtype);
		this.date_and_time = datetime;
		this.ordertime = Integer.parseInt(ordertime);
		this.duration = duration;
		this.repetition_count = repetition;
		this.delay = delay;
	}
	public String getDate_and_time() {
		return date_and_time;
	}

	public int getDuration() {
		return duration;
	}

	public int getRepetition_count() {
		return repetition_count;
	}

	public int getDelay() {
		return delay;
	}
	

}
