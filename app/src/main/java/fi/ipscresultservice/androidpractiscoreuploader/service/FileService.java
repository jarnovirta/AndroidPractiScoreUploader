package fi.ipscresultservice.androidpractiscoreuploader.service;
import android.net.Uri;
import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

import fi.ipscresultservice.androidpractiscoreuploader.Constants;
import fi.ipscresultservice.androidpractiscoreuploader.UploaderAppContext;
import fi.ipscresultservice.androidpractiscoreuploader.domain.Competitor;
import fi.ipscresultservice.androidpractiscoreuploader.domain.Match;
import fi.ipscresultservice.androidpractiscoreuploader.domain.MatchScore;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileParser;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileType;


public class FileService {
	private static Long lastModified = null;
	private static String practiScoreExportFilePath;

	private static String matchName;

	private static final String TAG = FileService.class.getSimpleName();

	public static void checkPractiScoreExportFileModified(boolean forceSend) {
		try {
				File practiScoreExportFile = new File(practiScoreExportFilePath);
				Long fileModifiedTime = practiScoreExportFile.lastModified();
				if (!forceSend && lastModified != null && lastModified.equals(fileModifiedTime)) return;
				lastModified = fileModifiedTime;
				NotificationService.sendServiceNotifications("Sending data...", Constants.NOTIFICATION_TYPE.DISCREET);
				Match match = PractiScoreFileParser
						.readMatchDefDataFromExportFile(practiScoreExportFile);
				MatchScore matchScore = PractiScoreFileParser
					.readMatchResultDataFromExportFile((practiScoreExportFile));
				HttpService.sendMatchData(match, matchScore);
			} catch (Exception e) {
				Log.e(TAG, "Error sending result data", e);
				NotificationService.sendServiceNotifications("Error while sending data");
			}
	}

	public static void setPractiScoreExportFilePath(Uri uri) {
		practiScoreExportFilePath = FileUtil.getPath(UploaderAppContext.getAppContext(), uri);
		readMatchNameFromFile();
	}

	public static void setPractiScoreExportFilePath(String path) {
		practiScoreExportFilePath = path;
		readMatchNameFromFile();
	}

	private static void readMatchNameFromFile() {
		try {
			if (practiScoreExportFilePath == null) return;

			File file = new File(practiScoreExportFilePath);
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = PractiScoreFileParser.readPractiScoreExportFileData(file, PractiScoreFileType.MATCH_DEF);
			if (jsonString != null) {
				Match match = objectMapper.readValue(jsonString, new TypeReference<Match>() {
				});
				matchName = match.getName();
			}
		}
		catch (IOException e) {
			Log.e(TAG, "Error setting PractiScore export file path", e);
			NotificationService.sendServiceNotifications("Error setting file path",
					Constants.NOTIFICATION_TYPE.LOUD);
		}
	}
	public static String getPractiScoreExportFilePath() { return practiScoreExportFilePath; }

	public static String getPractiScoreExportFileMatchname() {
		return matchName;
	}

	public static boolean isPractiScoreExportFileUriSet() {
		return practiScoreExportFilePath != null;
	}

	public static void setLastModifiedToZero() {
		lastModified = 0L;
	}
}
