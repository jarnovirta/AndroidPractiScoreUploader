package fi.ipscresultservice.androidpractiscoreuploader;

/**
 * Created by Jarno on 26.1.2018.
 */

public class Constants {

	public static final long CHECK_FILE_MODIFIED_INTERVAL = 5 * 1000;

	public static final String EXTRAS_SERVER_ADDRESS = "SERVER_ADDRESS";

	public static final String EXTRAS_RESULT_RECEIVER_KEY = "EXTRAS_RESULT_RECEIVER_KEY";

	public static final int DATA_TRAMSMISSION_RESULT_KEY = 1;

	public static final String DATA_TRANSMISSION_RESULT_CODE_KEY = "DATA_TRANSMISSION_RESULT_CODE_KEY";

	public static final String DATA_TRANSMISSION_RESULT_MESSAGE_KEY
			= "DATA_TRANSMISSION_RESULT_MESSAGE_KEY";

	public static final String DATA_TRANSMISSION_TIME_KEY = "DATA_TRANSMISSION_TIME_KEY";

	public static final String APP_DATA_SERVER_ADDRESS = "APP_DATA_SERVER_ADDRESS";


	public interface ACTION {
		public static String MAIN_ACTION = "com.nkdroid.alertdialog.action.main";
		public static String STARTFOREGROUND_ACTION = "com.nkdroid.alertdialog.action.startforeground";
		public static String STOPFOREGROUND_ACTION = "com.nkdroid.alertdialog.action.stopforeground";
	}

	public interface NOTIFICATION_ID {
		public static int FOREGROUND_SERVICE = 101;
	}


}
