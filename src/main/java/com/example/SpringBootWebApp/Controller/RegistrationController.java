
package com.example.SpringBootWebApp.Controller;

import com.example.SpringBootWebApp.Models.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class RegistrationController {
    /**
     * Handles HTTP GET requests to "/registration" endpoint and renders the registration/signup page.
     *
     * @param model The `Model` object used to add attributes to the view.
     * @return The logical view name "signup" associated with the registration page.
     */
    @GetMapping("/registration")
    public String registerUser(Model model) {
        model.addAttribute("user",new User());
        return "signup";
    }

}
