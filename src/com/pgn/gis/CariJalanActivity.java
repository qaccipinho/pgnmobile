package com.pgn.gis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.esri.core.geometry.Point;
import com.pgn.gis.libraries.ArcGISREST;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CariJalanActivity extends Activity {
	
	ImageButton ibtnCariJalan;
	TextView tvDebug;
	EditText txtCari;
	Button btnBersih;
	
	ArcGISREST arcGISRest;
	
	public static final String TAG = "CariJalan";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_cari_jalan);
		
		arcGISRest = new ArcGISREST();
		
		btnBersih = (Button) findViewById(R.id.btnBersih);
		ibtnCariJalan = (ImageButton) findViewById(R.id.btnCari);
		txtCari = (EditText) findViewById(R.id.txtSearchKey);
		
		ibtnCariJalan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Log.d(TAG, "Proses Cari");
				
				JSONObject json;
				String urlMap = "http://gis.pgn.co.id/ArcGIS/rest/services/BasemapPGN_Pusat/MapServer";
				String paramType = "pjson";
				String searchKey = txtCari.getText().toString();
				String layers = "3";
				String fields = "LABEL";
				
				json = arcGISRest.find(urlMap, paramType, searchKey, layers, fields);
				
				prosesHasilCari(json);
			}
		});
		
		btnBersih.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	private void prosesHasilCari(JSONObject json) {
		try {
			JSONArray jsonArray = json.getJSONArray("results");
			Log.d(TAG, String.valueOf(jsonArray.length()));
			
			if (jsonArray.length() > 0) {
				for (int i=0;i<jsonArray.length();i++) {
					
					String objectID = jsonArray.getJSONObject(i).getJSONObject("attributes").getString("OBJECTID");
					String namaJalan = jsonArray.getJSONObject(i).getString("value");
					
					JSONArray paths = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("paths").getJSONArray(0).getJSONArray(0);
					
					double x = paths.getDouble(0);
					double y = paths.getDouble(1);
					
					Point titikAwalJalan = new Point(x, y);
					
					String msg = objectID + " " + namaJalan + " (" + x + "," + y + ")";
					Log.d(TAG, msg);
				}
			}
			else {
				//data tidak ditemukan
				Log.d(TAG, "Data Tidak ditemukan");
			}
		} catch (JSONException e) {
			Log.e(TAG, "prosesHasilCari" + e.getMessage());
		}
	}
	
	@Override
    public void onDestroy() {
    	super.onDestroy();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }
}
