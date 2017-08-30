package com.jh.common;

import net.sf.json.JSONObject;

public class Msg {
	private String msg;
	private Object result;
	private boolean sussess;

	public Msg() {

	}

	public Msg(String msg, boolean isok) {
		this.msg = msg;
		this.sussess = isok;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isSussess() {
		return sussess;
	}

	public void setSussess(boolean sussess) {
		this.sussess = sussess;
	}

	public String toJson() {
		JSONObject temp = JSONObject.fromObject(this);
		return temp.toString();
	}
}
