package com.arcticlord.jkav.utils;
import java.io.File;
import java.io.IOException;

/**
 * This class can open sub folders from a specific
 * main directory. The sub folders will be created if
 * they do not exist.
 * @author johann
 */
public class FolderControl{
	/**
	 * The full path to the main directory.
	 */
	private static String mainPath = "";
	
	/**
	 * Checks if the given path exists and is a
	 * real writeable directory.
	 * @param path	The path to the directory to be checked.
	 * @return		Whether this check failed or not.
	 */
	private static boolean exists(String path){
		File f = new File(path);
		if(f.isDirectory() && f.canWrite())
			return true;
		return false;
	}
	

	/**
	 * Creates a specific directory.
	 * @param path	The full path of the directory to be created.
	 * @return		Whether the folder creation failed or not.
	 */
	private static boolean create(String path){
		File f = new File(path);
		return f.mkdir();
	}
	
	/**
	 * Creates the given folder in the main path if it does not
	 * exists and opens it in the Windows-Explorer.
	 * @param subFolder		SubFolder name in the main path.
	 * @return				Whether the existence check or the 
	 * 						creation of the directory failed or not.
	 */
	public static boolean open(String subFolder){
		String fullPath = mainPath + subFolder;
		// check that main path was set before
		if(mainPath.isEmpty())
			return false;
		// check existence and create if not
		if(!exists(fullPath)){
			if(!create(fullPath))
				return false;
		}
		// open Windows-Explorer
		try {
			Runtime.getRuntime().exec("explorer.exe \""+ fullPath+"\"");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
		return true;
	}
	
	/**
	 * Sets the path to the main directory if exists.
	 * @param path	The full path to the main directory.
	 * @return		Whether the path exists or not.
	 */
	public static boolean setMainPath(String path){
		// Check that last character is seperator
		if (!path.endsWith(File.separator))
			path = path + File.separator;
		if(exists(path)){
			mainPath = path;
			return true;
		}
		return false;
	}
	
	/* deprecated code start */
	public static boolean existsOLD(String subFolder){
		File f = new File(mainPath+subFolder);
		if(f.isDirectory() && f.canWrite())
			return true;
		return false;
	}
	public static void renameOLD(String oldSubFolderName, String newSubFolderName){
		System.out.println("old:"+oldSubFolderName + " new:" + newSubFolderName);
		String fullPathOld = mainPath + oldSubFolderName;
		String fullPathNew = mainPath + newSubFolderName;
		
		File f = new File(fullPathOld);
		f.renameTo(new File(fullPathNew));
	}
	/* deprecated code stop */
}