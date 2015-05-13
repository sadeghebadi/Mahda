package ir.rayacell.mahda;

import ir.rayacell.mahda.view.TypefaceUtil;
import android.app.Application;
import android.content.Context;

public class App extends Application {

	/**
	 * Keeps a reference of the application context
	 */
	private static Context mAppContext;

	@Override
	public void onCreate() {
		super.onCreate();
//		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF",
//				"fonts/DroidKufi-Bold.ttf"); // font from assets:
												// "assets/fonts/Roboto-Regular.ttf
		mAppContext = getApplicationContext();
//		if (!Container.getCreditManager().isUserExist()) {
//			Container.getCreditManager().createUser();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF",
                "fonts/BYekan.ttf"); // font from assets:
                                        // "assets/fonts/
                                        // mAppContext =
                                        // getApplicationContext();
		
	}

	/**
	 * Returns the application context
	 * 
	 * @return application context
	 */
	public static Context getContext() {
		return mAppContext;
	}
}
