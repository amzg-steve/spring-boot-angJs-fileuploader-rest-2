package com.stevesmedia.fileuploader.restapi.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST web service for file uploading service.
 * All service calls are delegated to instances of {@link FileUploaderService}
 * 
 * /fileUploaderApi/uploadfile?file={file}  				   Uplolad file by POST
 *   file: A file posted in a multipart request 
 * @author steves
 */
@RestController
@RequestMapping(value = "/fileUploaderApi")
@Api(value = "document")
public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	FileUploaderService fileUploaderService;

	/**
	 * Adds a document to the file store.
	 * 
	 * Url: /fileUploaderApi/uploadfile?file={file} [POST]
	 * 
	 * @param file A file posted in a multipart request
	 * @return The meta data of the added document
	 * @throws Exception 
	 */
	@PostMapping(value = "/uploadfile")
	@ApiOperation(value = "operation to upload file to repo")
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
			fileUploaderService.save(document);
			
			return "You have successfully uploaded file: " + file.getOriginalFilename();
			
		} catch (Exception e) {
			throw new Exception("File Upload Failed. Please ensure file size < 2MB");
		}
	}

	/**
	 * Finds document in the archive.
	 * 
	 * Url: /fileUploaderApi/files [GET]
	 * 
	 * @return A list of document meta data
	 */
	@GetMapping("/files")
	@ApiOperation(value = "operation to retrieve all files available at repo")
	public HttpEntity<List<FileDocMetaData>> retrieveDocuments() {
		return new ResponseEntity<List<FileDocMetaData>>(fileUploaderService.findDocuments(),
				new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Returns the document file from the store with the given UUID.
	 * 
	 * Url: /fileUploaderApi/files/{uuid} [GET]
	 * 
	 * @param id The UUID of a document
	 * @return The document file
	 */
	@GetMapping(value = "/files/{uuid}")
	@ApiOperation(value = "operation to retrieve file based on id")
	public HttpEntity<byte[]> getDocument(@PathVariable String uuid) {         

		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(fileUploaderService.getDocumentFile(uuid), httpHeaders, HttpStatus.OK);
		
	}
	
	/**
	 * Deletes all files from repo
	 * 
	 * Url: /fileUploaderApi/files/deleteAll [DELETE]
	 */
	@DeleteMapping("/files/deleteAll")
	@ApiOperation(value = "operation to delete all files from repo")
	public HttpEntity<String> deleteAllFiles() {
	
		return fileUploaderService.deleteAll();
	}

}
