package com.arcticlord.jkav.utils;


public class HTMLGenerator{
	
	public static final String BLACK = "#000000";
	public static final String RED = "#FF0000";
	public static final String GREEN = "#00FF00";
	public static final String BLUE = "#0000FF";
	
	public static String base(String code){
		return "<HTML><BODY>" + code + "</BODY></HTML>";
	}
	
	public static String headline(String code, int size){
		return "<H" + size + ">" + code + "</H" + size + ">";
	}
	
	public static String table(int rows, int cols, String... cont){
		if(cont == null ) return "no Parameters";
		if(cont.length != (rows * cols)) return "wrong amount of Parameters";
		String ret = "";
		ret = "<TABLE border=\"0\" align=\"left\">";
		for(int r = 0; r < rows; r++){
			ret += "<TR>";
			for(int c = 0; c < cols; c++){
				ret += "<TD>";
				ret += cont[(r * cols) + c];
				ret += "</TD>";
			}
			ret += "</TR>";
		}
		ret += "</TABLE>";
		return ret;
	}
	public static String emailLink(String code){
		return "<a href=\"mailto:kontakt@seite.cc\">" + code + "</a>";
	}
	
	public static String text(String code, int size){
		return text(code, size, BLACK);
	}
	public static String text(String code, int size, String color){
		return "<font size=\""+ size +"\" color=\""+color+"\">" + code + "</font>";
	}
	public static String br(){
		return "<br>";
	}
}