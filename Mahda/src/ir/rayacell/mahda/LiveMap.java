package ir.rayacell.mahda;

import java.util.ArrayList;

import ir.rayacell.mahda.dao.DeviceDao;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.DateTimeManager;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.BaseModel;
import ir.rayacell.mahda.model.Command;
import ir.rayacell.mahda.model.ShowOnlineMapModel;
import ir.rayacell.mahda.model.device;
import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpClient.StringCallback;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;

public class LiveMap extends FragmentActivity implements MapUpdater {
	GoogleMap mMap;
	private SupportMapFragment fragment;
	View v;

	public void setActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setLogo(R.drawable.fedora);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		((TextView) getActionBar().getCustomView().findViewById(
				R.id.tv_app_name_in_action_bar)).setText("شماره دستگاه : "
				+ NetworkManager.clientPhoneNumber);
		((ImageView) getActionBar().getCustomView().findViewById(R.id.toggle))
				.setVisibility(View.GONE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void request() {
		BaseModel command = new BaseModel(0, NetworkManager.clientPhoneNumber,
				App.getContext().getResources()
						.getString(R.string.command_get_location_live));
		// Command command = new Command(0,NetworkManager.clientPhoneNumber, App
		// .getContext().getResources()
		// .getString(R.string.command_get_location_live) ,
		// 0);
		Manager.showOnlineMap(command, LiveMap.this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		fragment = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map));
		// mMap.getUiSettings().setMyLocationButtonEnabled(false);
		mMap = fragment.getMap();
		// mMap.setMyLocationEnabled(true);
		setActionBar();
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// mMap.addTileOverlay(new TileOverlayOptions()
		// .tileProvider(new CustomMapTileProvider(getResources()
		// .getAssets())));
		// GotoGeo(35.7, 51.4);
		trackLive.clear();
		request();
		final AsyncHttpGet ahg = new AsyncHttpGet("http://"
				+ NetworkManager.dstAddress + ":" + 5001 + "/geo");

		// requestPosition(ahg);

		ImageButton btn_refresh = (ImageButton) findViewById(R.id.btn_refresh);
		btn_refresh.setVisibility(View.VISIBLE);
		btn_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				request();

				// requestPosition(ahg);
			}
		});
		// AsyncHttpClient.getDefaultInstance().executeString(
		// ahg, new StringCallback() {
		// @Override
		// public void onCompleted(Exception e,
		// AsyncHttpResponse source,
		// final String result) {
		// //update order
		// Container.BASEACTIVITY.runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// String [] str = result.split(" ");
		// double lat= Double.parseDouble(str[0]);
		// double lng = Double.parseDouble(str[1]);
		// GotoGeo(lat, lng);
		// DeviceDao dao = new DeviceDao(LiveMap.this);
		// // device d = dao.readWhere("number",
		// NetworkManager.clientPhoneNumber);
		// // d.lat = str[0];
		// // d.lng = str[1];
		// // dao.update(d._id,d);
		// mMap.addMarker(new
		// MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).position(
		// new LatLng(lat,lng)).snippet("lat :" +lat + "\n" + "lng :" +
		// lng).title(NetworkManager.clientPhoneNumber ));
		// }
		// });
		// }
		// });
		// toggle.setBackground(getResources().getDrawable(R.drawable.ic_drawer));
		// mapView = (MapView) v.findViewById(R.id.map);
		// mapView.onCreate(savedInstanceState);

		// Gets to GoogleMap from the MapView and does initialization stuff
		// mMap = mapView.getMap();

		// Needs to call MapsInitializer before doing any CameraUpdateFactory
		// calls
		// try {
		// MapsInitializer.initialize(this.getActivity());
		// } catch (GooglePlayServicesNotAvailableException e) {
		// e.printStackTrace();
		// }

		// TODO load from db and show markers
	}

	ArrayList<LatLng> trackLive = new ArrayList<LatLng>();

	public void showOnMap(String result) {
		String[] str = result.split(" ");
		double lat = Double.parseDouble(str[0]);
		double lng = Double.parseDouble(str[1]);
		trackLive.add(new LatLng(lat, lng));
		// GotoGeo(lat, lng);
		DeviceDao dao = new DeviceDao(LiveMap.this);
		// device d = dao.readWhere("number",
		// NetworkManager.clientPhoneNumber);
		// d.lat = str[0];
		// d.lng = str[1];
		// dao.update(d._id,d);

	}

	private Polyline line;

	public void drawLineOnMap() {
		if (trackLive.size() <= 1) {
			return;
		}
		LatLng n_1 = trackLive.get(trackLive.size() - 2);
		LatLng n = trackLive.get(trackLive.size() - 1);
		line = mMap.addPolyline(new PolylineOptions()
				.add(new LatLng(n_1.latitude, n_1.longitude),
						new LatLng(n.latitude, n.longitude)).width(5)
				.color(Color.BLUE).geodesic(true));

		// mMap.clear();
		// for (int z = 0; z < trackLive.size() - 1; z++) {
		// LatLng src = trackLive.get(z);
		// LatLng dest = trackLive.get(z + 1);
		//
		// line = mMap.addPolyline(new PolylineOptions()
		// .add(new LatLng(src.latitude, src.longitude),
		// new LatLng(dest.latitude, dest.longitude)).width(5)
		// .color(Color.BLUE).geodesic(true));
		// }

	}

	@Override
	protected void onDestroy() {
		trackLive = null;
		super.onDestroy();
	}

	public void addStartAndEnd() {

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
				.position(
						new LatLng(trackLive.get(0).latitude,
								trackLive.get(0).longitude))
				.snippet(
						"lat :" + trackLive.get(0).latitude + "\n" + "lng :"
								+ trackLive.get(0).longitude)
				.title(NetworkManager.clientPhoneNumber));
		if (trackLive.size() > 1) {
			mMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
					.position(
							new LatLng(
									trackLive.get(trackLive.size() - 1).latitude,
									trackLive.get(trackLive.size() - 1).longitude))
					.snippet(
							"lat :"
									+ trackLive.get(trackLive.size() - 1).latitude
									+ "\n"
									+ "lng :"
									+ trackLive.get(trackLive.size() - 1).longitude)
					.title(NetworkManager.clientPhoneNumber));
		}
	}

	// public void requestPosition(AsyncHttpGet ahg) {
	// AsyncHttpClient.getDefaultInstance().executeString(ahg,
	// new StringCallback() {
	// @Override
	// public void onCompleted(Exception e,
	// AsyncHttpResponse source, final String result) {
	// // update order
	// Container.BASEACTIVITY.runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// showOnMap(result);
	// }
	// });
	// }
	// });
	// }
	private void updateDeviceLocation(double lat, double lng, String phonenumber) {
		DeviceDao dao = new DeviceDao(this);
		device dev = dao.readWhere("number", phonenumber);
		dev.lat = lat + "";
		dev.lng = lng + "";
		dao.update("number", phonenumber, dev);
	}

	public void GotoGeo(double lat, double lng, String phonenumber) {
		trackLive.add(new LatLng(lat, lng));
		updateDeviceLocation(lat, lng, phonenumber);
		changeCamera(
				CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
						.target(new LatLng(lat, lng)).zoom(14f).bearing(0)
						.build()), new CancelableCallback() {
					@Override
					public void onFinish() {
					}

					@Override
					public void onCancel() {
					}
				});
	}

	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
		mMap.animateCamera(update, Math.max(1000, 1), callback);
	}

	@Override
	public void updateMap(ShowOnlineMapModel mapModel) {

		GotoGeo(mapModel.getLocation().latitude,
				mapModel.getLocation().longitude, mapModel.getPhone_number());
		drawLineOnMap();
		addStartAndEnd();
	}

}
