package com.vypeensoft.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Which_Class_In_Which_Jar {
    private static FileOutputStream logStream = null;	
    private static List<String> inputFolderArray = new ArrayList<String>();
    private static SimpleDateFormat sdf_for_timestamp       = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static NumberFormat numberFormatter = NumberFormat.getInstance();
    
	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			inputFolderArray.add(".");
		}
		if (args != null && args.length >= 1) {
			inputFolderArray.add(args[0]);
		}
		if (args != null && args.length >= 2) {
			inputFolderArray.add(args[1]);
		}
		if (args != null && args.length >= 3) {
			inputFolderArray.add(args[2]);
		}
//		inputFolderArray.add("D:\\devspace\\repository\\com\\vypeensoft\\");
		logStream = new FileOutputStream("jar_class_list.txt");

		List<String> fileList = new ArrayList<String>();
		for (int i = 0; i < inputFolderArray.size(); i++) {
			String oneFolder = inputFolderArray.get(i);
			fileList.addAll(listFilesForFolder(new File(oneFolder)));
			System.out.println("oneFolder size()=" + fileList.size());
		}
		Collections.sort(fileList);

		System.out.println("Final.size()" + fileList.size());
		for (int i = 0; i < fileList.size(); i++) {
			String fileName = fileList.get(i);
			getZipFileList(fileName);
		}
		logStream.close();
	}

	/*------------------------------------------------------------------------------*/
	public static List<String> listFilesForFolder(final File folder) {
		List<String> returnList = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				returnList.addAll(listFilesForFolder(fileEntry));
			} else {
				String fileName = fileEntry.getAbsolutePath();
				if (fileName.endsWith(".jar") || fileName.endsWith(".war") || fileName.endsWith(".ear")) {
					returnList.add(fileEntry.getAbsolutePath());
				}
			}
		}
		return returnList;
	}

	/*------------------------------------------------------------------------------*/
	private static void getZipFileList(String fileName) throws IOException {
		final ZipFile file = new ZipFile(fileName);
		try {
			final Enumeration<? extends ZipEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
				final ZipEntry entry = entries.nextElement();
				if (!entry.isDirectory()) {
				    String timeStampString = sdf_for_timestamp.format(new Date(entry.getTime()));
				    String numberFormatted = numberFormatter.format(entry.getSize());
					String logLine = fileName +" | "+ timeStampString +" | " + leftPadding(numberFormatted, 10) +" | "+ entry.getName();
					System.out.println(logLine);
					logStream.write((logLine+"\r\n").getBytes());
				}
			}
		} finally {
			file.close();
		}
	}
	/*------------------------------------------------------------------------------*/
	public static String rightPadding(String str, int num) {
	    return String.format("%1$-" + num + "s", str);
	}
	/*------------------------------------------------------------------------------*/
	 public static String leftPadding(String str, int n) {
	    return String.format("%1$" + n + "s", str);
	}	
	/*------------------------------------------------------------------------------*/
	/*------------------------------------------------------------------------------*/
	/*------------------------------------------------------------------------------*/
	/*------------------------------------------------------------------------------*/
	/*------------------------------------------------------------------------------*/
}
