package ir.rayacell.mahdaclient.param;

public class BaseParam {
	public String mCommand;

	private long command_id;

	private String phone_number;

	private String command_type;

	public BaseParam(long comandid, String phonenumber, String commandtype) {
		this.command_id = comandid;
		this.phone_number = phonenumber;
		this.command_type = commandtype;
	}

	public long getCommand_id() {
		return command_id;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public String getCommand_type() {
		return command_type;
	}

	public String getCommand() {
		return mCommand;
	}
}
