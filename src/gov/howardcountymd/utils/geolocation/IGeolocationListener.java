package gov.howardcountymd.utils.geolocation;

import android.location.Location;

public interface IGeolocationListener {
	abstract void onLocationFound(Location location);
}
