package gov.howardcountymd.utils.http;

import java.util.List;

import org.apache.http.NameValuePair;

public interface IHttpAccess {

	void requestBitmapForHttpGet(String addr, int reqdWidth, int reqdHeight, IHttpResultBitmap httpResultBitmap );
	
	void requestStringForHttpGet(String addr, List<NameValuePair> params, IHttpResult httpResult );

}
