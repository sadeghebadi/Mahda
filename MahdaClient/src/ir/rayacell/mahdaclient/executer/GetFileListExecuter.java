package ir.rayacell.mahdaclient.executer;

import java.io.File;

import android.util.Log;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.Manager;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.manager.Utils;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.FilesListModel;
import ir.rayacell.mahdaclient.model.StatusModel;
import ir.rayacell.mahdaclient.model.StatusResponceModel;
import ir.rayacell.mahdaclient.provider.InternetProvider;
import ir.rayacell.mahdaclient.provider.Xmpp3GProvider;

public class GetFileListExecuter {
	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	public GetFileListExecuter(BaseModel model) {

		File dir = new File(DEFAULT_STORAGE_LOCATION);

		// test dir for existence and writeability
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				// return null;
			}
		}
		// String path = dir.getPath();
		// Log.d("Files", "Path: " + path);
		// File f = new File(path);
		File file[] = dir.listFiles();
		String files = new String();
		if (!(Container.getProviderManager().getProvider() instanceof InternetProvider) && !(Container.getProviderManager().getProvider() instanceof Xmpp3GProvider)) {
			files = "internet_fail*";
		} else if (file.length != 0) {

			Log.d("Files", "Size: " + file.length);
			for (int i = 0; i < file.length; i++) {
				String value = null;
				long Filesize = file[i].length() / 1024;// call function and
														// convert bytes into Kb
				if (Filesize >= 1024) {
					value = Filesize / 1024 + " MB";
				} else {
					value = Filesize + " KB";
				}
				Log.d("Files", "FileName:" + file[i].getName());
				files += file[i].getName() + "#" + value + "*";
			}
		} else {
			files = "empty*";
		}
		FilesListModel fileslistmodel = new FilesListModel(
				model.getCommand_id(), NetworkManager.getServerNumber(),
				model.getCommand_type(), files);
		Manager.sendFilesList(fileslistmodel);
	}

}
