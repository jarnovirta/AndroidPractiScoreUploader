package fi.ipscresultservice.androidpractiscoreuploader.service;

/**
 * Created by Jarno on 29.1.2018.
 */

public interface HttpResponseHandler {
	void process(int responseCode, String responseMessage);
}
