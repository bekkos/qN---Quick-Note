package com.example.qn.controllers;


import com.example.qn.models.Note;
import com.example.qn.models.Notebook;
import com.example.qn.models.User;
import com.example.qn.repositories.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    DatabaseRepository databaseRepository;

    @GetMapping("/notebooks")
    public ArrayList<Notebook> getNotebooks(HttpSession session) {
        if(session.getAttribute("logged_in") == null) return null;
        System.out.printf("Request recieved.");
        return databaseRepository.getNotebooksFromDatabase((String) session.getAttribute("user_email"));
    }

    @GetMapping("/notes")
    public ArrayList<Note> getNotebooks(@RequestParam int notebook_id, HttpSession session) {
        if(session.getAttribute("logged_in") == null) return null;
        User user = databaseRepository.getUserFromDatabase((String) session.getAttribute("user_email"));
        ArrayList<Notebook> notebooks = databaseRepository.getNotebooksFromDatabase(user.getEmail());
        boolean hasAccess = false;
        for(Notebook n: notebooks) {
            if(n.getOwner_id() == user.getId()) hasAccess = true;
        }
        if(!hasAccess) return null;
        return databaseRepository.getNotesFromDatabase(notebook_id);
    }

    @GetMapping("/note")
    public Note getNote(@RequestParam int note_id, HttpSession session) {
        if(session.getAttribute("logged_in") == null) return null;
        /* TODO: Fix secuirty flaw here. NOTE: Without verification here, anyone can grab any note with a get request. */

        return databaseRepository.getNoteFromDatabase(note_id);
    }

    @PostMapping("/updateNote")
    public void updateNote(@RequestParam int note_id, @RequestParam String content) {
        /* TODO: Fix secuirty flaw here. NOTE: Without verification here, anyone can grab any note with a get request. */
        databaseRepository.updateNote(note_id, content);
    }
}
