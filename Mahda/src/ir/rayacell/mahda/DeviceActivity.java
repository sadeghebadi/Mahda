package ir.rayacell.mahda;

import ir.rayacell.mahda.adapter.DrawerItemCustomAdapter;
import ir.rayacell.mahda.fragment.DeviceInfoFragment;
import ir.rayacell.mahda.fragment.DeviceMainFragment;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.device;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceActivity extends FragmentActivity {
	DrawerItemCustomAdapter adapter;

	private ImageView toggle;
	device tmp;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	int pos = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_device);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.main_layout_main_device, new DeviceMainFragment())
				.commit();
		// test

		// Bundle extras = getIntent().getExtras();
		// if (extras != null) {
		//
		// pos=Integer.parseInt(extras.getString("pos"));
		// }

		setActionBar();

		Container.BASEACTIVITY = this;

		// Manager.getStatus();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	public void setActionBarForFlight(final boolean show){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ImageView flightMode=((ImageView)getActionBar().getCustomView().findViewById(
						R.id.flight));
				if(show){
					flightMode.setBackgroundDrawable(getResources().getDrawable(R.drawable.airplane_mode_icon));
				}else{
					flightMode.setBackgroundColor(android.R.color.transparent);
					
				}
				
			}
		});
	}
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
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();

		// init();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		// init();
	}
	// private void init(){
	//
	// DeviceInfoFragment.PROGRESS3G = 0;
	// DeviceInfoFragment.PROGRESSCONNECTION = 0;
	// DeviceInfoFragment.PROGRESSDIRECT = 0;
	// }
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

}
