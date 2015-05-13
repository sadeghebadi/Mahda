package ir.rayacell.mahda;

import ir.rayacell.mahda.manager.NetworkManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

public class TrackMap extends FragmentActivity {
	GoogleMap mMap;
	private SupportMapFragment fragment;
	View v;
	private Polyline line;

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

	String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);

		filePath = getIntent().getStringExtra("filePath");
		fragment = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map));
		// mMap.getUiSettings().setMyLocationButtonEnabled(false);
		mMap = fragment.getMap();
		// mMap.setMyLocationEnabled(true);
		setActionBar();
		
		// mMap.addTileOverlay(new TileOverlayOptions()
		// .tileProvider(new
		// CustomMapTileProvider(getResources().getAssets())));
		// GotoGeo(35.7, 51.4);
		List<LatLng> list = decodePoly();
		if (list.size() != 0)
			GotoGeo(list.get(0).latitude, list.get(0).longitude);

		for (int z = 0; z < list.size()-1; z++) {
			LatLng src = list.get(z);
			LatLng dest = list.get(z + 1);
			line = mMap.addPolyline(new PolylineOptions()
					.add(new LatLng(src.latitude, src.longitude),
							new LatLng(dest.latitude, dest.longitude)).width(5)
					.color(Color.BLUE).geodesic(true));
		}
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//		mMap.addTileOverlay(new TileOverlayOptions()
//		.tileProvider(new CustomMapTileProvider(getResources()
//				.getAssets())));
	}
	private List<LatLng> decodePoly() {
		List<LatLng> geos = new ArrayList<LatLng>();
		BufferedReader reader;
		String tmp = "";
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath)));
			while ((tmp = reader.readLine()) != null) {

				double lat = Double.parseDouble(tmp.split("#")[1]);
				double lng = Double.parseDouble(tmp.split("#")[0]);
				LatLng ll = new LatLng(lat, lng);
				geos.add(ll);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return geos;
	}

	public void GotoGeo(double lat, double lng) {
		changeCamera(
				CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
						.target(new LatLng(lat, lng)).zoom(15f).bearing(0)
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

}
