package gov.howardcountymd.utils.gis;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import gov.howardcountymd.utils.http.HttpAccess;
import gov.howardcountymd.utils.http.IHttpAccess;

public abstract class Gis {

	// Constants
	
	protected static String gisUrlStr =  "http://data.howardcountymd.gov";
	
	// Contains
	
	protected IHttpAccess itsHttpAccess;
	
	public Gis() {
		itsHttpAccess = new HttpAccess();
	}
	
	// Utils
	
	public static int getIntWithKey(String key, JSONObject jsonObj) {
		int result = 0;
		try {
			result = jsonObj.getInt(key);
		} 
		catch (JSONException e) {
			System.out.println("Unable to retrieve int from json object with key: " + key);
		}
		return result;
	}
	
	public static double getDoubleWithKey(String key, JSONObject jsonObj) {
		double result = 0.0;
		try {
			result = jsonObj.getDouble(key);
		} 
		catch (JSONException e) {
			System.out.println("Unable to retrieve double from json object with key: " + key);
		}
		return result;
	}
	
	public static String getStringWithKey(String key, JSONObject jsonObj) {
		String result = null;
		try {
			result = jsonObj.getString(key).trim();
		} 
		catch (JSONException e) {
			System.out.println("Unable to retrieve string from json object with key: " + key);
		}
		return result;
	}
	
	public static boolean getBooleanWithKey(String key, JSONObject jsonObj) {
		boolean result = false;
		try {
			result = jsonObj.getBoolean(key);
		} 
		catch (JSONException e) {
			System.out.println("Unable to retrieve boolean from json object with key: " + key);
		}
		return result;
	}
	
	public static Bitmap getBitmapWithKey(String key, JSONObject jsonObj) {
		String bitmapStr = getStringWithKey(key, jsonObj);
		Bitmap bitmap = getBitmapForString(bitmapStr);
		return bitmap;
	}
	
	public static Bitmap getBitmapForString(String bitmapStrIn) {
		Bitmap bitmap = null;
		try {
			// If decoding fails, just return a null
			String bitmapStr = bitmapStrIn.replace(" ", "+");
		    byte[] bitmapBytes = Base64.decode(bitmapStr.getBytes(), Base64.DEFAULT);
		    bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
		} 
		finally {
		}
		return bitmap;
	}
	
	public static Bitmap getDecodedBitmapForBitmap(Bitmap bitmapIn, int quality) {
		// Use this to see effect of compression on bitmap given.
		String bitmapStr = getStringForBitmap(bitmapIn, quality);
		Bitmap bitmap = getBitmapForString(bitmapStr);
		return bitmap;
	}
	
	public static String getStringForBitmap(Bitmap bitmap, int quality) {	
		// Create buffer for bitmap bytes, compress to .jpeg, output to byte array
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
	    bitmap.compress(CompressFormat.JPEG, quality, buffer);
		byte[] bitmapBytes = buffer.toByteArray();
		// Convert String in Base64
		String bitmapStr = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);	
		return bitmapStr;
	}
	
}
