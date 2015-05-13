package ir.rayacell.mahda.param;

import ir.rayacell.mahda.model.BaseModel;

public class CallDeviceParam extends BaseParam {

	public CallDeviceParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
//		mCommand = new String();
//		mCommand = "*" + model.getCommand_type() + "*"
//				+ NetworkManager.getIpAddress() + "*" ;
	}
}
