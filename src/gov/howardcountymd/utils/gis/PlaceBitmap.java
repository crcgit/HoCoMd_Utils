package gov.howardcountymd.utils.gis;

import android.graphics.Bitmap;

import gov.howardcountymd.utils.http.HttpAccess;
import gov.howardcountymd.utils.http.HttpResultBitmap;
import gov.howardcountymd.utils.http.IHttpAccess;
import gov.howardcountymd.utils.http.IHttpResultBitmap;

public class PlaceBitmap extends PlaceDecorator {
	
	// Contains
	
	private IHttpAccess itsHttpAccess;
	protected Bitmap itsBitmap;
	private PlaceBitmapResult itsPlaceBitmapResult;
	protected String itsUrlStr;
	
	// Constructors
	
	public PlaceBitmap(String urlStr) {
		itsUrlStr = urlStr;
		itsHttpAccess = new HttpAccess();
	}
	
	public PlaceBitmap(String urlStr, IPlace place) {
		itsUrlStr = urlStr;
		itsPlace = place;
		itsHttpAccess = new HttpAccess();
	}
	
	public PlaceBitmap(String urlStr, IPlace place, IHttpAccess httpAccess) {
		itsUrlStr = urlStr;
		itsPlace = place;
		itsHttpAccess = httpAccess;
	}
	
	// Public 

	public void requestBitmap(int reqdWidth, int reqdHeight, PlaceBitmapResult placeBitmapResult) {
		// Save where callback can see it
		itsPlaceBitmapResult = placeBitmapResult;
		// Define and instantiate callback for HttpAccess
		IHttpResultBitmap httpResultBitmap = new HttpResultBitmap() {
			@Override
			public void gotResultBitmap(Bitmap resultBitmap) {
				itsBitmap = resultBitmap;
				if( itsPlaceBitmapResult != null ) {
					itsPlaceBitmapResult.gotBitmap(resultBitmap);
				}
				// Release this bitmap to garbage collection at end of cycle
				itsBitmap = null;
			}
		};
		// Request string of http get
		String bitmapUrlStr = itsUrlStr + itsPlace.getName() + ".png";
		itsHttpAccess.requestBitmapForHttpGet(bitmapUrlStr, reqdWidth,  reqdHeight, httpResultBitmap);
	}
	
	// Setters
	
	public void setBitmap(Bitmap bitmap) {
		itsBitmap = bitmap;
	}
	
	public void setBitmapUrlStr(String bitmapUrlStr) {
		itsUrlStr = bitmapUrlStr;
	}
	
	// Getters
	
	public Bitmap getBitmap() {
		return itsBitmap;
	}
	
	public String getBitmapUrlStr() {
		return itsUrlStr;
	}
	
	// Users get bitmap by call thing this function with a callback
	
	public abstract static class PlaceBitmapResult {
		public abstract void gotBitmap(Bitmap bitmap);
	}

	// Static
	
    public static PlaceBitmap findPlaceBitmap(IPlace place) {
    	// Drill down through decorators and return first PlaceBitmap
    	PlaceBitmap placeBitmap = null;
    	if( !(place instanceof Place) ) {
        	while( (!(place instanceof PlaceBitmap)) && (!(place instanceof Place)) ) {
        		place = ((PlaceDecorator) place).itsPlace;
        	}
        	if( place instanceof PlaceBitmap ) {
        		placeBitmap = (PlaceBitmap) place;
        	}
    	}
    	return placeBitmap;
    }

}
