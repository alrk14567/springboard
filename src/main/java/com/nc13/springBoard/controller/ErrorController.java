package com.nc13.springBoard.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ErrorController {
    @GetMapping("/showMessage")
    public String showError(@ModelAttribute("message") String message, Model model) {

        model.addAttribute("message", message);

        return "showMessage";
    }

}
