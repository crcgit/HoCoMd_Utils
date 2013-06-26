package gov.howardcountymd.utils.http;

import android.graphics.Bitmap;

public abstract class HttpResultBitmap implements IHttpResultBitmap {

	@Override
	public abstract void gotResultBitmap(Bitmap resultBitmap);

}
