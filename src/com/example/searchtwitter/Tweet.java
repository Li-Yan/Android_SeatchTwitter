package com.example.searchtwitter;

public class Tweet {
	private int id;
	private String imageURL;
	private String name;
	private String location;
	private String text;
	
	public Tweet() {
		imageURL = "";
		name = "";
		location = "";
		text = "";
	}
	
	public void setID(int ID) {
		id = ID;
	}
	public void setImageURL(String s) {
		imageURL = s;
	}
	public void setName(String s) {
		name = s;
	}
	public void setLocation(String s) {
		location = s;
	}
	public void setText(String s) {
		text = s;
	}
	
	public int getID() {
		return id;
	}
	public String getImageURL() {
		return imageURL;
	}
	public String getName() {
		return name;
	}
	public String getLocation() {
		return location;
	}
	public String getText() {
		return text;
	}
}
