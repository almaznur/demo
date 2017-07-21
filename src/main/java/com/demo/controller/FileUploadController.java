package com.demo.controller;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.demo.service.Document;
import com.demo.service.Metadata;
import com.demo.service.IFileUploadService;

/**
 * REST web service
 * 
 */
@Controller
@RequestMapping(value = "/storage")
public class FileUploadController {

	private static final Logger LOG = Logger.getLogger(FileUploadController.class);

	@Autowired
	IFileUploadService fileUploadService;

	/**
	 * Saving file to a storage alone with metadata.
	 * 
	 * Url: /storage/upload?file={file}&owner={owner}&date={date} [POST]
	 * 
	 * @param file
	 *            A file posted in a multipart request
	 * @param owner
	 *            The name of the uploading person
	 * @param date
	 *            The date of the document
	 * @return The meta data of the added document
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody Metadata handleFileUpload(
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "owner", required = true) String owner,
			@RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

		try {
			Document document = new Document(file.getBytes(), file.getOriginalFilename(), date, owner);
			getFileUploadService().save(document);
			return document.getMetadata();
		} catch (Exception e) {
			LOG.error("Error while uploading.", e);
			throw new RuntimeException(e);
		}
	}

	public IFileUploadService getFileUploadService() {
		return fileUploadService;
	}

	public void setFileUploadService(IFileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}

}
