package fi.ipscresultservice.androidpractiscoreuploader;

import android.widget.Button;
import android.widget.ToggleButton;

/**
 * Created by Jarno on 26.1.2018.
 */

public class Constants {

	public static final String NOTIFICATION_CHANNEL_ID = "PRACTI_SCORE_UPLOADER_NOTIFICATION_CHANNEL";

	public enum NOTIFICATION_TYPE { LOUD, DISCREET; }

	public static final long CHECK_FILE_MODIFIED_INTERVAL = 5 * 1000;

	public static final String EXTRAS_SERVER_ADDRESS = "SERVER_ADDRESS";

	public static final String EXTRAS_USERNAME = "USERNAME";

	public static final String EXTRAS_PASSWORD = "PASSWORD";

	public static final String PRACTISCORE_FILE_PATH = "PRACTISCORE_FILE_PATH";

	public static final String EXTRAS_RESULT_RECEIVER_KEY = "EXTRAS_RESULT_RECEIVER_KEY";

	public static final int DATA_TRAMSMISSION_RESULT_KEY = 1;

	public static final String DATA_TRANSMISSION_RESULT_CODE_KEY = "DATA_TRANSMISSION_RESULT_CODE_KEY";

	public static final String DATA_TRANSMISSION_RESULT_MESSAGE_KEY
			= "DATA_TRANSMISSION_RESULT_MESSAGE_KEY";

	public static final String RESULT_MESSAGE_TIMESTAMP_KEY = "DATA_TRANSMISSION_TIME_KEY";

	public static final String APP_DATA_SERVER_ADDRESS = "APP_DATA_SERVER_ADDRESS";

	public static final String MAIN_ACTIVITY_BUTTONS_STATE = "MAIN_ACTIVITY_BUTTONS_STATE";

	public static final String INFO_VIEW_GROUP_VISIBILITY = "INFO_VIEW_GROUP_VISIBILITY";

	public static final String DATA_SENT_TIME_TEXT_VIEW_STATE = "DATA_SENT_TIME_TEXT_VIEW_STATE";

	public static final String STATUS_TEXT_VIEW_STATE = "STATUS_TEXT_VIEW_STATE";

	public interface ACTION {
		public static String MAIN_ACTION = "com.nkdroid.alertdialog.action.main";
		public static String STARTFOREGROUND_ACTION = "com.nkdroid.alertdialog.action.startforeground";
		public static String STOPFOREGROUND_ACTION = "com.nkdroid.alertdialog.action.stopforeground";
	}

	public interface NOTIFICATION_ID {
		public static int FOREGROUND_SERVICE = 101;
	}
}
