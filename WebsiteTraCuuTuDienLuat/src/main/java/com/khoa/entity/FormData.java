package com.khoa.entity;

import java.util.List;

public class FormData {
	private String keySearch;
	private String description;
	private List<String> image;
	private List<String> audio;
	private List<String> linked;
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
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}
	public List<String> getAudio() {
		return audio;
	}
	public void setAudio(List<String> audio) {
		this.audio = audio;
	}
	public List<String> getLinked() {
		return linked;
	}
	public void setLinked(List<String> linked) {
		this.linked = linked;
	}
	
	
}
