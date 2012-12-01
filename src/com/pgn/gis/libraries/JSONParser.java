package com.pgn.gis.libraries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	
	//properties
	static InputStream inputStream = null;
	static JSONObject jsonObj = null;
	static String json = "";
	//end of properties
	
	//constructor
	public JSONParser()
	{
	}
	//end of constructor
	
	//methods
	/**
	 * Method for send service request and get the json response
	 * @param url
	 * @param params
	 * @return JSONObject
	 */
	public JSONObject getJsonFromUrl(String url, List<NameValuePair> params)
	{
		//request
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();
		}
		catch(UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
		}
		catch(ClientProtocolException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		//end of request
		
		//read response
		try
		{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			
			while((line = bufferedReader.readLine()) != null)
			{
				stringBuilder.append(line + "n");
			}
			
			inputStream.close();
			json = stringBuilder.toString();
			Log.e("JSON", json);
		}
		catch(Exception ex)
		{
			Log.e("Buffer Error", "Error converting result " + ex.toString());
		}
		//end of read response
		
		//parse
		try
		{
			jsonObj = new JSONObject(json);
		}
		catch(JSONException ex)
		{
			Log.e("JSON Parser", "Error parsing data " + ex.toString());
		}
		//end of parse
		return jsonObj;
	}
	//end of methods
}
