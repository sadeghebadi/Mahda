package ir.rayacell.mahda.param;

import ir.rayacell.mahda.model.Command;

public class VoiceRecordParam extends BaseParam {

	private String date_and_time;
	private int duration;

	public VoiceRecordParam(Command command) {
		super(command.getCommand_id(), command.getPhone_number(), command
				.getCommand_type());
		// command type must be sat when making the params
		this.date_and_time = command.getDate_and_time();
		this.duration = command.getDuration();
		makeVoiceCommand(command);
	}

	public String getDate_and_time() {
		return date_and_time;
	}

	public int getDuration() {
		return duration;
	}

	private String makeVoiceCommand(Command command) {
		mCommand = new String();
		mCommand = "*" + command.getCommand_id() + "*"
				+ command.getCommand_type() + "*" + command.getPhone_number()
				+ "*" + command.getDate_and_time() + "*"
				+ command.getDuration() + "*";
		return mCommand;
	}
}
