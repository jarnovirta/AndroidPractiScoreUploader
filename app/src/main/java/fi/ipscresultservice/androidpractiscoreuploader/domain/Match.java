package fi.ipscresultservice.androidpractiscoreuploader.domain;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {

	private Long id;

	@JsonProperty("match_id")
	private String uuid;

	@JsonProperty("match_name")
	private String name;

	@JsonProperty("match_level")
	private String level;

	@JsonProperty("match_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Calendar date;

	@JsonProperty("match_modifieddate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Calendar modifiedDate;

	@JsonProperty("match_cats")
	private List<String> divisions;

	@JsonProperty("match_stages")
	private List<Stage> stages;

	@JsonProperty("match_shooters")
	private List<Competitor> competitors;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<String> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<String> divisions) {
		this.divisions = divisions;
	}

	public List<Stage> getStages() {
		return stages;
	}

	public void setStages(List<Stage> stages) {
		this.stages = stages;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<Competitor> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(List<Competitor> competitors) {
		this.competitors = competitors;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
