package com.beans;

public class Toddler {

	private long toddlerId;
	private String toodlerName;
	private int toddlerAge;
	private BabySitter babySitter = new BabySitter();

	public long getToddlerId() {
		return toddlerId;
	}

	public void setToddlerId(long toddlerId) {
		this.toddlerId = toddlerId;
	}

	public String getToodlerName() {
		return toodlerName;
	}

	public void setToodlerName(String toodlerName) {
		this.toodlerName = toodlerName;
	}

	public int getToddlerAge() {
		return toddlerAge;
	}

	public void setToddlerAge(int toddlerAge) {
		this.toddlerAge = toddlerAge;
	}

	public BabySitter getBabySitter() {
		return babySitter;
	}

	public void setBabySitter(BabySitter babySitter) {
		this.babySitter = babySitter;
	}

}
