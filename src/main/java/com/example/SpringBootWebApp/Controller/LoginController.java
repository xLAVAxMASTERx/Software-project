package com.example.SpringBootWebApp.Controller;

import com.example.SpringBootWebApp.Models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * Handles HTTP GET requests to "/login" endpoint and renders the login/signin page.
     *
     * @param model The `Model` object used to add attributes to the view.
     * @return The logical view name "signin" associated with the login page.
     */
    @GetMapping("/login")
    public String registerUser(Model model) {
        model.addAttribute("user",new User());
        return "signin";
    }

}
