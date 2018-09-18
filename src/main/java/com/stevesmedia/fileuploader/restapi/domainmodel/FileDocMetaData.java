package com.stevesmedia.fileuploader.restapi.domainmodel;

import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.datastax.driver.core.utils.UUIDs;

/**
 * Meta data of a document from an archive managed by {@link FileUploaderService}.
 * @author steves
 */
public class FileDocMetaData implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(FileDocMetaData.class);

	private static final String PROP_UUID = "id";
	private static final String PROP_FILE_NAME = "file-name";
	private static final String PROP_FILE_SIZE = "file-size";
	private static final String PROP_DOCUMENT_DATE = "file-timestamp";

	private  String uuid;
	private  String mfileName;
	private  String mtimeStamp;
	private  String mfileSize;

	public FileDocMetaData() {
		super();
	}

	public FileDocMetaData(String fileName, String timeStamp, String fileSize) {
		this(UUIDs.timeBased().toString(), fileName, timeStamp, fileSize);
	}

	public FileDocMetaData(String uuid, String fileName, String timeStamp, String fileSize) {
		super();
		this.uuid = uuid;
		this.mfileName = fileName;
		this.mtimeStamp = timeStamp;
		this.mfileSize = fileSize;
	}

	public FileDocMetaData(Properties properties) {
		this(properties.getProperty(PROP_UUID),
				properties.getProperty(PROP_FILE_NAME),
				properties.getProperty(PROP_DOCUMENT_DATE),
				properties.getProperty(PROP_FILE_SIZE));
	}

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMfileName() {
		return mfileName;
	}

	public void setMfileName(String mfileName) {
		this.mfileName = mfileName;
	}

	public String getMtimeStamp() {
		return mtimeStamp;
	}

	public void setMtimeStamp(String mtimeStamp) {
		this.mtimeStamp = mtimeStamp;
	}

	public String getMfileSize() {
		return mfileSize;
	}

	public void setMfileSize(String mfileSize) {
		this.mfileSize = mfileSize;
	}

	public Properties createProperties() {
		Properties props = new Properties();
		props.setProperty(PROP_UUID, getUuid());
		props.setProperty(PROP_FILE_NAME, getMfileName());
		props.setProperty(PROP_FILE_SIZE, getMfileSize());
		props.setProperty(PROP_DOCUMENT_DATE, getMtimeStamp());
		return props;
	}

}
