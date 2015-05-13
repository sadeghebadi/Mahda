//package ir.rayacell.mahdaclient.provider;
//
//import ir.rayacell.mahdaclient.model.BaseModel;
//import ir.rayacell.mahdaclient.model.Command;
//import ir.rayacell.mahdaclient.Constants;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//
//public class ServerService extends IntentService {
//	final int SocketServerPORT = 8081;
//	int count = 0;
//	Socket socket = null;
//	DataInputStream dataInputStream = null;
//	DataOutputStream dataOutputStream = null;
//	private ServerSocket serverSocket;
//	private BaseModel model = null;
//	private String message = "";
//
//	public ServerService() {
//		super("serverIntentService");
//
//	}
//
//	@Override
//	protected void onHandleIntent(Intent arg0) {
//
//		try {
//			serverSocket = new ServerSocket(SocketServerPORT);
//			while (true) {
//				socket = serverSocket.accept();
//				dataInputStream = new DataInputStream(socket.getInputStream());
//				dataOutputStream = new DataOutputStream(
//						socket.getOutputStream());
//				String messageFromClient = "";
//
//				messageFromClient = dataInputStream.readUTF();
//				if (messageFromClient.equals("busy")) {
//					new AsyncTask<String, Void, Void>() {
//
//						@Override
//						protected Void doInBackground(String... arg0) {
//							System.out
//									.println(arg0
//											+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//							try {
//								serverSocket.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//							return null;
//						}
//					}.execute(message);
//				}
//
//				count++;
//				message = new String();
//				message = "#" + count + " from " + socket.getInetAddress()
//						+ ":" + socket.getPort() + "\n" + "Msg from client: "
//						+ messageFromClient + "\n";
//
//				new AsyncTask<String, Void, Void>() {
//
//					@Override
//					protected Void doInBackground(String... arg0) {
//						System.out.println(arg0);
//						Log.d("message from client", arg0[0]
//								+ "##################################3");
//						model = new Command(1L, 1+"", arg0[0].toString(), "", 1, 1,
//								1);
//						return null;
//					}
//
//				}.execute(message);
//				String msgReply = "Hello from Android, you are #" + count;
//				dataOutputStream.writeUTF(msgReply);
//				
//				publishResults(message);
//			}
//		} catch (IOException e1) {
//			e1.printStackTrace();
//			final String errMsg = e1.toString();
//			System.out.println(" errrrrrrrroorrrrrr" + errMsg);
//		} finally {
//			Log.d("finally", "finally &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//			if (socket != null) {
//				try {
//					socket.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//			if (dataInputStream != null) {
//				try {
//					dataInputStream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//			if (dataOutputStream != null) {
//				try {
//					dataOutputStream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//		}
//
//	}
//
//	private void publishResults( String result) {
//	    Intent intent = new Intent(Constants.BROADCAST_ACTION);
//	    intent.putExtra(Constants.RESULT_KEY, result);
//	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//	  }
//}
