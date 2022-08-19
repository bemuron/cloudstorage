package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
//@RequestMapping("/credential")
public class CredentialController {
    private UserService userService;
    private CredentialService credentialService;

    public CredentialController(UserService userService,
                                CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    /*
    @GetMapping()
    public String homeView() {
        return "home";
    }
    */

    //@GetMapping
    public String getHomePa(Model model) {
        //model.addAttribute("notes", this.noteService.getNotes());
        //model.addAttribute("files", this.fileService.getFiles());
        //model.addAttribute("credentials", this.credentialService.getCredentials());
        return "home";
    }

    //handle the creating/editing of a credential
    //@PostMapping(path = {"/add-credential"})
    public String createCredential(Authentication authentication, CredentialForm credentialForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        if(credentialForm.getCredentialId() > 0){
            this.credentialService.editCredential(credentialForm, user.getUserId());
        }else{
            this.credentialService.addCredential(credentialForm, user.getUserId());
        }
        credentialForm.setUrl("");
        credentialForm.setEncKey("");
        credentialForm.setPassword("");
        credentialForm.setUserName("");
        credentialForm.setCredentialId(0);

        //model.addAttribute("credentials", this.credentialService.getCredentials());
        model.addAttribute("credentialForm", new CredentialForm());
        model.addAttribute("fileForm", new FileForm());

        return "redirect:/home";
    }

    //delete a credential
    //@GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") int credentialId, Authentication authentication,
                                   CredentialForm credentialForm, Model model){
        credentialService.deleteCredential(credentialId);

        return "redirect:/home";
    }
}
