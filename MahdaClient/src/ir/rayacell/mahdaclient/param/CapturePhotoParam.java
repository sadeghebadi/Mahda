package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.model.Command;

public class CapturePhotoParam extends BaseParam {

	private String date_and_time;

	private int count;

	private int delay;

	public CapturePhotoParam(Command command) {
		super(command.getCommand_id(), command.getPhone_number(), command
				.getCommand_type());
		// command type must be sat when making the params
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
