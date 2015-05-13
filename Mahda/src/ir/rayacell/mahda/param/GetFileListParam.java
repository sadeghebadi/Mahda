package ir.rayacell.mahda.param;

import ir.rayacell.mahda.model.BaseModel;

public class GetFileListParam extends BaseParam {

	public GetFileListParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		mCommand = new String();
		mCommand = "*" + model.getCommand_id() + "*" + model.getCommand_type()
				+ "*" + model.getPhone_number() + "*";
	}
}
