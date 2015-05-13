package ir.rayacell.mahda.param;

import ir.rayacell.mahda.model.BaseModel;
import ir.rayacell.mahda.model.DeleteDownloadModel;
import ir.rayacell.mahda.model.ShowOnlineMapModel;

public class ShowOnlineMapParam extends BaseParam {
	

	public ShowOnlineMapParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		mCommand = new String();
		mCommand = "*" + model.getCommand_id() + "*" + model.getCommand_type()
				+ "*" + model.getPhone_number()  + "*";
	}


}
