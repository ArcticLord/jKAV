package com.arcticlord.jkav.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.data.AddressData;
import com.arcticlord.jkav.data.ContactData;
import com.arcticlord.jkav.data.CustomerData;
import com.arcticlord.jkav.utils.DocGenerator;
import com.arcticlord.jkav.utils.ReplacementRuleSet;

public class FrameWord extends Frame {

	private static final long serialVersionUID = -8304149540527354507L;

	public FrameWord(JKAV c) {
		super(c);
	}
	
	private JComboBox<String> cmbAddress1;
	private JComboBox<String> cmbPhone;
	private JComboBox<String> cmbFax;
	private JComboBox<String> cmbEmail;
	
	private JButton btnOpenDraft;
	
	private JTextField edtDraftDoc;
	private JTextField edtCompany;
	private JTextField edtName;
	private JTextField edtAddress2;
	
	private JLabel lblCompanyTag;
	private JLabel lblNameTag;
	private JLabel lblAddress1Tag;
	private JLabel lblAddress2Tag;
	private JLabel lblPhoneTag;
	private JLabel lblFaxTag;
	private JLabel lblEmailTag;
	
	private JFileChooser	fileChooser;
	
	private CustomerData customerData = null;
	
	private int activeAddress;
	
	@Override
	public void init() {	
		setMaxSize(400, -1);
		setButtonList(FrameFoot.BTN_BACK | FrameFoot.BTN_WORD);
		setScrollable(true);
		
		lblCompanyTag = createLabel("<company>");
		lblNameTag = createLabel("<name>");
		lblAddress1Tag = createLabel("<address1>");
		lblAddress2Tag = createLabel("<address2>");
		lblPhoneTag = createLabel("<phone>");
		lblFaxTag = createLabel("<fax>");
		lblEmailTag = createLabel("<email>");
		
		edtDraftDoc = createLabelTextField();
		edtCompany = createLabelTextField();
		edtName = createLabelTextField();
		edtAddress2 = createLabelTextField();
		
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter(){

			/*
		     * Get the extension of a file.
		     */
		    public String getExtension(File f) {
		        String ext = null;
		        String s = f.getName();
		        int i = s.lastIndexOf('.');

		        if (i > 0 &&  i < s.length() - 1) {
		            ext = s.substring(i+1).toLowerCase();
		        }
		        return ext;
		    }
		    
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
			    String extension = getExtension(f);
				    if (extension != null) {
				    	if (extension.equals("docx")) 
				    			return true;
						else
						    return false;
				    }
				    return false;
			}

			@Override
			public String getDescription() {
				return "Microsoft Word 2003 Dokumente (*.docx)";
			}});
		
		btnOpenDraft = new JButton("Öffnen");
		btnOpenDraft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				LoadDialog();
			}});
		
		cmbAddress1 = createComboBox();
		cmbPhone = createComboBox();
		cmbFax = createComboBox();
		cmbEmail = createComboBox();
		
		
		int row = 0;
		addComponent( createLabel("Word Vorlage"), 	0, row, 2, 1, 1, 0 );
		row++;
		addComponent( createGap(), 					0, row, 2, 1, 1, 0 );
		row++;
		addComponent( btnOpenDraft, 				0, row, 1, 1, 0, 0 );
		addComponent( edtDraftDoc,	 				1, row, 1, 1, 1, 0 );
		row++;
		addComponent( createGap(), 					0, row, 2, 1, 1, 0 );
		row++;
		addComponent( createGap(), 					0, row, 2, 1, 1, 0 );
		row++;
		addComponent( createGap(), 					0, row, 2, 1, 1, 0 );
		row++;
		addComponent( createLabel("WORD TAG"),	0, row, 1, 1, 0, 0 );
		addComponent( createLabel("INHALT"),	 	1, row, 1, 1, 1, 0 );
		row++;
		addComponent( createGap(), 					0, row, 2, 1, 1, 0 );
		row++;
		addComponent( lblCompanyTag,		 		0, row, 1, 1, 0, 0 );
		addComponent( edtCompany, 					1, row, 1, 1, 1, 0 );
		row++;
		addComponent( lblNameTag,					0, row, 1, 1, 0, 0 );
		addComponent( edtName,						1, row, 1, 1, 1, 0 );
		row++;
		addComponent( createGap(), 					0, row, 2, 1, 1, 0 );
		row++;
		addComponent( lblAddress1Tag, 				0, row, 1, 1, 0, 0 );
		addComponent( cmbAddress1,					1, row, 1, 1, 1, 0 );
		row++;
		addComponent( lblAddress2Tag,				0, row, 1, 1, 0, 0 );
		addComponent( edtAddress2,					1, row, 1, 1, 1, 0 );
		row+=4;
		addComponent( createGap(), 					0, row, 2, 1, 1, 0 );
		row++;
		addComponent( lblPhoneTag,	 				0, row, 1, 1, 0, 0 );
		addComponent( cmbPhone,						1, row, 1, 1, 1, 0 );
		row++;
		addComponent( lblFaxTag,	 	0, row, 1, 1, 0, 0 );
		addComponent( cmbFax,			1, row, 1, 1, 1, 0 );
		row++;
		addComponent( lblEmailTag,	 	0, row, 1, 1, 0, 0 );
		addComponent( cmbEmail,			1, row, 1, 1, 1, 0 );
	}
	
	
	public void clearContent(){
		edtCompany.setText("");
		edtName.setText("");
		cmbAddress1.removeAllItems();
		edtAddress2.setText("");
		cmbPhone.removeAllItems();
		cmbFax.removeAllItems();
		cmbEmail.removeAllItems();
		activeAddress = -1;
	}
	
	private void setContent(){
		clearContent();
		if(customerData != null){
			edtCompany.setText(customerData.company);
			edtName.setText(CustomerData.titleSelection.getNameOf(customerData.title) +
					" " + customerData.forename + " " + customerData.name);
			
			for(AddressData adr : customerData.address){
				cmbAddress1.addItem(adr.street + " " + adr.housenumber);
			}
			
			if(customerData.address.size() > 0){
				AddressData adr = customerData.address.get(0);
				edtAddress2.setText(adr.plz + " " +adr.city);
			}
			
			for(ContactData con : customerData.contact){
				switch(con.type){
					case ContactData.TYPE_PHONE:
						cmbPhone.addItem(con.data);
						break;
					case ContactData.TYPE_FAX:
						cmbFax.addItem(con.data);
						break;
					case ContactData.TYPE_EMAIL:
						cmbEmail.addItem(con.data);
						break;
					default : break;
				}
			}
		}
	}
	
	@Override
	public void contentChanged(){
		if(activeAddress != cmbAddress1.getSelectedIndex()){
			activeAddress = cmbAddress1.getSelectedIndex();
			if(activeAddress > -1 && activeAddress < customerData.address.size()){
				AddressData adr = customerData.address.get(activeAddress);
				edtAddress2.setText(adr.plz + " " + adr.city);
			}
		}
	}
	
	@Override
	public void update(Object... params){
		if(params.length > 0){
			try{
				customerData = (CustomerData) params[0];
			} catch( Exception e){
				e.printStackTrace();
			}
		}
		else customerData = new CustomerData();
	}
	
	@Override
	public void prepare(){
		setHeadline("Kundendaten exportieren");
		setContent();
	}

	@Override
	public void back(){
		switchToFrame(JKAV.FRAME_CUSTOMER_LIST);
	}
	
	@Override
	public void word(){
		String draft = edtDraftDoc.getText();
		String company = "" + edtCompany.getText();
		String name = "" + edtName.getText();
		String address1 = "" + (String)cmbAddress1.getSelectedItem();
		String address2 = "" + edtAddress2.getText();
		String phone = "" + (String)cmbPhone.getSelectedItem();
		String fax = "" + (String)cmbFax.getSelectedItem();
		String email = "" + (String)cmbEmail.getSelectedItem();
		
		ReplacementRuleSet rules = new ReplacementRuleSet();
		rules.setReplacementRule(ReplacementRuleSet.TAG_COMPANY, 	company);
		rules.setReplacementRule(ReplacementRuleSet.TAG_NAME,	 	name);
		rules.setReplacementRule(ReplacementRuleSet.TAG_ADDRESS1,	address1);
		rules.setReplacementRule(ReplacementRuleSet.TAG_ADDRESS2,	address2);
		rules.setReplacementRule(ReplacementRuleSet.TAG_PHONE,		phone);
		rules.setReplacementRule(ReplacementRuleSet.TAG_FAX, 		fax);
		rules.setReplacementRule(ReplacementRuleSet.TAG_EMAIL,		email);
		
		String target = System.getProperty("user.dir") + "\\jkav.docx";
		
		// SOME DAY LATER
		
		if(DocGenerator.generateDocX(draft, target, rules)){
			try {
				ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd", "/c", target});
				builder.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.showErrorBox("Fehler", "Beim Erstellen des Word Dokuments ist ein Fehler aufgetreten!");
		} 
	}
	
	private void LoadDialog(){ 
		int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File file = fileChooser.getSelectedFile();
        	edtDraftDoc.setText(file.getAbsolutePath());
          
        } else {
     	   System.out.println("Open command cancelled by user.");
        }

	} 
	
	private JTextField createLabelTextField(){
		JTextField t = createTextField();
		t.setEditable(false);
		return t;
	}

	@Override public void open() {}
	@Override public void save() {}
	@Override public void add() {}
	@Override public void edit() {}
	@Override public void remove() {}
}