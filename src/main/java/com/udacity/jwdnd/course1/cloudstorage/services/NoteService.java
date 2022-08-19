package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating NoteService bean");
    }

    public int addNote(NoteForm noteForm, int userId) {

        return noteMapper.addNote(new Note(null, noteForm.getNoteTitle(),
                noteForm.getNoteDescription(),
                userId));
    }

    //edit a note
    public int editNote(NoteForm noteForm, int userId){

        return noteMapper.editNote(new Note(noteForm.getNoteId(), noteForm.getNoteTitle(),
                noteForm.getNoteDescription(),
                userId));
    }

    //deleting a note
    public int deleteNote(int noteId){
        return noteMapper.deleteNote(noteId);
    }

    public List<Note> getNotes(int userId) {
        return noteMapper.getAllNotes(userId);
    }
}
