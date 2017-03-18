package com.khoa.entity;


public class UserDetailsForm  {
	private String username;  
	private String password;  
	private String email;
	private String fullName;
	private String address;
	private long dateCreate;
	private long lastUpdate;
	private long birthDay;
	private String sex;
	private String phoneNumber;
	private String role;
	private String createBy;
	public UserDetailsForm() {
		username = new String();
		password = new String();
		email = new String();
		fullName = new String();
		address = new String();
		phoneNumber = new String();
		role = new String();
		sex = new String();
		createBy = new String();
	}
	
	public long getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(long birthDay) {
		this.birthDay = birthDay;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public boolean validate(){
		if(username=="" || password=="" || fullName=="" || username==null || password==null || fullName==null){
			return false;
		}else return true;
	}
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getDateCreate() {
		return dateCreate;
	}
	public void setDateCreate(long dateCreate) {
		this.dateCreate = dateCreate;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	

	
}
