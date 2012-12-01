package com.pgn.gis.libraries;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;

public class MyQuery extends AsyncTask<String, Void, FeatureSet> {
	
	MapView map;
	Context context;
	Envelope qEnvelope;
	
	public static final String TAG = "MyQuery";
	
	public MyQuery(Context context, MapView map) {
		this.context = context;
		this.map = map;
	}
	
	public void setGeometry(Envelope env) {
		this.qEnvelope = env;
	}
	
	@Override
	protected void onPreExecute() {
	}

	@Override
	protected FeatureSet doInBackground(String... qParams) {
		if (qParams == null || qParams.length <= 1)
			return null;

		String url = qParams[0];
		Query query = new Query();
		String whereClause = qParams[1];
		SpatialReference sr = map.getSpatialReference();
		query.setGeometry(qEnvelope);
		query.setOutSpatialReference(sr);
		query.setReturnGeometry(true);
		query.setWhere(whereClause);

		QueryTask qTask = new QueryTask(url);
		FeatureSet fs = null;
		
		try {
			fs = qTask.execute(query);
		} catch (Exception e) {
			Log.d(TAG, "Error di doInBackGround");
			return fs;
		}

		return fs;
	}
	
	@Override
	protected void onPostExecute(FeatureSet result) {
		super.onPostExecute(result);
	}

}
