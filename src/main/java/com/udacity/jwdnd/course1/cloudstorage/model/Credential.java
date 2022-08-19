package com.udacity.jwdnd.course1.cloudstorage.model;

import org.springframework.lang.Nullable;

public class Credential {
    private Integer credentialId;
    private String url;
    private String userName;
    private String encKey;
    private String password;
    private Integer userId;

    public Credential(@Nullable Integer credentialId, String url, String userName, String encKey, String password, Integer userId) {
        this.credentialId = credentialId;
        this.url = url;
        this.userName = userName;
        this.encKey = encKey;
        this.password = password;
        this.userId = userId;
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncKey() {
        return encKey;
    }

    public void setEncKey(String encKey) {
        this.encKey = encKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
