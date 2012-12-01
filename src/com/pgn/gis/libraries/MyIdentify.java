package com.pgn.gis.libraries;

import com.pgn.gis.*;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.identify.IdentifyTask;

public class MyIdentify extends AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {

	String[] listIdentifyField;
	String[] listIdentifyId;
	String[] listIdentify;
	SparseArray<String> layerIdName = new SparseArray<String>();
	SparseArray<String> layerIdField = new SparseArray<String>();
	
	IdentifyTask myIdentifyTask;
	LinearLayout calloutLayout;
	Context myContext;
	Callout myCallout;
	String url = "";
	Point mAnchor;
	MapView map;
	MyLib myLib;
	
	private final static String TAG = "Mydentify";

	MyIdentify(Context context, Point anchor, MapView view, String urlMapService) {
		mAnchor = anchor;
		myContext = context;
		url = urlMapService;
		map = view;
		myCallout = map.getCallout();
		myCallout.setStyle(R.xml.callout_style_1);

		prepareIdentifyLayer();
	}
	
	MyIdentify(Context context, MapView view, String urlMapService) {
		myContext = context;
		url = urlMapService;
		map = view;
		myCallout = map.getCallout();
		myCallout.setStyle(R.xml.callout_style_1);

		prepareIdentifyLayer();
	}
	
	@Override
	protected void onPreExecute() {
		myIdentifyTask = new IdentifyTask(url);
		myCallout.show(mAnchor, createCalloutLayout(mAnchor));
	}

	@Override
	protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
		IdentifyResult[] ret = null;
		if (params != null && params.length > 0) {
			IdentifyParameters mParams = params[0];
			try {
				ret = myIdentifyTask.execute(mParams);
			}
			catch (Exception e) {
				Log.d(TAG, "error di doInBackground");
			}
			
		}
		return ret;
	}

	@Override
	protected void onPostExecute(IdentifyResult[] results) {
		if (!isCancelled()) {
			if (results.length > 0) {
				if(results[0].getAttributes().get(results[0].getDisplayFieldName())!=null) {
					String fieldName = layerIdField.get(results[0].getLayerId());
					
					if (results[0].getAttributes().containsKey(fieldName)) {
						setDescription(results[0].getAttributes().get(fieldName).toString());
					}
					else {
						setDescription(null);
					}
				}
			}			
		}
	}
	
	public void setDescription(String desc) {
		TextView description = (TextView)calloutLayout.findViewById(R.id.desc);
		if (desc != null)
			description.setText(desc);
		else
			description.setText("Data tidak ditemukan");
	}
	
	public View createCalloutLayout(Point p) {
		LayoutInflater layoutInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		calloutLayout = (LinearLayout) layoutInflater.inflate(R.layout.layout_callout, null);
		TextView longitude = (TextView)calloutLayout.findViewById(R.id.longitude);
		TextView latitude = (TextView)calloutLayout.findViewById(R.id.latitude);
		TextView description = (TextView)calloutLayout.findViewById(R.id.desc);
		longitude.setText(String.valueOf(p.getX()) + "  ");
		latitude.setText(String.valueOf(p.getY()));
		description.setText("Proses Identify...");
		
		return calloutLayout;
	}
	
	public void setAnchor(Point anchor) {
		this.mAnchor = anchor;
	}
	
	private void prepareIdentifyLayer() {
		this.listIdentify = myContext.getString(R.string.list_layer_identify42).split(",");
		this.listIdentifyId = myContext.getString(R.string.list_layer_identify_id42).split(",");
		this.listIdentifyField = myContext.getString(R.string.list_layer_identify_field42).split(",");
		
		for (int i=0;i<this.listIdentifyId.length;i++) {
			this.layerIdName.put(Integer.parseInt(this.listIdentifyId[i]), this.listIdentify[i]);
			this.layerIdField.put(Integer.parseInt(this.listIdentifyId[i]), this.listIdentifyField[i]);
		}
	}
	
	public IdentifyParameters createIdentifyParameters(Point pointClicked) {
		IdentifyParameters iParams = new IdentifyParameters();

		try 
		{
			iParams.setLayers(myLib.toIntArray(listIdentifyId));
			iParams.setTolerance(5);
			iParams.setDPI(98);
			iParams.setLayerMode(IdentifyParameters.ALL_LAYERS);
			iParams.setGeometry(pointClicked);
			iParams.setSpatialReference(map.getSpatialReference());
			iParams.setMapHeight(map.getHeight());
			iParams.setMapWidth(map.getWidth());
			Envelope env = new Envelope();
			map.getExtent().queryEnvelope(env);
			iParams.setMapExtent(env);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		return iParams;
	}
	
}

