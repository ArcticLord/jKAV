package com.arcticlord.jkav.data;
public class AddressData{
	
	public int aid;
	public String street;
	public String housenumber;
	public String plz;
	public String city;
	public boolean valid;		/* if valid=false -> entry will deleted */
	
	public AddressData(){
		aid = -1;
		street = "";
		housenumber = "";
		plz = "";
		city = "";
		valid = true;
	}
	
	@Override
	public String toString(){
		return	"ADDRESSE:\n" + 
				"aid: " + aid + "\n" +
				"Straße: " + street + "\n" +
				"Hausnummer: " + housenumber + "\n" +
				"Postleitzahl: " + plz + "\n" +
				"Stadt: " + city + "\n";
	}
	public void checkValid(){
		if(	street.isEmpty() && housenumber.isEmpty() &&
			plz.isEmpty() && city.isEmpty())
				valid = false;
	}
	
	public static AddressData createDummy(int i){
		AddressData d = new AddressData();
			d.street = "Fakestreet " + i;
			d.housenumber = "" + i;
			d.plz = "" + i + "" + i + "" + i + "" + i + "" + i;
			d.city = "Fakecity" + i;
		return d;
	}
}