package ir.rayacell.mahdaclient.param;

import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.FilesListModel;

public class FilesListParam extends BaseParam {

	public FilesListParam(BaseModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		FilesListModel mModel = (FilesListModel) model;
		mCommand = new String();
		mCommand = "*" + mModel.getCommand_id() + "*"
				+ mModel.getCommand_type() + "*" + mModel.getPhone_number()
				+ "*" + mModel.getFiles();
	}
}
