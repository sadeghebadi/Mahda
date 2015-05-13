package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.StatusResponceModel;

public class statusParam extends BaseParam {

	public statusParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		StatusResponceModel mModel = (StatusResponceModel) model;
		mCommand = new String();
		mCommand = "*" + mModel.getCommand_id() + "*"
				+ mModel.getCommand_type() + "*" + mModel.getPhone_number()
				+ "*" + mModel.getIMEI() + "*" + mModel.getSerial_number()
				+ "*" + mModel.getLatitude() + "*" + mModel.getLongitude()
				+ "*" + mModel.getBattery_level() + "*"
				+ mModel.getMemory_total() + "*" + mModel.getMemory_left()
				+ "*" + mModel.getWifi_state() + "*" + mModel.getIPaddress()
				+ "*";
	}
}
