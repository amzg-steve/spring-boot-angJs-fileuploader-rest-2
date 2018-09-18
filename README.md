# File-Store-Project
File upload and retrieval Spring Boot Rest based Service.
=====================

A simple file system based archive with REST interface. An angularJS based web client is also included to test the service.

The available Restful APIs as follows:

* **Upload a file operation(max 2MB size):**
*http://localhost:8787/fileUploadApi/uploadfile?uploadfile={filename.xxx} POST*

* **Retrive a file from filesystem by file id operation:**
*http://localhost:8787/fileUploadApi/file/{uuid} GET*

* **Retrieve all files from store operation:**
*http://localhost:8787/fileUploadApi/getallfiles GET*

* **Delete all files from filesystem operation:**
*http://localhost:8787/fileUploadApi/deleteAll DELETE*

How to Build and run ?
-------------

```bash
git clone https://github.com/amzg-steve/File-Store-Project.git
#After extracting the files
cd File-Store-Project-master/spring-boot-angJs-fileuploader-rest
```
To start this Tomcat based application from pwd
```bash
./mvnw spring-boot:run
```
Or

```bash
mvn package
java -jar target/spring-boot-angJs-fileuploader-rest-0.0.1-SNAPSHOT.jar
```

How to invoke the client application ?
-------------
**http://localhost:8787/index.html**

Directory name where the uploaded files will be stored:
-------------
**spring-boot-angJs-fileuploader-rest/fileStore**

Each file will be stored within individual child folders with a metadata.properties file.

The file metadata displayed on the frontend if you click the **Show Files** button will come from this .properties file.


****Disclaimer: Application has been tested and verified on macOS, using Chrome and Firefox browsers.**


