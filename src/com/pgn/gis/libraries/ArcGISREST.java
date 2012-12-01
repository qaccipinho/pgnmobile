package com.pgn.gis.libraries;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ArcGISREST {
	
	//properties
	private JSONParser jsonParser;
	//private static String urlRESTService = "http://gis.pgn.co.id/ArcGIS/rest/services";
	//end of properties
	
	//constructor
	public ArcGISREST()
	{
		jsonParser = new JSONParser();
	}
	
	/*
	public ArcGISREST(String urlRESTService)
	{
		this.urlRESTService = urlRESTService;
		jsonParser = new JSONParser();
	}
	*/
	//end of constructor

	//methods
	public Boolean isTiledMapServiceLayer(String urlMapServiceLayer, String paramType)
	{
		Boolean ret = false;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		try
		{
			params.add(new BasicNameValuePair("f",paramType));
			JSONObject json = jsonParser.getJsonFromUrl(urlMapServiceLayer, params);
			ret = Boolean.parseBoolean(json.get("singleFusedMapCache").toString());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return ret;
	}
	
	public JSONObject getExtent(String urlMapServiceLayer, String paramType)
	{
		JSONObject ret = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		try
		{
			params.add(new BasicNameValuePair("f",paramType));
			JSONObject json = jsonParser.getJsonFromUrl(urlMapServiceLayer, params);
			ret = (JSONObject)json.get("initialExtent");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return ret;
	}
	
	public JSONObject getLayers(String urlMapServiceLayer, String paramType)
	{
		JSONObject ret = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		try
		{
			params.add(new BasicNameValuePair("f",paramType));
			urlMapServiceLayer = urlMapServiceLayer + "/layers";
			JSONObject json = jsonParser.getJsonFromUrl(urlMapServiceLayer, params);
			ret = (JSONObject)json.get("layers");
			//ret = json;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return ret;
	}
	//end of methods
}
