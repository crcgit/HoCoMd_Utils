package gov.howardcountymd.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public class HttpAccess implements IHttpAccess {

	private IHttpResult itsHttpResult;
	private IHttpResultDrawable itsHttpResultDrawable;
	private IHttpResultBitmap itsHttpResultBitmap;
	private URL itsUrl;
	private int itsReqdWidth;
	private int itsReqdHeight;
	
	public void requestStringForHttpGet(String addr, List<NameValuePair> params, IHttpResult httpResult ) {
		// Store where thread can get to them
		itsUrl = urlForAddressAndParams(addr, params);
		itsHttpResult = httpResult;
		
		// Run network query on another thread (required)
		Thread thread = new Thread() {
		    @Override
		    public void run() {
		        try {
		        	// Do Http Get, return result
		        	String outputStr = stringForHttpGetAtUrl( itsUrl );
		        	itsHttpResult.gotOutputStr(outputStr);
		        } 
		        catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		};
		thread.start();
	}
	
	public void requestBitmapForHttpGet(String addr, int reqdWidth, int reqdHeight, IHttpResultBitmap httpResultBitmap ) {
		// Store where thread can get to them
		itsUrl = urlForAddressAndParams(addr, null);
		itsHttpResultBitmap = httpResultBitmap;
		itsReqdWidth = reqdWidth;
		itsReqdHeight = reqdHeight;
		
		// Run network query on another thread (required)
		Thread thread = new Thread() {
		    @Override
		    public void run() {
		        try {
		        	// Do Http Get, return result
		        	Bitmap bitmap = bitmapForHttpGetAtUrl( itsUrl, itsReqdWidth, itsReqdHeight );
		        	itsHttpResultBitmap.gotResultBitmap(bitmap);
		        } 
		        catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		};
		thread.start();
	}
	
	public void requestDrawableForHttpGet(String addr, List<NameValuePair> params, IHttpResultDrawable httpResultDrawable ) {
		// Store where thread can get to them
		itsUrl = urlForAddressAndParams(addr, params);
		itsHttpResultDrawable = httpResultDrawable;
		
		// Run network query on another thread (required)
		Thread thread = new Thread() {
		    @Override
		    public void run() {
		        try {
		        	// Do Http Get, return result
		        	Drawable drawable = drawableForHttpGetAtUrl( itsUrl );
		        	itsHttpResultDrawable.gotResultDrawable(drawable);
		        } 
		        catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		};
		thread.start();
	}
	
	// Private
	
	private Bitmap bitmapForHttpGetAtUrl(URL url, int reqWidth, int reqHeight) {
	    // First decode with inJustDecodeBounds=true to check dimensions
        InputStream inputStream = inputStreamForHttpGetAtUrl(url);
	    final BitmapFactory.Options bfOptions = new BitmapFactory.Options();
	    bfOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(inputStream, null, bfOptions);
	    try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // Calculate inSampleSize
	    bfOptions.inSampleSize = calculateInSampleSize(bfOptions, reqWidth, reqHeight);
	    
	    // Decode bitmap with inSampleSize set. Have to restart input stream (it cannot be "rewound")
	    inputStream = inputStreamForHttpGetAtUrl(url);
	    bfOptions.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, bfOptions);
		return bitmap;
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    if (height > reqHeight || width > reqWidth) {
	
	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	
	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	    return inSampleSize;
	}
	
	private Drawable drawableForHttpGetAtUrl(URL url) {
		Drawable drawable = null;
		// Get input stream from url
        //InputStream inputStream = inputStreamForHttpGetAtUrl(url);
        // OR?
        InputStream inputStream = null;
		try {
			inputStream = (InputStream) url.getContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Get drawable from input stream
        drawable = Drawable.createFromStream(inputStream, null);
		return drawable;
	}
	
	private String stringForHttpGetAtUrl(URL url) {
        //Get string for result and parse
        InputStream inputStream = inputStreamForHttpGetAtUrl(url);
		
		// Read input stream into a string. 
		String inputStreamStr = "";
		try { 
			InputStreamReader streamReader = new InputStreamReader(inputStream,"iso-8859-1");
			BufferedReader reader = new BufferedReader(streamReader, 8); 
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n"); 
			}
			inputStream.close(); 
			inputStreamStr=sb.toString(); 
		} 
		catch(Exception ex) { 
			System.out.println("Error reading server response: " + ex.toString()); 
		}
        return inputStreamStr;
	}
	
	private InputStream inputStreamForHttpGetAtUrl( URL url ) {
		InputStream inputStream = null;

		try {				
			// Define http connection, set up GET before connection is established
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		    httpCon.setRequestMethod("GET");			// default is GET, but being explicit in code
		    httpCon.setDoInput( true );
			httpCon.setUseCaches( false );
			httpCon.setAllowUserInteraction( false );
			httpCon.setReadTimeout( 10000 /* milliseconds */ );
		    httpCon.setConnectTimeout( 15000 /* milliseconds */ );
		    //httpCon.addRequestProperty("Authorization", "Basic YWRtaW4fYFgjkl5463"); // for reference
				
			// Any input pull will establish connection
	        int response = httpCon.getResponseCode();
	        System.out.println("The response code is: " + response);
			
	        //Get string for result and parse
	        inputStream = httpCon.getInputStream();
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
	}
	
	private URL urlForAddressAndParams(String addr, List<NameValuePair> params) {
		// Convert to parameter string for URL
		String paramStr = null;
		if( params != null ) {
			paramStr = "?" + URLEncodedUtils.format(params, "utf-8");
		}
		else {
			paramStr = "";
		}
		
		URL url = null;
		try {
			String urlStr = addr + paramStr;
			System.out.println("HttpGet string length: " + urlStr.length());
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
}
