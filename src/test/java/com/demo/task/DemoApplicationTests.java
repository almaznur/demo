package com.demo.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.demo.controller.FileUploadController;
import com.demo.service.Document;
import com.demo.service.FileUploadService;
import com.demo.service.Metadata;

@RunWith(SpringRunner.class)
@SpringBootTest()
@WebAppConfiguration
@RestClientTest(FileUploadController.class)
public class DemoApplicationTests {

	private static final Logger LOG = Logger.getLogger(DemoApplicationTests.class);

	RestTemplate restTemplate;

	@Autowired
	private FileUploadController fileUploadController;
	@MockBean
	private FileUploadService fileUploadService;
	@Autowired
	private MockRestServiceServer mockServer;
	
	@Test
	public void testUpload() throws IOException, URISyntaxException {
		System.out.println();

		restTemplate = new RestTemplate();
		//mockServer = MockRestServiceServer.bindTo(restTemplate).build();
		
		StringBuilder sb = new StringBuilder();
		String fileName = "AlfrescoIntro.txt";
		Path path = Paths.get(getClass().getResource("/"+fileName).toURI());
		
		byte[] fileData = Files.readAllBytes(path);
		Date today = Calendar.getInstance().getTime();
		String owner = getOwner();
		
		Document document = new Document(fileData, fileName, today, owner);
		
		String tempFilePath = writeDocumentToTempFile(document);
		MultiValueMap<String, Object> parts = createMultipartFileParam(tempFilePath);
		String dateString = Metadata.DATE_FORMAT.format(document.getFileDate());
		Metadata metadata = restTemplate.postForObject("http://localhost:8080/storage/upload?owner={name}&date={date}", parts, Metadata.class, document.getOwner(), dateString);
		
		assertThat(metadata.getFileName()).isEqualToIgnoringCase(fileName);
		
		LOG.info("File has been uploaded, uuid: " + metadata.getFileUuid());
		
	}
	
	private String getOwner() {
        return this.getClass().getSimpleName();
    }
	
	private MultiValueMap<String, Object> createMultipartFileParam(String tempFilePath) {
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new FileSystemResource(tempFilePath));
		return parts;
	}

	private String writeDocumentToTempFile(Document document) throws IOException, FileNotFoundException {
		Path path;
		path = Files.createTempDirectory(document.getFileUuid());
		String tempDirPath = path.toString();
		File file = new File(tempDirPath, document.getFileName());
		FileOutputStream fo = new FileOutputStream(file);
		fo.write(document.getFileData());
		fo.close();
		return file.getPath();
	}


}
