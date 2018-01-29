package fi.ipscresultservice.androidpractiscoreuploader.service;
import android.net.Uri;
import android.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

				// TODO: Only change lastModified when response with OK received from server!
				lastModified = fileModifiedTime;
				Match match = PractiScoreFileParser
						.readMatchDefDataFromExportFile(practiScoreExportFile);
				if (match != null) {
					Log.i(TAG, "Calling sendMatchResultData");
					HttpService.sendMatchDefinitionData(match);
				}
				MatchScore matchScore = PractiScoreFileParser
						.readMatchResultDataFromExportFile((practiScoreExportFile));
				if (matchScore != null) {
					Log.i(TAG, "Calling sendMatchResultData");
					HttpService.sendMatchResultData(matchScore);
				}
			} catch (Exception e) {
				Log.e(TAG, e.getStackTrace().toString());
			}
	}

//	public static File readPractiScoreExportFile() {
//		try {
//			if (practiScoreExportFilePath == null) return null;
//			final File tempFile = File.createTempFile("PSUploader", "psc");
//			tempFile.deleteOnExit();
//			FileOutputStream out = new FileOutputStream(tempFile);
//			IOUtils.copy(UploaderAppContext.getAppContext().getContentResolver().openInputStream(practiScoreExportFilePath), out);
//
//			return tempFile;
//		}
//		catch (Exception e) {
//			Log.e("FileService", e.getMessage());
//		}
//		return null;
// 	}
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

}
