package gov.howardcountymd.utils.gis;

import com.google.android.gms.maps.model.LatLng;

public abstract class PlaceDecorator implements IPlace {

	// Place being decorated
	
	public IPlace itsPlace;		// Left public so users can drill down to places inside
	
	// Constructor
	
	public PlaceDecorator() {
		itsPlace = new Place();
	}
	
	// Implement Place methods
	
	@Override
	public void setName(String name) {
		itsPlace.setName(name);
	}

	@Override
	public void setLatLng(LatLng latLng) {
		itsPlace.setLatLng(latLng);
	}

	@Override
	public void setType(String type) {
		itsPlace.setType(type);
	}

	@Override
	public void setTable(String table) {
		itsPlace.setTable(table);
	}

	@Override
	public void setAddress1(String address1) {
		itsPlace.setAddress1(address1);
	}

	@Override
	public void setAddress2(String address2) {
		itsPlace.setAddress2(address2);
	}

	@Override
	public String getName() {
		return itsPlace.getName();
	}

	@Override
	public String getType() {
		return itsPlace.getType();
	}
	
	@Override
	public LatLng getLatLng() {
		return itsPlace.getLatLng();
	}

	@Override
	public String getTable() {
		return itsPlace.getTable();
	}

	@Override
	public String getAddress1() {
		return itsPlace.getAddress1();
	}

	@Override
	public String getAddress2() {
		return itsPlace.getAddress2();
	}

}
