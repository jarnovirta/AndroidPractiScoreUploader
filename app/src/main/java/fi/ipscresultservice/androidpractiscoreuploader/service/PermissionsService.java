package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import fi.ipscresultservice.androidpractiscoreuploader.activity.MainActivity;


/**
 * Created by Jarno on 31.7.2018.
 */

public class PermissionsService {

	public final static int PERMISSIONS_REQUEST_READ_AND_WRITE_SDK = 1;

	public static void getPermissions(MainActivity mainActivity) {
		if (Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(mainActivity,
						Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(mainActivity,
						Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(mainActivity,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
							Manifest.permission.READ_EXTERNAL_STORAGE},
					PERMISSIONS_REQUEST_READ_AND_WRITE_SDK);
		}
	}

}
