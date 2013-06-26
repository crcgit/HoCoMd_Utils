package gov.howardcountymd.utils.gis;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLngBounds;

public class GisPlaces extends Gis {

	private String itsGisOnlineMarqueeSelectPathStr = "/iOSmyNeighborhood/gisOnlineMarqueeSelect.asp";
	
	// Contains
	
	private ResultListener itsResultListener;
	private IPlaceFactory itsPlaceFactory;
	private GisMarqueeSelect itsGisMarqueeSelect;
	
	// Public
	
	public GisPlaces() {
		super();
		itsGisMarqueeSelect = new GisMarqueeSelect(itsGisOnlineMarqueeSelectPathStr);
	}
	
	public void requestPlaces(LatLngBounds latLngBounds, String dbTable, 
								ResultListener resultListener, 
								IPlaceFactory placeFactory) {
		
		// Store callback
		itsResultListener = resultListener;
		itsPlaceFactory = placeFactory;
		
		GisMarqueeSelect.ResultListener listener = new GisMarqueeSelect.ResultListener() {
			@Override
			public void gotJsonArray(JSONArray jsonArray) {
				// TODO Auto-generated method stub
				ArrayList<IPlace> places = getPlacesFromJsonStr(jsonArray, itsPlaceFactory);
				itsResultListener.gotPlaces(places);
			}
		};
		itsGisMarqueeSelect.requestJsonArray(latLngBounds, dbTable, listener);
	}
	
	private ArrayList<IPlace> getPlacesFromJsonStr(JSONArray jsonArray, IPlaceFactory placeFactory) {
		// Create array of places from JSONArray
		ArrayList<IPlace> places = new ArrayList<IPlace>();
		for(int i=0; i<jsonArray.length(); i++) { 
			// Get an item to query
			JSONObject placeJsonObj = null;
			try {
				placeJsonObj = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IPlace place = placeFactory.createPlace(placeJsonObj);
	        places.add(place);
		}	
		return places;
	}
	
	// Callback for results
	
	public abstract static class ResultListener {
		public abstract void gotPlaces(ArrayList<IPlace> places);
	}
}
