package fi.ipscresultservice.androidpractiscoreuploader.service;

/**
 * Created by Jarno on 16.3.2018.
 */

public class UserService {
	private static String username;
	private static String password;

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		UserService.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		UserService.password = password;
	}
}
