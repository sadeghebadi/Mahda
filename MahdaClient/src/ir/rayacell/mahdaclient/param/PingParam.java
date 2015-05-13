package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.Direct3GResponseModel;
import ir.rayacell.mahdaclient.model.DirectWIFIResponseModel;
import ir.rayacell.mahdaclient.model.PingResponseModel;
import ir.rayacell.mahdaclient.model.ShowOlineMapResponceModel;
import ir.rayacell.mahdaclient.model.StatusResponceModel;

public class PingParam extends BaseParam {

	public PingParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		PingResponseModel mModel = (PingResponseModel) model;
		mCommand = new String();
		mCommand = "*" + mModel.getCommand_id() + "*"
				+ mModel.getCommand_type() + "*" + mModel.getPhone_number()
				+ "*" +mModel.getStatus()+ "*";
	}
}
