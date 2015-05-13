//package ir.rayacell.mahdaclient.services;
//
//import ir.rayacell.mahdaclient.Constants;
//import ir.rayacell.mahdaclient.manager.Container;
//import ir.rayacell.mahdaclient.manager.NetworkManager;
//import ir.rayacell.mahdaclient.model.BaseModel;
//import ir.rayacell.mahdaclient.model.Command;
//import ir.rayacell.mahdaclient.provider.InternetProvider;
//import ir.rayacell.mahdaclient.provider.ProviderManager;
//import ir.rayacell.mahdaclient.security.EncryptDecrypt;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.AsyncTask;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//
//public class ServerService extends IntentService {
//	final int SocketServerPORT = 8849;
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
//			NetworkManager.getIpAddress();
//			serverSocket = new ServerSocket(SocketServerPORT);
//			while (true) {
//				synchronized (this) {
//
//					socket = serverSocket.accept();
//					dataInputStream = new DataInputStream(
//							socket.getInputStream());
//					dataOutputStream = new DataOutputStream(
//							socket.getOutputStream());
//					String messageFromClient = "";
//
//					messageFromClient = dataInputStream.readUTF();
//					messageFromClient = EncryptDecrypt
//							.decrypt(messageFromClient);
//					if (messageFromClient.equals("busy")) {
//						new AsyncTask<String, Void, Void>() {
//
//							@Override
//							protected Void doInBackground(String... arg0) {
//								System.out
//										.println(arg0
//												+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//								try {
//									serverSocket.close();
//								} catch (IOException e) {
//									e.printStackTrace();
//								}
//								return null;
//							}
//						}.execute(message);
//					}
//
//					count++;
//					message = new String();
//					message = "#" + count + " from " + socket.getInetAddress()
//							+ ":" + socket.getPort() + "\n"
//							+ "Msg from client: " + messageFromClient + "\n";
//					// insert to db order
//					new AsyncTask<String, Void, Void>() {
//
//						@Override
//						protected Void doInBackground(String... arg0) {
//							System.out.println(arg0);
//							Log.d("message from client", arg0[0]
//									+ "##################################3");
//							model = new Command(1, "1", arg0[0].toString(), "",
//									1, 1, 1);
//							return null;
//						}
//					}.execute(message);
//					String msgReply = messageFromClient.split("\\*")[messageFromClient
//							.split("\\*").length - 1];
//					msgReply = EncryptDecrypt.encrypt(msgReply);
//					dataOutputStream.writeUTF(msgReply);
//					// update sen
//					publishResults(messageFromClient);
//				}
//			}
//		} catch (IOException e1) {
//			e1.printStackTrace();
//			final String errMsg = e1.toString();
//			System.out.println(" errrrrrrrroorrrrrr" + errMsg);
//		} catch (InvalidKeyException e1) {
//			e1.printStackTrace();
//		} catch (IllegalBlockSizeException e1) {
//			e1.printStackTrace();
//		} catch (BadPaddingException e1) {
//			e1.printStackTrace();
//		} catch (NoSuchAlgorithmException e1) {
//			e1.printStackTrace();
//		} catch (NoSuchPaddingException e1) {
//			e1.printStackTrace();
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
//	private void publishResults(String result) {
//		ResponseReceiver mResponseReceiver = new ResponseReceiver();
//		IntentFilter mActionIntentFilter = new IntentFilter(
//				Constants.BROADCAST_ACTION);
//		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
//				mResponseReceiver, mActionIntentFilter);
//
//		Intent intent = new Intent(Constants.BROADCAST_ACTION);
//		intent.putExtra(Constants.RESULT_KEY, result);
//		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//	}
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		try {
//			serverSocket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
