package com.arcticlord.jkav.ui;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.arcticlord.jkav.data.AddressData;
import com.arcticlord.jkav.data.ContactData;
import com.arcticlord.jkav.data.CustomerData;
import com.arcticlord.jkav.utils.FolderControl;
import com.arcticlord.jkav.utils.HTMLGenerator;



public class PanelCustomerView extends JPanel{

	private static final long serialVersionUID = 2972772027630696389L;
		
	public PanelCustomerView(){
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(new Color(255,255,255));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	
	private JLabel createEmailLabel(final String content, final String url){
		JLabel lbl = new JLabel(content);
		lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					if(Desktop.isDesktopSupported())
						Desktop.getDesktop().mail(new URI("mailto:"+url));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return lbl;
	}

	/**
	 * Completely clears the Panel
	 */
	public void clear(){
		// remove all child elements
		removeAll();
		// repaint Panel, so it is full of white again
		repaint();
	}
	
	public void setData(final CustomerData d){
		clear();
		/// NAME / COMPANY
		String name =	CustomerData.titleSelection.getNameOf(d.title) +
						" " + d.forename + " " + d.name;
		add(new JLabel(		HTMLGenerator.base(
								HTMLGenerator.text(name, 7) +
								HTMLGenerator.br() +
								HTMLGenerator.text(d.company, 5))));
		/// ADDRESS
		add(new JLabel(	HTMLGenerator.base(
				HTMLGenerator.br() +
				HTMLGenerator.text("Anschrift:", 4))));
		for(AddressData ad : d.address){
			add(new JLabel(	HTMLGenerator.base(
								HTMLGenerator.br() +
								HTMLGenerator.table(2, 2,
									HTMLGenerator.text(ad.street, 3),
									HTMLGenerator.text(ad.housenumber, 3),
									HTMLGenerator.text(ad.plz, 3),
									HTMLGenerator.text(ad.city, 3)))));
		}
		/// PHONES
		add(new JLabel(	HTMLGenerator.base(
				HTMLGenerator.br() +
				HTMLGenerator.text("Telefon:", 4))));
		for(ContactData cd : d.contact){
			if(cd.type == ContactData.TYPE_PHONE){
				add(new JLabel(	HTMLGenerator.base(
									HTMLGenerator.text(cd.data, 3))));
			}
		}
		/// FAX
		add(new JLabel(	HTMLGenerator.base(
				HTMLGenerator.br() +
				HTMLGenerator.text("Fax:", 4))));
		for(ContactData cd : d.contact){
			if(cd.type == ContactData.TYPE_FAX){
				add(new JLabel(	HTMLGenerator.base(
									HTMLGenerator.text(cd.data, 3))));
			}
		}
		/// EMAIL
		add(new JLabel(	HTMLGenerator.base(
				HTMLGenerator.br() +
				HTMLGenerator.text("E-Mail:", 4))));
		for(ContactData cd : d.contact){
			if(cd.type == ContactData.TYPE_EMAIL) {
				add (createEmailLabel(	HTMLGenerator.base(
											HTMLGenerator.text(cd.data, 3, HTMLGenerator.BLUE)),
											cd.data));
			}
		}
	
		/// OTHER
		add(new JLabel(	HTMLGenerator.base(
				HTMLGenerator.br() +
				HTMLGenerator.text("Bemerkung:", 4))));
		add (new JLabel(	HTMLGenerator.base(
								HTMLGenerator.text(d.other, 3))));
		

		/// OPEN CUSTOMER FOLDER BUTTON
		add(new JLabel(	HTMLGenerator.base(
				HTMLGenerator.br() +
				HTMLGenerator.br())));
		JButton btnOpenFolder = new JButton("Kunden-Ordner öffnen");
		btnOpenFolder.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String subFolderName = d.getCustomerFolderName();
				/* deprecated code for transition time - start */
				String oldSubFolderName = d.getCustomerFolderNameOld();
				if(FolderControl.existsOLD(oldSubFolderName))
					FolderControl.renameOLD(oldSubFolderName, subFolderName);
				/* deprecated code - end */
				FolderControl.open(subFolderName);
			}});
		add(btnOpenFolder);
		
		
		getParent().validate();
	}
}