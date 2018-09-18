package com.stevesmedia.fileuploader.restapi.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileSystemUtils;

import com.stevesmedia.fileuploader.restapi.domainmodel.FileDocMetaData;
import com.stevesmedia.fileuploader.restapi.domainmodel.FileDocument;

@Repository
public class FileUploaderServiceDaoFileSystemImpl implements FileUploaderServiceDao, Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(FileUploaderServiceDaoFileSystemImpl.class);

	public static final String ROOT_DIR = "fileStore";
	public static final String METADATA_FILENAME = "metadata.properties";

	@PostConstruct
	public void init() {
		createDirectory(ROOT_DIR);
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(new File(ROOT_DIR));
		createDirectory(ROOT_DIR);
	}
	
	/**
	 * Inserts a file in the file system store by creating a folder with uuid of file
	 * A properties file is also created with metadata.
	 */
	@Override
	public void insert(FileDocument document) {
		try {
			createDirectory(document);
			saveFileData(document);
			saveMetaData(document);
		} catch (IOException e) {
			String message = "Failed inserting document";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	/**
	 * Finds files in the file system store
	 */
	@Override
	public List<FileDocMetaData> findFiles() {
		try {
			return findInFileSystem();
		} catch (IOException e) {
			String message = "Failed to find file";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	/**
	 * Returns the document from the data store with the given uuid.
	 */
	@Override
	public FileDocument load(String uuid) {
		try {
			return loadFromFileSystem(uuid);
		} catch (IOException e) {
			String message = "Failed loading document id: " + uuid;
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	private String createDirectory(FileDocument document) {
		String path = getDirectoryPath(document);
		createDirectory(path);
		return path;
	}

	private String getDirectoryPath(FileDocument document) {
		return getDirectoryPath(document.getUuid());
	}

	private String getDirectoryPath(String uuid) {
		StringBuilder sb = new StringBuilder();
		sb.append(ROOT_DIR).append(File.separator).append(uuid);
		String path = sb.toString();
		return path;
	}

	private void createDirectory(String path) {
		File file = new File(path);
		file.mkdirs();
	}

	private void saveFileData(FileDocument document) throws IOException {
		String path = getDirectoryPath(document);
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(new File(path), document.getFileName())));
		stream.write(document.getFileData());
		stream.close();
	}

	public void saveMetaData(FileDocument document) throws IOException {
		String path = getDirectoryPath(document);
		Properties props = document.createProperties();
		File f = new File(new File(path), METADATA_FILENAME);
		OutputStream out = new FileOutputStream( f );
		props.store(out, "File metadata");       
	}

	private List<FileDocMetaData> findInFileSystem() throws IOException  {
		List<String> uuidList = getUuidList();
		List<FileDocMetaData> metadataList = new ArrayList<FileDocMetaData>(uuidList.size());
		for (String uuid : uuidList) {
			FileDocMetaData metadata = loadMetadataFromFileSystem(uuid);
			metadataList.add(metadata);
		}
		return metadataList;
	}

	private List<String> getUuidList() {
		File file = new File(ROOT_DIR);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return Arrays.asList(directories);
	}

	private FileDocMetaData loadMetadataFromFileSystem(String uuid) throws IOException {
		FileDocMetaData document = null;
		String dirPath = getDirectoryPath(uuid);
		File file = new File(dirPath);
		if(file.exists()) {
			Properties properties = readProperties(uuid);
			document = new FileDocMetaData(properties);
		} 
		return document;
	}

	private Properties readProperties(String uuid) throws IOException {
		Properties prop = new Properties();
		InputStream input = null;     
		try {
			input = new FileInputStream(new File(getDirectoryPath(uuid),METADATA_FILENAME));
			prop.load(input);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	private FileDocument loadFromFileSystem(String uuid) throws IOException {
		FileDocMetaData metadata = loadMetadataFromFileSystem(uuid);
		if(metadata==null) {
			return null;
		}
		Path path = Paths.get(getFilePath(metadata));
		FileDocument document = new FileDocument(metadata);
		document.setFileData(Files.readAllBytes(path));
		return document;
	}
	
    private String getFilePath(FileDocMetaData metadata) {
        String dirPath = getDirectoryPath(metadata.getUuid());
        StringBuilder sb = new StringBuilder();
        sb.append(dirPath).append(File.separator).append(metadata.getFileName());
        return sb.toString();
    }
}
