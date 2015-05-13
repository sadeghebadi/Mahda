package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class DeleteDownloadModel extends BaseModel {

	@Column(type = ColumnType.TEXT)
	private String file_name;

	public DeleteDownloadModel(long commandid, String phonenumber, String commandtype,
			String filename) {
		super(commandid, phonenumber, commandtype);
		this.file_name = filename;
	}

	public String getFile_Name() {
		return file_name;
	}

	

}
