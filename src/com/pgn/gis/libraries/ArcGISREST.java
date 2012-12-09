package com.pgn.gis.libraries;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

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
	
	public JSONObject find(String urlMapServiceLayer, String paramType
			, String searchKey, String layers, String fields)
	{
		JSONObject ret = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		try
		{
			params.add(new BasicNameValuePair("searchText",searchKey));
			params.add(new BasicNameValuePair("contains","true"));
			params.add(new BasicNameValuePair("searchFields",fields));
			params.add(new BasicNameValuePair("sr",""));
			params.add(new BasicNameValuePair("layers",layers));
			params.add(new BasicNameValuePair("layerdefs",""));
			params.add(new BasicNameValuePair("returnGeometry","true"));
			params.add(new BasicNameValuePair("maxAllowableOffset",""));
			params.add(new BasicNameValuePair("f",paramType));
			
			urlMapServiceLayer = urlMapServiceLayer + "/find";
			JSONObject json = jsonParser.getJsonFromUrl(urlMapServiceLayer, params);
			//ret = (JSONObject)json.get("layers");
			ret = json;
		}
		catch(Exception ex)
		{
			Log.d("ArcGISREST", ex.getMessage());
		}
		
		return ret;
	}
	//end of methods
}
