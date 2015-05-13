//package ir.rayacell.mahda.fragment;
//
//import com.androidmapsextensions.GoogleMap;
//import com.androidmapsextensions.MapView;
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.model.LatLng;
//
//import ir.rayacell.mahda.R;
//import ir.rayacell.mahda.manager.Container;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class MapFragment extends Fragment {
//	private View v;
//	private MapView mMapView;
//	private GoogleMap map;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		v = inflater.inflate(R.layout.fragment_map, container, false);
//		mMapView = (MapView) v.findViewById(R.id.mapView);
//		mMapView.onCreate(savedInstanceState);
//		mMapView.onResume();
//		Container.setMapView(mMapView);
//		map = mMapView.getExtendedMap();
//		Container.setGoogleMap(map);
//		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
//				new LatLng(35.699856, 51.392643), 15);
//		map.animateCamera(cameraUpdate);
//		return v;
//	}
//}
