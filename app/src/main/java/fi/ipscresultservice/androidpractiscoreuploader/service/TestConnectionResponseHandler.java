package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.util.Log;
import android.widget.Toast;

import fi.ipscresultservice.androidpractiscoreuploader.UploaderAppContext;

/**
 *
 * Created by Jarno on 29.1.2018.
 */

public class TestConnectionResponseHandler implements HttpResponseHandler {
	public void process(int responseCode, String responseMessage) {

		if (responseCode == 200) {
			Toast.makeText(UploaderAppContext.getAppContext(), "Connection ok!", Toast.LENGTH_SHORT).show();
		}
		else {
			String errorString = "Connection failed";
			if (responseCode != 0) errorString += " (code: " + responseCode + ")";
			if (responseMessage != null) errorString += " " + responseMessage;
			errorString += "!";
			Toast.makeText(UploaderAppContext.getAppContext(), errorString, Toast.LENGTH_SHORT).show();
		}
	}
}
