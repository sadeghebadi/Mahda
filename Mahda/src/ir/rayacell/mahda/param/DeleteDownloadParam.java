package ir.rayacell.mahda.param;

import ir.rayacell.mahda.model.DeleteDownloadModel;

public class DeleteDownloadParam extends BaseParam {
	
	private String file_name;

	public DeleteDownloadParam(DeleteDownloadModel model) {
		super(model.getCommand_id(), model.getPhone_number(), model
				.getCommand_type());
		this.file_name = model.getFile_Name();
		mCommand = new String();
		mCommand = "*" + model.getCommand_id() + "*" + model.getCommand_type()
				+ "*" + model.getPhone_number() + "*" + model.getFile_Name() + "*";
	}

	public String getFile_name() {
		return file_name;
	}

}
