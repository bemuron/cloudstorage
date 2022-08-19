package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating CredentialService bean");
    }

    public int addCredential(CredentialForm credentialForm, int userId) {

        return credentialMapper.addCredential(new Credential(null, credentialForm.getUrl(),
                credentialForm.getUserName(),
                credentialForm.getEncKey(),
                credentialForm.getPassword(),
                userId));
    }

    //edit a credential
    public int editCredential(CredentialForm credentialForm, int userId){

        return credentialMapper.editCredential(new Credential(credentialForm.getCredentialId(),
                credentialForm.getUrl(),
                credentialForm.getUserName(),
                credentialForm.getEncKey(),
                credentialForm.getPassword(),
                userId));
    }

    //deleting a credential
    public int deleteCredential(int credentialId){
        return credentialMapper.deleteCredential(credentialId);
    }

    public List<Credential> getCredentials(int userId) {
        /*Credential credential;
        List<Credential> creds = new ArrayList<Credential>();
        for (int i = 0; i < credentialMapper.getAllCredentials().size(); i++){
            credential = new Credential();
            credential.setCredentialId(credentialMapper.getAllCredentials().get(i).getCredentialId());
            credential.setUserId(credentialMapper.getAllCredentials().get(i).getUserId());
            credential.setPassword(credentialMapper.getAllCredentials().get(i).getPassword());
            credential.setEncKey(credentialMapper.getAllCredentials().get(i).getEncKey());
            credential.setUserName(credentialMapper.getAllCredentials().get(i).getUserName());
            credential.setUrl(credentialMapper.getAllCredentials().get(i).getUrl());
        }*/
        return credentialMapper.getAllCredentials(userId);
    }

    public Credential getCredential(int credId) {
        return credentialMapper.getCredential(credId);
    }
}
