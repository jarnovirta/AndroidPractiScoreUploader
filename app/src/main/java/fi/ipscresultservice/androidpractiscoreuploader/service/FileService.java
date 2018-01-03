package fi.ipscresultservice.androidpractiscoreuploader.service;

import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import fi.ipscresultservice.androidpractiscoreuploader.domain.MatchScore;
import fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser.PractiScoreFileParser;


public class FileService {
	private static MatchScore matchScore = null;
	private static Long lastModified = null;

	public static MatchScore checkPractiScoreExportFileChange() {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			while(true) {
				Thread.sleep(1000);
				System.out.println("Checking file...");
				String exportFilePath = "/PractiScore/Match/My_First_Match_Export.psc";
				File exportFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
						+ exportFilePath);
				Long fileModifiedTime = exportFile.lastModified();
				if (lastModified != null && lastModified.equals(fileModifiedTime)) continue;
				System.out.println("File modified!");
				String jsonString = PractiScoreFileParser.readMatchScoreData(exportFile);
				if (jsonString != null) {
					System.out.println("Sending JSON");
					matchScore = objectMapper.readValue(jsonString, new TypeReference<MatchScore>() {
					});
					System.out.println("File modified, time: " + lastModified);
					lastModified = fileModifiedTime;
					HttpService.sendMatchScore(jsonString);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchScore;
	}

	public static File getPractiScoreExportFile() {
		File practiScoreExportFile = null;
		try {

			Log.d("Fileservice", "IS READABLE: " + isExternalStorageReadable());
			String exportFilePath = "/PractiScore/Match/My_First_Match_Export.psc";
			practiScoreExportFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
					+ exportFilePath);
			Log.d("FileService", "Opened match file");

		}
		catch (Exception e) {
			Log.d("Fileservice", "ERROR opening directory", e);
			e.printStackTrace();
		}
		return practiScoreExportFile;
	}

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
}
