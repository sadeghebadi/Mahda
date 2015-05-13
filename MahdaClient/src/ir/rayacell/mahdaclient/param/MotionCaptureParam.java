package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.model.Command;

public class MotionCaptureParam extends BaseParam {

	private String date_and_time;
	private String orderTime;
	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	private int count;

	private int delay;

	public MotionCaptureParam(Command command) {
		super(command.getCommand_id(), command.getPhone_number(), command
				.getCommand_type());
		// command type must be sat when making the params
		this.orderTime= command.getOrderTime();
		this.date_and_time = command.getDate_and_time();
		this.count = command.getRepetition_count();
		this.delay = command.getDelay();
	}

	public String getDate_and_time() {
		return date_and_time;
	}

	public int getCount() {
		return count;
	}

	public int getDelay() {
		return delay;
	}
}
