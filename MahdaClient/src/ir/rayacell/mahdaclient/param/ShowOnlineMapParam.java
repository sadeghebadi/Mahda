package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.ShowOlineMapResponceModel;
import ir.rayacell.mahdaclient.model.StatusResponceModel;

public class ShowOnlineMapParam extends BaseParam {

	public ShowOnlineMapParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		ShowOlineMapResponceModel mModel = (ShowOlineMapResponceModel) model;
		mCommand = new String();
		mCommand = "*" + mModel.getCommand_id() + "*"
				+ mModel.getCommand_type() + "*" + mModel.getPhone_number()
				+ "*" + mModel.getLatitude() + "*" + mModel.getLongitude()
				+ "*" + mModel.getIPaddress() + "*";
	}
}
