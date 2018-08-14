package fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import fi.ipscresultservice.androidpractiscoreuploader.domain.Match;
import fi.ipscresultservice.androidpractiscoreuploader.domain.MatchScore;
import fi.ipscresultservice.androidpractiscoreuploader.domain.StageScore;

public class PractiScoreFileParser {
/*
	private static final String TAG = PractiScoreFileParser.class.getSimpleName();
	public static Match readMatchDefDataFromExportFile(File file) {
		try {
			String jason = readPractiScoreExportFileData(file, PractiScoreFileType.MATCH_DEF);
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(jason, new TypeReference<Match>() {
			});
		}
		catch (Exception e) {
			Log.d(TAG, e.getStackTrace().toString());
			return null;
		}
	}
	public static MatchScore readMatchResultDataFromExportFile(File file) {
		try {
			String jason = readPractiScoreExportFileData(file, PractiScoreFileType.MATCH_SCORES);
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(jason, new TypeReference<MatchScore>() {
			});
		}
		catch (Exception e) {
			Log.d(TAG, e.getStackTrace().toString());
			return null;
		}
	}
*/
}
