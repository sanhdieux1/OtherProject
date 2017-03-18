package com.khoa.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class WordDelail {
	private long date;
	private String keySearch;
	private String description;
	private Multimedia multimedia;
	private List<Version> previous;
	private String byUser;
	public WordDelail(){
		previous = new ArrayList<Version>();
		date = new Date().getTime();
		keySearch = new String();
		description = new String();
		multimedia = new Multimedia();
		byUser = new String();
	}
	
	
	public long getDate() {
		return date;
	}


	public void setDate(long date) {
		this.date = date;
	}


	public String getKeySearch() {
		return keySearch;
	}
	public void setKeySearch(String keySearch) {
		this.keySearch = keySearch;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Multimedia getMultimedia() {
		return multimedia;
	}
	public void setMultimedia(Multimedia multimedia) {
		this.multimedia = multimedia;
	}
	public List<Version> getPrevious() {
		return previous;
	}
	public void setPrevious(List<Version> previous) {
		this.previous = previous;
	}
	public String getByUser() {
		return byUser;
	}
	public void setByUser(String byUser) {
		this.byUser = byUser;
	}
	
	
}
