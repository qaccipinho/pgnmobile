package com.pgn.gis.libraries;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.tasks.ags.identify.IdentifyParameters;

public class MyTouchListener extends MapOnTouchListener {

	MapView map;
	Context context;
	Magnifier mag;
	String opmap;
	Bitmap snapshot = null;
	boolean calloutStatus = false;
	boolean redrawCache = true;
	boolean showmag = false;
	
	private final static String TAG = "MyTouchListener";

	public MyTouchListener(Context context, MapView view, String opmap) {
		super(context, view);
		this.context = context;
		this.opmap = opmap;
		this.map = view;
	}
	
	@Override
	public void onLongPress(MotionEvent point) {
		this.magnify(point);
		Log.d(TAG,"Magnify Long Press Event");
		this.showmag = true;
	}
	
	@Override
	public boolean onDragPointerMove(MotionEvent from, final MotionEvent to) {
		if (this.showmag) {
			this.magnify(to);
			return true;
		}
		return super.onDragPointerMove(from, to);
	}
	
	@Override
	public boolean onDragPointerUp(MotionEvent from, final MotionEvent to) {
		if (this.showmag) {
			if(this.mag != null) {
				this.mag.hide();
			}
			this.mag.postInvalidate();
			this.showmag = false;
			this.redrawCache = true;
			Point point = this.map.toMapPoint(new Point(to.getX(), to.getY()));

			if (!calloutStatus) {
				MyIdentify myIdentify = new MyIdentify(context, point, map, opmap);
				IdentifyParameters iParams = myIdentify.createIdentifyParameters(point);
				myIdentify.execute(iParams);
			}
			else {
				map.getCallout().hide();
			}

			return true;
		}
		return super.onDragPointerUp(from, to);
	}
	
	void magnify(MotionEvent to) {
		if (this.mag == null) {
			this.mag = new Magnifier(this.context, this.map);
			this.map.addView(mag);
			this.mag.prepareDrawingCacheAt(to.getX(), to.getY());
			Log.d(TAG, "Magnify first time");
		}
		else {
			this.mag.prepareDrawingCacheAt(to.getX(), to.getY());
			Log.d(TAG, "Magnify");
		}
	}
	
	@Override
	public boolean onSingleTap(MotionEvent e) {
		Log.d(TAG, "Single Tap");
		
		Point point = this.map.toMapPoint(new Point(e.getX(), e.getY()));

		if (!calloutStatus) {
			MyIdentify myIdentify = new MyIdentify(context, point, map, opmap);
			IdentifyParameters iParams = myIdentify.createIdentifyParameters(point);
			myIdentify.execute(iParams);
		}
		else {
			map.getCallout().hide();
		}

		this.redrawCache = true;
		return true;
	}

}
