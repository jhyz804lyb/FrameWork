package com.jh.common;

public class InitMsg {
	private int size;
	private String []filds;
	private Object[]values;
	private boolean isId;
	private boolean isSelectKey;
	public InitMsg() {
		// TODO Auto-generated constructor stub
	}
	
	public InitMsg(int size, String[] filds, Object[] values) {
		super();
		this.size = size;
		this.filds = filds;
		this.values = values;
	}
	

	public boolean isId() {
		return isId;
	}

	public void setId(boolean isId) {
		this.isId = isId;
	}

	public boolean isIskeys() {
		return isSelectKey;
	}

	public void setIskeys(boolean iskeys) {
		this.isSelectKey = iskeys;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String[] getFilds() {
		return filds;
	}
	public void setFilds(String[] filds) {
		this.filds = filds;
	}
	public Object[] getValues() {
		return values;
	}
	public void setValues(Object[] values) {
		this.values = values;
	}
	

}
