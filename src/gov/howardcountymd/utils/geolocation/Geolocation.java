package gov.howardcountymd.utils.geolocation;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Geolocation implements IGeolocation {

	Timer itsLocTimeoutTimer;
    LocationManager itsLM;
    IGeolocationListener itsGeolocationListener;
    boolean isGpsEnabled;
    boolean isNetworkEnabled;
	
    public Geolocation() {
    	isGpsEnabled = false;
    	isNetworkEnabled = false;
    	itsGeolocationListener = null;
    	itsLM = null;
    	itsLocTimeoutTimer = null;
    }
    
	@Override
	public boolean requestLocation(Context context, IGeolocationListener geolocationListener) {
        // Save callback
        itsGeolocationListener = geolocationListener;
        
        //Get location manager if there isn't one already and save for other methods later
        if(itsLM==null) {
            itsLM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        //Exceptions will be thrown if provider is not permitted, so try/catch them (really?)
        try{
        	isGpsEnabled=itsLM.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex){
        	isGpsEnabled = false;
        }
        
        try{
        	isNetworkEnabled=itsLM.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex){
        	isNetworkEnabled = false;
        }

        // Request updates from providers enabled
        boolean isLocationAvail = false;
        if(isGpsEnabled || isNetworkEnabled) {
        	isLocationAvail = true;	// Location updates will be made
            if(isGpsEnabled) {
                itsLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, itsLocationListenerGps);
            }
            if(isNetworkEnabled) {
                itsLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, itsLocationListenerNetwork);
            }
            // Set up a timer to limit how long to wait on location update
            itsLocTimeoutTimer=new Timer();
            itsLocTimeoutTimer.schedule(new LocationTimeOutTimerTask(), 20000);
        }
        
        return isLocationAvail;
	}

	@Override
	public void cancelRequest() {
    	itsLocTimeoutTimer.cancel(); 
    	itsLM.removeUpdates(itsLocationListenerGps); 
    	itsLM.removeUpdates(itsLocationListenerNetwork); 
	}
	
    // Call-backs when GPS location update is made
    LocationListener itsLocationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
        	//Cancel timer, end update requests, callback with result
            itsLocTimeoutTimer.cancel();
            itsLM.removeUpdates(this);
            itsLM.removeUpdates(itsLocationListenerNetwork);
            itsGeolocationListener.onLocationFound(location);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    // Call-backs when Network location update is made
    LocationListener itsLocationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            itsLocTimeoutTimer.cancel();
            itsLM.removeUpdates(this);
            itsLM.removeUpdates(itsLocationListenerGps);
            itsGeolocationListener.onLocationFound(location);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    class LocationTimeOutTimerTask extends TimerTask {
        @Override
        public void run() {
        	// No location reported yet, otherwise timer would have been cancelled.
        	
        	// Stop asking for updates
            itsLM.removeUpdates(itsLocationListenerGps);
            itsLM.removeUpdates(itsLocationListenerNetwork);

            // Get last known locations
             Location locationNet=null, locationGps=null;
             if(isGpsEnabled)
                 locationGps=itsLM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             if(isNetworkEnabled)
                 locationNet=itsLM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

             // Favor latest known location if both results are available
             if(locationGps!=null && locationNet!=null){
                 if(locationGps.getTime()>locationNet.getTime()) {
                	 itsGeolocationListener.onLocationFound(locationGps);
                 }
                 else {
                	 itsGeolocationListener.onLocationFound(locationNet);
                 }
             }
             else {
            	 // Only one (or none) available, report that one (or null)
                 if(locationGps!=null){
                	 itsGeolocationListener.onLocationFound(locationGps);
                 } 
                 else {
                	 if(locationNet!=null){
                		 itsGeolocationListener.onLocationFound(locationNet);
                	 }
                	 else {
                		 itsGeolocationListener.onLocationFound(null);
                	 }
                 } 
             }

             return;
        }
    }
    
    // Callback for Geolocation Listener
    public abstract static class GeolocationListener implements IGeolocationListener {
    	@Override
    	public abstract void onLocationFound(Location location);
    }
}
