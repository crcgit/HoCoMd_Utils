package gov.howardcountymd.utils.gis;

import com.google.android.gms.maps.model.LatLng;

public interface IPlace {

	// Setters
	
	public void setName(String name);
	
	public void setLatLng(LatLng latLng);
	
	public void setType(String type);
	
	public void setTable(String table);
	
	public void setAddress1(String address1);
	
	public void setAddress2(String address2);
	
	// Getters
	
	public String getName();
	
	public String getType();
	
	public LatLng getLatLng();
	
	public String getTable();
	
	public String getAddress1();
	
	public String getAddress2();
}
