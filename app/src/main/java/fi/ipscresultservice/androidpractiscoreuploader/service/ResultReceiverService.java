package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.os.ResultReceiver;

/**
 *
 * Created by Jarno on 29.1.2018.
 */

public class ResultReceiverService {

	private static ResultReceiver fileTrackerResultReceiver;

	// Used in case the user stops tracker and FileTrackerService is stopped
	public static ResultReceiver getFileTrackerResultReceiver() {
		return fileTrackerResultReceiver;
	}
	public static void setFileTrackerResultReceiver(ResultReceiver resultReceiver) {
		fileTrackerResultReceiver = resultReceiver;
	}
}
