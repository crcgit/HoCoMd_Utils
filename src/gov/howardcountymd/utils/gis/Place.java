package gov.howardcountymd.utils.gis;

import com.google.android.gms.maps.model.LatLng;

public class Place implements IPlace {
	
	// Data
	
	private String itsType;
	private String itsTable;
	private String itsAddress1;
	private String itsAddress2;
	private String itsName;
	private LatLng itsLatLng;
	
	// Constructors
	
	public Place() {
		itsName = "";
		itsLatLng = new LatLng(0.0,0.0);
		itsType = "";
		itsTable = "";
		itsAddress1 = "";
		itsAddress2 = "";
	}
	
	public Place(IPlace np) {
		itsName = np.getName();
		itsLatLng = np.getLatLng();
		itsType = np.getType();
		itsTable = np.getTable();
		itsAddress1 = np.getAddress1();
		itsAddress2 = np.getAddress2();
	}
	
	// Setters
	
	@Override
	public void setType(String type) {
		itsType = type;
	}

	@Override
	public void setTable(String table) {
		itsTable = table;
	}

	@Override
	public void setAddress1(String address1) {
		itsAddress1 = address1;
	}

	@Override
	public void setAddress2(String address2) {
		itsAddress2 = address2;
	}
	
	@Override
	public void setName(String name) {
		itsName = name;
	}
	
	@Override
	public void setLatLng(LatLng latLng) {
		itsLatLng = latLng;
	}
	
	// Getters

	@Override
	public String getType() {
		return itsType;
	}

	@Override
	public String getTable() {
		return itsTable;
	}

	@Override
	public String getAddress1() {
		return itsAddress1;
	}

	@Override
	public String getAddress2() {
		return itsAddress2;
	}

	@Override
	public LatLng getLatLng() {
		return itsLatLng;
	}
	
	@Override
	public String getName() {
		return itsName;
	}
	
//	// TBR... shouldn't need this... decorators point to this
//    public static Place findPlace(IPlace place) {
//    	// Drill down through decorators and return first PlaceCategory
//    	if( !(place instanceof Place) ) {
//        	while( !(place instanceof Place) ) {
//        		place = ((PlaceDecorator) place).itsPlace;
//        	}
//    	}
//    	return (Place) place;
//    }
}
