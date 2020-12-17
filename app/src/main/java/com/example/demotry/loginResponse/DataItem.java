package com.example.demotry.loginResponse;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("PASSWORD")
	private String password;

	@SerializedName("USER_NAME")
	private String username;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NonNull
	@Override
	public String toString() {
		return "Username : "+this.username + " Password : "+ password;
	}
}