package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
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
//@RequestMapping("/file")
public class FileController {
    private UserService userService;
    private FileService fileService;

    public FileController(UserService userService,
                          FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    /*
    @GetMapping()
    public String homeView() {
        return "home";
    }
    */

    //@GetMapping
    public String getHomePag(Model model) {
        //model.addAttribute("notes", this.noteService.getNotes());
        //model.addAttribute("files", this.fileService.getFiles());
        //model.addAttribute("credentials", this.credentialService.getCredentials());
        return "home";
    }

    //handle the creating/editing of a file
    //@PostMapping(path = {"/add-file"})
    public String createFile(Authentication authentication, FileForm fileForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        if(fileForm.getFileId() > 0){
            this.fileService.editFile(fileForm, user.getUserId());
        }else{
            this.fileService.addFile(fileForm, user.getUserId());
        }
        fileForm.setFileName("");
        fileForm.setContentType("");
        fileForm.setFileSize("");
        fileForm.setFileId(0);

        //model.addAttribute("files", this.fileService.getFiles());
        return "home";
    }

    //delete a file
    //@GetMapping("/delete-file/{fileId}")
    public String deleteFile(@PathVariable("fileId") int fileId, Authentication authentication,
                                   FileForm fileForm, Model model){
        fileService.deleteFile(fileId);

        return getHomePag(model);
    }
}
