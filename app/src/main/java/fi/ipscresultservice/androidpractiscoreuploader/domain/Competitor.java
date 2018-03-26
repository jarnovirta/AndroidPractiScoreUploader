package fi.ipscresultservice.androidpractiscoreuploader.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;

/**
 * Created by Jarno on 27.1.2018.
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressWarnings("unused")
public class Competitor {

	@JsonProperty("sh_uid")
	private String uuid;

	@JsonProperty("sh_num")
	private int shooterNumber;

	@JsonProperty("sh_fn")
	private String firstName;

	@JsonProperty("sh_ln")
	private String lastName;

	@JsonProperty("sh_id")
	private int ipscAlias;

	@JsonProperty("sh_dvp")
	private String division;

	@JsonProperty("sh_ctgs")
	private String practiScoreCategoryString;

	@JsonProperty("sh_cc")
	private String country;

	@JsonProperty("sh_dq")
	private boolean disqualified;

	@JsonProperty("sh_grd")
	private char classification;

	@JsonProperty("sh_team")
	private String team;

	@JsonProperty("sh_mod")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Calendar modifiedDate;

	@JsonProperty("sh_pf")
	private String powerFactor;

	@JsonProperty("sh_sqd")
	private int squad;

	@JsonProperty("sh_del")
	private boolean deleted;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getShooterNumber() {
		return shooterNumber;
	}

	public void setShooterNumber(int shooterNumber) {
		this.shooterNumber = shooterNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isDisqualified() {
		return disqualified;
	}

	public void setDisqualified(boolean disqualified) {
		this.disqualified = disqualified;
	}

	public char getClassification() {
		return classification;
	}

	public void setClassification(char classification) {
		this.classification = classification;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getPowerFactor() {
		return powerFactor;
	}

	public void setPowerFactor(String powerFactor) {
		this.powerFactor = powerFactor;
	}

	public int getSquad() {
		return squad;
	}

	public void setSquad(int squad) {
		this.squad = squad;
	}

	public int getIpscAlias() {
		return ipscAlias;
	}

	public void setIpscAlias(int ipscAlias) {
		this.ipscAlias = ipscAlias;
	}

	public String getPractiScoreCategoryString() {
		return practiScoreCategoryString;
	}

	public void setPractiScoreCategoryString(String practiScoreCategoryString) {
		this.practiScoreCategoryString = practiScoreCategoryString;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
