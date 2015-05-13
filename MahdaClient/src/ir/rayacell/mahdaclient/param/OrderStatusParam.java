package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.OrderStatusResponseModel;

public class OrderStatusParam extends BaseParam {

	public OrderStatusParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		OrderStatusResponseModel mModel = (OrderStatusResponseModel) model;
		mCommand = new String();
		mCommand = "*" + mModel.getCommand_id() + "*"
				+ mModel.getCommand_type() + "*" + mModel.getPhone_number()
				+ "*" +mModel.getStatus()+ "*";
	}
}
