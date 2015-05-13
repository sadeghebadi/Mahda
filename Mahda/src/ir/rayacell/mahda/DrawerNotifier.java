package ir.rayacell.mahda;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public interface DrawerNotifier {

	void notifyList(String name, String number, LatLng latlng , GoogleMap mMap);

}
