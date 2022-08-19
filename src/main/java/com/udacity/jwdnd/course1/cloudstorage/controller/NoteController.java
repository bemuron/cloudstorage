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

@Controller
@RequestMapping("/note")
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService,UserService userService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping
    public String getHomeP(Model model) {
        //model.addAttribute("notes", this.noteService.getNotes());

        model.addAttribute("credentialForm", new CredentialForm());
        return "home";
    }

    //handle the creating/editing of a note
    @PostMapping(path = {"/add-note"})
    public String createNewNote(Authentication authentication, NoteForm noteForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);

        if(noteForm.getNoteId() > 0){
            this.noteService.editNote(noteForm, user.getUserId());
        }else{
            this.noteService.addNote(noteForm, user.getUserId());
        }
        noteForm.setNoteDescription("");
        noteForm.setNoteTitle("");

        //model.addAttribute("notes", this.noteService.getNotes());
        model.addAttribute("credentialForm", new CredentialForm());
        model.addAttribute("fileForm", new FileForm());
        return "home";
    }

    //handle deleting of a note
    //@RequestMapping("/delete-note/{noteId}")
    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(@PathVariable("noteId") int noteId, Authentication authentication,
                             NoteForm noteForm, Model model){
        noteService.deleteNote(noteId);

        //model.addAttribute("notes", this.noteService.getNotes());
        return getHomeP(model);
    }
}
