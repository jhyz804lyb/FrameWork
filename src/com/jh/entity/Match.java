package com.jh.entity;

import com.jh.Interceptor.ListField;
import com.jh.Interceptor.ShowField;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Match")
@Table(name="match_all")
public class Match implements Serializable{
	private static final long serialVersionUID = 1L;
	//����  
	@Column(name="match_Name")
	@ListField(showName = "赛事",orderId = 0)
	private String matchName;
	
	@Column(name="match_Time")
	@ListField(showName = "比赛时间",orderId = 5)
	private Date matchTime;
	
	@Column(name="match_state")
	@ListField(showName = "比赛状态",orderId = 6)
	private String state;
	
	@Column(name="home_Team")
	@ListField(showName = "主队名称",orderId = 1)
	private String homeTeam;
	
	@Column(name="result")
	private String result;
	
	@Column(name="guest_Team")
	@ListField(showName = "客队名称",orderId = 2)
	private String guestTeam;
	
	@Column(name="match_centre")
	@ListField(showName = "赛果",orderId = 3)
	private String centre;
	
	@Column(name="home_Scpre")
	private Integer homeScpre;
	
	@Column(name="guest_Scpre")
	private Integer guestScpre;
	
	@Column(name="home_Centre_Scpre")
	private Integer homeCentreScpre;
	
	@Column(name="guest_Centre_Scpre")
	private Integer guestCentreScpre;
	
	@Column(name="match_dish")
	private String dish;
	
	@Column(name="size_Dish")
	private String sizeDish;
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	@Column(name="match_Id")
	private Integer matchId;
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer ID;
	
	
	@Column(name="match_note")
	private String  note;
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getMatchName() {
		return matchName;
	}
	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	public Date getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(Date matchTime) {
		this.matchTime = matchTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getGuestTeam() {
		return guestTeam;
	}
	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}
	public String getCentre() {
		return centre;
	}
	public void setCentre(String centre) {
		this.centre = centre;
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
	public Integer getHomeCentreScpre() {
		return homeCentreScpre;
	}
	public void setHomeCentreScpre(Integer homeCentreScpre) {
		this.homeCentreScpre = homeCentreScpre;
	}
	public Integer getGuestCentreScpre() {
		return guestCentreScpre;
	}
	public void setGuestCentreScpre(Integer guestCentreScpre) {
		this.guestCentreScpre = guestCentreScpre;
	}
	public String getDish() {
		return dish;
	}
	public void setDish(String dish) {
		this.dish = dish;
	}
	public String getSizeDish() {
		return sizeDish;
	}
	public void setSizeDish(String sizeDish) {
		this.sizeDish = sizeDish;
	}
	public Integer getMatchId() {
		return matchId;
	}
	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}
	@Override
	public String toString() {
		return "Match [matchName=" + matchName + ", matchTime=" + matchTime
				+ ", state=" + state + ", homeTeam=" + homeTeam + ", result="
				+ result + ", guestTeam=" + guestTeam + ", centre=" + centre
				+ ", homeScpre=" + homeScpre + ", guestScpre=" + guestScpre
				+ ", homeCentreScpre=" + homeCentreScpre
				+ ", guestCentreScpre=" + guestCentreScpre + ", dish=" + dish
				+ ", sizeDish=" + sizeDish + ", matchId=" + matchId + "note="+note;
	}
	
	
}
