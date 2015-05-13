package ir.rayacell.mahda.param;

import ir.rayacell.mahda.model.Command;

public class CaptureMotionPhotoParam extends BaseParam {

	private String date_and_time;
	private int orderTime;



	private int count;
	
	private int delay;

	public CaptureMotionPhotoParam(Command command) {
		super(command.getCommand_id(), command.getPhone_number(), command
				.getCommand_type());
		// command type must be sat when making the params
		this.orderTime = command.getOrdertime();
		this.date_and_time = command.getDate_and_time();
		this.count = command.getRepetition_count();
		this.delay = command.getDelay();
		makeVoiceCommand(command);
	}

	public int getCount() {
		return count;
	}

	public int getDelay() {
		return delay;
	}

	public String getDate_and_time() {
		return date_and_time;
	}

	

	private String makeVoiceCommand(Command command) {
		mCommand = new String();
		mCommand = "*" + command.getCommand_id() + "*"
				+ command.getCommand_type() + "*" + command.getPhone_number()
				+ "*" + command.getDate_and_time() + "*"
				+ command.getRepetition_count() + "*" + command.getDelay() + "*"+ command.getOrdertime() + "*";
		return mCommand;
	}
}
