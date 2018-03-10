package com.arcticlord.jkav.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;


public class DocGenerator{
	
	
	private static void paragraphReplacer(	XWPFParagraph paragraph,
											ReplacementRuleSet.ReplacementRule rule){
		for(XWPFRun run : paragraph.getRuns()){
			
			CTR ctr  = run.getCTR();
			
			XmlToken xmlToken = null;
		    try{
		    	String code = ctr.toString().replace(	rule.getTag(),
		    											rule.getReplaceText());
		        xmlToken = XmlToken.Factory.parse(code);
		    } catch(XmlException xe) {
		    	
		    }
		    ctr.set(xmlToken);
		}
	}
	
	private static void docReplace(	XWPFDocument doc,
									ReplacementRuleSet.ReplacementRule rule){
		// Header
		for(XWPFHeader h :doc.getHeaderList()){
			for(XWPFParagraph paragraph: h.getListParagraph()){
				paragraphReplacer(paragraph, rule);
			}
		}
		
		// Body
		for(IBodyElement b : doc.getBodyElements()){
			if(b.getElementType() == BodyElementType.PARAGRAPH){
				XWPFParagraph ppp = ((XWPFParagraph)b);
				paragraphReplacer(ppp, rule);
			}
			else if(b.getElementType() == BodyElementType.TABLE){
				XWPFTable t = ((XWPFTable)b);
				for(XWPFTableRow r :t.getRows()){
					for(XWPFTableCell c : r.getTableCells()){
						for(XWPFParagraph p : c.getParagraphs()){
							paragraphReplacer(p, rule);
						}
					}
				}				
			}
		}
		// Footer
		for(XWPFFooter f :doc.getFooterList()){
			for(XWPFParagraph paragraph: f.getListParagraph()){
				paragraphReplacer(paragraph, rule);
			}
		}
	}

	public static boolean generateDocX(	String draftFile,
										String targetFile,
										ReplacementRuleSet rules){

		try{
			XWPFDocument document = new XWPFDocument(new FileInputStream(draftFile));

			for(int i = 0; i < ReplacementRuleSet.ANZ_TAGS; i++)
				docReplace(	document, 
							rules.getReplacementRule(i));
			
			document.write(new FileOutputStream(new File(targetFile)));
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}