package com.arcticlord.jkav.ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.arcticlord.jkav.utils.ImageLib;


public class FrameHead extends JPanel {

	private static final long serialVersionUID = 5259007394845678269L;
	private JLabel headline;
	private JLabel lblIcon;
	
	public FrameHead() {
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		setBackground(new Color(0,150,0));
		lblIcon = new JLabel(ImageLib.LOGO);
		headline = new JLabel("", JLabel.CENTER);
		headline.setFont(new Font("Monospaced", Font.PLAIN, 38));
	
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 1; gbc.gridheight = 2;
		gbc.weightx = 0; gbc.weighty = 0;
		layout.setConstraints( lblIcon, gbc );		
		add (lblIcon);
		setIconVisible(false);
		
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1; gbc.weighty = 0;
		layout.setConstraints( headline, gbc );
		add(headline);
	}

	
	public void setHeadlineText(String h){
		headline.setText(h);
	}
	
	public void setIconVisible(boolean visible){
		lblIcon.setVisible(visible);
	}

}