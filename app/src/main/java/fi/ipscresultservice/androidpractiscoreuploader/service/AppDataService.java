package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.activity.MainActivity;

/**
 * Created by Jarno on 31.7.2018.
 */

public class AppDataService {
	public static void saveAppData(MainActivity mainActivity) {
		SharedPreferences sharedPref = mainActivity.getSharedPreferences("PSUploaderData", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(Constants.APP_DATA_SERVER_ADDRESS, HttpService.getServerUrl());
		editor.putString(Constants.EXTRAS_USERNAME, UserService.getUsername());
		editor.putString(Constants.EXTRAS_PASSWORD, UserService.getPassword());
		editor.putString(Constants.PRACTISCORE_FILE_PATH, FileService.getPractiScoreExportFilePath());
		editor.commit();
	}

	public static void loadAppData(MainActivity mainActivity) {
		SharedPreferences sharedPref = mainActivity.getSharedPreferences("PSUploaderData", Context.MODE_PRIVATE);
		HttpService.setServerUrl(sharedPref.getString(Constants.APP_DATA_SERVER_ADDRESS, null));
		UserService.setUsername(sharedPref.getString(Constants.EXTRAS_USERNAME, null));
		UserService.setPassword(sharedPref.getString(Constants.EXTRAS_PASSWORD, null));
		FileService.setPractiScoreExportFilePath(sharedPref.getString(Constants.PRACTISCORE_FILE_PATH, null));
	}
}
