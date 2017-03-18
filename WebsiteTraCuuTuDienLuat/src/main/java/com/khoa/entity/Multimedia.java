package com.khoa.entity;

import java.util.ArrayList;
import java.util.List;

public class Multimedia {
	private List<String> image;
	private List<String> audio;
	private List<String> linked;
	
	public Multimedia(){
		image= new ArrayList<String>();
		audio= new ArrayList<String>();
		linked= new ArrayList<String>();
	}
	public void putImage(String imageLink){
		image.add(imageLink);
	}
	public void putAudio(String audioLink){
		audio.add(audioLink);
	}
	public void putLinked(String linkedLink){
		linked.add(linkedLink);
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
