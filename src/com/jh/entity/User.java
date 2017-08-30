package com.jh.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="User")
@Table(name="User")
public class User implements Serializable{
/**
	 * 
	 */
private static final long serialVersionUID = 1L;
@Column(name="U_NAME")
private String username;
@Column(name="U_PWD")
private String pwd;
@Column(name="U_PHONE")
private String phone;
@Column(name="U_ID")
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private Integer id;
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPwd() {
	return pwd;
}
public void setPwd(String pwd) {
	this.pwd = pwd;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}

}
