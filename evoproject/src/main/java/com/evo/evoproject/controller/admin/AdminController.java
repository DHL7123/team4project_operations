package com.evo.evoproject.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/admin/manageOrder")
    public String manageOrder() {
        return "admin/manageOrder";
    }

    @GetMapping("/admin/manageBoard")
    public String manageBoard() {
        return "admin/manageBoard";
    }
}