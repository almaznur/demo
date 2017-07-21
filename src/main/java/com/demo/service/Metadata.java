package com.demo.service;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 * Metadata of a file
 * 
 */
public class Metadata implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(Metadata.class);

	private static final String FILE_UUID = "file_uuid";
	private static final String OWNER = "owner_name";
	private static final String FILE_NAME = "file_name";
	private static final String FILE_DATE = "file_date";
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	protected String fileUuid;
	protected String fileName;
	protected Date fileDate;
	protected String owner;

	public Metadata() {
		super();
	}

	public Metadata(String fileName, Date fileDate, String owner) {
		this(UUID.randomUUID().toString(), fileName, fileDate, owner);
	}

	public Metadata(String uuid, String fileName, Date fileDate, String owner) {
		super();
		this.fileUuid = uuid;
		this.fileName = fileName;
		this.fileDate = fileDate;
		this.owner = owner;
	}

	public Metadata(Properties properties) {
		this(properties.getProperty(FILE_UUID), properties.getProperty(FILE_NAME), null, properties.getProperty(OWNER));
		String dateString = properties.getProperty(FILE_DATE);
		if (dateString != null) {
			try {
				this.fileDate = DATE_FORMAT.parse(dateString);
			} catch (ParseException e) {
				LOG.error("Error parsing date string: " + dateString + ", format: yyyy-MM-dd", e);
			}
		}
	}

	public String getFileUuid() {
		return fileUuid;
	}

	public void setFileUuid(String fileUuid) {
		this.fileUuid = fileUuid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getFileDate() {
		return fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Properties createProperties() {
		Properties props = new Properties();
		props.setProperty(FILE_UUID, getFileUuid());
		props.setProperty(FILE_NAME, getFileName());
		props.setProperty(OWNER, getOwner());
		props.setProperty(FILE_DATE, DATE_FORMAT.format(getFileDate()));
		return props;
	}
	
}
