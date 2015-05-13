package ir.rayacell.mahda;

import ir.rayacell.mahda.manager.NetworkManager;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.setting);
    setActionBar();
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
		
		((TextView)getActionBar().getCustomView().findViewById(R.id.tv_app_name_in_action_bar)).setVisibility(View.GONE);
		((ImageView)getActionBar().getCustomView().findViewById(R.id.toggle)).setVisibility(View.GONE);

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		super.onBackPressed();
		
//		init();
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
} 
