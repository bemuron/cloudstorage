package com.udacity.jwdnd.course1.cloudstorage.controller;

import ch.qos.logback.classic.Logger;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;

    private UserService userService;
    private FileService fileService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(NoteService noteService,
                          UserService userService,
                          FileService fileService,
                          CredentialService credentialService,
                          EncryptionService encryptionService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    /*
    @GetMapping()
    public String homeView() {
        return "home";
    }
    */

    @GetMapping
    public String getHomePage(Authentication authentication,
                              NoteForm noteForm, Credential credentialForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("noteForm", noteForm);
        model.addAttribute("files", this.fileService.getFiles(user.getUserId()));
        model.addAttribute("credentialForm", credentialForm);
        model.addAttribute("credentials", this.credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    //handle the creating/editing of a note
    @PostMapping(path = {"/add-note"})
    public String createNewNote(@ModelAttribute("noteForm") NoteForm noteForm,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        if(noteForm.getNoteId() == null){
            if(this.noteService.addNote(noteForm, user.getUserId()) == 1){
                redirectAttributes.addFlashAttribute("successMsg",
                        "Note: "+ noteForm.getNoteTitle() +" - successfully added");
            }else{
                redirectAttributes.addFlashAttribute("errorMsg",
                        "Sorry, something went wrong, note not saved");
            }
        }else{
            if(this.noteService.editNote(noteForm, user.getUserId()) == 1){
                redirectAttributes.addFlashAttribute("successMsg",
                        "Note: "+ noteForm.getNoteTitle() +" - successfully edited");
            }else{
                redirectAttributes.addFlashAttribute("successMsg",
                        "Sorry, yur edit to: "+ noteForm.getNoteTitle() +" was not saved. Please try again");
            }
        }
        noteForm.setNoteDescription("");
        noteForm.setNoteTitle("");

        return "redirect:/home";
    }

    //handle deleting of a note
    //@RequestMapping("/delete-note/{noteId}")
    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(@PathVariable("noteId") int noteId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes,
                             NoteForm noteForm, Model model){

        if(noteService.deleteNote(noteId) == 1){
            redirectAttributes.addFlashAttribute("successMsg",
                    "Note deleted");
        }else{
            redirectAttributes.addFlashAttribute("errorMsg",
                    "Sorry, something went wrong, note delete failed");
        }

        return "redirect:/home";
    }


    //handle the creating/editing of a credential
    @PostMapping(path = {"/add-credential"})
    public String createCredential(Authentication authentication,
                                   RedirectAttributes redirectAttributes,
                                   CredentialForm credentialForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        //encrypt the password
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);

        credentialForm.setEncKey(encodedKey);
        credentialForm.setPassword(encryptedPassword);

        if(credentialForm.getCredentialId() == null){
            if(this.credentialService.addCredential(credentialForm, user.getUserId()) == 1){
                redirectAttributes.addFlashAttribute("successMsg",
                        "Credentials for "+credentialForm.getUrl() +" added successfully");
            }else{
                redirectAttributes.addFlashAttribute("errorMsg",
                        "Sorry, something went wrong. Credential not added.");
            }

        }else{
            if(this.credentialService.editCredential(credentialForm, user.getUserId()) == 1){
                redirectAttributes.addFlashAttribute("successMsg",
                        "Credentials for "+credentialForm.getUrl() +" edited successfully");
            }else{
                redirectAttributes.addFlashAttribute("errorMsg",
                        "Sorry something went wrong. Credential not edited.");
            }
        }
        credentialForm.setUrl("");
        credentialForm.setEncKey("");
        credentialForm.setPassword("");
        credentialForm.setUserName("");
        credentialForm.setCredentialId(0);

        redirectAttributes.addFlashAttribute("message",
                "Credential successfully added");

        return "redirect:/home";
    }

    //delete a credential
    @GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") int credentialId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes,
                                   CredentialForm credentialForm, Model model){

        if(credentialService.deleteCredential(credentialId) == 1){
            redirectAttributes.addFlashAttribute("successMsg",
                    "Credential successfully deleted");
        }else{
            redirectAttributes.addFlashAttribute("errorMsg",
                    "Sorry something went wrong. Credential not deleted");
        }

        return "redirect:/home";
    }

    //decrypt the password
    @RequestMapping(value = "/decrypt-pass", method = RequestMethod.GET)
    public @ResponseBody String decryptPassword(HttpServletRequest request,
                                                HttpServletResponse response,CredentialForm credentialForm, Model model)
            throws Exception {
        String cid = request.getParameter("credId");
        Credential credential = credentialService.getCredential(Integer.parseInt(cid));

        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getEncKey());

        model.addAttribute("credential-password", decryptedPassword);
        //return new ModelAndView("ExpectedReturnView");
        return decryptedPassword;
    }

    //handle the creating/editing of a file
    @PostMapping("/upload-file")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload,
                                   RedirectAttributes redirectAttributes,Exception exc,
                                   Authentication authentication, FileForm fileForm, Model model)
            throws IOException {

        /*if (exc instanceof MaxUploadSizeExceededException) {
            redirectAttributes.addFlashAttribute("infoMsg",
                    "size exceeds 5MB limit.");
        }*/

        String username = authentication.getName();
        User user = userService.getUser(username);

        if(!fileUpload.isEmpty() || fileUpload.getSize()>0) { //check if user selected a file
            File fileByName = this.fileService.getFileByName(fileUpload.getOriginalFilename(), user.getUserId());
            if (fileByName == null) {

                fileForm.setFileData(fileUpload.getBytes());
                fileForm.setFileName(fileUpload.getOriginalFilename());
                fileForm.setContentType(fileUpload.getContentType());
                fileForm.setFileSize(String.valueOf(fileUpload.getSize()));

                int res = this.fileService.addFile(fileForm, user.getUserId());
                if (res == 1) {
                    redirectAttributes.addFlashAttribute("successMsg",
                            "You successfully uploaded " + fileUpload.getOriginalFilename() + "!");
                } else {
                    redirectAttributes.addFlashAttribute("errorMsg",
                            "Failed to upload " + fileUpload.getOriginalFilename() + ".");
                }
            } else {
                redirectAttributes.addFlashAttribute("infoMsg",
                        "A file with this name " + fileUpload.getOriginalFilename() + " already exists");
            }
        }else {
            redirectAttributes.addFlashAttribute("infoMsg",
                    "Please select a file first");
        }

        return "redirect:/home";
    }

    //download file
    @RequestMapping("/download-file/{fid}")
    public String downloadFile(@PathVariable("fid")
                           int fid,
                               Model model,
                               HttpServletResponse response) throws IOException {

        File file = fileService.getFile(fid);

        InputStream inputStream = null;

        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
            //header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
            inputStream = new ByteArrayInputStream(file.getFileData());
            OutputStream out = response.getOutputStream();
            response.setContentType(file.getContentType());
            IOUtils.copy(inputStream, out);
            out.flush();
            out.close();

            model.addAttribute("successMsg",
                    "File " + file.getFileName() + "downloaded successfully!");

        } catch (IOException e) {
            System.out.println(e.toString());
            //Handle exception here
            model.addAttribute("errorMsg", "Failed to download " + file.getFileName() + ".");
        }

        //return "redirect:/home";
        return "Success";
    }

    //delete a file
    @GetMapping("/delete-file/{fileId}")
    public String deleteFile(@PathVariable("fileId") int fileId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes,
                                   FileForm fileForm, Model model){
        if(fileService.deleteFile(fileId) == 1){
            redirectAttributes.addFlashAttribute("successMsg",
                    "File deleted successfully!");
        }else{
            redirectAttributes.addFlashAttribute("errorMsg",
                    "Failed to delete file.");
        }
        return "redirect:/home";
    }


}
