package fi.ipscresultservice.androidpractiscoreuploader.service;
import android.net.Uri;
import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

				String matchDefJson = readPractiScoreExportFileData(practiScoreExportFile, PractiScoreFileType.MATCH_DEF);
				String scoresJson = readPractiScoreExportFileData(practiScoreExportFile, PractiScoreFileType.MATCH_SCORES);
				String json = "{\"match\": " + matchDefJson;
				if (scoresJson != null && scoresJson.length() > 0) {
					json += ", \"matchScore\": " + scoresJson;
				}
				json += "}";

				HttpService.sendMatchData(json);
			} catch (Exception e) {
				Log.e(TAG, "Error sending result data", e);
				NotificationService.sendServiceNotifications("Error while sending data");
			}
	}

	private static void readMatchNameFromFile() {
		try {
			if (practiScoreExportFilePath == null) return;

			File file = new File(practiScoreExportFilePath);
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = readPractiScoreExportFileData(file, PractiScoreFileType.MATCH_DEF);
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

	public static String readPractiScoreExportFileData(File file, PractiScoreFileType fileType) {
		String fileContentString;

		try {
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().equals(fileType.fileName)) {
					InputStream inputStream = zipFile.getInputStream(entry);
					fileContentString = IOUtils.toString(inputStream, "utf-8");
					inputStream.close();
					return fileContentString;
				}
			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void setPractiScoreExportFilePath(Uri uri) {
		practiScoreExportFilePath = FileUtil.getPath(UploaderAppContext.getAppContext(), uri);
		readMatchNameFromFile();
	}

	public static void setPractiScoreExportFilePath(String path) {
		practiScoreExportFilePath = path;
		readMatchNameFromFile();
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
