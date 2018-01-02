package fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

public class PractiScoreFileParser {

	public static String readMatchScoreData(File file) {
		String fileContentString = null;
		try {
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().equals("match_scores.json")) {
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

}
