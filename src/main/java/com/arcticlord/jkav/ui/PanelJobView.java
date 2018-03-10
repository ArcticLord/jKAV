package com.arcticlord.jkav.ui;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.arcticlord.jkav.data.JobData;
import com.arcticlord.jkav.db.DBController;
import com.arcticlord.jkav.utils.HTMLGenerator;

public class PanelJobView extends JPanel{

	private static final long serialVersionUID = 2972772027630696389L;
	private JLabel lblTitle;
	private JLabel lblCustomer;
	private JLabel lblPeriod;
	private JLabel lblPrice;
	private JLabel lblDescription;
	private JPanel pnlChangeState;
	private JPanel pnlJobInfo;
	
	private JobData jobData;
	private DBController dbAccess;
	
	
	public PanelJobView(DBController db, final Frame.IRefreshParent ref){
		dbAccess = db;
		jobData = null;
		lblCustomer = new JLabel();
		lblTitle = new JLabel();
		lblPeriod = new JLabel();
		lblPrice = new JLabel();
		lblDescription = new JLabel();
	
		pnlChangeState = new JPanel();
		pnlChangeState.setBackground(new Color(255,255,255));
		pnlChangeState.setLayout(new GridLayout(1,2));
		
		pnlJobInfo = new JPanel();
		pnlJobInfo.setBackground(new Color(255,255,255));
		pnlJobInfo.setLayout(new BoxLayout(pnlJobInfo, BoxLayout.PAGE_AXIS));
		
		JButton btnAccept = new JButton("Auftrag Annehmen");
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dbAccess.changeJobState(jobData.jid, JobData.jobStates.getEnumIdOf("accepted"));
				ref.refreshParent();
			}
		});
		JButton btnReject = new JButton("Auftrag Ablehnen");
		btnReject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dbAccess.changeJobState(jobData.jid, JobData.jobStates.getEnumIdOf("rejected"));
				ref.refreshParent();
			}
		});
		
	
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		setBackground(new Color(255,255,255));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		pnlJobInfo.add(lblTitle);
		pnlJobInfo.add(lblCustomer);
		pnlJobInfo.add(lblPeriod);
		pnlJobInfo.add(lblPrice);
		pnlJobInfo.add(lblDescription);
		
	
		pnlChangeState.add(btnAccept);
		pnlChangeState.add(btnReject);
		
		
		add(pnlJobInfo);
		add(pnlChangeState);
		layout.putConstraint(SpringLayout.NORTH, pnlChangeState,
                20, SpringLayout.SOUTH, pnlJobInfo);
		
		pnlChangeState.setVisible(false);
	}
	
	public void clear(){
		lblCustomer.setText("");
		lblTitle.setText("");
		lblPeriod.setText("");
		lblPrice.setText("");
		lblDescription.setText("");
		pnlChangeState.setVisible(false);
	}
	
	
	public void setData(JobData d, String customerName){
		jobData = d;
		lblTitle.setText(	HTMLGenerator.base(
								HTMLGenerator.headline(jobData.title, 1)));
		lblCustomer.setText(HTMLGenerator.base(
								HTMLGenerator.headline("Auftraggeber: "+ customerName, 3)));
		lblPeriod.setText(	HTMLGenerator.base(
								HTMLGenerator.table( 2, 2,
									"Von:", JobData.convertDate(jobData.beginDate, JobData.DATE_STYLE_NORMAL),
									"Bis:", JobData.convertDate(jobData.endDate, JobData.DATE_STYLE_NORMAL))));
		lblPrice.setText(	HTMLGenerator.base(
								HTMLGenerator.headline(jobData.price + "€", 3)));
					
		lblDescription.setText(	HTMLGenerator.base(jobData.describtion) );
		
		if(d.state != JobData.jobStates.getEnumIdOf("pending")) pnlChangeState.setVisible(false);
		else pnlChangeState.setVisible(true);
	}
}