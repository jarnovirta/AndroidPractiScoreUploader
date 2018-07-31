package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.activity.MainActivity;

/**
 * Created by Jarno on 31.7.2018.
 */

public class FileTrackerResultReceiver extends ResultReceiver {

	MainActivity mainActivity;

	public FileTrackerResultReceiver(Handler handler, MainActivity mainActivity) {
		super(handler);
		this.mainActivity = mainActivity;
	}

	@Override
	protected void onReceiveResult(int resultCode, final Bundle resultData) {
		super.onReceiveResult(resultCode, resultData);
		if (resultCode == Constants.DATA_TRAMSMISSION_RESULT_KEY) {
			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String resultMessage = resultData.getString(Constants.DATA_TRANSMISSION_RESULT_MESSAGE_KEY);
					String dataSentTime = resultData.getString(Constants.RESULT_MESSAGE_TIMESTAMP_KEY);
					mainActivity.setInfoViewGroupVisibility(View.VISIBLE);

					if (dataSentTime != null && dataSentTime.length() > 0)
						mainActivity.setDataSentTimeTextViewText(dataSentTime);
					if (resultMessage != null && resultMessage.length() > 0)
						mainActivity.setStatusTextView(resultMessage);
				}
			});
		}
	}

}
