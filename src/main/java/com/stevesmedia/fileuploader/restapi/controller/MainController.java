package com.stevesmedia.fileuploader.restapi.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stevesmedia.fileuploader.restapi.domainmodel.FileDocMetaData;
import com.stevesmedia.fileuploader.restapi.domainmodel.FileDocument;
import com.stevesmedia.fileuploader.restapi.service.FileUploaderService;

/**
 * REST web service for file uploading service.
 * All service calls are delegated to instances of {@link FileUploaderService}
 * 
 * /fileUploadApi/uploadfile?file={file}  				   Uplolad file by POST
 *   file: A file posted in a multipart request 
 * @author steves
 */
@RestController
@RequestMapping(value = "/fileUploadApi")
public class MainController {

	private static final Logger LOG = Logger.getLogger(MainController.class);

	@Autowired
	FileUploaderService fileUploaderService;

	/**
	 * Adds a document to the file store.
	 * 
	 * Url: /fileUploadApi/uploadfile?file={file} [POST]
	 * 
	 * @param file A file posted in a multipart request
	 * @return The meta data of the added document
	 * @throws Exception 
	 */
	@PostMapping(value = "/uploadfile")
	public String handleFileUpload(@RequestParam("file") MultipartFile file ) throws Exception {
		
		try {
			if(file == null || file.isEmpty()) {
				return "Please select a file";
			}
			//get the local timestamp
			LocalDateTime timeStamp = LocalDateTime.now();
			//get the uploaded filesize
			String fileSize = FileUtils.byteCountToDisplaySize(file.getSize());
			FileDocument document = new FileDocument(file.getBytes(), file.getOriginalFilename(), timeStamp.toString(), fileSize );
			getFileUploaderService().save(document);
			
			return "You have successfully uploaded file: " + file.getOriginalFilename();
			
		} catch (Exception e) {
			throw new Exception("File Upload Failed. Please ensure file size < 2MB");
		}
	}

	/**
	 * Finds document in the archive.
	 * 
	 * Url: /fileUploadApi/getallfiles [GET]
	 * 
	 * @return A list of document meta data
	 */
	@GetMapping("/getallfiles")
	public HttpEntity<List<FileDocMetaData>> findDocuments() {
		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<List<FileDocMetaData>>(getFileUploaderService().findDocuments(),
				httpHeaders,HttpStatus.OK);
	}

	/**
	 * Returns the document file from the store with the given UUID.
	 * 
	 * Url: /fileUploadApi/file/{id} [GET]
	 * 
	 * @param id The UUID of a document
	 * @return The document file
	 */
	@RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
	public HttpEntity<byte[]> getDocument(@PathVariable String id) {         

		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(getFileUploaderService().getDocumentFile(id), httpHeaders, HttpStatus.OK);
		
	}
	
	@GetMapping("/deleteAll")
	public void deleteAllFiles() {
		getFileUploaderService().deleteAll();
	}
	public FileUploaderService getFileUploaderService() {
		return fileUploaderService;
	}

	public void setFileUploaderService(FileUploaderService fileUploaderService) {
		this.fileUploaderService = fileUploaderService;
	}

}
