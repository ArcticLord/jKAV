package com.arcticlord.jkav.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.data.AddressData;
import com.arcticlord.jkav.data.ContactData;
import com.arcticlord.jkav.data.CustomerData;
import com.arcticlord.jkav.utils.ImageLib;

public class FrameCustomerEdit extends Frame {

	private class IDTextFieldGroup{
		public JTextField[] textfield;
		public int id;
		public IDTextFieldGroup(int amount){
			id = -1;
			textfield = new JTextField[amount];
			for(int i = 0; i < amount; i++)
				textfield[i] = createTextField();
		}
	}


	private static final long serialVersionUID = 704032377245836805L;
	
	private JComboBox<String> cmbTitle;
	private JTextField edtForename;
	private JTextField edtName;
	private JTextField edtCompany;
	private JTextArea edtOther;
	private List<IDTextFieldGroup> edtAddressList;
	private List<IDTextFieldGroup> edtPhoneList;
	private List<IDTextFieldGroup> edtFaxList;
	private List<IDTextFieldGroup> edtEmailList;
		
	private JButton btnAddPhone;
	private JButton btnAddFax;
	private JButton btnAddEmail;
	private JButton btnAddAddress;
	
	private boolean changed = false;
	
	// id = -1 means new Entry
	private CustomerData customerData;
	
	public FrameCustomerEdit(JKAV c) {
		super(c);
	}
	
	@Override
	public void init() {	
		setMaxSize(400, -1);
		setMinBorder(100,100);
		setButtonList(FrameFoot.BTN_BACK | FrameFoot.BTN_SAVE);
		setScrollable(true);

		edtPhoneList = new ArrayList<IDTextFieldGroup>();
		edtFaxList = new ArrayList<IDTextFieldGroup>();
		edtEmailList = new ArrayList<IDTextFieldGroup>();
		edtAddressList = new ArrayList<IDTextFieldGroup>();
		
		btnAddPhone = createAddButton(1, edtPhoneList);
		btnAddFax = createAddButton(1, edtFaxList);
		btnAddEmail = createAddButton(1, edtEmailList);
		btnAddAddress = createAddButton(4, edtAddressList);
		
		edtForename = createTextField();
		edtName = createTextField();
		edtCompany = createTextField();
		edtOther = createTextArea(6);
		
		cmbTitle = createComboBox();
		addComboBoxItems(cmbTitle, CustomerData.titleSelection.getStringArray());
	}
	
	private JButton createAddButton(final int amount, final List<IDTextFieldGroup> list){
		JButton btnAdd = new JButton();
		btnAdd.setIcon(ImageLib.ADD_SMALL);
		btnAdd.setContentAreaFilled( false );
		btnAdd.setBorderPainted( false );
		btnAdd.setFocusable( false );
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				list.add(new IDTextFieldGroup(amount));
				setupView();
				getParent().validate();
			}
		});
		return btnAdd;
	}
	
	private void setupView(){
		removeAll();
		int row = 0;
		addComponent( createLabel("Firma"),		0, row, 1, 1, 0, 0 );
		addComponent( edtCompany, 				1, row, 4, 1, 1, 0 );
		row++;
		addComponent( createGap(), 				0, row, 5, 1, 1, 0 );
		row++;
		addComponent( createLabel("Titel"),		0, row, 1, 1, 0, 0 );
		addComponent( cmbTitle, 				1, row, 4, 1, 1, 0 );
		row++;
		addComponent( createLabel("Vorname"), 	0, row, 1, 1, 0, 0 );
		addComponent( edtForename, 				1, row, 4, 1, 1, 0 );
		row++;
		addComponent( createLabel("Nachname"),	0, row, 1, 1, 0, 0 );
		addComponent( edtName, 					1, row, 4, 1, 1, 0 );
		row++;
		addComponent( createGap(), 				0, row, 5, 1, 1, 0 );
		row++;
		addComponent( btnAddAddress,	 		5, row - 1, 1, 2, 0, 0 );
		for(IDTextFieldGroup a : edtAddressList){
			addComponent( createLabel("Straße"),0, row, 1, 1, 0, 0 );
			addComponent( a.textfield[0], 		1, row, 1, 1, 1, 0 );
			addComponent( createLabel("Nr."),	2, row, 1, 1, 0, 0 );
			addComponent( a.textfield[1],		3, row, 2, 1, 1, 0 );
			row++;
			addComponent( createLabel("PLZ"),	0, row, 1, 1, 0, 0 );
			addComponent( a.textfield[2], 		1, row, 1, 1, 1, 0 );
			addComponent( createLabel("Stadt"), 2, row, 1, 1, 0, 0 );
			addComponent( a.textfield[3],		3, row, 2, 1, 1, 0 );
			row++;
			addComponent( createGap(), 			0, row, 5, 1, 1, 0 );
			row++;
		}
		addComponent( createLabel("Telefon"),	0, row, 1, 1, 0, 0 );
		addComponent( btnAddPhone,	 			5, row - 1, 1, 2, 0, 0 );
		for(IDTextFieldGroup t : edtPhoneList){
			addComponent( t.textfield[0], 		1, row, 4, 1, 1, 0 );
			row++;
		}
		addComponent( createGap(), 				0, row, 5, 1, 1, 0 );
		row++;
		addComponent( createLabel("Fax"),		0, row, 1, 1, 0, 0 );
		addComponent( btnAddFax,	 			5, row - 1, 1, 2, 0, 0 );
		for(IDTextFieldGroup t : edtFaxList){
			addComponent( t.textfield[0], 		1, row, 4, 1, 1, 0 );
			row++;
		}
		addComponent( createGap(), 				0, row, 5, 1, 1, 0 );
		row++;
		addComponent( createLabel("E-Mail"),	0, row, 1, 1, 0, 0 );
		addComponent( btnAddEmail,	 			5, row - 1, 1, 2, 0, 0 );
		for(IDTextFieldGroup t : edtEmailList){
			addComponent( t.textfield[0], 		1, row, 4, 1, 1, 0 );
			row++;
		}
		addComponent( createGap(), 				0, row, 5, 1, 1, 0 );
		row++;
		addComponent(createLabel("Bemerkungen"),0, row, 1, 1, 0, 0 );
		addComponent( edtOther, 				1, row, 4, 4, 1, 0 );
	}
		
	private void clearContent(){
		edtForename.setText("");
		edtName.setText("");
		cmbTitle.setSelectedIndex(CustomerData.titleSelection.getArrIdOf("Herr"));
		edtCompany.setText("");
		edtAddressList.clear();
		edtAddressList.add(new IDTextFieldGroup(4));
		edtPhoneList.clear();
		edtPhoneList.add(new IDTextFieldGroup(1));
		edtFaxList.clear();
		edtFaxList.add(new IDTextFieldGroup(1));
		edtEmailList.clear();
		edtEmailList.add(new IDTextFieldGroup(1));
		edtOther.setText("");
	}
	
	private void setContent(){
		if(customerData != null){
			
			edtForename.setText(customerData.forename);
			edtName.setText(customerData.name);
			edtCompany.setText(customerData.company);
			edtOther.setText(customerData.other);
			cmbTitle.setSelectedIndex(CustomerData.titleSelection.getArrIdOf(customerData.title));

			edtAddressList.clear();
			for(AddressData adr : customerData.address){
				IDTextFieldGroup t = new IDTextFieldGroup(4);
				t.id = adr.aid;
				t.textfield[0].setText(adr.street);
				t.textfield[1].setText(adr.housenumber);
				t.textfield[2].setText(adr.plz);
				t.textfield[3].setText(adr.city);
				edtAddressList.add(t);
			}
			if(edtAddressList.isEmpty())
				edtAddressList.add(new IDTextFieldGroup(4));

			edtPhoneList.clear();
			edtFaxList.clear();
			edtEmailList.clear();
			for(ContactData contact : customerData.contact){
				IDTextFieldGroup t = new IDTextFieldGroup(1);
				t.id = contact.iid;
				t.textfield[0].setText(contact.data);
				switch(contact.type){
					case ContactData.TYPE_EMAIL:
						edtEmailList.add(t);
						break;
					case ContactData.TYPE_FAX:
						edtFaxList.add(t);
						break;
					case ContactData.TYPE_PHONE:
						edtPhoneList.add(t);
						break;
					default : break;
				}
			}
			if(edtPhoneList.isEmpty())
				edtPhoneList.add(new IDTextFieldGroup(1));
			if(edtFaxList.isEmpty())
				edtFaxList.add(new IDTextFieldGroup(1));
			if(edtEmailList.isEmpty())
				edtEmailList.add(new IDTextFieldGroup(1));
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
		if(customerData.cid == -1){
			clearContent();
			setHeadline("neuen Kunden erstellen");
		}
		else {
			setContent();
			disableButtons(FrameFoot.BTN_SAVE);
			changed = false;
			setHeadline("Kunden bearbeiten");
		}
		setupView();
	}

	@Override
	public void contentChanged(){
		if(changed != true){
			changed = true;
			enableButtons(FrameFoot.BTN_SAVE);
		}
	}
	
	@Override
	public void save(){
		customerData.forename = edtForename.getText();
		customerData.name = edtName.getText();
		customerData.company = edtCompany.getText();
		customerData.other = edtOther.getText();
		customerData.title = CustomerData.titleSelection.getEnumIdOf(cmbTitle.getSelectedIndex());
		
		customerData.address.clear();
		for(IDTextFieldGroup af : edtAddressList){
			AddressData ad = new AddressData();
			ad.aid = af.id;
			ad.street = af.textfield[0].getText();
			ad.housenumber = af.textfield[1].getText();
			ad.plz = af.textfield[2].getText();
			ad.city = af.textfield[3].getText();
			ad.checkValid();
			customerData.address.add(ad);
		}
		customerData.contact.clear();
		for(IDTextFieldGroup t : edtPhoneList){
			ContactData c = new ContactData();
			c.iid = t.id;
			c.type = ContactData.TYPE_PHONE;
			c.data = t.textfield[0].getText();
			c.checkValid();
			customerData.contact.add(c);
		}
		
		for(IDTextFieldGroup t : edtFaxList){
			ContactData c = new ContactData();
			c.iid = t.id;
			c.type = ContactData.TYPE_FAX;
			c.data = t.textfield[0].getText();
			c.checkValid();
			customerData.contact.add(c);
		}
		
		for(IDTextFieldGroup t : edtEmailList){
			ContactData c = new ContactData();
			c.iid = t.id;
			c.type = ContactData.TYPE_EMAIL;
			c.data = t.textfield[0].getText();
			c.checkValid();
			customerData.contact.add(c);
		}
		changed = false;
		
		if(!getDBConnection().storeCustomerData(customerData))
			showErrorBox("Fehler", "Beim Speichern des Eintrages ist ein Fehler aufgetreten.");
		else prepare();
	}
	
	@Override
	public void back(){
		switchToFrame(JKAV.FRAME_CUSTOMER_LIST);
	}

	@Override public void open() {}
	@Override public void add() {}
	@Override public void edit() {}
	@Override public void word() {}
	@Override public void remove() {}
}