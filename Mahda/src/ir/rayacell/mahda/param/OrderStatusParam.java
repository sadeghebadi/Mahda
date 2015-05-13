package ir.rayacell.mahda.param;

import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.BaseModel;
import ir.rayacell.mahda.model.Command;

public class OrderStatusParam extends BaseParam {

	public OrderStatusParam(BaseModel model ) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		mCommand = new String();
		mCommand = "*" + model.getCommand_id() + "*" + model.getCommand_type()
				+ "*" + model.getPhone_number() + "*" +((Command)model).getRepetition_count()+ "*";
	}
}
