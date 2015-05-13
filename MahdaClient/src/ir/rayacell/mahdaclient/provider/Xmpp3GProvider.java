package ir.rayacell.mahdaclient.provider;

import ir.rayacell.mahdaclient.App;
import ir.rayacell.mahdaclient.Constants;
import ir.rayacell.mahdaclient.MainActivity;
import ir.rayacell.mahdaclient.R;
import ir.rayacell.mahdaclient.manager.Container;
import ir.rayacell.mahdaclient.manager.NetworkManager;
import ir.rayacell.mahdaclient.param.BaseParam;
import ir.rayacell.mahdaclient.security.EncryptDecrypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.ibb.provider.CloseIQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.OpenIQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.Nick;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.HeaderProvider;
import org.jivesoftware.smackx.provider.HeadersProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.pubsub.provider.AffiliationProvider;
import org.jivesoftware.smackx.pubsub.provider.AffiliationsProvider;
import org.jivesoftware.smackx.pubsub.provider.ConfigEventProvider;
import org.jivesoftware.smackx.pubsub.provider.EventProvider;
import org.jivesoftware.smackx.pubsub.provider.FormNodeProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemsProvider;
import org.jivesoftware.smackx.pubsub.provider.PubSubProvider;
import org.jivesoftware.smackx.pubsub.provider.RetractEventProvider;
import org.jivesoftware.smackx.pubsub.provider.SimpleNodeProvider;
import org.jivesoftware.smackx.pubsub.provider.SubscriptionProvider;
import org.jivesoftware.smackx.pubsub.provider.SubscriptionsProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class Xmpp3GProvider extends BaseProvider {
	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	public Xmpp3GProvider(
			ir.rayacell.mahdaclient.provider.ProviderManager providerManager,
			MainActivity activity) {
		super(providerManager, activity);
	}

	// public static void connect(String IMEI) {
	//
	// }

	// public String getImei(String number) {
	// DeviceDao dao = new DeviceDao(App.getContext());
	// device d = dao.readWhere("number", number);
	// return d.imei.substring(0, 3);
	//
	// }
	public static void publishResults(String result) {
		Intent intent = new Intent(Constants.BROADCAST_ACTION);
		intent.putExtra(Constants.RESULT_KEY, result);
		LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(
				intent);
	}

	public static void initChat() {
		ChatManager chatmanager = Container.getXmppConnection()
				.getChatManager();
		chatmanager.addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean arg1) {
				// TODO Auto-generated method stub
				chat.addMessageListener(new MessageListener() {

					@Override
					public void processMessage(Chat chat, Message message) {
						String meg;
						try {
							meg = EncryptDecrypt.decrypt(message.getBody());
							// publishResults(meg);
							final BaseParam param = new BaseParam(0,
									NetworkManager.getServerNumber(), null);
							param.mCommand = meg;
							Container.setChat(chat);
							// Container.getProviderManager().setXmpp3GProvider();
							Container.activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									Container.getProviderManager()
											.getProvider().recieve(param);
								}
							});
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalBlockSizeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BadPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						;
					}
				});
			}
		});
		// Container.setChat(newChat);

		
		
	}

	public static void sendMessage(String message) {
		// BufferedReader br = new BufferedReader(new
		// InputStreamReader(System.in));

		try {
			// if (message.equalsIgnoreCase("shutdown")) {
			Container.getChat().sendMessage(message);
			// }

		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public static long getIMEI() {
		TelephonyManager tMgr = (TelephonyManager) App.getContext()
				.getSystemService(App.getContext().TELEPHONY_SERVICE);
		String IMEI = tMgr.getDeviceId();
		if (IMEI == null) {
			return -1;
		} else {

			if (IMEI.equalsIgnoreCase(""))
				return 0;
			return Long.parseLong(IMEI);
			// return Long.parseLong("242424242");
			// return Long.parseLong(phoneNumber);
		}
	}

	public void configure() {

		ProviderManager pm = ProviderManager.getInstance();

		// The order is the same as in the smack.providers file

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			System.err
					.println("Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());
		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());
		pm.addExtensionProvider("delay", "urn:xmpp:delay",
				new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			System.err
					.println("Can't load class for org.jivesoftware.smackx.packet.Version");
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());
		pm.addIQProvider("open", "http://jabber.org/protocol/ibb",
				new OpenIQProvider());
		pm.addIQProvider("data", "http://jabber.org/protocol/ibb",
				new DataPacketProvider());
		pm.addIQProvider("close", "http://jabber.org/protocol/ibb",
				new CloseIQProvider());
		pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb",
				new DataPacketProvider());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());

		// SHIM
		pm.addExtensionProvider("headers", "http://jabber.org/protocol/shim",
				new HeadersProvider());
		pm.addExtensionProvider("header", "http://jabber.org/protocol/shim",
				new HeaderProvider());

		// PubSub
		pm.addIQProvider("pubsub", "http://jabber.org/protocol/pubsub",
				new PubSubProvider());
		pm.addExtensionProvider("create", "http://jabber.org/protocol/pubsub",
				new SimpleNodeProvider());
		pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub",
				new ItemsProvider());
		pm.addExtensionProvider("item", "http://jabber.org/protocol/pubsub",
				new ItemProvider());
		pm.addExtensionProvider("subscriptions",
				"http://jabber.org/protocol/pubsub",
				new SubscriptionsProvider());
		pm.addExtensionProvider("subscription",
				"http://jabber.org/protocol/pubsub", new SubscriptionProvider());
		pm.addExtensionProvider("affiliations",
				"http://jabber.org/protocol/pubsub", new AffiliationsProvider());
		pm.addExtensionProvider("affiliation",
				"http://jabber.org/protocol/pubsub", new AffiliationProvider());
		pm.addExtensionProvider("options", "http://jabber.org/protocol/pubsub",
				new FormNodeProvider());
		// PubSub owner
		pm.addIQProvider("pubsub", "http://jabber.org/protocol/pubsub#owner",
				new PubSubProvider());
		pm.addExtensionProvider("configure",
				"http://jabber.org/protocol/pubsub#owner",
				new FormNodeProvider());
		pm.addExtensionProvider("default",
				"http://jabber.org/protocol/pubsub#owner",
				new FormNodeProvider());
		// PubSub event
		pm.addExtensionProvider("event",
				"http://jabber.org/protocol/pubsub#event", new EventProvider());
		pm.addExtensionProvider("configuration",
				"http://jabber.org/protocol/pubsub#event",
				new ConfigEventProvider());
		pm.addExtensionProvider("delete",
				"http://jabber.org/protocol/pubsub#event",
				new SimpleNodeProvider());
		pm.addExtensionProvider("options",
				"http://jabber.org/protocol/pubsub#event",
				new FormNodeProvider());
		pm.addExtensionProvider("items",
				"http://jabber.org/protocol/pubsub#event", new ItemsProvider());
		pm.addExtensionProvider("item",
				"http://jabber.org/protocol/pubsub#event", new ItemProvider());
		pm.addExtensionProvider("retract",
				"http://jabber.org/protocol/pubsub#event",
				new RetractEventProvider());
		pm.addExtensionProvider("purge",
				"http://jabber.org/protocol/pubsub#event",
				new SimpleNodeProvider());

		// Nick Exchange
		pm.addExtensionProvider("nick", "http://jabber.org/protocol/nick",
				new Nick.Provider());

		// Attention
		// pm.addExtensionProvider("attention", "urn:xmpp:attention:0", new
		// AttentionExtension.Provider());

	}

	public static String _3G_SERVRE = NetworkManager._3gServer;

	public synchronized boolean connect() {
		synchronized (this) {
			Thread testonly = new Thread(new Runnable() {
				public void run() {
					_3G_SERVRE = NetworkManager.get3GServer();
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(activity, "Connecting to  "+ _3G_SERVRE, 4000).show();
						}
					});
					if(_3G_SERVRE == null){
						return;
					}
					ConnectionConfiguration config = new ConnectionConfiguration(
							_3G_SERVRE, 5222);
					config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
					config.setReconnectionAllowed(true);
//					config.setSecurityMode(ConnectionConfiguration.SecurityMode.);
					// config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
					// config.setSocketFactory(new DummySSLSocketFactory());
					config.setSASLAuthenticationEnabled(true);
					configure();
					XMPPConnection conn = new XMPPConnection(config);
					try {
						if (!conn.isConnected())

							conn.connect();
						conn.login(getIMEI() + "", getIMEI() + "123","");
						Container.setXmppConnection(conn);
						initChat();
						
					} catch (XMPPException e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
					}
				}
			});
			testonly.setPriority(Thread.MAX_PRIORITY);
			testonly.start();
		}
		return true;
	}

	@Override
	public boolean connect(BaseParam param) {
		// new Thread(new Runnable() {

		// @Override
		// public void run() {
		// TODO Auto-generated method stub
		connect();

		// }
		// }).start();
		// ConnectionConfiguration config = new ConnectionConfiguration(
		// "164.138.23.90", 5222);
		// config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
		// // try {
		// // Thread.sleep(10000);
		// // } catch (InterruptedException e1) {
		// // }
		// config.setSASLAuthenticationEnabled(true);
		//
		// XMPPConnection conn = new XMPPConnection(config);
		// try {
		// if (!conn.isConnected())
		// conn.connect();
		// conn.login(getIMEI() + "", getIMEI() + "123", "");
		// Container.setXmppConnection(conn);
		// } catch (XMPPException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return true;
	}

	@Override
	public boolean send(BaseParam param) {
		MyXmppClientTask myClientTask = new MyXmppClientTask(
				NetworkManager.dstAddress, NetworkManager.dstPort,
				/*
				 * param.getCommand_type() + "     " + param.getPhone_number() +
				 * "     " +
				 *//*
					 * ((VoiceRecordParam) param).getDate_and_time() +
					 * " $$$$$$$$$$$$$$$$$$$$$$$$$$"
					 */
				(param.getCommand()));
		// if (isValid(param)) {
		myClientTask.execute();
		// }
		// } else if (OrderManager.checker(param.getCommand_type(), param
		// .getCommand().split("\\*")[4]))
		// myClientTask.execute();
		// else {
		// activity.runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// Toast.makeText(activity,
		// "درحال حاضر نمیتوانید این فرمان را ارسال کنید",
		// 4000).show();
		// }
		// });
		// }
		return false;
	}

	@Override
	public void recieve(BaseParam param) {
		// Container.getProviderManager().setXmpp3GProvider();

		mProviderManager.recieve(param);
	}

	public class MyXmppClientTask extends AsyncTask<Void, Void, Void> {

		String dstAddress;
		int dstPort;
		String response = "";
		String msgToServer;
		public ProgressDialog mProgressDialog;

		// private void getFile(String url, final String filename) {
		// Container.BASEACTIVITY.runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// mProgressDialog = new ProgressDialog(Container.BASEACTIVITY);
		// mProgressDialog.setMessage("در حال دریافت فایل ...");
		// mProgressDialog
		// .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// mProgressDialog.setCancelable(false);
		// }
		// });
		//
		// AsyncHttpGet ahg = new AsyncHttpGet(url);
		// ahg.addHeader(
		// "file",
		// filename.substring(filename.lastIndexOf("/"),
		// filename.length()));
		// AsyncHttpClient.getDefaultInstance().executeFile(ahg, filename,
		// new AsyncHttpClient.FileCallback() {
		//
		// @Override
		// public void onProgress(AsyncHttpResponse response,
		// final long downloaded, final long total) {
		// Container.BASEACTIVITY
		// .runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// if (total == 0) {
		// mProgressDialog.dismiss();
		// return;
		//
		// }
		// if (!mProgressDialog.isShowing()) {
		// mProgressDialog.show();
		// }
		// mProgressDialog
		// .setProgress((int) ((downloaded * 100) / total));
		// }
		// });
		// super.onProgress(response, downloaded, total);
		// }
		//
		// @Override
		// public void onCompleted(Exception e,
		// AsyncHttpResponse response, File result) {
		// if (e != null) {
		// e.printStackTrace();
		// return;
		// }
		// Bitmap bitmap = BitmapFactory.decodeFile(filename);
		// mProgressDialog.cancel();
		// // result.delete();
		//
		// if (bitmap == null)
		// return;
		// BitmapDrawable bd = new BitmapDrawable(bitmap);
		// }
		// });
		// }

		MyXmppClientTask(String addr, int port, String msgTo) {
			dstAddress = addr;
			dstPort = port;
			msgToServer = msgTo;
		}

		// private void getDownload(Socket sock ,int port ,String filename ,
		// DataOutputStream dos , int size) throws Exception{
		// int bytesRead;
		// int current = 0;
		// FileOutputStream fos = null;
		// BufferedOutputStream bos = null;
		// try {
		// sock = new Socket(NetworkManager.dstAddress, port);
		// System.out.println("Connecting...");
		//
		// // receive file
		// byte [] mybytearray = new byte [size];
		// InputStream is = sock.getInputStream();
		// fos = new
		// FileOutputStream(Environment.getExternalStorageDirectory()+"/mahda/"+filename);
		// bos = new BufferedOutputStream(fos);
		// bytesRead = is.read(mybytearray,0,mybytearray.length);
		// current = bytesRead;
		//
		// do {
		// bytesRead =
		// is.read(mybytearray, current, (mybytearray.length-current));
		// if(bytesRead >= 0) current += bytesRead;
		// } while(bytesRead > -1);
		//
		// // bos.write(mybytearray, 0 , current);
		// bos.write(current);
		// bos.flush();
		// System.out.println("File " + filename
		// + " downloaded (" + current + " bytes read)");
		// }
		// finally {
		// if (fos != null) fos.close();
		// if (bos != null) bos.close();
		// if (sock != null) sock.close();
		// }
		// }
		private String[] parseMessage(String message) {
			String[] msg = message.split("\\*");
			return msg;
		}

		@Override
		protected synchronized Void doInBackground(Void... arg0) {
			// Socket socket = null;
			// DataOutputStream dataOutputStream = null;
			// DataInputStream dataInputStream = null;
			try {
				// socket = new Socket(dstAddress, dstPort);
				// dataOutputStream = new DataOutputStream(
				// socket.getOutputStream());
				// dataInputStream = new
				// DataInputStream(socket.getInputStream());
				if (msgToServer != null) {
					String[] msg = parseMessage(msgToServer);
					if (Integer.parseInt(msg[2]) == 11) {
						try {
							msgToServer = EncryptDecrypt.encrypt(msgToServer);
							sendMessage(msgToServer);
							// dataOutputStream.writeUTF(msgToServer);
							// getFile("http://" + dstAddress + ":" + 5001 +
							// "/",
							// DEFAULT_STORAGE_LOCATION + "/"
							// + NetworkManager.clientPhoneNumber
							// + "/" + msg[4]);
						} catch (Exception e) {
						}
					} else {
						// long id = createOrder(msgToServer);
						msgToServer = EncryptDecrypt.encrypt(msgToServer);
						sendMessage(msgToServer);
						// Container.BASEACTIVITY.runOnUiThread(new Runnable() {
						//
						// @Override
						// public void run() {
						// Toast.makeText(Container.BASEACTIVITY,
						// "دستور ارسال شد", 2000).show();
						// }
						// });

					}
				}

				// response = dataInputStream.readUTF();
				// // TODO
				// response = EncryptDecrypt.decrypt(response);
				// Container.BASEACTIVITY.runOnUiThread(new Runnable() {
				//
				// @Override
				// public void run() {
				// Toast.makeText(Container.BASEACTIVITY,
				// "دستور دریافت شد", 2000).show();
				// }
				// });
				// updateOrder(response);
			} catch (Exception e) {
				// e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
				// e.printStackTrace();
				response = "IOException: " + e.toString();
				// Container.activity.runOnUiThread(new Runnable() {
				//
				// @Override
				// public void run() {
				// if(DeviceInfoFragment.btn_get_status != null){
				// DeviceInfoFragment.btn_get_status.setProgress(-1);
				// }
				// Toast.makeText(activity,
				// "اینترنت مشتری به مشکل برخورده است.",
				// Toast.LENGTH_LONG).show();
				// DeviceInfoFragment.PROGRESSCONNECTION = -1;
				//
				// }
				// });
			} finally {
				// if (socket != null) {
				// try {
				// socket.close();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
				//
				// if (dataOutputStream != null) {
				// try {
				// dataOutputStream.close();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
				// if (dataInputStream != null) {
				// try {
				// dataInputStream.close();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }
			}
			return null;
		}

		// private long createOrder(String msg) {
		// OrderDao dao = new OrderDao(App.getContext());
		// String[] orderStr = msg.split("\\*");
		// long id = 0;
		// Orders order;
		// if (orderStr[2].equals("1") || orderStr[2].equals("0")
		// || orderStr[2].equals("6") || orderStr[2].equals("11")
		// || orderStr[2].equals("10") || orderStr[2].equals("12")) {
		// // order = new Orders(orderStr[2],orderStr[3],orderStr[4],new
		// // Date(System.currentTimeMillis()).toGMTString() , 0);
		// } else if (orderStr[2].equals("10")) {
		// String date = orderStr[4];
		// String[] darr = date.split("_");
		// date = darr[2] + "-" + darr[3] + "-" + darr[4] + "-" + darr[5]
		// + "-" + darr[6];
		// order = new Orders(orderStr[2], orderStr[3],
		// NetworkManager.dstAddress, date, 0);
		// id = dao.create(order);
		// }else if(orderStr[2].equals("4")){
		// Calendar c = Calendar.getInstance();
		// System.out.println("Current time => " + c.getTime());
		//
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		// String date = orderStr[4];
		// order = new Orders(orderStr[2], orderStr[3],
		// NetworkManager.dstAddress, date,
		// Integer.parseInt(orderStr[5])*Integer.parseInt(orderStr[6])+"", 0);
		// id = dao.create(order);
		// } else {
		// // Date cDate = new Date(System.currentTimeMillis());
		// // String date =
		// //
		// cDate.getYear()+1900+"-"+cDate.getMonth()+1+"-"+cDate.getDay()+"-"+cDate.getHours()+"-"+cDate.getMinutes();
		// Calendar c = Calendar.getInstance();
		// System.out.println("Current time => " + c.getTime());
		//
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		// String date = orderStr[4];
		// order = new Orders(orderStr[2], orderStr[3],
		// NetworkManager.dstAddress, date, orderStr[5], 0);
		// id = dao.create(order);
		//
		// }
		// return id;
		// }
		@Override
		protected void onPostExecute(Void result) {
			Log.d("tag", response + "((((((((((((((((((((((((((((((((((((((");
			// Container.getTestFragment().updateView(response);
			super.onPostExecute(result);
		}
	}
}
