package com.emdoor.yispace.request;

public class UserRegisterRequest {

	private String username;
	private String password;
	private String email;

    public UserRegisterRequest(String username,String password,String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //getter and setter
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
        this.email = email;
    }
    // toString method for debugging
    @Override
    public String toString() {
        return "UserRegisterRequest{" +
        "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email +"'}";
    }
}
