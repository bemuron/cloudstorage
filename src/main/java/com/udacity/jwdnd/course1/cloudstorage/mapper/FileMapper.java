package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userId} ORDER BY fileId DESC")
    List<File> getAllFiles(int userId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userId}")
    File getFileByName(String filename,int userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fid}")
    File getFile(int fid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int addFile(File file);

    @Update("UPDATE FILES SET filename = #{fileName}, contenttype = #{contentType}, filesize = #{fileSize}, userid = #{userId}, filedata = #{fileData} WHERE fileId = #{fileId}")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int editFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId =#{fileId}")
    int deleteFile(int fileId);

}

