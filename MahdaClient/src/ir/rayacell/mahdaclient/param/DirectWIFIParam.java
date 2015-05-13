package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.DirectWIFIResponseModel;
import ir.rayacell.mahdaclient.model.ShowOlineMapResponceModel;
import ir.rayacell.mahdaclient.model.StatusResponceModel;

public class DirectWIFIParam extends BaseParam {

	public DirectWIFIParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		DirectWIFIResponseModel mModel = (DirectWIFIResponseModel) model;
		mCommand = new String();
		mCommand = "*" + mModel.getCommand_id() + "*"
				+ mModel.getCommand_type() + "*" + mModel.getPhone_number()
				+ "*" + mModel.getSsid() + "*"+mModel.getPassword() + "*" +mModel.getStatus()+ "*";
	}
}
