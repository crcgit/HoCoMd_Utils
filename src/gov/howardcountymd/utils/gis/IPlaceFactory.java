package gov.howardcountymd.utils.gis;

import org.json.JSONObject;

public interface IPlaceFactory {

	IPlace createPlace();
	
	IPlace createPlace(JSONObject jsonObj);
}
