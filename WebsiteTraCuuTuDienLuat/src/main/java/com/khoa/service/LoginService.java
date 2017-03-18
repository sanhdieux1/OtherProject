package com.khoa.service;

import org.elasticsearch.action.get.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * 
 * @author MyPC
 *
 *Reconnect to Elasticsearch
 */
@Service
public class LoginService {
	
	@Autowired
	public MyClient myClient;

	@Value("${USER_TYPE:user_table}")
	public String USER_TYPE;
	
	@Value("${INDEX_NAME:dictionary_db}")
	public String INDEX_NAME;
	
	public boolean isValidUser(String username, String password){
		GetResponse response = myClient.getClient().prepareGet(INDEX_NAME, USER_TYPE, username)
		        .execute()
		        .actionGet();
		if(response.isExists()){
			if(response.getSource().get("password").equals(password));
			return true;
		}else return false;
	}


	public void setMyClient(MyClient myClient) {
		this.myClient = myClient;
	}
}
