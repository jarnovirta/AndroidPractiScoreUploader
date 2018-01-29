package fi.ipscresultservice.androidpractiscoreuploader.service;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.UploaderAppContext;
import fi.ipscresultservice.androidpractiscoreuploader.domain.Match;
import fi.ipscresultservice.androidpractiscoreuploader.domain.MatchScore;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileParser;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileType;


public class FileService {
	private static Long lastModified = null;
	private static String practiScoreExportFilePath;

	private static String matchName;

	private static final String TAG = FileService.class.getSimpleName();

	public static void checkPractiScoreExportFileModified() {
		try {

				File practiScoreExportFile = new File(practiScoreExportFilePath);
				Long fileModifiedTime = practiScoreExportFile.lastModified();
				Log.i(TAG, "\n**** PractiScore export file last modified: " + fileModifiedTime.toString());
				if (lastModified != null && lastModified.equals(fileModifiedTime)) return;
				Log.i(TAG, "File modified! Starting data upload...");


				ResultReceiver resultReceiver = ResultReceiverService.getFileTrackerResultReceiver();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.DATA_TRANSMISSION_RESULT_MESSAGE_KEY, "Sending data...");

				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				String timeString = dateFormat.format(cal.getTime());
				bundle.putString(Constants.DATA_TRANSMISSION_TIME_KEY, timeString);
				resultReceiver.send(1, bundle);

				// TODO: Only change lastModified when response with OK received from server!
				lastModified = fileModifiedTime;
				Match match = PractiScoreFileParser
						.readMatchDefDataFromExportFile(practiScoreExportFile);
				Log.i(TAG, "Calling sendMatchResultData");
				int resultCode = HttpService.sendMatchDefinitionData(match);
				if (resultCode != 200) sendResultInfo("Error code: " + resultCode);
				MatchScore matchScore = PractiScoreFileParser
						.readMatchResultDataFromExportFile((practiScoreExportFile));

				Log.i(TAG, "Calling sendMatchResultData");
				resultCode = HttpService.sendMatchResultData(matchScore);
				if (resultCode != 200) sendResultInfo("Error code: " + resultCode);
				else sendResultInfo("Data successfully sent");
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				sendResultInfo("Error");
			}
	}
	private static void sendResultInfo(String info) {

		ResultReceiver resultReceiver = ResultReceiverService.getFileTrackerResultReceiver();
		Bundle bundle = new Bundle();
		bundle.putString(Constants.DATA_TRANSMISSION_RESULT_MESSAGE_KEY, info);
		resultReceiver.send(1, bundle);
	}



	public static void setPractiScoreExportFilePath(Uri uri) {
		practiScoreExportFilePath = FileUtil.getPath(UploaderAppContext.getAppContext(), uri);

		File file = new File(practiScoreExportFilePath);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = PractiScoreFileParser.readPractiScoreExportFileData(file, PractiScoreFileType.MATCH_DEF);
			if (jsonString != null) {
				Match match = objectMapper.readValue(jsonString, new TypeReference<Match>() {
				});
				matchName = match.getName();
			}

		}
		catch (IOException e) {
			Log.e("FileService", e.getMessage());
		}
	}
	public static String getPractiScoreExportFileMatchname() {
		return matchName;
	}

	public static String getPraciScoreExportFilePath() { return practiScoreExportFilePath; }

	public static boolean isPractiScoreExportFileUriSet() {
		return practiScoreExportFilePath != null;
	}

	public static void setLastModifiedToZero() {
		lastModified = 0L;
	}
}
