package com.pgn.gis;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.KeyEvent;

import com.esri.android.map.LocationService;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.pgn.gis.libraries.MyTouchListener;


public class MobilePGNActivity extends Activity {

	ArcGISDynamicMapServiceLayer dynamicLayer;
	ArcGISTiledMapServiceLayer tiledLayer;
	MapView mMapView;
	Point gpsPoint;
	MyTouchListener myTouchListener;
	String besmap, opmap;

	public static final String TAG = "MobilePGN-Map";
	public final double DEFAULT_SCALE = 9017.87105013534;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map);
        
        besmap = "http://gis.pgn.co.id/ArcGIS/rest/services/BasemapPGN_Pusat/MapServer";
        opmap = "http://gis.pgn.co.id/ArcGIS/rest/services/mobile/MapServer";

		mMapView = (MapView)findViewById(R.id.peta);
		mMapView.setScale(DEFAULT_SCALE);
		
		tiledLayer = new ArcGISTiledMapServiceLayer(besmap);
		dynamicLayer = new ArcGISDynamicMapServiceLayer(opmap);

		mMapView.addLayer(tiledLayer);
		mMapView.addLayer(dynamicLayer);
		
		if (dynamicLayer.isInitialized()) {
			currentLocation();
			myTouchListener = new MyTouchListener(getApplicationContext(), mMapView, opmap);
			mMapView.setOnTouchListener(myTouchListener);
		}
		else {
			dynamicLayer.setOnStatusChangedListener(new OnStatusChangedListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void onStatusChanged(Object source, STATUS status) {
					if (status == STATUS.INITIALIZED) {
						currentLocation();
						myTouchListener = new MyTouchListener(getApplicationContext(), mMapView, opmap);
						mMapView.setOnTouchListener(myTouchListener);
					}
				}
			});
		}

    }
    
    public void currentLocation() {
		LocationService locationService = this.mMapView.getLocationService();
		locationService.setAutoPan(false);
		locationService.setLocationListener(new LocationListener() {
			boolean locationChanged = false;
			
			public void onLocationChanged(Location loc) {

				double locy = loc.getLatitude();
		        double locx = loc.getLongitude();

				if(!locationChanged) {
					locationChanged = true;

			        Point titikWGS = new Point(locx, locy);
			        gpsPoint = (Point)GeometryEngine.project
			        		(titikWGS, SpatialReference.create(4326), mMapView.getSpatialReference());

			        zoomTo(gpsPoint, DEFAULT_SCALE);
				}
			}

			public void onProviderDisabled(String arg0) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
		});
		
		locationService.start();
    }
    
    public void zoomTo(Point point, double scale) {
    	mMapView.zoomToScale(point, scale);
    }

	@Override 
	protected void onDestroy() { 
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
	}
	@Override 	protected void onResume() {
		super.onResume(); 
		mMapView.unpause();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

}