package ir.rayacell.mahda;

import ir.rayacell.mahda.adapter.DrawerItemCustomAdapter;
import ir.rayacell.mahda.dao.DeviceDao;
import ir.rayacell.mahda.fragment.MapMainFragment;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.device;
import ir.rayacell.mahda.services.ServerInit;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity  implements DrawerNotifier{
	DrawerItemCustomAdapter adapter;

	
	private ImageView toggle;
	device tmp;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_map);
		Container.activity = this;
		Container.setManager(new Manager());
		// test
		new ServerInit().init();

		dao = new DeviceDao(this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.right_drawer);
		setActionBar();

		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.maplay, new MapMainFragment(this)/*
															 * new
															 * DeviceMainFragment
															 * ()
															 */).commit();

//		Container.getProviderManager().setInternetProvider();

		loadFromDB();

		ImageView iv = (ImageView) getActionBar().getCustomView().findViewById(
				R.id.toggle);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
					mDrawerLayout.closeDrawer(Gravity.RIGHT);
				} else {
					mDrawerLayout.openDrawer(Gravity.RIGHT);
				}
			}
		});
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				final Dialog ed = new Dialog(MainActivity.this, R.style.cDialog);
				ed.setContentView(R.layout.get_device_infor_dialog);
				TextView creditDesk = (TextView) ed
						.findViewById(R.id.exitecontent);
				tmp = dao.loadDeviceFromDB(position);
				adapter.notifyDataSetInvalidated();
				// creditDesk.setTypeface(tf);
				final EditText number = (EditText) ed.findViewById(R.id.ersal);
				final EditText name = (EditText) ed.findViewById(R.id.name);
				number.setText(tmp.number);
				name.setText(tmp.name);
				Button ok = (Button) ed.findViewById(R.id.ok_button);
				ok.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						ed.dismiss();
						// insertIntoDB(name.getText().toString(), number
						// .getText().toString(), latlng);
						tmp.number = number.getText().toString();
						tmp.name = name.getText().toString();
						dao.updateDevice(tmp);
						loadFromDB();
					}

				});
				Button delete = (Button) ed.findViewById(R.id.delete_button);
				delete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						ed.dismiss();
						// insertIntoDB(name.getText().toString(), number
						// .getText().toString(), latlng);
						tmp.number = number.getText().toString();
						tmp.name = name.getText().toString();
						dao.delete(tmp._id);
						loadFromDB();
					}

				});
				if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
					mDrawerLayout.closeDrawer(Gravity.RIGHT);
				} else {
					mDrawerLayout.openDrawer(Gravity.RIGHT);
				}
				
				ed.findViewById(R.id.exit).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								ed.dismiss();
							}
						});
				ed.show();
				return false;
			}

		});

		// Manager.getStatus();

	}

//	@Override
//	public void onBackPressed() {
//		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//			 getSupportFragmentManager().popBackStack();
//
//		} else {	
//			finish();
//		}
//	}
	ArrayList<device> devices;
	private void loadFromDB() {
		DeviceDao dao = new DeviceDao(App.getContext());
		if (dao.readAllAsc() == null) {
			return;
		}
		 devices = (ArrayList<device>) dao.readAllAsc();
		adapter = new DrawerItemCustomAdapter(this, R.layout.device_list_item,
				devices);
		mDrawerList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
//		for (device devi : devices) {
//			Marker marker = mMap.addMarker(new MarkerOptions()
//					.position(
//							new LatLng(Double.parseDouble(devi.lat), Double
//									.parseDouble(devi.lng))).title(devi.name)
//					.snippet(devi.number));
//		}
	}

	
	public void setActionBar() {
		ActionBar mActionBar = getActionBar();
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
//		mActionBar.setLogo(R.drawable.fedora);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setCustomView(R.layout.custom_actionbar);
	}

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	 // Inflate the menu; this adds items to the action bar if it is present.
	 getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // action with ID action_refresh was selected
	    case R.id.action_settings:
	      Intent i = new Intent(this, SettingsActivity.class);
	      startActivity(i);
	      break;
	    // action with ID action_settings was selected
	    default:
	      break;
	    }

	    return true;
	  } 

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}

	}

	DeviceDao dao;

	private void selectItem(int position) {
//		device mdevice = dao.loadDeviceFromDB(position);

//		Fragment newfragment = new DeviceMainFragment();
		// Bundle mbund = new Bundle();
		// mbund.putInt("position", position);
		// newfragment.setArguments(mbund);
		
		
//		Container.activity.getSupportFragmentManager().beginTransaction()
//				.replace(R.id.maplay, newfragment)
//				.addToBackStack("main").commit();
		
		
		Intent i = new Intent(this, DeviceActivity.class);
//		i.putExtra("number", devices.get(position).number);
		NetworkManager.clientPhoneNumber = devices.get(position).number;
		startActivity(i);
		finish();
		// LatLng point = new LatLng(
		// Double.parseDouble(loadDeviceFromDB(position).lat),
		// Double.parseDouble(loadDeviceFromDB(position).lng));
		// CameraUpdate center = CameraUpdateFactory.newLatLng(point);
		// CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
		// mMap.moveCamera(center);
		// mMap.animateCamera(zoom);
		// mDrawerList.setItemChecked(position, true);
		// mDrawerList.setSelection(position);
		// mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void notifyList(String name
			,String number, LatLng latlng , GoogleMap mMap) {
		 mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)).position(
					new LatLng(latlng.latitude, latlng.longitude)).title(number));
		new DeviceDao(this).insertIntoDB(name, number, latlng );
		loadFromDB();
	}
	
}
