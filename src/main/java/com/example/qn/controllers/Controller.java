package com.example.qn.controllers;

import com.example.qn.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.qn.respitories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    UserRepository userDatabase;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "qN");
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session, HttpServletResponse response) {
        if(userDatabase.checkCredentials(email, password)) {
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
        if(userDatabase.checkIfEmailTaken(email)) {
            model.addAttribute("error", "Email already taken.");
            return "register";
        }

        /* Input validation */

        /* --- */
        User user = new User(email, password, username);
        userDatabase.addUser(user);
        session.setAttribute("logged_in", true);
        session.setAttribute("user_email", email);
        response.setHeader("Location", "/home");
        response.setStatus(302);
        return "index";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model, HttpServletResponse response) {
        if(session.getAttribute("logged_in") != null) {
            model.addAttribute("username", userDatabase.getUserFromDatabase((String) session.getAttribute("user_email")).getUsername());
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
