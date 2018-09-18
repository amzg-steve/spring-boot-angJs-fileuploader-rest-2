package com.stevesmedia.fileuploader.restapi.domainmodel;

import java.io.Serializable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.utils.UUIDs;
import com.stevesmedia.fileuploader.restapi.service.FileUploaderService;

import lombok.Data;

/**
 * Meta data of a document from an archive managed by {@link FileUploaderService}.
 * @author steves
 */
@Data
public class FileDocMetaData implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(FileDocMetaData.class);

	private static final String PROP_UUID = "id";
	private static final String PROP_FILE_NAME = "file-name";
	private static final String PROP_FILE_SIZE = "file-size";
	private static final String PROP_DOCUMENT_DATE = "file-timestamp";

	private  String uuid;
	private  String fileName;
	private  String timeStamp;
	private  String fileSize;

	public FileDocMetaData() {
		super();
	}

	public FileDocMetaData(String fileName, String timeStamp, String fileSize) {
		this(UUIDs.timeBased().toString(), fileName, timeStamp, fileSize);
	}

	public FileDocMetaData(String uuid, String fileName, String timeStamp, String fileSize) {
		super();
		this.uuid = uuid;
		this.fileName = fileName;
		this.timeStamp = timeStamp;
		this.fileSize = fileSize;
	}

	public FileDocMetaData(Properties properties) {
		this(properties.getProperty(PROP_UUID),
				properties.getProperty(PROP_FILE_NAME),
				properties.getProperty(PROP_DOCUMENT_DATE),
				properties.getProperty(PROP_FILE_SIZE));
	}

	public Properties createProperties() {
		Properties props = new Properties();
		props.setProperty(PROP_UUID, this.uuid);
		props.setProperty(PROP_FILE_NAME, this.fileName);
		props.setProperty(PROP_FILE_SIZE, this.fileSize);
		props.setProperty(PROP_DOCUMENT_DATE, this.timeStamp);
		return props;
	}

}
