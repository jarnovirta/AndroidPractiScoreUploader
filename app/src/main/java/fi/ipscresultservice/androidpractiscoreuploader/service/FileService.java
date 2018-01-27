package fi.ipscresultservice.androidpractiscoreuploader.service;
import android.net.Uri;
import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import fi.ipscresultservice.androidpractiscoreuploader.UploaderApp;
import fi.ipscresultservice.androidpractiscoreuploader.domain.Match;
import fi.ipscresultservice.androidpractiscoreuploader.domain.MatchScore;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileParser;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileType;


public class FileService {
	private static MatchScore matchScore = null;
	private static Long lastModified = null;
	private static Uri practiScoreExportFileUri;

	private static String matchName;

	public static MatchScore checkPractiScoreExportFileChange() {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			while(true) {
				Thread.sleep(1000);
				Log.d("FileService", "Checking file...");
				File practiScoreExportFile = readPractiScoreExportFile();
				Long fileModifiedTime = practiScoreExportFile.lastModified();
				if (lastModified != null && lastModified.equals(fileModifiedTime)) continue;
				Log.d("FileService", "File modified!...");
				String jsonString = PractiScoreFileParser.readMatchScoreData(practiScoreExportFile, PractiScoreFileType.MATCH_SCORES);
				if (jsonString != null) {
					Log.d("FileService", "Sending JSON...");
					matchScore = objectMapper.readValue(jsonString, new TypeReference<MatchScore>() {
						});
					lastModified = fileModifiedTime;
//					HttpService.sendMatchScore(jsonString);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchScore;
	}

	public static File readPractiScoreExportFile() {
		try {
			if (practiScoreExportFileUri == null) return null;
			final File tempFile = File.createTempFile("PSUploader", "psc");
			tempFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(UploaderApp.getAppContext().getContentResolver().openInputStream(practiScoreExportFileUri), out);

			return tempFile;
		}
		catch (Exception e) {
			Log.e("FileService", e.getMessage());
		}
		return null;
 	}
	public static void setPractiScoreExportFileUri(Uri uri) {
		practiScoreExportFileUri = uri;
		File file = readPractiScoreExportFile();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = PractiScoreFileParser.readMatchScoreData(file, PractiScoreFileType.MATCH_DEF);
			if (jsonString != null) {
				Match match = objectMapper.readValue(jsonString, new TypeReference<Match>() {
				});
				Log.d("FileService", "*** match name " + match.getName());
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

	public static boolean isPractiScoreExportFileUriSet() {
		return practiScoreExportFileUri != null;
	}
}
