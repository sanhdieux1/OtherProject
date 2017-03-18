package com.khoa.entity;


public class Document {
	private String idFile;
	private String title;
	private String byUser;
	private long dateCreate;
	private String document;
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public String getIdFile() {
		return idFile;
	}
	public void setIdFile(String idFile) {
		this.idFile = idFile;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getByUser() {
		return byUser;
	}
	public void setByUser(String byUser) {
		this.byUser = byUser;
	}
	public long getDateCreate() {
		return dateCreate;
	}
	public void setDateCreate(long dateCreate) {
		this.dateCreate = dateCreate;
	}
	
	
}
