package ir.rayacell.mahdaclient.executer;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.DeleteDownloadModel;

import java.io.File;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import android.util.Log;

public class DownloadFileExecuter {
	FileTransferManager fileTransferManager;

	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	public void sendFile(final String userID, final String path)
			throws XMPPException {
		Thread thread = new Thread() {

			public void run() {
				ServiceDiscoveryManager sdm = ServiceDiscoveryManager
						.getInstanceFor(Container.getXmppConnection());

				if (sdm == null)
					sdm = new ServiceDiscoveryManager(
							Container.getXmppConnection());

				sdm.addFeature("http://jabber.org/protocol/disco#info");
				sdm.addFeature("jabber:iq:privacy");
				fileTransferManager = new FileTransferManager(
						Container.getXmppConnection());
				FileTransferNegotiator.setServiceEnabled(
						Container.getXmppConnection(), true);

				OutgoingFileTransfer fileTransfer = fileTransferManager
						.createOutgoingFileTransfer(userID);
				System.out.println("status is:" + fileTransfer.getStatus());
				try {
					fileTransfer.sendFile(new File(path),
							"this is the description");
					System.out.println("status is:" + fileTransfer.getStatus());
					System.out.println("sent .. just");
					while (!fileTransfer.isDone()) {

							Log.i("transfere file", "sending file status "
									+ fileTransfer.getStatus() + "progress: "
									+ fileTransfer.getProgress());
							if (fileTransfer.getStatus() == Status.error) {
								fileTransfer.cancel();
								break;
							}

					}
					Log.i("transfere file", "sending file done");
					System.out.println(fileTransfer.getFileName()
							+ "has been successfully transferred.");

					System.out.println("The Transfer is "
							+ fileTransfer.isDone());

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	public DownloadFileExecuter(final BaseModel model) {
		// send file{
		try {
			sendFile(Container.getChat().getParticipant(),
					DEFAULT_STORAGE_LOCATION + "/"
							+ ((DeleteDownloadModel) model).getFile_Name());
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// DeleteDownloadModel deletemodel = (DeleteDownloadModel) model;
		// File dir = new File(DEFAULT_STORAGE_LOCATION);
		// if (!dir.exists()) {
		// try {
		// dir.mkdirs();
		// } catch (Exception e) {
		// // return null;
		// }
		// }
		// File file[] = dir.listFiles();
		// String files = new String();
		// if (!(Container.getProviderManager().getProvider() instanceof
		// InternetProvider)) {
		// files = "internet_fail*";
		// } else if (file.length != 0) {
		//
		// Log.d("Files", "Size: " + file.length);
		// for (int i = 0; i < file.length; i++) {
		// // if (file[i].getName().equals(deletemodel.getFile_Name())) {
		//
		// // file[i].delete();
		//
		// // } else {
		// String value = null;
		// long Filesize = file[i].length() / 1024;// call function and
		// // convert bytes
		// // into Kb
		// if (Filesize >= 1024) {
		// value = Filesize / 1024 + " MB";
		// } else {
		// value = Filesize + " KB";
		// }
		// Log.d("Files", "FileName:" + file[i].getName());
		// files += file[i].getName() + "#" + value + "*";
		// // }
		// }
		// } else {
		// files = "empty*";
		// }
		// FilesListModel fileslistmodel = new FilesListModel(
		// model.getCommand_id(), NetworkManager.getServerNumber(),
		// model.getCommand_type(), files);
		// Manager.sendFilesList(fileslistmodel);
	}

}
