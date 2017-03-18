package com.khoa.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.khoa.entity.MyUserDetails;

@Service("customUserDetailsService")

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    Gson gson;

    @Autowired
    ElasticsearchService elasticsearchService;

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (userName.equals("admin")) {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            SimpleGrantedAuthority authorityDBA = new SimpleGrantedAuthority("ROLE_DBA");
            authorities.add(authorityDBA);
            MyUserDetails admin = new MyUserDetails("admin", "admin", authorities);
            admin.setFullName("Adminitrator");
            return admin;
        } else {
            GetResponse response = elasticsearchService.getUserDetails(userName);
            if (response == null) {
                throw new UsernameNotFoundException("User name not found");
            }
            if (response.isSourceEmpty()) {
                throw new UsernameNotFoundException("User name not found");
            }

            JsonObject obj = gson.fromJson(response.getSourceAsString(), JsonObject.class);
            String password = obj.get("password").getAsString();
            String role = obj.get("role").getAsString();

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            if (role.equals("ADMIN")) {
                SimpleGrantedAuthority authorityADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
                authorities.add(authorityADMIN);
            } else if (role.equals("DBA")) {
                SimpleGrantedAuthority authorityDBA = new SimpleGrantedAuthority("ROLE_DBA");
                authorities.add(authorityDBA);
            } else {
                SimpleGrantedAuthority authorityUSER = new SimpleGrantedAuthority("ROLE_USER");
                authorities.add(authorityUSER);
            }

            MyUserDetails userDetail = new MyUserDetails(userName, password, authorities);
            try {
                userDetail.setAddress(obj.get("address").getAsString());
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            try {
                userDetail.setDateCreate(obj.get("dateCreate").getAsLong());
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
            try {
                userDetail.setEmail(obj.get("email").getAsString());
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            try {
                userDetail.setFullName(obj.get("fullName").getAsString());
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            try {
                userDetail.setPhoneNumber(obj.get("phoneNumber").getAsString());
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            try {
                userDetail.setSex(obj.get("sex").getAsString());
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            try {
                userDetail.setBirthDay(obj.get("birthDay").getAsLong());
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            return userDetail;
        }
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
