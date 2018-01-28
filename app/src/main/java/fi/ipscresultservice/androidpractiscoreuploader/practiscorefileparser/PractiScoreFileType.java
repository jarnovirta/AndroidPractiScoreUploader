package fi.ipscresultservice.androidpractiscoreuploader.practiscorefileparser;

/**
 * Created by Jarno on 26.1.2018.
 *
 */

public enum PractiScoreFileType {
	MATCH_DEF("match_def.json"), MATCH_SCORES("match_scores.json");

	public String fileName;

	PractiScoreFileType(String fileName) {
		this.fileName = fileName;
	}

}
