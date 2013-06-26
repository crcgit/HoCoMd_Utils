package gov.howardcountymd.utils.gis;

import gov.howardcountymd.utils.http.HttpResult;
import gov.howardcountymd.utils.http.IHttpResult;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gms.maps.model.LatLngBounds;

public class GisMarqueeSelect extends Gis {
	
	// ASP file paths
	
	private String itsGisOnlineMarqueeSelectPathStr = "/iOSmyNeighborhood/gisOnlineMarqueeSelect.asp";
	
	// Contains
	
	private ResultListener itsResultListener;
	
	// Constructor
	
	public GisMarqueeSelect(String pathStr) {
		itsGisOnlineMarqueeSelectPathStr = pathStr;
	}
	
	// Public
	
	public void requestJsonArray(LatLngBounds latLngBounds, String dbTable, 
									ResultListener resultListener) {
		// Store callback
		itsResultListener = resultListener;

		// Setup params for http get (note mapping from NE/SW to NW/SE corner... :p)
		String lonNEStr = Double.toString(latLngBounds.northeast.longitude);
		String latNEStr = Double.toString(latLngBounds.northeast.latitude);
		String lonSWStr = Double.toString(latLngBounds.southwest.longitude);
		String latSWStr = Double.toString(latLngBounds.southwest.latitude);
		
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("lonNW", lonSWStr)); 
		params.add(new BasicNameValuePair("latNW", latNEStr)); 
		params.add(new BasicNameValuePair("lonSE", lonNEStr)); 
		params.add(new BasicNameValuePair("latSE", latSWStr)); 
		params.add(new BasicNameValuePair("dbTable", dbTable)); 
		
		// Define and instantiate callback for HttpAccess
		IHttpResult httpResult = new HttpResult() {
			@Override
			public void gotOutputStr(String outputStr) {
				// Convert string output to JSONArray
				JSONArray jsonArray = null;
				try {
					jsonArray = new JSONArray(outputStr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				itsResultListener.gotJsonArray(jsonArray);
			}
		};
		
		// Request string of http get
		String gisOnlineMarqueeSelectUrlStr = gisUrlStr + itsGisOnlineMarqueeSelectPathStr;
		itsHttpAccess.requestStringForHttpGet(gisOnlineMarqueeSelectUrlStr, params, httpResult);
	}
	
	// Callback for results
	
	public abstract static class ResultListener {
		public abstract void gotJsonArray(JSONArray jsonArray);
	}
}
