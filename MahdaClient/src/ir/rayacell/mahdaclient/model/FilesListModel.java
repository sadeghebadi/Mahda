package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class FilesListModel extends BaseModel {

	@Column(type = ColumnType.TEXT)
	private String Files;


	public FilesListModel(long commandid, String phonenumber,
			String commandtype, String files) {
		super(commandid, phonenumber, commandtype);
		this.Files = files;
	}

	public String getFiles() {
		return Files;
	}

}