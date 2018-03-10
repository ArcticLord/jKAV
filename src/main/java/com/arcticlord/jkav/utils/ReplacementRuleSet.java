package com.arcticlord.jkav.utils;

public class ReplacementRuleSet{
	
	public static final int TAG_COMPANY 	= 0;
	public static final int TAG_NAME	 	= 1;
	public static final int TAG_ADDRESS1 	= 2;
	public static final int TAG_ADDRESS2 	= 3;
	public static final int TAG_PHONE 		= 4;
	public static final int TAG_FAX 		= 5;
	public static final int TAG_EMAIL 		= 6;
	public static final int ANZ_TAGS 		= 7;
	
	private ReplacementRule[] rules;
	
	public class ReplacementRule{
		private String tag;
		private String replaceText;
		
		public ReplacementRule(String t){
			tag = t;
			replaceText = "";
		}
		public String getTag(){
			return tag;
		}
		public String getReplaceText(){
			return replaceText;
		}
	}
	
	public ReplacementRuleSet(){
		rules = new ReplacementRule[ANZ_TAGS];
		rules[TAG_COMPANY]	= new ReplacementRule("&lt;company>");
		rules[TAG_NAME] 	= new ReplacementRule("&lt;name>");
		rules[TAG_ADDRESS1] = new ReplacementRule("&lt;address1>");
		rules[TAG_ADDRESS2] = new ReplacementRule("&lt;address2>");
		rules[TAG_PHONE] = new ReplacementRule("&lt;phone>");
		rules[TAG_FAX] = new ReplacementRule("&lt;fax>");
		rules[TAG_EMAIL] = new ReplacementRule("&lt;email>");
	}
	
	public void setReplacementRule(int tagId, String repText){
		if( tagId > -1 && tagId < ANZ_TAGS){
			rules[tagId].replaceText = repText;
		}
	}
	public ReplacementRule getReplacementRule(int id){
		if( id > -1 && id < ANZ_TAGS){
			return rules[id];
		}
		return null;
	}
}
