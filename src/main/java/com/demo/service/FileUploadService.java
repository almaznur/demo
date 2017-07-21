package com.demo.service;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.dao.IDocumentDao;

@Service("fileUploadService")
public class FileUploadService implements IFileUploadService, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private IDocumentDao documentDao;

	@Override
	public Metadata save(Document document) {
		// TODO Auto-generated method stub
		getDocumentDao().save(document);
		return document.getMetadata();
	}
	
	public IDocumentDao getDocumentDao() {
        return documentDao;
    }

    public void setDocumentDao(IDocumentDao documentDao) {
        this.documentDao = documentDao;
    }

}