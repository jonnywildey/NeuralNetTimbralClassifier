package com.filemanager;

public class CSVValue {
	/* object for sending back a parsed csv value and its original length */
	private StringBuffer CSVString;
	private int originalLength;
	
	public CSVValue(StringBuffer CSVString, int originalLength) {
		this.CSVString = CSVString;
		this.originalLength = originalLength;
	}
	
	public int getOriginalLength() {
		return originalLength;
	}
	public String getCSVString() {
		return CSVString.toString();
	}
	public StringBuffer getCSVStringBuffer() {
		return CSVString;
	}

}
