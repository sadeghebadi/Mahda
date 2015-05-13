package ir.rayacell.mahda.model;

import java.util.ArrayList;

import ir.rayacell.mahda.model.BaseModel;

public class FileModel extends BaseModel {

	private ArrayList<MyFile> mFilesList;

	public ArrayList<MyFile> getmFilesList() {
		return mFilesList;
	}

	public FileModel(long commandid, String phonenumber, String commandtype,
			String files) {
		super(commandid, phonenumber, commandtype);
		mFilesList = new ArrayList<MyFile>();
		fileListParser(files);
	}

	public void fileListParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}

		long commandid = Long.parseLong((String) (received_command.subSequence(
				star_index.get(0) + 1, star_index.get(1))));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));

		for (int i = 3; i < star_index.size() - 1; i++) {
			String tempfiles = received_command.substring(
					star_index.get(i) + 1, star_index.get(i + 1));
			int spliter = tempfiles.indexOf('#');
			if (spliter != -1) {
				MyFile mfile = new MyFile(tempfiles.substring(0, spliter),
						tempfiles.substring(spliter + 1));
				mFilesList.add(mfile);
			}
		}
	}

	public class MyFile {
		private String name;
		private String size;

		public MyFile(String Name, String Size) {
			this.name = Name;
			this.size = Size;
		}

		public String getName() {
			return name;
		}

		public String getSize() {
			return size;
		}
	}

}
