package ir.rayacell.mahdaclient.executer;

import ir.rayacell.mahdaclient.manager.Manager;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.model.StatusModel;
import ir.rayacell.mahdaclient.model.StatusResponceModel;

public class GetStatusExecuter {

	public GetStatusExecuter(StatusModel model) {
		NetworkManager.setIpAddress(model.getIp_Address());
		StatusResponceModel responcemodel = (StatusResponceModel) Utils
				.statusMaker(model);
		Manager.sendStatus(responcemodel);
	}

}
