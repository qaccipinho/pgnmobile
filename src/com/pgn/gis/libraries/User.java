package com.pgn.gis.libraries;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class User {

	//properties
	private JSONParser jsonParser;
	private static String urlService = "http://gis.pgn.co.id/PGNServices/UntukAndroid/PGNGISServices.asmx";
	//end of properties

	//constructor
	public User()
	{
		jsonParser = new JSONParser();
	}
	
	public User(String url)
	{
		jsonParser = new JSONParser();
		urlService = url;
	}
	//end of constructor
	
	//methods
	/**
	 * Method for login
	 * @param user_ldap
	 * @param password
	 * @return JSONObject
	 */
	public JSONObject loginUser(String userLDAP, String password)
	{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		String url = urlService + "/Login";
		params.add(new BasicNameValuePair("UserLDAP", userLDAP));
		params.add(new BasicNameValuePair("Password", password));
		JSONObject json = jsonParser.getJsonFromUrl(url, params);
		
		return json;
	}
	//end of methods
}
