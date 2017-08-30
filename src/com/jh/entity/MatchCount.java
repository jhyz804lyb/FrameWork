package com.jh.entity;

import com.jh.Interceptor.*;
import enums.OrderType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "MatchCount")
@Table(name = "match_count")
public class MatchCount {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotSerachField
	@OrderByField(orderType = OrderType.DESC)
	private Integer id;
    //比赛id
	@Column(name = "match_id")
	private Integer matchId;
   //技术分析类型  角球，危险进攻等
	@Column(name = "type_name")
	private String typeName;
  //技术分析id
	@Column(name = "count_type_id")
	private Integer countTypeId;
   //主队名称
	@Column(name = "home_Name")
	@FindKey(selectType = "like",left = "%",right = "%")
	private String homeName;
	//客队名称
	@Column(name = "guest_Name")
	private String guestName;
	//主队统计
	@Column(name = "home_Count")
	private Integer homeCount;
	//客队统计
	@Column(name = "guest_Count")
	private Integer guestCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getCountTypeId() {
		return countTypeId;
	}

	public void setCountTypeId(Integer countTypeId) {
		this.countTypeId = countTypeId;
	}

	public String getHomeName() {
		return homeName;
	}

	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public Integer getHomeCount() {
		return homeCount;
	}

	public void setHomeCount(Integer homeCount) {
		this.homeCount = homeCount;
	}

	public Integer getGuestCount() {
		return guestCount;
	}

	public void setGuestCount(Integer guestCount) {
		this.guestCount = guestCount;
	}
	

}
