package com.example.qn.controllers;

import com.example.qn.models.Note;
import com.example.qn.models.Notebook;
import com.example.qn.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.qn.repositories.DatabaseRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    DatabaseRepository databaseRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "qN");
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session, HttpServletResponse response) {
        if(databaseRepository.checkCredentials(email, password)) {
            session.setAttribute("user_email", email);
            session.setAttribute("logged_in", true);
            response.setHeader("Location", "/home");
            response.setStatus(302);
            return "home";
        } else {
            model.addAttribute("title", "qN");
            model.addAttribute("error", "Incorrect credentials.");
            return "index";
        }
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String email, @RequestParam String password, Model model, HttpServletResponse response, HttpSession session) {
        if(databaseRepository.checkIfEmailTaken(email)) {
            model.addAttribute("error", "Email already taken.");
            return "register";
        }

        /* Input validation */
        String spicyRegex = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
        boolean pass = true;
        String errorMsg = "";

        if(!username.matches(spicyRegex)) {
            pass = false;
            errorMsg = "Username does not meet the requirements.";
        }
        if(username == null || email == null || password == null) {
            pass = false;
            errorMsg = "Something went wrong, please try again.";
        }
        if(!pass) {
            model.addAttribute("error", errorMsg);
            return "register";
        }
        /* --- */

        User user = new User(email, password, username);
        databaseRepository.addUser(user);
        session.setAttribute("logged_in", true);
        session.setAttribute("user_email", email);
        response.setHeader("Location", "/home");
        response.setStatus(302);
        return "index";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model, HttpServletResponse response) {
        if(session.getAttribute("logged_in") != null) {
            model.addAttribute("username", databaseRepository.getUserFromDatabase((String) session.getAttribute("user_email")).getUsername());
            return "home";
        } else {
            response.setHeader("Location", "/");
            response.setStatus(302);
            return "index";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        response.setHeader("Location", "/");
        response.setStatus(302);
        return "index";
    }



}
