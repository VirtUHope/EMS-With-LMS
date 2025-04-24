package com.virtualHope.EMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile"; // maps to templates/profile.html
    }

    @GetMapping("/index")
    public String showIndexPage() {
        return "index"; // maps to templates/profile.html
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin"; // maps to templates/profile.html
    }

}
