package ir.rayacell.mahda.param;

import ir.rayacell.mahda.model.Command;

public class VideoMotionRecordParam extends BaseParam {

	private String date_and_time;

	private int duration;
	private int quality;
	private int orderTime;
	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public VideoMotionRecordParam(Command command) {
		super(command.getCommand_id(), command.getPhone_number(), command
				.getCommand_type());
		// command type must be sat when making the params
		this.date_and_time = command.getDate_and_time();
		this.orderTime = command.getOrdertime();
		this.duration = command.getDuration();
		this.quality = command.getQuality();
		makeVideoCommand(command);
	}

	public String getDate_and_time() {
		return date_and_time;
	}

	public int getDuration() {
		return duration;
	}

	private String makeVideoCommand(Command command) {
		mCommand = new String();
		mCommand = "*" + command.getCommand_id() + "*"
				+ command.getCommand_type() + "*" + command.getPhone_number()
				+ "*" + command.getDate_and_time() + "*"
				+ command.getDuration() + "*" + command.getQuality() + "*"+ command.getOrdertime()+ "*";
		return mCommand;
	}
}
