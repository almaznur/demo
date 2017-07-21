package com.demo.service;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

/**
 * Represents file model.
 * 
 */
public class Document extends Metadata implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private byte[] fileData;

	public Document(byte[] fileData, String fileName, Date fileDate, String owner) {
		super(fileName, fileDate, owner);
		this.fileData = fileData;
	}

	public Document(Properties properties) {
		super(properties);
	}

	public Document(Metadata metadata) {
		super(metadata.getFileUuid(), metadata.getFileName(), metadata.getFileDate(), metadata.getOwner());
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public Metadata getMetadata() {
		return new Metadata(getFileUuid(), getFileName(), getFileDate(), getOwner());
	}
}
