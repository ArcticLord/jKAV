package com.arcticlord.jkav.data;
public class ContactData{
	public static final int TYPE_PHONE =	1;
	public static final int TYPE_FAX =		2;
	public static final int TYPE_EMAIL = 	3;
	
	public int iid;
	public int type;
	public String data;
	public boolean valid;
	
	public ContactData(){
		iid = -1;
		type = 1;
		data = "";
		valid = true;
	}
	
	
	public String toString(){
		return	"CONTACT:\n" +
				"iid: " + iid + "\n" +
				"Typ: " + typeToString() + "\n" +
				"Inhalt: " + data + "\n";
	}
	
	// TODO: Why not simple toString ??
	public String typeToString(){
		String ret = "";
		switch(type){
			case TYPE_PHONE:
				ret = "phone number";
				break;
			case TYPE_FAX:
				ret = "fax number";
				break;
			case TYPE_EMAIL:
				ret = "e-mail address";
				break;
			default : break;
		}
		return ret;
	}
	
	public void checkValid(){
		if(data.isEmpty()) valid = false;
	}
	
	public static ContactData createDummyPhone(int i){
		ContactData d = new ContactData();
			d.data = i+""+i+""+i+""+i+""+i+""+i+""+i;
			d.type = TYPE_PHONE;
		return d;
	}
	public static ContactData createDummyFax(int i){
		ContactData d = new ContactData();
			d.data = "00"+i+""+i+""+i+""+i+""+i;
			d.type = TYPE_FAX;
		return d;
	}

	public static ContactData createDummyEmail(int i){
		ContactData d = new ContactData();
			d.data = i+""+i+"@"+i+""+i+".de";
			d.type = TYPE_EMAIL;
		return d;
	}
}