package com.arcticlord.jkav.utils;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageLib {
	
	private static final boolean JAR = true;
	
	/*
	 * FrameCustomerEdit  -> add_small.png
	 * FrameSplash -> logo_big.gif
	 * FrameMain -> customerlist.png
	 *			 -> quit.png
	 *			 -> export.png
	 *			 -> import.png
	 * FrameHead -> logo.gif
	 * FrameFoot -> add.png
	 * 			 -> remove.png
	 *			 -> back.png
	 *			 -> save.png
	 *			 -> open.png
	 *			 -> edit.png
	 *			 -> word.png
	 */
	
	public static final Image LOGO_BIG = LoadImg(JAR, "images/logo_big.gif");
	public static final Icon LOGO = LoadIcon(JAR, "images/logo.gif");	
	public static final Icon CUSTOMERLIST = LoadIcon(JAR, "images/customerlist.png");
	public static final Icon QUIT = LoadIcon(JAR, "images/quit.png");
	public static final Icon EXPORT = LoadIcon(JAR, "images/export.png");
	public static final Icon IMPORT = LoadIcon(JAR, "images/import.png");
	public static final Icon ADD = LoadIcon(JAR, "images/add.png");
	public static final Icon ADD_SMALL = LoadIcon(JAR, "images/add_small.png");
	public static final Icon REMOVE = LoadIcon(JAR, "images/remove.png");
	public static final Icon BACK = LoadIcon(JAR, "images/back.png");
	public static final Icon SAVE = LoadIcon(JAR, "images/save.png");
	public static final Icon OPEN = LoadIcon(JAR, "images/open.png");
	public static final Icon EDIT = LoadIcon(JAR, "images/edit.png");
	public static final Icon WORD = LoadIcon(JAR, "images/word.png");
	
	
	private static Icon LoadIcon(boolean isJar, String iconName){
		if(isJar)
			return new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					ImageLib.class.getClassLoader().getResource(iconName)));
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(iconName));		
	}
	
	private static Image LoadImg(boolean isJar, String imgName){
		if(isJar)
			return new ImageIcon(ImageLib.class.getClassLoader()
					.getResource(imgName)).getImage();
		return new ImageIcon(imgName).getImage();
		
	}
}
