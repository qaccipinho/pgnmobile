package com.pgn.gis.libraries;

import java.util.Map;

public class MyLib {

	public static final String TAG = "MyLib";

	public Map<String,Integer> getLayersID(String[] layers) {
		Map<String,Integer> ret = null;

		try {
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	/**
	 * Method to convert string array to integer array
	 * @param strArray array of string to be converted
	 * @return array of integer
	 */
	public int[] toIntArray(String[] strArray) {

		if (strArray == null) return null;

		int[] ret = new int[strArray.length];

		try {
			for (int i=0;i<strArray.length;i++) {
				ret[i] = Integer.parseInt(strArray[i]);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		return ret;
	}
}
