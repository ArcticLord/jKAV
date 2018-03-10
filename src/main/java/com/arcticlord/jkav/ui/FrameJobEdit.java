package com.arcticlord.jkav.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.data.JobData;
import com.toedter.calendar.JDateChooser;

public class FrameJobEdit extends Frame {

	private static final long serialVersionUID = -571971082726107417L;

	public FrameJobEdit(JKAV c) {
		super(c);
	}
	
	private JTextField edtTitle;
	private JTextField edtPrice;
	private JDateChooser edtBeginDate;
	private JDateChooser edtEndDate;
	private JTextArea edtDescription;
	
	private JButton btnResetState;
	
	private JLabel lblTitle;
	private JLabel lblPrice;
	private JLabel lblBeginDate;
	private JLabel lblEndDate;
	private JLabel lblDescription;
	
	private boolean changed = false;

	private JobData jobData = null;
	
	@Override
	public void init() {	
		setMaxSize(400, -1);
		setButtonList(FrameFoot.BTN_BACK | FrameFoot.BTN_SAVE);
		setScrollable(true);
		
		lblTitle = createLabel("Bezeichnung");
		lblPrice = createLabel("Preis");
		lblBeginDate = createLabel("Bearbeitungsbegin");
		lblEndDate = createLabel("Bearbeitungsende");
		lblDescription = createLabel("Detail Beschreibung");
		
		btnResetState = new JButton("Status zurücksetzen");
		btnResetState.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(showYesNoBox("Zurücksetzen","Den Status von diesem Job wirklich zurücksetzen?")){
					if(!getDBConnection().changeJobState(jobData.jid, JobData.jobStates.getEnumIdOf("pending"))){
						showErrorBox("Fehler", "Beim Zurücksetzen ist ein Fehler aufgetreten.");
					}
					else {
						jobData.state = JobData.jobStates.getEnumIdOf("pending");
						updateStateButton();
						showMessageBox("Erfolg", "Der Status wurde erfolgreich zurückgesetzt.");
					}
				}
				
			}});
		edtTitle = createTextField();
		edtPrice = createNumberTextField();

		edtBeginDate = createDateChooser();
		edtEndDate = createDateChooser();
		
		edtDescription = createTextArea(6);
		
		int row = 0;
		addComponent( lblTitle, 		0, row, 1, 1, 0, 0 );
		addComponent( edtTitle, 		1, row, 1, 1, 1, 0 );
		row++;
		addComponent( createGap(), 		0, row, 2, 1, 1, 0 );
		row++;
		addComponent( lblPrice, 		0, row, 1, 1, 0, 0 );
		addComponent( edtPrice, 		1, row, 1, 1, 1, 0 );
		row++;
		addComponent( createGap(), 		0, row, 2, 1, 1, 0 );
		row++;
		addComponent( lblBeginDate,		0, row, 1, 1, 0, 0 );
		addComponent( edtBeginDate,		1, row, 1, 1, 1, 0 );
		row++;
		addComponent( lblEndDate,	 	0, row, 1, 1, 0, 0 );
		addComponent( edtEndDate,		1, row, 1, 1, 1, 0 );
		row++;
		addComponent( createGap(), 		0, row, 2, 1, 1, 0 );
		row++;
		addComponent( lblDescription,	0, row, 1, 1, 0, 0 );
		addComponent( edtDescription,	1, row, 1, 4, 1, 0 );
		row+=4;
		addComponent( createGap(), 		0, row, 2, 1, 1, 0 );
		row++;
		addComponent( btnResetState,	0, row, 2, 1, 1, 0 );
	}

	private void clearContent(){
		edtTitle.setText("");
		edtPrice.setText("");
		edtBeginDate.setDate(Calendar.getInstance().getTime());
		edtEndDate.setDate(Calendar.getInstance().getTime());
		edtDescription.setText("");
		updateStateButton();
	}
	
	private void updateStateButton(){
		if(jobData != null){
			if(jobData.state != JobData.jobStates.getEnumIdOf("pending")){
				btnResetState.setVisible(true);
				return;
			}
		}
		btnResetState.setVisible(false);
	}
	
	private void setContent(){
		if(jobData != null){
			edtTitle.setText(jobData.title);
			edtPrice.setText(""+jobData.price);
			edtBeginDate.setDate(jobData.beginDate);
			edtEndDate.setDate(jobData.endDate);
			edtDescription.setText(jobData.describtion);
			
			updateStateButton();
		}
	}
	
	
	
	@Override
	public void update(Object... params){
		if(params.length > 0){
			try{
				jobData = (JobData) params[0];
			} catch( Exception e){
				e.printStackTrace();
			}
		}
		else jobData = new JobData();
	}
	
	@Override
	public void prepare(){
		if (jobData.jid == -1){
			clearContent();
			setHeadline("neuen Auftrag erstellen");
		}
		else {
			setContent();
			disableButtons(FrameFoot.BTN_SAVE);
			changed = false;
			setHeadline("Auftrag #" + jobData.jid + " bearbeiten");
		}
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
		jobData.title = edtTitle.getText();
		jobData.price = Double.parseDouble(edtPrice.getText());
		jobData.beginDate = edtBeginDate.getDate();
		jobData.endDate = edtEndDate.getDate();
		jobData.describtion = edtDescription.getText();
		changed = false;
		
		if(!getDBConnection().storeJobData(jobData))
			showErrorBox("Fehler", "Beim Speichern des Eintrages ist ein Fehler aufgetreten.");
		else prepare();
	}
	
	@Override
	public void back(){
		switchToFrame(JKAV.FRAME_JOB_LIST);
	}

	@Override public void open() {}
	@Override public void add() {}
	@Override public void edit() {}
	@Override public void word() {}
	@Override public void remove() {}
}