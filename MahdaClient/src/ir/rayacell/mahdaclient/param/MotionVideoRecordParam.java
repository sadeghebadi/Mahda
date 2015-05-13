package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.model.Command;
import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class MotionVideoRecordParam extends BaseParam {

	private String date_and_time;

	private int duration;
	private int quality;
	private String orderTime;
	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public MotionVideoRecordParam(Command command) {
		super(command.getCommand_id(), command.getPhone_number(), command
				.getCommand_type());
		// command type must be sat when making the params
		this.date_and_time = command.getDate_and_time();
		this.duration = command.getDuration();
		this.quality = command.getQuality();
		this.orderTime = command.getOrderTime();
	}

	public String getDate_and_time() {
		return date_and_time;
	}

	public int getDuration() {
		return duration;
	}

	/**
	 * @return the orderTime
	 */
	public String getOrderTime() {
		return orderTime;
	}

	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
}
