package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId} ORDER BY credentialid DESC")
    List<Credential> getAllCredentials(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid =#{credId}")
    Credential getCredential(int credId);

    @Insert("INSERT INTO CREDENTIALS (url, username, enckey, password, userid) VALUES(#{url}, #{userName}, #{encKey}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int addCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{userName}, enckey = #{encKey}, password = #{password} WHERE credentialid = #{credentialId}")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int editCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid =#{credentialId}")
    int deleteCredential(int credentialId);

}

