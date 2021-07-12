package com.example.qn.controllers;


import com.example.qn.models.Note;
import com.example.qn.models.Notebook;
import com.example.qn.models.User;
import com.example.qn.repositories.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    DatabaseRepository databaseRepository;

    @GetMapping("/notebooks")
    public ArrayList<Notebook> getNotebooks(HttpSession session, HttpServletResponse response) {
        if(session.getAttribute("logged_in") == null) {
            response.setHeader("Location", "/");
            response.setStatus(302);
            return null;
        }
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

        /* VERIFICATION */
        Note n = databaseRepository.getNoteFromDatabase(note_id);
        Notebook nb = databaseRepository.getNotebookFromDatabase(n.getNotebook_id());
        User u = databaseRepository.getUserFromDatabase((String) session.getAttribute("user_email"));
        if(u.getId() != nb.getOwner_id()) return null;
        /* --- */
        return databaseRepository.getNoteFromDatabase(note_id);
    }

    @PostMapping("/updateNote")
    public void updateNote(@RequestParam int note_id, @RequestParam String content, HttpSession session) {
        /* VERIFICATION */
        Note n = databaseRepository.getNoteFromDatabase(note_id);
        Notebook nb = databaseRepository.getNotebookFromDatabase(n.getNotebook_id());
        User u = databaseRepository.getUserFromDatabase((String) session.getAttribute("user_email"));
        if(u.getId() != nb.getOwner_id()) return;
        /* --- */
        databaseRepository.updateNote(note_id, content);
    }

    @PostMapping("/newNotebook")
    public void newNotebook(@RequestParam String name, HttpSession session) {
        if(session.getAttribute("logged_in") == null) return;

        User user = databaseRepository.getUserFromDatabase((String) session.getAttribute("user_email"));
        databaseRepository.addNotebook(name, user.getId());
    }

    @PostMapping("/newNote")
    public void newNote(@RequestParam String name, @RequestParam int notebook_id, HttpSession session) {
        if(session.getAttribute("logged_in") == null) return;
        databaseRepository.addNote(name, notebook_id);
    }

}
