package com.arcticlord.jkav.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.data.CustomerData;


public class FrameCustomerList extends Frame {

	private static final long serialVersionUID = 704032377245836805L;
	
	// The different Search Filter
	private static final String PERSONALDATA =	"Personendaten";
	private static final String CONTACTDATA =	"Kontaktdaten";
	private static final String ADDRESSDATA =	"Addressdaten";
	private static final String JOBDATA =		"Jobdaten";

	private PanelCustomerView customerView;
	private JKAVTable table;
	
	private ArrayList<Object[]> customerData;
	private CustomerData activeCustomerData = null;
	
	private String searchType = "";
	private String searchString = "";

	public FrameCustomerList(JKAV c) {
		super(c);
	}
	
	@Override
	public void init() {	
		setHeadline("Kundenliste");
		setButtonList(FrameFoot.BTN_BACK | FrameFoot.BTN_OPEN 
					| FrameFoot.BTN_EDIT | FrameFoot.BTN_ADD 
					| FrameFoot.BTN_REMOVE | FrameFoot.BTN_WORD);
	
	
		customerView = new PanelCustomerView();
		
		JKAVTable.ContentDescription content = new JKAVTable.ContentDescription();
		content.addDescription("KundenID", true, true);
		content.addDescription("Firma",true,false);
		content.addDescription("Vorname",true,false);
		content.addDescription("Nachname",true,false);
		table = new JKAVTable(content);
		table.setSelectionListener(new ITableSelectionListener(){
			
			// NOTE:  When DoubleClick is  triggered, the SingleClick has always been triggered before!
			@Override
			public void clicked(int selectedRow) {
				int activeCustomerId = (Integer)customerData.get(selectedRow)[0];
				activeCustomerData = getDBConnection().getCustomerData(activeCustomerId);
				customerView.setData(activeCustomerData);
				enableButtons(FrameFoot.BTN_EDIT | FrameFoot.BTN_REMOVE
					   		| FrameFoot.BTN_OPEN | FrameFoot.BTN_WORD);
			}

			@Override
			public void doubleClicked(int selectedRow) {
				open();
			}
		});

		JScrollPane ss = new JScrollPane(customerView);
		Dimension size = getSize();
		size.width = 450;
		ss.setPreferredSize(size);
		
		// search zeug
		JPanel pnlSearch = new JPanel();
		JPanel pnlSearchControl = new JPanel();
		
		pnlSearch.setLayout(new BorderLayout());
		TitledBorder bb = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black),"Suche");
		bb.setTitleColor(Color.black);
		pnlSearch.setBorder(bb);
		pnlSearch.setBackground(new Color(0,150,0));
		
		pnlSearchControl.setBackground(new Color(0,150,0));
		
		final JTextField edtSearchText = createTextField();
		final JComboBox<String> cmbSearchFilter = createComboBox();
		addComboBoxItems(cmbSearchFilter, new String[]{	PERSONALDATA,
														CONTACTDATA,
														ADDRESSDATA,
														JOBDATA});
		JButton btnSearch = new JButton("Suche");
		btnSearch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchString = edtSearchText.getText();
				searchType = (String)cmbSearchFilter.getSelectedItem();
				prepare();
			}});
		JButton btnSearchReset = new JButton("Reset");
		btnSearchReset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchString = "";
				searchType = "";
				edtSearchText.setText("");
				cmbSearchFilter.setSelectedIndex(0);
				prepare();
			}});
		
		pnlSearchControl.add(cmbSearchFilter);
		pnlSearchControl.add(btnSearch);
		pnlSearchControl.add(btnSearchReset);
		
		pnlSearch.add(edtSearchText);
		pnlSearch.add(pnlSearchControl,BorderLayout.EAST);
		
		addComponent(new JScrollPane(table),	0, 0, 1, 1, 1, 1);
		addComponent(ss,						1, 0, 1, 2, 1, 1);
		addComponent(table.getFilterText(),		0, 1, 1, 1, 1, 0);
		addComponent(createGap(),				0, 2, 2, 1, 1, 0);
		addComponent(pnlSearch,					0, 3, 2, 1, 1, 0);
	}

	
	@Override
	public void prepare(){
		table.clearSelection();
		activeCustomerData = null;
		customerView.clear();		
		disableButtons(	FrameFoot.BTN_EDIT | FrameFoot.BTN_OPEN
					  | FrameFoot.BTN_REMOVE | FrameFoot.BTN_WORD );
		// Fetch Data depending on active SearchType
		if(searchType.compareTo(PERSONALDATA)==0)
			customerData = getDBConnection().searchCustomerByPersonal(searchString);
		else if(searchType.compareTo(CONTACTDATA)==0)
			customerData = getDBConnection().searchCustomerByContact(searchString);
		else if(searchType.compareTo(ADDRESSDATA)==0)
			customerData = getDBConnection().searchCustomerByAddress(searchString);
		else if(searchType.compareTo(JOBDATA)==0)
			customerData = getDBConnection().searchCustomerByJob(searchString);
		else
			customerData = getDBConnection().getCustomerList();		
		table.setData(customerData);
	}
	
	@Override
	public void back(){
		switchToFrame(JKAV.FRAME_MAIN);
	}
	@Override
	public void open(){
		switchToFrame(JKAV.FRAME_JOB_LIST, activeCustomerData.cid);
	}
	@Override
	public void edit(){
		switchToFrame(JKAV.FRAME_CUSTOMER_EDIT, activeCustomerData);
	}
	@Override
	public void add(){
		switchToFrame(JKAV.FRAME_CUSTOMER_EDIT);
	}
	@Override
	public void remove(){
		if(showYesNoBox("Löschen","Den ausgewählten Kunden wirklich löschen?")){
			if(!getDBConnection().removeCustomerData(activeCustomerData.cid))
				showErrorBox("Fehler", "Beim Löschen des Kunden ist ein Fehler aufgetreten.");
			else {
				prepare();			
				showMessageBox("Erfolg", "Der ausgewählte Kunde wurde erfolgreich gelöscht.");
			}
		}
	}
	@Override
	public void word(){
		switchToFrame(JKAV.FRAME_WORD, activeCustomerData);
	}

	@Override public void contentChanged() {}
	@Override public void update(Object... params) {}
	@Override public void save() {}
}