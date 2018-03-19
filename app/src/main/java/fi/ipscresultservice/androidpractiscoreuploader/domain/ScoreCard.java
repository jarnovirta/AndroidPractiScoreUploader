package fi.ipscresultservice.androidpractiscoreuploader.domain;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class ScoreCard {
	@JsonProperty("shtr")
	private String shooterId;

	@JsonProperty("mod")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Calendar modified;

	@JsonProperty("popm")
	private int popperMisses;

	@JsonProperty("poph")
	private int popperHits;

	@JsonProperty("popnpm")
	private int popperNonPenaltyMisses;

	@JsonProperty("popns")
	private int popperNoshootHits;

	@JsonProperty("rawpts")
	private int points;

	@JsonProperty("str")
	private double[] time;

	@JsonProperty("ts")
	private int[] paperTargetHits;

	@JsonProperty("proc")
	private int proceduralPenalties;

	public String getShooterId() {
		return shooterId;
	}

	public void setShooterId(String shooterId) {
		this.shooterId = shooterId;
	}

	public Calendar getModified() {
		return modified;
	}

	public void setModified(Calendar modified) {
		this.modified = modified;
	}

	public int getPopperMisses() {
		return popperMisses;
	}

	public void setPopperMisses(int popperMisses) {
		this.popperMisses = popperMisses;
	}

	public int getPopperHits() {
		return popperHits;
	}

	public void setPopperHits(int popperHits) {
		this.popperHits = popperHits;
	}

	public int getPopperNonPenaltyMisses() {
		return popperNonPenaltyMisses;
	}

	public void setPopperNonPenaltyMisses(int popperNonPenaltyMisses) {
		this.popperNonPenaltyMisses = popperNonPenaltyMisses;
	}
	public int getPopperNoshootHits() {
		return popperNoshootHits;
	}

	public void setPopperNoshootHits(int popperNoshootHits) {
		this.popperNoshootHits = popperNoshootHits;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public double[] getTime() {
		return time;
	}

	public void setTime(double[] time) {
		this.time = time;
	}

	public int[] getPaperTargetHits() {
		return paperTargetHits;
	}

	public void setPaperTargetHits(int[] paperTargetHits) {
		this.paperTargetHits = paperTargetHits;
	}

	public int getProceduralPenalties() {
		return proceduralPenalties;
	}

	public void setProceduralPenalties(int proceduralPenalties) {
		this.proceduralPenalties = proceduralPenalties;
	}

}
