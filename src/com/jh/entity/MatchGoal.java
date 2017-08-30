package com.jh.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "MatchGoal")
@Table(name = "match_goal")
public class MatchGoal {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "match_id")
	private String matchId;

	@Column(name = "time")
	private Date time;
	
	@Column(name = "home_scpre")
	private Integer homeScpre;
	
	@Column(name = "guest_scpre")
	private Integer guestScpre;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getHomeScpre() {
		return homeScpre;
	}

	public void setHomeScpre(Integer homeScpre) {
		this.homeScpre = homeScpre;
	}

	public Integer getGuestScpre() {
		return guestScpre;
	}

	public void setGuestScpre(Integer guestScpre) {
		this.guestScpre = guestScpre;
	}
	
	
}
