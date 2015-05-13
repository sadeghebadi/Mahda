package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.CustomMapTileProvider;
import ir.rayacell.mahda.DrawerNotifier;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.dao.DeviceDao;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.model.device;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
//import com.androidmapsextensions.GoogleMap;
//import com.androidmapsextensions.MapView;

public class MapMainFragment extends Fragment {
	private View v;
	private GoogleMap mMap;
	private SupportMapFragment fragment;
	public DrawerNotifier notify;

	public MapMainFragment(DrawerNotifier notifier) {
		this.notify = notifier;
	}

	// MapView mapView;

	public MapMainFragment(){
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.map_layout, container, false);

		// toggle.setBackground(getResources().getDrawable(R.drawable.ic_drawer));
		// mapView = (MapView) v.findViewById(R.id.map);
		// mapView.onCreate(savedInstanceState);

		ImageButton btn_refresh = (ImageButton) v.findViewById(R.id.btn_refresh);
		btn_refresh.setVisibility(View.GONE);
		// Gets to GoogleMap from the MapView and does initialization stuff
		// mMap = mapView.getMap();
		fragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
		// mMap.getUiSettings().setMyLocationButtonEnabled(false);
//		if(fragment == null){
//			return v;
//		}
		mMap = fragment.getMap();
		// mMap.setMyLocationEnabled(true);

		mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
		mMap.addTileOverlay(new TileOverlayOptions()
				.tileProvider(new CustomMapTileProvider(getActivity()
						.getResources().getAssets())));
		// Needs to call MapsInitializer before doing any CameraUpdateFactory
		// calls
		// try {
		// MapsInitializer.initialize(this.getActivity());
		// } catch (GooglePlayServicesNotAvailableException e) {
		// e.printStackTrace();
		// }
		onGoToTehran();
		// TODO load from db and show markers

		loadAllDevice();
		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			
			@Override
			public void onCameraChange(CameraPosition position) {
				// TODO Auto-generated method stub
				  float maxZoom = 17.0f;
				    if (position.zoom > maxZoom)
				        mMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));	
			}
		});
		// mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(final LatLng latlng) {
				final Dialog ed = new Dialog(Container.activity,
						R.style.cDialog);
				ed.setContentView(R.layout.add_device_infor_dialog);
				TextView creditDesk = (TextView) ed
						.findViewById(R.id.exitecontent);
				// creditDesk.setTypeface(tf);
				final EditText number = (EditText) ed.findViewById(R.id.ersal);
				final EditText name = (EditText) ed.findViewById(R.id.name);
				Button ok = (Button) ed.findViewById(R.id.ok_button);
			
				ok.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if(name.getText().toString().matches("") || number.getText().toString().matches("")){
							Toast.makeText(Container.activity, "فیلد ها نباید خالی باشد", 3000).show();
							return ;
						}
						ed.dismiss();
						notify.notifyList(name.getText().toString(), number
								.getText().toString(), latlng, mMap);

						
					}
				});
				ed.findViewById(R.id.exit).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								ed.dismiss();
							}
						});
				ed.show();
			}
		});
		// Updates the location and zoom of the MapView

		return v;

	}

	private void loadAllDevice() {
		if (new DeviceDao(getActivity()).readAllAsc().size() == 0)
			return;
		ArrayList<device> devices = (ArrayList<device>) new DeviceDao(
				getActivity()).readAllAsc();
		for (device d : devices) {
		 mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).position(
					new LatLng(Double.parseDouble(d.lat), Double.parseDouble(d.lng))).title(d.number));
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	private void showOnMap() {

	}

	private void deleteFromDB(String number) {

	}

	// mMapView = (MapView) v.findViewById(R.id.mapView);
	// mMapView.onCreate(savedInstanceState);
	// mMapView.onResume();
	// Container.setMapView(mMapView);
	// map = mMapView.getExtendedMap();
	// Container.setGoogleMap(map);
	// CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
	// new LatLng(35.699856, 51.392643), 15);
	// map.animateCamera(cameraUpdate);
	// return v;
	static final CameraPosition Tehran = new CameraPosition.Builder()
			.target(new LatLng(35.7, 51.4)).zoom(12.5f).bearing(0).build();

	public void onGoToTehran() {
		changeCamera(CameraUpdateFactory.newCameraPosition(Tehran),
				new CancelableCallback() {
					@Override
					public void onFinish() {
					}

					@Override
					public void onCancel() {
					}
				});
	}

	/**
	 * Change the camera position by moving or animating the camera depending on
	 * the state of the animate toggle button.
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
		// The duration must be strictly positive so we make it at least 1.
		mMap.animateCamera(update, Math.max(2000, 1), callback);
	}
}
