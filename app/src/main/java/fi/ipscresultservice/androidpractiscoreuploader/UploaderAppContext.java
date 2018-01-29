package fi.ipscresultservice.androidpractiscoreuploader;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jarno on 27.1.2018.
 */

public class UploaderAppContext extends Application {
	private static Context context;

	public void onCreate() {
		super.onCreate();
		UploaderAppContext.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return UploaderAppContext.context;
	}
}
