package com.demo.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.service.Document;

@Service("documentDao")
public class DocumentDao implements IDocumentDao {

	private static final Logger LOG = Logger.getLogger(DocumentDao.class);

	@Value("${directory}")
	private String directory;

	@Value("${metadata_file_name_prefix}")
	private String metadata_file_name_prefix;

	@PostConstruct
	public void init() {
		createDir(directory);
	}

	/**
	 * Inserts a document to the storage by creating a folder with the UUID of
	 * the document. In the folder the document is saved and a properties file
	 * with the meta data of the document.
	 * 
	 */
	@Override
	public void save(Document document) {
		// TODO Auto-generated method stub
		try {
			createDir(document);
			saveFileData(document);
			saveMetaData(document);
		} catch (IOException e) {
			String message = "Error while saving file";
			LOG.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	private void saveFileData(Document document) throws IOException {
		String path = getDirPath(document);
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(new File(path), document.getFileName())));
		stream.write(document.getFileData());
		stream.close();
	}

	public void saveMetaData(Document document) throws IOException {
		String path = getDirPath(document);
		Properties props = document.createProperties();
		File f = new File(new File(path), metadata_file_name_prefix);
		OutputStream out = new FileOutputStream(f);
		props.store(out, "Document meta data");
	}

	private String createDir(Document document) {
		String path = getDirPath(document);
		createDir(path);
		return path;
	}

	private String getDirPath(Document document) {
		return getDirPath(document.getFileUuid());
	}

	private String getDirPath(String uuid) {
		StringBuilder sb = new StringBuilder();
		sb.append(directory).append(File.separator).append(uuid);
		String path = sb.toString();
		return path;
	}

	private void createDir(String path) {
		File file = new File(path);
		file.mkdirs();
	}

}
