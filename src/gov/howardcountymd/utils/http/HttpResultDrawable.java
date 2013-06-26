package gov.howardcountymd.utils.http;

import android.graphics.drawable.Drawable;

public abstract class HttpResultDrawable implements IHttpResultDrawable {

	@Override
	public abstract void gotResultDrawable(Drawable resultDrawable);

}
