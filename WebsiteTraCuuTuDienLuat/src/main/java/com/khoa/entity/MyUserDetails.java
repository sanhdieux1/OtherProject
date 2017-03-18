package com.khoa.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;  

public class MyUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;  
	private String password;  
	private String email;
	private String fullName;
	private String address;
	private long birthDay;
	private String sex;
	private long dateCreate;
	private String phoneNumber;
	private List<GrantedAuthority> authorities;  
	public MyUserDetails(String userName, String password, List<GrantedAuthority> authorities){  
		this.username = userName;  
		this.password = password;  
		this.authorities = authorities;  
	}
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public String getPassword() {
		return password;
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
	public String getUsername() {
		return username;
	}
	public boolean isAccountNonExpired() {
		return true;
	}
	public boolean isAccountNonLocked() {
		return true;
	}
	public boolean isCredentialsNonExpired() {
		return true;
	}
	public boolean isEnabled() {
		return true;
	}
	public String getEmail() {
		return email;
	}
	public String getFullName() {
		return fullName;
	}
	public String getAddress() {
		return address;
	}
	public long getDateCreate() {
		return dateCreate;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setDateCreate(long dateCreate) {
		this.dateCreate = dateCreate;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}  
	
	
}
