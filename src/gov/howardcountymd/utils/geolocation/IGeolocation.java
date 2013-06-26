package gov.howardcountymd.utils.geolocation;

import android.content.Context;

public interface IGeolocation {
	
	boolean requestLocation(Context context, IGeolocationListener geolocationListener);
	
	void cancelRequest();
}
