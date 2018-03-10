package com.arcticlord.jkav.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.data.CustomerData;
import com.arcticlord.jkav.data.JobData;
import com.arcticlord.jkav.utils.ImageLib;


public class FrameMain extends Frame {

	private static final long serialVersionUID = -5474809069692879635L;
	private JButton			btnCustomerList;
	private JButton			btnQuit;
	private JButton			btnSave;
	private JButton			btnLoad;
	private JFileChooser	fileChooser;

	
	public FrameMain(JKAV c) {
		super(c);
	}
	
	@Override
	public void init() {	
		setMaxSize(300, 300);
		setHeadline("Hauptmenü");
		
		btnCustomerList = createIconButton("Kundenliste",ImageLib.CUSTOMERLIST);
		btnQuit = createIconButton("Beenden", ImageLib.QUIT);
		btnSave = createIconButton("Datenbank exportieren", ImageLib.EXPORT);
		btnLoad = createIconButton("Datenbank importieren", ImageLib.IMPORT);
		fileChooser = new JFileChooser();
		
		btnCustomerList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switchToFrame(JKAV.FRAME_CUSTOMER_LIST);
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveDialog();
			}
		});
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				loadDialog();
			}
		});
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				quit();
			}

		});

		addComponent(btnCustomerList,	0, 0, 1, 1, 1, 1);
		addComponent(createGap(),		0, 1, 1, 1, 1, 1);
		addComponent(btnLoad, 			0, 2, 1, 1, 1, 1);
		addComponent(createGap(),		0, 3, 1, 1, 1, 1);
		addComponent(btnSave, 			0, 4, 1, 1, 1, 1);
		addComponent(createGap(),		0, 5, 1, 1, 1, 1);
		addComponent(btnQuit, 			0, 6, 1, 1, 1, 1);
		
	}
	
	private void saveDialog(){ 
		int returnVal = fileChooser.showSaveDialog(this);   
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(getDBControl().dumpDB(file.getAbsolutePath()))
            	showMessageBox("Erfolg", "Die Datenbank wurde erfolgreich exportiert.");
            else showErrorBox("Fehler", "Beim Exportieren der Datenbank ist ein Fehler aufgetreten.");
        } else {
            System.out.println("Save command cancelled by user.");
        }
	} 
	
	@SuppressWarnings("unused")
	private void createBigDummyDB(){
		int customers = 10000;
		int jobs = 50;

		for(int i = 0; i < customers; i++){
			CustomerData c = CustomerData.createDummy(i);
			getDBConnection().storeCustomerData(c);
				for(int a = 0; a < jobs; a++ ){
					JobData j = JobData.createDummy(a, c.cid);
					getDBConnection().storeJobData(j);
				}
		}
	}
	
	// bei eltern/kind import fehlern hier checkn: http://www.webmasterfragen.com/fragen/28/cannot-delete-or-update-a-parent-row-a-foreign-key-constraint-fails.html
	private void loadDialog(){ 
		int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(showYesNoBox("Datenbank importieren","Vorhandene Datenbank wirklich überschreiben?")){
            	if(getDBControl().restoreDB(file.getAbsolutePath()))
            		showMessageBox("Erfolg", "Die ausgewählte Datenbank wurde erfolgreich importiert.");
            	else showErrorBox("Fehler", "Beim Importieren der Datenbank ist ein Fehler aufgetreten.");
            }
        } else {
     	   System.out.println("Open command cancelled by user.");
        }

	}

	@Override public void prepare() {}
	@Override public void contentChanged() {}
	@Override public void update(Object... params) {}
	@Override public void back() {}
	@Override public void open() {}
	@Override public void save() {}
	@Override public void add() {}
	@Override public void edit() {}
	@Override public void word() {}
	@Override public void remove() {}	 
}