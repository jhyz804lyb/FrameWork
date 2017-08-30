package com.jh.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "MatchOdd")
@Table(name = "match_odd_info")
public class MatchOdd {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "match_id")
	private String matchId;
	
	@Column(name = "match_info_time")
	private Date matchInfoTime;
	
	
	@Column(name = "home_scpre")
	private Integer homeScpre;
	
	@Column(name = "guest_scpre")
	private Integer guestScpre;
	
	@Column(name = "let_dish")
	private String letDish;
	
	@Column(name = "home_let_odd")
	private Double homeLetOdd;
	
	@Column(name = "guest_let_odd")
	private Double guestLetOdd;
	
	@Column(name = "imme_let_dish")
	private String immeLetDish;
	
	@Column(name = "imme_home_let_odd")
	private Double immeHomeLetOdd;
	
	@Column(name = "imme_guest_let_odd")
	private Double immeGuestLetOdd;
	
	private String sizeDish;
	
	
}
