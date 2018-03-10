package com.arcticlord.jkav.data;
import java.util.ArrayList;
import java.util.List;

import com.arcticlord.jkav.db.MySQLEnum;



public class CustomerData{
		
	public int cid;
	public String company;
	public int title;
	public String forename;
	public String name;
	public String other;
	public List<ContactData> contact;
	public List<AddressData> address;
	
	public static MySQLEnum titleSelection = new MySQLEnum();
	
	public CustomerData(){
		cid = -1;
		company = "";
		title = 1;
		forename = "";
		name = "";
		other = "";
		contact = new ArrayList<ContactData>();
		address = new ArrayList<AddressData>();
	}
	
	@Override
	public String toString(){
		String ret = "";
		ret +=	"CUSTOMER\n" +
				"cid: " + cid + "\n" +
				"Firma: " + company + "\n" +
				"Titel: " + titleSelection.getNameOf(title) + "\n" +
				"Vorname: " + forename + "\n" +
				"Nachname: " + name + "\n" +
				"Bemerkung: " + other + "\n";
		for(AddressData a : address) ret += a.toString();
		for(ContactData c : contact) ret += c.toString();
		return ret;
				
	}
	/* deprecated - for transition time only */
	public String getCustomerFolderNameOld(){
		if(!company.isEmpty())
			return company;
		if(!forename.isEmpty() && !name.isEmpty())
			return name + ", " + forename;
		if(!name.isEmpty())
			return name;
		return Integer.toString(cid);
	}
	
	public String getCustomerFolderName(){
		String strCid = Integer.toString(cid);
		if(!company.isEmpty())
			return company + " (" + strCid + ")";
		if(!forename.isEmpty() && !name.isEmpty())
			return name + ", " + forename + " (" + strCid + ")";
		if(!name.isEmpty())
			return name + " (" + strCid + ")";
		return "(" + strCid + ")";
	}
	/*
	public Iterable<ContactData> getContacts(final int type){
		return new Iterable<ContactData>(){
			@Override
			public Iterator<ContactData> iterator() {
				return new Iterator<ContactData>(){
					Iterator<ContactData> orig = contact.iterator();
					@Override
					public boolean hasNext() {
						return orig.hasNext();
					}

					@Override
					public ContactData next() {
						ContactData c = orig.next();
						if(c.type == type)
							return c;
						return null;
					}

					@Override
					public void remove() {
						//do nothing
					}};
			}};
	}*/
	
	public static CustomerData createDummy(int i){
		CustomerData d = new CustomerData();
			d.company = "Fake"+i+" GmbH & Co KG";
			d.title = titleSelection.getEnumIdOf("Herr");
			d.forename = "Vorname"+i;
			d.name = "Name"+i;
			d.other = "bla bla bla "+i;
			d.address.add(AddressData.createDummy(i));
			d.contact.add(ContactData.createDummyEmail(i));
			d.contact.add(ContactData.createDummyPhone(i));
			d.contact.add(ContactData.createDummyFax(i));
		return d;
	}
}