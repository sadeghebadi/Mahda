/**
 * 
 */
package ir.rayacell.mahdaclient.services;

import ir.rayacell.mahdaclient.MainActivity;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author rayacell
 *Mar 5, 2015
 */
public class TaskChecker extends Service {

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
//		if(!isForeground("ir.rayacell.mahdaclient")){
			Intent in = new Intent(getApplicationContext(),MainActivity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(in);
//		}
		super.onStart(intent, startId);
	}
	public boolean isForeground(String myPackage){
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		 List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(1); 

		     ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
		   if(componentInfo.getPackageName().equals(myPackage)) return true;
		return false;
		}

}
