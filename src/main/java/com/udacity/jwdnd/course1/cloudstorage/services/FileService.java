package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating FileService bean");
    }

    public int addFile(FileForm fileForm, int userId) {
        File newFile = new File();
        newFile.setUserId(userId);
        newFile.setFileData(fileForm.getFileData());
        newFile.setFileName(fileForm.getFileName());
        newFile.setFileSize(fileForm.getFileSize());
        newFile.setContentType(fileForm.getContentType());

        return fileMapper.addFile(newFile);
    }

    //edit a file
    public int editFile(FileForm fileForm, int userId){
        File file = new File();

        file.setUserId(userId);
        file.setFileId(fileForm.getFileId());
        file.setFileData(fileForm.getFileData());
        file.setFileName(fileForm.getFileName());
        file.setFileSize(fileForm.getFileSize());
        file.setContentType(fileForm.getContentType());

        return fileMapper.editFile(file);
    }

    //deleting a file
    public int deleteFile(int fileId){
        return fileMapper.deleteFile(fileId);
    }

    public List<File> getFiles(int userId) {
        return fileMapper.getAllFiles(userId);
    }

    public File getFile(int fid) {
        return fileMapper.getFile(fid);
    }

    public File getFileByName(String filename, int userId) {
        return fileMapper.getFileByName(filename, userId);
    }
}
