package gov.howardcountymd.utils.gis;

import com.google.android.gms.maps.model.LatLng;

public class PlaceRelative extends PlaceDecorator {
	
	private double itsDistanceMi;
	private double itsBearingToDeg;
	private String itsBearingToCardinal;
	private boolean isRelativeAvail;

	public PlaceRelative( IPlace place, LatLng userLatLng ) {
		itsPlace = place;
		updateRelativePosition(userLatLng);
	}
	
	public PlaceRelative( IPlace place ) {
		itsPlace = place;
		updateRelativePosition(null);
	}
	
	public PlaceRelative() {
		itsPlace = new Place();
		updateRelativePosition(null);
	}
	
	// Override PlaceDecorator so that when lat and lng change, relative calcs change too.
	@Override
	public void setLatLng(LatLng latLng) {
		itsPlace.setLatLng(latLng);
		updateRelativePosition(latLng);
	}
	
	public void updateRelativePosition(LatLng userLatLng) {
		if( userLatLng != null ) {
			itsDistanceMi = setDistanceBetweenLatLng(userLatLng, itsPlace.getLatLng());
			itsBearingToDeg = setBearingDegFromUserToObjLatLng(userLatLng, itsPlace.getLatLng());
			itsBearingToCardinal = getBearingToString(itsBearingToDeg);
			isRelativeAvail = true;
		}
		else {
			itsDistanceMi = 0.0;
			itsBearingToDeg = 0.0;
			itsBearingToCardinal = getBearingToString(itsBearingToDeg);
			isRelativeAvail = false;
		}
	}
	
	public boolean getIsRelativeAvail() {
		return isRelativeAvail;
	}
	
	public Double getDistanceMi() {
		return itsDistanceMi;
	}
	
	public Double getBearingToDeg() {
		return itsBearingToDeg;
	}
	
	public String getBearingToStr() {
		return itsBearingToCardinal;
	}
	
	private String getBearingToString( double bearingStringDeg ) {
		String bearingStr = null;
		int x = (int) Math.floor((bearingStringDeg+22.5) / 45.0);
		switch( x ) {
		case 0:
			bearingStr = "N";
			break;
		case 1:
			bearingStr = "NE";
			break;
		case 2:
			bearingStr = "E";
			break;
		case 3:
			bearingStr = "SE";
			break;
		case 4:
			bearingStr = "S";
			break;
		case 5:
			bearingStr = "SW";
			break;
		case 6:
			bearingStr = "W";
			break;
		case 7:
			bearingStr = "NW";
			break;	
		case 8:
			bearingStr = "N";
			break;	
		}
		return bearingStr;
	}
	
	private double setBearingDegFromUserToObjLatLng(LatLng userLatLng, LatLng objectLatLng) {
		// Source: http://www.movable-type.co.uk/scripts/latlong.html
		double dLon = (objectLatLng.longitude - userLatLng.longitude) * (Math.PI/180.0);
		double lat1rad = userLatLng.latitude * (Math.PI/180.0);
		double lat2rad = objectLatLng.latitude * (Math.PI/180.0);
		
		double y = Math.sin(dLon) * Math.cos(lat2rad);
		double x = Math.cos(lat1rad)*Math.sin(lat2rad) -
		        Math.sin(lat1rad)*Math.cos(lat2rad)*Math.cos(dLon);
		double bearingDeg = Math.atan2(y, x) * (180.0/Math.PI);
		
		// Force bearing to be positive
		if(bearingDeg < 0.0) {
			bearingDeg += 360.0;
		}
		
		return bearingDeg;
	}
	
	private double setDistanceBetweenLatLng(LatLng userLatLng, LatLng objectLatLng) {
		// Source: http://www.movable-type.co.uk/scripts/latlong.html (Haversine)
		Double  R = 6371.0 / 1.609344; // miles
		Double dLat = (userLatLng.latitude-objectLatLng.latitude) * (Math.PI/180.0);
		Double dLon = (userLatLng.longitude-objectLatLng.longitude) * (Math.PI/180.0);
		Double lat1 = objectLatLng.latitude * (Math.PI/180.0);
		Double lat2 = userLatLng.latitude * (Math.PI/180.0);
		
		Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
					Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		Double d = R * c;
		return d;
	}
	
    public static PlaceRelative findPlaceRelative(IPlace place) {
    	// Drill down through decorators and return first PlaceRelative
    	PlaceRelative placeRelative = null;
    	if( !(place instanceof Place) ) {
        	while( (!(place instanceof PlaceRelative)) && (!(place instanceof Place)) ) {
        		place = ((PlaceDecorator) place).itsPlace;
        	}
        	if( place instanceof PlaceRelative ) {
        		placeRelative = (PlaceRelative) place;
        	}
    	}
    	return placeRelative;
    }
	
}
