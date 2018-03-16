package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.util.Base64;

/**
 * Created by Jarno on 16.3.2018.
 */

public class HttpUtil {
	public static String getBasicAuthHeader() {
		String authHeader = null;
		if (UserService.getUsername() != null && UserService.getPassword() != null) {
			authHeader = "Basic " + Base64.encodeToString((UserService.getUsername()
					+ ":" + UserService.getPassword()).getBytes(), Base64.DEFAULT);
		}
		return authHeader;
	}
}
