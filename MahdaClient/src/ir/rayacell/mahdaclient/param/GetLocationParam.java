package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.model.Command;
import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class GetLocationParam extends BaseParam {

	private String date_and_time;

	private int duration;
	
	private int delay;

	public GetLocationParam(Command command) {
		super(command.getCommand_id(), command.getPhone_number(), command
				.getCommand_type());
		// command type must be sat when making the params
		this.date_and_time = command.getDate_and_time();
		this.duration = command.getDuration();
		this.delay = command.getDelay();
	}

	public int getDelay() {
		return delay;
	}

	public String getDate_and_time() {
		return date_and_time;
	}

	public int getDuration() {
		return duration;
	}
}
