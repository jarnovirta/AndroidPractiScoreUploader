package fi.ipscresultservice.androidpractiscoreuploader;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jarno on 27.1.2018.
 */

public class UploaderApp extends Application {
	private static Context context;

	public void onCreate() {
		super.onCreate();
		UploaderApp.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return UploaderApp.context;
	}
}
