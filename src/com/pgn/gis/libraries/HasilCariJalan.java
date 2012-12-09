package com.pgn.gis.libraries;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.esri.core.geometry.Point;

@SuppressWarnings("serial")
public class HasilCariJalan implements Serializable {

	private Map<Integer, Jalan> HasilCari;
	private int selectedObjectID = -1;

	public HasilCariJalan() {
		HasilCari = new HashMap<Integer, Jalan>();
	}
	
	public void hapusHasilCari() {
		HasilCari.clear();
		selectedObjectID = -1;
	}
	
	public int getSizeHasilCari() {
		return HasilCari.size();
	}
	
	public int getSelectedObjectID() {
		if (selectedObjectID == -1 || getSizeHasilCari() == 0) return -1;
		
		return HasilCari.get(selectedObjectID).getObjectID();
	}
	
	public String getSelectedNamaJalan() {
		if (selectedObjectID == -1 || getSizeHasilCari() == 0) return null;
		
		return HasilCari.get(selectedObjectID).getNamaJalan();
	}
	
	public Point getSelectedTitikJalan() {
		if (selectedObjectID == -1 || getSizeHasilCari() == 0) return null;
		
		return HasilCari.get(selectedObjectID).getTitikJalan();
	}
	
	public void addDataHasilCari(int objectID, String namaJalan, Point titikJalan) {
		
		Jalan jalan = new Jalan();
		
		jalan.setObjectID(objectID);
		jalan.setNamaJalan(namaJalan);
		jalan.setTitikJalan(titikJalan);
		
		HasilCari.put(objectID, jalan);
	}
	
	private class Jalan {
		private int objectID;
		private String namaJalan;
		private Point titikJalan;

		public int getObjectID() {
			return objectID;
		}
		
		public void setObjectID(int objectID) {
			this.objectID = objectID;
		}
		
		public String getNamaJalan() {
			return namaJalan;
		}
		
		public void setNamaJalan(String namaJalan) {
			this.namaJalan = namaJalan;
		}
		
		Point getTitikJalan() {
			return titikJalan;
		}
		
		void setTitikJalan(Point titikJalan) {
			this.titikJalan = titikJalan;
		}
	}
}
