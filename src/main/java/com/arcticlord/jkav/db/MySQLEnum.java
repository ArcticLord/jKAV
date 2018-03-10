package com.arcticlord.jkav.db;
import java.util.ArrayList;



public class MySQLEnum extends ArrayList<MySQLEnum.EnumEntry>{

	private static final long serialVersionUID = -6758712050282484351L;

	public class EnumEntry{
		public int id;
		public String name;
		
		public EnumEntry(int i, String n){
			id = i;
			name = n;
		}
		
		public String toString(){
			return	"ENUM ENTRY:\n" +
					"id: " + id + "\n" +
					"Name: " + name + "\n";
		}
	}
	
	public void add(int id, String name){
		add(new EnumEntry(id, name));
	}

	/**
	 * Lookup for EnumId and returns the Array Id
	 * @param searchEnumId	The MySQL EnumId to look for
	 * @return				Either the Array Id or -1 if not found
	 */
	public int getArrIdOf(int searchEnumId){
		int count = 0;
		for(EnumEntry e : this){
			if(e.id == searchEnumId){
				return count;
			}
			count++;
		}
		return -1;
	}
	
	/**
	 * Lookup for a MySQL Enum Array Id with given Enum Name 
	 * @param searchEnumName	The MySQL EnumName to look for
	 * @return					the found Enum Array Id
	 * 							or -1 if not found
	 */
	public int getArrIdOf(String searchEnumName){
		return getIdOf(searchEnumName,true);
	}
	
	/**
	 * Lookup for the EnumId of an ArrayId
	 * @param searchArrId	The Array Id to look for
	 * @return				The MySQL Enum Id or -1 if not found
	 */
	public int getEnumIdOf(int searchArrId){
		if(searchArrId >= 0 && searchArrId < this.size())
			return get(searchArrId).id;
		return -1;
	}
	
	/**
	 * Lookup for EnumId if EnumState Name
	 * @param searchEnumName	The Enum State Name to look for
	 * @return					The MySQL Enum Id or -1 if not found
	 */
	public int getEnumIdOf(String searchEnumName){
		return getIdOf(searchEnumName,false);
	}
	
	/**
	 * Lookup for a MySQL Enum state Name with given Id 
	 * @param searchEnumId	MySQL Enum Id to look for
	 * @return				the found Enum State name
	 * 						or "" if not found
	 */
	public String getNameOf(int searchEnumId){
		for(EnumEntry e : this){
			if(e.id == searchEnumId)
				return e.name;
		}
		return "";		
	}
	
	/**
	 * Lookup for for searchName and returns either the
	 * MySQL Enum Id or the Array Id
	 * @param searchName	name of Enum state to look for
	 * @param arrayId		true - lookup for array Id
	 * 						false - lookup for Enum Id
	 * @return				MySQL or Enum ID of found search Name
	 * 						or -1 if not found 
	 */
	private int getIdOf(String searchName, boolean arrayId){
		int count = 0;
		for(EnumEntry e : this){
			if(e.name.compareTo(searchName)==0){
				if(arrayId) return count;
				else return e.id;
			}
			count++;
		}
		return -1;
	}
	
	
	/**
	 * Generates an String Array consisting all
	 * the Enum Names in ArrayList Iterator order 
	 * @return	the created String Array
	 */
	public String[] getStringArray(){
		String[] ret = new String[this.size()];
		int i = 0;
		for(EnumEntry e : this){
			ret[i++] = e.name;
		}
		return ret;
	}
	
	/**
	 * Generates an String output of all Enum States
	 */
	public String toString(){
		String ret = "ENUM DATA LIST:\n";
		for(EnumEntry e : this){
			ret += e;
		}
		return ret;
	}
}