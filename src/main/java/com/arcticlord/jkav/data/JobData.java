package com.arcticlord.jkav.data;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.arcticlord.jkav.db.MySQLEnum;



public class JobData{
	public int jid;
	public int customerId;
	public String title;
	public String describtion;
	public double price;
	public int state;
	public Date beginDate;
	public Date endDate;
	
	public static final int DATE_STYLE_SQL =		1;
	public static final int DATE_STYLE_NORMAL = 	2;
	
	public static MySQLEnum jobStates = new MySQLEnum();
	
	public JobData(){
		jid = -1;
		customerId = -1;
		title = "";
		describtion = "";
		price = 0.0f;
		state = 1;
		beginDate = new Date(0);
		endDate = new Date(0);
	}
	
	public String toString(){
		return	"JOB\n" +
				"jid: " + jid + "\n" +
				"Titel: " + title + "\n" +
				"Auftraggeber: " + customerId + "\n" +
				"Begin: " + convertDate(beginDate, DATE_STYLE_NORMAL) + "\n" +
				"Ende: " + convertDate(endDate, DATE_STYLE_NORMAL) + "\n" +
				"Status: " + jobStates.getNameOf(state) + "\n" +
				"Preis: " + price + "\n" +
				"Beschreibung: " + describtion;
	}
	
	public static String convertDate(Date date, int style){
		String format = "";
		String str = "";
		switch(style){
			case DATE_STYLE_SQL:
				format = "yyyy.MM.dd";
				break;
			case DATE_STYLE_NORMAL:
				format = "EEE, d. MMM yyyy";
				break;
			default : return str;
		}
		str = (new SimpleDateFormat( format )).format(date);
		return str;
	}
	
	public static JobData createDummy(int i, int cID){
		JobData j = new JobData();
			j.title = "Job" + i + " für " + cID;
			j.price = i * cID;
			j.beginDate = new Date();
			j.endDate = new Date();
			j.describtion = i + "" + i + "" + i;
			j.customerId = cID;
			j.state = jobStates.getEnumIdOf("pending");
			
		return j;
	}
}