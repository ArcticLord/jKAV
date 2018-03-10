package com.arcticlord.jkav.utils;
import java.util.Map;

/**
 * This class manages the settings for the jKAV application.
 * These settings are read and stored in Windows-Registry under
 * the key HKEY_CURRENT_USER/Software/jKAV.
 *
 */
public class Settings{
	
	public String mySQLDir;
	public String customerDir;
	public String username;
	public String password;
	
	/**
	 * Constructor inits the values.
	 */
	public Settings(){
		mySQLDir = "";
		customerDir = "";
		username = "";
		password = "";
	}
	
	/**
	 * Reads the settings from registry.
	 */
	public void loadSettings(){
		Map<String,String> vals = null;
		// read all values from jKAV Registry Key
		try{
			vals = WinRegistry.readStringValues(
					WinRegistry.HKEY_CURRENT_USER,
					"Software\\jKAV");
		} catch (Exception e){
			e.printStackTrace();
		}
		// if Registry key could not be found
		// or an error occured
		if (vals == null){
			// set the default values
			mySQLDir = searchMySQLDir();
			customerDir = "";
			username = "root";
		} else {
			// otherwies set the results
			mySQLDir = vals.get("mySQLDir");
			customerDir = vals.get("customerDir");
			username = vals.get("username");
		}
	}
	
	/**
	 * Stores the settings to registry
	 */
	public void storeSettings(){
		// write settings to registry
		try {
			// create jKAV Registry Key
			WinRegistry.createKey(
					WinRegistry.HKEY_CURRENT_USER,
					"Software\\jKAV");
			// store MySQL directory
			WinRegistry.writeStringValue(
					WinRegistry.HKEY_CURRENT_USER,
					"Software\\jKAV",
					"mySQLDir",
					mySQLDir);
			// store customer directory
			WinRegistry.writeStringValue(
					WinRegistry.HKEY_CURRENT_USER,
					"Software\\jKAV",
					"customerDir",
					customerDir);
			// store database username
			WinRegistry.writeStringValue(
					WinRegistry.HKEY_CURRENT_USER,
					"Software\\jKAV",
					"username",
					username);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tries to get the MySQL bin Path from Registry.
	 * @return	The MySQL bin Path
	 */
	private String searchMySQLDir(){
		String value = "";
		try {
			value = WinRegistry.readString(
				    	WinRegistry.HKEY_LOCAL_MACHINE,
				    	"SOFTWARE\\MySQL AB\\MySQL Server 5.5",
				   		"Location") + "\\bin";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
} 
