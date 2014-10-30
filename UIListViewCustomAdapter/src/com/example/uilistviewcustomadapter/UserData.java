package com.example.uilistviewcustomadapter;

import java.util.ArrayList;
import java.util.List;

public class UserData {

	private String 	name;
	private String	birthday;
	private int		picture;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public int getPicture() {
		return picture;
	}
	
	public void setPicture(int picture) {
		this.picture = picture;
	}
	
	public static List<UserData> generateRandomUsers(int numberOfUsers) {
		
		List<UserData> generatedUsers = new ArrayList<>();
		
		for ( int i = 0; i < numberOfUsers; i++ ) {
			
			UserData user = new UserData();
			user.name = Long.toHexString(Double.doubleToLongBits(Math.random()));
			user.birthday = String.valueOf(Math.random());
			user.picture = R.drawable.usericon;
			
			generatedUsers.add(user);
		}
		
		return generatedUsers;
	}
}
