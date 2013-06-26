package gov.howardcountymd.utils.http;

public abstract class HttpResult implements IHttpResult {

	@Override
	public abstract void gotOutputStr(String outputStr);

}
